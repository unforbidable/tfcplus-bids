package com.unforbidable.tfc.bids.features.crafting.firestarting.main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class FireStartingHandler {

    public final int priority;

    public FireStartingHandler(int priority) {
        this.priority = priority;
    }

    public boolean start(EntityPlayer player, World world, int x, int y, int z, int side) {
        return false;
    }

    public boolean complete(EntityPlayer player, World world, int x, int y, int z, int side) {
        return true;
    }

    public boolean ignite(EntityPlayer player, World world, int x, int y, int z, int side) {
        return false;
    }

    public boolean propagate(EntityPlayer player, World world, int x, int y, int z, int side) {
        return false;
    }

}
