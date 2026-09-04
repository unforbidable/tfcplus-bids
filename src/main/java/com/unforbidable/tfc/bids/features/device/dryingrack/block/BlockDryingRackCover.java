package com.unforbidable.tfc.bids.features.device.dryingrack.block;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDryingRackCover extends Block {

    public BlockDryingRackCover() {
        super(Material.grass);

        setHardness(1);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Item getItemDropped(int i, Random r, int j) {
        return TFCItems.straw;
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random) {
        return 4;
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
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        if (!canBlockStay(world, x, y, z)) {
            world.setBlock(x, y, z, Blocks.air, 0, 2);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        Block below = world.getBlock(x, y - 1, z);
        return below instanceof BlockDryingRack || below instanceof BlockDryingRackSide;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return side == ForgeDirection.DOWN;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return TFCBlocks.thatch.getIcon(side, meta);
    }

}
