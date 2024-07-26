package com.unforbidable.tfc.bids.Core.Kilns.BeehiveKiln;

import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationParams;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidator;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BeehiveKilnValidator extends KilnValidator<KilnValidationParams> {

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

        for (int y = 2; y < 4; y++) {
            if (!requireAir(0, y, 0)) {
                return false;
            }
        }

        return true;
    }

    private boolean validateChamber() {
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                if (x == 0 && z == 0) {
                    // Skip center
                    continue;
                }

                if (!requireWall(x, 1, z, ForgeDirection.UP)) {
                    return false;
                }

                if (!requireAirOrPottery(x, 2, z)) {
                    return false;
                }

                if (!requireAir(x, 3, z)) {
                    return false;
                }

                if (!requireWall(x, 4, z, ForgeDirection.DOWN)) {
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

            for (int y = 2; y < 4; y++) {
                if (!requireWall(x + r.offsetX, y, z + r.offsetZ, d)) {
                    return false;
                }

                if (!requireWall(x, y, z, d)) {
                    return false;
                }

                if (!requireWall(x - r.offsetX, y, z - r.offsetZ, d)) {
                    return false;
                }
            }
        }

        return true;
    }

}
