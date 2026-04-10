package com.unforbidable.tfc.bids.Core.Drying.Environment;

import net.minecraft.world.World;

public class StaticEnvironment {

    protected final World world;
    protected final int blockX;
    protected final int blockY;
    protected final int blockZ;

    private Boolean exposed;
    private Boolean heated;

    public StaticEnvironment(World world, int blockX, int blockY, int blockZ) {
        this.world = world;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }

    public DynamicEnvironment ofTicks(long ticks) {
        return new DynamicEnvironment(world, blockX, blockY, blockZ, ticks, this);
    }

    public boolean isExposed() {
        if (exposed == null) {
            exposed = EnvironmentHelper.isExposed(world, blockX, blockY, blockZ);
        }

        return exposed;
    }

    public boolean isHeated() {
        if (heated == null) {
            heated = EnvironmentHelper.isHeatSourceNearby(world, blockX, blockY, blockZ);
        }

        return heated;
    }

}
