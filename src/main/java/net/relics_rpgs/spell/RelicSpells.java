package net.relics_rpgs.spell;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.relics_rpgs.RelicsMod;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RelicSpells {
    public record Entry(Identifier id, Spell spell, String title, String description, @Nullable SpellTooltip.DescriptionMutator mutator) { }
    public static final List<Entry> entries = new ArrayList<>();
    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static final float T1_USE_EFFECT_DURATION = 10;
    private static final float T1_PROC_EFFECT_DURATION = 6;
    private static final float T1_USE_EFFECT_COOLDOWN = 60;
    private static final float T1_PROC_EFFECT_COOLDOWN = 45;
    private static final float T1_PROC_CHANCE = 0.05F;

    private static final float T2_USE_EFFECT_DURATION = 10;
    private static final float T2_PROC_EFFECT_DURATION = 8;
    private static final float T2_USE_EFFECT_COOLDOWN = 60;
    private static final float T2_PROC_EFFECT_COOLDOWN = 45;
    private static final float T2_PROC_CHANCE = 0.06F;

    private static final float T3_PERK_CC_DURATION = 2;
    private static final float T3_PERK_CC_COOLDOWN = 20;

    private static Spell activeSpellBase() {
        var spell = new Spell();
        spell.range = 0;
        spell.tier = 7;

        spell.type = Spell.Type.ACTIVE;
        spell.active = new Spell.Active();

        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        return spell;
    }

    private static Spell passiveSpellBase() {
        var spell = new Spell();
        spell.range = 0;
        spell.tier = 7;

        spell.type = Spell.Type.PASSIVE;
        spell.passive = new Spell.Passive();

        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        return spell;
    }

    private static Spell.Impact createEffectImpact(Identifier effectId, float t1ProcEffectDuration) {
        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effectId.toString();
        buff.action.status_effect.duration = t1ProcEffectDuration;
        return buff;
    }

    private static void configureCooldown(Spell spell, float duration) {
        if (spell.cost == null) {
            spell.cost = new Spell.Cost();
        }
        if (spell.cost.cooldown == null) {
            spell.cost.cooldown = new Spell.Cost.Cooldown();
        }
        spell.cost.cooldown.duration = duration;
    }


    public static Entry lesser_use_damage = add(lesser_use_damage());
    private static Entry lesser_use_damage() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "lesser_use_damage");
        var description = "Use: Increases attack damage by {buff} for {effect_duration} seconds.";
        var effect = RelicEffects.LESSER_ATTACK_DAMAGE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.SHARPEN.id().toString());

        var buff = createEffectImpact(effect.id(), T1_USE_EFFECT_DURATION);
        spell.impacts = List.of(buff);

        spell.cost = new Spell.Cost();
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = T1_USE_EFFECT_COOLDOWN;

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry lesser_use_dex = add(lesser_use_dex());
    private static Entry lesser_use_dex() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "lesser_use_dex");
        var description = "Use: Increases melee and ranged attack speed by {buff} for {effect_duration} seconds.";
        var effect = RelicEffects.LESSER_ATTACKS_SPEED;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.MEDAL_USE.id().toString());

        spell.impacts = List.of(createEffectImpact(effect.id(), T1_USE_EFFECT_DURATION));
        configureCooldown(spell, T1_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry lesser_use_ranged = add(lesser_use_ranged());
    private static Entry lesser_use_ranged() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "lesser_use_ranged");
        var description = "Use: Increases ranged attack damage by {buff} for {effect_duration} seconds.";
        var effect = RelicEffects.LESSER_RANGED_DAMAGE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.EAGLE_BOOST.id().toString());

        spell.impacts = List.of(createEffectImpact(effect.id(), T1_USE_EFFECT_DURATION));
        configureCooldown(spell, T1_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry lesser_use_health = add(lesser_use_health());
    private static Entry lesser_use_health() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "lesser_use_health");
        var description = "Use: Heals you for {heal_percent} of your max health.";
        var title = "Sip";
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifiedDescription = args.description();
            var spell = args.spellEntry().value();
            var heal = spell.impacts.get(0).action.heal;
            if (heal != null) {
                modifiedDescription = modifiedDescription.replace("{heal_percent}", SpellTooltip.percent(heal.spell_power_coefficient));
            }
            return modifiedDescription;
        };

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;

        // spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.POTION_GENERIC.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_nature_impact_decelerate", ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        null, 10, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        var heal = new Spell.Impact();
        heal.attribute = EntityAttributes.GENERIC_MAX_HEALTH.getIdAsString();
        heal.action = new Spell.Impact.Action();
        heal.action.type = Spell.Impact.Action.Type.HEAL;
        heal.action.heal = new Spell.Impact.Action.Heal();
        heal.action.heal.spell_power_coefficient = 0.2f;

        spell.impacts = List.of(heal);
        configureCooldown(spell, T1_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry lesser_use_spell_power = add(lesser_use_spell_power());
    private static Entry lesser_use_spell_power() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "lesser_use_spell_power");
        var description = "Use: Increases spell power by {buff} for {effect_duration} seconds.";
        var effect = RelicEffects.LESSER_SPELL_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = activeSpellBase();
        spell.school = SpellSchools.ARCANE;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_white_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 15, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T1_USE_EFFECT_DURATION));
        configureCooldown(spell, T1_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry lesser_use_spell_haste = add(lesser_use_spell_haste());
    private static Entry lesser_use_spell_haste() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "lesser_use_spell_haste");
        var description = "Use: Increases spell haste by {buff} for {effect_duration} seconds.";
        var effect = RelicEffects.LESSER_SPELL_HASTE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = activeSpellBase();
        spell.school = SpellSchools.ARCANE;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.HASTE_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_holy_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 15, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T1_USE_EFFECT_DURATION));
        configureCooldown(spell, T1_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry lesser_proc_spell_crit = add(lesser_proc_spell_crit());
    private static Entry lesser_proc_spell_crit() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "lesser_proc_spell_crit");
        var description = "On spell hit: {trigger_chance} chance to increase spell critical chance by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.LESSER_SPELL_CRIT;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = passiveSpellBase();
        spell.school = SpellSchools.ARCANE;

        var trigger = new Spell.Trigger();
        trigger.chance = T1_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_ANY;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.archetype = SpellSchool.Archetype.MAGIC;

        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_holy_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 25, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T1_PROC_EFFECT_DURATION));
        configureCooldown(spell, T1_PROC_EFFECT_COOLDOWN);


        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry lesser_proc_crit_damage = add(lesser_proc_crit_damage());
    private static Entry lesser_proc_crit_damage() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "lesser_proc_crit_damage");
        var description = "On spell hit: Spell critical strikes have {trigger_chance} chance to increase spell critical damage by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.LESSER_PROC_CRIT_DAMAGE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = passiveSpellBase();
        spell.school = SpellSchools.ARCANE;

        var trigger = new Spell.Trigger();
        trigger.chance = 0.5F;
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.archetype = SpellSchool.Archetype.MAGIC;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();

        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_holy_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 25, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T1_PROC_EFFECT_DURATION));
        configureCooldown(spell, T1_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry lesser_proc_arcane_fire = add(lesser_proc_arcane_fire());
    private static Entry lesser_proc_arcane_fire() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "lesser_proc_arcane_fire");
        var description = "On spell hit: {trigger_chance} chance to increase arcane and fire spell power by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.LESSER_POWER_ARCANE_FIRE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = passiveSpellBase();
        spell.school = SpellSchools.ARCANE;

        var trigger = new Spell.Trigger();
        trigger.chance = T1_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_ANY;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.archetype = SpellSchool.Archetype.MAGIC;

        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_holy_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 25, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T1_PROC_EFFECT_DURATION));
        configureCooldown(spell, T1_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry lesser_proc_frost_healing = add(lesser_proc_frost_healing());
    private static Entry lesser_proc_frost_healing() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "lesser_proc_frost_healing");
        var description = "On spell hit: {trigger_chance} chance to increase frost and healing spell power by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.LESSER_POWER_FROST_HEALING;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = passiveSpellBase();
        spell.school = SpellSchools.HEALING;

        var trigger = new Spell.Trigger();
        trigger.chance = T1_PROC_CHANCE;
        
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_ANY;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.archetype = SpellSchool.Archetype.MAGIC;

        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_holy_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 25, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T1_PROC_EFFECT_DURATION));
        configureCooldown(spell, T1_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry medium_proc_attack_damage = add(medium_proc_attack_damage());
    private static Entry medium_proc_attack_damage() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "medium_proc_attack_damage");
        var description = "On melee hit: {trigger_chance} chance to increase attack damage by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.MEDIUM_ATTACK_DAMAGE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var trigger = new Spell.Trigger();
        trigger.chance = T2_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.SHARPEN.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_nature_impact_decelerate", ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        null, 10, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T2_PROC_EFFECT_DURATION));
        configureCooldown(spell, T2_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry medium_proc_attack_speed = add(medium_proc_attack_speed());
    private static Entry medium_proc_attack_speed() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "medium_proc_attack_speed");
        var description = "On hit: {trigger_chance_1} chance to increase melee and ranged attack speed by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.MEDIUM_ATTACKS_SPEED;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var melee_trigger = new Spell.Trigger();
        melee_trigger.chance = T2_PROC_CHANCE;
        melee_trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        var ranged_trigger = new Spell.Trigger();
        ranged_trigger.chance = T2_PROC_CHANCE;
        ranged_trigger.type = Spell.Trigger.Type.ARROW_IMPACT;
        spell.passive.triggers = List.of(melee_trigger, ranged_trigger);

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.MEDAL_USE.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_nature_impact_decelerate", ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        null, 10, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T2_PROC_EFFECT_DURATION));
        configureCooldown(spell, T2_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry medium_proc_ranged_damage = add(medium_proc_ranged_damage());
    private static Entry medium_proc_ranged_damage() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "medium_proc_ranged_damage");
        var description = "On arrow hit: {trigger_chance} chance to increase ranged attack damage by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.MEDIUM_RANGED_DAMAGE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var trigger = new Spell.Trigger();
        trigger.chance = T2_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.ARROW_IMPACT;
        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.EAGLE_BOOST.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_nature_impact_decelerate", ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        null, 10, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T2_PROC_EFFECT_DURATION));
        configureCooldown(spell, T2_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry medium_proc_defense = add(medium_proc_defense());
    private static Entry medium_proc_defense() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "medium_proc_defense");
        var description = "On damage taken: {trigger_chance} chance to increase armor toughness by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.MEDIUM_DEFENSE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}",  SpellTooltip.formattedNumber(effect.config().firstModifierValue()));

        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var trigger = new Spell.Trigger();
        trigger.chance = T2_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.DAMAGE_TAKEN;
        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.SHARPEN.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_nature_impact_decelerate", ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        null, 10, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T2_PROC_EFFECT_DURATION));
        configureCooldown(spell, T2_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry medium_proc_spell_power = add(medium_proc_spell_power());
    private static Entry medium_proc_spell_power() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "medium_proc_spell_power");
        var description = "On spell hit: {trigger_chance} chance to increase spell power by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.MEDIUM_SPELL_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = passiveSpellBase();
        spell.school = SpellSchools.ARCANE;

        var trigger = new Spell.Trigger();
        trigger.chance = T2_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.archetype = SpellSchool.Archetype.MAGIC;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();

        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_holy_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 25, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T2_PROC_EFFECT_DURATION));
        configureCooldown(spell, T2_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry medium_proc_spell_haste = add(medium_proc_spell_haste());
    private static Entry medium_proc_spell_haste() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "medium_proc_spell_haste");
        var description = "On spell hit: {trigger_chance} chance to increase spell haste by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.MEDIUM_SPELL_HASTE;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = passiveSpellBase();
        spell.school = SpellSchools.ARCANE;

        var trigger = new Spell.Trigger();
        trigger.chance = T2_PROC_CHANCE;
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.spell = new Spell.Trigger.SpellCondition();
        trigger.spell.archetype = SpellSchool.Archetype.MAGIC;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();

        spell.passive.triggers = List.of(trigger);

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.HASTE_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_holy_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 25, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T2_PROC_EFFECT_DURATION));
        configureCooldown(spell, T2_PROC_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry medium_use_arcane_power = add(medium_use_arcane_power());
    private static Entry medium_use_arcane_power() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "medium_use_arcane_power");
        var description = "Use: Increases arcane spell power by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.MEDIUM_ARCANE_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = activeSpellBase();
        spell.school = SpellSchools.ARCANE;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_white_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 15, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry medium_use_fire_power = add(medium_use_fire_power());
    private static Entry medium_use_fire_power() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "medium_use_fire_power");
        var description = "Use: Increases fire spell power by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.MEDIUM_FIRE_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = activeSpellBase();
        spell.school = SpellSchools.FIRE;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_white_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 15, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry medium_use_frost_power = add(medium_use_frost_power());
    private static Entry medium_use_frost_power() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "medium_use_frost_power");
        var description = "Use: Increases frost spell power by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.MEDIUM_FROST_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = activeSpellBase();
        spell.school = SpellSchools.FROST;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_white_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 15, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry medium_use_healing_power = add(medium_use_healing_power());
    private static Entry medium_use_healing_power() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "medium_use_healing_power");
        var description = "Use: Increases healing spell power by {buff_percent} for {effect_duration} seconds.";
        var effect = RelicEffects.MEDIUM_HEALING_POWER;
        var title = effect.title;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff_percent}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;

        spell.release.animation = "spell_engine:dual_handed_weapon_charge";
        spell.release.sound = new Sound(RelicSounds.INTELLECT_BUFF.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_white_spark_decelerate",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        null, 15, 0.15F, 0.25F, 0.0F, -0.2F)
        };

        spell.impacts = List.of(createEffectImpact(effect.id(), T2_USE_EFFECT_DURATION));
        configureCooldown(spell, T2_USE_EFFECT_COOLDOWN);

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry greater_perk_roll_damage = add(greater_perk_roll_damage());
    private static Entry greater_perk_roll_damage() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "greater_perk_roll_damage");
        var title = "Lightning Roll";
        var description = "Rolling conjures a small discharge of lightning around you, dealing {damage} damage to nearby enemies.";
        var spell = passiveSpellBase();
        spell.school = SpellSchools.LIGHTNING;
        spell.range = 3;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.ROLL;
        spell.passive.triggers = List.of(trigger);

        spell.target = new Spell.Target();
        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();

        spell.release.sound = new Sound(RelicSounds.LIGHTNING_IMPACT_SMALL.id().toString());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:electric_arc_a", ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        null, 15, 0.01F, 0.05F, 0.0F, spell.range),
                new ParticleBatch("spell_engine:electric_arc_b", ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        null, 15, 0.01F, 0.05F, 0.0F, spell.range)
        };


        var damage = new Spell.Impact();
        damage.action = new Spell.Impact.Action();
        damage.action.type = Spell.Impact.Action.Type.DAMAGE;
        damage.action.damage = new Spell.Impact.Action.Damage();
        damage.action.min_power = 6;
        damage.action.max_power = 12;

        spell.impacts = List.of(damage);
        configureCooldown(spell, 10);

        return new Entry(id, spell, title, description, null);
    }

    public static Entry greater_perk_melee_stun = add(greater_perk_melee_stun());
    private static Entry greater_perk_melee_stun() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "greater_perk_melee_stun");
        var title = "Stunning Strikes";
        var description = "On melee hit: {trigger_chance} chance to stun the target and nearby enemies for {effect_duration} seconds.";
        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.MELEE_IMPACT;
        trigger.chance = 0.1F;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var stun = createEffectImpact(RelicEffects.STUN.id(), T3_PERK_CC_DURATION);
        stun.sound = new Sound(RelicSounds.STUN_GENERIC.id().toString());
        spell.impacts = List.of(stun);
        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 2.0F;

        configureCooldown(spell, T3_PERK_CC_COOLDOWN);

        return new Entry(id, spell, title, description, null);
    }

    public static Entry greater_perk_spell_stun = add(greater_perk_spell_stun());
    private static Entry greater_perk_spell_stun() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "greater_perk_spell_stun");
        var title = "Disruption";
        var description = "On spell hit: {trigger_chance} chance to stun the target and nearby enemies for {effect_duration} seconds.";
        var spell = passiveSpellBase();
        spell.school = SpellSchools.ARCANE;

        var trigger = new Spell.Trigger();
        trigger.chance = 0.1F;
        trigger.type = Spell.Trigger.Type.SPELL_IMPACT_SPECIFIC;
        trigger.impact = new Spell.Trigger.ImpactCondition();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var stun = createEffectImpact(RelicEffects.STUN.id(), T3_PERK_CC_DURATION);
        stun.sound = new Sound(RelicSounds.STUN_GENERIC.id().toString());
        spell.impacts = List.of(stun);
        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 2.0F;

        configureCooldown(spell, T3_PERK_CC_COOLDOWN);

        return new Entry(id, spell, title, description, null);
    }

    public static Entry greater_perk_ranged_levitate = add(greater_perk_ranged_levitate());
    private static Entry greater_perk_ranged_levitate() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "greater_perk_ranged_levitate");
        var title = "Levitation";
        var description = "On arrow hit: {trigger_chance} chance to levitate the target and nearby enemies for {effect_duration} seconds.";
        var spell = passiveSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_RANGED;

        var trigger = new Spell.Trigger();
        trigger.chance = 0.1F;
        trigger.type = Spell.Trigger.Type.ARROW_IMPACT;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var levitate = createEffectImpact(StatusEffects.LEVITATION.getKey().get().getValue(),T3_PERK_CC_DURATION);
        levitate.sound = new Sound(RelicSounds.LEVITATE_GENERIC.id().toString());
        levitate.action.status_effect.amplifier = 3;
        levitate.particles = new ParticleBatch[] {
                new ParticleBatch("spell_engine:magic_nature_spark_float", ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        null, 20, 0.15F, 0.25F, 0.0F, 0F)
        };
        spell.impacts = List.of(levitate);
        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 2.0F;

        configureCooldown(spell, T3_PERK_CC_COOLDOWN);

        return new Entry(id, spell, title, description, null);
    }
}
