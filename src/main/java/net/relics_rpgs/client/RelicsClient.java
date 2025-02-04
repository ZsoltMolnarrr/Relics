package net.relics_rpgs.client;

import net.fabricmc.api.ClientModInitializer;
import net.relics_rpgs.spell.RelicEffects;
import net.relics_rpgs.spell.RelicSpells;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.BuffParticleSpawner;
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
                RelicEffects.METEORITE_WHETSTONE.effect,
                new BuffParticleSpawner("spell_engine:magic_rage_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.MEDAL_OF_VALOR.effect,
                new BuffParticleSpawner("spell_engine:magic_frost_stripe_float", 1)
        );
        CustomParticleStatusEffect.register(
                RelicEffects.EAGLE_EYE.effect,
                new BuffParticleSpawner("spell_engine:magic_nature_stripe_float", 1)
        );
    }
}
