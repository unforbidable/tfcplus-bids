package com.unforbidable.tfc.bids.api.features.drying;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;

public abstract class DryingPegsAnchorEvent extends Event {

    public final World world;
    public final int x;
    public final int y;
    public final int z;
    public final Block block;

    public DryingPegsAnchorEvent(World world, int x, int y, int z, Block block) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
    }

    @Cancelable
    public static class Attach extends DryingPegsAnchorEvent {

        public boolean canAttach;

        public Attach(World world, int x, int y, int z, Block block) {
            super(world, x, y, z, block);
        }

    }

    public static class Bounds extends DryingPegsAnchorEvent {

        public AxisAlignedBB knotBounds;

        public Bounds(World world, int x, int y, int z, Block block) {
            super(world, x, y, z, block);
        }

    }

}
