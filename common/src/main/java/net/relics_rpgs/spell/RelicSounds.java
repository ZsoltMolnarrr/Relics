package net.relics_rpgs.spell;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.relics_rpgs.RelicsMod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RelicSounds {
    public record Entry(String name) {
        public Identifier id() {
            return new Identifier(RelicsMod.NAMESPACE, name);
        }
    }
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static final Entry SHARPEN = add(new Entry("sharpen"));
    public static final Entry MEDAL_USE = add(new Entry("medal_use"));
    public static final Entry EAGLE_BOOST = add(new Entry("eagle_boost"));
    public static final Entry POTION_GENERIC = add(new Entry("potion_generic"));
    public static final Entry INTELLECT_BUFF = add(new Entry("intellect_buff"));
    public static final Entry SPELL_HASTE_ACTIVATE_1 = add(new Entry("spell_haste_activate_1"));
    public static final Entry LIGHTNING_IMPACT_SMALL = add(new Entry("lightning_impact_small"));
    public static final Entry STUN_GENERIC = add(new Entry("stun_generic"));
    public static final Entry LEVITATE_GENERIC = add(new Entry("levitate_generic"));
    public static final Entry BLOODLUST_ACTIVATE = add(new Entry("bloodlust_activate"));
    public static final Entry SPELL_HASTE_ACTIVATE_2 = add(new Entry("spell_haste_activate_2"));
    public static final Entry BOW_STRING_ACTIVATE = add(new Entry("bow_string_activate"));
    public static final Entry HOLY_WATER_IMPACT = add(new Entry("holy_water_impact"));
    public static final Entry DEFENSE_ACTIVATE_1 = add(new Entry("defense_activate_1"));
    public static final Entry DEFENSE_ACTIVATE_2 = add(new Entry("defense_activate_2"));
    public static final Entry MELEE_ACTIVATE_1 = add(new Entry("melee_activate_1"));
    public static final Entry SPELL_POWER_ACTIVATE_2 = add(new Entry("spell_power_activate_2"));
    public static final Entry SPELL_POWER_ACTIVATE_3 = add(new Entry("spell_power_activate_3"));
    public static final Entry HEART_OF_BEAST_ACTIVATE = add(new Entry("heart_of_beast_activate"));
    public static final Entry HEALING_ZONE_ACTIVATE = add(new Entry("healing_zone_activate"));
    public static final Entry SPELL_ZONE_ACTIVATE = add(new Entry("spell_zone_activate"));
    public static final Entry MAGIC_ZONE_PRESENCE = add(new Entry("magic_zone_presence"));
    public static final Entry HORN_ACTIVATE = add(new Entry("horn_activate"));
    public static final Entry MONKEY_ACTIVATE = add(new Entry("monkey_activate"));
    public static final Entry HOOK_ACTIVATE = add(new Entry("hook_activate"));

    public static void register() {
        soundsToRegister().forEach((id, soundEvent) -> Registry.register(Registries.SOUND_EVENT, id, soundEvent));
    }

    /// Creation only — builds every sound event keyed by the id it registers under, and writes nothing.
    /// Forge's entrypoint iterates this into the `RegisterEvent` helper instead of calling
    /// {@link #register}, because a plain `Registry.register` throws on Forge 47.0-47.3.
    /// Skips ids already present, so it stays idempotent.
    public static Map<Identifier, SoundEvent> soundsToRegister() {
        var toRegister = new LinkedHashMap<Identifier, SoundEvent>();
        for (var entry: entries) {
            var soundId = entry.id();
            if (Registries.SOUND_EVENT.containsId(soundId)) { continue; }
            toRegister.put(soundId, SoundEvent.of(soundId));
        }
        return toRegister;
    }
}
