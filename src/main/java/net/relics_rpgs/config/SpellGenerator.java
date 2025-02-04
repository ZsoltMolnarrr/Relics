package net.relics_rpgs.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class SpellGenerator implements DataProvider {
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup;
    protected final FabricDataOutput dataOutput;

    public SpellGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        this.dataOutput = dataOutput;
        this.registryLookup = registryLookup;
    }

    public record Entry(Identifier id, Spell spell) { }
    public static class Builder {
        private final List<Entry> entries = new ArrayList<>();
        public void add(Identifier id, Spell spell) {
            entries.add(new Entry(id, spell));
        }
    }

    public abstract void generateSpells(Builder builder);

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        var builder = new Builder();
        generateSpells(builder);
        var entries = builder.entries;

        List<CompletableFuture> writes = new ArrayList<>();
        for (var entry: entries) {
            var spell = entry.spell;
            var spellId = entry.id;
            var json = gson.toJsonTree(spell);
            writes.add(DataProvider.writeToPath(writer, json, getFilePath(spellId)));
        }

        return CompletableFuture.allOf(writes.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "Spell Generator";
    }

    private Path getFilePath(Identifier spellId) {
        return this.dataOutput.getResolver(DataOutput.OutputType.DATA_PACK, "spell").resolveJson(spellId);
    }
}
