package net.relics_rpgs.util;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/// Attribute id helpers for 1.20.1.
///
/// On 1.20.1 `EntityAttribute` is a plain object with no id accessor (1.21's `getIdAsString()` does not
/// exist), so ids have to be read back out of the registry.
///
/// The `ranged_weapon:*` entries are string constants on purpose: RangedWeaponAPI is **not** a compile
/// dependency of Relics on this line (there is no two-platform 1.20.1 artifact yet, and the legacy one is
/// Fabric-only). Every consumer routes them through the `conditional_attributes` mechanism keyed on
/// {@link #RANGED_WEAPON_API_MOD_ID}, so they are only ever resolved when the mod is actually installed.
public class AttributeIds {
    public static String of(EntityAttribute attribute) {
        return Registries.ATTRIBUTE.getId(attribute).toString();
    }

    public static Identifier identifierOf(EntityAttribute attribute) {
        return Registries.ATTRIBUTE.getId(attribute);
    }

    public static final String RANGED_WEAPON_API_MOD_ID = "ranged_weapon_api";
    public static final String RANGED_WEAPON_DAMAGE = "ranged_weapon:damage";
    public static final String RANGED_WEAPON_HASTE = "ranged_weapon:haste";
}
