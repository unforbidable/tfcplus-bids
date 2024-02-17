package com.unforbidable.tfc.bids.Core.ScrewPress;

import net.minecraft.util.AxisAlignedBB;

public class ScrewPressBounds {

    private static final float rackA = 1f / 4; // leg depth
    private static final float rackB = 1f / 4; // leg width
    private static final float rackC = 1f / 4; // claw height
    private static final float rackD = 1f / 4; // feet&arm height

    private static final float basinBottomH = 1f / 32;
    private static final float basinRimH = 1f / 2;
    private static final float basinRimW = 1f / 32;
    private static final float basinLiquidH = 1f / 32 * 14;
    private static final float basketBottomH = 1f / 32;
    private static final float basketBottomXZ = 1f / 32 * 5;
    private static final float basketBottomY = 1f / 32 * 12;
    private static final float basketRimW = 1f / 16;
    private static final float basketFootXZ = 1f / 4;
    private static final float basketHoopW = 1f / 256;
    private static final float basketHoopY = 0;
    private static final float discBottomXZ = 1f / 32 * 7;
    private static final float discBottomH = 1f / 32 * 6;
    private static final float discTopH = 1f / 8;
    private static final float discJointY = 1f / 32 * 16;
    private static final float discJointH = 1f / 8;
    private static final float discEarW = 1f / 4;
    private static final float discEarD = 1f / 8;
    private static final float discEarH = 1f / 8 * 8;
    private static final float leverD = 1f / 32 * 10;
    private static final float leverH = 1f / 4;
    private static final float leverA = -(1f / 32 * 30 + 1);
    private static final float leverB = 1f / 32 * 30 + 2;
    private static final float leverPreForkW = 1f / 2;
    private static final float leverPreForkD = 1f / 32 * 16;
    private static final float leverForkW = 1f / 4 * 5;
    private static final float leverForkD = 1f / 32 * 6;
    private static final float leverForkS = 1f / 3;
    private static final float leverForkR = 1f / 3;
    private static final float nutW = 1f / 32 * 20;
    private static final float nutH = 1f / 6;
    private static final float nutD = 1f / 32 * 20;
    private static final float boltW = 1f / 8;
    private static final float boltD = 1f / 32 * 34;

    private final AxisAlignedBB[] rackBottom;
    private final AxisAlignedBB[] rackMiddle;
    private final AxisAlignedBB[] rackTop;
    private final AxisAlignedBB[] rackBridge;
    private final AxisAlignedBB[] rackBottomInv;
    private final AxisAlignedBB[] rackTopInv;
    private final AxisAlignedBB[] basinBottom;
    private final AxisAlignedBB basinFluid;
    private final AxisAlignedBB[] basinSides;
    private final AxisAlignedBB[] basketBottom;
    private final AxisAlignedBB[] basketSides;
    private final AxisAlignedBB[] basketHoops;
    private final AxisAlignedBB[] disc;
    private final AxisAlignedBB[] discRotated;
    private final AxisAlignedBB[] lever;
    private final AxisAlignedBB[] leverInv;
    private final AxisAlignedBB[] nut;
    private final AxisAlignedBB[] bolt;

    private final AxisAlignedBB leverAll;
    private final AxisAlignedBB rackMiddleAll;
    private final AxisAlignedBB rackBridgeAll;

    private final static ScrewPressBounds[] cached = new ScrewPressBounds[2];

    public static ScrewPressBounds getBoundsForOrientation(int orientation) {
        if (cached[orientation] == null) {
            cached[orientation] = new ScrewPressBounds(orientation);
        }

        return cached[orientation];
    }

    public static ScrewPressBounds getBounds() {
        return getBoundsForOrientation(0);
    }

