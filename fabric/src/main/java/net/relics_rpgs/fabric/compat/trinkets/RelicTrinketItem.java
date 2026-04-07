package net.relics_rpgs.fabric.compat.trinkets;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import com.google.common.collect.ArrayListMultimap;

public class RelicTrinketItem extends TrinketItem {
    private AttributeModifiersComponent customAttributes = AttributeModifiersComponent.builder().build();

    public RelicTrinketItem(Settings settings, @Nullable AttributeModifiersComponent customAttributes) {
        super(settings);
        if (customAttributes != null) {
            this.customAttributes = customAttributes;
        }
    }

    public Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, Identifier slotIdentifier) {
        var defaultModifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> mutableModifiers = ArrayListMultimap.create(defaultModifiers);
        String itemName = net.minecraft.registry.Registries.ITEM.getId(stack.getItem()).getPath();

        for (var entry : this.customAttributes.modifiers()) {
            Identifier uniqueModId = Identifier.of(slotIdentifier.getNamespace(),
                    slotIdentifier.getPath() + "_" + itemName + "_" + entry.modifier().id().getPath());
            mutableModifiers.put(entry.attribute(),
                    new EntityAttributeModifier(uniqueModId, entry.modifier().value(), entry.modifier().operation()));
        }
        return mutableModifiers;
    }
    public void setConfigurableModifiers(AttributeModifiersComponent component) {
        this.customAttributes = component;
    }

    @Override
    public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        var isOnCooldown = false;
        if (entity instanceof PlayerEntity player) {
            isOnCooldown = !player.isCreative() && player.getItemCooldownManager().isCoolingDown(stack.getItem());
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