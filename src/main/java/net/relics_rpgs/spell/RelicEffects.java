package net.relics_rpgs.spell;

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

    public static final Entry METEORITE_WHETSTONE = add(new Entry("meteorite_whetstone",
            "Sharpness",
            "Increases attack damage.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x880000),
            new EffectConfig.Entry(
                    List.of(
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE.getIdAsString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
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
            entry.entry = Registry.registerReference(Registries.STATUS_EFFECT, entry.id(), entry.effect);
        }
    }
}
