package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import com.dunk.tfc.Blocks.BlockMudBricks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class CarvingMudBrick implements ICarving {
    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block instanceof BlockMudBricks;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata,int equipmentTier) {
        return true;
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
        int damage = block.damageDropped(metadata);
        return new ItemStack[] {
            new ItemStack(TFCItems.mudBrick, 1, damage),
            new ItemStack(TFCItems.mudBrick, 1, damage)
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
