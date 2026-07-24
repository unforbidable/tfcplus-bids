package com.unforbidable.tfc.bids.features.building.decorativesurface.eventhandler;

import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.surfaceitem.SurfaceItemEvent;
import com.unforbidable.tfc.bids.features.building.decorativesurface.main.DecorativeSurfaceHelper;
import com.unforbidable.tfc.bids.features.building.decorativesurface.main.DecorativeSurfaceMetadata;
import com.unforbidable.tfc.bids.features.building.decorativesurface.tileentity.TileEntityDecorativeSurface;
import com.unforbidable.tfc.bids.features.device.processingsurface.main.ProcessingSurfaceHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class DecorativeSurfaceEventHandler {

    @SubscribeEvent
    public void onSurfaceItemPlace(SurfaceItemEvent.Place event) {
        if (!event.placed && event.player.isSneaking() && event.face > 0) {
            if (DecorativeSurfaceHelper.isDecorativeSurfaceItem(event.player.getHeldItem())) {
                ForgeDirection dir = ForgeDirection.getOrientation(event.face);
                int x2 = event.x + dir.offsetX;
                int y2 = event.y + dir.offsetY;
                int z2 = event.z + dir.offsetZ;

                if (event.world.isAirBlock(x2, y2, z2)) {
                    Block block = event.world.getBlock(event.x, event.y, event.z);
                    if (block.isSideSolid(event.world, event.x, event.y, event.z, dir)) {
                        // meta is player's orientation (angle) for vertical placement
                        // and forge direction (face) for horizontal placement
                        DecorativeSurfaceMetadata meta = event.face == 1
                            ? DecorativeSurfaceMetadata.forHorizontalOrientation(ProcessingSurfaceHelper.getOrientation(event.player))
                            : DecorativeSurfaceMetadata.forVerticalFace(dir);

                        if (!event.world.isRemote) {
                            event.world.setBlock(x2, y2, z2, BidsBlocks.decorativeSurface, meta.getMetadata(), 2);
                            TileEntityDecorativeSurface te = (TileEntityDecorativeSurface) event.world.getTileEntity(x2, y2, z2);
                            ItemStack heldItem = event.player.getHeldItem().copy();
                            heldItem.stackSize = 1;
                            te.setItem(heldItem);
                            event.player.getHeldItem().stackSize--;
                        }

                        event.placed = true;
                    }
                }
            }
        }
    }

}
