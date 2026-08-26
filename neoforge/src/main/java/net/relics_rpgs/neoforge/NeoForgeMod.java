package net.relics_rpgs.neoforge;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
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
        event.register(Registries.SOUND_EVENT, reg -> {
            RelicsMod.registerSounds();
        });
        event.register(Registries.CREATIVE_MODE_TAB, reg -> {
            // Create and register item group (NeoForge-specific). Vanilla ItemGroup.Builder — the static
            // ItemGroup.builder() is a Fabric API interface-injected method absent on NeoForge at runtime.
            Group.GROUP = new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
                    .icon(Group.ICON)
                    .title(Component.translatable(Group.translationKey))
                    .build();
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Group.KEY, Group.GROUP);
        });
        event.register(Registries.ITEM, reg -> {
            RelicsMod.registerItems();
        });
        event.register(Registries.MOB_EFFECT, reg -> {
            RelicsMod.registerEffects();
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(Group.KEY)) {
            return;
        }
        for (var entry : RelicItems.entries) {
            if (entry.isEnabled()) {
                event.accept(entry.item().get());
            }
        }
    }
}
