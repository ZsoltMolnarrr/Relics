package net.relics_rpgs.item;

import net.fabricmc.loader.api.FabricLoader;

public class ItemCompat {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            RelicsItems.factory = args -> new RelicTrinketItem(args.settings(), args.attributes());
        }
    }
}
