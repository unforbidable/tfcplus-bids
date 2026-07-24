package com.unforbidable.tfc.bids.core.network.container;

import com.unforbidable.tfc.bids.core.network.NetworkMessage;
import com.unforbidable.tfc.bids.core.network.packet.Packet;
import io.netty.buffer.ByteBuf;

public class ContainerMessage extends NetworkMessage {

    int id;

    public ContainerMessage() {
    }

    public ContainerMessage(Packet packet, int id) {
        super(packet);

        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        id = buf.readShort();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeShort(id);
    }

}
