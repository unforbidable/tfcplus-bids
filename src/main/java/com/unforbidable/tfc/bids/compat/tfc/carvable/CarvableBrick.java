package com.unforbidable.tfc.bids.compat.tfc.carvable;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.carving.Carvable;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CarvableBrick implements Carvable {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == TFCBlocks.bricks;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata, int equipmentTier) {
        return equipmentTier > 0;
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
        return new ItemStack[] { new ItemStack(TFCBlocks.bricks, 1, 0) };
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
