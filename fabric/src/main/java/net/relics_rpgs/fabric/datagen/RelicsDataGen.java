package net.relics_rpgs.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.relics_rpgs.RelicsMod;
import net.relics_rpgs.item.Group;
import net.relics_rpgs.item.RelicItemTags;
import net.relics_rpgs.item.RelicItems;
import net.relics_rpgs.spell.RelicEffects;
import net.relics_rpgs.spell.RelicSounds;
import net.relics_rpgs.spell.RelicSpells;
import net.spell_engine.api.datagen.SimpleSoundGenerator;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.item.Equipment;

import java.util.HashMap;
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

    public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {

        public ItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            var all = builder(RelicItemTags.ALL);
            RelicItems.entries.forEach(entry -> all.addOptional(ResourceKey.create(Registries.ITEM, entry.id())));

            HashMap<Identifier, Equipment.LootProperties> relicEntries = new HashMap<>();
            for (var entry: RelicItems.entries) {
                var id = entry.id();
                var lootProperties = Equipment.LootProperties.of(entry.tier(), entry.lootTheme);
                relicEntries.put(id, lootProperties);
            }
            generateRelicTags(relicEntries);
        }
    }

    public static class LangGenerator extends FabricLanguageProvider {
        protected LangGenerator(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, "en_us", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder translationBuilder) {
            translationBuilder.add("trinkets.slot.spell.trinket", "Trinket");
            translationBuilder.add("trinkets.slot.charm.trinket", "Trinket");
            translationBuilder.add(Group.translationKey, "Relics");
            RelicItems.entries.forEach(entry ->
                translationBuilder.add(entry.item().get().getDescriptionId(), entry.translatedName())
            );
            //   "spell.archers.entangling_roots.name": "Entangling Roots",
            RelicSpells.entries.forEach(entry -> {
                var id = entry.id();
                translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name" , entry.title());
                translationBuilder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description" , entry.description());
            });
            RelicEffects.entries.forEach(entry -> {
                translationBuilder.add(entry.effect.getDescriptionId(), entry.title);
                translationBuilder.add(entry.effect.getDescriptionId() + ".description", entry.description);
            });
        }
    }

    public static class ModelProvider extends FabricModelProvider {
        public ModelProvider(FabricPackOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

        }

        @Override
        public void generateItemModels(ItemModelGenerators itemModelGenerator) {
            RelicItems.entries.forEach(entry -> {
                itemModelGenerator.generateFlatItem(entry.item().get(), ModelTemplates.FLAT_ITEM);
            });
        }
    }

    public static class RelicsSpellGen extends SpellGenerator {
        public RelicsSpellGen(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
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
        public SoundGen(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generateSounds(Builder builder) {
            builder.entries.add(new Entry(RelicsMod.NAMESPACE,
                    RelicSounds.entries.stream().map(RelicSounds.Entry::name).toList()));
        }
    }
}
