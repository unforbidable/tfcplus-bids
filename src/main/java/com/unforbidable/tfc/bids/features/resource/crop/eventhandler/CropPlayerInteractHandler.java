package com.unforbidable.tfc.bids.features.resource.crop.eventhandler;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.features.resource.crop.CropConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class CropPlayerInteractHandler {

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        EntityItem entityItem = event.item;
        EntityPlayer player = event.entityPlayer;

        if (CropConfig.enableCerealSeedAutoConversion) {
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

        if (CropConfig.enableHardySeedAutoConversion) {
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
