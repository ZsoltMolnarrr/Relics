package net.relics_rpgs.client;

import net.fabricmc.api.ClientModInitializer;
import net.relics_rpgs.spell.RelicSpells;
import net.spell_engine.client.gui.SpellTooltip;

public class RelicsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (var entry: RelicSpells.entries) {
            if (entry.mutator() != null) {
                SpellTooltip.addDescriptionMutator(entry.id(), entry.mutator());
            }
        }
    }
}
