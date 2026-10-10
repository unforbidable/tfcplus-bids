package com.unforbidable.tfc.bids.features.device.cookingpit.main;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class HideCookingPitBounds extends CookingPitBounds {

    static final float rimWidth = 6 / 32f;
    static final float bottomHeight = 20 / 32f;
    static final float rimLift = 20 / 32f;
    static final float rimHeight = 12 / 32f;
    static final float maxFluidHeight = 8 / 32f;

    private final Vec3 itemPos;
    private final AxisAlignedBB bottomBounds;
    private final AxisAlignedBB[] sideBounds;
    private final AxisAlignedBB fluidBounds;

    protected HideCookingPitBounds(Vec3 itemPos, AxisAlignedBB bottomBounds, AxisAlignedBB[] sideBounds, AxisAlignedBB fluidBounds) {
        this.itemPos = itemPos;
        this.bottomBounds = bottomBounds;
        this.sideBounds = sideBounds;
        this.fluidBounds = fluidBounds;
    }

    public static HideCookingPitBounds getBounds() {
        Vec3 itemPos = Vec3.createVectorHelper(0.5, bottomHeight, 0.5);

        AxisAlignedBB bottomBounds = AxisAlignedBB.getBoundingBox(0, 0,0, 1, bottomHeight, 1);

        AxisAlignedBB[] sidesBounds = new AxisAlignedBB[4];
        float rimStartXZ = 0;
        float rimEndXZ = rimWidth;
        sidesBounds[0] = AxisAlignedBB.getBoundingBox(rimStartXZ, rimLift, rimEndXZ, rimEndXZ, rimLift + rimHeight, 1 - rimEndXZ);
        sidesBounds[1] = AxisAlignedBB.getBoundingBox(1 - rimEndXZ, rimLift, rimEndXZ, 1 - rimStartXZ, rimLift + rimHeight, 1 - rimEndXZ);
        sidesBounds[2] = AxisAlignedBB.getBoundingBox(rimStartXZ, rimLift, rimStartXZ, 1 - rimStartXZ, rimLift + rimHeight, rimEndXZ);
        sidesBounds[3] = AxisAlignedBB.getBoundingBox(rimStartXZ, rimLift, 1 - rimEndXZ, 1 - rimStartXZ, rimLift + rimHeight, 1 - rimStartXZ);

        AxisAlignedBB fluidBounds = AxisAlignedBB.getBoundingBox(rimWidth, bottomHeight, rimWidth, 1 - rimWidth, bottomHeight + (1 / 32f), 1 - rimWidth);

        return new HideCookingPitBounds(itemPos, bottomBounds, sidesBounds, fluidBounds);
    }

    @Override
    public Vec3 getItemPos() {
        return itemPos;
    }

    @Override
    public float getMaxFluidHeight() {
        return maxFluidHeight;
    }

    @Override
    public AxisAlignedBB getBottomBounds() {
        return bottomBounds;
    }

    @Override
    public AxisAlignedBB[] getSidesBounds() {
        return sideBounds;
    }

    @Override
    public AxisAlignedBB getFluidBounds() {
        return fluidBounds;
    }

}
