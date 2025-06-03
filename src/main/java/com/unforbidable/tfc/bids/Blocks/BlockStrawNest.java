package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.TileEntities.TileEntityStrawNest;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsGui;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockStrawNest extends BlockContainer {

    public BlockStrawNest() {
        super(Material.grass);

        setHardness(1f);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setBlockBounds(0.2f, 0, 0.2f, 0.8f, 0.3f, 0.8f);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
        // For some reason the nest has such a size that allows chicken to get stuck and take damage
        // Change collision box so that no gaps exist between the nest and the neighboring blocks
        AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 0.2, z + 1);

        if (aabb.intersectsWith(axisalignedbb1))
        {
            list.add(axisalignedbb1);
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return TFCBlocks.basket.getIcon(side, meta);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return TFCBlocks.basket.getIcon(world, x, y, z, side);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return BidsBlocks.strawNestRenderId;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            entityplayer.openGui(Bids.instance, BidsGui.strawNestGui, world, x, y, z);
            return true;
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityStrawNest();
    }

}
