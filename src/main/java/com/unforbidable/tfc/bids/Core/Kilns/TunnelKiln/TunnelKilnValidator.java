package com.unforbidable.tfc.bids.Core.Kilns.TunnelKiln;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidator;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class TunnelKilnValidator extends KilnValidator<TunnelKilnValidationParams> {

    private static final int MAX_HEIGHT = 2;

    public TunnelKilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        super(world, sourceX, sourceY, sourceZ);
    }

    @Override
    public BlockCoord getChimneyLocation(TunnelKilnValidationParams params) {
        ForgeDirection d = params.direction;
        int x = sourceX + d.offsetX * 5;
        int y = sourceY + 1 + params.height;
        int z = sourceZ + d.offsetZ * 5;

        return new BlockCoord(x, y, z);
    }

    @Override
    public List<BlockCoord> getPotteryLocations(TunnelKilnValidationParams params) {
        ForgeDirection d = params.direction;

        List<BlockCoord> list = new ArrayList<BlockCoord>();

        for (int i = 2; i <= 5; i++) {
            int x = sourceX + d.offsetX * i;
            int y = sourceY + 1;
            int z = sourceZ + d.offsetZ * i;
            list.add(new BlockCoord(x, y, z));
        }

        return list;
    }

    @Override
    public TunnelKilnValidationParams validate() {
        if (!validateFireHole()) {
            return null;
        }

        TunnelKilnValidationParams params = determineKilnParams();
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

    private TunnelKilnValidationParams determineKilnParams() {
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
        int tunnelEndZ = direction.offsetZ * 5;

        int height = 0;
        for (int h = 1; h <= MAX_HEIGHT; h++) {
            if (checkChimneyTier(tunnelEndX, 1 + h, tunnelEndZ, 0)) {
                height = h;
                break;
            }
        }

        if (height == 0) {
            Bids.LOG.debug("Expected chimney in tunnel end");
            return null;
        }

        return new TunnelKilnValidationParams(direction, height);
    }

    private boolean validateEntryTunnel(TunnelKilnValidationParams params) {
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

    private boolean validateTunnelEnd(TunnelKilnValidationParams params) {
        ForgeDirection dir = params.direction;

        int tunnelX = dir.offsetX * 5;
        int tunnelZ = dir.offsetZ * 5;

        // Require air or pottery
        if (!requireAirOrPottery(tunnelX, 1, tunnelZ)) {
            return false;
        }

        // Require air
        for (int y = 2; y < params.height + 1; y++) {
            if (!requireAir(tunnelX, y, tunnelZ)) {
                return false;
            }
        }

        // Require wall bottom
        if (!requireWall(tunnelX, 0, tunnelZ, ForgeDirection.DOWN)) {
            return false;
        }

        for (int y = 1; y < params.height + 1; y++) {
            // Require wall side 1
            ForgeDirection side1 = dir.getRotation(ForgeDirection.UP);
            if (!requireWall(tunnelX + side1.offsetX, y, tunnelZ + side1.offsetZ, side1)) {
                return false;
            }

            // Require wall side 2
            ForgeDirection side2 = dir.getRotation(ForgeDirection.DOWN);
            if (!requireWall(tunnelX + side2.offsetX, y, tunnelZ + side2.offsetZ, side2)) {
                return false;
            }

            // Require wall front
            if (!requireWall(tunnelX + dir.offsetX, y, tunnelZ + dir.offsetZ, dir)) {
                return false;
            }
        }

        return true;
    }

    private boolean validateTunnel(TunnelKilnValidationParams params) {
        for (int i = 2; i < 5; i++) {
            int tunnelX = params.direction.offsetX * i;
            int tunnelZ = params.direction.offsetZ * i;

            // Require air or pottery
            if (!requireAirOrPottery(tunnelX, 1, tunnelZ)) {
                return false;
            }

            // Require air
            for (int y = 2; y < params.height + 1; y++) {
                if (!requireAir(tunnelX, y, tunnelZ)) {
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

            for (int y = 1; y < params.height + 1; y++) {
                // Require wall side 1
                ForgeDirection side1 = params.direction.getRotation(ForgeDirection.UP);
                if (!requireWall(tunnelX + side1.offsetX, y, tunnelZ + side1.offsetZ, side1)) {
                    return false;
                }

                // Require wall side 2
                ForgeDirection side2 = params.direction.getRotation(ForgeDirection.DOWN);
                if (!requireWall(tunnelX + side2.offsetX, y, tunnelZ + side2.offsetZ, side2)) {
                    return false;
                }
            }
        }

        // Wall above heat source
        for (int y = 2; y < params.height + 1; y++) {
            if (!requireWall(0, y, 0, params.direction.getOpposite())) {
                return false;
            }
        }

        return true;
    }

}
