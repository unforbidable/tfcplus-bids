package com.unforbidable.tfc.bids.Core.Common;

public class BlockCoord {

    public final int x;
    public final int y;
    public final int z;

    public BlockCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distanceTo(BlockCoord bc) {
        int d0 = bc.x - x;
        int d1 = bc.y - y;
        int d2 = bc.z - z;
        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockCoord that = (BlockCoord) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        return z == that.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

}
