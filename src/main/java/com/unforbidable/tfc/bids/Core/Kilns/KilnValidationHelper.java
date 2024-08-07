package com.unforbidable.tfc.bids.Core.Kilns;

import com.dunk.tfc.Blocks.BlockFireBrick;
import com.unforbidable.tfc.bids.Core.Chimney.ChimneyHelper;
import com.unforbidable.tfc.bids.api.BidsEventFactory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class KilnValidationHelper {

    public static boolean isChimneyTier(World world, int x, int y, int z, int tier) {
        TileEntity te = world.getTileEntity(x, y, z);
        return ChimneyHelper.getChimneyTier(te) >= tier;
    }

    public static boolean isAir(World world, int x, int y, int z) {
        return world.isAirBlock(x, y, z);
    }

    public static boolean isAirOrPottery(World world, int x, int y, int z) {
        return isAir(world, x, y, z) || isPottery(world, x, y, z);
    }

    private static boolean isPottery(World world, int x, int y, int z) {
        return BidsEventFactory.onKilnFireBlockCheck(world, x, y, z);
    }

    public static boolean isWall(World world, int x, int y, int z, ForgeDirection d) {
        Block block = world.getBlock(x, y, z);
        return block.isOpaqueCube() && !block.isFlammable(world, x, y, z, d.getOpposite());
    }

    public static boolean isAirOrFire(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block.getMaterial() == Material.air || block.getMaterial() == Material.fire;
    }

    public static boolean isFireBrick(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block instanceof BlockFireBrick;
    }

}
