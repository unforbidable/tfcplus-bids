package com.unforbidable.tfc.bids.features.device.dryingrack.block;

import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import com.unforbidable.tfc.bids.features.device.dryingrack.main.DryingRackBounds;
import com.unforbidable.tfc.bids.features.device.dryingrack.main.DryingRackHelper;
import com.unforbidable.tfc.bids.features.device.dryingrack.tileentity.TileEntityDryingRack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDryingRack extends BlockContainer {

    public BlockDryingRack() {
        super(Material.wood);

        setHardness(2);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
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
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        TileEntityDryingRack te = (TileEntityDryingRack) world.getTileEntity(x, y, z);
        te.onDryingRackBroken();
    }

    @Override
    public Item getItemDropped(int meta, Random rnd, int fortune) {
        return null;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canBlockStay(world, x, y ,z)) {
            world.setBlock(x, y, z, Blocks.air, 0, 2);
            world.notifyBlockChange(x, y, z, Blocks.air);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        int orientation = world.getBlockMetadata(x, y, z) % 2;
        ForgeDirection d = ForgeDirection.getOrientation(orientation * 2 + 2);
        ForgeDirection o = d.getOpposite();

        return isValidDryingRackNeighbor(world, x + d.offsetX, y, z + d.offsetZ, o) &&
            isValidDryingRackNeighbor(world, x + o.offsetX, y, z + o.offsetZ, d);
    }

    private boolean isValidDryingRackNeighbor(World world, int x, int y, int z, ForgeDirection d) {
        Block block = world.getBlock(x, y, z);
        return block instanceof BlockDryingRack ||
            block instanceof BlockDryingRackSide ||
            block.isSideSolid(world, x, y, z, d);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(this);
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
