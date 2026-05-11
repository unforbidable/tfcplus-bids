package com.unforbidable.tfc.bids.features.building.carving.main.carvable;

import com.unforbidable.tfc.bids.features.building.logwall.block.BlockLogWallVert;
import net.minecraft.block.Block;

public class CarvableLogWallVert extends CarvableLogWall {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block instanceof BlockLogWallVert;
    }

    @Override
    protected int getItemDamage(Block block, int metadata) {
        return ((BlockLogWallVert)block).getOffset() + metadata;
    }

}
