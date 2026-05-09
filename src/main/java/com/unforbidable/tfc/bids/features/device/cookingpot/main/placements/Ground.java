package com.unforbidable.tfc.bids.features.device.cookingpot.main.placements;

import com.unforbidable.tfc.bids.features.device.cookingpot.main.CookingPotPlacement;
import com.unforbidable.tfc.bids.api._obsolete.Enums.EnumCookingHeatLevel;
import com.unforbidable.tfc.bids.api._obsolete.Interfaces.ICookingPotHeatProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Ground extends CookingPotPlacement {

    @Override
    public EnumCookingHeatLevel getHeatLevel(World world, int xCoord, int yCoord, int zCoord) {
        // Check if tile below can provide heat (stove or similar)
        TileEntity te = world.getTileEntity(xCoord, yCoord - 1, zCoord);
        if (te instanceof ICookingPotHeatProvider) {
            ICookingPotHeatProvider heatProvider = (ICookingPotHeatProvider) te;
            return heatProvider.getHeatLevel();
        }

        // Check for lava blocks nearby to enable cooking near lava
        // ...

        return EnumCookingHeatLevel.NONE;
    }

}