    private ScrewPressBounds(int orientation) {
        rackBottom = new AxisAlignedBB[5];
        {
            float axz = (1 - rackA) * 0.5f;
            float aXZ = (1 + rackA) * 0.5f;
            float bxz = (1 - rackB) * 0.5f;
            float bXZ = (1 + rackB) * 0.5f;

            // feet
            rackBottom[0] = AxisAlignedBB.getBoundingBox(0, 0, 0, 1, rackD, rackA);
            rackBottom[1] = AxisAlignedBB.getBoundingBox(0, 0, 1 - rackA, 1, rackD, 1);
            // legs
            rackBottom[2] = AxisAlignedBB.getBoundingBox(bxz, rackC, 0, bXZ, 1, rackA);
            rackBottom[3] = AxisAlignedBB.getBoundingBox(bxz, rackC, 1 - rackA, bXZ, 1, 1);
            // across
            rackBottom[4] = AxisAlignedBB.getBoundingBox(axz, 1 - rackD, rackA, aXZ, 1, 1 - rackA);
        }

        rackMiddle = new AxisAlignedBB[2];
        {
            float bxz = (1 - rackB) * 0.5f;
            float bXZ = (1 + rackB) * 0.5f;

            // legs
            rackMiddle[0] = AxisAlignedBB.getBoundingBox(bxz, 0, 0, bXZ, 1, rackA);
            rackMiddle[1] = AxisAlignedBB.getBoundingBox(bxz, 0, 1 - rackA, bXZ, 1, 1);

            rackMiddleAll = AxisAlignedBB.getBoundingBox(bxz, 0, 0, bXZ, 1, 1);
        }

        rackTop = new AxisAlignedBB[5];
        {
            float axz = (1 - rackA) * 0.5f;
            float aXZ = (1 + rackA) * 0.5f;
            float bxz = (1 - rackB) * 0.5f;
            float bXZ = (1 + rackB) * 0.5f;

            // arms
            rackTop[0] = AxisAlignedBB.getBoundingBox(0, 1 - rackD * 2, 0, 1, 1 - rackD, rackA);
            rackTop[1] = AxisAlignedBB.getBoundingBox(0, 1 - rackD * 2, 1 - rackA, 1, 1 - rackD, 1);
            // legs
            rackTop[2] = AxisAlignedBB.getBoundingBox(bxz, 0, 0, bXZ, 1 - rackD * 2, rackA);
            rackTop[3] = AxisAlignedBB.getBoundingBox(bxz, 0, 1 - rackA, bXZ, 1 - rackD * 2, 1);
            // across
            rackTop[4] = AxisAlignedBB.getBoundingBox(axz, 1 - rackD, 0, aXZ, 1, 1);
        }

        rackBridge = new AxisAlignedBB[2];
        {
            // arms
            rackBridge[0] = AxisAlignedBB.getBoundingBox(0, 1 - rackD * 2, 0, 1, 1 - rackD, rackA);
            rackBridge[1] = AxisAlignedBB.getBoundingBox(0, 1 - rackD * 2, 1 - rackA, 1, 1 - rackD, 1);

            rackBridgeAll = AxisAlignedBB.getBoundingBox(0, 1 - rackD * 2, 0, 1, 1 - rackD, 1);
        }

        rackBottomInv = new AxisAlignedBB[rackBottom.length + rackMiddle.length];
        {
            int i = 0;
            for (AxisAlignedBB bb : rackBottom) {
                rackBottomInv[i++] = bb.copy().offset(0.2, -0.2, 0.2);
            }
            for (AxisAlignedBB bb : rackMiddle) {
                rackBottomInv[i++] = bb.copy().offset(0.2, 0.8, 0.2);
            }

            double s = 0.6;
            for (AxisAlignedBB bb : rackBottomInv) {
                bb.setBounds(bb.minX * s, bb.minY * s, bb.minZ * s, bb.maxX * s, bb.maxY * s, bb.maxZ * s);
            }
        }

        rackTopInv = new AxisAlignedBB[rackTop.length * 2];
        {
            int i = 0;
            for (AxisAlignedBB bb : rackTop) {
                rackTopInv[i++] = bb.copy().offset(-0.2, 0, 0.2);
            }
            for (AxisAlignedBB bb : rackTop) {
                rackTopInv[i++] = bb.copy().offset(0.8, 0, 0.2);
            }

            double s = 0.6;
            for (AxisAlignedBB bb : rackTopInv) {
                bb.setBounds(bb.minX * s, bb.minY * s, bb.minZ * s, bb.maxX * s, bb.maxY * s, bb.maxZ * s);
            }
        }

        basinBottom = new AxisAlignedBB[1];
        {
            basinBottom[0] = AxisAlignedBB.getBoundingBox(basinRimW, 0, basinRimW, 1 - basinRimW, basinBottomH, 1 - basinRimW);
        }

        {
            basinFluid = AxisAlignedBB.getBoundingBox(basinRimW, basinBottomH, basinRimW, 1 - basinRimW, basinBottomH + basinLiquidH, 1 - basinRimW);
        }

        basinSides = new AxisAlignedBB[4];
        {
            float bxz = 0;
            float bXZ = 1;

            basinSides[0] = AxisAlignedBB.getBoundingBox(bxz, 0, bxz, bxz + basinRimW, basinRimH, bXZ);
            basinSides[1] = AxisAlignedBB.getBoundingBox(bXZ - basinRimW, 0, bxz, bXZ, basinRimH, bXZ);
            basinSides[2] = AxisAlignedBB.getBoundingBox(bxz + basinRimW, 0, bxz, bXZ - basinRimW, basinRimH, bxz + basinRimW);
            basinSides[3] = AxisAlignedBB.getBoundingBox(bxz + basinRimW, 0, bXZ - basinRimW, bXZ - basinRimW, basinRimH, bXZ);
        }

        basketBottom = new AxisAlignedBB[2];
        {
            float bxz = basketBottomXZ + basketRimW;
            float bXZ = 1 - basketBottomXZ - basketRimW;

            basketBottom[0] = AxisAlignedBB.getBoundingBox(bxz, basketBottomY, bxz, bXZ, basketBottomY + basketBottomH, bXZ);

            basketBottom[1] = AxisAlignedBB.getBoundingBox(basketFootXZ, basinBottomH, basketFootXZ, 1 - basketFootXZ, basketBottomY, 1 - basketFootXZ);
        }

        basketSides = new AxisAlignedBB[4];
        {
            float bxz = basketBottomXZ;
            float bXZ = 1 - basketBottomXZ;

            basketSides[0] = AxisAlignedBB.getBoundingBox(bxz, basketBottomY, bxz, bxz + basketRimW, 1, bXZ);
            basketSides[1] = AxisAlignedBB.getBoundingBox(bXZ - basketRimW, basketBottomY, bxz, bXZ, 1, bXZ);
            basketSides[2] = AxisAlignedBB.getBoundingBox(bxz + basketRimW, basketBottomY, bxz, bXZ - basketRimW, 1, bxz + basketRimW);
            basketSides[3] = AxisAlignedBB.getBoundingBox(bxz + basketRimW, basketBottomY, bXZ - basketRimW, bXZ - basketRimW, 1, bXZ);
        }

        basketHoops = new AxisAlignedBB[4];
        {
            float hxz = basketBottomXZ - basketHoopW;
            float hXZ = 1 - basketBottomXZ + basketHoopW;

            basketHoops[0] = AxisAlignedBB.getBoundingBox(hxz, basketHoopY, hxz, hxz + basketHoopW, 1, hXZ);
            basketHoops[1] = AxisAlignedBB.getBoundingBox(hXZ - basketHoopW, basketHoopY, hxz, hXZ, 1, hXZ);
            basketHoops[2] = AxisAlignedBB.getBoundingBox(hxz + basketHoopW, basketHoopY, hxz, hXZ - basketHoopW, 1, hxz + basketHoopW);
            basketHoops[3] = AxisAlignedBB.getBoundingBox(hxz + basketHoopW, basketHoopY, hXZ - basketHoopW, hXZ - basketHoopW, 1, hXZ);
        }

        disc = new AxisAlignedBB[5];
        discRotated = new AxisAlignedBB[5];
        {
            float ex = (1 - discEarW) * 0.5f;
            float eX = (1 + discEarW) * 0.5f;
            float dz = discBottomXZ;
            float dZ = 1 - discBottomXZ;

            // disc
            disc[0] = AxisAlignedBB.getBoundingBox(discBottomXZ, 0, dz, 1 - discBottomXZ, discBottomH, dZ);
            // ears
            disc[1] = AxisAlignedBB.getBoundingBox(ex, discBottomH, dz, eX, discBottomH + discEarH, dz + discEarD);
            disc[2] = AxisAlignedBB.getBoundingBox(ex, discBottomH, dZ - discEarD, eX, discBottomH + discEarH, dZ);
            // across
            disc[3] = AxisAlignedBB.getBoundingBox(ex, discBottomH, dz + discEarD, eX, discBottomH + discTopH, dZ - discEarD);
            disc[4] = AxisAlignedBB.getBoundingBox(ex, discBottomH + discTopH + discJointY, dz + discEarD, eX, discBottomH + discTopH + discJointY + discJointH, dZ - discEarD);

            for (int i = 0; i < 5; i++) {
                discRotated[i] = AxisAlignedBB.getBoundingBox(disc[i].minY, disc[i].minX, disc[i].minZ, disc[i].maxY, disc[i].maxX, disc[i].maxZ);
            }
        }

        lever = new AxisAlignedBB[5];
        leverInv = new AxisAlignedBB[3];
        {
            float ly = (1 - leverH) * 0.5f;
            float lY = (1 + leverH) * 0.5f;
            float lz = (1 - leverD) * 0.5f;
            float lZ = (1 + leverD) * 0.5f;
            float pz = (1 - leverPreForkD) * 0.5f;
            float pZ = (1 + leverPreForkD) * 0.5f;
            float f1z = (1 - leverForkD * 2 - leverForkS) * 0.5f;
            float f1Z = (1 - leverForkS) * 0.5f;
            float f2z = (1 + leverForkS) * 0.5f;
            float f2Z = (1 + leverForkD * 2 + leverForkS) * 0.5f;

            // main
            lever[0] = AxisAlignedBB.getBoundingBox(leverA, ly, lz, leverB - leverForkW - leverPreForkW, lY, lZ);
            // fork
            lever[1] = AxisAlignedBB.getBoundingBox(leverB - leverForkW - leverPreForkW, ly, pz, leverB - leverForkW, lY, pZ);
            lever[2] = AxisAlignedBB.getBoundingBox(leverB - leverForkW, ly, f1Z, leverB - leverForkW + leverForkR, lY, f2z);
            lever[3] = AxisAlignedBB.getBoundingBox(leverB - leverForkW, ly, f1z, leverB, lY, f1Z);
            lever[4] = AxisAlignedBB.getBoundingBox(leverB - leverForkW, ly, f2z, leverB, lY, f2Z);

            leverInv[0] = AxisAlignedBB.getBoundingBox(0.1, ly, lz, 1.3, lY, lZ);
            leverInv[1] = AxisAlignedBB.getBoundingBox(-0.2, ly, f1z, 0.3, lY, f1Z);
            leverInv[2] = AxisAlignedBB.getBoundingBox(-0.2, ly, f2z, 0.3, lY, f2Z);

            leverAll = AxisAlignedBB.getBoundingBox(leverA, ly, f1z, leverB, lY, f2Z);
        }

        nut = new AxisAlignedBB[1];
        {
            float nx = (1 - nutW) * 0.5f;
            float nX = (1 + nutW) * 0.5f;
            float nz = (1 - nutD) * 0.5f;
            float nZ = (1 + nutD) * 0.5f;

            nut[0] = AxisAlignedBB.getBoundingBox(nx, 0, nz, nX, nutH, nZ);
        }

        bolt = new AxisAlignedBB[1];
        {
            float bxy = (1 - boltW) * 0.5f;
            float bXY = (1 + boltW) * 0.5f;
            float bz = (1 - boltD) * 0.5f;
            float bZ = (1 + boltD) * 0.5f;

            bolt[0] = AxisAlignedBB.getBoundingBox(bxy, bxy, bz, bXY, bXY, bZ);
        }

        if (orientation == 1) {
            flipAxisAlignedBBXZ(rackBottom);
            flipAxisAlignedBBXZ(rackMiddle);
            flipAxisAlignedBBXZ(rackTop);
            flipAxisAlignedBBXZ(rackBridge);
            flipAxisAlignedBBXZ(disc);
            flipAxisAlignedBBXZ(lever);
            flipAxisAlignedBBXZ(bolt);

            flipAxisAlignedBBXZ(rackMiddleAll);
            flipAxisAlignedBBXZ(rackBridgeAll);
            flipAxisAlignedBBXZ(leverAll);
        }
    }

