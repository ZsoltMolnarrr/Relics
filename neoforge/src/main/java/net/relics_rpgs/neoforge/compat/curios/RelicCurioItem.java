package net.relics_rpgs.neoforge.compat.curios;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class RelicCurioItem extends Item implements ICurioItem {
    public RelicCurioItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        var isOnCooldown = false;
        if (slotContext.entity() instanceof PlayerEntity player) {
            isOnCooldown = !player.isCreative() && player.getItemCooldownManager().isCoolingDown(stack);
        }
        return ICurioItem.super.canUnequip(slotContext, stack) && !isOnCooldown;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        // Relics have no sound of their own, so this is the generic equip sound Curios would
        // play on use. Doing it here covers dragging into the slot as well, which is silent otherwise.
        var entity = slotContext.entity();
        if (entity == null) {
            return;
        }
        var world = entity.getEntityWorld();
        if (world.isClient()                        // the server broadcast below reaches every nearby client
                || entity.age <= 100                // gear already worn when entering a world/dimension
                || prevStack.isOf(stack.getItem())) // same item, only its data changed
        {
            return;
        }
        world.playSound(null, entity.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value(),
                entity.getSoundCategory(), 1.0F, 1.0F);
    }

    @Override
    public void onEquipFromUse(SlotContext slotContext, ItemStack stack) {
        // Silent on purpose: `onEquip` above already fires for every equip path, this one included.
    }
}
