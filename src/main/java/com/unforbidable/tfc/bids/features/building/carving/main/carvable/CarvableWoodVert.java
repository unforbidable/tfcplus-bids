package com.unforbidable.tfc.bids.features.building.carving.main.carvable;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.features.building.carving.tileentity.TileEntityCarving;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.carving.Carvable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class CarvableWoodVert implements Carvable {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block == TFCBlocks.woodVert || block == TFCBlocks.woodVert2 || block == TFCBlocks.woodVert3;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata, int equipmentTier) {
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
