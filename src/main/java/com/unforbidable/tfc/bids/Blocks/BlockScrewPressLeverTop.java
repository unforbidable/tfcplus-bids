package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressLever;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public class BlockScrewPressLeverTop extends Block {

    public BlockScrewPressLeverTop() {
        super(Material.wood);

        setHardness(2f);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        if (world.getTileEntity(x, y - 1, z) instanceof TileEntityScrewPressLever) {
            AxisAlignedBB bb = ScrewPressHelper.getLeverBlockBoundsAt(world, x, y - 1, z)
                .offset(0, -1, 0);
            setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
        }
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
        TileEntity te = world.getTileEntity(x, y - 1, z);
        if (te instanceof TileEntityScrewPressLever) {
            TileEntityScrewPressLever teLever = (TileEntityScrewPressLever) te;
            ForgeDirection d = teLever.getScrewDirection().getOpposite();

            // Rack middle, bridge one above, and middle again
            return world.getBlock(x + d.offsetX, y, z + d.offsetZ) == BidsBlocks.screwPressRackTop &&
                world.getBlock(x + d.offsetX * 2, y, z + d.offsetZ * 2) == BidsBlocks.screwPressRackBridge &&
                world.getBlock(x + d.offsetX * 3, y, z + d.offsetZ * 3) == BidsBlocks.screwPressRackTop;
        } else {
            return false;
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canBlockStay(world, x, y, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<ItemStack>();
    }

}
