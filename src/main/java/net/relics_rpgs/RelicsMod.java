package net.relics_rpgs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.relics_rpgs.item.Group;
import net.relics_rpgs.item.ItemCompat;
import net.relics_rpgs.item.RelicsItems;

public class RelicsMod implements ModInitializer {

    public static final String NAMESPACE = "relics_rpgs";

    @Override
    public void onInitialize() {
        ItemCompat.init();
        Group.GROUP = FabricItemGroup.builder()
                .icon(() -> new ItemStack(RelicsItems.entries.get(0).item().get()))
                .displayName(Text.translatable(Group.translationKey))
                .build();
        Registry.register(Registries.ITEM_GROUP, Group.KEY, Group.GROUP);
        RelicsItems.register();
    }
}
