package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Common.Bounds.WallHookBounds;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWallHook;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockWallHook extends BlockContainer {

    public BlockWallHook() {
        super(Material.wood);

        setHardness(1f);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int orientation = world.getBlockMetadata(x, y, z) & 3;
        AxisAlignedBB bounds = WallHookBounds.getBoundsForOrientation(orientation).getEntireBounds();
        setBlockBounds((float) bounds.minX, (float) bounds.minY, (float) bounds.minZ, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        int orientation = world.getBlockMetadata(x, y, z) & 3;
        return WallHookBounds.getBoundsForOrientation(orientation).getEntireBounds();
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        switch (ForgeDirection.getOrientation(side)) {
            case WEST:
                return 3;
            case EAST:
                return 1;
            case NORTH:
                return 0;
            case SOUTH:
                return 2;
        }

        return 0;
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        int orientation = world.getBlockMetadata(x, y, z) & 3;
        if (orientation == 0) {
            return canBlockStayOnNeighborSide(world, x, y, z, ForgeDirection.NORTH);
        } else if (orientation == 1) {
            return canBlockStayOnNeighborSide(world, x, y, z, ForgeDirection.EAST);
        } else if (orientation == 2) {
            return canBlockStayOnNeighborSide(world, x, y, z, ForgeDirection.SOUTH);
        } else {
            return canBlockStayOnNeighborSide(world, x, y, z, ForgeDirection.WEST);
        }
    }

    private boolean canBlockStayOnNeighborSide(World world, int x, int y, int z, ForgeDirection dir) {
        if (dir == ForgeDirection.EAST || dir == ForgeDirection.WEST || dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH) {
            return world.isSideSolid(x - dir.offsetX, y, z - dir.offsetZ, dir);
        }

        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        super.onNeighborBlockChange(world, x, y, z, block);

        if (!canBlockStay(world, x, y, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack is) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityWallHook)
        {
            TileEntityWallHook te = (TileEntityWallHook) world.getTileEntity(x, y, z);
            world.markBlockForUpdate(x, y, z);
        }
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
        if (canPlaceBlockAt(world, x, y, z)) {
            ForgeDirection dir = ForgeDirection.getOrientation(side);
            if (dir == ForgeDirection.EAST || dir == ForgeDirection.WEST || dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH) {
                return world.isSideSolid(x - dir.offsetX, y, z - dir.offsetZ, dir);
            }
        }

        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
                                    float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntityWallHook te = (TileEntityWallHook) world.getTileEntity(x, y, z);

            ItemStack heldItemStack = player.getCurrentEquippedItem();
            if (heldItemStack == null) {
                te.tryRetrieveItemStack(player);
            } else {
                te.tryPlaceItemStack(heldItemStack, player);
            }
        }

        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityWallHook te = (TileEntityWallHook) world.getTileEntity(x, y, z);
        te.onBlockBroken();

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess bAccess, int x, int y, int z, int side) {
        return true;
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
        return BidsBlocks.wallHookRenderId;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon(Tags.MOD_ID + ":Wall Hook");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityWallHook();
    }

}
