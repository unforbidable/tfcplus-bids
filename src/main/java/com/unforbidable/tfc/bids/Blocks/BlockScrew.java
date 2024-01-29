package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.Devices.BlockAxle;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrew;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockScrew extends BlockAxle {

    IIcon topIcon;
    IIcon sideIcon;

    public BlockScrew(Material m) {
        super(m);

        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
        topIcon = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + " Top");
        sideIcon = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + " 1");
    }

    @Override
    public IIcon getIcon(int i, int j) {
        if (i < 2) {
            return topIcon;
        } else {
            return sideIcon;
        }
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.screwRenderId;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityScrew();
    }

}
