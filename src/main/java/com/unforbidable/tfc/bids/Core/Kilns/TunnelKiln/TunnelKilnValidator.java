package com.unforbidable.tfc.bids.Core.Kilns.TunnelKiln;

import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationException;
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
        return new BlockCoord(d.offsetX * 5, 1 + params.height, d.offsetZ * 5);
    }

    @Override
    public List<BlockCoord> getPotteryLocations(TunnelKilnValidationParams params) {
        ForgeDirection d = params.direction;

        List<BlockCoord> list = new ArrayList<BlockCoord>();

        for (int i = 3; i <= 6; i++) {
            list.add(new BlockCoord(d.offsetX * i, 1, d.offsetZ * i));
        }

        return list;
    }

    @Override
    public TunnelKilnValidationParams validate() throws KilnValidationException {
        validateFireHole();

        TunnelKilnValidationParams params = determineKilnParams();

        validateEntryTunnel(params);
        validateTunnelEnd(params);
        validateTunnel(params);

        return params;
    }

    private void validateFireHole() throws KilnValidationException {
        // Require air or fire above
        requireAirOrFire(0, 1, 0);

        // Require wall above
        requireWall(0, 2, 0, ForgeDirection.UP);
    }

    private TunnelKilnValidationParams determineKilnParams() throws KilnValidationException {
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
            throw new KilnValidationException("Expected exactly 3 walls surrounding +1");
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
            throw new KilnValidationException(String.format("Expected chimney %s in tunnel end at +2 or +3", direction));
        }

        return new TunnelKilnValidationParams(direction, height);
    }

    private void validateEntryTunnel(TunnelKilnValidationParams params) throws KilnValidationException {
        ForgeDirection chamberDir = params.direction;
        ForgeDirection leftDir = chamberDir.getRotation(ForgeDirection.UP);
        ForgeDirection rightDir = chamberDir.getRotation(ForgeDirection.DOWN);

        int x = chamberDir.offsetX;
        int z = chamberDir.offsetZ;

        requireAir(x, 1, z);
        requireWall(x, 0, z, ForgeDirection.DOWN);
        requireWall(x, 2, z, ForgeDirection.UP);
        requireWall(x + leftDir.offsetX, 1, z + leftDir.offsetZ, leftDir);
        requireWall(x + rightDir.offsetX, 1, z + rightDir.offsetZ, rightDir);
    }

    private void validateTunnelEnd(TunnelKilnValidationParams params) throws KilnValidationException {
        ForgeDirection dir = params.direction;

        int tunnelX = dir.offsetX * 5;
        int tunnelZ = dir.offsetZ * 5;

        // Require air or pottery
        requireAirOrPottery(tunnelX, 1, tunnelZ);

        // Require air
        for (int y = 2; y < params.height + 1; y++) {
            requireAir(tunnelX, y, tunnelZ);
        }

        // Require wall bottom
        requireWall(tunnelX, 0, tunnelZ, ForgeDirection.DOWN);

        for (int y = 1; y < params.height + 1; y++) {
            // Require wall right
            ForgeDirection right = dir.getRotation(ForgeDirection.UP);
            requireWall(tunnelX + right.offsetX, y, tunnelZ + right.offsetZ, right);

            // Require wall left
            ForgeDirection left = dir.getRotation(ForgeDirection.DOWN);
            requireWall(tunnelX + left.offsetX, y, tunnelZ + left.offsetZ, left);

            // Require wall front
            requireWall(tunnelX + dir.offsetX, y, tunnelZ + dir.offsetZ, dir);
        }
    }

    private void validateTunnel(TunnelKilnValidationParams params) throws KilnValidationException {
        for (int i = 2; i < 5; i++) {
            int tunnelX = params.direction.offsetX * i;
            int tunnelZ = params.direction.offsetZ * i;

            // Require air or pottery
            requireAirOrPottery(tunnelX, 1, tunnelZ);

            // Require air
            for (int y = 2; y < params.height + 1; y++) {
                requireAir(tunnelX, y, tunnelZ);
            }

            // Require wall top
            requireWall(tunnelX, 1 + params.height, tunnelZ, ForgeDirection.UP);

            // Require wall bottom
            requireWall(tunnelX, 0, tunnelZ, ForgeDirection.DOWN);

            for (int y = 1; y < params.height + 1; y++) {
                // Require wall right
                ForgeDirection right = params.direction.getRotation(ForgeDirection.UP);
                requireWall(tunnelX + right.offsetX, y, tunnelZ + right.offsetZ, right);

                // Require wall left
                ForgeDirection left = params.direction.getRotation(ForgeDirection.DOWN);
                requireWall(tunnelX + left.offsetX, y, tunnelZ + left.offsetZ, left);
            }
        }

        // Wall above heat source
        for (int y = 2; y < params.height + 1; y++) {
            requireWall(0, y, 0, params.direction.getOpposite());
        }
    }

}
