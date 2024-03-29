package com.unforbidable.tfc.bids.Core.SaddleQuern;

import com.dunk.tfc.Blocks.BlockPlanks;
import com.dunk.tfc.Blocks.BlockPlasteredBlock;
import com.dunk.tfc.Blocks.BlockWattleDaub;
import com.dunk.tfc.Blocks.Terrain.BlockSmooth;
import com.unforbidable.tfc.bids.Blocks.BlockLogWall;
import com.unforbidable.tfc.bids.Blocks.BlockLogWallVert;
import com.unforbidable.tfc.bids.Blocks.BlockRoughStoneBrick;
import com.unforbidable.tfc.bids.Items.ItemPeeledLogSeasoned;
import com.unforbidable.tfc.bids.TileEntities.TileEntitySaddleQuern;
import com.unforbidable.tfc.bids.TileEntities.TileEntityStonePressLever;
import com.unforbidable.tfc.bids.TileEntities.TileEntityStonePressWeight;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class StonePressHelper {
    public static boolean canPlaceLeverAt(World world, int x, int y, int z) {
        TileEntitySaddleQuern saddleQuern = (TileEntitySaddleQuern) world.getTileEntity(x, y, z);
        if (saddleQuern.getWorkStoneType() == EnumWorkStoneType.SADDLE_QUERN_PRESSING && world.isAirBlock(x, y + 1, z)) {
            ForgeDirection d = saddleQuern.getOutputForgeDirection();

            // Anchor block behind, 4x air above and in front
            return isValidAnchorBlockAt(world,x - d.offsetX, y + 1, z - d.offsetZ)
                && world.isAirBlock(x, y + 1, z)
                && world.isAirBlock(x + d.offsetX, y + 1, z + d.offsetZ)
                && world.isAirBlock(x + d.offsetX * 2, y + 1, z + d.offsetZ * 2)
                && world.isAirBlock(x + d.offsetX * 3, y + 1, z + d.offsetZ * 3);
        }

        return false;
    }

    public static void placeLeverAt(World world, int x, int y, int z, ItemStack itemStack) {
        TileEntitySaddleQuern saddleQuern = (TileEntitySaddleQuern) world.getTileEntity(x, y, z);
        ForgeDirection d = saddleQuern.getOutputForgeDirection();

        placeLeverPartAt(world, x, y + 1, z, itemStack, saddleQuern.getOrientation(), TileEntityStonePressLever.PART_BASE);
        placeLeverPartAt(world, x + d.offsetX, y + 1, z + d.offsetZ, itemStack, saddleQuern.getOrientation(), TileEntityStonePressLever.PART_FREE);
        placeLeverPartAt(world, x + d.offsetX * 2, y + 1, z + d.offsetZ * 2, itemStack, saddleQuern.getOrientation(), TileEntityStonePressLever.PART_FREE);
        placeLeverPartAt(world, x + d.offsetX * 3, y + 1, z + d.offsetZ * 3, itemStack, saddleQuern.getOrientation(), TileEntityStonePressLever.PART_WEIGHT);

        itemStack.stackSize -= 4;
    }

    private static void placeLeverPartAt(World world, int x, int y, int z, ItemStack itemStack, int orientation, int part) {
        world.setBlock(x, y, z, BidsBlocks.stonePressLever, orientation, 2);
        TileEntityStonePressLever lever = (TileEntityStonePressLever) world.getTileEntity(x, y, z);
        lever.setOrientation(orientation);
        lever.setLeverPart(part);
        lever.setLogItem(new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage()));
    }

    public static boolean isValidLever(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() instanceof ItemPeeledLogSeasoned && itemStack.stackSize > 3;
    }

    public static boolean isValidAnchorBlockAt(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block instanceof BlockRoughStoneBrick ||
            block instanceof BlockLogWall ||
            block instanceof BlockLogWallVert ||
            block instanceof BlockSmooth ||
            block instanceof BlockPlanks ||
            block instanceof BlockWattleDaub ||
            block instanceof BlockPlasteredBlock;
    }

    public static boolean isWeightLiftedAt(IBlockAccess world, int x, int y, int z) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityStonePressWeight) {
            TileEntityStonePressWeight weightTileEntity = (TileEntityStonePressWeight) world.getTileEntity(x, y, z);
            return weightTileEntity.isLifted();
        }

        return false;
    }

}
