package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class CarvingStoneBrick implements ICarving {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == TFCBlocks.stoneSedBrick || block == TFCBlocks.stoneIgInBrick || block == TFCBlocks.stoneIgExBrick || block == TFCBlocks.stoneMMBrick;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata, int equipmentTier) {
        return block == TFCBlocks.stoneSedBrick || equipmentTier > 0;
    }

    @Override
    public boolean canCarveBlockAt(Block block, int metadata, World world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public Block getCarvingBlock(Block block, int metadata) {
        return BidsBlocks.carvingRock;
    }

    @Override
    public ItemStack[] getCarvingHarvest(Block block, int metadata, Random random) {
        return new ItemStack[] {
            new ItemStack(block, 1, metadata)
        };
    }

    @Override
    public ItemStack getCarvingExtraHarvest(Block block, int metadata, Random random, float bitRatio) {
        return null;
    }

    @Override
    public String getCarvingSoundEffect() {
        return "dig.stone";
    }

}
