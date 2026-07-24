package com.unforbidable.tfc.bids.features.food.milk.eventhandler;

import com.dunk.tfc.Items.Tools.ItemCustomBucketMilk;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.util.fluid.FillContainerEvent;
import com.unforbidable.tfc.bids.core.drink.FluidHelper;
import com.unforbidable.tfc.bids.features.food.milk.MilkConfig;
import com.unforbidable.tfc.bids.features.food.milk.main.MilkHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.oredict.OreDictionary;

public class MilkingInteractHandler {

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
        return MilkConfig.enableDefaultMilkingInteractionOverride ||
            MilkHelper.alwaysOverrideMilkingInteraction(entityPlayer, target);
    }

    private boolean isValidMilkingContainer(ItemStack heldItem) {
        for (ItemStack ore : OreDictionary.getOres("itemMilkingContainer")) {
            if (OreDictionary.itemMatches(ore, heldItem, false)) {
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

}
