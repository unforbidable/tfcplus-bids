package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import java.util.Random;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
    public boolean canCarveBlockAt(Block block, int metadata, World world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public ItemStack[] getCarvingHarvest(Block block, int metadata, Random random) {
        final int n = random.nextInt(2) + 1;
        ItemStack[] list = new ItemStack[n];
        for (int i = 0; i < n; i++)
            list[i] = getLooseRock(block, metadata);
        return list;
    }

    @Override
    public ItemStack getCarvingExtraHarvest(Block block, int metadata, Random random, float bitRatio) {
        return random.nextDouble() < 2 * bitRatio
                ? getLooseRock(block, metadata)
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

    protected ItemStack getLooseRock(Block block, int metadata) {
        if (block == BidsBlocks.roughStoneSed) {
            return new ItemStack(TFCItems.looseRock, 1, metadata + Global.STONE_SED_START);
        } else {
            return new ItemStack(TFCItems.looseRock, 1, metadata + Global.STONE_MM_START);
        }
    }

}
