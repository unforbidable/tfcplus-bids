package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.world.World;

public interface IKilnHeatSource {

    World getWorld();
    int getTileX();
    int getTileY();
    int getTileZ();
    boolean isActive();
    double getProgress();
    void resetProgress();

}
