package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class CarvingRoughStoneTile implements ICarving {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == BidsBlocks.roughStoneTileSed ||
            block == BidsBlocks.roughStoneTileMM ||
            block == BidsBlocks.roughStoneTileIgIn ||
            block == BidsBlocks.roughStoneTileIgEx;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata, int equipmentTier) {
        return block == BidsBlocks.roughStoneTileSed || equipmentTier > 0;
    }

    @Override
    public boolean canCarveBlockAt(Block block, int metadata, World world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public ItemStack[] getCarvingHarvest(Block block, int metadata, Random random) {
        ItemStack[] list = new ItemStack[2];
        for (int i = 0; i < 2; i++)
            list[i] = getLooseRoughTile(block, metadata);
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

    protected ItemStack getLooseRoughTile(Block block, int metadata) {
        if (block == BidsBlocks.roughStoneTileSed) {
            return new ItemStack(BidsItems.roughStoneTile, 1, metadata + Global.STONE_SED_START);
        } else if (block == BidsBlocks.roughStoneTileIgIn) {
            return new ItemStack(BidsItems.roughStoneTile, 1, metadata + Global.STONE_IGIN_START);
        } else if (block == BidsBlocks.roughStoneTileIgEx) {
            return new ItemStack(BidsItems.roughStoneTile, 1, metadata + Global.STONE_IGEX_START);
        } else {
            return new ItemStack(BidsItems.roughStoneTile, 1, metadata + Global.STONE_MM_START);
        }
    }

}
