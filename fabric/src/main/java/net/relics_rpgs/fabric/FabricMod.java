package net.relics_rpgs.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.relics_rpgs.fabric.compat.CompatFeatures;
import net.relics_rpgs.RelicsMod;
import net.relics_rpgs.item.Group;
import net.relics_rpgs.item.RelicItems;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CompatFeatures.init();
        RelicsMod.init();
        RelicsMod.registerSounds();

        // Create and register item group (Fabric-specific)
        Group.GROUP = FabricItemGroup.builder()
                .icon(Group.ICON)
                .title(Component.translatable(Group.translationKey))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Group.KEY, Group.GROUP);

        RelicsMod.registerItems();
        RelicsMod.registerEffects();

        // Relic items into the Relics creative tab — Fabric API (NeoForge uses BuildCreativeModeTabContentsEvent).
        ItemGroupEvents.modifyEntriesEvent(Group.KEY).register(content -> {
            for (var entry : RelicItems.entries) {
                if (entry.isEnabled()) {
                    content.accept(entry.item().get());
                }
            }
        });
    }
}
