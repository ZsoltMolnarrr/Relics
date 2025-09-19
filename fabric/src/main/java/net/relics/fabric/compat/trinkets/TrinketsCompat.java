package net.relics.fabric.compat.trinkets;

import net.fabricmc.loader.api.FabricLoader;
import net.relics_rpgs.item.RelicFactory;

public class TrinketsCompat {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            RelicFactory.factory = args -> new RelicTrinketItem(args.settings(), args.attributes());
        }
    }
}