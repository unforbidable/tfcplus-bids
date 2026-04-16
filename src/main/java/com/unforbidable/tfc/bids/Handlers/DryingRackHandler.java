package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.Drying.DryingHelper;
import com.unforbidable.tfc.bids.Core.Drying.DryingItem;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackHelper;
import com.unforbidable.tfc.bids.Items.ItemDryingMudBrick;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingSurface;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Events.DryingItemEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class DryingRackHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.world.isRemote) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (event.face > 1) {
                    if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == TFCItems.pole && event.entityPlayer.getHeldItem().stackSize > 1) {
                        ForgeDirection dir = ForgeDirection.getOrientation(event.face);
                        if (DryingRackHelper.canPlaceDryingRackAt(event.world, event.x, event.y, event.z, dir)) {
                            DryingRackHelper.placeDryingRackFromItemsAt(event.entityPlayer.getHeldItem(), event.entityPlayer,
                                event.world, event.x, event.y, event.z, dir);

                            event.setCanceled(true);
                        }
                    }
                }
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
