package com.unforbidable.tfc.bids.features.device.dryingframe.eventhandler;

import com.dunk.tfc.Blocks.BlockWoodenSpear;
import com.dunk.tfc.Blocks.Flora.BlockBranch;
import com.dunk.tfc.Blocks.Vanilla.BlockTFCFence;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.features.drying.DryingPegsAnchorEvent;
import com.unforbidable.tfc.bids.api.features.surfaceitem.SurfaceItemEvent;
import com.unforbidable.tfc.bids.features.device.dryingframe.main.DryingPegsHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class DryingPegsEventHandler {

    @SubscribeEvent
    public void onSurfaceItemPlace(SurfaceItemEvent.Place event) {
        if (!event.placed && DryingPegsHelper.canPlayerDryItem(event.player, event.itemStack)) {
            if (DryingPegsHelper.isBlockDryingPegAnchor(event.world, event.x, event.y, event.z)) {
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

    @SubscribeEvent
    public void onDryingPegsAnchorAttach(DryingPegsAnchorEvent.Attach event) {
        if (event.block instanceof BlockTFCFence) {
            event.canAttach = true;
        } else if (event.block instanceof BlockBranch) {
            // Only tree trunk
            event.canAttach = ((BlockBranch) event.block).getSourceX() == 0 && ((BlockBranch) event.block).getSourceZ() == 0;
        } else if (event.block == TFCBlocks.shrub) {
            event.canAttach = true;
        } else if (event.block instanceof BlockWoodenSpear) {
            event.canAttach = true;
        }
    }

    private final static AxisAlignedBB fencePoleBounds = AxisAlignedBB.getBoundingBox(0.375F, 0, 0.375F,
        0.625F, 1, 0.625F);
    private final static AxisAlignedBB treeTrunkBounds = AxisAlignedBB.getBoundingBox(0.45F, 0, 0.45F,
        0.55F, 1, 0.55F);
    private final static AxisAlignedBB spearBounds = AxisAlignedBB.getBoundingBox(0.45F, 0, 0.45F,
        0.55F, 1, 0.55F);
    private final static AxisAlignedBB shrubBounds = AxisAlignedBB.getBoundingBox(0.475F, 0, 0.475F,
        0.525F, 1, 0.525F);

    @SubscribeEvent
    public void onDryingPegsAnchorBounds(DryingPegsAnchorEvent.Bounds event) {
        if (event.block instanceof BlockTFCFence) {
            event.knotBounds = fencePoleBounds;
        } else if (event.block instanceof BlockBranch) {
            // For now the anchor renders inside the tree trunk - isn't visible
            // TODO calculate variable trunk width based on Branch
            event.knotBounds = treeTrunkBounds;
        } else if (event.block instanceof BlockWoodenSpear) {
            event.knotBounds = spearBounds;
        } else if (event.block == TFCBlocks.shrub) {
            event.knotBounds = shrubBounds;
        }
    }

}
