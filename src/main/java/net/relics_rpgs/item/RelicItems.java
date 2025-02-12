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
import net.minecraft.util.Rarity;
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
        private final int tier;
        private final String name;
        private final String translatedName;
        private ItemConfig.Entry config;
        public ItemConfig.Entry defaults;
        private final Supplier<Item> item;
        private SpellContainer spellContainer;

        public Entry(int tier, String name, String translatedName) {
            this(tier, name, translatedName, ItemConfig.Entry.EMPTY);
        }

        public Entry(int tier, String name, String translatedName, ItemConfig.Entry config) {
            this.tier = tier;
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

                var rarity = rarityFrom(tier);
                if (rarity != Rarity.COMMON) {
                    settings = settings.rarity(rarity);
                }
                return getFactory().apply(new ItemArgs(settings, attributes));
            });
        }

        private static Rarity rarityFrom(int tier) {
            return switch (tier) {
                case 0, 1 -> Rarity.COMMON;
                case 2 -> Rarity.UNCOMMON;
                case 3 -> Rarity.RARE;
                default -> Rarity.EPIC;
            };
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

    public static final Entry JEWEL_FIGURINE_RUBY = add(new Entry(1, "jewel_figurine_ruby", "Ruby Serpent Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(), tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_TOPAZ = add(new Entry(1, "jewel_figurine_topaz", "Topaz Fox Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.ARCANE.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(SpellSchools.FIRE.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_CITRINE = add(new Entry(1, "jewel_figurine_citrine", "Citrine Cat Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.HEALING.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(SpellSchools.LIGHTNING.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_JADE = add(new Entry(1, "jewel_figurine_jade", "Jade Hawk Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes_RangedWeapon.DAMAGE.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_SAPPHIRE = add(new Entry(1, "jewel_figurine_sapphire", "Sapphire Turtle Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString(), 2, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry JEWEL_FIGURINE_TANZANITE = add(new Entry(1, "jewel_figurine_tanzanite", "Tanzanite Bat Figurine"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(SpellSchools.FROST.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            new AttributeModifier(SpellSchools.SOUL.id, tier_0_multiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    ))
            );

    public static final Entry LESSER_ROLL = add(new Entry(1, "lesser_roll", "Feather Talisman"))
            .config(new ItemConfig.Entry()
                    .withAttributes(List.of(
                            new AttributeModifier(COMBAT_ROLL_COUNT, 1, EntityAttributeModifier.Operation.ADD_VALUE)
                    ))
            );
    public static final Entry LESSER_USE_DAMAGE = add(new Entry(1, "lesser_use_damage", "Meteorite Whetstone"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.lesser_use_damage.id()));
    public static final Entry LESSER_USE_DEX = add(new Entry(1, "lesser_use_dex", "Medal of Valor"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.lesser_use_dex.id()));
    public static final Entry LESSER_USE_RANGED = add(new Entry(1, "lesser_use_ranged", "Eagle Eye"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.lesser_use_ranged.id()));
    public static final Entry LESSER_USE_HEALTH = add(new Entry(1, "lesser_use_health", "Everflowing Vial"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.lesser_use_health.id()));
    public static final Entry LESSER_USE_SPELL_POWER = add(new Entry(1, "lesser_use_spell_power", "Silver Crescent"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.lesser_use_spell_power.id()));
    public static final Entry LESSER_USE_SPELL_HASTE = add(new Entry(1, "lesser_use_spell_haste", "Sorcerer’s Chronograph"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.lesser_use_spell_haste.id()));
    public static final Entry LESSER_PROC_SPELL_CRIT = add(new Entry(1, "lesser_proc_spell_crit", "Scarab of Infinite Mysteries"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.lesser_proc_spell_crit.id()));
    public static final Entry LESSER_PROC_CRIT_DAMAGE = add(new Entry(1, "lesser_proc_crit_damage", "Splintered Focus Crystal"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.lesser_proc_crit_damage.id()));
    public static final Entry LESSER_PROC_ARCANE_FIRE = add(new Entry(1, "lesser_proc_arcane_fire", "Spellfire Stone"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.lesser_proc_arcane_fire.id()));
    public static final Entry LESSER_PROC_FROST_HEALING = add(new Entry(1, "lesser_proc_frost_healing", "Frozen Lotus"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.lesser_proc_frost_healing.id()));

    public static final Entry MEDIUM_PROC_ATTACK_DAMAGE = add(new Entry(2, "medium_proc_attack_damage", "Badge of Tenacity"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_proc_attack_damage.id()));
    public static final Entry MEDIUM_PROC_ATTACK_SPEED = add(new Entry(2, "medium_proc_attack_speed", "Dragon Skull Trophy"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_proc_attack_speed.id()));
    public static final Entry MEDIUM_PROC_RANGED_DAMAGE = add(new Entry(2, "medium_proc_ranged_damage", "Golden Bowstring"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_proc_ranged_damage.id()));
    public static final Entry MEDIUM_PROC_DEFENSE = add(new Entry(2, "medium_proc_defense", "Titanium Nautilus Shell"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_proc_defense.id()));
    public static final Entry MEDIUM_PROC_SPELL_POWER = add(new Entry(2, "medium_proc_spell_power", "Crystal Skull"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_proc_spell_power.id()));
    public static final Entry MEDIUM_PROC_SPELL_HASTE = add(new Entry(2, "medium_proc_spell_haste", "Hourglass of the Unraveller"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_proc_spell_haste.id()));
    public static final Entry MEDIUM_USE_ARCANE_POWER = add(new Entry(2, "medium_proc_arcane_power", "Arcane Orb"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_use_arcane_power.id()));
    public static final Entry MEDIUM_USE_FIRE_POWER = add(new Entry(2, "medium_proc_fire_power", "Fire Orb"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_use_fire_power.id()));
    public static final Entry MEDIUM_USE_FROST_POWER = add(new Entry(2, "medium_proc_frost_power", "Frost Orb"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_use_frost_power.id()));
    public static final Entry MEDIUM_USE_HEALING_POWER = add(new Entry(2, "medium_proc_healing_power", "Holy Orb"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_use_healing_power.id()));
//    public static final Entry MEDIUM_USE_LIGHTNING_POWER = add(new Entry(2, "medium_proc_lightning_power", "Lightning Orb"))
//            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_use_lightning_power.id()));
//    public static final Entry MEDIUM_USE_SOUL_POWER = add(new Entry(2, "medium_proc_soul_power", "Soul Orb"))
//            .spell(SpellContainerHelper.createForRelic(RelicSpells.medium_use_soul_power.id()));

    public static final Entry GREATER_PERK_ROLL_DAMAGE = add(new Entry(3, "greater_perk_roll_damage", "Thunderbird Feather"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.greater_perk_roll_damage.id()));
    public static final Entry GREATER_PERK_MELEE_STUN = add(new Entry(3, "greater_perk_melee_stun", "Blackjack"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.greater_perk_melee_stun.id()));
    public static final Entry GREATER_PERK_RANGED_LEVITATE = add(new Entry(3, "greater_perk_ranged_levitate", "Updraft Arrow"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.greater_perk_ranged_levitate.id()));
    public static final Entry GREATER_PERK_SPELL_STUN = add(new Entry(3, "greater_perk_spell_stun", "Malevolent Gaze"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.greater_perk_spell_stun.id()));
    public static final Entry GREATER_PERK_DEFENSE_BLOCK = add(new Entry(3, "greater_perk_defense_block", "Sacred Wardstone"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.greater_perk_defense_block.id()));
    public static final Entry GREATER_PERK_HEAL_CLEANSE = add(new Entry(3, "greater_perk_heal_cleanse", "Holy Water"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.greater_perk_heal_cleanse.id()));

    public static final Entry GREATER_PROC_PHYSICAL_TRANCE = add(new Entry(3, "greater_proc_physical_trance", "Sharpened Dragon Scale"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.greater_proc_physical_trance.id()));
    public static final Entry GREATER_PROC_SPELL_TRANCE = add(new Entry(3, "greater_proc_spell_trance", "Twisted Dragon Scale"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.greater_proc_spell_trance.id()));
    public static final Entry GREATER_PERK_HEAL_DANGER = add(new Entry(3, "greater_perk_heal_danger", "Verdant Dragon Scale"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.greater_perk_heal_danger.id()));
    public static final Entry GREATER_PROC_DEFENSE_DANGER = add(new Entry(3, "greater_proc_defense_danger", "Petrified Dragon Scale"))
            .spell(SpellContainerHelper.createForRelic(RelicSpells.greater_proc_defense_danger.id()));

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
