package com.unforbidable.tfc.bids.Core.Kilns.BeehiveKiln;

import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationException;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidationParams;
import com.unforbidable.tfc.bids.Core.Kilns.KilnValidator;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BeehiveKilnValidator extends KilnValidator<KilnValidationParams> {

    public BeehiveKilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        super(world, sourceX, sourceY, sourceZ);
    }

    @Override
    public BlockCoord getChimneyLocation(KilnValidationParams params) {
        return new BlockCoord(sourceX, sourceY + 4, sourceZ);
    }

    @Override
    public List<BlockCoord> getPotteryLocations(KilnValidationParams params) {
        List<BlockCoord> list = new ArrayList<BlockCoord>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    // Skip center
                    continue;
                }

                list.add(new BlockCoord(sourceX + i, sourceY + 2, sourceZ + j));
            }
        }

        return list;
    }

    @Override
    public KilnValidationParams validate() throws KilnValidationException {
        validateCenter();
        validateChamber();
        validateOuterWalls();

        return new KilnValidationParams();
    }

    private void validateCenter() throws KilnValidationException {
        requireAirOrFire(0, 1, 0);
        requireChimneyTier(0, 4, 0, 1);

        for (ForgeDirection d : HORIZONTAL_DIRECTIONS) {
            requireWall(d.offsetX, 1, d.offsetZ, d);
        }

        for (int y = 2; y < 4; y++) {
            requireAir(0, y, 0);
        }
    }

    private void validateChamber() throws KilnValidationException {
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                if (x == 0 && z == 0) {
                    // Skip center
                    continue;
                }

                requireWall(x, 1, z, ForgeDirection.UP);
                requireAirOrPottery(x, 2, z);
                requireAir(x, 3, z);
                requireWall(x, 4, z, ForgeDirection.DOWN);
            }
        }
    }


    private void validateOuterWalls() throws KilnValidationException {
        for (ForgeDirection d : HORIZONTAL_DIRECTIONS) {
            ForgeDirection r = d.getRotation(ForgeDirection.UP);
            int x = d.offsetX * 2;
            int z = d.offsetZ * 2;

            for (int y = 2; y < 4; y++) {
                requireWall(x + r.offsetX, y, z + r.offsetZ, d);
                requireWall(x, y, z, d);
                requireWall(x - r.offsetX, y, z - r.offsetZ, d);
            }
        }
    }

}
