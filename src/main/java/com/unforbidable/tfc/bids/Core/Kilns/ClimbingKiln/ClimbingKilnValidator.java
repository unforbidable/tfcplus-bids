package com.unforbidable.tfc.bids.Core.Kilns.ClimbingKiln;

import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationException;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidator;
import com.unforbidable.tfc.bids.api.BidsOptions;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class ClimbingKilnValidator extends KilnValidator<ClimbingKilnValidationParams> {

    private static final int MIN_CHIMNEY_TIER = 2;

    public ClimbingKilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        super(world, sourceX, sourceY, sourceZ);
    }

    @Override
    public BlockCoord getChimneyLocation(ClimbingKilnValidationParams params) {
        ForgeDirection d = params.direction;
        int h = params.height;
        return new BlockCoord(d.offsetX * (2 + 2 * h),  4 + h, d.offsetZ * (2 + 2 * h));
    }

    @Override
    public List<BlockCoord> getPotteryLocations(ClimbingKilnValidationParams params) {
        ForgeDirection direction = params.direction;
        ForgeDirection directionLeft = params.direction.getRotation(ForgeDirection.DOWN);

        ArrayList<BlockCoord> list = new ArrayList<BlockCoord>();

        for (int sec = 0; sec <= params.height; sec++) {
            // Floor
            for (int u = -1; u < 2; u++) {
                int y = sec + 2;
                // Middle floor is one block farther
                final int vStart = sec * 2 + (u == 0 ? 1 : 0);

                for (int v = vStart; v < vStart + 2; v++) {
                    list.add(new BlockCoord(direction.offsetX * v + directionLeft.offsetX * u, y, direction.offsetZ * v + directionLeft.offsetZ * u));
                }
            }
        }

        return list;
    }

    @Override
    public ClimbingKilnValidationParams validate() throws KilnValidationException {
        validateFireBox();

        ClimbingKilnValidationParams params = determineKilnParams();
        validateChamber(params);

        return params;
    }

    private void validateFireBox() throws KilnValidationException {
        requireAirOrFire(0, 1, 0);

        for (ForgeDirection d : HORIZONTAL_DIRECTIONS) {
            requireWall(d.offsetX, 1, d.offsetZ, d);
        }
    }

    private ClimbingKilnValidationParams determineKilnParams() throws KilnValidationException {
        ForgeDirection direction = ForgeDirection.UNKNOWN;
        int height = 0;

        int wallCount = 0;
        for (ForgeDirection d : HORIZONTAL_DIRECTIONS) {
            if (checkWall(d.offsetX, 2, d.offsetZ, d)) {
                wallCount++;
                direction = d.getOpposite();
            }
        }

        if (wallCount != 1) {
            throw new KilnValidationException("Expected exactly 1 wall surrounding +2");
        }

        for (int h = 1; h <= getMaxHeight(); h++) {
            int x = direction.offsetX * (2 + 2 * h);
            int y = 4 + h;
            int z = direction.offsetZ * (2 + 2 * h);

            if (checkChimneyTier(x, y, z, MIN_CHIMNEY_TIER)) {
                height = h;
                break;
            }
        }

        if (height == 0) {
            throw new KilnValidationException(String.format("Expected fire brick chimney %s at +4,+5,0 or +6,+6,0 or +8,+7,0", direction));
        }

        return new ClimbingKilnValidationParams(direction, height);
    }

    private void validateChamber(ClimbingKilnValidationParams params) throws KilnValidationException {
        ForgeDirection direction = params.direction;
        ForgeDirection directionOpp = params.direction.getOpposite();
        ForgeDirection directionLeft = params.direction.getRotation(ForgeDirection.DOWN);
        ForgeDirection directionRight = params.direction.getRotation(ForgeDirection.UP);

        // Column of air
        for (int y = 2; y < 4; y++) {
            requireAir(0, y, 0);
        }

        // Near wall
        for (int y = 2; y < 4; y++) {
            for (int xz = -1; xz < 2; xz++ ) {
                requireWall(directionOpp.offsetX + directionLeft.offsetX * xz, y, directionOpp.offsetZ + directionLeft.offsetZ * xz, directionOpp);
            }
        }

        // Far wall
        for (int y = 2 + params.height; y < 4 + params.height; y++) {
            for (int u = -1; u < 2; u++ ) {
                // Middle far wall is one block farther
                int v = params.height * 2 + (u == 0 ? 3 : 2);
                requireWall(direction.offsetX * v + directionLeft.offsetX * u, y, direction.offsetZ * v + directionLeft.offsetZ * u, direction);
            }
        }

        // Sections
        for (int sec = 0; sec <= params.height; sec++) {
            // Floor
            for (int u = -1; u < 2; u++) {
                int y = sec + 1;
                // Middle floor is one block farther
                final int vStart = sec * 2 + (u == 0 ? 1 : 0);

                for (int v = vStart; v < vStart + 2; v++) {
                    requireFireBrick(direction.offsetX * v + directionLeft.offsetX * u, y, direction.offsetZ * v + directionLeft.offsetZ * u);
                }
            }

            // Pottery
            for (int u = -1; u < 2; u++) {
                int y = sec + 2;
                // Middle floor is one block farther
                final int vStart = sec * 2 + (u == 0 ? 1 : 0);

                for (int v = vStart; v < vStart + 2; v++) {
                    requireAirOrPottery(direction.offsetX * v + directionLeft.offsetX * u, y, direction.offsetZ * v + directionLeft.offsetZ * u);
                }
            }

            // Air
            for (int u = -1; u < 2; u++) {
                int y = sec + 3;
                // Middle floor is one block farther
                final int vStart = sec * 2 + (u == 0 ? 1 : 0);

                for (int v = vStart; v < vStart + 2; v++) {
                    requireAir(direction.offsetX * v + directionLeft.offsetX * u, y, direction.offsetZ * v + directionLeft.offsetZ * u);
                }
            }

            // Roof
            for (int u = -1; u < 2; u++) {
                int y = sec + 4;
                final int vStart = sec * 2;

                for (int v = vStart; v < vStart + 2; v++) {
                    requireFireBrick(direction.offsetX * v + directionLeft.offsetX * u, y, direction.offsetZ * v + directionLeft.offsetZ * u);
                }
            }

            // Walls
            final int yStart = sec + 2;
            for (int y = yStart; y < yStart + 2; y++) {
                final int vStart = sec * 2;
                for (int v = vStart; v < vStart + 2; v++) {
                    requireWall(direction.offsetX * v + directionLeft.offsetX * 2, y, direction.offsetZ * v + directionLeft.offsetZ * 2, directionLeft);
                    requireWall(direction.offsetX * v + directionRight.offsetX * 2, y, direction.offsetZ * v + directionRight.offsetZ * 2, directionRight);
                }
            }
        }
    }

    protected int getMaxHeight() {
        return BidsOptions.Kiln.maxClimbingKilnHeight;
    }

}
