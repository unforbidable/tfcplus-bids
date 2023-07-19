package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class CarvingWoodVert implements ICarving {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == TFCBlocks.woodVert || block == TFCBlocks.woodVert2 || block == TFCBlocks.woodVert3;
    }

    @Override
    public boolean canCarveBlockWithTool(Block block, int metadata, ICarvingTool tool) {
        return true;
    }

    @Override
    public boolean canCarveBlockAt(Block block, int metadata, World world, int x, int y, int z, int side) {
        return world.isAirBlock(x, y + 1, z) ||
            world.getTileEntity(x, y + 1, z) instanceof TileEntityCarving;
    }

    @Override
    public Block getCarvingBlock(Block block, int metadata) {
        return BidsBlocks.carvingWood;
    }

    @Override
    public ItemStack[] getCarvingHarvest(Block block, int metadata, Random random) {
        return null;
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
