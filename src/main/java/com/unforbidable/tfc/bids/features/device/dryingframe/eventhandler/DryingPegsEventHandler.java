package com.unforbidable.tfc.bids.features.device.dryingframe.eventhandler;

import com.unforbidable.tfc.bids.api.features.surfaceitem.SurfaceItemEvent;
import com.unforbidable.tfc.bids.features.building.peg.block.BlockPeg;
import com.unforbidable.tfc.bids.features.device.dryingframe.main.DryingPegsHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

public class DryingPegsEventHandler {

    @SubscribeEvent
    public void onSurfaceItemPlace(SurfaceItemEvent.Place event) {
        if (!event.placed && DryingPegsHelper.canPlayerDryItem(event.player, event.itemStack)) {
            Block block = event.world.getBlock(event.x, event.y, event.z);
            if (block instanceof BlockPeg) {
                ForgeDirection dir = DryingPegsHelper.findValidDryingPegsNeighbor(event.world, event.x, event.y, event.z, event.face);
                if (dir != ForgeDirection.UNKNOWN) {
                    if (!event.world.isRemote) {
                        DryingPegsHelper.placeDryingPegs(event.world, event.x + dir.offsetX, event.y, event.z + dir.offsetZ, event.itemStack, event.player);
                    }

                    event.player.getHeldItem().stackSize--;

                    event.placed = true;
                }
            }
        }

    }

}
