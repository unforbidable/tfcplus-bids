package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Textures;
import com.unforbidable.tfc.bids.Core.Common.Metadata.DecorativeSurfaceMetadata;
import com.unforbidable.tfc.bids.Core.ProcessingSurface.ProcessingSurfaceHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDecorativeSurface;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockDecorativeSurface extends BlockContainer {

    private static final float ITEM_DEPTH = 0.06f;

    public BlockDecorativeSurface() {
        super(Material.cloth);

        setHardness(1f);
        setBlockBounds(0, 0, 0, 0, 0, 0);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        DecorativeSurfaceMetadata meta = DecorativeSurfaceMetadata.at(world, x, y, z);
        if (meta.isHorizontal()) {
            setBlockBounds(0, 0, 0, 1, ITEM_DEPTH, 1);
        } else {
            switch (meta.getVerticalFace()) {
                case NORTH:
                    setBlockBounds(0, 0, 1 - ITEM_DEPTH, 1, 1, 1);
                    break;

                case SOUTH:
                    setBlockBounds(0, 0, 0, 1, 1, ITEM_DEPTH);
                    break;

                case WEST:
                    setBlockBounds(1 - ITEM_DEPTH, 0, 0, 1, 1, 1);
                    break;

                case EAST:
                    setBlockBounds(0, 0, 0, 0.05f, 1, 1);
                    break;
            }
        }
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ) {
        if (entityplayer.getHeldItem() == null) {
            if (!world.isRemote && entityplayer.isSneaking()) {
                world.setBlock(x, y, z, Blocks.air, 0, 2);
            }
        }

        return true;
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        DecorativeSurfaceMetadata meta = DecorativeSurfaceMetadata.at(world, x, y, z);
        if (meta.isHorizontal()) {
            Block block = world.getBlock(x, y - 1, z);
            return block.isSideSolid(world, x, y - 1, z, ForgeDirection.UP);
        } else {
            ForgeDirection dir = meta.getVerticalFace();
            ForgeDirection opposite = dir.getOpposite();
            int x2 = x + opposite.offsetX;
            int y2 = y + opposite.offsetY;
            int z2 = z + opposite.offsetZ;
            Block block = world.getBlock(x2, y2, z2);
            return block.isSideSolid(world, x2, y2, z2, dir);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        if (!canBlockStay(world, x, y ,z)) {
            world.setBlock(x, y, z, Blocks.air, 0, 2);
        }
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        TileEntityDecorativeSurface te = (TileEntityDecorativeSurface)world.getTileEntity(x, y, z);
        te.onDecorativeSurfaceBroken();
    }

    @Override
    public Item getItemDropped(int meta, Random rnd, int fortune) {
        return null;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess w, int x, int y, int z) {
        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
        ProcessingSurfaceHelper.registerDecorativeSurfaceIcons(registerer);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return TFC_Textures.invisibleTexture;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileEntityDecorativeSurface te = (TileEntityDecorativeSurface) world.getTileEntity(x, y, z);
        if (te.getItem() != null) {
            return ProcessingSurfaceHelper.getIconForItem(te.getItem());
        } else {
            return super.getIcon(world, x, y, z, side);
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.decorativeSurfaceRenderId;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityDecorativeSurface();
    }

}
