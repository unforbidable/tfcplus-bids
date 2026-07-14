package com.unforbidable.tfc.bids.features.device.processingsurface.eventhandler;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Reference;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.processing.ProcessingSurfaceEvent;
import com.unforbidable.tfc.bids.api.features.surfaceitem.SurfaceItemEvent;
import com.unforbidable.tfc.bids.features.device.processingsurface.main.ProcessingSurfaceHelper;
import com.unforbidable.tfc.bids.features.device.processingsurface.tileentity.TileEntityProcessingSurface;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ProcessingSurfaceEventHandler  {

    @SubscribeEvent
    public void onSurfaceItemPlace(SurfaceItemEvent.Place event) {
        if (!event.placed && !event.player.isSneaking() && event.face == 1) {
            if (event.world.isAirBlock(event.x, event.y + 1, event.z)){
                if (ProcessingSurfaceHelper.isValidProcessingSurfaceItem(event.itemStack, event.world, event.x, event.y, event.z)) {
                    Bids.LOG.debug("Found matching ProcessingSurfaceRecipe");

                    if (!event.world.isRemote) {
                        event.world.setBlock(event.x, event.y + 1, event.z, BidsBlocks.processingSurface, 0, 2);
                        TileEntityProcessingSurface te = (TileEntityProcessingSurface) event.world.getTileEntity(event.x, event.y + 1, event.z);
                        ItemStack heldItem = event.itemStack.copy();
                        heldItem.stackSize = 1;
                        te.setInputItem(heldItem);
                        event.itemStack.stackSize--;
                    }

                    event.placed = true;
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

    @SubscribeEvent
    public void onProcessingSurfaceToolEfficiencyCheck(ProcessingSurfaceEvent.ToolEfficiencyCheck event) {
        if (event.tool != null) {
            int primitiveToolId = OreDictionary.getOreID("itemPrimitiveTool");
            for (int id : OreDictionary.getOreIDs(event.tool)) {
                if (id == primitiveToolId) {
                    // Primitive tools have reduced efficiency
                    event.newEfficiency = event.originalEfficiency * 0.5f;
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void onProcessingSurfaceProgress(ProcessingSurfaceEvent.Progress event) {
        if (event.progress == 1f) {
            if (event.result.getItem() == TFCItems.hide) {
                if (event.input.getItem() == TFCItems.wolfFur) {
                    TFC_Core.giveItemToPlayer(new ItemStack(TFCItems.wolfFurHat, 1, 0), event.player);
                } else if (event.input.getItem() == TFCItems.bearFur) {
                    TFC_Core.giveItemToPlayer(new ItemStack(TFCItems.bearFurHat, 1, 0), event.player);
                } else if (event.input.getItem() == TFCItems.sheepSkin) {
                    TFC_Core.giveItemToPlayer(new ItemStack(TFCItems.wool, event.result.getItemDamage() + 1, 0), event.player);
                }
            }
        }
    }

}
