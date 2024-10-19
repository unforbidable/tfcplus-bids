package com.unforbidable.tfc.bids.Core.Stone;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class StoneBlockGroup {

    public static final StoneBlockGroup RAW_STONE = new StoneBlockGroup(TFCBlocks.stoneSed, TFCBlocks.stoneMM, TFCBlocks.stoneIgIn, TFCBlocks.stoneIgEx);
    public static final StoneBlockGroup SMOOTH_STONE = new StoneBlockGroup(TFCBlocks.stoneSedSmooth, TFCBlocks.stoneMMSmooth, TFCBlocks.stoneIgInSmooth, TFCBlocks.stoneIgExSmooth);
    public static final StoneBlockGroup ROUGH_STONE = new StoneBlockGroup(BidsBlocks.roughStoneSed, BidsBlocks.roughStoneMM, BidsBlocks.roughStoneIgIn, BidsBlocks.roughStoneIgEx);
    public static final StoneBlockGroup ROUGH_STONE_BRICK = new StoneBlockGroup(BidsBlocks.roughStoneBrickSed, BidsBlocks.roughStoneBrickMM, BidsBlocks.roughStoneBrickIgIn, BidsBlocks.roughStoneBrickIgEx);
    public static final StoneBlockGroup ROUGH_STONE_TILE = new StoneBlockGroup(BidsBlocks.roughStoneTileSed, BidsBlocks.roughStoneTileMM, BidsBlocks.roughStoneTileIgIn, BidsBlocks.roughStoneTileIgEx);
    public static final StoneBlockGroup SADDLE_QUERN = new StoneBlockGroup(BidsBlocks.saddleQuernBaseSed, null, null, null);
    public static final StoneBlockGroup HAND_STONE = new StoneBlockGroup(BidsBlocks.saddleQuernHandstoneSed, null, null, null);
    public static final StoneBlockGroup PRESSING_STONE = new StoneBlockGroup(BidsBlocks.saddleQuernPressingStoneSed, null, null, null);
    public static final StoneBlockGroup WEIGHT_STONE = new StoneBlockGroup(BidsBlocks.stonePressWeightSed, null, null, null);
    public static final StoneBlockGroup STONE_BRICKS = new StoneBlockGroup(TFCBlocks.stoneSedBrick, TFCBlocks.stoneMMBrick, TFCBlocks.stoneIgInBrick, TFCBlocks.stoneIgExBrick);
    public static final StoneBlockGroup STONE_BRICK_CHIMNEY = new StoneBlockGroup(16, TFCBlocks.chimneyBricks, TFCBlocks.chimneyBricks2);
    public static final StoneBlockGroup STONE_LARGE_BRICKS = new StoneBlockGroup(TFCBlocks.stoneSedLargeBrick, TFCBlocks.stoneMMLargeBrick, TFCBlocks.stoneIgInLargeBrick, TFCBlocks.stoneIgExLargeBrick);
    public static final StoneBlockGroup STONE_LARGE_BRICK_CHIMNEY = new StoneBlockGroup(16, TFCBlocks.chimneyLargeBricks, TFCBlocks.chimneyLargeBricks2);
    public static final StoneBlockGroup MUD_BRICK = new StoneBlockGroup(16, TFCBlocks.mudBricks, TFCBlocks.mudBricks2);
    public static final StoneBlockGroup MUD_BRICK_CHIMNEY = new StoneBlockGroup(16, BidsBlocks.mudBrickChimney, BidsBlocks.mudBrickChimney2);
    public static final StoneBlockGroup ROUGH_BRICK_FENCE = new StoneBlockGroup(BidsBlocks.roughStoneBrickFenceSed, BidsBlocks.roughStoneBrickFenceMM, BidsBlocks.roughStoneBrickFenceIgIn, BidsBlocks.roughStoneBrickFenceIgEx);
    public static final StoneBlockGroup ROUGH_TILE_FENCE = new StoneBlockGroup(BidsBlocks.roughStoneTileFenceSed, BidsBlocks.roughStoneTileFenceMM, BidsBlocks.roughStoneTileFenceIgIn, BidsBlocks.roughStoneTileFenceIgEx);

    private final Block sed;
    private final Block mm;
    private final Block igex;
    private final Block igin;

    private final int stride;
    private final Block[] blocks;

    public StoneBlockGroup(Block sed, Block mm, Block igex, Block igin) {
        this.sed = sed;
        this.mm = mm;
        this.igex = igex;
        this.igin = igin;
        this.stride = 0;
        this.blocks = null;
    }

    public StoneBlockGroup(int stride, Block... blocks) {
        this.sed = null;
        this.mm = null;
        this.igex = null;
        this.igin = null;
        this.stride = stride;
        this.blocks = blocks;
    }
    public ItemStack getBlockStack(int damage, int stackSize) {
        if (igin != null && damage >= Global.STONE_IGIN_START && damage < Global.STONE_IGIN_START + Global.STONE_IGIN.length) {
            return new ItemStack(igin, stackSize, damage - Global.STONE_IGIN_START);
        } else if (sed != null && damage >= Global.STONE_SED_START && damage < Global.STONE_SED_START + Global.STONE_SED.length) {
            return new ItemStack(sed, stackSize, damage - Global.STONE_SED_START);
        } else if (igex != null && damage >= Global.STONE_IGEX_START && damage < Global.STONE_IGEX_START + Global.STONE_IGEX.length) {
            return new ItemStack(igex, stackSize, damage - Global.STONE_IGEX_START);
        } else if (mm != null && damage >= Global.STONE_MM_START && damage < Global.STONE_MM_START + Global.STONE_MM.length) {
            return new ItemStack(mm, stackSize, damage - Global.STONE_MM_START);
        } else if (blocks != null) {
            int i = damage % stride;
            int j = damage / stride;
            if (j <= blocks.length) {
                return new ItemStack(blocks[j], stackSize, i);
            }
        }

        return null;
    }

}
