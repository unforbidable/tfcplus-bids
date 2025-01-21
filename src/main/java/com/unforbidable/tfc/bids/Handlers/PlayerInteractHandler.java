package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Items.Tools.ItemCustomBucketMilk;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Drinks.FluidHelper;
import com.unforbidable.tfc.bids.Core.Drinks.Milk.MilkHelper;
import com.unforbidable.tfc.bids.Items.ItemExtraFood;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Events.FillContainerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PlayerInteractHandler {

    private Map<UUID, Integer> lastOpenContainerWindowId = new HashMap<UUID, Integer>();

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        if (!event.entityPlayer.worldObj.isRemote) {
            if (isValidMilkingContainer(event.entityPlayer.getHeldItem())) {
                if (canOverrideMilkingInteraction(event.entityPlayer, event.target)) {
                    if (FluidHelper.fillContainerOnEntityInteractEvent(event.entityPlayer, event.target)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private boolean canOverrideMilkingInteraction(EntityPlayer entityPlayer, Entity target) {
        return BidsOptions.Husbandry.enableDefaultMilkingInteractionOverride ||
            MilkHelper.alwaysOverrideMilkingInteraction(entityPlayer, target);
    }

    private boolean isValidMilkingContainer(ItemStack heldItem) {
        for (ItemStack ore : OreDictionary.getOres("itemMilkingContainer")) {
            if (OreDictionary.itemMatches(ore, heldItem, true)) {
                Bids.LOG.debug("Valid milking container: " + heldItem);

                return true;
            }
        }

        Bids.LOG.debug("Invalid milking container: " + heldItem);

        return false;
    }

    @SubscribeEvent
    public void onFillContainer(FillContainerEvent event) {
        if (event.output.getItem() instanceof IFood) {
            ItemCustomBucketMilk.createTag(event.output, 20f);
        }
    }

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

        if (BidsOptions.Crops.enableHardySeedAutoConversion) {
            if (entityItem.getEntityItem().getItem() == TFCItems.seedsOnion) {
                convertPickedItem(entityItem, player, BidsItems.seedsNewOnion);
            } else if (entityItem.getEntityItem().getItem() == TFCItems.seedsCabbage) {
                convertPickedItem(entityItem, player, BidsItems.seedsNewCabbage);
            } else if (entityItem.getEntityItem().getItem() == TFCItems.seedsGarlic) {
                convertPickedItem(entityItem, player, BidsItems.seedsNewGarlic);
            } else if (entityItem.getEntityItem().getItem() == TFCItems.seedsCarrot) {
                convertPickedItem(entityItem, player, BidsItems.seedsNewCarrot);
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
