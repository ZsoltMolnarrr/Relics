package net.relics_rpgs.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.relics_rpgs.item.RelicItemTags;
import net.relics_rpgs.item.RelicsItems;

import java.util.concurrent.CompletableFuture;

public class RelicsDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(FabricDocsReferenceItemTagProvider::new);
    }
    
    public static class FabricDocsReferenceItemTagProvider extends FabricTagProvider<Item> {
        public FabricDocsReferenceItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.ITEM, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            var all = getOrCreateTagBuilder(RelicItemTags.ALL);
            RelicsItems.entries.forEach(entry -> all.addOptional(entry.id()));
        }
    }
}
