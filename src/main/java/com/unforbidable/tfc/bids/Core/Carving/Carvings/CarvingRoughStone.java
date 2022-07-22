package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import java.util.Random;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class CarvingRoughStone implements ICarving {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == BidsBlocks.roughStoneSed;
    }

    @Override
    public boolean canCarveBlockWithTool(Block block, int metadata, ICarvingTool tool) {
        return true;
    }

    @Override
    public ItemStack[] getCarvingHarvest(Block block, int metadata, Random random) {
        final int n = random.nextInt(2) + 1;
        ItemStack[] list = new ItemStack[n];
        for (int i = 0; i < n; i++)
            list[i] = new ItemStack(TFCItems.looseRock, 1, metadata + Global.STONE_SED_START);
        return list;
    }

    @Override
    public ItemStack getCarvingExtraHarvest(Block block, int metadata, Random random, float bitRatio) {
        return random.nextDouble() < 2 * bitRatio
                ? new ItemStack(TFCItems.looseRock, 1, metadata + Global.STONE_SED_START)
                : null;
    }

    @Override
    public Block getCarvingBlock(Block block, int metadata) {
        return BidsBlocks.carvingRock;
    }

    @Override
    public String getCarvingSoundEffect() {
        return "dig.stone";
    }

}
