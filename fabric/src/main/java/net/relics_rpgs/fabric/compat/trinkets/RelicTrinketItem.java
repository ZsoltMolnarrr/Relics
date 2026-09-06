package net.relics_rpgs.fabric.compat.trinkets;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.relics_rpgs.compat.RelicModifierIds;
import net.spell_engine.api.item.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RelicTrinketItem extends TrinketItem {
    private ItemAttributeModifiers customAttributes = ItemAttributeModifiers.builder().build();

    public RelicTrinketItem(Settings settings, @Nullable ItemAttributeModifiers customAttributes) {
        super(settings);
        if (customAttributes != null) {
            this.customAttributes = customAttributes;
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // Trinkets hands out a slot-unique `uuid`, so bonuses already stack across slots. Fold the item
        // id into it as well so quickly swapping a different item within the same slot doesn't reuse a
        // UUID and trip vanilla's "Modifier is already applied" guard.
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

    public void setConfigurableModifiers(ItemAttributeModifiers modifiers) {
        this.customAttributes = modifiers;
    }

    @Override
    public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var isOnCooldown = false;
        if (entity instanceof PlayerEntity player) {
            isOnCooldown = !player.isCreative() && player.getItemCooldownManager().isCoolingDown(stack.getItem());
        }
        return super.canUnequip(stack, slot, entity) && !isOnCooldown;
    }
}
