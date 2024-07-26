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
        if (!checkAirOrFire(0, 1, 0)) {
            Bids.LOG.debug("Expected air or fire at +1");
            return false;
        }

        // Require wall above
        if (!checkWall(0, 2, 0, ForgeDirection.UP)) {
            Bids.LOG.debug("Expected roof at +2");
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
        if (!checkAir(x, y, z)) {
            Bids.LOG.debug("Expected air in entry tunnel +1");
            return false;
        }

        // Floor
        if (!checkWall(x, y - 1, z, ForgeDirection.DOWN)) {
            Bids.LOG.debug("Expected floor in entry tunnel +1");
            return false;
        }

        // Roof
        if (!checkWall(x, y + 1, z, ForgeDirection.UP)) {
            Bids.LOG.debug("Expected roof in entry tunnel +1");
            return false;
        }

        // Left
        if (!checkWall(x + leftDir.offsetX, y, z + leftDir.offsetZ, leftDir)) {
            Bids.LOG.debug("Expected left wall in entry tunnel +1");
            return false;
        }

        // Right
        if (!checkWall(x + rightDir.offsetX, y, z + rightDir.offsetZ, rightDir)) {
            Bids.LOG.debug("Expected right wall in entry tunnel +1");
            return false;
        }

        return true;
    }

    private boolean validateTunnelEnd(TunnelKilnValidatorParams params) {
        ForgeDirection dir = params.direction;

        int tunnelX = dir.offsetX * 5;
        int tunnelZ = dir.offsetZ * 5;

        // Require air or pottery
        if (!checkAirOrPottery(tunnelX, 1, tunnelZ)) {
            Bids.LOG.debug("Expected air or pottery in tunnel end");
            return false;
        }

        // Require air
        for (int j = 1; j < params.height; j++) {
            if (!checkAir(tunnelX, 1 + j, tunnelZ)) {
                Bids.LOG.debug("Expected air or pottery in +{} tunnel end", (j + 1));
                return false;
            }
        }

        // Require wall bottom
        if (!checkWall(tunnelX, 0, tunnelZ, ForgeDirection.DOWN)) {
            Bids.LOG.debug("Expected floor in tunnel end");
            return false;
        }

        for (int j = 0; j < params.height; j++) {
            // Require wall side 1
            ForgeDirection side1 = dir.getRotation(ForgeDirection.UP);
            if (!checkWall(tunnelX + side1.offsetX, 1 + j, tunnelZ + side1.offsetZ, side1)) {
                Bids.LOG.debug("Expected floor in +{} tunnel end side 1", (j + 1));
                return false;
            }

            // Require wall side 2
            ForgeDirection side2 = dir.getRotation(ForgeDirection.DOWN);
            if (!checkWall(tunnelX + side2.offsetX, 1 + j, tunnelZ + side2.offsetZ, side2)) {
                Bids.LOG.debug("Expected floor in +{} tunnel end side 2", (j + 1));
                return false;
            }

            // Require wall front
            if (!checkWall(tunnelX + dir.offsetX, 1 + j, tunnelZ + dir.offsetZ, dir)) {
                Bids.LOG.debug("Expected floor in +{} tunnel end front", (j + 1));
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
            if (!checkAirOrPottery(tunnelX, 1, tunnelZ)) {
                Bids.LOG.debug("Expected air or pottery at +1 in tunnel section {}", i);
                return false;
            }

            // Require air
            for (int j = 1; j < params.height; j++) {
                if (!checkAir(tunnelX, 1 + j, tunnelZ)) {
                    Bids.LOG.debug("Expected air at +{} in tunnel section {}", (j + 1), i);
                    return false;
                }
            }

            // Require wall top
            if (!checkWall(tunnelX, 1 + params.height, tunnelZ, ForgeDirection.UP)) {
                Bids.LOG.debug("Expected roof in tunnel section {}", i);
                return false;
            }

            // Require wall bottom
            if (!checkWall(tunnelX, 0, tunnelZ, ForgeDirection.DOWN)) {
                Bids.LOG.debug("Expected floor in tunnel section {}", i);
                return false;
            }

            for (int j = 0; j < params.height; j++) {
                // Require wall side 1
                ForgeDirection side1 = params.direction.getRotation(ForgeDirection.UP);
                if (!checkWall(tunnelX + side1.offsetX, 1 + j, tunnelZ + side1.offsetZ, side1)) {
                    Bids.LOG.debug("Expected wall at +{} in tunnel section {} side 1", (j + 1), i);
                    return false;
                }

                // Require wall side 2
                ForgeDirection side2 = params.direction.getRotation(ForgeDirection.DOWN);
                if (!checkWall(tunnelX + side2.offsetX, 1 + j, tunnelZ + side2.offsetZ, side2)) {
                    Bids.LOG.debug("Expected wall at +{} in tunnel section {} side 2", (j + 1), i);
                    return false;
                }
            }
        }

        // Wall above heat source
        for (int i = 1; i < params.height; i++) {
            if (!checkWall(0, 1 + i, 0, params.direction.getOpposite())) {
                Bids.LOG.debug("Expected wall at +{} tunnel start {}", (1 + i), params.direction.getOpposite());
                return false;
            }
        }

        return true;
    }

}
