package com.unforbidable.tfc.bids.core.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PacketContext {

    public final World world;
    public final EntityPlayer player;

    public boolean handled;

    public PacketContext(World world, EntityPlayer player) {
        this.world = world;
        this.player = player;
    }

}
