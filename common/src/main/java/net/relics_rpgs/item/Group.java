package net.relics_rpgs.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.relics_rpgs.RelicsMod;

import java.util.function.Supplier;

public class Group {
    public static Identifier ID = Identifier.fromNamespaceAndPath(RelicsMod.NAMESPACE, "generic");
    public static String translationKey = "itemGroup." + ID.getNamespace() + "." + ID.getPath();
    public static ResourceKey<CreativeModeTab> KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ID);
    public static CreativeModeTab GROUP;
    public static Supplier<ItemStack> ICON = () -> {
        return new ItemStack(RelicItems.MEDIUM_USE_FIRE_POWER.item().get());
    };
}
