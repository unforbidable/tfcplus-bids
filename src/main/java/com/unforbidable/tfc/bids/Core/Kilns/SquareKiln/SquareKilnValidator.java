package com.unforbidable.tfc.bids.Core.Kilns.SquareKiln;

import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationException;
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
    public SquareKilnValidationParams validate() throws KilnValidationException {
        validateFireHole();

        SquareKilnValidationParams params = determineKilnParams();

        validateEntryTunnel(params);
        validateWalls(params);
        validateChamber(params);

        return params;
    }

    private void validateChamber(SquareKilnValidationParams params) throws KilnValidationException {
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
                requireAirOrPottery(x, 1, z);

                for (int y = 2; y < params.height + 1; y++) {
                    requireAir(x, y, z);
                }
            }
        }
    }

    private void validateFireHole() throws KilnValidationException {
        requireAirOrFire(0, 1, 0);
        requireWall(0, 2, 0, ForgeDirection.UP);
    }

    private void validateEntryTunnel(SquareKilnValidationParams params) throws KilnValidationException {
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

    private SquareKilnValidationParams determineKilnParams() throws KilnValidationException {
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
            throw new KilnValidationException(String.format("Expected exactly 1 chimney %s in the opposite chamber corner at +2 or +3", direction));
        }

        return new SquareKilnValidationParams(direction, chimneyRotation, height);
    }

    private void validateWalls(SquareKilnValidationParams params) throws KilnValidationException {
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
                    requireWall(x, 1 + params.height, z, ForgeDirection.UP);
                }

                requireWall(x, 0, z, ForgeDirection.DOWN);
            }
        }

        for (int y = 1; y < params.height + 1; y++) {
            // Opposite wall
            requireWall(chamberDir.offsetX * 4, y, chamberDir.offsetZ * 4, chamberDir);
            requireWall(chamberDir.offsetX * 4 + chimneyDir.offsetX, y, chamberDir.offsetZ * 4 + chimneyDir.offsetZ, chamberDir);

            // Near wall
            requireWall(chimneyDirOpp.offsetX + chamberDir.offsetX * 2, y, chimneyDirOpp.offsetZ + chamberDir.offsetZ * 2, chimneyDirOpp);
            requireWall(chimneyDirOpp.offsetX + chamberDir.offsetX * 3, y, chimneyDirOpp.offsetZ + chamberDir.offsetZ * 3, chimneyDirOpp);

            // Far wall
            requireWall(chimneyDir.offsetX * 2 + chamberDir.offsetX * 2, y, chimneyDir.offsetZ * 2 + chamberDir.offsetZ * 2, chimneyDir);
            requireWall(chimneyDir.offsetX * 2 + chamberDir.offsetX * 3, y, chimneyDir.offsetZ * 2 + chamberDir.offsetZ * 3, chimneyDir);

            // Short wall
            requireWall(chamberDir.offsetX + chimneyDir.offsetX, y, chimneyDir.offsetZ + chamberDir.offsetZ, chamberDirOpp);

            if (y > 1) {
                requireWall(chamberDir.offsetX, y, chamberDir.offsetZ, chamberDirOpp);
            }
        }
    }

}
