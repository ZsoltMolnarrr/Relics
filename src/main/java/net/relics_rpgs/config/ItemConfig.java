package net.relics_rpgs.config;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;

public class ItemConfig {
    public LinkedHashMap<String, Entry> entries = new LinkedHashMap<>();

    public static class Entry {
        public static final Entry EMPTY = new Entry();

        public boolean enabled = true;
        @Nullable public Conditions conditions;
        public List<AttributeModifier> attributes = List.of();

        public Entry withAttributes(List<AttributeModifier> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Entry withRequiredMod(String required_mod) {
            if (conditions == null) {
                conditions = new Conditions();
            }
            conditions.required_mod = required_mod;
            return this;
        }

        public boolean isEnabled() {
            return enabled && (conditions == null || conditions.isSatisfied());
        }
    }

    public static class AttributeModifier { AttributeModifier() { }
        public String id = "";
        public float value = 0;
        public EntityAttributeModifier.Operation operation = EntityAttributeModifier.Operation.ADD_VALUE;

        public AttributeModifier(Identifier id, float value, EntityAttributeModifier.Operation operation) {
            this(id.toString(), value, operation);
        }

        public AttributeModifier(String id, float value, EntityAttributeModifier.Operation operation) {
            this.id = id;
            this.value = value;
            this.operation = operation;
        }
    }

    public static class Conditions {
        @Nullable public String required_mod;

        public boolean isSatisfied() {
            if (required_mod != null || !required_mod.isEmpty()) {
                if (!FabricLoader.getInstance().isModLoaded(required_mod)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static AttributeModifiersComponent.Builder getBuilder(Identifier modifierId, List<AttributeModifier> attributesConfig) {
        AttributeModifiersComponent.Builder attributes = AttributeModifiersComponent.builder();
        for (var modifier : attributesConfig) {
            var id = Identifier.of(modifier.id);
            var attribute = Registries.ATTRIBUTE.getEntry(id);
            if (attribute.isPresent()) {
                attributes.add(attribute.get(),
                        new EntityAttributeModifier(
                                modifierId,
                                modifier.value,
                                modifier.operation), AttributeModifierSlot.ANY);
            } else {
                System.err.println("Failed to resolve EntityAttribute with id: " + modifier.id);
            }
        }
        return attributes;
    }
}