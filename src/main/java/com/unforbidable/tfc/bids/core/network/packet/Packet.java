package com.unforbidable.tfc.bids.core.network.packet;

import io.netty.buffer.ByteBuf;

public abstract class Packet {

    public void fromBytes(ByteBuf buf) {}

    public void toBytes(ByteBuf buf) {}

    @Override
    public String toString() {
        return getClass().getName() + "()";
    }

}
