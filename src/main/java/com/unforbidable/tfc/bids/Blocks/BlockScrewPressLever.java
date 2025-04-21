package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressLever;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockScrewPressLever extends BlockContainer {

    public BlockScrewPressLever() {
        super(Material.wood);

        setHardness(2f);
        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        AxisAlignedBB bb = ScrewPressHelper.getLeverBlockBoundsAt(world, x, y, z);
        setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
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
        return BidsBlocks.screwPressLeverRenderId;
    }

    @Override
    public IIcon getIcon(int i, int j) {
        return TFCBlocks.woodSupportH.getIcon(0, 0);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        TileEntityScrewPressLever te = (TileEntityScrewPressLever) world.getTileEntity(x, y, z);
        ForgeDirection d = te.getScrewDirection().getOpposite();

        // Lever top above, rack middle, bridge one above, and middle again
        return world.getBlock(x, y + 1, z) == BidsBlocks.screwPressLeverTop &&
            world.getBlock(x + d.offsetX, y, z + d.offsetZ) == BidsBlocks.screwPressRackMiddle &&
            world.getBlock(x + d.offsetX * 2, y + 1, z + d.offsetZ * 2) == BidsBlocks.screwPressRackBridge &&
            world.getBlock(x + d.offsetX * 3, y, z + d.offsetZ * 3) == BidsBlocks.screwPressRackMiddle;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canBlockStay(world, x, y, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.isAirBlock(x, y + 1, z) &&
            ScrewPressHelper.getValidDirectionToPlaceLever(world, x, y, z) != ForgeDirection.UNKNOWN;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack is) {
        ForgeDirection screwDirection = ScrewPressHelper.getValidDirectionToPlaceLever(world, x, y, z);
        int meta = world.getBlockMetadata(x - screwDirection.offsetX, y, z - screwDirection.offsetZ);

        world.setBlockMetadataWithNotify(x, y, z, meta, 2);

        // Direction where the screw is expected
        TileEntityScrewPressLever te = (TileEntityScrewPressLever) world.getTileEntity(x, y, z);
        te.setScrewDirection(screwDirection);

        world.setBlock(x, y + 1, z, BidsBlocks.screwPressLeverTop, meta, 0);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityScrewPressLever();
    }

}
