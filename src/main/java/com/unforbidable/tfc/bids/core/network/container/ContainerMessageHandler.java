package com.unforbidable.tfc.bids.core.network.container;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.network.NetworkUtil;
import com.unforbidable.tfc.bids.core.network.packet.Packet;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerMessageHandler implements IMessageHandler<ContainerMessage, IMessage> {

    @Override
    public IMessage onMessage(ContainerMessage message, MessageContext ctx) {
        Bids.LOG.debug("Received container message id {} on side {}", message.id, ctx.side);

        EntityPlayer player = NetworkUtil.getPlayerFromMessageContext(ctx);
        Container container = player.openContainer;

        if (container.windowId == message.id) {
            handlePacket(message.getPacket(), container);
        } else {
            Bids.LOG.warn("Open container ID {} does not match message ID {}",
                container.windowId, message.id);
        }

        return null;
    }

    private <T extends Packet> void handlePacket(T packet, Container container) {
        PacketHandler<T> handler = getContainerHandler(container);
        if (handler != null) {
            Bids.LOG.debug("Open container {} will receive packet {}", container.getClass().getCanonicalName(),
                packet.getClass().getCanonicalName());

            handler.handleNetworkPacket(packet);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Packet> PacketHandler<T> getContainerHandler(Container container) {
        try {
            return (PacketHandler<T>) container;
        } catch (ClassCastException ex) {
            Bids.LOG.warn("Open container {} must implement interface {} to receive network packets",
                container.getClass().getCanonicalName(), PacketHandler.class.getCanonicalName());

            return null;
        }
    }

}
