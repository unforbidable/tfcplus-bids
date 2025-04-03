package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Reference;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityProcessingSurface;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceManager;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.Events.ProcessingSurfaceEvent;
import com.unforbidable.tfc.bids.api.Events.SurfaceItemEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.OreDictionary;

public class ProcessingSurfaceCraftingHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.world.isRemote) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (event.entityPlayer.getHeldItem() != null && isItemAllowed(event.entityPlayer.getHeldItem())) {
                    if (tryPlaceProcessingSurface(event.entityPlayer.getHeldItem(), event.world, event.x, event.y, event.z, event.face, event.entityPlayer)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    private boolean tryPlaceProcessingSurface(ItemStack itemStack, World world, int x, int y, int z, int face, EntityPlayer player) {
        if (face == 1) {
            ProcessingSurfaceRecipe recipe = ProcessingSurfaceManager.findMatchingRecipe(itemStack, world, x, y, z);
            if (recipe != null) {
                Bids.LOG.debug("Found matching ProcessingSurfaceRecipe");

                if (world.setBlock(x, y + 1, z, BidsBlocks.processingSurface, 0, 2)) {
                    TileEntityProcessingSurface te = (TileEntityProcessingSurface) world.getTileEntity(x, y + 1, z);
                    ItemStack heldItem = player.getHeldItem().copy();
                    heldItem.stackSize = 1;
                    te.setInputItem(heldItem);
                    player.getHeldItem().stackSize--;

                    return true;
                }
            }
        }

        return false;
    }

    private boolean isItemAllowed(ItemStack heldItem) {
        // Only allow soaked hides to be scrapped if enabled in the config
        return BidsOptions.Crafting.enableProcessingSurfaceLeatherRackOverride ||
            heldItem.getItem() != TFCItems.soakedHide;
    }

    @SubscribeEvent
    public void onProcessingSurfaceToolEfficiencyCheck(ProcessingSurfaceEvent.ToolEfficiencyCheck event) {
        if (event.tool != null) {
            int primitiveToolId = OreDictionary.getOreID("itemPrimitiveTool");
            for (int id : OreDictionary.getOreIDs(event.tool)) {
                if (id == primitiveToolId) {
                    // Primitive tools have reduced efficiency
                    event.efficiency *= 0.5;
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void onSurfaceItemIcon(SurfaceItemEvent.Icon event) {
        if (event.itemStack.getItem() == TFCItems.soakedHide) {
            event.iconName = Reference.MOD_ID + ":" + "Soaked Hide";
        } else if (event.itemStack.getItem() == TFCItems.scrapedHide) {
            event.iconName = Reference.MOD_ID + ":" + "Scraped Hide";
        }
    }

}
