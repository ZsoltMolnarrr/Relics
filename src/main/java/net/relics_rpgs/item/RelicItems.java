package net.relics_rpgs.item;

import com.google.common.base.Suppliers;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.relics_rpgs.RelicsMod;
import net.relics_rpgs.config.AttributeModifier;
import net.relics_rpgs.config.ItemConfig;
import net.relics_rpgs.spell.RelicSpells;
import net.relics_rpgs.util.AttributesUtil;
import net.spell_engine.api.spell.SpellDataComponents;
import net.spell_engine.api.spell.container.SpellContainer;
import net.spell_engine.api.spell.container.SpellContainerHelper;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class RelicItems {
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
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
        private ItemConfig.Entry config;
        public ItemConfig.Entry defaults;
        private final Supplier<Item> item;
        private SpellContainer spellContainer;

        public Entry(String name, String translatedName) {
            this(name, translatedName, ItemConfig.Entry.EMPTY);
        }

        public Entry(String name, String translatedName, ItemConfig.Entry config) {
            this.name = name;
            this.translatedName = translatedName;
            this.config = config;
            this.defaults = config;

            this.item = Suppliers.memoize(() -> {
                var settings = new Item.Settings()
                        .maxCount(1);
                var attributes = (config().attributes != null && !config().attributes.isEmpty())
                        ? AttributesUtil.attributesComponent(Identifier.of(RelicsMod.NAMESPACE, name), config().attributes).build()
                        : null;
                var spellContainer = spellContainer();
                if (spellContainer != null) {
                    settings = settings.component(SpellDataComponents.SPELL_CONTAINER, spellContainer);
                }
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

        public ItemConfig.Entry config() {
            return config;
        }

        public Supplier<Item> item() {
            return item;
        }

        @Nullable public SpellContainer spellContainer() {
            return spellContainer;
        }

        public Entry config(ItemConfig.Entry config) {
            this.config = config;
            return this;
        }

        public Entry spell(SpellContainer spellContainer) {
            this.spellContainer = spellContainer;
            return this;
        }
    }

    public static final String COMBAT_ROLL_COUNT = "combat_roll:count";

    private static final float tier_0_multiplier = 0.05F;

    public static final Entry JEWEL_FIGURINE_RUBY = add(new Entry("jewel_figurine_ruby", "Ruby Serpent Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_TOPAZ = add(new Entry("jewel_figurine_topaz", "Topaz Fox Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.ARCANE.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(SpellSchools.FIRE.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_CITRINE = add(new Entry("jewel_figurine_citrine", "Citrine Cat Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.HEALING.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(SpellSchools.LIGHTNING.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_JADE = add(new Entry("jewel_figurine_jade", "Jade Hawk Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes_RangedWeapon.DAMAGE.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_SAPPHIRE = add(new Entry("jewel_figurine_sapphire", "Sapphire Turtle Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), 2, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_TANZANITE = add(new Entry("jewel_figurine_tanzanite", "Tanzanite Bat Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.FROST.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(SpellSchools.SOUL.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );

    public static final Entry FEATHER_TALISMAN = add(new Entry("feather_talisman", "Feather Talisman"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(COMBAT_ROLL_COUNT, 1, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry METEORITE_WHETSTONE = add(new Entry("meteorite_whetstone", "Meteorite Whetstone"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.meteorite_whetstone.id()));
    public static final Entry MEDAL_OF_VALOR = add(new Entry("medal_of_valor", "Medal of Valor"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.medal_of_valor.id()));

    public static void register(Map<String, ItemConfig.Entry> config) {
        for (var entry : entries) {
            var key = entry.id().toString();
            var configEntry = config.get(key);
            if (configEntry != null) {
                entry.config(configEntry);
            } else {
                config.put(key, entry.config());
            }
        }

        for(var entry: entries) {
            // if (entry.isEnabled()) {
                Registry.register(Registries.ITEM, entry.id(), entry.item().get());
            // }
        }
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content -> {
            for(var entry: entries) {
                //if (entry.isEnabled()) {
                    content.add(entry.item().get());
                // }
            }
        });
    }
}
