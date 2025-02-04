package net.relics_rpgs.item;

import net.fabricmc.loader.api.FabricLoader;

public class ItemCompat {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            RelicItems.factory = args -> new RelicTrinketItem(args.settings(), args.attributes());
        }
    }
}
