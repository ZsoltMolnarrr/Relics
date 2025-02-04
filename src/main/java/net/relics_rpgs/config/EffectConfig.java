package net.relics_rpgs.config;

import java.util.LinkedHashMap;
import java.util.List;

public class EffectConfig {
    public LinkedHashMap<String, Entry> entries = new LinkedHashMap<>();

    public record Entry(List<AttributeModifier> attributes) {
        public float firstModifierValue() {
            if (!attributes.isEmpty()) {
                return attributes.get(0).value;
            }
            return 0;
        }
    }
}
