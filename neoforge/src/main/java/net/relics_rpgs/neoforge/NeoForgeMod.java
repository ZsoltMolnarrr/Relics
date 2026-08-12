package net.relics_rpgs.neoforge;

import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.relics_rpgs.neoforge.compat.CompatFeatures;
import net.relics_rpgs.RelicsMod;
import net.relics_rpgs.item.Group;
import net.relics_rpgs.item.RelicItems;

@Mod(RelicsMod.NAMESPACE)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        CompatFeatures.init();
        RelicsMod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        // Relic items into the Relics creative tab — NeoForge mod-bus event (replaces ItemGroupEvents).
        modBus.addListener(BuildCreativeModeTabContentsEvent.class, NeoForgeMod::buildTabContents);
    }

    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> {
            RelicsMod.registerSounds();
        });
        event.register(RegistryKeys.ITEM_GROUP, reg -> {
            // Create and register item group (NeoForge-specific). Vanilla ItemGroup.Builder — the static
            // ItemGroup.builder() is a Fabric API interface-injected method absent on NeoForge at runtime.
            Group.GROUP = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                    .icon(Group.ICON)
                    .displayName(Text.translatable(Group.translationKey))
                    .build();
            Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.GROUP);
        });
        event.register(RegistryKeys.ITEM, reg -> {
            RelicsMod.registerItems();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            RelicsMod.registerEffects();
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(Group.KEY)) {
            return;
        }
        for (var entry : RelicItems.entries) {
            if (entry.isEnabled()) {
                event.add(entry.item().get());
            }
        }
    }
}
