package net.relics_rpgs.spell;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.relics_rpgs.RelicsMod;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.internals.SpellExecution;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;

public class RelicMechanics {
    public static final Identifier SHIELD_RESET = Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE, "shield_reset");

    public static void init() {
        SpellHandlers.registerCustomImpact(SHIELD_RESET, new SpellHandlers.CustomImpact() {
            @Override
            public SpellHandlers.ImpactResult onSpellImpact(Holder<Spell> registryEntry, SpellPower.Result result, LivingEntity caster, @Nullable Entity entity, SpellExecution.ImpactContext impactContext) {
                if (caster instanceof Player player) {
                    var success = tryResetShield(player, player.getMainHandItem()) || tryResetShield(player, player.getOffhandItem());
                    return new SpellHandlers.ImpactResult(success, false);
                }
                return new SpellHandlers.ImpactResult(false, false);
            }

            private boolean tryResetShield(Player player, ItemStack itemStack) {
                // 1.21.11: shields are component driven (`minecraft:blocks_attacks`), so `ShieldItem` is
                // no longer the marker - Paladins' kite shields for example are plain `Item`s.
                // Disabling puts the stack's *cooldown group* on cooldown (`BlocksAttacksComponent#applyShieldCooldown`).
                if (itemStack != null && itemStack.get(DataComponents.BLOCKS_ATTACKS) != null
                        && player.getCooldowns().isOnCooldown(itemStack)) {
                    player.getCooldowns().removeCooldown(player.getCooldowns().getCooldownGroup(itemStack));
                    return true;
                }
                return false;
            }
        });

    }
}
