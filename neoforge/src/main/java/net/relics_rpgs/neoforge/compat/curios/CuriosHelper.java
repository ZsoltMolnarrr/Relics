package net.relics_rpgs.neoforge.compat.curios;

import net.relics_rpgs.item.RelicFactory;

public class CuriosHelper {
    public static void registerFactory() {
        RelicFactory.factory = args -> new RelicCurioItem(args.settings(), args.attributes());
    }
}
