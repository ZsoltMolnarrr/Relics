package net.relics_rpgs.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.relics_rpgs.RelicsMod;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.internals.SpellExecution;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;

public class RelicMechanics {
    public static final Identifier SHIELD_RESET = Identifier.of(RelicsMod.NAMESPACE, "shield_reset");

    public static void init() {
        SpellHandlers.registerCustomImpact(SHIELD_RESET, new SpellHandlers.CustomImpact() {
            @Override
            public SpellHandlers.ImpactResult onSpellImpact(RegistryEntry<Spell> registryEntry, SpellPower.Result result, LivingEntity caster, @Nullable Entity entity, SpellExecution.ImpactContext impactContext) {
                if (caster instanceof PlayerEntity player) {
                    var success = tryResetShield(player, player.getMainHandStack()) || tryResetShield(player, player.getOffHandStack());
                    return new SpellHandlers.ImpactResult(success, false);
                }
                return new SpellHandlers.ImpactResult(false, false);
            }

            private boolean tryResetShield(PlayerEntity player, ItemStack itemStack) {
                // 1.21.11: shields are component driven (`minecraft:blocks_attacks`), so `ShieldItem` is
                // no longer the marker - Paladins' kite shields for example are plain `Item`s.
                // Disabling puts the stack's *cooldown group* on cooldown (`BlocksAttacksComponent#applyShieldCooldown`).
                if (itemStack != null && itemStack.get(DataComponentTypes.BLOCKS_ATTACKS) != null
                        && player.getItemCooldownManager().isCoolingDown(itemStack)) {
                    player.getItemCooldownManager().remove(player.getItemCooldownManager().getGroup(itemStack));
                    return true;
                }
                return false;
            }
        });

    }
}
