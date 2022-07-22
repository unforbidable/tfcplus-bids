package com.unforbidable.tfc.bids.api;

import java.util.ArrayList;
import java.util.List;

import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class CarvingRegistry {

    static List<ICarving> list = new ArrayList<ICarving>();

    public static void registerCarving(ICarving carving) {
        list.add(carving);
    }

    public static boolean canCarveBlockAt(World world, int x, int y, int z) {
        return getBlockCarvingAt(world, x, y, z) != null;
    }

    public static boolean canCarveBlock(Block block, int metadata) {
        return getBlockCarving(block, metadata) != null;
    }

    public static ICarving getBlockCarving(TileEntityCarving te) {
        Block block = Block.getBlockById(te.getCarvedBlockId());
        int metadata = te.getCarvedBlockMetadata();

        return getBlockCarving(block, metadata);
    }

    public static ICarving getBlockCarvingAt(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        int metadata = world.getBlockMetadata(x, y, z);

        return getBlockCarving(block, metadata);
    }

    public static ICarving getBlockCarving(Block block, int metadata) {
        for (ICarving c : list) {
            if (c.canCarveBlock(block, metadata)) {
                return c;
            }
        }

        return null;
    }

}
