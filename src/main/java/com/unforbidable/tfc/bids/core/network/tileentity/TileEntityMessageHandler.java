package com.unforbidable.tfc.bids.core.network.tileentity;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.network.NetworkUtil;
import com.unforbidable.tfc.bids.core.network.packet.Packet;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityMessageHandler implements IMessageHandler<TileEntityMessage, IMessage> {

    @Override
    public IMessage onMessage(TileEntityMessage message, MessageContext ctx) {
        Bids.LOG.debug("Received tile entity message for location [{},{},{}] on side {}",
            message.x, message.y, message.z, ctx.side);

        World world = NetworkUtil.getWorldFromMessageContext(ctx);
        TileEntity tileEntity = world.getTileEntity(message.x, message.y, message.z);

        handlePacket(message.getPacket(), tileEntity);

        return null;
    }

    private <T extends Packet> void handlePacket(T packet, TileEntity tileEntity) {
        PacketHandler<T> handler = getTileEntityHandler(tileEntity);
        if (handler != null) {
            Bids.LOG.debug("Tile entity {} will receive packet {}", tileEntity.getClass().getCanonicalName(),
                packet.getClass().getCanonicalName());

            handler.handleNetworkPacket(packet);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Packet> PacketHandler<T> getTileEntityHandler(TileEntity tileEntity) {
        try {
            return (PacketHandler<T>) tileEntity;
        } catch (ClassCastException ex) {
            Bids.LOG.warn("Tile entity {} must implement interface {} to receive network packets",
                tileEntity.getClass().getCanonicalName(), PacketHandler.class.getCanonicalName());

            return null;
        }
    }

}
