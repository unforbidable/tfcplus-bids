package com.unforbidable.tfc.bids.Core.WorldGen;

import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

public class WorldGenHelper {

    public static final ForgeDirection[] YAXIS_DIRS = new ForgeDirection[]{ ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH };

    public static Set<BlockCoord> getClusterArea(Random random, int xCoord, int yCoord, int zCoord, int size) {
        Set<BlockCoord> area = new HashSet<BlockCoord>();

        // Add origin
        BlockCoord firstOrigin = new BlockCoord(xCoord, yCoord, zCoord);
        area.add(firstOrigin);

        Set<BlockCoord> availableOrigins = new HashSet<BlockCoord>(area);
        List<BlockCoord> nextOriginPool = new ArrayList<BlockCoord>(area);

        while (area.size() < size) {
            BlockCoord origin = nextOriginPool.get(random.nextInt(nextOriginPool.size()));
            int side = random.nextInt(4);

            for (int i = 0; i < 4; i++) {
                ForgeDirection dir = YAXIS_DIRS[(side + i) & 3];
                BlockCoord next = new BlockCoord(origin.x + dir.offsetX, yCoord, origin.z + dir.offsetZ);
                if (area.add(next)) {
                    availableOrigins.add(next);

                    // When new locations is added,
                    // add all existing locations to the next origin pool
                    // Older locations will be more likely selected
                    nextOriginPool.addAll(availableOrigins);

                    break;
                } else if (i == 3) {
                    // When a location is completely surrounded remove it,
                    // so it can be no longer selected
                    availableOrigins.remove(origin);
                }
            }
        }

        return area;
    }

    public static Set<BlockCoord> getDiscArea(Random random, int xCoord, int yCoord, int zCoord, int count) {
        Set<BlockCoord> area = new HashSet<BlockCoord>();

        int d = random.nextInt(4);
        int x = xCoord;
        int z = zCoord;

        area.add(new BlockCoord(x, yCoord, z));

        x += YAXIS_DIRS[d].offsetX;
        z += YAXIS_DIRS[d].offsetZ;

        d++;
        if (d == 4) {
            d = 0;
        }

        while (area.size() < count) {
            area.add(new BlockCoord(x, yCoord, z));

            // We want to keep turning, unless the block we are turning into is already in the area
            // If so we carry on in the same direction

            // get new direction
            int d2 = d + 1;
            if (d2 == 4) {
                d2 = 0;
            }

            // The coords in the new direction
            int x2 = x + YAXIS_DIRS[d2].offsetX + YAXIS_DIRS[d].offsetX;
            int z2 = z + YAXIS_DIRS[d2].offsetZ + YAXIS_DIRS[d].offsetZ;

            if (area.contains(new BlockCoord(x2, yCoord, z2))) {
                // Coords in the new direction are in the area, so keep going
                x += YAXIS_DIRS[d].offsetX;
                z += YAXIS_DIRS[d].offsetZ;
            } else {
                x = x2;
                z = z2;
                d = d2;
            }
        }

        return area;
    }

    public static Set<BlockCoord> getBorderOfArea(Set<BlockCoord> area) {
        Set<BlockCoord> border = new HashSet<BlockCoord>();

        for (BlockCoord bc: area) {
            for (ForgeDirection d: YAXIS_DIRS) {
                BlockCoord bc2 = new BlockCoord(bc.x + d.offsetX, bc.y, bc.z + d.offsetZ);
                if (!area.contains(bc2)) {
                    border.add(bc2);
                }
            }
        }

        return border;
    }

    public static Set<BlockCoord> getBorderOfAreaExcluding(Set<BlockCoord> area, Set<BlockCoord> areaToExclude) {
        Set<BlockCoord> border = new HashSet<BlockCoord>();

        for (BlockCoord bc: area) {
            for (ForgeDirection d: YAXIS_DIRS) {
                BlockCoord bc2 = new BlockCoord(bc.x + d.offsetX, bc.y, bc.z + d.offsetZ);
                if (!area.contains(bc2) && !areaToExclude.contains(bc2)) {
                    border.add(bc2);
                }
            }
        }

        return border;
    }

    public static Random getChunkYearRandom(World world, int chunkX, int chunkZ, int year) {
        return new Random(world.getSeed() + (chunkX * chunkX * 4987142L) + (chunkX * 5947611L) + (chunkZ * chunkZ * 4392871L + (chunkZ * 389711L) ^ year));
    }

}
