package com.unforbidable.tfc.bids.Core.Carving;

import net.minecraft.util.Vec3;

public class CarvingBitCollision {

    public final Vec3 hitVec;
    public final int side;
    public final double distance;

    public CarvingBitCollision(Vec3 hitVec, int side, double distance) {
        this.hitVec = hitVec;
        this.side = side;
        this.distance = distance;
    }

}
