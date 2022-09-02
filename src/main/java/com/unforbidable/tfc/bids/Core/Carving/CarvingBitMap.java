package com.unforbidable.tfc.bids.Core.Carving;

import com.unforbidable.tfc.bids.Bids;

import net.minecraft.nbt.NBTTagCompound;

public class CarvingBitMap {

    final int dimension;
    byte[] data;

    public CarvingBitMap(int dimension) {
        this.dimension = dimension;
        final int totalBits = dimension * dimension * dimension / 8;
        data = new byte[totalBits];
    }

    public void setBit(CarvingBit bit) {
        int i = bit.flatten(dimension);
        data[i / 8] |= 1 << (i % 8);
    }

    public void clearBit(CarvingBit bit) {
        int i = bit.flatten(dimension);
        data[i / 8] &= ~(1 << (i % 8));
    }

    public boolean testBit(int bitX, int bitY, int bitZ) {
        return testBit(new CarvingBit(bitX, bitY, bitZ));
    }

    public boolean testBit(CarvingBit bit) {
        int i = bit.flatten(dimension);
        return (data[i / 8] & (1 << i % 8)) != 0;
    }

    public int count() {
        final int totalBits = dimension * dimension * dimension / 8;
        int carvedBits = 0;
        int i = 0;
        while (i < totalBits) {
            int j = 0;
            int b = data[i++];
            while (j++ < 8) {
                if ((b & 1) != 0)
                    carvedBits++;

                b >>= 1;
            }
        }
        return carvedBits;
    }

    public void writeToNBT(NBTTagCompound tag, String name) {
        tag.setByteArray(name, data);
    }

    public void readFromNBT(NBTTagCompound tag, String name) {
        byte[] loaded = tag.getByteArray(name);
        setBytes(loaded);
    }

    public byte[] getBytes() {
        return data;
    }

    public void setBytes(byte[] loaded) {
        if (loaded.length == 0) {
            // Nothing loaded so just re-init
            data = new byte[data.length];
        } else if (loaded.length > data.length) {
            Bids.LOG.warn("Loaded data is larger " + loaded.length
                    + " and will be trimmed to " + data.length);
            System.arraycopy(loaded, 0, data, 0, data.length);
        } else if (loaded.length < data.length) {
            Bids.LOG.warn("Loaded data is smaller " + loaded.length
                    + " and will be padded to " + data.length);
            data = new byte[data.length];
            System.arraycopy(loaded, 0, data, 0, loaded.length);
        } else {
            data = loaded;
        }
    }

    public CarvingBitMap rotate(int rotation) {
        CarvingBitMap rotated = new CarvingBitMap(dimension);

        for (int bitX = 0; bitX < dimension; bitX++) {
            for (int bitY = 0; bitY < dimension; bitY++) {
                for (int bitZ = 0; bitZ < dimension; bitZ++) {
                    if (testBit(bitX, bitY, bitZ)) {
                        rotated.setBit(getRotatedBit(bitX, bitY, bitZ, rotation));
                    }
                }
            }
        }

        return rotated;
    }

    private CarvingBit getRotatedBit(int bitX, int bitY, int bitZ, int rotation) {
        switch (rotation % 4) {
            case 0:
                return new CarvingBit(bitX, bitY, bitZ);

            case 1:
                return new CarvingBit(bitZ, bitY, dimension - bitX - 1);

            case 2:
                return new CarvingBit(dimension - bitX - 1, bitY, dimension - bitZ - 1);

            case 3:
            default:
                return new CarvingBit(dimension - bitZ - 1, bitY, bitX);
        }
    }

    public boolean equals(CarvingBitMap carvedBits) {
        if (dimension != carvedBits.dimension) {
            return false;
        }

        for (int bitX = 0; bitX < dimension; bitX++) {
            for (int bitY = 0; bitY < dimension; bitY++) {
                for (int bitZ = 0; bitZ < dimension; bitZ++) {
                    if (testBit(bitX, bitY, bitZ) != carvedBits.testBit(bitX, bitY, bitZ)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
