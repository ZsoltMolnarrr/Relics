package net.relics_rpgs.compat;

import net.spell_engine.Platform;

public class AccessoriesCompat {
    public static void init() {
        if (Platform.util().isModLoaded("accessories")) {
            // Outsource to avoid class loading issues
            AccessoriesHelper.registerFactory();
        }
    }
}