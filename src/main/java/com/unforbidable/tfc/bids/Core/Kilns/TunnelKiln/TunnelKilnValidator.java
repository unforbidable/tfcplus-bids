package com.unforbidable.tfc.bids.Core.Kilns.TunnelKiln;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationHelper;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidator;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TunnelKilnValidator extends KilnValidator<TunnelKilnValidatorParams> {

    private static final int MAX_HEIGHT = 2;

    static final ForgeDirection[] HORIZONTAL_DIRECTIONS = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };

    public TunnelKilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        super(world, sourceX, sourceY, sourceZ);
    }

    @Override
    protected TunnelKilnValidatorParams validateStructure() {
        if (!validateFireHole()) {
            return null;
        }

        TunnelKilnValidatorParams params = determineKilnParams();
        if (params == null) {
            return null;
        }

        if (!validateTunnelEnd(params)) {
            return null;
        }

        if (!validateTunnel(params)) {
            return null;
        }

        return params;
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

    private TunnelKilnValidatorParams determineKilnParams() {
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
        int tunnelEndX = sourceX + direction.offsetX * 4;
        int tunnelEndY = sourceY + 1;
        int tunnelEndZ = sourceZ + direction.offsetZ * 4;

        int height = 0;
        for (int i = 1; i <= MAX_HEIGHT; i++) {
            if (KilnValidationHelper.isChimneyTier(world, tunnelEndX, tunnelEndY + i, tunnelEndZ, 0)) {
                height = i;
                break;
            }
        }

        if (height == 0) {
            Bids.LOG.debug("Expected chimney in tunnel end");
            return null;
        }

        return new TunnelKilnValidatorParams(direction, height);
    }

    private boolean validateTunnelEnd(TunnelKilnValidatorParams params) {
        ForgeDirection dir = params.direction;

        int tunnelX = sourceX + dir.offsetX * 4;
        int tunnelY = sourceY + 1;
        int tunnelZ = sourceZ + dir.offsetZ * 4;

        // Require air or pottery
        if (!KilnValidationHelper.isAirOrPottery(world, tunnelX, tunnelY, tunnelZ)) {
            Bids.LOG.debug("Expected air or pottery in tunnel end");
            return false;
        }

        // Require air
        for (int j = 1; j < params.height; j++) {
            if (!KilnValidationHelper.isAir(world, tunnelX, tunnelY + j, tunnelZ)) {
                Bids.LOG.debug("Expected air or pottery in +{} tunnel end", (j + 1));
                return false;
            }
        }

        // Require wall bottom
        if (!KilnValidationHelper.isWall(world, tunnelX, tunnelY - 1, tunnelZ, ForgeDirection.DOWN)) {
            Bids.LOG.debug("Expected floor in tunnel end");
            return false;
        }

        for (int j = 0; j < params.height; j++) {
            // Require wall side 1
            ForgeDirection side1 = dir.getRotation(ForgeDirection.UP);
            if (!KilnValidationHelper.isWall(world, tunnelX + side1.offsetX, tunnelY + j, tunnelZ + side1.offsetZ, side1)) {
                Bids.LOG.debug("Expected floor in +{} tunnel end side 1", (j + 1));
                return false;
            }

            // Require wall side 2
            ForgeDirection side2 = dir.getRotation(ForgeDirection.DOWN);
            if (!KilnValidationHelper.isWall(world, tunnelX + side2.offsetX, tunnelY + j, tunnelZ + side2.offsetZ, side2)) {
                Bids.LOG.debug("Expected floor in +{} tunnel end side 2", (j + 1));
                return false;
            }

            // Require wall front
            if (!KilnValidationHelper.isWall(world, tunnelX + dir.offsetX, tunnelY + j, tunnelZ + dir.offsetZ, dir)) {
                Bids.LOG.debug("Expected floor in +{} tunnel end front", (j + 1));
                return false;
            }
        }

        return true;
    }

    private boolean validateTunnel(TunnelKilnValidatorParams params) {
        for (int i = 1; i < 4; i++) {
            int tunnelX = sourceX + params.direction.offsetX * i;
            int tunnelY = sourceY + 1;
            int tunnelZ = sourceZ + params.direction.offsetZ * i;

            // Require air or pottery
            if (!KilnValidationHelper.isAirOrPottery(world, tunnelX, tunnelY, tunnelZ)) {
                Bids.LOG.debug("Expected air or pottery at +1 in tunnel section {}", i);
                return false;
            }

            // Require air
            for (int j = 1; j < params.height; j++) {
                if (!KilnValidationHelper.isAir(world, tunnelX, tunnelY + j, tunnelZ)) {
                    Bids.LOG.debug("Expected air at +{} in tunnel section {}", (j + 1), i);
                    return false;
                }
            }

            // Require wall top
            if (!KilnValidationHelper.isWall(world, tunnelX, tunnelY + params.height, tunnelZ, ForgeDirection.UP)) {
                Bids.LOG.debug("Expected roof in tunnel section {}", i);
                return false;
            }

            // Require wall bottom
            if (!KilnValidationHelper.isWall(world, tunnelX, tunnelY - 1, tunnelZ, ForgeDirection.DOWN)) {
                Bids.LOG.debug("Expected floor in tunnel section {}", i);
                return false;
            }

            for (int j = 0; j < params.height; j++) {
                // Require wall side 1
                ForgeDirection side1 = params.direction.getRotation(ForgeDirection.UP);
                if (!KilnValidationHelper.isWall(world, tunnelX + side1.offsetX, tunnelY + j, tunnelZ + side1.offsetZ, side1)) {
                    Bids.LOG.debug("Expected wall at +{} in tunnel section {} side 1", (j + 1), i);
                    return false;
                }

                // Require wall side 2
                ForgeDirection side2 = params.direction.getRotation(ForgeDirection.DOWN);
                if (!KilnValidationHelper.isWall(world, tunnelX + side2.offsetX, tunnelY + j, tunnelZ + side2.offsetZ, side2)) {
                    Bids.LOG.debug("Expected wall at +{} in tunnel section {} side 2", (j + 1), i);
                    return false;
                }
            }
        }

        // Wall above heat source
        for (int i = 1; i < params.height; i++) {
            if (!KilnValidationHelper.isWall(world, sourceX, sourceY + 1 + i, sourceZ, params.direction.getOpposite())) {
                Bids.LOG.debug("Expected wall at +{} tunnel start {}", (1 + i), params.direction.getOpposite());
                return false;
            }
        }

        return true;
    }

}
