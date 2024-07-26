package com.unforbidable.tfc.bids.Core.Kilns.BeehiveKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationParams;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidator;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BeehiveKilnValidator extends KilnValidator<KilnValidationParams> {

    static final ForgeDirection[] HORIZONTAL_DIRECTIONS = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };

    public BeehiveKilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        super(world, sourceX, sourceY, sourceZ);
    }

    @Override
    protected KilnValidationParams validateStructure() {
        if (!validateCenter()) {
            return null;
        }

        if (!validateChamber()) {
            return null;
        }

        if (!validateOuterWalls()) {
            return null;
        }

        return new KilnValidationParams();
    }

    private boolean validateCenter() {
        if (!requireAirOrFire(0, 1, 0)) {
            return false;
        }

        if (!checkChimneyTier(0, 4, 0, 1)) {
            return false;
        }

        for (ForgeDirection d : HORIZONTAL_DIRECTIONS) {
            if (!requireWall(d.offsetX, 1, d.offsetZ, d)) {
                return false;
            }
        }

        for (int i = 2; i < 4; i++) {
            if (!requireAir(0, i, 0)) {
                return false;
            }
        }

        return true;
    }

    private boolean validateChamber() {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    // Skip center
                    continue;
                }

                if (!requireWall(i, 1, j, ForgeDirection.UP)) {
                    return false;
                }

                if (!requireAirOrPottery(i, 2, j)) {
                    return false;
                }

                if (!requireAir(i, 3, j)) {
                    return false;
                }

                if (!requireWall(i, 4, j, ForgeDirection.DOWN)) {
                    return false;
                }
            }
        }

        return true;
    }


    private boolean validateOuterWalls() {
        for (ForgeDirection d : HORIZONTAL_DIRECTIONS) {
            ForgeDirection r = d.getRotation(ForgeDirection.UP);
            int x = d.offsetX * 2;
            int z = d.offsetZ * 2;

            for (int i = 2; i < 4; i++) {
                if (!requireWall(x + r.offsetX, i, z + r.offsetZ, d)) {
                    return false;
                }

                if (!requireWall(x, i, z, d)) {
                    return false;
                }

                if (!requireWall(x - r.offsetX, i, z - r.offsetZ, d)) {
                    return false;
                }
            }
        }

        return true;
    }

}
