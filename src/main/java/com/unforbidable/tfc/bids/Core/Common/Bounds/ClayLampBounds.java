package com.unforbidable.tfc.bids.Core.Common.Bounds;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class ClayLampBounds {

    private static final float rimWidth = 2 / 32f;
    private static final float rimHeight = 4 / 32f;
    private static final float rimLift = 2 / 32f;
    private static final float bottomHeight = 2 / 32f;
    private static final float bottomWidth = 12 / 32f;
    private static final float spoutWidth = 6 / 32f;
    private static final float spoutLength = 2 / 32f;
    private static final float spoutHeight = 2 / 32f;

    private static final float wickWidth = 2 / 32f;
    private static final float wickHeight = 1 / 32f;
    private static final float wickLength = 8 / 32f;

    AxisAlignedBB entire;
    AxisAlignedBB bottom;
    AxisAlignedBB[] sides = new AxisAlignedBB[4];
    AxisAlignedBB[] fuels = new AxisAlignedBB[3];
    AxisAlignedBB[] wicks = new AxisAlignedBB[2];
    AxisAlignedBB spout;

    Vec3 flame;

    public static ClayLampBounds getBoundsForOrientation(int orientation) {
        return new ClayLampBounds(orientation);
    }

    public ClayLampBounds(int orientation) {
        float bottomXZ = (1 - bottomWidth) / 2;
        bottom = AxisAlignedBB.getBoundingBox(bottomXZ, 0, bottomXZ, 1 - bottomXZ, bottomHeight, 1 - bottomXZ);

        float rimStartXZ = bottomXZ - rimWidth / 2;
        float rimEndXZ = bottomXZ + rimWidth / 2;
        sides[0] = AxisAlignedBB.getBoundingBox(rimStartXZ, rimLift, rimStartXZ, rimEndXZ, rimLift + rimHeight, 1 - rimStartXZ);
        sides[1] = AxisAlignedBB.getBoundingBox(1 - rimEndXZ, rimLift, rimStartXZ, 1 - rimStartXZ, rimLift + rimHeight, 1 - rimStartXZ);
        sides[2] = AxisAlignedBB.getBoundingBox(rimStartXZ, rimLift, rimStartXZ, 1 - rimStartXZ, rimLift + rimHeight, rimEndXZ);
        sides[3] = AxisAlignedBB.getBoundingBox(rimStartXZ, rimLift, 1 - rimEndXZ, 1 - rimStartXZ, rimLift + rimHeight, 1 - rimStartXZ);

        for (int i = 0; i < fuels.length; i++) {
            fuels[i] = AxisAlignedBB.getBoundingBox(bottomXZ, bottomHeight, bottomXZ, 1 - bottomXZ, bottomHeight + ((i + 1) / 32f), 1 - bottomXZ);
        }

        if (orientation == 0 || orientation == 2) {
            float spoutX = (1 - spoutWidth) / 2;
            float spoutZ = rimStartXZ - spoutLength;
            float spoutY = rimLift + rimHeight - spoutHeight;

            float wickX = (1 - wickWidth) / 2;
            float wickZ = rimEndXZ - wickWidth + 1 / 128f;
            float wickY = rimLift;

            if (orientation == 0) {
                spout = AxisAlignedBB.getBoundingBox(spoutX, spoutY, spoutZ, spoutX + spoutWidth, spoutY + spoutHeight, spoutZ + spoutLength);
                wicks[0] = AxisAlignedBB.getBoundingBox(wickX, wickY, wickZ, wickX + wickWidth, wickY + rimHeight + wickHeight, wickZ + wickWidth);
                wicks[1] = AxisAlignedBB.getBoundingBox(wickX, wickY, wickZ + wickWidth, wickX + wickWidth, wickY + wickWidth / 2 + 1 / 128f, wickZ + wickWidth + wickLength);

                entire = AxisAlignedBB.getBoundingBox(rimStartXZ, 0, rimStartXZ - spoutLength, 1 - rimStartXZ, rimLift + rimHeight, 1 - rimStartXZ);

                flame = Vec3.createVectorHelper(wickX + wickWidth / 2, wickY + rimLift + rimHeight + wickHeight, wickZ + wickWidth / 2);
            } else {
                spout = AxisAlignedBB.getBoundingBox(spoutX, spoutY, 1 - spoutZ - spoutLength, spoutX + spoutWidth, spoutY + spoutHeight, 1 - spoutZ);
                wicks[0] = AxisAlignedBB.getBoundingBox(wickX, wickY, 1 - wickZ - wickWidth, wickX + wickWidth, wickY + rimHeight + wickHeight, 1 - wickZ);
                wicks[1] = AxisAlignedBB.getBoundingBox(wickX, wickY, 1 - wickZ - wickWidth - wickLength, wickX + wickWidth, wickY + wickWidth / 2 + 1 / 128f, 1 - wickZ);

                entire = AxisAlignedBB.getBoundingBox(rimStartXZ, 0, rimStartXZ, 1 - rimStartXZ, rimLift + rimHeight, 1 - rimStartXZ + spoutLength);

                flame = Vec3.createVectorHelper(wickX + wickWidth / 2, wickY + rimLift + rimHeight + wickHeight, 1 - wickZ - wickWidth / 2);
            }
        } else {
            float spoutX = rimStartXZ - spoutLength;
            float spoutZ = (1 - spoutWidth) / 2;
            float spoutY = rimLift + rimHeight - spoutHeight;

            float wickX = rimEndXZ - wickWidth + 1 / 128f;
            float wickZ = (1 - wickWidth) / 2;
            float wickY = rimLift;

            if (orientation == 3) {
                spout = AxisAlignedBB.getBoundingBox(spoutX, spoutY, spoutZ, spoutX + spoutLength, spoutY + spoutHeight, spoutZ + spoutWidth);
                wicks[0] = AxisAlignedBB.getBoundingBox(wickX, wickY, wickZ, wickX + wickWidth, wickY + rimHeight + wickHeight, wickZ + wickWidth);
                wicks[1] = AxisAlignedBB.getBoundingBox(wickX, wickY, wickZ, wickX + wickWidth + wickLength, wickY + wickWidth / 2 + 1 / 128f, wickZ + wickWidth);

                entire = AxisAlignedBB.getBoundingBox(rimStartXZ - spoutLength, 0, rimStartXZ, 1 - rimStartXZ, rimLift + rimHeight, 1 - rimStartXZ);

                flame = Vec3.createVectorHelper(wickX + wickWidth / 2, wickY + rimLift + rimHeight + wickHeight, wickZ + wickWidth / 2);
            } else {
                spout = AxisAlignedBB.getBoundingBox(1 - spoutX - spoutLength, spoutY, 1 - spoutZ - spoutWidth, 1 - spoutX, spoutY + spoutHeight, 1 - spoutZ);
                wicks[0] = AxisAlignedBB.getBoundingBox(1 - wickX - wickWidth, wickY, 1 - wickZ - wickWidth, 1 - wickX, wickY + rimHeight + wickHeight, 1 - wickZ);
                wicks[1] = AxisAlignedBB.getBoundingBox(1 - wickX - wickWidth - wickLength, wickY, 1 - wickZ - wickWidth, 1 - wickX, wickY + wickWidth / 2 + 1 / 128f, 1 - wickZ);

                entire = AxisAlignedBB.getBoundingBox(rimStartXZ, 0, rimStartXZ, 1 - rimStartXZ + spoutLength, rimLift + rimHeight, 1 - rimStartXZ);

                flame = Vec3.createVectorHelper(1 - wickX - wickWidth / 2, wickY + rimLift + rimHeight + wickHeight, wickZ + wickWidth / 2);
            }
        }
    }

    public AxisAlignedBB getEntireBounds() {
        return entire;
    }

    public AxisAlignedBB getBottomBounds() {
        return bottom;
    }

    public AxisAlignedBB[] getSidesBounds() {
        return sides;
    }

    public AxisAlignedBB[] getWicksBounds() {
        return wicks;
    }

    public AxisAlignedBB[] getFuelsBounds() {
        return fuels;
    }

    public AxisAlignedBB getSpoutBounds() {
        return spout;
    }

    public Vec3 getFlamePos() {
        return flame;
    }

}
