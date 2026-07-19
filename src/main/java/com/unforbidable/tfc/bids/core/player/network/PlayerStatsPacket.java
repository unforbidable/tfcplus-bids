package com.unforbidable.tfc.bids.core.player.network;

import com.unforbidable.tfc.bids.core.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class PlayerStatsPacket extends Packet {

    public long lastSoapUsageTicks;
    public long lastSoapUsageRewardedTicks;

    public PlayerStatsPacket() {
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

}
