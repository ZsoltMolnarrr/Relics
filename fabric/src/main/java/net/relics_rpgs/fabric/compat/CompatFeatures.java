package net.relics_rpgs.fabric.compat;

import net.relics_rpgs.fabric.compat.trinkets.TrinketsCompat;
import net.relics_rpgs.compat.AccessoriesCompat;

public class CompatFeatures {
    public static void init() {
        AccessoriesCompat.init();
        TrinketsCompat.init();
    }
}
