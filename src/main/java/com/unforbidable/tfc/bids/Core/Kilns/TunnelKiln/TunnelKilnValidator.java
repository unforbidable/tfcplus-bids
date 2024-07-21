package com.unforbidable.tfc.bids.Core.Kilns.TunnelKiln;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationHelper;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidator;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static com.unforbidable.tfc.bids.Core.Kilns.TunnelKiln.TunnelKilnValidationResult.failure;
import static com.unforbidable.tfc.bids.Core.Kilns.TunnelKiln.TunnelKilnValidationResult.success;

public class TunnelKilnValidator extends KilnValidator<TunnelKilnValidationResult> {

    static final ForgeDirection[] HORIZONTAL_DIRECTIONS = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };

    public TunnelKilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        super(world, sourceX, sourceY, sourceZ);
    }

    public ForgeDirection getDirection() {
        return getResult().direction;
    }

    @Override
    protected TunnelKilnValidationResult validateStructure() {
        if (!validateFireHole()) {
            return failure();
        }

        ForgeDirection kilnDirection = determineKilnDirection();
        if (kilnDirection == ForgeDirection.UNKNOWN) {
            return failure();
        }

        if (!validateTunnelEnd(kilnDirection)) {
            return failure();
        }

        if (!validateTunnel(kilnDirection)) {
            return failure();
        }

        return success(kilnDirection);
    }

    private boolean validateFireHole() {
        // Require air or fire above
        if (!KilnValidationHelper.isAirOrFire(world, sourceX, sourceY + 1, sourceZ)) {
            Bids.LOG.debug("Expected air or fire at +1");
            return false;
        }

        // Require wall above
        if (!KilnValidationHelper.isWall(world, sourceX, sourceY + 2, sourceZ, ForgeDirection.UP)) {
            Bids.LOG.debug("Expected roof at +2");
            return false;
        }

        return true;
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

    private boolean validateTunnelEnd(ForgeDirection dir) {
        int tunnelX = sourceX + dir.offsetX * 4;
        int tunnelY = sourceY + 1;
        int tunnelZ = sourceZ + dir.offsetZ * 4;

        // Require any chimney top
        if (!KilnValidationHelper.isChimneyTier(world, tunnelX, tunnelY + 1, tunnelZ, 0)) {
            Bids.LOG.debug("Expected chimney in tunnel end");
            return false;
        }

        // Require air or pottery
        if (!KilnValidationHelper.isAirOrPottery(world, tunnelX, tunnelY, tunnelZ)) {
            Bids.LOG.debug("Expected air or pottery in tunnel end");
            return false;
        }

        // Require wall bottom
        if (!KilnValidationHelper.isWall(world, tunnelX, tunnelY - 1, tunnelZ, ForgeDirection.DOWN)) {
            Bids.LOG.debug("Expected floor in tunnel end");
            return false;
        }

        // Require wall side 1
        ForgeDirection side1 = dir.getRotation(ForgeDirection.UP);
        if (!KilnValidationHelper.isWall(world, tunnelX + side1.offsetX, tunnelY, tunnelZ + side1.offsetZ, side1)) {
            Bids.LOG.debug("Expected floor in tunnel end side 1");
            return false;
        }

        // Require wall side 2
        ForgeDirection side2 = dir.getRotation(ForgeDirection.DOWN);
        if (!KilnValidationHelper.isWall(world, tunnelX + side2.offsetX, tunnelY, tunnelZ + side2.offsetZ, side2)) {
            Bids.LOG.debug("Expected floor in tunnel end side 2");
            return false;
        }

        // Require wall front
        if (!KilnValidationHelper.isWall(world, tunnelX + dir.offsetX, tunnelY, tunnelZ + dir.offsetZ, dir)) {
            Bids.LOG.debug("Expected floor in tunnel end front");
            return false;
        }

        return true;
    }

    private boolean validateTunnel(ForgeDirection dir) {
        for (int i = 1; i < 4; i++) {
            int tunnelX = sourceX + dir.offsetX * i;
            int tunnelY = sourceY + 1;
            int tunnelZ = sourceZ + dir.offsetZ * i;

            // Require air or pottery
            if (!KilnValidationHelper.isAirOrPottery(world, tunnelX, tunnelY, tunnelZ)) {
                Bids.LOG.debug("Expected air or pottery in tunnel section {}", i);
                return false;
            }

            // Require wall top
            if (!KilnValidationHelper.isWall(world, tunnelX, tunnelY + 1, tunnelZ, ForgeDirection.UP)) {
                Bids.LOG.debug("Expected roof in tunnel section {}", i);
                return false;
            }

            // Require wall bottom
            if (!KilnValidationHelper.isWall(world, tunnelX, tunnelY - 1, tunnelZ, ForgeDirection.DOWN)) {
                Bids.LOG.debug("Expected floor in tunnel section {}", i);
                return false;
            }

            // Require wall side 1
            ForgeDirection side1 = dir.getRotation(ForgeDirection.UP);
            if (!KilnValidationHelper.isWall(world, tunnelX + side1.offsetX, tunnelY, tunnelZ + side1.offsetZ, side1)) {
                Bids.LOG.debug("Expected wall in tunnel section {} side 1", i);
                return false;
            }

            // Require wall side 2
            ForgeDirection side2 = dir.getRotation(ForgeDirection.DOWN);
            if (!KilnValidationHelper.isWall(world, tunnelX + side2.offsetX, tunnelY, tunnelZ + side2.offsetZ, side2)) {
                Bids.LOG.debug("Expected wall in tunnel section {} side 2", i);
                return false;
            }
        }

        return true;
    }

}
