package com.unforbidable.tfc.bids.api.features.kiln;

import net.minecraft.nbt.NBTTagCompound;

public interface KilnEngine {

    void writeKilnManagerToNBT(NBTTagCompound tag);
    void readKilnManagerFromNBT(NBTTagCompound tag);
    void update();
    void updateProgress(double lastKilnProgress, double currentKilnProgress);

}
