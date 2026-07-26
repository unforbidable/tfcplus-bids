package com.unforbidable.tfc.bids.core.network;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.network.packet.PacketContext;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class NetworkMessageHandler implements IMessageHandler<NetworkMessage, IMessage> {

    @Override
    public IMessage onMessage(NetworkMessage message, MessageContext ctx) {
        Bids.LOG.debug("Received message type {} on side {}", message.getPacket().getClass(), ctx.side);

        PacketContext context = NetworkUtil.getPackerContextFromMessageContext(ctx);

        // Use consumers to handle packet
        Network.consumePacket(message.getPacket(), context);

        return null;
    }

}
