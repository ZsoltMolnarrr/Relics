package net.relics_rpgs.config;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class AttributeModifier {
    public static final AttributeModifier EMPTY = new AttributeModifier();
    AttributeModifier() { }

    @Nullable public String id;
    public String attribute = "";
    public float value = 0;
    public EntityAttributeModifier.Operation operation = EntityAttributeModifier.Operation.ADD_VALUE;

    public AttributeModifier(Identifier attribute, float value, EntityAttributeModifier.Operation operation) {
        this(attribute.toString(), value, operation);
    }

    public AttributeModifier(String attribute, float value, EntityAttributeModifier.Operation operation) {
        this.attribute = attribute;
        this.value = value;
        this.operation = operation;
    }
}
