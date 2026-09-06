package net.relics_rpgs.util;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/// Attribute id helpers for 1.20.1.
///
/// On 1.20.1 `EntityAttribute` is a plain object with no id accessor (1.21's `getIdAsString()` does not
/// exist), so ids have to be read back out of the registry.
///
/// The `ranged_weapon:*` entries are string constants on purpose. A two-platform 1.20.1 RangedWeaponAPI
/// artifact does exist now, but Relics only ever *writes* these attributes into item config — every
/// consumer routes them through the `conditional_attributes` mechanism keyed on
/// {@link #RANGED_WEAPON_API_MOD_ID}, so they are only ever resolved when the mod is actually installed.
/// Keeping them as ids means no compile dependency and no `isModLoaded` holder class is needed at all.
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
