package com.unforbidable.tfc.bids.Core.DryingRack;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class DryingRackBounds {

    static final float unit = 1 / 32f;
    static final float poleStartXZ = unit * 7;
    static final float poleStartY = unit * 9;
    static final float poleWidth = unit * 2;
    static final float knotStartXZ = unit * 6.5f;
    static final float knotStartY = poleStartY - unit * 0.5f;
    static final float knotWidth = unit * 3;
    static final float knotHeight = unit * 3;
    static final float stringStartXZ = unit * 7.75f;
    static final float stringStartY = poleStartY - unit * 6;
    static final float stringWidth = unit * 0.5f;
    static final float stringHeight = unit * 6;
    static final float half = unit * 16;
    static final float itemOffsetSub = unit * 8;
    static final float itemOffsetPole = unit * 6.4f;
    static final float itemStridePole = unit * 19;
    static final float itemOffsetY = poleStartY + unit * 5;
    static final float itemTiedOffsetPole = unit * 7.8f;
    static final float itemTiedStridePole = unit * 16;
    static final float itemTiedOffsetY = poleStartY;

    public final AxisAlignedBB[] poles;
    public final AxisAlignedBB[] sections;
    public final AxisAlignedBB[] knots;
    public final AxisAlignedBB[] strings;

    private DryingRackBounds(AxisAlignedBB[] poles, AxisAlignedBB[] sections, AxisAlignedBB[] knots,
            AxisAlignedBB[] strings) {
        this.poles = poles;
        this.sections = sections;
        this.knots = knots;
        this.strings = strings;
    }

    public static AxisAlignedBB getEntireDryingRackBounds() {
        double minY = half + poleStartY;
        double maxY = minY + poleWidth;

        return AxisAlignedBB.getBoundingBox(0, minY, 0, 1, maxY, 1);
    }

    public static Vec3 getRenderItemOffset(int orientation, int section, boolean tied) {
        final boolean isNorthSouthOrientation = orientation % 2 == 0;

        int sub = section % 2;
        int pole = section < 2 ? 0 : 1;

        if (tied) {
            double y = itemTiedOffsetY;

            if (isNorthSouthOrientation) {
                double x = itemTiedOffsetPole + itemTiedStridePole * pole;
                double z = itemOffsetSub + half * sub;

                return Vec3.createVectorHelper(x, y, z);
            } else {
                double x = itemOffsetSub + half * sub;
                double z = itemTiedOffsetPole + itemTiedStridePole * pole;

                return Vec3.createVectorHelper(x, y, z);
            }

        } else {
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

    }

    public static DryingRackBounds fromOrientation(int orientation) {
        final AxisAlignedBB[] poles = new AxisAlignedBB[2];
        final AxisAlignedBB[] sections = new AxisAlignedBB[4];
        final AxisAlignedBB[] knots = new AxisAlignedBB[4];
        final AxisAlignedBB[] strings = new AxisAlignedBB[4];

        final boolean isNorthSouthOrientation = orientation % 2 == 0;

        for (int i = 0; i < 2; i++) {
            double minY = half + poleStartY;
            double maxY = minY + poleWidth;

            if (isNorthSouthOrientation) {
                double minX = poleStartXZ + half * i;
                double minZ = 0;
                double maxX = minX + poleWidth;
                double maxZ = 1;

                poles[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            } else {
                double minX = 0;
                double minZ = poleStartXZ + half * i;
                double maxX = 1;
                double maxZ = minZ + poleWidth;

                poles[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }

        for (int i = 0; i < 4; i++) {
            int section = i % 2;
            int pole = i < 2 ? 0 : 1;

            double minY = half + poleStartY;
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
                double minZ = poleStartXZ + half * section;
                double maxX = minX + knotWidth;
                double maxZ = minZ + poleWidth;

                knots[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            } else {
                double minX = poleStartXZ + half * section;
                double minZ = knotStartXZ + half * pole;
                double maxX = minX + poleWidth;
                double maxZ = minZ + knotWidth;

                knots[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }

        for (int i = 0; i < 4; i++) {
            int section = i % 2;
            int pole = i < 2 ? 0 : 1;

            double minY = half + stringStartY;
            double maxY = minY + stringHeight;

            if (isNorthSouthOrientation) {
                double minX = stringStartXZ + half * pole;
                double minZ = stringStartXZ + half * section;
                double maxX = minX + stringWidth;
                double maxZ = minZ + stringWidth;

                strings[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            } else {
                double minX = stringStartXZ + half * section;
                double minZ = stringStartXZ + half * pole;
                double maxX = minX + stringWidth;
                double maxZ = minZ + stringWidth;

                strings[i] = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }

        return new DryingRackBounds(poles, sections, knots, strings);
    }

}
