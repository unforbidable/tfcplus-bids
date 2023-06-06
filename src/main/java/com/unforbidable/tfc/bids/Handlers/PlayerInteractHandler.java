package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Items.ItemExtraFood;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInteractHandler {

    private Map<UUID, Integer> lastOpenContainerWindowId = new HashMap<UUID, Integer>();

    @SubscribeEvent
    public void onPlayerOpenContainer(PlayerOpenContainerEvent event) {
        // This event is continuously fired, so we keep track of container changing, and act only when new one opens
        int lastOpenContainerWindowIdForPlayer = lastOpenContainerWindowId.containsKey(event.entityPlayer.getUniqueID()) ?
                lastOpenContainerWindowId.get(event.entityPlayer.getUniqueID()) : -1;

        if (lastOpenContainerWindowIdForPlayer != event.entityPlayer.openContainer.windowId) {
            lastOpenContainerWindowId.put(event.entityPlayer.getUniqueID(), event.entityPlayer.openContainer.windowId);

            Bids.LOG.debug("Player open container changed: " + event.entityPlayer.openContainer.windowId);

            // This handles porridge not receiving all the food tags when cooked in a large vessel
            for (Object s : event.entityPlayer.openContainer.inventorySlots) {
                if (s instanceof Slot) {
                    Slot slot = (Slot) s;
                    ItemStack itemStack = slot.getStack();
                    if (itemStack != null && itemStack.getItem() instanceof ItemExtraFood) {
                        NBTTagCompound nbt = itemStack.getTagCompound();
                        if (!nbt.hasKey(Food.DECAY_TIMER_TAG)) {
                            ItemStack is = ItemFoodTFC.createTag(itemStack, Food.getWeight(itemStack));
                            event.entityPlayer.openContainer.putStackInSlot(slot.slotNumber, is);

                            Bids.LOG.info("Item stack " + itemStack.getDisplayName() + " missing decay tags corrected.");
                        }
                    }
                }
            }
        }
    }

}
