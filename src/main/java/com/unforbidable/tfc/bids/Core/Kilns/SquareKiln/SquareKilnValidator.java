package com.unforbidable.tfc.bids.Core.Kilns.SquareKiln;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidator;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class SquareKilnValidator extends KilnValidator<SquareKilnValidationParams> {

    private static final int MAX_HEIGHT = 2;

    public SquareKilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        super(world, sourceX, sourceY, sourceZ);
    }

    @Override
    public BlockCoord getChimneyLocation(SquareKilnValidationParams params) {
        ForgeDirection chamberDir = params.direction;
        SquareKilnChimneyRotation chimneyRotation = params.chimneyRotation;
        ForgeDirection chimneyDir = chamberDir.getRotation(chimneyRotation.getAxis());

        int x = sourceX + chamberDir.offsetX * 3 + chimneyDir.offsetX;
        int y = sourceY + 1 + params.height;
        int z = sourceZ + chamberDir.offsetZ * 3 + chimneyDir.offsetZ;
        return new BlockCoord(x, y, z);
    }

    @Override
    public List<BlockCoord> getPotteryLocations(SquareKilnValidationParams params) {
        ForgeDirection chamberDir = params.direction;
        SquareKilnChimneyRotation chimneyRotation = params.chimneyRotation;
        ForgeDirection chimneyDir = chamberDir.getRotation(chimneyRotation.getAxis());

        int entryX = chamberDir.offsetX * 2;
        int entryZ = chamberDir.offsetZ * 2;
        int chimneyX = chamberDir.offsetX * 3 + chimneyDir.offsetX;
        int chimneyZ =  chamberDir.offsetZ * 3 + chimneyDir.offsetZ;
        int minX = Math.min(entryX, chimneyX);
        int minZ = Math.min(entryZ, chimneyZ);

        List<BlockCoord> list = new ArrayList<BlockCoord>();

        for (int x = minX; x < minX + 2; x++) {
            for (int z = minZ; z < minZ + 2; z++) {
                list.add(new BlockCoord(sourceX + x, sourceY + 1, sourceZ + z));
            }
        }

        return list;
    }

    @Override
    public SquareKilnValidationParams validate() {
        if (!validateFireHole()) {
            return null;
        }

        SquareKilnValidationParams params = determineKilnParams();
        if (params == null) {
            return null;
        }

        if (!validateEntryTunnel(params)) {
            return null;
        }

        if (!validateWalls(params)) {
            return null;
        }

        if (!validateChamber(params)) {
            return null;
        }

        return params;
    }

    private boolean validateChamber(SquareKilnValidationParams params) {
        ForgeDirection chamberDir = params.direction;
        SquareKilnChimneyRotation chimneyRotation = params.chimneyRotation;
        ForgeDirection chimneyDir = chamberDir.getRotation(chimneyRotation.getAxis());

        int entryX = chamberDir.offsetX * 2;
        int entryZ = chamberDir.offsetZ * 2;
        int chimneyX = chamberDir.offsetX * 3 + chimneyDir.offsetX;
        int chimneyZ =  chamberDir.offsetZ * 3 + chimneyDir.offsetZ;
        int minX = Math.min(entryX, chimneyX);
        int minZ = Math.min(entryZ, chimneyZ);

        for (int x = minX; x < minX + 2; x++) {
            for (int z = minZ; z < minZ + 2; z++) {
                if (!requireAirOrPottery(x, 1, z)) {
                    return false;
                }

                for (int y = 2; y < params.height + 1; y++) {
                    if (!requireAir(x, y, z)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean validateFireHole() {
        if (!requireAirOrFire(0, 1, 0)) {
            return false;
        }

        if (!requireWall(0, 2, 0, ForgeDirection.UP)) {
            return false;
        }

        return true;
    }

    private boolean validateEntryTunnel(SquareKilnValidationParams params) {
        ForgeDirection chamberDir = params.direction;
        ForgeDirection leftDir = chamberDir.getRotation(ForgeDirection.UP);
        ForgeDirection rightDir = chamberDir.getRotation(ForgeDirection.DOWN);

        int x = chamberDir.offsetX;
        int z = chamberDir.offsetZ;

        // Air
        if (!requireAir(x, 1, z)) {
            return false;
        }

        // Floor
        if (!requireWall(x, 0, z, ForgeDirection.DOWN)) {
            return false;
        }

        // Roof
        if (!requireWall(x, 2, z, ForgeDirection.UP)) {
            return false;
        }

        // Left
        if (!requireWall(x + leftDir.offsetX, 1, z + leftDir.offsetZ, leftDir)) {
            return false;
        }

        // Right
        if (!requireWall(x + rightDir.offsetX, 1, z + rightDir.offsetZ, rightDir)) {
            return false;
        }

        return true;
    }

    private SquareKilnValidationParams determineKilnParams() {
        ForgeDirection direction = ForgeDirection.UNKNOWN;
        int wallCount = 0;
        for (ForgeDirection d : HORIZONTAL_DIRECTIONS) {
            if (checkWall(d.offsetX, 1, d.offsetZ, d)) {
                wallCount++;
            } else {
                direction = d;
            }
        }

        // Require exactly 3 wall
        if (wallCount != 3) {
            Bids.LOG.debug("Expected exactly 3 walls surrounding +1");
            return null;
        }

        // Find the chimney
        // Require any chimney top
        int height = 0;
        SquareKilnChimneyRotation chimneyRotation = SquareKilnChimneyRotation.UNKNOWN;

        for (int h = 1; h <= MAX_HEIGHT; h++) {
            int x = direction.offsetX * 3;
            int y = 1 + h;
            int z = direction.offsetZ * 3;

            int chimneyCount = 0;
            SquareKilnChimneyRotation lastChimneyRotation = SquareKilnChimneyRotation.UNKNOWN;

            for (SquareKilnChimneyRotation rotation : SquareKilnChimneyRotation.VALID_OFFSETS) {
                ForgeDirection r = direction.getRotation(rotation.getAxis());

                if (checkChimneyTier(x + r.offsetX, y, z + r.offsetZ, 0)) {
                    chimneyCount++;
                    lastChimneyRotation = rotation;
                }
            }

            if (chimneyCount == 1) {
                height = h;
                chimneyRotation = lastChimneyRotation;
                break;
            }
        }

        if (height == 0) {
            Bids.LOG.debug("Expected exactly 1 chimney in the opposite chamber corner at +2 or +3");
            return null;
        }

        return new SquareKilnValidationParams(direction, chimneyRotation, height);
    }

    private boolean validateWalls(SquareKilnValidationParams params) {
        ForgeDirection chamberDir = params.direction;
        ForgeDirection chamberDirOpp = chamberDir.getOpposite();
        ForgeDirection chimneyDir = chamberDir.getRotation(params.chimneyRotation.getAxis());
        ForgeDirection chimneyDirOpp = chimneyDir.getOpposite();

        int entryX = chamberDir.offsetX * 2;
        int entryZ = chamberDir.offsetZ * 2;
        int chimneyX = chamberDir.offsetX * 3 + chimneyDir.offsetX;
        int chimneyZ =  chamberDir.offsetZ * 3 + chimneyDir.offsetZ;
        int minX = Math.min(entryX, chimneyX);
        int minZ = Math.min(entryZ, chimneyZ);

        for (int x = minX; x < minX + 2; x++) {
            for (int z = minZ; z < minZ + 2; z++) {
                // No need to check chimney
                if (x != chimneyX || z != chimneyZ) {
                    if (!requireWall(x, 1 + params.height, z, ForgeDirection.UP)) {
                        return false;
                    }
                }

                if (!requireWall(x, 0, z, ForgeDirection.DOWN)) {
                    return false;
                }
            }
        }

        for (int y = 1; y < params.height + 1; y++) {

            // Opposite wall
            if (!requireWall(chamberDir.offsetX * 4, y, chamberDir.offsetZ * 4, chamberDir)) {
                return false;
            }
            if (!requireWall(chamberDir.offsetX * 4 + chimneyDir.offsetX, y, chamberDir.offsetZ * 4 + chimneyDir.offsetZ, chamberDir)) {
                return false;
            }

            // Near wall
            if (!requireWall(chimneyDirOpp.offsetX + chamberDir.offsetX * 2, y, chimneyDirOpp.offsetZ + chamberDir.offsetZ * 2, chimneyDirOpp)) {
                return false;
            }
            if (!requireWall(chimneyDirOpp.offsetX + chamberDir.offsetX * 3, y, chimneyDirOpp.offsetZ + chamberDir.offsetZ * 3, chimneyDirOpp)) {
                return false;
            }

            // Far wall
            if (!requireWall(chimneyDir.offsetX * 2 + chamberDir.offsetX * 2, y, chimneyDir.offsetZ * 2 + chamberDir.offsetZ * 2, chimneyDir)) {
                return false;
            }
            if (!requireWall(chimneyDir.offsetX * 2 + chamberDir.offsetX * 3, y, chimneyDir.offsetZ * 2 + chamberDir.offsetZ * 3, chimneyDir)) {
                return false;
            }

            // Short wall
            if (!requireWall(chamberDir.offsetX + chimneyDir.offsetX, y, chimneyDir.offsetZ + chamberDir.offsetZ, chamberDirOpp)) {
                return false;
            }

            if (y > 1) {
                if (!requireWall(chamberDir.offsetX, y, chamberDir.offsetZ, chamberDirOpp)) {
                    return false;
                }
            }
        }

        return true;
    }

}
