package net.relics_rpgs.forge;

import net.minecraft.item.ItemGroup;

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
import net.relics_rpgs.spell.RelicEffects;
import net.relics_rpgs.spell.RelicSounds;
import net.spell_engine.api.effect.Effects;

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

    /// One window per registry — Forge posts one `RegisterEvent` per registry and `event.register` is a
    /// no-op unless its key matches, so every block below runs in exactly its own window.
    ///
    /// Everything goes through the `RegisterHelper` the event hands out, never `Registry.register`: only
    /// Forge 47.4.0+ clears the vanilla registry's own lock, so on 47.0-47.3 (and NeoForge 1.20.1, both
    /// inside our `loaderVersion = "[47,)"`) a plain `Registry.register` throws "Can not register to a
    /// locked registry" even here. These loops are deliberate duplicates of what `common` runs on Fabric.
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.SOUND_EVENT, helper ->
                RelicSounds.soundsToRegister().forEach(helper::register));

        event.register(RegistryKeys.ITEM, helper -> {
            RelicItems.itemsToRegister(RelicsMod.itemConfig.value.entries).forEach(helper::register);
            RelicsMod.itemConfig.save();
        });

        event.register(RegistryKeys.STATUS_EFFECT, helper -> {
            RelicEffects.configureBehaviours();
            Effects.effectsToRegister(RelicEffects.entries, RelicsMod.effectConfig.value.effects)
                    .forEach(helper::register);
            // Forge's helper returns void where `Registry.registerReference` returns the entry, so
            // `Effects.Entry#entry` has to be read back out of the registry.
            Effects.linkEntries(RelicEffects.entries);
            RelicsMod.effectConfig.save();
        });

        // The item group gets its own block: `creative_mode_tab` is event 65 while `item` is event 7, so
        // registering it from the ITEM pass would file it under the wrong key and drop it in silence.
        event.register(RegistryKeys.ITEM_GROUP, helper -> {
            // Vanilla `ItemGroup.Builder` — the static `ItemGroup.builder()` is a Fabric API
            // interface-injected method and does not exist on Forge at runtime.
            Group.GROUP = new ItemGroup.Builder(ItemGroup.Row.TOP, 0)
                    .icon(Group.ICON)
                    .displayName(Text.translatable(Group.translationKey))
                    .build();
            helper.register(Group.ID, Group.GROUP);
        });
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
