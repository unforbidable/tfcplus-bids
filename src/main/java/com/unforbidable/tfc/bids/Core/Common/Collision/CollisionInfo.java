package com.unforbidable.tfc.bids.Core.Common.Collision;

import net.minecraft.util.Vec3;

public class CollisionInfo {

    public final Vec3 hitVec;
    public final int side;
    public final double distance;

    public CollisionInfo(Vec3 hitVec, int side, double distance) {
        this.hitVec = hitVec;
        this.side = side;
        this.distance = distance;
    }

}
