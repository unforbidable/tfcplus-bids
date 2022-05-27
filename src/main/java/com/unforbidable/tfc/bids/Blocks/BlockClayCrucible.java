package com.unforbidable.tfc.bids.Blocks;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayCrucible;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockClayCrucible extends BlockCrucible {

    public BlockClayCrucible() {
        super(Material.rock);
        setTextureName("Clay Crucible");
        setCreativeTab(BidsCreativeTabs.BidsDefault);
        setBlockBounds(0.125f, 0.25f, 0.125f, 0.875f, 0.625f, 0.875f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityClayCrucible();
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.clayCrucibleRenderId;
    }

}
