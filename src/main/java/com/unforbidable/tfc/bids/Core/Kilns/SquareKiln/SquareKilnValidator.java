package com.unforbidable.tfc.bids.Core.Kilns.SquareKiln;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidator;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class SquareKilnValidator extends KilnValidator<SquareKilnValidationParams> {

    private static final int MAX_HEIGHT = 2;

    static final ForgeDirection[] HORIZONTAL_DIRECTIONS = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };

    public SquareKilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        super(world, sourceX, sourceY, sourceZ);
    }

    @Override
    protected SquareKilnValidationParams validateStructure() {
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

        for (int i = minX; i < minX + 2; i++) {
            for (int j = minZ; j < minZ + 2; j++) {
                if (!requireAirOrPottery(i, 1, j)) {
                    return false;
                }

                for (int k = 1; k < params.height; k++) {
                    if (!requireAir(i, 1 + k, j)) {
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

        for (int i = minX; i < minX + 2; i++) {
            for (int j = minZ; j < minZ + 2; j++) {
                // No need to check chimney
                if (i != chimneyX || j != chimneyZ) {
                    if (!requireWall(i, 1 + params.height, j, ForgeDirection.UP)) {
                        return false;
                    }
                }

                if (!requireWall(i, 0, j, ForgeDirection.DOWN)) {
                    return false;
                }
            }
        }

        for (int i = 0; i < params.height; i++) {

            // Opposite wall
            if (!requireWall(chamberDir.offsetX * 4, 1 + i, chamberDir.offsetZ * 4, chamberDir)) {
                return false;
            }
            if (!requireWall(chamberDir.offsetX * 4 + chimneyDir.offsetX, 1 + i, chamberDir.offsetZ * 4 + chimneyDir.offsetZ, chamberDir)) {
                return false;
            }

            // Near wall
            if (!requireWall(chimneyDirOpp.offsetX + chamberDir.offsetX * 2, 1 + i, chimneyDirOpp.offsetZ + chamberDir.offsetZ * 2, chimneyDirOpp)) {
                return false;
            }
            if (!requireWall(chimneyDirOpp.offsetX + chamberDir.offsetX * 3, 1 + i, chimneyDirOpp.offsetZ + chamberDir.offsetZ * 3, chimneyDirOpp)) {
                return false;
            }

            // Far wall
            if (!requireWall(chimneyDir.offsetX * 2 + chamberDir.offsetX * 2, 1 + i, chimneyDir.offsetZ * 2 + chamberDir.offsetZ * 2, chimneyDir)) {
                return false;
            }
            if (!requireWall(chimneyDir.offsetX * 2 + chamberDir.offsetX * 3, 1 + i, chimneyDir.offsetZ * 2 + chamberDir.offsetZ * 3, chimneyDir)) {
                return false;
            }

            // Short wall
            if (!requireWall(chamberDir.offsetX + chimneyDir.offsetX, 1 + i, chimneyDir.offsetZ + chamberDir.offsetZ, chamberDirOpp)) {
                return false;
            }

            if (i > 1) {
                if (!requireWall(chamberDir.offsetX, 1 + i, chamberDir.offsetZ, chamberDirOpp)) {
                    return false;
                }
            }
        }

        return true;
    }

}
