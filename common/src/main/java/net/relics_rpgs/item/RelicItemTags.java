package net.relics_rpgs.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.relics_rpgs.RelicsMod;

public class RelicItemTags {
    public static final TagKey<Item> ALL = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE, "all"));
}
