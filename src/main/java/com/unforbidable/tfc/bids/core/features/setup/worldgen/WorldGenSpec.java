package com.unforbidable.tfc.bids.core.features.setup.worldgen;

import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenSpec {

    public final IWorldGenerator generator;
    public final int priority;

    public WorldGenSpec(IWorldGenerator generator, int priority) {
        this.generator = generator;
        this.priority = priority;
    }

}
