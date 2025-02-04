package net.relics_rpgs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.relics_rpgs.config.EffectConfig;
import net.relics_rpgs.config.ItemConfig;
import net.relics_rpgs.item.Group;
import net.relics_rpgs.item.ItemCompat;
import net.relics_rpgs.item.RelicItems;
import net.relics_rpgs.spell.RelicEffects;
import net.relics_rpgs.spell.RelicSounds;
import net.tinyconfig.ConfigManager;

public class RelicsMod implements ModInitializer {
    public static final String NAMESPACE = "relics_rpgs";
    public static final String DIRECTORY = "relics";
    public static ConfigManager<ItemConfig> itemConfig = new ConfigManager<>
            ("items", new ItemConfig())
            .builder()
            .setDirectory(DIRECTORY)
            .sanitize(true)
            .build();

    public static ConfigManager<EffectConfig> effectConfig = new ConfigManager<>
            ("effects", new EffectConfig())
            .builder()
            .setDirectory(DIRECTORY)
            .sanitize(true)
            .build();

    @Override
    public void onInitialize() {
        RelicSounds.register();
        itemConfig.refresh();
        effectConfig.refresh();
        ItemCompat.init();
        Group.GROUP = FabricItemGroup.builder()
                .icon(() -> new ItemStack(RelicItems.entries.get(0).item().get()))
                .displayName(Text.translatable(Group.translationKey))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.GROUP);
        RelicItems.register(itemConfig.value.entries);
        itemConfig.save();
        RelicEffects.register(effectConfig.value);
        effectConfig.save();
    }
}
