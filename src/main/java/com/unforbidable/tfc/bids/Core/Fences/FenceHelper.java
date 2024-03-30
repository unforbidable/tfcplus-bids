package com.unforbidable.tfc.bids.Core.Fences;

import com.dunk.tfc.Blocks.BlockWattle;
import com.dunk.tfc.Blocks.Vanilla.BlockCustomWall;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.IConnectableFence;
import com.unforbidable.tfc.bids.api.Interfaces.IConnectableFenceGate;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class FenceHelper {

    public static boolean isFenceBlock(Block block) {
        return block instanceof IConnectableFence
            // all TFC walls and wattle - since they will return the connection
            || block instanceof BlockCustomWall || block instanceof BlockWattle;
    }

    public static boolean isFenceGateBlock(Block block) {
        return block instanceof IConnectableFenceGate ||
            // all TFC fence gates are also connectable
            block == TFCBlocks.fenceGate || block == TFCBlocks.fenceGate2 || block == TFCBlocks.fenceGate3 || block == Blocks.fence_gate;
    }

}
