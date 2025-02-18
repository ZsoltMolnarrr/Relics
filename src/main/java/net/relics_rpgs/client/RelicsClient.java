package net.relics_rpgs.client;

import net.fabricmc.api.ClientModInitializer;
import net.relics_rpgs.spell.RelicEffects;
import net.relics_rpgs.spell.RelicSpells;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.api.render.StunParticleSpawner;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.fx.SpellEngineParticles;

import java.util.List;

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
                new BuffParticleSpawner("spell_engine:magic_rage_spark_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_ATTACKS_SPEED.effect,
                new BuffParticleSpawner("spell_engine:magic_frost_spark_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_RANGED_DAMAGE.effect,
                new BuffParticleSpawner("spell_engine:magic_nature_spark_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_SPELL_POWER.effect,
                new BuffParticleSpawner("spell_engine:magic_white_spark_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_SPELL_HASTE.effect,
                new BuffParticleSpawner("spell_engine:magic_holy_spark_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_SPELL_CRIT.effect,
                new BuffParticleSpawner("spell_engine:magic_holy_spark_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_POWER_ARCANE_FIRE.effect,
                new BuffParticleSpawner(List.of("spell_engine:magic_arcane_spark_float", "spell_engine:flame_spark"), 0.5F, 0.11F, 0.12F)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_POWER_FROST_HEALING.effect,
                new BuffParticleSpawner(List.of("spell_engine:magic_frost_spark_float", "spell_engine:magic_holy_spark_float"), 0.5F, 0.11F, 0.12F)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.LESSER_PROC_CRIT_DAMAGE.effect,
                new BuffParticleSpawner("spell_engine:magic_white_spark_float", 1)
        );

        CustomParticleStatusEffect.register(
                RelicEffects.MEDIUM_ATTACK_DAMAGE.effect,
                new BuffParticleSpawner("spell_engine:magic_rage_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.MEDIUM_ATTACKS_SPEED.effect,
                new BuffParticleSpawner("spell_engine:magic_frost_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.MEDIUM_RANGED_DAMAGE.effect,
                new BuffParticleSpawner("spell_engine:magic_nature_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.MEDIUM_DEFENSE.effect,
                new BuffParticleSpawner(SpellEngineParticles.shield_small.id().toString(), 0.2F)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.MEDIUM_SPELL_POWER.effect,
                new BuffParticleSpawner("spell_engine:magic_white_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.MEDIUM_SPELL_HASTE.effect,
                new BuffParticleSpawner("spell_engine:magic_holy_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.MEDIUM_ARCANE_POWER.effect,
                new BuffParticleSpawner("spell_engine:magic_arcane_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.MEDIUM_FROST_POWER.effect,
                new BuffParticleSpawner("spell_engine:magic_frost_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.MEDIUM_FIRE_POWER.effect,
                new BuffParticleSpawner(SpellEngineParticles.flame_medium_a.id().toString(), 0.5F)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.MEDIUM_HEALING_POWER.effect,
                new BuffParticleSpawner("spell_engine:magic_holy_stripe_float", 1)
        );

        CustomParticleStatusEffect.register(
                RelicEffects.STUN.effect,
                new StunParticleSpawner()
        );
        CustomParticleStatusEffect.register(
                RelicEffects.GREATER_PHYSICAL_TRANCE.effect,
                new BuffParticleSpawner("spell_engine:magic_rage_stripe_float", 0.5F)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.GREATER_SPELL_TRANCE.effect,
                new BuffParticleSpawner("spell_engine:magic_white_stripe_float", 0.5F)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.GREATER_DEFENSE_ARMOR.effect,
                new BuffParticleSpawner(SpellEngineParticles.shield_small.id().toString(), 0.5F)
        );

        CustomParticleStatusEffect.register(
                RelicEffects.SUPERIOR_ATTACK_DAMAGE.effect,
                new BuffParticleSpawner("spell_engine:magic_holy_spell_float", 0.5F)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.SUPERIOR_SPELL_POWER.effect,
                new BuffParticleSpawner("spell_engine:magic_white_spell_float", 0.5F)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.SUPERIOR_HEALING_TAKEN.effect,
                new BuffParticleSpawner("spell_engine:magic_frost_spell_float", 0.5F)
        );
    }
}
