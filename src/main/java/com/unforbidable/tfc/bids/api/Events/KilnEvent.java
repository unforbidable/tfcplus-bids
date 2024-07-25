package com.unforbidable.tfc.bids.api.Events;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public abstract class KilnEvent extends Event {

    public final World world;
    public final int x;
    public final int y;
    public final int z;

    public KilnEvent(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static class FireBlockCheck extends KilnEvent {

        public boolean success;

        public FireBlockCheck(World world, int x, int y, int z, boolean success) {
            super(world, x, y, z);

            this.success = success;
        }
    }

    public static class FireBlock extends KilnEvent {

        public final double progress;

        public FireBlock(World world, int x, int y, int z, double progress) {
            super(world, x, y, z);

            this.progress = progress;
        }

    }

}
