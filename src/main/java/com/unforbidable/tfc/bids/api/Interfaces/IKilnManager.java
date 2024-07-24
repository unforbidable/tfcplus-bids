package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.nbt.NBTTagCompound;

public interface IKilnManager {

    void writeKilnManagerToNBT(NBTTagCompound tag);
    void readKilnManagerFromNBT(NBTTagCompound tag);
    void update();

}
