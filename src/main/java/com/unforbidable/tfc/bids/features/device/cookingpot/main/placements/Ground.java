package com.unforbidable.tfc.bids.features.device.cookingpot.main.placements;

import com.unforbidable.tfc.bids.features.device.cookingpot.main.CookingPotPlacementSpec;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingPotHeatProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Ground extends CookingPotPlacementSpec {

    @Override
    public CookingHeatLevel getHeatLevel(World world, int xCoord, int yCoord, int zCoord) {
        // Check if tile below can provide heat (stove or similar)
        TileEntity te = world.getTileEntity(xCoord, yCoord - 1, zCoord);
        if (te instanceof CookingPotHeatProvider) {
            CookingPotHeatProvider heatProvider = (CookingPotHeatProvider) te;
            return heatProvider.getHeatLevel();
        }

        // Check for lava blocks nearby to enable cooking near lava
        // ...

        return CookingHeatLevel.NONE;
    }

}
