package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class CarvingStoneLargeBrick implements ICarving {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == TFCBlocks.stoneSedLargeBrick || block == TFCBlocks.stoneIgInLargeBrick || block == TFCBlocks.stoneIgExLargeBrick || block == TFCBlocks.stoneMMLargeBrick;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata, int equipmentTier) {
        return block == TFCBlocks.stoneSedLargeBrick || equipmentTier > 0;
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
        Block smooth = getSmoothStoneBlock(block, metadata);
        return new ItemStack[] {
            new ItemStack(smooth, 1, metadata),
            new ItemStack(smooth, 1, metadata),
            new ItemStack(smooth, 1, metadata),
            new ItemStack(smooth, 1, metadata),
            new ItemStack(smooth, 1, metadata)
        };
    }

    protected Block getSmoothStoneBlock(Block block, int metadata) {
        if (block == TFCBlocks.stoneSedLargeBrick) {
            return TFCBlocks.stoneSedSmooth;
        } else if (block == TFCBlocks.stoneIgInLargeBrick) {
            return TFCBlocks.stoneIgInSmooth;
        } else if (block == TFCBlocks.stoneIgExLargeBrick) {
            return TFCBlocks.stoneIgExSmooth;
        } else {
            return TFCBlocks.stoneMMSmooth;
        }
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
