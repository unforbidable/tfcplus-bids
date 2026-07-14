package com.unforbidable.tfc.bids.features.device.dryingsurface.eventhandler;

import com.unforbidable.tfc.bids.api.features.drying.DryingItemEvent;
import com.unforbidable.tfc.bids.api.features.drying.DryingRecipe;
import com.unforbidable.tfc.bids.api.features.surfaceitem.SurfaceItemEvent;
import com.unforbidable.tfc.bids.features.building.mudbrick.item.ItemDryingMudBrick;
import com.unforbidable.tfc.bids.features.crafting.drying.main.DryingHelper;
import com.unforbidable.tfc.bids.features.crafting.drying.main.DryingItem;
import com.unforbidable.tfc.bids.features.device.dryingsurface.main.DryingSurfaceHelper;
import com.unforbidable.tfc.bids.features.device.dryingsurface.tileentity.TileEntityDryingSurface;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;

public class DryingSurfaceEventHandler {

    @SubscribeEvent
    public void onSurfaceItemPlace(SurfaceItemEvent.Place event) {
        if (!event.placed && event.player.isSneaking() && event.face == 1) {
            if (DryingSurfaceHelper.canPlaceDryingItemAt(event.world, event.x, event.y, event.z, event.itemStack)) {
                if (!event.world.isRemote) {
                    ItemStack heldItem = event.itemStack.copy();
                    heldItem.stackSize = 1;

                    if (DryingSurfaceHelper.placeDryingItemAt(event.world, event.x, event.y, event.z, event.hitX, event.hitZ, heldItem)) {
                        event.player.getHeldItem().stackSize--;
                    }
                }

                event.placed = true;
            }

        }
    }

    @SubscribeEvent
    public void onDryingItemNextRecipeSelected(DryingItemEvent.SelectNextRecipe event) {
        if (event.nextDryingRecipe.getInputItem().getItem() instanceof ItemDryingMudBrick) {
            // The recipe for the second stage of mud brick drying is not automatically selected
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onDryingItemActivated(DryingItemEvent.Activate event) {
        if (event.dryingItem.progress == 1 &&
            event.dryingItem.resultItem != null &&
            event.dryingItem.resultItem.getItem() instanceof ItemDryingMudBrick &&
            event.dryingTileEntity instanceof TileEntityDryingSurface) {
            // Manually activate the second stage of mud brick drying

            // Making sure recipe exists
            DryingItem nextDryingItem = new DryingItem();
            nextDryingItem.inputItem = event.dryingItem.resultItem;
            DryingRecipe recipe = ((TileEntityDryingSurface) event.dryingTileEntity).getDryingRecipe(nextDryingItem);
            if (recipe != null) {
                event.dryingItem.inputItem = event.dryingItem.resultItem;
                DryingHelper.initializeInputItem(event.dryingItem, recipe);

                event.dryingTileEntity.markDirty();
                event.dryingTileEntity.getWorldObj().markBlockForUpdate(event.dryingTileEntity.xCoord, event.dryingTileEntity.yCoord, event.dryingTileEntity.zCoord);
                ((TileEntityDryingSurface) event.dryingTileEntity).notifyClientChanges();

                event.handled = true;
            }
        }

    }

}
