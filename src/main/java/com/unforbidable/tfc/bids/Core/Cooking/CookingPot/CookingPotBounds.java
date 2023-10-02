package com.unforbidable.tfc.bids.Core.Cooking.CookingPot;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class CookingPotBounds {

    private static final float rimWidth = 2 / 32f;
    private static final float rimHeight = 10 / 32f;
    private static final float rimLift = 2 / 32f;
    private static final float bottomHeight = 2 / 32f;
    private static final float bottomWidth = 16 / 32f;
    private static final float lidLift = 12 / 32f;
    private static final float lidWidth = 20 / 32f;
    private static final float lidHeight = 2 / 32f;
    private static final float lidHandleHeight = 2 / 32f;
    private static final float lidHandleWidth = 4 / 32f;
    private static final float meshLift = 10 / 32f;
    private static final float meshHeight = 1 / 32f;

    private static final float maxContentHeight = 8 / 32f;

    AxisAlignedBB entireBounds;
    AxisAlignedBB bottomBounds;
    AxisAlignedBB[] sidesBounds = new AxisAlignedBB[4];
    AxisAlignedBB[] lidsBounds = new AxisAlignedBB[2];
    AxisAlignedBB contentBounds;
    AxisAlignedBB meshBounds;
    Vec3 itemPos;
    Vec3 itemPosWithMesh;

    public CookingPotBounds(CookingPotPlacement placement) {
        float bottomXZ = (1 - bottomWidth) / 2;
        bottomBounds = AxisAlignedBB.getBoundingBox(bottomXZ, 0, bottomXZ, 1 - bottomXZ, bottomHeight, 1 - bottomXZ);

        float rimStartXZ = bottomXZ - rimWidth / 2;
        float rimEndXZ = bottomXZ + rimWidth / 2;
        sidesBounds[0] = AxisAlignedBB.getBoundingBox(rimStartXZ, rimLift, rimEndXZ, rimEndXZ, rimLift + rimHeight, 1 - rimEndXZ);
        sidesBounds[1] = AxisAlignedBB.getBoundingBox(1 - rimEndXZ, rimLift, rimEndXZ, 1 - rimStartXZ, rimLift + rimHeight, 1 - rimEndXZ);
        sidesBounds[2] = AxisAlignedBB.getBoundingBox(rimStartXZ, rimLift, rimStartXZ, 1 - rimStartXZ, rimLift + rimHeight, rimEndXZ);
        sidesBounds[3] = AxisAlignedBB.getBoundingBox(rimStartXZ, rimLift, 1 - rimEndXZ, 1 - rimStartXZ, rimLift + rimHeight, 1 - rimStartXZ);

        float lidXZ = (1 - lidWidth) / 2;
        float lidHandleXZ = (1 - lidHandleWidth) / 2;
        lidsBounds[0] = AxisAlignedBB.getBoundingBox(lidXZ, lidLift, lidXZ, 1 - lidXZ, lidLift + lidHeight, 1 - lidXZ);
        lidsBounds[1] = AxisAlignedBB.getBoundingBox(lidHandleXZ, lidLift + lidHeight, lidHandleXZ, 1 - lidHandleXZ, lidLift + lidHeight + lidHandleHeight, 1 - lidHandleXZ);

        contentBounds = AxisAlignedBB.getBoundingBox(bottomXZ, bottomHeight, bottomXZ, 1 - bottomXZ, bottomHeight + (1 / 32f), 1 - bottomXZ);

        meshBounds = AxisAlignedBB.getBoundingBox(bottomXZ, meshLift, bottomXZ, 1 - bottomXZ, meshLift + meshHeight, 1 - bottomXZ);

        entireBounds = AxisAlignedBB.getBoundingBox(rimStartXZ, 0, rimStartXZ, 1 - rimStartXZ, lidLift + lidHeight + lidHandleHeight, 1 - rimStartXZ);

        itemPos = Vec3.createVectorHelper(0.5, bottomHeight, 0.5);
        itemPosWithMesh = Vec3.createVectorHelper(0.5, meshLift - (2 / 32f), 0.5);

        Vec3 offset = placement.getOffset();
        if (offset.xCoord != 0 || offset.yCoord != 0 || offset.zCoord != 0) {
            bottomBounds.offset(offset.xCoord, offset.yCoord, offset.zCoord);
            sidesBounds[0].offset(offset.xCoord, offset.yCoord, offset.zCoord);
            sidesBounds[1].offset(offset.xCoord, offset.yCoord, offset.zCoord);
            sidesBounds[2].offset(offset.xCoord, offset.yCoord, offset.zCoord);
            sidesBounds[3].offset(offset.xCoord, offset.yCoord, offset.zCoord);
            lidsBounds[0].offset(offset.xCoord, offset.yCoord, offset.zCoord);
            lidsBounds[1].offset(offset.xCoord, offset.yCoord, offset.zCoord);
            contentBounds.offset(offset.xCoord, offset.yCoord, offset.zCoord);
            meshBounds.offset(offset.xCoord, offset.yCoord, offset.zCoord);
            entireBounds.offset(offset.xCoord, offset.yCoord, offset.zCoord);
            itemPos = itemPos.addVector(offset.xCoord, offset.yCoord, offset.zCoord);
            itemPosWithMesh = itemPosWithMesh.addVector(offset.xCoord, offset.yCoord, offset.zCoord);
        }
    }

    public static final AxisAlignedBB LID_ONLY_ENTIRE_BOUNDS = getEntireLidBoundsOnly();
    public static final AxisAlignedBB[] LID_ONLY_BOUNDS = getLidsBoundsOnly();
    public static final AxisAlignedBB GROUND_BOUNDS = getGroundBounds();

    private static AxisAlignedBB getGroundBounds() {
        return AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 2 / 32f, 1);
    }

    private static AxisAlignedBB getEntireLidBoundsOnly() {
        float lidXZ = (1 - lidWidth) / 2;
        return AxisAlignedBB.getBoundingBox(lidXZ, 0, lidXZ, 1 - lidXZ, 0 + lidHeight + lidHandleHeight, 1 - lidXZ);
    }

    private static AxisAlignedBB[] getLidsBoundsOnly() {
        float lidXZ = (1 - lidWidth) / 2;
        float lidHandleXZ = (1 - lidHandleWidth) / 2;

        AxisAlignedBB[] lidsBounds = new AxisAlignedBB[2];
        lidsBounds[0] = AxisAlignedBB.getBoundingBox(lidXZ, 0, lidXZ, 1 - lidXZ, 0 + lidHeight, 1 - lidXZ);
        lidsBounds[1] = AxisAlignedBB.getBoundingBox(lidHandleXZ, 0 + lidHeight, lidHandleXZ, 1 - lidHandleXZ, 0 + lidHeight + lidHandleHeight, 1 - lidHandleXZ);
        return lidsBounds;
    }

    public AxisAlignedBB getEntireBounds() {
        return entireBounds;
    }

    public AxisAlignedBB getBottomBounds() {
        return bottomBounds;
    }

    public AxisAlignedBB[] getSidesBounds() {
        return sidesBounds;
    }

    public AxisAlignedBB[] getLidsBounds() {
        return lidsBounds;
    }

    public AxisAlignedBB getContentBounds() {
        return contentBounds;
    }

    public AxisAlignedBB getMeshBounds() {
        return meshBounds;
    }

    public Vec3 getItemPos() {
        return itemPos;
    }

    public Vec3 getItemPosWithMesh() {
        return itemPosWithMesh;
    }

    public float getMaxContentHeight() {
        return maxContentHeight;
    }

    public static CookingPotBounds getBoundsForPlacement(CookingPotPlacement placement) {
        return new CookingPotBounds(placement);
    }

}
