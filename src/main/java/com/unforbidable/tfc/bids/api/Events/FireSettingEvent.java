package com.unforbidable.tfc.bids.api.Events;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public abstract class FireSettingEvent extends Event {

    public final World world;
    public final int sourceX;
    public final int sourceY;
    public final int sourceZ;

    public FireSettingEvent(World world, int sourceX, int sourceY, int sourceZ) {
        this.world = world;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
    }

    public static class BurningEvent extends FireSettingEvent {
        public final long ticks;
        public final int temp;
        public final int items;

        public BurningEvent(World world, int sourceX, int sourceY, int sourceZ, long ticks, int temp, int items) {
            super(world, sourceX, sourceY, sourceZ);
            this.ticks = ticks;
            this.temp = temp;
            this.items = items;
        }
    }

    public static abstract class FireSettingTargetEvent extends FireSettingEvent {
        public final int targetX;
        public final int targetY;
        public final int targetZ;

        public FireSettingTargetEvent(World world, int sourceX, int sourceY, int sourceZ, int targetX, int targetY, int targetZ) {
            super(world, sourceX, sourceY, sourceZ);
            this.targetX = targetX;
            this.targetY = targetY;
            this.targetZ = targetZ;
        }
    }

    @Cancelable
    public static class BlockCrackingEvent extends FireSettingTargetEvent {
        public BlockCrackingEvent(World world, int sourceX, int sourceY, int sourceZ, int targetX, int targetY, int targetZ) {
            super(world, sourceX, sourceY, sourceZ, targetX, targetY, targetZ);
        }
    }

    public static class BlockCrackedEvent extends FireSettingTargetEvent {
        public BlockCrackedEvent(World world, int sourceX, int sourceY, int sourceZ, int targetX, int targetY, int targetZ) {
            super(world, sourceX, sourceY, sourceZ, targetX, targetY, targetZ);
        }
    }

}
