package com.unforbidable.tfc.bids.features.crafting.firestarting.main.network;

import com.unforbidable.tfc.bids.core.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class TinderBurningPacket extends Packet {

    public double xCoord;
    public double yCoord;
    public double zCoord;

    public TinderBurningPacket() {
    }

    public TinderBurningPacket(double xCoord, double yCoord, double zCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(xCoord);
        buf.writeDouble(yCoord);
        buf.writeDouble(zCoord);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        xCoord = buf.readDouble();
        yCoord = buf.readDouble();
        zCoord = buf.readDouble();
    }

}
