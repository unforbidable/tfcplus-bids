package com.unforbidable.tfc.bids.Core.Kilns.SquareKiln;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationHelper;
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

        if (!validateWalls(params)) {
            return null;
        }

        return params;
    }

    private boolean validateFireHole() {
        if (!KilnValidationHelper.isAirOrFire(world, sourceX, sourceY + 1, sourceZ)) {
            Bids.LOG.debug("Expected air or fire at +1");
            return false;
        }

        if (!KilnValidationHelper.isWall(world, sourceX, sourceY + 2, sourceZ, ForgeDirection.UP)) {
            Bids.LOG.debug("Expected roof at +2");
            return false;
        }

        return true;
    }

    private SquareKilnValidationParams determineKilnParams() {
        ForgeDirection direction = ForgeDirection.UNKNOWN;
        int wallCount = 0;
        for (ForgeDirection d : HORIZONTAL_DIRECTIONS) {
            if (KilnValidationHelper.isWall(world, sourceX + d.offsetX, sourceY + 1, sourceZ + d.offsetZ, d)) {
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
            int x = sourceX + direction.offsetX * 2;
            int y = sourceY + 1 + h;
            int z = sourceZ + direction.offsetZ * 2;

            int chimneyCount = 0;
            for (SquareKilnChimneyRotation rotation : SquareKilnChimneyRotation.VALID_OFFSETS) {
                ForgeDirection r = direction.getRotation(rotation.getAxis());

                if (KilnValidationHelper.isChimneyTier(world, x + r.offsetX, y, z + r.offsetZ, 0)) {
                    chimneyCount++;
                    chimneyRotation = rotation;
                }
            }

            if (chimneyCount != 1) {
                Bids.LOG.debug("Expected exactly 1 chimney in opposite corner at +{}", (h + 1));
            } else {
                height = h;
                break;
            }
        }

        if (height == 0 || chimneyRotation == SquareKilnChimneyRotation.UNKNOWN) {
            Bids.LOG.debug("Expected chimney in opposite chamber corner at +2 or +3");
            return null;
        }

        return new SquareKilnValidationParams(direction, chimneyRotation, height);
    }

    private boolean validateWalls(SquareKilnValidationParams params) {
        ForgeDirection chamberDir = params.direction;
        ForgeDirection chamberDirOpp = chamberDir.getOpposite();
        ForgeDirection chimneyDir = chamberDir.getRotation(params.chimneyRotation.getAxis());
        ForgeDirection chimneyDirOpp = chimneyDir.getOpposite();

        int entryX = chamberDir.offsetX;
        int entryZ = chamberDir.offsetZ;
        int chimneyX = chamberDir.offsetX * 2 + chimneyDir.offsetX;
        int chimneyZ =  chamberDir.offsetZ * 2 + chimneyDir.offsetZ;
        int minX = Math.min(entryX, chimneyX);
        int minZ = Math.min(entryZ, chimneyZ);

        for (int i = minX; i < minX + 2; i++) {
            for (int j = minZ; j < minZ + 2; j++) {
                // No need to check chimney
                if (i != chimneyX || j != chimneyZ) {
                    if (!KilnValidationHelper.isWall(world, sourceX + i, sourceY + 1 + params.height, sourceZ + j, ForgeDirection.UP)) {
                        Bids.LOG.debug("Expected roof at {},{} +{}", i, j, (params.height + 1));
                        return false;
                    }
                }

                if (!KilnValidationHelper.isWall(world, sourceX + i, sourceY, sourceZ + j, ForgeDirection.DOWN)) {
                    Bids.LOG.debug("Expected floor at {},{}", i, j);
                    return false;
                }
            }
        }

        for (int i = 0; i < params.height; i++) {

            // Opposite wall
            if (!KilnValidationHelper.isWall(world, sourceX + chamberDir.offsetX * 3, sourceY + 1 + i, sourceZ + chamberDir.offsetZ * 3, chamberDir)) {
                Bids.LOG.debug("Expected opposite wall 1 {} at +{}", chamberDir, (1 + i));
                return false;
            }
            if (!KilnValidationHelper.isWall(world, sourceX + chamberDir.offsetX * 3 + chimneyDir.offsetX, sourceY + 1 + i, sourceZ + chamberDir.offsetZ * 3 + chimneyDir.offsetZ, chamberDir)) {
                Bids.LOG.debug("Expected opposite wall 2 {} at +{}", chamberDir, (1 + i));
                return false;
            }

            // Near wall
            if (!KilnValidationHelper.isWall(world, sourceX + chimneyDirOpp.offsetX + chamberDir.offsetX, sourceY + 1 + i, sourceZ + chimneyDirOpp.offsetZ + chamberDir.offsetZ, chimneyDirOpp)) {
                Bids.LOG.debug("Expected near wall 1 {} at +{}", chimneyDirOpp, (1 + i));
                return false;
            }
            if (!KilnValidationHelper.isWall(world, sourceX + chimneyDirOpp.offsetX + chamberDir.offsetX * 2, sourceY + 1 + i, sourceZ + chimneyDirOpp.offsetZ + chamberDir.offsetZ * 2, chimneyDirOpp)) {
                Bids.LOG.debug("Expected near wall 2 {} at +{}", chimneyDirOpp, (1 + i));
                return false;
            }

            // Far wall
            if (!KilnValidationHelper.isWall(world, sourceX + chimneyDir.offsetX * 2 + chamberDir.offsetX, sourceY + 1 + i, sourceZ + chimneyDir.offsetZ * 2 + chamberDir.offsetZ, chimneyDir)) {
                Bids.LOG.debug("Expected far wall 1 {} at +{}", chimneyDir, (1 + i));
                return false;
            }
            if (!KilnValidationHelper.isWall(world, sourceX + chimneyDir.offsetX * 2 + chamberDir.offsetX * 2, sourceY + 1 + i, sourceZ + chimneyDir.offsetZ * 2 + chamberDir.offsetZ * 2, chimneyDir)) {
                Bids.LOG.debug("Expected far wall 2 {} at +{}", chimneyDir, (1 + i));
                return false;
            }

            // Short wall
            if (!KilnValidationHelper.isWall(world, sourceX + chimneyDir.offsetX, sourceY + 1 + i, sourceZ + chimneyDir.offsetZ, chamberDirOpp)) {
                Bids.LOG.debug("Expected short wall {} at +{}", chamberDirOpp, (1 + i));
                return false;
            }

            if (i > 1) {
                if (!KilnValidationHelper.isWall(world, sourceX, sourceY + 1 + i, sourceZ, chamberDirOpp)) {
                    Bids.LOG.debug("Expected short wall {} at +{}", chamberDirOpp, (1 + i));
                    return false;
                }
            }
        }

        return true;
    }

}
