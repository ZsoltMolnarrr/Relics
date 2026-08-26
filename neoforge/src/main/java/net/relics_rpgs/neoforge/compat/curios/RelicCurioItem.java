package net.relics_rpgs.neoforge.compat.curios;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class RelicCurioItem extends Item implements ICurioItem {
    public RelicCurioItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        var isOnCooldown = false;
        if (slotContext.entity() instanceof Player player) {
            isOnCooldown = !player.isCreative() && player.getCooldowns().isOnCooldown(stack);
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
        var world = entity.level();
        if (world.isClientSide()                        // the server broadcast below reaches every nearby client
                || entity.tickCount <= 100                // gear already worn when entering a world/dimension
                || prevStack.is(stack.getItem())) // same item, only its data changed
        {
            return;
        }
        world.playSound(null, entity.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC.value(),
                entity.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public void onEquipFromUse(SlotContext slotContext, ItemStack stack) {
        // Silent on purpose: `onEquip` above already fires for every equip path, this one included.
    }
}
