package com.unforbidable.tfc.bids.Core.Kilns.BeehiveKiln;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationHelper;
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
        if (!KilnValidationHelper.isAirOrFire(world, sourceX, sourceY + 1, sourceZ)) {
            Bids.LOG.debug("Expected air or fire at +1");
            return false;
        }

        if (!KilnValidationHelper.isChimneyTier(world, sourceX, sourceY + 4, sourceZ, 1)) {
            Bids.LOG.debug("Expected chimney at +4");
            return false;
        }

        for (ForgeDirection d : HORIZONTAL_DIRECTIONS) {
            if (!KilnValidationHelper.isWall(world, sourceX + d.offsetX, sourceY + 1, sourceZ + d.offsetZ, d)) {
                Bids.LOG.debug("Expected wall {} at +1", d);
                return false;
            }
        }

        for (int i = 2; i < 4; i++) {
            if (!KilnValidationHelper.isAir(world, sourceX, sourceY + i, sourceZ)) {
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

                if (!KilnValidationHelper.isWall(world, sourceX + i, sourceY + 1, sourceZ + j, ForgeDirection.UP)) {
                    Bids.LOG.debug("Expected floor at +1 {}, {}", i, j);
                    return false;
                }

                if (!KilnValidationHelper.isAirOrPottery(world, sourceX + i, sourceY + 2, sourceZ + j)) {
                    Bids.LOG.debug("Expected air or pottery at +2 {}, {}", i, j);
                    return false;
                }

                if (!KilnValidationHelper.isAir(world, sourceX + i, sourceY + 3, sourceZ + j)) {
                    Bids.LOG.debug("Expected air at +3 {}, {}", i, j);
                    return false;
                }

                if (!KilnValidationHelper.isWall(world, sourceX + i, sourceY + 4, sourceZ + j, ForgeDirection.DOWN)) {
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
            int x = sourceX + d.offsetX * 2;
            int z = sourceZ + d.offsetZ * 2;

            for (int i = 2; i < 4; i++) {
                if (!KilnValidationHelper.isWall(world, x + r.offsetX, sourceY + i, z + r.offsetZ, d)) {
                    Bids.LOG.debug("Expected outer wall at +{} {} +1", i, d);
                    return false;
                }

                if (!KilnValidationHelper.isWall(world, x, sourceY + i, z, d)) {
                    Bids.LOG.debug("Expected outer wall at +{} {} 0", i, d);
                    return false;
                }

                if (!KilnValidationHelper.isWall(world, x - r.offsetX, sourceY + i, z - r.offsetZ, d)) {
                    Bids.LOG.debug("Expected outer wall at +{} {} -1", i, d);
                    return false;
                }
            }
        }

        return true;
    }

}
