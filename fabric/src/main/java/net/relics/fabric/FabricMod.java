package net.relics.fabric;

import net.fabricmc.api.ModInitializer;
import net.relics.fabric.compat.CompatFeatures;
import net.relics_rpgs.RelicsMod;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CompatFeatures.init();
        RelicsMod.init();
    }
}
