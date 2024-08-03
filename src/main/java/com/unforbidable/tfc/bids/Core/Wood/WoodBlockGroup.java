package com.unforbidable.tfc.bids.Core.Wood;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class WoodBlockGroup {

    public final static WoodBlockGroup CHOPPING_BLOCK = new WoodBlockGroup(16, BidsBlocks.choppingBlock, BidsBlocks.choppingBlock2, BidsBlocks.choppingBlock3);
    public final static WoodBlockGroup STACKED_FIREWOOD = new WoodBlockGroup(16, BidsBlocks.stackedFirewood, BidsBlocks.stackedFirewood2, BidsBlocks.stackedFirewood3);
    public final static WoodBlockGroup LOG_WALL = new WoodBlockGroup(16, BidsBlocks.logWallEast, BidsBlocks.logWallEast2, BidsBlocks.logWallEast3);
    public final static WoodBlockGroup LOG_WALL_VERT = new WoodBlockGroup(16, BidsBlocks.logWallVert, BidsBlocks.logWallVert2, BidsBlocks.logWallVert3);
    public final static WoodBlockGroup PALISADE = new WoodBlockGroup(16, BidsBlocks.palisade, BidsBlocks.palisade2, BidsBlocks.palisade3);
    public static final WoodBlockGroup WOOD_VERT = new WoodBlockGroup(16, TFCBlocks.woodVert, TFCBlocks.woodVert2, TFCBlocks.woodVert3);
    public static final WoodBlockGroup WOOD_SUPPORT = new WoodBlockGroup(16, TFCBlocks.woodSupportV, TFCBlocks.woodSupportV2, TFCBlocks.woodSupportV3);
    public static final WoodBlockGroup FENCE = new WoodBlockGroup(16, TFCBlocks.fence, TFCBlocks.fence2, TFCBlocks.fence3);

    private final int stride;
    private final Block[] blocks;

    public WoodBlockGroup(int stride, Block... blocks) {
        this.stride = stride;
        this.blocks = blocks;
    }

    public ItemStack getBlockStack(int index, int stackSize) {
        return new ItemStack(blocks[index / stride], stackSize, index % stride);
    }

}
