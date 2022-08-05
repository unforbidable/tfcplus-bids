package com.unforbidable.tfc.bids.Core.Common.Collision;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class CollisionHelper {

    public static CollisionInfo rayTraceAABB(AxisAlignedBB bound, Vec3 startVec, Vec3 endVec) {
        Vec3 minX = startVec.getIntermediateWithXValue(endVec, bound.minX);
        Vec3 maxX = startVec.getIntermediateWithXValue(endVec, bound.maxX);
        Vec3 minY = startVec.getIntermediateWithYValue(endVec, bound.minY);
        Vec3 maxY = startVec.getIntermediateWithYValue(endVec, bound.maxY);
        Vec3 minZ = startVec.getIntermediateWithZValue(endVec, bound.minZ);
        Vec3 maxZ = startVec.getIntermediateWithZValue(endVec, bound.maxZ);

        if (!isVecInsideYZBounds(bound, minX))
            minX = null;
        if (!isVecInsideYZBounds(bound, maxX))
            maxX = null;
        if (!isVecInsideXZBounds(bound, minY))
            minY = null;
        if (!isVecInsideXZBounds(bound, maxY))
            maxY = null;
        if (!isVecInsideXYBounds(bound, minZ))
            minZ = null;
        if (!isVecInsideXYBounds(bound, maxZ))
            maxZ = null;

        Vec3 hitVec = null;
        if (minX != null && (hitVec == null || startVec.distanceTo(minX) < startVec.distanceTo(hitVec)))
            hitVec = minX;
        if (maxX != null && (hitVec == null || startVec.distanceTo(maxX) < startVec.distanceTo(hitVec)))
            hitVec = maxX;
        if (minY != null && (hitVec == null || startVec.distanceTo(minY) < startVec.distanceTo(hitVec)))
            hitVec = minY;
        if (maxY != null && (hitVec == null || startVec.distanceTo(maxY) < startVec.distanceTo(hitVec)))
            hitVec = maxY;
        if (minZ != null && (hitVec == null || startVec.distanceTo(minZ) < startVec.distanceTo(hitVec)))
            hitVec = minZ;
        if (maxZ != null && (hitVec == null || startVec.distanceTo(maxZ) < startVec.distanceTo(hitVec)))
            hitVec = maxZ;

        if (hitVec == null)
            return null;

        int side = -1;
        if (hitVec == minX)
            side = 4;
        if (hitVec == maxX)
            side = 5;
        if (hitVec == minY)
            side = 0;
        if (hitVec == maxY)
            side = 1;
        if (hitVec == minZ)
            side = 2;
        if (hitVec == maxZ)
            side = 3;

        return new CollisionInfo(hitVec, side, startVec.distanceTo(hitVec));
    }

    private static boolean isVecInsideYZBounds(AxisAlignedBB bound, Vec3 vec3) {
        if (vec3 == null)
            return false;
        else
            return vec3.yCoord >= bound.minY && vec3.yCoord <= bound.maxY && vec3.zCoord >= bound.minZ
                    && vec3.zCoord <= bound.maxZ;
    }

    private static boolean isVecInsideXZBounds(AxisAlignedBB bound, Vec3 vec3) {
        if (vec3 == null)
            return false;
        else
            return vec3.xCoord >= bound.minX && vec3.xCoord <= bound.maxX && vec3.zCoord >= bound.minZ
                    && vec3.zCoord <= bound.maxZ;
    }

    private static boolean isVecInsideXYBounds(AxisAlignedBB bound, Vec3 vec3) {
        if (vec3 == null)
            return false;
        else
            return vec3.xCoord >= bound.minX && vec3.xCoord <= bound.maxX && vec3.yCoord >= bound.minY
                    && vec3.yCoord <= bound.maxY;
    }

}
