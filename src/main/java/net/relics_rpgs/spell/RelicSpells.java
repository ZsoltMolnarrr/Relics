package net.relics_rpgs.spell;

import net.minecraft.entity.attribute.EntityAttributes;
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

    private static final float T1_USE_EFFECT_DURATION = 10;
    private static final float T1_PROC_EFFECT_DURATION = 7;
    private static final float T1_USE_EFFECT_COOLDOWN = 60;
    private static final float T1_PROC_EFFECT_COOLDOWN = 45;

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

        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effect.id().toString();
        buff.action.status_effect.duration = T1_USE_EFFECT_DURATION;

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

        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effect.id().toString();
        buff.action.status_effect.duration = T1_USE_EFFECT_DURATION;

        spell.impacts = List.of(buff);

        spell.cost = new Spell.Cost();
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = T1_USE_EFFECT_COOLDOWN;

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

        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effect.id().toString();
        buff.action.status_effect.duration = T1_USE_EFFECT_DURATION;

        spell.impacts = List.of(buff);

        spell.cost = new Spell.Cost();
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = T1_USE_EFFECT_COOLDOWN;

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

        spell.cost = new Spell.Cost();
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = T1_USE_EFFECT_COOLDOWN;

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

        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effect.id().toString();
        buff.action.status_effect.duration = T1_USE_EFFECT_DURATION;

        spell.impacts = List.of(buff);

        spell.cost = new Spell.Cost();
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = T1_USE_EFFECT_COOLDOWN;

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

        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effect.id().toString();
        buff.action.status_effect.duration = T1_USE_EFFECT_DURATION;

        spell.impacts = List.of(buff);

        spell.cost = new Spell.Cost();
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = T1_USE_EFFECT_COOLDOWN;

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
        trigger.chance = 0.05F;
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

        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effect.id().toString();
        buff.action.status_effect.duration = T1_PROC_EFFECT_DURATION;

        spell.impacts = List.of(buff);

        spell.cost = new Spell.Cost();
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = T1_PROC_EFFECT_COOLDOWN;

        return new Entry(id, spell, title, description, mutator);
    }
}
