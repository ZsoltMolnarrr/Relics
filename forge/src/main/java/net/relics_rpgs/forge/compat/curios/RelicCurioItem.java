package net.relics_rpgs.forge.compat.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvents;
import net.relics_rpgs.compat.RelicModifierIds;
import net.spell_engine.api.item.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class RelicCurioItem extends Item implements ICurioItem {
    private ItemAttributeModifiers customAttributes = ItemAttributeModifiers.builder().build();

    public RelicCurioItem(Item.Settings settings, @Nullable ItemAttributeModifiers customAttributes) {
        super(settings);
        if (customAttributes != null) {
            this.customAttributes = customAttributes;
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        // Defensive copy: the default implementation's mutability is not part of the API contract.
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = LinkedHashMultimap.create();
        modifiers.putAll(ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack));
        // Curios hands out a slot-unique `uuid`, so bonuses already stack across slots. Fold the item id
        // into it as well so quickly swapping a different item within the same slot doesn't reuse a UUID
        // and trip vanilla's "Modifier is already applied" guard.
        var itemPath = Registries.ITEM.getId(stack.getItem()).getPath();
        var modifierUuid = RelicModifierIds.perSlotAndItem(uuid, itemPath);
        var modifierName = RelicModifierIds.name(itemPath);
        for (var entry : this.customAttributes.modifiers()) {
            modifiers.put(entry.attribute().value(),
                    new EntityAttributeModifier(modifierUuid, modifierName,
                            entry.modifier().getValue(), entry.modifier().getOperation()));
        }
        return modifiers;
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        var isOnCooldown = false;
        if (slotContext.entity() instanceof PlayerEntity player) {
            isOnCooldown = !player.isCreative() && player.getItemCooldownManager().isCoolingDown(stack.getItem());
        }
        return ICurioItem.super.canUnequip(slotContext, stack) && !isOnCooldown;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        // Relics have no sound of their own, so this is the generic equip sound Curios would
        // play on use. Doing it here covers dragging into the slot as well, which is silent otherwise.
        var entity = slotContext.entity();
        if (entity == null) {
            return;
        }
        var world = entity.getWorld();
        if (world.isClient()                        // the server broadcast below reaches every nearby client
                || entity.age <= 100                // gear already worn when entering a world/dimension
                || prevStack.isOf(stack.getItem())) // same item, only its data changed
        {
            return;
        }
        world.playSound(null, entity.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
                entity.getSoundCategory(), 1.0F, 1.0F);
    }

    @Override
    public void onEquipFromUse(SlotContext slotContext, ItemStack stack) {
        // Silent on purpose: `onEquip` above already fires for every equip path, this one included.
    }
}
