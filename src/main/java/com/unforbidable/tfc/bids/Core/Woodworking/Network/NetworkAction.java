package com.unforbidable.tfc.bids.Core.Woodworking.Network;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;

public class NetworkAction {

    public String name;
    public int x;
    public int y;

    public NetworkAction() {
    }

    public NetworkAction(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void fromBytes(ByteBuf buf) {
        name = ByteBufUtils.readUTF8String(buf);
        x = buf.readByte();
        y = buf.readByte();
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, name);
        buf.writeByte(x);
        buf.writeByte(y);
    }

}
