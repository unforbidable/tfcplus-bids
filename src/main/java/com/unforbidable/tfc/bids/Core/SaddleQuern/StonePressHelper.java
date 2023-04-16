package com.unforbidable.tfc.bids.Core.SaddleQuern;

import com.unforbidable.tfc.bids.Items.ItemPeeledLogSeasoned;
import com.unforbidable.tfc.bids.TileEntities.TileEntitySaddleQuern;
import com.unforbidable.tfc.bids.TileEntities.TileEntityStonePressLever;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class StonePressHelper {
    public static boolean canPlaceLeverAt(World world, int x, int y, int z) {
        TileEntitySaddleQuern saddleQuern = (TileEntitySaddleQuern) world.getTileEntity(x, y, z);
        if (saddleQuern.getWorkStoneType() == EnumWorkStoneType.SADDLE_QUERN_PRESSING && world.isAirBlock(x, y + 1, z)) {
            ForgeDirection d = saddleQuern.getOutputForgeDirection();

            return world.isSideSolid(x - d.offsetX, y, z - d.offsetZ, ForgeDirection.UP)
                && world.isAirBlock(x - d.offsetX, y + 1, z - d.offsetZ)
                && world.isAirBlock(x + d.offsetX, y + 1, z + d.offsetZ)
                && world.isAirBlock(x + d.offsetX * 2, y + 1, z + d.offsetZ * 2);
        }

        return false;
    }

    public static void placeLeverAt(World world, int x, int y, int z, ItemStack itemStack) {
        TileEntitySaddleQuern saddleQuern = (TileEntitySaddleQuern) world.getTileEntity(x, y, z);
        ForgeDirection d = saddleQuern.getOutputForgeDirection();

        placeLeverPartAt(world, x - d.offsetX, y + 1, z - d.offsetZ, itemStack, saddleQuern.getOrientation(), TileEntityStonePressLever.PART_ANCHOR);
        placeLeverPartAt(world, x, y + 1, z, itemStack, saddleQuern.getOrientation(), TileEntityStonePressLever.PART_BASE);
        placeLeverPartAt(world, x + d.offsetX, y + 1, z + d.offsetZ, itemStack, saddleQuern.getOrientation(), TileEntityStonePressLever.PART_FREE);
        placeLeverPartAt(world, x + d.offsetX * 2, y + 1, z + d.offsetZ * 2, itemStack, saddleQuern.getOrientation(), TileEntityStonePressLever.PART_WEIGHT);

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

}
