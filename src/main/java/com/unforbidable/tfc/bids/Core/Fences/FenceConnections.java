package com.unforbidable.tfc.bids.Core.Fences;

import com.unforbidable.tfc.bids.Blocks.BlockPalisade;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FenceConnections {

    private final static int NORTH = 0;
    private final static int SOUTH = 1;
    private final static int WEST = 2;
    private final static int EAST = 3;

    private final static Map<Integer, int[]> OTHERS_OPPOSITE_FIRST = new HashMap<Integer, int[]>();

    static {
        OTHERS_OPPOSITE_FIRST.put(NORTH, new int[] { SOUTH, WEST, EAST });
        OTHERS_OPPOSITE_FIRST.put(SOUTH, new int[] { NORTH, WEST, EAST });
        OTHERS_OPPOSITE_FIRST.put(WEST, new int[] { EAST, NORTH, SOUTH });
        OTHERS_OPPOSITE_FIRST.put(EAST, new int[] { WEST, NORTH, SOUTH });
    }

    private final boolean[] connections;
    private final boolean canFenceFill;

    public FenceConnections(IBlockAccess world, int x, int y, int z) {
        Block[] blocks = new Block[4];
        blocks[NORTH] = world.getBlock(x, y, z - 1);
        blocks[SOUTH] = world.getBlock(x, y, z + 1);
        blocks[WEST] = world.getBlock(x - 1, y, z);
        blocks[EAST] = world.getBlock(x + 1, y, z);

        connections = new boolean[4];
        int count = 0;
        int last = -1;

        if (world.getBlock(x, y, z) instanceof BlockPalisade) {
            BlockPalisade block = (BlockPalisade) world.getBlock(x, y, z);

            // Look for all fence connections
            for (int i = 0; i < 4; i++) {
                if (block.canFenceConnectToFence(blocks[i])) {
                    connections[i] = true;
                    count++;
                    last = i;
                }
            }

            if (count == 1) {
                // If we have exactly one fence connection,
                // look for one block connection, starting with the opposite
                for (int i : OTHERS_OPPOSITE_FIRST.get(last)) {
                    if (block.canFenceConnectToBlock(blocks[i])) {
                        connections[i] = true;
                        break;
                    }
                }
            }

            canFenceFill = getCanFenceFill(world, x, y, z, block);
        } else {
            canFenceFill = false;
        }
    }

    private boolean getCanFenceFill(IBlockAccess world, int x, int y, int z, BlockPalisade block) {
        if (isNorth() && isSouth()) {
            if (!isWest()) {
                Block west = world.getBlock(x - 1, y, z);
                if (block.canFenceFillWithBlock(west)) {
                    return true;
                }
            }
            if (!isEast()) {
                Block east = world.getBlock(x + 1, y, z);
                if (block.canFenceFillWithBlock(east)) {
                    return true;
                }
            }
        }

        if (isWest() && isEast()) {
            if (!isNorth()) {
                Block north = world.getBlock(x, y, z - 1);
                if (block.canFenceFillWithBlock(north)) {
                    return true;
                }
            }
            if (!isSouth()) {
                Block south = world.getBlock(x, y, z + 1);
                if (block.canFenceFillWithBlock(south)) {
                    return true;
                }
            }
        }

        if (isNorth() && isWest()) {
            Block nw = world.getBlock(x - 1, y, z - 1);
            if (block.canFenceFillWithBlock(nw)) {
                return true;
            }
        }

        if (isNorth() && isEast()) {
            Block ne = world.getBlock(x + 1, y, z - 1);
            if (block.canFenceFillWithBlock(ne)) {
                return true;
            }
        }

        if (isSouth() && isWest()) {
            Block sw = world.getBlock(x - 1, y, z + 1);
            if (block.canFenceFillWithBlock(sw)) {
                return true;
            }
        }

        if (isSouth() && isEast()) {
            Block sw = world.getBlock(x + 1, y, z + 1);
            if (block.canFenceFillWithBlock(sw)) {
                return true;
            }
        }

        return false;
    }

    public boolean isNorth() {
        return connections[NORTH];
    }

    public boolean isSouth() {
        return connections[SOUTH];
    }

    public boolean isWest() {
        return connections[WEST];
    }

    public boolean isEast() {
        return connections[EAST];
    }

    public int countConnections() {
        int count = 0;

        for (int i = 0; i < connections.length; i++) {
            if (connections[i]) {
                count++;
            }
        }

        return count;
    }

    public AxisAlignedBB getAllBounds(float width, float height) {
        float minX = isWest() ? 0 : (1 - width) * 0.5f;
        float minZ = isNorth() ? 0 : (1 - width) * 0.5f;
        float maxX = isEast() ? 1 : (1 + width) * 0.5f;
        float maxZ = isSouth() ? 1 : (1 + width) * 0.5f;

        return AxisAlignedBB.getBoundingBox(minX, 0, minZ, maxX, height, maxZ);
    }

    public AxisAlignedBB getCenterBounds(float width, float height) {
        float minX = (1 - width) * 0.5f;
        float minZ = (1 - width) * 0.5f;
        float maxX = (1 + width) * 0.5f;
        float maxZ = (1 + width) * 0.5f;

        return AxisAlignedBB.getBoundingBox(minX, 0, minZ, maxX, height, maxZ);
    }

    public List<AxisAlignedBB> getSegmentBounds(float width, float height) {
        List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();

        float minX = (1 - width) * 0.5f;
        float minZ = (1 - width) * 0.5f;
        float maxX = (1 + width) * 0.5f;
        float maxZ = (1 + width) * 0.5f;

        if (isNorth()) {
            list.add(AxisAlignedBB.getBoundingBox(minX, 0, 0, maxX, height, minZ));
        }

        if (isSouth()) {
            list.add(AxisAlignedBB.getBoundingBox(minX, 0, maxZ, maxX, height, 1));
        }

        if (isWest()) {
            list.add(AxisAlignedBB.getBoundingBox(0, 0, minZ, minX, height, maxZ));
        }

        if (isEast()) {
            list.add(AxisAlignedBB.getBoundingBox(maxX, 0, minZ, 1, height, maxZ));
        }

        return list;
    }

    public boolean canFenceFill() {
        return canFenceFill;
    }

}
