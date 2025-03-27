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
    public static final WoodBlockGroup THICK_LOG = new WoodBlockGroup(8, 0, TFCBlocks.woodHoriz, TFCBlocks.woodHoriz2, TFCBlocks.woodHoriz3, TFCBlocks.woodHoriz4, TFCBlocks.woodHoriz5, TFCBlocks.woodHoriz6);
    public static final WoodBlockGroup THICK_LOG_ALT = new WoodBlockGroup(8, 1, TFCBlocks.woodHoriz, TFCBlocks.woodHoriz2, TFCBlocks.woodHoriz3, TFCBlocks.woodHoriz4, TFCBlocks.woodHoriz5, TFCBlocks.woodHoriz6);
    public static final WoodBlockGroup STACKED_LOGS = new WoodBlockGroup(8, 0, TFCBlocks.stackedWoodHoriz, TFCBlocks.stackedWoodHoriz2, TFCBlocks.stackedWoodHoriz3, TFCBlocks.stackedWoodHoriz4, TFCBlocks.stackedWoodHoriz5, TFCBlocks.stackedWoodHoriz6);
    public static final WoodBlockGroup STACKED_LOGS_ALT = new WoodBlockGroup(8, 1,  TFCBlocks.stackedWoodHoriz, TFCBlocks.stackedWoodHoriz2, TFCBlocks.stackedWoodHoriz3, TFCBlocks.stackedWoodHoriz4, TFCBlocks.stackedWoodHoriz5, TFCBlocks.stackedWoodHoriz6);

    private final int stride;
    private final int offset;
    private final Block[] blocks;

    public WoodBlockGroup(int stride, Block... blocks) {
        this(stride, 0, blocks);
    }

    public WoodBlockGroup(int stride, int offset, Block... blocks) {
        this.stride = stride;
        this.offset = offset;
        this.blocks = blocks;
    }

    public ItemStack getBlockStack(int index, int stackSize) {
        return new ItemStack(blocks[index / stride], stackSize, (index % stride) * (16 / stride) + offset);
    }

}
