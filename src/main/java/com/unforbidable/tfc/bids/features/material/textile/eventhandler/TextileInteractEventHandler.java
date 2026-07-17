package com.unforbidable.tfc.bids.features.material.textile.eventhandler;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.features.material.textile.TextileConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

public class TextileInteractEventHandler {

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        EntityItem entityItem = event.item;
        EntityPlayer player = event.entityPlayer;

        if (TextileConfig.enableCottonBollAutoConversion) {
            // When harvesting cotton, return unrefined cotton boll instead
            if (entityItem.getEntityItem().getItem() == TFCItems.cotton) {
                convertPickedItem(entityItem, player, BidsItems.cottonBoll);
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

    @SubscribeEvent
    public void onUseItemStart(PlayerUseItemEvent.Start event) {
        if (TextileConfig.preventRopeMakingByRightClickingFibers) {
            if (event.item.getItem() == TFCItems.flaxFiber ||
                event.item.getItem() == TFCItems.juteFiber ||
                event.item.getItem() == TFCItems.sisalFiber) {
                event.setCanceled(true);
            }
        }
    }

}
