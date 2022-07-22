package com.unforbidable.tfc.bids.Core.Carving;

import com.unforbidable.tfc.bids.Bids;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class CarvingBit {

    private final boolean isEmpty;
    public final int bitX;
    public final int bitY;
    public final int bitZ;

    public static final CarvingBit Empty = new CarvingBit();

    CarvingBit() {
        bitX = 0;
        bitY = 0;
        bitZ = 0;
        isEmpty = true;
    }

    public CarvingBit(int bitX, int bitY, int bitZ) {
        this.bitX = bitX;
        this.bitY = bitY;
        this.bitZ = bitZ;
        isEmpty = false;
    }

    public int flatten(int dimension) {
        if (isEmpty) {
            Bids.LOG.warn("Trying to use an empty bit");
            return 0;
        }

        if (bitX >= dimension || bitY >= dimension || bitZ >= dimension ||
                bitX < 0 || bitY < 0 || bitX < 0) {
            Bids.LOG.warn("Trying to use an out of bounds bit: " + bitX + ", " + bitY + ", " + bitZ);
            return 0;
        }

        return flatten(bitX, bitY, bitZ, dimension);
    }

    private static int flatten(int bitX, int bitY, int bitZ, int dimension) {
        return ((bitX * dimension) + bitY) * dimension + bitZ;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public CarvingBit getBitToDirection(ForgeDirection d) {
        return new CarvingBit(bitX + d.offsetX, bitY + d.offsetY, bitZ + d.offsetZ);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final CarvingBit o = (CarvingBit) obj;

        if (isEmpty && o.isEmpty)
            return true;

        if (isEmpty && !o.isEmpty || !isEmpty && o.isEmpty)
            return false;

        return bitX == o.bitX && bitY == o.bitY && bitZ == o.bitZ;
    }

    @Override
    public int hashCode() {
        return isEmpty ? -1 : flatten(16);
    }

    public void writeToNBT(NBTTagCompound tag, String name) {
        int value = ((bitZ << 4) + bitY) << 4 + bitX;
        tag.setShort(name, (short) value);
    }

    public static CarvingBit readFromNBT(NBTTagCompound tag, String name) {
        int value = tag.getShort(name);
        return new CarvingBit(value & 0xf, (value >> 4) & 0xf, (value >> 8) & 0xf);
    }

}
