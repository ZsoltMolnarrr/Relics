package net.relics_rpgs;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.relics_rpgs.config.ItemConfig;
import net.relics_rpgs.item.Group;
import net.relics_rpgs.item.RelicItems;
import net.relics_rpgs.spell.RelicEffects;
import net.relics_rpgs.spell.RelicMechanics;
import net.relics_rpgs.spell.RelicSounds;
import net.spell_engine.api.config.ConfigFile;
import net.tiny_config.ConfigManager;

public class RelicsMod {
    public static final String NAMESPACE = "relics_rpgs";
    public static final String DIRECTORY = "relics";
    public static ConfigManager<ItemConfig> itemConfig = new ConfigManager<>
            ("items", new ItemConfig())
            .builder()
            .setDirectory(DIRECTORY)
            .sanitize(true)
            .build();

    public static ConfigManager<ConfigFile.Effects> effectConfig = new ConfigManager<>
            ("effects", new ConfigFile.Effects())
            .builder()
            .setDirectory(DIRECTORY)
            .sanitize(true)
            .build();

    public static void init() {
        RelicSounds.register();
        itemConfig.refresh();
        effectConfig.refresh();
        RelicMechanics.init();
        Group.GROUP = FabricItemGroup.builder()
                .icon(Group.ICON)
                .displayName(Text.translatable(Group.translationKey))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.GROUP);
        RelicItems.register(itemConfig.value.entries);
        itemConfig.save();
        RelicEffects.register(effectConfig.value);
        effectConfig.save();
    }
}
