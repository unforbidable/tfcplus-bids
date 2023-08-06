package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class CarvingRoughStoneBrick implements ICarving {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == BidsBlocks.roughStoneBrickSed ||
            block == BidsBlocks.roughStoneBrickMM ||
            block == BidsBlocks.roughStoneBrickIgIn ||
            block == BidsBlocks.roughStoneBrickIgEx;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata, int equipmentTier) {
        return block == BidsBlocks.roughStoneBrickSed || equipmentTier > 0;
    }

    @Override
    public boolean canCarveBlockAt(Block block, int metadata, World world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public ItemStack[] getCarvingHarvest(Block block, int metadata, Random random) {
        ItemStack[] list = new ItemStack[2];
        for (int i = 0; i < 2; i++)
            list[i] = getLooseRoughBrick(block, metadata);
        return list;
    }

    @Override
    public ItemStack getCarvingExtraHarvest(Block block, int metadata, Random random, float bitRatio) {
        return null;
    }

    @Override
    public Block getCarvingBlock(Block block, int metadata) {
        return BidsBlocks.carvingRock;
    }

    @Override
    public String getCarvingSoundEffect() {
        return "dig.stone";
    }

    protected ItemStack getLooseRoughBrick(Block block, int metadata) {
        if (block == BidsBlocks.roughStoneSed) {
            return new ItemStack(BidsItems.roughStoneBrick, 1, metadata + Global.STONE_SED_START);
        } else if (block == BidsBlocks.roughStoneIgIn) {
            return new ItemStack(BidsItems.roughStoneBrick, 1, metadata + Global.STONE_IGIN_START);
        } else if (block == BidsBlocks.roughStoneIgEx) {
            return new ItemStack(BidsItems.roughStoneBrick, 1, metadata + Global.STONE_IGEX_START);
        } else {
            return new ItemStack(BidsItems.roughStoneBrick, 1, metadata + Global.STONE_MM_START);
        }
    }

}
