package net.relics_rpgs.util;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.relics_rpgs.config.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

public class AttributesUtil {
    public record Entry(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) { }
    public static AttributeModifiersComponent.Builder attributesComponent(Identifier modifierId, List<AttributeModifier> attributesConfig) {
        AttributeModifiersComponent.Builder componentBuilder = AttributeModifiersComponent.builder();
        var modifiers = modifiersFrom(modifierId, attributesConfig);
        for (var modifier : modifiers) {
            componentBuilder.add(modifier.attribute(), modifier.modifier(), AttributeModifierSlot.ANY);
        }
        return componentBuilder;
    }

    public static List<Entry> modifiersFrom(Identifier modifierId, List<AttributeModifier> attributesConfig) {
        var modifiers = new ArrayList<Entry>();
        for (var modifier : attributesConfig) {
            var id = (modifier.id != null && !modifier.id.isEmpty())
                    ? Identifier.of(modifier.id)
                    : Identifier.of(modifier.attribute);
            var attribute = Registries.ATTRIBUTE.getEntry(id);
            if (attribute.isPresent()) {
                modifiers.add(new Entry(
                        attribute.get(),
                        new EntityAttributeModifier(
                                modifierId,
                                modifier.value,
                                modifier.operation
                        )
                ));
            } else {
                System.err.println("Failed to resolve EntityAttribute with id: " + modifier.attribute);
            }
        }
        return modifiers;
    }
}
