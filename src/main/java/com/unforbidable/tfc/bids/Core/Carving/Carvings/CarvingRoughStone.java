package com.unforbidable.tfc.bids.Core.Carving.Carvings;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class CarvingRoughStone implements ICarving {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == BidsBlocks.roughStoneSed ||
            block == BidsBlocks.roughStoneMM ||
            block == BidsBlocks.roughStoneIgIn ||
            block == BidsBlocks.roughStoneIgEx;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata, int equipmentTier) {
        return block == BidsBlocks.roughStoneSed || equipmentTier > 0;
    }

    @Override
    public boolean canCarveBlockAt(Block block, int metadata, World world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public ItemStack[] getCarvingHarvest(Block block, int metadata, Random random) {
        final int n = random.nextInt(2) + 1;
        ItemStack[] list = new ItemStack[n];
        for (int i = 0; i < n; i++)
            list[i] = getLooseRock(block, metadata);
        return list;
    }

    @Override
    public ItemStack getCarvingExtraHarvest(Block block, int metadata, Random random, float bitRatio) {
        return random.nextDouble() < 2 * bitRatio
                ? getLooseRock(block, metadata)
                : null;
    }

    @Override
    public Block getCarvingBlock(Block block, int metadata) {
        return BidsBlocks.carvingRock;
    }

    @Override
    public String getCarvingSoundEffect() {
        return "dig.stone";
    }

    protected ItemStack getLooseRock(Block block, int metadata) {
        if (block == BidsBlocks.roughStoneSed) {
            return new ItemStack(TFCItems.looseRock, 1, metadata + Global.STONE_SED_START);
        } else if (block == BidsBlocks.roughStoneIgIn) {
            return new ItemStack(TFCItems.looseRock, 1, metadata + Global.STONE_IGIN_START);
        } else if (block == BidsBlocks.roughStoneIgEx) {
            return new ItemStack(TFCItems.looseRock, 1, metadata + Global.STONE_IGEX_START);
        } else {
            return new ItemStack(TFCItems.looseRock, 1, metadata + Global.STONE_MM_START);
        }
    }

}
