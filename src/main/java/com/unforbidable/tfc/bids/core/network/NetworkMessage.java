package com.unforbidable.tfc.bids.core.network;

import com.unforbidable.tfc.bids.core.network.packet.Packet;
import com.unforbidable.tfc.bids.core.network.packet.PacketContext;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

import java.text.MessageFormat;

public class NetworkMessage implements IMessage {

    private int discriminator;
    private Packet packet;

    public NetworkMessage() {
    }

    public NetworkMessage(Packet packet) {
        int discriminator = Network.getPacketTypeDiscriminator(packet.getClass());
        if (discriminator == -1) {
            throw new RuntimeException(MessageFormat.format("Cannot send packet of type {} that has not been registered",
                packet.getClass().getCanonicalName()));
        }

        this.packet = packet;
        this.discriminator = discriminator;
    }

    public Packet getPacket() {
        return packet;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if (packet != null) {
            throw new RuntimeException("Expected packed instance null when receiving a message");
        }

        discriminator = buf.readByte();
        packet = Network.createPacketInstance(discriminator);

        packet.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (packet == null) {
            throw new RuntimeException("Expected packed instance set when sending a message");
        }

        buf.writeByte(discriminator);

        packet.toBytes(buf);
    }

}
