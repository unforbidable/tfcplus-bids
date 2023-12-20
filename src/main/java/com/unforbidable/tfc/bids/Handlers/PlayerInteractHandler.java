package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Items.ItemExtraFood;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        EntityItem entityItem = event.item;
        EntityPlayer player = event.entityPlayer;

        if (BidsOptions.Crops.enableCerealSeedAutoConversion) {
            if (entityItem.getEntityItem().getItem() == TFCItems.seedsBarley) {
                convertPickedItem(entityItem, player, BidsItems.seedsNewBarley);
            } else if (entityItem.getEntityItem().getItem() == TFCItems.seedsOat) {
                convertPickedItem(entityItem, player, BidsItems.seedsNewOat);
            } else if (entityItem.getEntityItem().getItem() == TFCItems.seedsRye) {
                convertPickedItem(entityItem, player, BidsItems.seedsNewRye);
            } else if (entityItem.getEntityItem().getItem() == TFCItems.seedsWheat) {
                convertPickedItem(entityItem, player, BidsItems.seedsNewWheat);
            }
        }
    }

    private static void convertPickedItem(EntityItem entityItem, EntityPlayer player, Item targetItem) {
        ItemStack is = entityItem.getEntityItem();
        int count = is.stackSize;
        entityItem.delayBeforeCanPickup = 100;
        entityItem.setDead();
        entityItem.setInvisible(true);
        Random rand = player.worldObj.rand;
        player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        ItemStack targetIs = new ItemStack(targetItem, count);
        player.inventory.addItemStackToInventory(targetIs);
    }

}
