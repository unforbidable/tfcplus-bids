package com.unforbidable.tfc.bids.Core.Kilns.TunnelKiln;

import com.unforbidable.tfc.bids.Bids;
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

        if (!validateEntryTunnel(params)) {
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
        if (!requireAirOrFire(0, 1, 0)) {
            return false;
        }

        // Require wall above
        if (!requireWall(0, 2, 0, ForgeDirection.UP)) {
            return false;
        }

        return true;
    }

    private TunnelKilnValidatorParams determineKilnParams() {
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
        int tunnelEndX = direction.offsetX * 5;
        int tunnelEndY = 1;
        int tunnelEndZ = direction.offsetZ * 5;

        int height = 0;
        for (int i = 1; i <= MAX_HEIGHT; i++) {
            if (checkChimneyTier(tunnelEndX, tunnelEndY + i, tunnelEndZ, 0)) {
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

    private boolean validateEntryTunnel(TunnelKilnValidatorParams params) {
        ForgeDirection chamberDir = params.direction;
        ForgeDirection leftDir = chamberDir.getRotation(ForgeDirection.UP);
        ForgeDirection rightDir = chamberDir.getRotation(ForgeDirection.DOWN);

        int x = chamberDir.offsetX;
        int y = 1;
        int z = chamberDir.offsetZ;

        // Air
        if (!requireAir(x, y, z)) {
            return false;
        }

        // Floor
        if (!requireWall(x, y - 1, z, ForgeDirection.DOWN)) {
            return false;
        }

        // Roof
        if (!requireWall(x, y + 1, z, ForgeDirection.UP)) {
            return false;
        }

        // Left
        if (!requireWall(x + leftDir.offsetX, y, z + leftDir.offsetZ, leftDir)) {
            return false;
        }

        // Right
        if (!requireWall(x + rightDir.offsetX, y, z + rightDir.offsetZ, rightDir)) {
            return false;
        }

        return true;
    }

    private boolean validateTunnelEnd(TunnelKilnValidatorParams params) {
        ForgeDirection dir = params.direction;

        int tunnelX = dir.offsetX * 5;
        int tunnelZ = dir.offsetZ * 5;

        // Require air or pottery
        if (!requireAirOrPottery(tunnelX, 1, tunnelZ)) {
            return false;
        }

        // Require air
        for (int j = 1; j < params.height; j++) {
            if (!requireAir(tunnelX, 1 + j, tunnelZ)) {
                return false;
            }
        }

        // Require wall bottom
        if (!requireWall(tunnelX, 0, tunnelZ, ForgeDirection.DOWN)) {
            return false;
        }

        for (int j = 0; j < params.height; j++) {
            // Require wall side 1
            ForgeDirection side1 = dir.getRotation(ForgeDirection.UP);
            if (!requireWall(tunnelX + side1.offsetX, 1 + j, tunnelZ + side1.offsetZ, side1)) {
                return false;
            }

            // Require wall side 2
            ForgeDirection side2 = dir.getRotation(ForgeDirection.DOWN);
            if (!requireWall(tunnelX + side2.offsetX, 1 + j, tunnelZ + side2.offsetZ, side2)) {
                return false;
            }

            // Require wall front
            if (!requireWall(tunnelX + dir.offsetX, 1 + j, tunnelZ + dir.offsetZ, dir)) {
                return false;
            }
        }

        return true;
    }

    private boolean validateTunnel(TunnelKilnValidatorParams params) {
        for (int i = 2; i < 5; i++) {
            int tunnelX = params.direction.offsetX * i;
            int tunnelZ = params.direction.offsetZ * i;

            // Require air or pottery
            if (!requireAirOrPottery(tunnelX, 1, tunnelZ)) {
                return false;
            }

            // Require air
            for (int j = 1; j < params.height; j++) {
                if (!requireAir(tunnelX, 1 + j, tunnelZ)) {
                    return false;
                }
            }

            // Require wall top
            if (!requireWall(tunnelX, 1 + params.height, tunnelZ, ForgeDirection.UP)) {
                return false;
            }

            // Require wall bottom
            if (!requireWall(tunnelX, 0, tunnelZ, ForgeDirection.DOWN)) {
                return false;
            }

            for (int j = 0; j < params.height; j++) {
                // Require wall side 1
                ForgeDirection side1 = params.direction.getRotation(ForgeDirection.UP);
                if (!requireWall(tunnelX + side1.offsetX, 1 + j, tunnelZ + side1.offsetZ, side1)) {
                    return false;
                }

                // Require wall side 2
                ForgeDirection side2 = params.direction.getRotation(ForgeDirection.DOWN);
                if (!requireWall(tunnelX + side2.offsetX, 1 + j, tunnelZ + side2.offsetZ, side2)) {
                    return false;
                }
            }
        }

        // Wall above heat source
        for (int i = 1; i < params.height; i++) {
            if (!requireWall(0, 1 + i, 0, params.direction.getOpposite())) {
                return false;
            }
        }

        return true;
    }

}
