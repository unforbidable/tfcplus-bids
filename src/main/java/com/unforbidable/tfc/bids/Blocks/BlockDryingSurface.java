package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Textures;
import com.unforbidable.tfc.bids.Core.DryingSurface.DryingSurfaceHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingSurface;
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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockDryingSurface extends BlockContainer {

    public BlockDryingSurface() {
        super(Material.ground);

        setHardness(1f);
        setBlockBounds(0, 0, 0, 1, 0.03f, 1);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if (player.isSneaking() && player.getHeldItem() == null) {
                return DryingSurfaceHelper.retrieveItemFromDryingSurface(world, x, y, z, player, side, hitX, hitY, hitZ);
            }

            if (!player.isSneaking()) {
                return DryingSurfaceHelper.activateDryingSurface(world, x, y, z, player, side, hitX, hitY, hitZ);
            }
        }

        return true;
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y - 1, z);
        return block.isSideSolid(world, x, y - 1, z, ForgeDirection.UP);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        if (!canBlockStay(world, x, y ,z)) {
            world.setBlock(x, y, z, Blocks.air, 0, 2);
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return TFC_Textures.invisibleTexture;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return TFC_Textures.invisibleTexture;
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        TileEntityDryingSurface te = (TileEntityDryingSurface)world.getTileEntity(x, y, z);
        te.onDryingSurfaceBroken();
    }

    @Override
    public Item getItemDropped(int meta, Random rnd, int fortune) {
        return null;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess w, int x, int y, int z) {
        return false;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        MovingObjectPosition mop = DryingSurfaceHelper.onDryingSurfaceCollisionRayTrace(world, x, y, z, startVec, endVec);
        if (mop != null) {
            return mop;
        }

        return super.collisionRayTrace(world, x, y, z, startVec, endVec);
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
        return BidsBlocks.dryingSurfaceRenderId;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityDryingSurface();
    }

}
