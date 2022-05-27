package com.unforbidable.tfc.bids.Blocks;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.TileEntities.TileEntityFireClayCrucible;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFireClayCrucible extends BlockCrucible {

    public BlockFireClayCrucible() {
        super(Material.rock);
        setTextureName("Fire Clay Crucible");
        setCreativeTab(BidsCreativeTabs.BidsDefault);
        setBlockBounds(0.0625f, 0.25f, 0.0625f, 0.9375f, 0.9375f, 0.9375f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityFireClayCrucible();
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.fireClayCrucibleRenderId;
    }

}
