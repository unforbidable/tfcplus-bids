package com.unforbidable.tfc.bids.Core.Player;

import com.unforbidable.tfc.bids.Bids;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerStats {

    private final EntityPlayer player;

    public long lastSoapUsageTicks;
    public long lastSoapUsageRewardedTicks;

    private PlayerStats(EntityPlayer player) {
        this.player = player;
    }

    public static PlayerStats of(EntityPlayer player) {
        PlayerStats playerStats = new PlayerStats(player);
        playerStats.readFromNBT(player.getEntityData());

        return playerStats;
    }

    public static PlayerStats fromMessage(PlayerStatsMessage message) {
        PlayerStats playerStats = new PlayerStats(Minecraft.getMinecraft().thePlayer);
        playerStats.readFromMessage(message);

        return playerStats;
    }

    private void readFromMessage(PlayerStatsMessage message) {
        lastSoapUsageTicks = message.lastSoapUsageTicks;
        lastSoapUsageRewardedTicks = message.lastSoapUsageRewardedTicks;
    }

    private void readFromNBT(NBTTagCompound entityData) {
        NBTTagCompound tag = entityData.getCompoundTag("bids");

        lastSoapUsageTicks = tag.getLong("lastSoapUsageTicks");
        lastSoapUsageRewardedTicks = tag.getLong("lastSoapUsageRewardedTicks");
    }

    public void save() {
        save(false);
    }

    public void save(boolean sendUpdate) {
        writeToNBT(player.getEntityData());

        if (!player.worldObj.isRemote && sendUpdate) {
            sendUpdate();
        }
    }

    private void writeToNBT(NBTTagCompound entityData) {
        NBTTagCompound tag = player.getEntityData().getCompoundTag("bids");

        tag.setLong("lastSoapUsageTicks", lastSoapUsageTicks);
        tag.setLong("lastSoapUsageRewardedTicks", lastSoapUsageRewardedTicks);

        entityData.setTag("bids", tag);
    }

    private void sendUpdate() {
        PlayerStatsMessage message = new PlayerStatsMessage();
        message.lastSoapUsageTicks = lastSoapUsageTicks;
        message.lastSoapUsageRewardedTicks = lastSoapUsageRewardedTicks;

        Bids.network.sendTo(message, (EntityPlayerMP) player);
    }

}
