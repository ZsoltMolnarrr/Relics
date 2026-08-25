package net.relics_rpgs.neoforge.compat.curios;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class RelicCurioItem extends Item implements ICurioItem {
    private AttributeModifiersComponent customAttributes = AttributeModifiersComponent.builder().build();

    public RelicCurioItem(Item.Settings settings, @Nullable AttributeModifiersComponent customAttributes) {
        super(settings);
        if (customAttributes != null) {
            this.customAttributes = customAttributes;
        }
    }

    @Override
    public Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getAttributeModifiers(SlotContext slotContext, Identifier id, ItemStack stack) {
        // Defensive copy: the default implementation's mutability is not part of the API contract.
        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifiers = LinkedHashMultimap.create();
        modifiers.putAll(ICurioItem.super.getAttributeModifiers(slotContext, id, stack));
        // `id` is already unique per equipped slot, so bonuses stack across slots. Tie the id
        // to the item as well so quickly swapping a different item within the same slot doesn't
        // reuse an id and trip vanilla's "Modifier is already applied" guard.
        var modifierId = id.withSuffixedPath("/" + Registries.ITEM.getId(stack.getItem()).getPath());
        for (var entry : this.customAttributes.modifiers()) {
            modifiers.put(entry.attribute(),
                    new EntityAttributeModifier(modifierId, entry.modifier().value(), entry.modifier().operation()));
        }
        return modifiers;
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        var isOnCooldown = false;
        if (slotContext.entity() instanceof PlayerEntity player) {
            isOnCooldown = !player.isCreative() && player.getItemCooldownManager().isCoolingDown(stack);
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
        var world = entity.getEntityWorld();
        if (world.isClient()                        // the server broadcast below reaches every nearby client
                || entity.age <= 100                // gear already worn when entering a world/dimension
                || prevStack.isOf(stack.getItem())) // same item, only its data changed
        {
            return;
        }
        world.playSound(null, entity.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value(),
                entity.getSoundCategory(), 1.0F, 1.0F);
    }

    @Override
    public void onEquipFromUse(SlotContext slotContext, ItemStack stack) {
        // Silent on purpose: `onEquip` above already fires for every equip path, this one included.
    }
}
