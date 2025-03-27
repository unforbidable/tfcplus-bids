package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.TileEntities.TileEntityProcessingSurface;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceManager;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceRecipe;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ProcessingSurfaceCraftingHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.world.isRemote) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (event.face == 1) {
                    if (event.entityPlayer.getHeldItem() != null && isItemAllowed(event.entityPlayer.getHeldItem())) {
                        ProcessingSurfaceRecipe recipe = ProcessingSurfaceManager.findMatchingRecipe(event.entityPlayer.getHeldItem(), event.world, event.x, event.y, event.z);
                        if (recipe != null) {
                            Bids.LOG.debug("Found matching ProcessingSurfaceRecipe");

                            if (event.world.setBlock(event.x, event.y + 1, event.z, BidsBlocks.processingSurface, 0, 2)) {
                                TileEntityProcessingSurface te = (TileEntityProcessingSurface) event.world.getTileEntity(event.x, event.y + 1, event.z);
                                ItemStack heldItem = event.entityPlayer.getHeldItem().copy();
                                heldItem.stackSize = 1;
                                te.setInputItem(heldItem);
                                event.entityPlayer.getHeldItem().stackSize--;

                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isItemAllowed(ItemStack heldItem) {
        // Only allow soaked hides to be scrapped if enabled in the config
        return BidsOptions.Crafting.enableProcessingSurfaceLeatherRackOverride ||
            heldItem.getItem() != TFCItems.soakedHide;
    }

}
