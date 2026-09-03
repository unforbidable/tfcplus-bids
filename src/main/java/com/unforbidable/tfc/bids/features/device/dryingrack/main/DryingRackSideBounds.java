package com.unforbidable.tfc.bids.features.device.dryingrack.main;

import net.minecraft.util.AxisAlignedBB;

public class DryingRackSideBounds {

    static final float unit = DryingRackBounds.unit;
    static final float half = unit * 16;
    static final float poleWidth = DryingRackBounds.poleWidth;

    static final float armOneStartXZ = half - poleWidth / 2 - poleWidth;
    static final float armTwoStartXZ = half + poleWidth / 2;

    static final float poleStartXZ = DryingRackBounds.poleStartXZ;
    static final float poleStartY = DryingRackBounds.poleStartY;
    static final float poleOneStart = armOneStartXZ - poleWidth;
    static final float poleTwoEnd = armTwoStartXZ + poleWidth + poleWidth;

    public final AxisAlignedBB[] legs;
    public final AxisAlignedBB armOne;
    public final AxisAlignedBB armTwo;
    public final AxisAlignedBB[] polesOne;
    public final AxisAlignedBB[] polesTwo;
    public final AxisAlignedBB[] polesBoth;

    public static AxisAlignedBB getEntireDryingRackSideBoundsForOrientation(int orientation) {
        double minX = 0;
        double maxX = 1;
        double minY = 0;
        double maxY = 1;
        double minZ = half - poleWidth / 2;
        double maxZ = minZ + poleWidth;

        boolean isNorthSouthOrientation = orientation % 2 == 0;
        if (isNorthSouthOrientation) {
            return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        } else {
            return AxisAlignedBB.getBoundingBox(minZ, minY, minX, maxZ, maxY, maxX);
        }
    }

    public DryingRackSideBounds(AxisAlignedBB[] legs, AxisAlignedBB armOne, AxisAlignedBB armTwo, AxisAlignedBB[] polesOne, AxisAlignedBB[] polesTwo, AxisAlignedBB[] polesBoth) {
        this.legs = legs;
        this.armOne = armOne;
        this.armTwo = armTwo;
        this.polesOne = polesOne;
        this.polesTwo = polesTwo;
        this.polesBoth = polesBoth;
    }

    public static DryingRackSideBounds fromOrientation(int orientation) {
        AxisAlignedBB[] legs = new AxisAlignedBB[2];
        AxisAlignedBB[] arms = new AxisAlignedBB[2];
        AxisAlignedBB[] polesOne = new AxisAlignedBB[2];
        AxisAlignedBB[] polesTwo = new AxisAlignedBB[2];
        AxisAlignedBB[] polesBoth = new AxisAlignedBB[2];

        boolean isNorthSouthOrientation = orientation % 2 == 0;

        for (int i = 0; i < 2; i++) {
            double minY = 0;
            double maxY = 1;

            if (isNorthSouthOrientation) {
                double minX = poleStartXZ + half * i + (i == 0 ? -poleWidth : poleWidth);
                double minZ = half - poleWidth / 2;
                double maxX = minX + poleWidth;
                double maxZ = minZ + poleWidth;

                legs[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            } else {
                double minX = half - poleWidth / 2;
                double minZ = poleStartXZ + half * i + (i == 0 ? -poleWidth : poleWidth);
                double maxX = minX + poleWidth;
                double maxZ = minZ + poleWidth;

                legs[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }

        for (int i = 0; i < 2; i++) {
            double minY = half + poleStartY;
            double maxY = minY + poleWidth;

            if (isNorthSouthOrientation) {
                double minX = poleStartXZ + half * i;
                double minZ = 0;
                double maxX = minX + poleWidth;
                double maxZ = 1;

                polesOne[i] = AxisAlignedBB.getBoundingBox(minX, minY, poleOneStart, maxX, maxY, maxZ);
                polesTwo[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, poleTwoEnd);
                polesBoth[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            } else {
                double minX = 0;
                double minZ = poleStartXZ + half * i;
                double maxX = 1;
                double maxZ = minZ + poleWidth;

                polesOne[i] = AxisAlignedBB.getBoundingBox(poleOneStart, minY, minZ, maxX, maxY, maxZ);
                polesTwo[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, poleTwoEnd, maxY, maxZ);
                polesBoth[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }

        for (int i = 0; i < 2; i++) {
            double minY = half + poleStartY - poleWidth;
            double maxY = minY + poleWidth;

            if (isNorthSouthOrientation) {
                double minX = 0 + unit;
                double minZ = half - poleWidth / 2 + (i == 0 ? poleWidth : -poleWidth);
                double maxX = 1 - unit;
                double maxZ = minZ + poleWidth;

                arms[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            } else {
                double minX = half - poleWidth / 2 + (i == 0 ? poleWidth : -poleWidth);
                double minZ = 0 + unit;
                double maxX = minX + poleWidth;
                double maxZ = 1 - unit;

                arms[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }

        return new DryingRackSideBounds(legs, arms[0], arms[1], polesOne, polesTwo, polesBoth);
    }

}
