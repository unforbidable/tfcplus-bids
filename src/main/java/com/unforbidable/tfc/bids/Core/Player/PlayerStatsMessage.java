package com.unforbidable.tfc.bids.Core.Player;

import com.unforbidable.tfc.bids.Bids;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class PlayerStatsMessage implements IMessage {

    public long lastSoapUsageTicks;
    public long lastSoapUsageRewardedTicks;

    public PlayerStatsMessage() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        lastSoapUsageTicks = buf.readLong();
        lastSoapUsageRewardedTicks = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(lastSoapUsageTicks);
        buf.writeLong(lastSoapUsageRewardedTicks);
    }

    public static class ServerHandler implements IMessageHandler<PlayerStatsMessage, IMessage> {

        @Override
        public IMessage onMessage(PlayerStatsMessage message, MessageContext ctx) {
            return null;
        }

    }

    @SuppressWarnings("unchecked")
    public static class ClientHandler implements IMessageHandler<PlayerStatsMessage, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PlayerStatsMessage message, MessageContext ctx) {
            PlayerStats playerStats = PlayerStats.fromMessage(message);
            playerStats.save();

            Bids.LOG.info("Player stats updated");

            return null;
        }

    }

}
