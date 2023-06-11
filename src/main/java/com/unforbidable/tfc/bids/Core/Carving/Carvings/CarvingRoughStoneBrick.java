package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import java.util.Random;

import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CarvingRoughStoneBrick implements ICarving {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == BidsBlocks.roughStoneBrickSed;
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
        ItemStack[] list = new ItemStack[2];
        for (int i = 0; i < 2; i++)
            list[i] = new ItemStack(BidsItems.roughStoneBrick, 1, Global.STONE_SED_START + metadata);
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

}
