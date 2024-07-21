package com.unforbidable.tfc.bids.Core.Kilns.SquareKiln;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationHelper;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidator;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static com.unforbidable.tfc.bids.Core.Kilns.SquareKiln.SquareKilnValidationResult.failure;
import static com.unforbidable.tfc.bids.Core.Kilns.SquareKiln.SquareKilnValidationResult.success;

public class SquareKilnValidator extends KilnValidator<SquareKilnValidationResult> {

    static final ForgeDirection[] HORIZONTAL_DIRECTIONS = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };

    public SquareKilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        super(world, sourceX, sourceY, sourceZ);
    }

    public ForgeDirection getDirection() {
        return getResult().direction;
    }

    public SquareKilnChimneyRotation getChimneyRotation() {
        return getResult().chimneyRotation;
    }

    @Override
    protected SquareKilnValidationResult validateStructure() {
        if (!validateFireHole()) {
            return failure();
        }

        ForgeDirection kilnDirection = determineKilnDirection();
        if (kilnDirection == ForgeDirection.UNKNOWN) {
            return failure();
        }

        SquareKilnChimneyRotation chimneyRotation = determineChimneyOffset(kilnDirection);
        if (chimneyRotation == SquareKilnChimneyRotation.UNKNOWN) {
            return failure();
        }

        if (!validateWalls(kilnDirection, chimneyRotation)) {
            return failure();
        }

        return success(kilnDirection, chimneyRotation);
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

    private SquareKilnChimneyRotation determineChimneyOffset(ForgeDirection dir) {
        int x = sourceX + dir.offsetX * 2;
        int y = sourceY + 2;
        int z = sourceZ + dir.offsetZ * 2;

        int chimneyCount = 0;
        SquareKilnChimneyRotation lastRotation = SquareKilnChimneyRotation.UNKNOWN;
        for (SquareKilnChimneyRotation rotation : SquareKilnChimneyRotation.VALID_OFFSETS) {
            ForgeDirection r = dir.getRotation(rotation.getAxis());

            if (KilnValidationHelper.isChimneyTier(world, x + r.offsetX, y, z + r.offsetZ, 0)) {
                chimneyCount++;
                lastRotation = rotation;
            }
        }

        if (chimneyCount == 1) {
            return lastRotation;
        } else {
            Bids.LOG.debug("Expected exactly 1 chimney +2 {} and +1 {} or {}", dir, dir.getRotation(ForgeDirection.UP), dir.getRotation(ForgeDirection.DOWN));
            return SquareKilnChimneyRotation.UNKNOWN;
        }
    }

    private ForgeDirection determineKilnDirection() {
        ForgeDirection kilnDirection = ForgeDirection.UNKNOWN;
        int wallCount = 0;
        for (ForgeDirection d : HORIZONTAL_DIRECTIONS) {
            if (KilnValidationHelper.isWall(world, sourceX + d.offsetX, sourceY + 1, sourceZ + d.offsetZ, d)) {
                wallCount++;
            } else {
                kilnDirection = d;
            }
        }

        // Require exactly 3 wall
        if (wallCount == 3) {
            return kilnDirection;
        } else {
            Bids.LOG.debug("Expected exactly 3 walls surrounding +1");
            return ForgeDirection.UNKNOWN;
        }
    }

    private boolean validateWalls(ForgeDirection chamberDir, SquareKilnChimneyRotation chimneyRotation) {
        ForgeDirection chamberDirOpp = chamberDir.getOpposite();
        ForgeDirection chimneyDir = chamberDir.getRotation(chimneyRotation.getAxis());
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
                    if (!KilnValidationHelper.isWall(world, sourceX + i, sourceY + 2, sourceZ + j, ForgeDirection.UP)) {
                        Bids.LOG.debug("Expected roof at {},{} +2", i, j);
                        return false;
                    }
                }

                if (!KilnValidationHelper.isWall(world, sourceX + i, sourceY, sourceZ + j, ForgeDirection.DOWN)) {
                    Bids.LOG.debug("Expected floor at {},{}", i, j);
                    return false;
                }
            }
        }

        // Opposite wall
        if (!KilnValidationHelper.isWall(world, sourceX + chamberDir.offsetX * 3, sourceY + 1, sourceZ + chamberDir.offsetZ * 3, chamberDir)) {
            Bids.LOG.debug("Expected opposite wall 1 {}", chamberDir);
            return false;
        }
        if (!KilnValidationHelper.isWall(world, sourceX + chamberDir.offsetX * 3 + chimneyDir.offsetX, sourceY + 1, sourceZ + chamberDir.offsetZ * 3 + chimneyDir.offsetZ, chamberDir)) {
            Bids.LOG.debug("Expected opposite wall 2 {}", chamberDir);
            return false;
        }

        // Near wall
        if (!KilnValidationHelper.isWall(world, sourceX + chimneyDirOpp.offsetX + chamberDir.offsetX, sourceY + 1, sourceZ + chimneyDirOpp.offsetZ + chamberDir.offsetZ, chimneyDirOpp)) {
            Bids.LOG.debug("Expected near wall 1 {}", chimneyDirOpp);
            return false;
        }
        if (!KilnValidationHelper.isWall(world, sourceX + chimneyDirOpp.offsetX + chamberDir.offsetX * 2, sourceY + 1, sourceZ + chimneyDirOpp.offsetZ + chamberDir.offsetZ * 2, chimneyDirOpp)) {
            Bids.LOG.debug("Expected near wall 2 {}", chimneyDirOpp);
            return false;
        }

        // Far wall
        if (!KilnValidationHelper.isWall(world, sourceX + chimneyDir.offsetX * 2 + chamberDir.offsetX, sourceY + 1, sourceZ + chimneyDir.offsetZ * 2 + chamberDir.offsetZ, chimneyDir)) {
            Bids.LOG.debug("Expected far wall 1 {}", chimneyDir);
            return false;
        }
        if (!KilnValidationHelper.isWall(world, sourceX + chimneyDir.offsetX * 2 + chamberDir.offsetX * 2, sourceY + 1, sourceZ + chimneyDir.offsetZ * 2 + chamberDir.offsetZ * 2, chimneyDir)) {
            Bids.LOG.debug("Expected far wall 2 {}", chimneyDir);
            return false;
        }

        // Short wall
        if (!KilnValidationHelper.isWall(world, sourceX + chimneyDir.offsetX, sourceY + 1, sourceZ + chimneyDir.offsetZ, chamberDirOpp)) {
            Bids.LOG.debug("Expected short wall {}", chamberDirOpp);
            return false;
        }

        return true;
    }

}
