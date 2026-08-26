package net.relics_rpgs.fabric.compat.trinkets;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;

public class RelicTrinketItem extends TrinketItem {
    private ItemAttributeModifiers customAttributes = ItemAttributeModifiers.builder().build();

    public RelicTrinketItem(Properties settings, @Nullable ItemAttributeModifiers customAttributes) {
        super(settings);
        if (customAttributes != null) {
            this.customAttributes = customAttributes;
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, Identifier slotIdentifier) {
        var modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
        // `slotIdentifier` is already unique per equipped slot (…/<slot>/<index>), so bonuses
        // stack across slots. Tie the id to the item as well so quickly swapping a different
        // item within the same slot doesn't reuse an id and trip vanilla's "Modifier is already
        // applied" guard.
        var modifierId = slotIdentifier.withSuffix("/" + BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath());
        for (var entry : this.customAttributes.modifiers()) {
            modifiers.put(entry.attribute(),
                    new AttributeModifier(modifierId, entry.modifier().amount(), entry.modifier().operation()));
        }
        return modifiers;
    }

    public void setConfigurableModifiers(ItemAttributeModifiers component) {
        this.customAttributes = component;
    }

    @Override
    public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var isOnCooldown = false;
        if (entity instanceof Player player) {
            isOnCooldown = !player.isCreative() && player.getCooldowns().isOnCooldown(stack);
        }
        return super.canUnequip(stack, slot, entity) && !isOnCooldown;
    }

//    @Override
//    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
//        super.onEquip(stack, slot, entity);
//
//        if (entity.getWorld().isClient() // Play sound only on client
//                && entity.age > 100      // Avoid playing sound on entering world / dimension
//        ) {
//            entity.playSound(SoundHelper.JEWELRY_EQUIP, 1.0F, 1.0F);
//        }
//    }
}