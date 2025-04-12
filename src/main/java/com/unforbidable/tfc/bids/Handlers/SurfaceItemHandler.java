package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Reference;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Common.Metadata.DecorativeSurfaceMetadata;
import com.unforbidable.tfc.bids.Core.ProcessingSurface.ProcessingSurfaceHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDecorativeSurface;
import com.unforbidable.tfc.bids.TileEntities.TileEntityProcessingSurface;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceManager;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.Events.ProcessingSurfaceEvent;
import com.unforbidable.tfc.bids.api.Events.SurfaceItemEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.OreDictionary;

public class SurfaceItemHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.entityPlayer.getHeldItem() != null) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (!event.world.isRemote) {
                    if (tryPlaceProcessingSurface(event.entityPlayer.getHeldItem(), event.world, event.x, event.y, event.z, event.face, event.entityPlayer) ||
                        tryPlaceDecorativeSurface(event.entityPlayer.getHeldItem(), event.world, event.x, event.y, event.z, event.face, event.entityPlayer)) {
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
                        if (canPlaceProcessingSurface(event.entityPlayer.getHeldItem(), event.world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, event.entityPlayer) ||
                            canPlaceDecorativeSurface(event.entityPlayer.getHeldItem(), event.world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, event.entityPlayer)) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    private boolean canPlaceProcessingSurface(ItemStack itemStack, World world, int x, int y, int z, int face, EntityPlayer player) {
        if (isItemAllowed(itemStack) && face == 1) {
            ProcessingSurfaceRecipe recipe = ProcessingSurfaceManager.findMatchingRecipe(itemStack, world, x, y, z);
            return recipe != null;
        }

        return false;
    }

    private boolean canPlaceDecorativeSurface(ItemStack itemStack, World world, int x, int y, int z, int face, EntityPlayer player) {
        if (isItemAllowed(itemStack) && face > 0) {
            if (isDecorativeSurfaceItem(itemStack)) {
                ForgeDirection dir = ForgeDirection.getOrientation(face);
                int x2 = x + dir.offsetX;
                int y2 = y + dir.offsetY;
                int z2 = z + dir.offsetZ;

                if (world.isAirBlock(x2, y2, z2)) {
                    Block block = world.getBlock(x, y, z);
                    return block.isSideSolid(world, x, y, z, dir);
                }
            }
        }

        return false;
    }

    private boolean tryPlaceProcessingSurface(ItemStack itemStack, World world, int x, int y, int z, int face, EntityPlayer player) {
        if (isItemAllowed(itemStack) && face == 1) {
            ProcessingSurfaceRecipe recipe = ProcessingSurfaceManager.findMatchingRecipe(itemStack, world, x, y, z);
            if (recipe != null) {
                Bids.LOG.debug("Found matching ProcessingSurfaceRecipe");

                if (world.isAirBlock(x, y + 1, z) &&
                    world.setBlock(x, y + 1, z, BidsBlocks.processingSurface, 0, 2)) {
                    TileEntityProcessingSurface te = (TileEntityProcessingSurface) world.getTileEntity(x, y + 1, z);
                    ItemStack heldItem = itemStack.copy();
                    heldItem.stackSize = 1;
                    te.setInputItem(heldItem);
                    player.getHeldItem().stackSize--;

                    return true;
                }
            }
        }

        return false;
    }

    private boolean tryPlaceDecorativeSurface(ItemStack itemStack, World world, int x, int y, int z, int face, EntityPlayer player) {
        if (isItemAllowed(itemStack) && face > 0) {
            if (isDecorativeSurfaceItem(itemStack)) {
                ForgeDirection dir = ForgeDirection.getOrientation(face);
                int x2 = x + dir.offsetX;
                int y2 = y + dir.offsetY;
                int z2 = z + dir.offsetZ;

                if (world.isAirBlock(x2, y2, z2)) {
                    Block block = world.getBlock(x, y, z);
                    if (block.isSideSolid(world, x, y, z, dir)) {
                        // meta is player's orientation (angle) for vertical placement
                        // and forge direction (face) for horizontal placement
                        DecorativeSurfaceMetadata meta = face == 1
                            ? DecorativeSurfaceMetadata.forHorizontalOrientation(ProcessingSurfaceHelper.getOrientation(player))
                            : DecorativeSurfaceMetadata.forVerticalFace(dir);

                        if (world.setBlock(x2, y2, z2, BidsBlocks.decorativeSurface, meta.getMetadata(), 2)) {
                            TileEntityDecorativeSurface te = (TileEntityDecorativeSurface) world.getTileEntity(x2, y2, z2);
                            ItemStack heldItem = itemStack.copy();
                            heldItem.stackSize = 1;
                            te.setItem(heldItem);
                            player.getHeldItem().stackSize--;

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean isDecorativeSurfaceItem(ItemStack itemStack) {
        int decorativeSurfaceItemOreId = OreDictionary.getOreID("itemDecorativeSurface");
        for (int oreId : OreDictionary.getOreIDs(itemStack)) {
            if (oreId == decorativeSurfaceItemOreId) {
                return true;
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
