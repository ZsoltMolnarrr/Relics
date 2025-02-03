package net.relics_rpgs.config;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemConfig {
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

    public static AttributeModifiersComponent.@NotNull Builder getBuilder(Identifier modifierId, List<AttributeModifier> attributesConfig) {
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