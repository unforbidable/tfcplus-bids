package com.unforbidable.tfc.bids.Core.Cooking.CookingPot.Placements;

import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.CookingPotPlacement;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;
import com.unforbidable.tfc.bids.api.Interfaces.ICookingPotHeatProvider;
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
