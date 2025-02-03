package net.relics_rpgs.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.relics_rpgs.item.Group;
import net.relics_rpgs.item.RelicItemTags;
import net.relics_rpgs.item.RelicsItems;

import java.util.concurrent.CompletableFuture;

public class RelicsDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(LangGenerator::new);
        pack.addProvider(ModelProvider::new);
    }

    public static class ItemTagGenerator extends FabricTagProvider<Item> {
        public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.ITEM, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            var all = getOrCreateTagBuilder(RelicItemTags.ALL);
            RelicsItems.entries.forEach(entry -> all.addOptional(entry.id()));
        }
    }

    public static class LangGenerator extends FabricLanguageProvider {
        protected LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, "en_us", registryLookup);
        }

        @Override
        public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
            translationBuilder.add("trinkets.slot.spell.trinket", "Trinket");
            translationBuilder.add("trinkets.slot.charm.trinket", "Trinket");
            translationBuilder.add(Group.translationKey, "Relics (RPG Series)");
            RelicsItems.entries.forEach(entry ->
                translationBuilder.add(entry.item().get().getTranslationKey(), entry.translatedName())
            );
        }
    }

    public static class ModelProvider extends FabricModelProvider {
        public ModelProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            RelicsItems.entries.forEach(entry -> {
                itemModelGenerator.register(entry.item().get(), Models.GENERATED);
            });
        }
    }
}
