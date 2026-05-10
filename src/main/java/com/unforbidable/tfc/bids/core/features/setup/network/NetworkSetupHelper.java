package com.unforbidable.tfc.bids.core.features.setup.network;

import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.network.packet.Packet;

public class NetworkSetupHelper {

    public NetworkSetupHelper register(Class<? extends Packet> packet) {
        Network.registerPacket(packet);

        return this;
    }

}
