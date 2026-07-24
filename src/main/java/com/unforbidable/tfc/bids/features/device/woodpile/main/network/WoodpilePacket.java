package com.unforbidable.tfc.bids.features.device.woodpile.main.network;

import com.unforbidable.tfc.bids.core.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class WoodpilePacket extends Packet {

    int action;
    private int selectedItemIndex;
    private UUID playerID;

    public WoodpilePacket() {
    }

    public WoodpilePacket(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public WoodpilePacket setSelectedItemIndex(int index) {
        selectedItemIndex = index;

        return this;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public WoodpilePacket setPlayer(EntityPlayer player) {
        playerID = player.getUniqueID();

        return this;
    }

    @SuppressWarnings("unchecked")
    public EntityPlayer getPlayer() {
        if (playerID != null) {
            for (EntityPlayerMP player : (List<EntityPlayerMP>) MinecraftServer.getServer()
                    .getConfigurationManager().playerEntityList) {
                if (player.getUniqueID().equals(playerID)) {
                    return player;
                }
            }
        }

        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        action = buf.readByte();
        selectedItemIndex = buf.readInt();

        final long mostSignificantBits = buf.readLong();
        final long leastSignificantBits = buf.readLong();
        playerID = mostSignificantBits != 0 || leastSignificantBits != 0
                ? new UUID(mostSignificantBits, leastSignificantBits)
                : null;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeByte(action);
        buf.writeInt(selectedItemIndex);

        if (playerID != null) {
            buf.writeLong(playerID.getMostSignificantBits());
            buf.writeLong(playerID.getLeastSignificantBits());
        } else {
            buf.writeLong(0);
            buf.writeLong(0);
        }
    }

}
