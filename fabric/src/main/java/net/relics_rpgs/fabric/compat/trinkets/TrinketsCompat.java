package net.relics_rpgs.fabric.compat.trinkets;

import net.fabricmc.loader.api.FabricLoader;

public class TrinketsCompat {
    public static void init() {
        var loader = FabricLoader.getInstance();
        // Trinkets Updated 4.0's mod id is `trinkets_updated`, but it declares `provides: trinkets`,
        // so the legacy id keeps matching. Check both to survive either spelling.
        if (loader.isModLoaded("trinkets") || loader.isModLoaded("trinkets_updated")) {
            // Outsource to avoid class loading issues
            TrinketsHelper.registerFactory();
        }
    }
}