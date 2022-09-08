package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import java.util.Random;

import com.unforbidable.tfc.bids.Blocks.BlockLogWallVert;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CarvingLogWallVert implements ICarving {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block instanceof BlockLogWallVert;
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
    public Block getCarvingBlock(Block block, int metadata) {
        return BidsBlocks.carvingWood;
    }

    @Override
    public ItemStack[] getCarvingHarvest(Block block, int metadata, Random random) {
        final int damage = ((BlockLogWallVert) block).getOffset() + metadata;
        return new ItemStack[] {
                new ItemStack(BidsItems.peeledLogSeasoned, 1, damage),
                new ItemStack(BidsItems.peeledLogSeasoned, 1, damage)
        };
    }

    @Override
    public ItemStack getCarvingExtraHarvest(Block block, int metadata, Random random, float bitRatio) {
        return null;
    }

    @Override
    public String getCarvingSoundEffect() {
        return "dig.wood";
    }

}
