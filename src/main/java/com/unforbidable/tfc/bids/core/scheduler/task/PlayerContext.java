package com.unforbidable.tfc.bids.core.scheduler.task;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerContext {

    public final boolean isRemote;
    public final EntityPlayer player;

    public PlayerContext(boolean isRemote, EntityPlayer player) {
        this.isRemote = isRemote;
        this.player = player;
    }

}
