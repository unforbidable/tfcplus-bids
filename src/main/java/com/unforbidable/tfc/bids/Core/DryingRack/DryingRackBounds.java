package com.unforbidable.tfc.bids.Core.DryingRack;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class DryingRackBounds {

    static final float unit = 1 / 32f;
    static final float poleStart = unit * 7;
    static final float poleWidth = unit * 2;
    static final float knotStartXZ = unit * 6;
    static final float knotStartY = unit * 4;
    static final float knotWidth = unit * 4;
    static final float knotHeight = unit * 6;
    static final float half = unit * 16;
    static final float itemOffsetSub = unit * 8;
    static final float itemOffsetPole = unit * 6.4f;
    static final float itemStridePole = unit * 19;
    static final float itemOffsetY = unit * 12;

    public final AxisAlignedBB[] poles;
    public final AxisAlignedBB[] sections;
    public final AxisAlignedBB[] knots;

    private DryingRackBounds(AxisAlignedBB[] poles, AxisAlignedBB[] sections, AxisAlignedBB[] knots) {
        this.poles = poles;
        this.sections = sections;
        this.knots = knots;
    }

    public static AxisAlignedBB getEntireDryingRackBounds() {
        double minY = half + poleStart;
        double maxY = minY + poleWidth;

        return AxisAlignedBB.getBoundingBox(0, minY, 0, 1, maxY, 1);
    }

    public static Vec3 getRenderItemOffset(int orientation, int section) {
        final boolean isNorthSouthOrientation = orientation % 2 == 0;

        int sub = section % 2;
        int pole = section < 2 ? 0 : 1;

        double y = itemOffsetY;

        if (isNorthSouthOrientation) {
            double x = itemOffsetPole + itemStridePole * pole;
            double z = itemOffsetSub + half * sub;

            return Vec3.createVectorHelper(x, y, z);
        } else {
            double x = itemOffsetSub + half * sub;
            double z = itemOffsetPole + itemStridePole * pole;

            return Vec3.createVectorHelper(x, y, z);
        }
    }

    public static DryingRackBounds fromOrientation(int orientation) {
        final AxisAlignedBB[] poles = new AxisAlignedBB[2];
        final AxisAlignedBB[] sections = new AxisAlignedBB[4];
        final AxisAlignedBB[] knots = new AxisAlignedBB[4];

        final boolean isNorthSouthOrientation = orientation % 2 == 0;

        for (int i = 0; i < 2; i++) {
            double minY = half + poleStart;
            double maxY = minY + poleWidth;

            if (isNorthSouthOrientation) {
                double minX = poleStart + half * i;
                double minZ = 0;
                double maxX = minX + poleWidth;
                double maxZ = 1;

                poles[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            } else {
                double minX = 0;
                double minZ = poleStart + half * i;
                double maxX = 1;
                double maxZ = minZ + poleWidth;

                poles[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }

        for (int i = 0; i < 4; i++) {
            int section = i % 2;
            int pole = i < 2 ? 0 : 1;

            double minY = half + poleStart;
            double maxY = minY + poleWidth;

            if (isNorthSouthOrientation) {
                double minX = half * pole;
                double minZ = half * section;
                double maxX = minX + half;
                double maxZ = minZ + half;

                sections[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            } else {
                double minX = half * section;
                double minZ = half * pole;
                double maxX = minX + half;
                double maxZ = minZ + half;

                sections[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }

        for (int i = 0; i < 4; i++) {
            int section = i % 2;
            int pole = i < 2 ? 0 : 1;

            double minY = half + knotStartY;
            double maxY = minY + knotHeight;

            if (isNorthSouthOrientation) {
                double minX = knotStartXZ + half * pole;
                double minZ = poleStart + half * section;
                double maxX = minX + knotWidth;
                double maxZ = minZ + poleWidth;

                knots[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            } else {
                double minX = poleStart + half * section;
                double minZ = knotStartXZ + half * pole;
                double maxX = minX + poleWidth;
                double maxZ = minZ + knotWidth;

                knots[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }

        return new DryingRackBounds(poles, sections, knots);
    }

}
