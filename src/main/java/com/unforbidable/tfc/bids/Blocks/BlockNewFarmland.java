package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.BlockFarmland;
import com.unforbidable.tfc.bids.Core.Crops.CropHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityNewFarmland;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockNewFarmland extends BlockFarmland {

    public BlockNewFarmland(Block block, int tex) {
        super(block, tex);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityNewFarmland();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        Block above = world.getBlock(x, y + 1, z);

        if (above != BidsBlocks.newCrops) {
            CropHelper.restoreOldFarmland(world, x, y, z);
        }

        super.onNeighborBlockChange(world, x, y, z, b);
    }

}
