package com.unforbidable.tfc.bids.features.device.cookingpot.main;

import com.unforbidable.tfc.bids.api._obsolete.Enums.EnumCookingHeatLevel;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class CookingPotPlacement {

    public EnumCookingHeatLevel getHeatLevel(World world, int xCoord, int yCoord, int zCoord) {
        return EnumCookingHeatLevel.NONE;
    }

    public Vec3 getOffset() {
        return Vec3.createVectorHelper(0, 0, 0);
    }

    public ForgeDirection getDirection() {
        return ForgeDirection.UNKNOWN;
    }

}
