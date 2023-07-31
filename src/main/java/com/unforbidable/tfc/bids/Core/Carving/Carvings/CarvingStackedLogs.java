package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import java.util.Random;

import com.dunk.tfc.Blocks.Flora.BlockStackedLogHoriz;
import com.dunk.tfc.Blocks.Flora.BlockStackedLogVert;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CarvingStackedLogs implements ICarving {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block instanceof BlockStackedLogVert || block instanceof BlockStackedLogHoriz;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata, int equipmentTier) {
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
        int damage = block.damageDropped(metadata) * 2;
        return new ItemStack[] {
                new ItemStack(TFCItems.logs, 1, damage),
                new ItemStack(TFCItems.logs, 1, damage),
                new ItemStack(TFCItems.logs, 1, damage),
                new ItemStack(TFCItems.logs, 1, damage)
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
