package com.unforbidable.tfc.bids.core.player;

import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.player.network.PlayerStatsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
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

    public static PlayerStats fromPacket(PlayerStatsPacket message) {
        PlayerStats playerStats = new PlayerStats(Minecraft.getMinecraft().thePlayer);
        playerStats.readFromMessage(message);

        return playerStats;
    }

    private void readFromMessage(PlayerStatsPacket message) {
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
        NBTTagCompound tag = entityData.getCompoundTag("bids");

        tag.setLong("lastSoapUsageTicks", lastSoapUsageTicks);
        tag.setLong("lastSoapUsageRewardedTicks", lastSoapUsageRewardedTicks);

        entityData.setTag("bids", tag);
    }

    private void sendUpdate() {
        PlayerStatsPacket packet = new PlayerStatsPacket();
        packet.lastSoapUsageTicks = lastSoapUsageTicks;
        packet.lastSoapUsageRewardedTicks = lastSoapUsageRewardedTicks;

        Network.sendToClient(packet, player);
    }

}
