package com.unforbidable.tfc.bids.Core.Quarry;

public class QuarryDrillTarget {

    public final int x;
    public final int y;
    public final int z;
    public final int side;

    public QuarryDrillTarget(int x, int y, int z, int side) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
        this.side = side;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuarryDrillTarget) {
            QuarryDrillTarget o = (QuarryDrillTarget) obj;
            return x == o.x && y == o.y && z == o.z && side == o.side;
        }

        return super.equals(obj);
    }

}
