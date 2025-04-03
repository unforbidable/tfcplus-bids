package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackBounds;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDryingRack;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Collections;

public class BlockDryingRack extends BlockContainer {

    public BlockDryingRack() {
        super(Material.wood);

        setHardness(2);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        AxisAlignedBB bounds = DryingRackBounds.getEntireDryingRackBounds();
        setBlockBounds((float) bounds.minX, (float) bounds.minY, (float) bounds.minZ,
                (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        return null;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return DryingRackBounds.getEntireDryingRackBounds();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
            float hitY, float hitZ) {
        ItemStack heldItemStack = player.getCurrentEquippedItem();
        if (heldItemStack != null) {
            if (DryingRackHelper.placeItemOnDryingRackAt(heldItemStack, player, world, x, y, z, hitX, hitY, hitZ)) {
                return true;
            }
        } else {
            if (DryingRackHelper.retrieveItemFromDryingRackAt(player, world, x, y, z, hitX, hitY, hitZ)) {
                return true;
            }
        }

        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityDryingRack te = (TileEntityDryingRack) world.getTileEntity(x, y, z);
        te.onDryingRackBroken();

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        if (((metadata & 8) != 0)) {
            return new ArrayList<ItemStack>(Collections.singleton(new ItemStack(TFCItems.pole, 2, 0)));
        } else {
            return super.getDrops(world, x, y, z, metadata, fortune);
        }
    }

    private boolean isBlockRackOrSolidSide(World world, int x, int y, int z, ForgeDirection d) {
        Block block = world.getBlock(x, y, z);
        return block instanceof BlockDryingRack || block.isSideSolid(world, x, y, z, d);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        super.onNeighborBlockChange(world, x, y, z, b);

        if (!world.isRemote) {
            int orientation = world.getBlockMetadata(x, y, z) % 2;
            ForgeDirection d = ForgeDirection.getOrientation(orientation * 2 + 2);
            ForgeDirection o = d.getOpposite();

            if (!isBlockRackOrSolidSide(world, x + d.offsetX, y, z + d.offsetZ, o)
                    || !isBlockRackOrSolidSide(world, x + o.offsetX, y, z + o.offsetZ, d)) {
                world.getBlock(x, y, z).dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
            }
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.dryingRackRenderId;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon(Tags.MOD_ID + ":Drying Rack");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityDryingRack();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        MovingObjectPosition mop = DryingRackHelper.onDryingRackCollisionRayTrace(world, x, y, z, startVec, endVec);
        if (mop != null) {
            return mop;
        }

        return super.collisionRayTrace(world, x, y, z, startVec, endVec);
    }

}
