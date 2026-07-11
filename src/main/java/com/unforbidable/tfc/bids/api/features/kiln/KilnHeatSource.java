package com.unforbidable.tfc.bids.api.features.kiln;

import net.minecraft.world.World;

public interface KilnHeatSource {

    World getWorld();
    int getTileX();
    int getTileY();
    int getTileZ();
    boolean isActive();
    double getProgress();
    void resetProgress();

}
