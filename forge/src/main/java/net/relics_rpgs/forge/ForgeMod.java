package net.relics_rpgs.forge;

import net.minecraft.item.ItemGroup;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import net.relics_rpgs.RelicsMod;
import net.relics_rpgs.forge.client.ForgeClientMod;
import net.relics_rpgs.forge.compat.CompatFeatures;
import net.relics_rpgs.item.Group;
import net.relics_rpgs.item.RelicItems;

@Mod(RelicsMod.NAMESPACE)
public final class ForgeMod {
    @SuppressWarnings("removal")
    public ForgeMod() {
        CompatFeatures.init();
        RelicsMod.init();
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        // Relic items into the Relics creative tab — Forge mod-bus event (replaces ItemGroupEvents).
        modBus.addListener(EventPriority.NORMAL, false, BuildCreativeModeTabContentsEvent.class, ForgeMod::buildTabContents);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeClientMod.register(modBus);
        }
    }

    /// One window per registry — Forge locks every other registry while a window is open.
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, reg -> RelicsMod.registerSounds());
        event.register(RegistryKeys.ITEM_GROUP, reg -> {
            // Vanilla `ItemGroup.Builder` — the static `ItemGroup.builder()` is a Fabric API
            // interface-injected method and does not exist on Forge at runtime.
            Group.GROUP = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                    .icon(Group.ICON)
                    .displayName(Text.translatable(Group.translationKey))
                    .build();
            Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.GROUP);
        });
        event.register(RegistryKeys.ITEM, reg -> RelicsMod.registerItems());
        event.register(RegistryKeys.STATUS_EFFECT, reg -> RelicsMod.registerEffects());
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(Group.KEY)) {
            return;
        }
        for (var entry : RelicItems.entries) {
            if (entry.isEnabled()) {
                // Forge 47 takes a `Supplier<? extends ItemConvertible>`; `Entry#item()` already is one.
                event.accept(entry.item());
            }
        }
    }
}
