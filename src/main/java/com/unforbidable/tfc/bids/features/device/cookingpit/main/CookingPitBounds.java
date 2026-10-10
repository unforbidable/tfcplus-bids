package com.unforbidable.tfc.bids.features.device.cookingpit.main;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public abstract class CookingPitBounds {

    public abstract Vec3 getItemPos();

    public abstract float getMaxFluidHeight();

    public abstract AxisAlignedBB getBottomBounds();

    public abstract AxisAlignedBB[] getSidesBounds();

    public abstract AxisAlignedBB getFluidBounds();

}
