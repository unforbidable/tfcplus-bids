package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class CarvingPlanks implements ICarving {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == TFCBlocks.planks || block == TFCBlocks.planks2 || block == TFCBlocks.planks3;
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
        int damage = getSinglePlankDamage(block, metadata);
        return new ItemStack[] {
            new ItemStack(TFCItems.singlePlank, 1, damage),
            new ItemStack(TFCItems.singlePlank, 1, damage),
            new ItemStack(TFCItems.singlePlank, 1, damage),
            new ItemStack(TFCItems.singlePlank, 1, damage)
        };
    }

    protected int getSinglePlankDamage(Block block, int metadata) {
        if (block == TFCBlocks.planks) {
            return metadata;
        } else if (block == TFCBlocks.planks2) {
            return metadata + 16;
        } else {
            return metadata + 32;
        }
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
