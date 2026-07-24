package com.unforbidable.tfc.bids.core.network.packet;

public class PacketDiscriminator {

    public final int discriminator;
    public final Class<? extends Packet> type;

    public PacketDiscriminator(int discriminator, Class<? extends Packet> type) {
        this.discriminator = discriminator;
        this.type = type;
    }

}
