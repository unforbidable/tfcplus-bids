package com.unforbidable.tfc.bids.Core.Kilns.BeehiveKiln;

import com.unforbidable.tfc.bids.Bids;
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
        if (!checkAirOrFire(0, 1, 0)) {
            Bids.LOG.debug("Expected air or fire at +1");
            return false;
        }

        if (!checkChimneyTier(0, 4, 0, 1)) {
            Bids.LOG.debug("Expected chimney at +4");
            return false;
        }

        for (ForgeDirection d : HORIZONTAL_DIRECTIONS) {
            if (!checkWall(d.offsetX, 1, d.offsetZ, d)) {
                Bids.LOG.debug("Expected wall {} at +1", d);
                return false;
            }
        }

        for (int i = 2; i < 4; i++) {
            if (!checkAir(0, i, 0)) {
                Bids.LOG.debug("Expected air above at +{}", i);
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

                if (!checkWall(i, 1, j, ForgeDirection.UP)) {
                    Bids.LOG.debug("Expected floor at +1 {}, {}", i, j);
                    return false;
                }

                if (!checkAirOrPottery(i, 2, j)) {
                    Bids.LOG.debug("Expected air or pottery at +2 {}, {}", i, j);
                    return false;
                }

                if (!checkAir(i, 3, j)) {
                    Bids.LOG.debug("Expected air at +3 {}, {}", i, j);
                    return false;
                }

                if (!checkWall(i, 4, j, ForgeDirection.DOWN)) {
                    Bids.LOG.debug("Expected roof at +4 {}, {}", i, j);
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
                if (!checkWall(x + r.offsetX, i, z + r.offsetZ, d)) {
                    Bids.LOG.debug("Expected outer wall at +{} {} +1", i, d);
                    return false;
                }

                if (!checkWall(x, i, z, d)) {
                    Bids.LOG.debug("Expected outer wall at +{} {} 0", i, d);
                    return false;
                }

                if (!checkWall(x - r.offsetX, i, z - r.offsetZ, d)) {
                    Bids.LOG.debug("Expected outer wall at +{} {} -1", i, d);
                    return false;
                }
            }
        }

        return true;
    }

}
