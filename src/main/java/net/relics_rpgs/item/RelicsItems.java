package net.relics_rpgs.item;

import com.google.common.base.Suppliers;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.relics_rpgs.RelicsMod;
import net.relics_rpgs.config.ItemConfig;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class RelicsItems {
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public record Config(List<ItemConfig.AttributeModifier> attributes) {
        public static Config EMPTY = new Config(List.of());
    }

    public record ItemArgs(Item.Settings settings, @Nullable AttributeModifiersComponent attributes) { }
    public static Function<ItemArgs, Item> factory = args -> {
        var settings = args.settings;
        if (args.attributes != null) {
            settings.attributeModifiers(args.attributes);
        }
        return new Item(settings);
    };
    private static Function<ItemArgs, Item> getFactory() { return factory; }

    public static final class Entry {
        private final String name;
        private final String translatedName;
        private Config config;
        private final Supplier<Item> item;

        public Entry(String name, String translatedName) {
            this(name, translatedName, Config.EMPTY);
        }

        public Entry(String name, String translatedName, Config config) {
            this.name = name;
            this.translatedName = translatedName;
            this.config = config;

            this.item = Suppliers.memoize(() -> {
                var settings = new Item.Settings()
                        .maxCount(1);
                var attributes = (config().attributes() != null && !config().attributes().isEmpty())
                        ? ItemConfig.getBuilder(Identifier.of(RelicsMod.NAMESPACE, name), config().attributes()).build()
                        : null;
                return getFactory().apply(new ItemArgs(settings, attributes));
            });
        }

        public Identifier id() {
            return Identifier.of(RelicsMod.NAMESPACE, name);
        }

        public String name() {
            return name;
        }

        public String translatedName() {
            return translatedName;
        }

        public Config config() {
            return config;
        }

        public Supplier<Item> item() {
            return item;
        }

        public Entry config(Config config) {
            this.config = config;
            return this;
        }
    }

    public static final Entry JEWEL_FIGURINE_RUBY = add(new Entry("jewel_figurine_ruby", "Ruby Serpent Figurine")
            .config(new Config(List.of(
                    new ItemConfig.AttributeModifier("generic.attack_damage", 1, EntityAttributeModifier.Operation.ADD_VALUE)))
            ));
    public static final Entry JEWEL_FIGURINE_TOPAZ = add(new Entry("jewel_figurine_topaz", "Topaz Fox Figurine"));
    public static final Entry JEWEL_FIGURINE_CITRINE = add(new Entry("jewel_figurine_citrine", "Citrine Cat Figurine"));
    public static final Entry JEWEL_FIGURINE_JADE = add(new Entry("jewel_figurine_jade", "Jade Hawk Figurine"));
    public static final Entry JEWEL_FIGURINE_SAPPHIRE = add(new Entry("jewel_figurine_sapphire", "Sapphire Turtle Figurine"));
    public static final Entry JEWEL_FIGURINE_TANZANITE = add(new Entry("jewel_figurine_tanzanite", "Tanzanite Bat Figurine"));

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