    private void flipAxisAlignedBBXZ(AxisAlignedBB[] bbs) {
        for (AxisAlignedBB bb : bbs) {
            AxisAlignedBB copy = bb.copy();
            bb.setBounds(copy.minZ, copy.minY, copy.minX, copy.maxZ, copy.maxY, copy.maxX);
        }
    }

    private void flipAxisAlignedBBXZ(AxisAlignedBB bb) {
        AxisAlignedBB copy = bb.copy();
        bb.setBounds(copy.minZ, copy.minY, copy.minX, copy.maxZ, copy.maxY, copy.maxX);
    }

    public AxisAlignedBB[] getRackBottom() {
        return rackBottom;
    }

    public AxisAlignedBB[] getRackMiddle() {
        return rackMiddle;
    }

    public AxisAlignedBB[] getRackTop() {
        return rackTop;
    }

    public AxisAlignedBB[] getRackBridge() {
        return rackBridge;
    }

    public AxisAlignedBB[] getRackBottomInv() {
        return rackBottomInv;
    }

    public AxisAlignedBB[] getRackTopInv() {
        return rackTopInv;
    }

    public AxisAlignedBB[] getBasinBottom() {
        return basinBottom;
    }

    public AxisAlignedBB[] getBasinSides() {
        return basinSides;
    }

    public AxisAlignedBB getBasinFluid() {
        return basinFluid;
    }

    public AxisAlignedBB[] getBasketBottom() {
        return basketBottom;
    }

    public AxisAlignedBB[] getBasketSides() {
        return basketSides;
    }

    public AxisAlignedBB[] getBasketHoops() {
        return basketHoops;
    }

    public AxisAlignedBB[] getDisc() {
        return disc;
    }

    public AxisAlignedBB[] getDiscRotated() {
        return discRotated;
    }

    public AxisAlignedBB[] getLever() {
        return lever;
    }

    public AxisAlignedBB[] getLeverInv() {
        return leverInv;
    }

    public AxisAlignedBB[] getNut() {
        return nut;
    }

    public AxisAlignedBB[] getBolt() {
        return bolt;
    }

    public AxisAlignedBB getLeverAll() {
        return leverAll;
    }

    public AxisAlignedBB getRackMiddleAll() {
        return rackMiddleAll;
    }

    public AxisAlignedBB getRackBridgeAll() {
        return rackBridgeAll;
    }

}
