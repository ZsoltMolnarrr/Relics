package net.relics_rpgs.spell;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.relics_rpgs.RelicsMod;

import java.util.ArrayList;
import java.util.List;

public class RelicSounds {
    public record Entry(String name) {
        public Identifier id() {
            return Identifier.of(RelicsMod.NAMESPACE, name);
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
    public static final Entry HASTE_BUFF = add(new Entry("haste_buff"));
    public static final Entry LIGHTNING_IMPACT_SMALL = add(new Entry("lightning_impact_small"));

    public static void register() {
        for (var entry: entries) {
            var soundId = entry.id();
            var soundEvent = SoundEvent.of(soundId);
            Registry.register(Registries.SOUND_EVENT, soundId, soundEvent);
        }
    }
}
