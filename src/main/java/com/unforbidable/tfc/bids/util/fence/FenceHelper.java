package com.unforbidable.tfc.bids.util.fence;

import com.dunk.tfc.Blocks.BlockWattle;
import com.dunk.tfc.Blocks.Vanilla.BlockCustomWall;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.util.fence.ConnectableFence;
import com.unforbidable.tfc.bids.api.util.fence.ConnectableFenceGate;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class FenceHelper {

    public static boolean isFenceBlock(Block block) {
        return block instanceof ConnectableFence
            // all TFC walls and wattle - since they will return the connection
            || block instanceof BlockCustomWall || block instanceof BlockWattle;
    }

    public static boolean isFenceGateBlock(Block block) {
        return block instanceof ConnectableFenceGate ||
            // all TFC fence gates are also connectable
            block == TFCBlocks.fenceGate || block == TFCBlocks.fenceGate2 || block == TFCBlocks.fenceGate3 || block == Blocks.fence_gate;
    }

}
