package net.relics_rpgs.client;

import net.fabricmc.api.ClientModInitializer;
import net.relics_rpgs.spell.RelicEffects;
import net.relics_rpgs.spell.RelicSpells;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.api.render.StunParticleSpawner;
import net.spell_engine.client.gui.SpellTooltip;

public class RelicsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (var entry: RelicSpells.entries) {
            if (entry.mutator() != null) {
                SpellTooltip.addDescriptionMutator(entry.id(), entry.mutator());
            }
        }

        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_ATTACK_DAMAGE.effect,
                new BuffParticleSpawner("spell_engine:magic_rage_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_ATTACKS_SPEED.effect,
                new BuffParticleSpawner("spell_engine:magic_frost_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_RANGED_DAMAGE.effect,
                new BuffParticleSpawner("spell_engine:magic_nature_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_SPELL_POWER.effect,
                new BuffParticleSpawner("spell_engine:magic_white_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_SPELL_HASTE.effect,
                new BuffParticleSpawner("spell_engine:magic_holy_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_SPELL_CRIT.effect,
                new BuffParticleSpawner("spell_engine:magic_holy_stripe_float", 1)
        );

        CustomParticleStatusEffect.register(
                RelicEffects.STUN.effect,
                new StunParticleSpawner()
        );

        CustomParticleStatusEffect.register(
                RelicEffects.GREATER_PHYSICAL_TRANCE.effect,
                new BuffParticleSpawner("spell_engine:magic_rage_stripe_float", 1, 0.1F, 0.15F)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.GREATER_SPELL_TRANCE.effect,
                new BuffParticleSpawner("spell_engine:magic_frost_spark_float", 1, 0.1F, 0.15F)
        );
    }
}
