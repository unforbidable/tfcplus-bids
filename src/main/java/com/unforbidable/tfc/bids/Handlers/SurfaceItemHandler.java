package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Reference;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.BidsRegistry;
import com.unforbidable.tfc.bids.api.Events.ProcessingSurfaceEvent;
import com.unforbidable.tfc.bids.api.Events.SurfaceItemEvent;
import com.unforbidable.tfc.bids.api.Interfaces.ISurfaceItemPlacer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.OreDictionary;

public class SurfaceItemHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.entityPlayer.getHeldItem() != null) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (!event.world.isRemote) {
                    if (placeSurfaceItem(event.world, event.x, event.y, event.z, event.face, event.entityPlayer)) {
                        event.setCanceled(true);
                    }
                }
            } else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
                // After placing surface item above,
                // to prevent right-clicking with the remaining stack
                // this event needs to be cancelled too
                if (event.world.isRemote) {
                    MovingObjectPosition mop = Helper.getMouseOverObject(event.entityPlayer, event.world);
                    if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        if (placeSurfaceItem(event.world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, event.entityPlayer)) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    private boolean placeSurfaceItem(World world, int x, int y, int z, int face, EntityPlayer entityPlayer) {
        if (isItemAllowed(entityPlayer.getHeldItem())) {
            for (ISurfaceItemPlacer placer : BidsRegistry.SURFACE_PLACERS) {
                if (placer.placeItemOnSurface(world, x, y, z, face, entityPlayer)) {
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

    @SubscribeEvent
    public void onSurfaceItemIcon(SurfaceItemEvent.Icon event) {
        if (event.itemStack.getItem() == TFCItems.soakedHide) {
            event.iconName = Reference.MOD_ID + ":" + "Soaked Hide";
        } else if (event.itemStack.getItem() == TFCItems.scrapedHide) {
            event.iconName = Reference.MOD_ID + ":" + "Scraped Hide";
        }
    }

}
