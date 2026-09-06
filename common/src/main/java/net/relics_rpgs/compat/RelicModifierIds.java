package net.relics_rpgs.compat;

import net.relics_rpgs.RelicsMod;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/// Shared derivation of the attribute-modifier identity used by both accessory integrations
/// (Trinkets on Fabric, Curios on Forge).
///
/// On 1.20.1 an `EntityAttributeModifier` is keyed by a `UUID` + display name rather than by the 1.21
/// `Identifier`, and both slot APIs hand the item a **slot-unique** UUID. Folding the item id into that
/// UUID reproduces the 1.21 `<slot>/<item>` modifier id: bonuses stack across slots, and swapping a
/// different item into the same slot cannot reuse a key and trip vanilla's
/// "Modifier is already applied" guard.
public class RelicModifierIds {
    public static UUID perSlotAndItem(UUID slotUuid, String itemPath) {
        return UUID.nameUUIDFromBytes((slotUuid + "/" + itemPath).getBytes(StandardCharsets.UTF_8));
    }

    public static String name(String itemPath) {
        return RelicsMod.NAMESPACE + ":" + itemPath;
    }
}
