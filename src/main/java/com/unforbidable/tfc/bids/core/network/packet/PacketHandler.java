package com.unforbidable.tfc.bids.core.network.packet;

public interface PacketHandler<T extends Packet> {

    void handleNetworkPacket(T packet);

}
