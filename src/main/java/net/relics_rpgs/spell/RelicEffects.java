package net.relics_rpgs.spell;

import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.relics_rpgs.RelicsMod;
import net.relics_rpgs.config.AttributeModifier;
import net.relics_rpgs.config.EffectConfig;
import net.relics_rpgs.util.AttributesUtil;
import net.spell_engine.api.effect.Synchronized;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

public class RelicEffects {
    public static final List<Entry> entries = new ArrayList<>();
    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static final class Entry {
        private final String name;
        public final String title;
        public final String description;
        public final StatusEffect effect;
        public final EffectConfig.Entry defaults;
        public EffectConfig.Entry config;
        public RegistryEntry<StatusEffect> entry;

        public Entry(String name, String title, String description, StatusEffect effect, EffectConfig.Entry config) {
            this.name = name;
            this.title = title;
            this.description = description;
            this.effect = effect;
            this.defaults = config;
            this.config = config;
        }

        public Identifier id() {
            return Identifier.of(RelicsMod.NAMESPACE, name);
        }

        public EffectConfig.Entry config() {
            return config;
        }
    }

    private static final float T1_BUFF_MULTIPLIER = 0.1F;
    private static final float T2_BUFF_MULTIPLIER = 0.2F;

    public static final Entry LESSER_ATTACK_DAMAGE = add(new Entry("lesser_attack_damage",
            "Sharpness",
            "Increases attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    T1_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static final Entry LESSER_ATTACKS_SPEED = add(new Entry("lesser_attack_speed",
            "Valor",
            "Increases attack speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x008800),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString(),
                                    T1_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.HASTE.id.toString(),
                                    T1_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static final Entry LESSER_RANGED_DAMAGE = add(new Entry("lesser_ranged_damage",
            "Eagle Eye",
            "Increases ranged attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x000088),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.DAMAGE.id,
                                    T1_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Entry LESSER_SPELL_POWER = add(new Entry("lesser_spell_power",
            "Spell Power",
            "Increases spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig.Entry(
                    SpellSchools.all().stream()
                            .filter(school -> school.archetype == SpellSchool.Archetype.MAGIC
                                    && !school.id.toString().contains("generic"))
                            .map(school ->
                                    new AttributeModifier(
                                            school.id,
                                            T1_BUFF_MULTIPLIER,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Entry LESSER_SPELL_HASTE = add(new Entry("lesser_spell_haste",
            "Spell Haste",
            "Increases spell haste.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x880088),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.HASTE.id,
                                    T1_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Entry LESSER_SPELL_CRIT = add(new Entry("lesser_spell_crit_chance",
            "Volatility",
            "Increases spell critical strike chance.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888888),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_CHANCE.id,
                                    0.15F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Entry LESSER_POWER_ARCANE_FIRE = add(new Entry("lesser_arcane_fire",
            "Spell Power",
            "Increases spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig.Entry(
                    List.of(SpellSchools.ARCANE, SpellSchools.FIRE).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id,
                                            0.15F,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Entry LESSER_POWER_FROST_HEALING = add(new Entry("lesser_frost_healing",
            "Spell Power",
            "Increases spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig.Entry(
                    List.of(SpellSchools.FROST, SpellSchools.HEALING).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id,
                                            0.15F,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Entry LESSER_PROC_CRIT_DAMAGE = add(new Entry("lesser_spell_crit_damage",
            "Amplify Spell",
            "Increases spell critical damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_DAMAGE.id,
                                    0.5F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Entry MEDIUM_ATTACK_DAMAGE = add(new Entry("medium_attack_damage",
            "Strength",
            "Increases attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    T2_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Entry MEDIUM_ATTACKS_SPEED = add(new Entry("medium_attack_speed",
            "Tempo",
            "Increases attack speed.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x008800),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString(),
                                    T2_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.HASTE.id.toString(),
                                    T2_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Entry MEDIUM_RANGED_DAMAGE = add(new Entry("medium_ranged_damage",
            "Power",
            "Increases ranged attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x000088),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.DAMAGE.id,
                                    T2_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Entry MEDIUM_DEFENSE = add(new Entry("medium_defense",
            "Toughness",
            "Increases armor toughness.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888888),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ARMOR_TOUGHNESS.getIdAsString(),
                                    2F,
                                    EntityAttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Entry MEDIUM_SPELL_POWER = add(new Entry("medium_spell_power",
            "Spell Power",
            "Increases spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig.Entry(
                    SpellSchools.all().stream()
                            .filter(school -> school.archetype == SpellSchool.Archetype.MAGIC
                                    && !school.id.toString().contains("generic"))
                            .map(school ->
                                    new AttributeModifier(
                                            school.id,
                                            T2_BUFF_MULTIPLIER,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Entry MEDIUM_SPELL_HASTE = add(new Entry("medium_spell_haste",
            "Spell Haste",
            "Increases spell haste.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x880088),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.HASTE.id,
                                    T2_BUFF_MULTIPLIER,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Entry MEDIUM_ARCANE_POWER = add(new Entry("medium_arcane_power",
            "Arcane Power",
            "Increases spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig.Entry(
                    List.of(SpellSchools.ARCANE).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id,
                                            T2_BUFF_MULTIPLIER,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Entry MEDIUM_FIRE_POWER = add(new Entry("medium_fire_power",
            "Fire Power",
            "Increases spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig.Entry(
                    List.of(SpellSchools.FIRE).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id,
                                            T2_BUFF_MULTIPLIER,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Entry MEDIUM_FROST_POWER = add(new Entry("medium_frost_power",
            "Frost Power",
            "Increases spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig.Entry(
                    List.of(SpellSchools.FROST).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id,
                                            T2_BUFF_MULTIPLIER,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Entry MEDIUM_HEALING_POWER = add(new Entry("medium_healing_power",
            "Healing Power",
            "Increases spell power.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig.Entry(
                    List.of(SpellSchools.HEALING).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id,
                                            T2_BUFF_MULTIPLIER,
                                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));


    public static void register(EffectConfig config) {
        for (var entry: entries) {
            var key = entry.id().toString();
            var current = config.entries.get(key);
            if (current != null) {
                entry.config = current;
            } else {
                config.entries.put(key, entry.config);
            }

            var modifiers = AttributesUtil.modifiersFrom(entry.id(), entry.config.attributes());
            for (var modifier : modifiers) {
                entry.effect
                        .addAttributeModifier(modifier.attribute(),
                                modifier.modifier().id(),
                                modifier.modifier().value(),
                                modifier.modifier().operation());
            }
        }

        for (var entry: entries) {
            Synchronized.configure(entry.effect, true);
            entry.entry = Registry.registerReference(Registries.STATUS_EFFECT, entry.id(), entry.effect);
        }
    }
}
