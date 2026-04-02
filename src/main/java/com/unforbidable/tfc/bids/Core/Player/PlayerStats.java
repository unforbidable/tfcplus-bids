package com.unforbidable.tfc.bids.Core.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerStats {

    private long lastWashedTicks;

    private PlayerStats() {
    }

    public long getLastWashedTicks() {
        return lastWashedTicks;
    }

    public void setLastWashedTicks(long lastWashedTicks) {
        this.lastWashedTicks = lastWashedTicks;
    }

    public static PlayerStats load(EntityPlayer player) {
        NBTTagCompound tag = player.getEntityData().getCompoundTag("bids");

        PlayerStats playerStats = new PlayerStats();
        playerStats.setLastWashedTicks(tag.getLong("lastWashedTicks"));
        return playerStats;
    }

    public void save(EntityPlayer player) {
        NBTTagCompound tag = player.getEntityData().getCompoundTag("bids");

        tag.setLong("lastWashedTicks", lastWashedTicks);

        player.getEntityData().setTag("bids", tag);
    }

}
