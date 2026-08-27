package net.relics_rpgs.fabric.compat.trinkets;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.callback.TrinketCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;

/// Relic item worn in a Trinkets slot.
///
/// Trinkets Updated 4.0 dropped the `TrinketItem` base class: per-item behaviour is a
/// {@link TrinketCallback}, resolved by Trinkets via `item instanceof TrinketCallback`
/// (`TrinketCallback.getCallback`). Slot compatibility stays data-driven — Spell Engine's
/// built-in `trinkets_compat` pack tags `#spell_engine:spell_trinket` (which contains
/// `#relics_rpgs:all`) into `trinkets:spell/trinket` and `trinkets:charm/trinket` — so no
/// `TrinketEquippable` component is needed here.
public class RelicTrinketItem extends Item implements TrinketCallback {
    private ItemAttributeModifiers customAttributes = ItemAttributeModifiers.builder().build();

    public RelicTrinketItem(Properties settings, @Nullable ItemAttributeModifiers customAttributes) {
        super(settings);
        if (customAttributes != null) {
            this.customAttributes = customAttributes;
        }
    }

    /// Successor of Trinkets 3.x `TrinketItem#getModifiers`. `slotIdentifier` is
    /// `SlotAttributes.getIdentifier(slot)`, already unique per equipped slot
    /// (`<group>/<slot><index>`), so bonuses stack across slots. Tie the id to the item as well so
    /// quickly swapping a different item within the same slot doesn't reuse an id and trip vanilla's
    /// "Modifier is already applied" guard.
    @Override
    public void forEachTrinketModifier(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity,
                                       Identifier slotIdentifier,
                                       BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        TrinketCallback.super.forEachTrinketModifier(stack, slot, entity, slotIdentifier, consumer);
        var modifierId = slotIdentifier.withSuffix("/" + BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath());
        for (var entry : this.customAttributes.modifiers()) {
            consumer.accept(entry.attribute(),
                    new AttributeModifier(modifierId, entry.modifier().amount(), entry.modifier().operation()));
        }
    }

    public void setConfigurableModifiers(ItemAttributeModifiers component) {
        this.customAttributes = component;
    }

    /// Right-click equips into the first free matching slot — the old `TrinketItem#use` behaviour
    /// (Trinkets Updated routes `Item#use` through this when it returns `true`).
    @Override
    public boolean canEquipFromUse(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
        var isOnCooldown = false;
        if (entity instanceof Player player) {
            isOnCooldown = !player.isCreative() && player.getCooldowns().isOnCooldown(stack);
        }
        return TrinketCallback.super.canUnequip(stack, slot, entity) && !isOnCooldown;
    }

    // Equip sound: Trinkets Updated's default `getEquipSound` falls back to
    // `SoundEvents.ARMOR_EQUIP_GENERIC`, which is exactly what `RelicCurioItem#onEquip` plays on
    // NeoForge — so the two loaders now match without any code here.
}
