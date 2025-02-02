package net.relics_rpgs.item;

import com.google.common.base.Suppliers;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.relics_rpgs.RelicsMod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RelicsItems {
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public record Config() {

    }

    public record Entry(String name, String translatedName, Supplier<Item> item) {
        public Entry(String name, String translatedName) {
            this(name, translatedName,
                    Suppliers.memoize(() ->
                            new Item(new Item.Settings()
                                    .maxCount(1)
                            )
                    )
            );
        }

        public Identifier id() {
            return Identifier.of(RelicsMod.NAMESPACE, name);
        }
    }

    public static final Entry EXAMPLE = add(new Entry("example", "Example"));
    public static final Entry EXAMPLE_2 = add(new Entry("example_2", "Example 2"));

    public static void register() {
        for(var entry: entries) {
            Registry.register(Registries.ITEM, entry.id(), entry.item().get());
        }
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content -> {
            for(var entry: entries) {
                content.add(entry.item().get());
            }
        });
    }
}
