package net.relics_rpgs.forge.client;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.relics_rpgs.client.RelicsClientMod;

/// Only ever touched behind `FMLEnvironment.dist == Dist.CLIENT` (see `ForgeMod`), so no
/// `@EventBusSubscriber(value = Dist.CLIENT)` annotation is needed.
public class ForgeClientMod {
    public static void register(IEventBus modBus) {
        modBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, ForgeClientMod::onClientSetup);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        RelicsClientMod.init();
    }
}
