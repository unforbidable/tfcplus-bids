package com.unforbidable.tfc.bids.core.features.setup.worldgen;

import cpw.mods.fml.common.IWorldGenerator;
import java.util.ArrayList;
import java.util.List;

public class WorldGenSpecCollector {

    private final List<WorldGenSpec> generators = new ArrayList<>();

    public WorldGenSpecCollector gen(IWorldGenerator generator, int priority) {
        generators.add(new WorldGenSpec(generator, priority));

        return this;
    }

    public List<WorldGenSpec> build() {
        return generators;
    }

}
