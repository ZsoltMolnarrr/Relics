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
import net.relics_rpgs.RelicsMod;
import net.relics_rpgs.item.Group;
import net.relics_rpgs.item.RelicItemTags;
import net.relics_rpgs.item.RelicItems;
import net.relics_rpgs.spell.RelicEffects;
import net.relics_rpgs.spell.RelicSounds;
import net.relics_rpgs.spell.RelicSpells;
import net.spell_engine.api.data_gen.SimpleSoundGenerator;
import net.spell_engine.api.data_gen.SpellGenerator;

import java.util.concurrent.CompletableFuture;

public class RelicsDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(LangGenerator::new);
        pack.addProvider(ModelProvider::new);
        pack.addProvider(RelicsSpellGen::new);
        pack.addProvider(SoundGen::new);
    }

    public static class ItemTagGenerator extends FabricTagProvider<Item> {
        public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.ITEM, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            var all = getOrCreateTagBuilder(RelicItemTags.ALL);
            RelicItems.entries.forEach(entry -> all.addOptional(entry.id()));
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
            RelicItems.entries.forEach(entry ->
                translationBuilder.add(entry.item().get().getTranslationKey(), entry.translatedName())
            );
            //   "spell.archers.entangling_roots.name": "Entangling Roots",
            RelicSpells.entries.forEach(entry -> {
                var id = entry.id();
                translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name" , entry.title());
                translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description" , entry.description());
            });
            RelicEffects.entries.forEach(entry -> {
                translationBuilder.add(entry.effect.getTranslationKey(), entry.title);
                translationBuilder.add(entry.effect.getTranslationKey() + ".description", entry.description);
            });
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
            RelicItems.entries.forEach(entry -> {
                itemModelGenerator.register(entry.item().get(), Models.GENERATED);
            });
        }
    }

    public static class RelicsSpellGen extends SpellGenerator {
        public RelicsSpellGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSpells(Builder builder) {
            for (var entry: RelicSpells.entries) {
                builder.add(entry.id(), entry.spell());
            }
        }
    }

    public static class SoundGen extends SimpleSoundGenerator {
        public SoundGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSounds(Builder builder) {
            builder.entries.add(new Entry(RelicsMod.NAMESPACE,
                    RelicSounds.entries.stream().map(RelicSounds.Entry::name).toList()));
        }
    }
}
