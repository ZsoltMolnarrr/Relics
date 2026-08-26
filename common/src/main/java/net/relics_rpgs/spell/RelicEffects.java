package net.relics_rpgs.spell;

import net.rpg_foundation.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.relics_rpgs.RelicsMod;
import net.relics_rpgs.util.SpellSchoolUtil;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.spell_engine.rpg_series.config.EffectConfig;
import net.spell_engine.api.effect.ActionImpairing;
import net.spell_engine.api.effect.Effects;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.api.effect.Synchronized;
import net.spell_engine.api.entity.SpellEngineAttributes;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

public class RelicEffects {
    public static final List<Effects.Entry> entries = new ArrayList<>();
    private static Effects.Entry add(Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }
    
    private static final float T1_BUFF_MULTIPLIER = 0.1F;
    private static final float T2_BUFF_MULTIPLIER = 0.2F;

    public static final Effects.Entry LESSER_ATTACK_DAMAGE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE, "lesser_attack_damage"),
            "Sharpness",
            "Increases attack damage.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    Attributes.ATTACK_DAMAGE.getRegisteredName(),
                                    T1_BUFF_MULTIPLIER,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static final Effects.Entry LESSER_ATTACKS_SPEED = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"lesser_attack_speed"),
            "Valor",
            "Increases attack speed.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x008800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    Attributes.ATTACK_SPEED.getRegisteredName(),
                                    T1_BUFF_MULTIPLIER,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.HASTE.id.toString(),
                                    T1_BUFF_MULTIPLIER,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static final Effects.Entry LESSER_RANGED_DAMAGE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"lesser_ranged_damage"),
            "Eagle Eye",
            "Increases ranged attack damage.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x000088),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
                                    T1_BUFF_MULTIPLIER,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry LESSER_SPELL_POWER = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"lesser_spell_power"),
            "Spell Power",
            "Increases spell power.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    SpellSchoolUtil.allMagicSchools().stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            T1_BUFF_MULTIPLIER,
                                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry LESSER_SPELL_HASTE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"lesser_spell_haste"),
            "Spell Haste",
            "Increases spell haste.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x880088),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.HASTE.id.toString(),
                                    T1_BUFF_MULTIPLIER,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry LESSER_SPELL_CRIT = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"lesser_spell_crit_chance"),
            "Volatility",
            "Increases spell critical strike chance.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888888),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_CHANCE.id.toString(),
                                    0.15F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry LESSER_POWER_ARCANE_FIRE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"lesser_arcane_fire"),
            "Spell Power",
            "Increases spell power.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(SpellSchools.ARCANE, SpellSchools.FIRE).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            0.15F,
                                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry LESSER_POWER_FROST_HEALING = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"lesser_frost_healing"),
            "Spell Power",
            "Increases spell power.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(SpellSchools.FROST, SpellSchools.HEALING).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            0.15F,
                                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry LESSER_PROC_CRIT_DAMAGE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"lesser_spell_crit_damage"),
            "Amplify Spell",
            "Increases spell critical damage.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_CHANCE.id.toString(),
                                    0.5F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry MEDIUM_ATTACK_DAMAGE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"medium_attack_damage"),
            "Strength",
            "Increases attack damage.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    Attributes.ATTACK_DAMAGE.getRegisteredName(),
                                    T2_BUFF_MULTIPLIER,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry MEDIUM_ATTACKS_SPEED = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"medium_attack_speed"),
            "Tempo",
            "Increases attack speed.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x008800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    Attributes.ATTACK_SPEED.getRegisteredName(),
                                    T2_BUFF_MULTIPLIER,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.HASTE.id.toString(),
                                    T2_BUFF_MULTIPLIER,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry MEDIUM_RANGED_DAMAGE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"medium_ranged_damage"),
            "Power",
            "Increases ranged attack damage.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x000088),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
                                    T2_BUFF_MULTIPLIER,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry MEDIUM_DEFENSE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"medium_defense"),
            "Toughness",
            "Increases armor toughness.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888888),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    Attributes.ARMOR_TOUGHNESS.getRegisteredName(),
                                    4F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));
    public static Effects.Entry MEDIUM_EVASION = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"medium_evasion"),
            "Monkey's Agility",
            "Increases evasion chance.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888888),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.EVASION_CHANCE.id.toString(),
                                    T2_BUFF_MULTIPLIER,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry MEDIUM_SPELL_POWER = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"medium_spell_power"),
            "Spell Power",
            "Increases spell power.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    SpellSchoolUtil.allMagicSchools().stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            T2_BUFF_MULTIPLIER,
                                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry MEDIUM_SPELL_HASTE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"medium_spell_haste"),
            "Spell Haste",
            "Increases spell haste.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x880088),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.HASTE.id.toString(),
                                    T2_BUFF_MULTIPLIER,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));
    public static Effects.Entry MEDIUM_ARCANE_POWER = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"medium_arcane_power"),
            "Arcane Power",
            "Increases spell power.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(SpellSchools.ARCANE).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            T2_BUFF_MULTIPLIER,
                                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry MEDIUM_FIRE_POWER = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"medium_fire_power"),
            "Fire Power",
            "Increases spell power.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(SpellSchools.FIRE).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            T2_BUFF_MULTIPLIER,
                                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry MEDIUM_FROST_POWER = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"medium_frost_power"),
            "Frost Power",
            "Increases spell power.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(SpellSchools.FROST).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            T2_BUFF_MULTIPLIER,
                                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));
    public static Effects.Entry MEDIUM_HEALING_POWER = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"medium_healing_power"),
            "Healing Power",
            "Increases spell power.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(SpellSchools.HEALING).stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            T2_BUFF_MULTIPLIER,
                                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));

    public static Effects.Entry STUN = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"stun"),
            "Stunned",
            "Cannot move or act.",
            new CustomStatusEffect(MobEffectCategory.HARMFUL, 0x888800),
            new EffectConfig(List.of(
                    new AttributeModifier(
                            Attributes.JUMP_STRENGTH.getRegisteredName(),
                            0,
                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            ))
    ));

    public static Effects.Entry GREATER_EVASION_ATTACK = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"greater_evasion_attack"),
            "Evasion",
            "Increases attack damage.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888888),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    Attributes.ATTACK_DAMAGE.getRegisteredName(),
                                    0.5F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry GREATER_PHYSICAL_TRANCE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"greater_physical_trance"),
            "Battle Trance",
            "Increases melee and ranged attack speed.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    Attributes.ATTACK_SPEED.getRegisteredName(),
                                    0.1F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.HASTE.id.toString(),
                                    0.1F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry GREATER_SPELL_TRANCE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"greater_spell_trance"),
            "Spell Trance",
            "Increases spell haste.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.HASTE.id.toString(),
                                    0.1F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry GREATER_DEFENSE_ARMOR = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"greater_defense_armor"),
            "Fortitude",
            "Increases armor.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888888),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    Attributes.ARMOR.getRegisteredName(),
                                    10F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE
                            )
                    )
            )
    ));

    public static Effects.Entry SUPERIOR_ATTACK_DAMAGE = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"superior_attack_damage"),
            "Might",
            "Increases attack damage.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    Attributes.ATTACK_DAMAGE.getRegisteredName(),
                                    0.2F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes_RangedWeapon.DAMAGE.id.toString(),
                                    0.2F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    Attributes.SCALE.getRegisteredName(),
                                    0.2F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry SUPERIOR_SPELL_POWER = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"superior_spell_power"),
            "Spell Power",
            "Increases spell power.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x888800),
            new EffectConfig(
                    SpellSchoolUtil.allOffensiveMagicSchools().stream()
                            .map(school ->
                                    new AttributeModifier(
                                            school.id.toString(),
                                            0.2F,
                                            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                                    )
                            )
                            .toList()
            )
    ));

    public static Effects.Entry SUPERIOR_HEALING_TAKEN = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"superior_healing_taken"),
            "Divinity",
            "Increases healing taken.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x008800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellEngineAttributes.HEALING_TAKEN.id.toString(),
                                    0.5F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry SUPERIOR_DEFENSE_HEALTH = add(new Effects.Entry(Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE,"superior_defense_health"),
            "Vigor",
            "Increases maximum health.",
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0x008800),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    Attributes.MAX_HEALTH.getRegisteredName(),
                                    0.5F,
                                    net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static void register(ConfigFile.Effects config) {
        ActionImpairing.configure(STUN.effect, EntityActionsAllowed.STUN);

        for (var entry: entries) {
            Synchronized.configure(entry.effect, true);
        }

        Effects.register(entries, config.effects);
    }
}
