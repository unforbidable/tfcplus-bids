package com.unforbidable.tfc.bids.Core.WoodPile;

import java.util.List;
import java.util.UUID;

import com.unforbidable.tfc.bids.Core.Network.TileEntityMessageBase;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class WoodPileMessage extends TileEntityMessageBase {

    private int selectedItemIndex;
    private UUID playerID;

    public WoodPileMessage() {
        super(0, 0, 0, 0);
    }

    public WoodPileMessage(int x, int y, int z, int action) {
        super(x, y, z, action);
    }

    public WoodPileMessage setSelectedItemIndex(int index) {
        selectedItemIndex = index;

        return this;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public WoodPileMessage setPlayer(EntityPlayer player) {
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

        buf.writeInt(selectedItemIndex);

        if (playerID != null) {
            buf.writeLong(playerID.getMostSignificantBits());
            buf.writeLong(playerID.getLeastSignificantBits());
        } else {
            buf.writeLong(0);
            buf.writeLong(0);
        }
    }

    public static class ClientHandler extends ClientHandlerBase<WoodPileMessage> {
    }

    public static class ServerHandler extends ServerHandlerBase<WoodPileMessage> {
    }

}
