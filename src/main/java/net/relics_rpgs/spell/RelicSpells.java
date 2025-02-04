package net.relics_rpgs.spell;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.relics_rpgs.RelicsMod;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
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
        spell.learn.enabled = false;
        spell.learn.tier = 7;
        spell.active = new Spell.Active();
        spell.active.scroll.generate = false;

        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        return spell;
    }

    private static final float T1_USE_EFFECT_DURATION = 10;
    private static final float T1_USE_EFFECT_COOLDOWN = 60;

    public static Entry meteorite_whetstone = add(meteorite_whetstone());
    private static Entry meteorite_whetstone() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "meteorite_whetstone");
        var description = "Use: Increases attack damage by {buff} for {effect_duration} seconds.";
        var effect = RelicEffects.METEORITE_WHETSTONE;
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

        spell.impact = new Spell.Impact[] {
            buff
        };

        spell.cost = new Spell.Cost();
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = T1_USE_EFFECT_COOLDOWN;

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry medal_of_valor = add(medal_of_valor());
    private static Entry medal_of_valor() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "medal_of_valor");
        var description = "Use: Increases melee and ranged attack speed by {buff} for {effect_duration} seconds.";
        var effect = RelicEffects.MEDAL_OF_VALOR;
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

        spell.impact = new Spell.Impact[] {
            buff
        };

        spell.cost = new Spell.Cost();
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = T1_USE_EFFECT_COOLDOWN;

        return new Entry(id, spell, title, description, mutator);
    }

    public static Entry eagle_eye = add(eagle_eye());
    private static Entry eagle_eye() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "eagle_eye");
        var description = "Use: Increases ranged attack damage by {buff} for {effect_duration} seconds.";
        var effect = RelicEffects.EAGLE_EYE;
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

        spell.impact = new Spell.Impact[] {
            buff
        };

        spell.cost = new Spell.Cost();
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = T1_USE_EFFECT_COOLDOWN;

        return new Entry(id, spell, title, description, mutator);
    }
}
