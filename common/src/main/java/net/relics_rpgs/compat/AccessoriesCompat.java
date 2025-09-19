package net.relics_rpgs.compat;

import io.wispforest.accessories.api.components.AccessoriesDataComponents;
import io.wispforest.accessories.api.components.AccessoryItemAttributeModifiers;
import net.fabricmc.loader.api.FabricLoader;
import net.relics_rpgs.item.RelicFactory;

public class AccessoriesCompat {
    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("accessories")) {
            RelicFactory.factory = args -> {
                var settings = args.settings();
                if (args.attributes() != null) {
                    var builder = AccessoryItemAttributeModifiers.builder();
                    for (var entry : args.attributes().modifiers()) {
                        builder = builder.addForSlot(entry.attribute(), entry.modifier(), "spell_trinket", true);
                    }
                    settings = settings.component(AccessoriesDataComponents.ATTRIBUTES, builder.build());
                }
                return new RelicAccessoriesItem(settings);
            };
        }
    }
}