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

}
