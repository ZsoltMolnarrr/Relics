package net.relics_rpgs.spell;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.relics_rpgs.RelicsMod;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.client.gui.SpellTooltip;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RelicSpells {
    public record Entry(Identifier id, Spell spell, String description, @Nullable SpellTooltip.DescriptionMutator mutator) { }
    public static final List<Entry> entries = new ArrayList<>();
    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static Entry cast_attack_damage = add(cast_attack_damage());
    public static Entry cast_attack_damage() {
        var id = Identifier.of(RelicsMod.NAMESPACE, "cast_attack_damage");
        var description = "Use: Increases attack damage by {buff} for {effect_duration} seconds.";
        var effect = RelicEffects.METEORITE_WHETSTONE;
        SpellTooltip.DescriptionMutator mutator = (args) -> args.description().replace("{buff}", SpellTooltip.percent(
                effect.config().firstModifierValue())
        );

        var spell = new Spell();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;
        spell.rank = 7;
        spell.learn.enabled = false;

        spell.tooltip.name = new Spell.Tooltip.LineOptions(false, false);
        spell.tooltip.description.color = Formatting.DARK_GREEN.asString();
        spell.tooltip.description.show_in_compact = true;

        spell.active = new Spell.Active();
        spell.active.scroll.generate = true;

        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = Identifier.ofVanilla("strength").toString();
        buff.action.status_effect.duration = 10;

        spell.impact = new Spell.Impact[] {
            buff
        };

        spell.cost = new Spell.Cost();
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = 60;

        return new Entry(id, spell, description, mutator);
    }
}
