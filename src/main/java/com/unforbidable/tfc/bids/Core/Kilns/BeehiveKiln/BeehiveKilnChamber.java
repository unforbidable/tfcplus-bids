package com.unforbidable.tfc.bids.Core.Kilns.BeehiveKiln;

import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
import com.unforbidable.tfc.bids.Core.Kilns.KilnChamber;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BeehiveKilnChamber extends KilnChamber<BeehiveKilnValidator> {

    public BeehiveKilnChamber(IKilnHeatSource heatSource) {
        super(heatSource);
    }

    @Override
    protected BeehiveKilnValidator createValidator(World world, int x, int y, int z) {
        return new BeehiveKilnValidator(world, x, y, z);
    }

    @Override
    public String getName() {
        return "BEEHIVE_KILN";
    }

    @Override
    protected BlockCoord getChimneyLocation() {
        return new BlockCoord(heatSource.getTileX(), heatSource.getTileY() + 4, heatSource.getTileZ());
    }

    @Override
    protected List<BlockCoord> getPotteryLocations() {
        List<BlockCoord> list = new ArrayList<BlockCoord>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    // Skip center
                    continue;
                }

                list.add(new BlockCoord(heatSource.getTileX() + i, heatSource.getTileY() + 2, heatSource.getTileZ() + j));
            }
        }

        return list;
    }

}
