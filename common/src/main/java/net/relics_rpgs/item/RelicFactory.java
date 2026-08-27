package net.relics_rpgs.item;

import org.jspecify.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class RelicFactory {
    public record ItemArgs(Item.Properties settings, @Nullable ItemAttributeModifiers attributes) { }
    public static Function<ItemArgs, Item> factory = args -> {
        var settings = args.settings;
        if (args.attributes != null) {
            settings.attributes(args.attributes);
        }
        return new Item(settings);
    };
    public static Function<ItemArgs, Item> getFactory() { return factory; }
}
