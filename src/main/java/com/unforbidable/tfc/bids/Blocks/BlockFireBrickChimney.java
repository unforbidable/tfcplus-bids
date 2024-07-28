package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.BlockChimney;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.TileEntities.TileEntityFireBrickChimney;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockFireBrickChimney extends BlockChimney {

    public BlockFireBrickChimney() {
        setHardness(8);
        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
    }

    @Override
    public IIcon getIcon(int s, int m) {
        return TFCBlocks.fireBrick.getIcon(s, m);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityFireBrickChimney();
    }

}
