package com.unforbidable.tfc.bids.Core.Fences;

import com.unforbidable.tfc.bids.Blocks.BlockPalisade;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

import java.util.*;

public class FenceConnections {

    private final static int NORTH = 0;
    private final static int SOUTH = 1;
    private final static int WEST = 2;
    private final static int EAST = 3;
    private final static int CORNER_NW = 4;
    private final static int CORNER_NE = 5;
    private final static int CORNER_SW = 6;
    private final static int CORNER_SE = 7;

    private final static Map<Integer, int[]> OTHERS_OPPOSITE_FIRST = new HashMap<Integer, int[]>();

    static {
        OTHERS_OPPOSITE_FIRST.put(NORTH, new int[] { SOUTH, WEST, EAST });
        OTHERS_OPPOSITE_FIRST.put(SOUTH, new int[] { NORTH, WEST, EAST });
        OTHERS_OPPOSITE_FIRST.put(WEST, new int[] { EAST, NORTH, SOUTH });
        OTHERS_OPPOSITE_FIRST.put(EAST, new int[] { WEST, NORTH, SOUTH });
    }

    private final boolean[] connections;
    private final boolean[] fills;
    private final int connectionCount;

    public FenceConnections(IBlockAccess world, int x, int y, int z) {
        connections = new boolean[4];
        fills = new boolean[8];

        int count = 0;
        int last = -1;

        if (world.getBlock(x, y, z) instanceof BlockPalisade) {
            Block[] blocks = new Block[8];
            blocks[NORTH] = world.getBlock(x, y, z - 1);
            blocks[SOUTH] = world.getBlock(x, y, z + 1);
            blocks[WEST] = world.getBlock(x - 1, y, z);
            blocks[EAST] = world.getBlock(x + 1, y, z);
            blocks[CORNER_NW] = world.getBlock(x - 1, y, z - 1);
            blocks[CORNER_NE] = world.getBlock(x + 1, y, z - 1);
            blocks[CORNER_SW] = world.getBlock(x - 1, y, z + 1);
            blocks[CORNER_SE] = world.getBlock(x + 1, y, z + 1);

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
                        count++;
                        break;
                    }
                }
            }

            connectionCount = count;

            if (count > 1) {
                if (isNorth() && isSouth()) {
                    if (!isWest() && block.canFenceFillWithBlock(blocks[WEST])) {
                        fills[WEST] = true;
                    }

                    if (!isEast() && block.canFenceFillWithBlock(blocks[EAST])) {
                        fills[EAST] = true;
                    }
                }

                if (isWest() && isEast()) {
                    if (!isNorth() && block.canFenceFillWithBlock(blocks[NORTH])) {
                        fills[NORTH] = true;
                    }

                    if (!isSouth() && block.canFenceFillWithBlock(blocks[SOUTH])) {
                        fills[SOUTH] = true;
                    }
                }

                if (isNorth() && isWest()) {
                    if (block.canFenceFillWithBlock(blocks[CORNER_NW])) {
                        fills[CORNER_NW] = true;
                    }

                    if (!isSouth() && block.canFenceFillWithBlock(blocks[SOUTH])) {
                        fills[SOUTH] = true;
                    }

                    if (!isEast()  && block.canFenceFillWithBlock(blocks[EAST])) {
                        fills[EAST] = true;
                    }
                }

                if (isNorth() && isEast()) {
                    if (block.canFenceFillWithBlock(blocks[CORNER_NE])) {
                        fills[CORNER_NE] = true;
                    }

                    if (!isSouth() && block.canFenceFillWithBlock(blocks[SOUTH])) {
                        fills[SOUTH] = true;
                    }

                    if (!isWest()  && block.canFenceFillWithBlock(blocks[WEST])) {
                        fills[WEST] = true;
                    }
                }

                if (isSouth() && isWest()) {
                    if (block.canFenceFillWithBlock(blocks[CORNER_SW])) {
                        fills[CORNER_SW] = true;
                    }

                    if (!isNorth() && block.canFenceFillWithBlock(blocks[NORTH])) {
                        fills[NORTH] = true;
                    }

                    if (!isEast()  && block.canFenceFillWithBlock(blocks[EAST])) {
                        fills[EAST] = true;
                    }
                }

                if (isSouth() && isEast()) {
                    if (block.canFenceFillWithBlock(blocks[CORNER_SE])) {
                        fills[CORNER_SE] = true;
                    }

                    if (!isNorth() && block.canFenceFillWithBlock(blocks[NORTH])) {
                        fills[NORTH] = true;
                    }

                    if (!isWest() && block.canFenceFillWithBlock(blocks[WEST])) {
                        fills[WEST] = true;
                    }
                }
            }
        } else {
            connectionCount = 0;
        }
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

    public int getConnectionCount() {
        return connectionCount;
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
        for (boolean fill : fills) {
            if (fill) {
                return true;
            }
        }

        return false;
    }

    public boolean isFillNorth() {
        return fills[NORTH];
    }

    public boolean isFillSouth() {
        return fills[SOUTH];
    }

    public boolean isFillWest() {
        return fills[WEST];
    }

    public boolean isFillEast() {
        return fills[EAST];
    }

    public boolean isFillCornerNW() {
        return fills[CORNER_NW];
    }

    public boolean isFillCornerNE() {
        return fills[CORNER_NE];
    }

    public boolean isFillCornerSW() {
        return fills[CORNER_SW];
    }

    public boolean isFillCornerSE() {
        return fills[CORNER_SE];
    }

}
