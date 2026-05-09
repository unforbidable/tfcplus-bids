package com.unforbidable.tfc.bids.core.network.packet;

public class PacketConsumable<T extends Packet> {

    public final T packet;
    public final PacketContext context;

    public PacketConsumable(T packet, PacketContext context) {
        this.packet = packet;
        this.context = context;
    }

}
