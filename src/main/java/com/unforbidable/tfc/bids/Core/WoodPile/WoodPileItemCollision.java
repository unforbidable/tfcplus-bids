package com.unforbidable.tfc.bids.Core.WoodPile;

import net.minecraft.util.Vec3;

public class WoodPileItemCollision {

    public final Vec3 hitVec;
    public final int side;
    public final double distance;

    public WoodPileItemCollision(Vec3 hitVec, int side, double distance) {
        this.hitVec = hitVec;
        this.side = side;
        this.distance = distance;
    }

}
