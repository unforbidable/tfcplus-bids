package com.unforbidable.tfc.bids.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLight extends Block {

    public BlockLight() {
        super(Material.air);
    }

    public int getRenderType() {
        return -1;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean canCollideCheck(int meta, boolean fullHit) {
        return false;
    }

    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
    }

    @Override
    public int getLightValue() {
        return 15;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return 15;
    }

}
