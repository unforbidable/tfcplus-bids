package com.unforbidable.tfc.bids.core.network;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.network.packet.*;
import com.unforbidable.tfc.bids.core.network.tileentity.TileEntityMessage;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import java.text.MessageFormat;
import java.util.function.Consumer;

public class Network {

    private static final ListRegistry<PacketDiscriminator> packets = new ListRegistry<>();
    private static final ListRegistry<PacketConsumer<?>> consumers = new ListRegistry<>();

    private static int nextDiscriminator = 0;

    public static <T extends Packet> void registerPacket(Class<T> type) {
        if (getPacketTypeDiscriminator(type) == -1) {
            Bids.LOG.info("Register network packet {}", type.getCanonicalName());

            int discriminator = nextDiscriminator++;
            packets.add(new PacketDiscriminator(discriminator, type));
        } else {
            Bids.LOG.warn("Network packed type {} has already been registered", type);
        }
    }

    public static <T extends Packet> void handlePacket(Class<T> type, Consumer<PacketConsumable<T>> consumer) {
        consumers.add(new PacketConsumer<>(type, consumer));
    }

    public static <T extends Packet> void sendToClient(T packet, EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            IMessage msg = new NetworkMessage(packet);
            Bids.network.sendTo(msg, (EntityPlayerMP) player);
        } else {
            Bids.LOG.warn("Only server can send a message to the player/client");
        }
    }

    public static <T extends Packet> void sendToServer(T packet) {
        IMessage msg = new NetworkMessage(packet);
        Bids.network.sendToServer(msg);
    }

    public static <T extends Packet> void sendToContainer(T packet) {
        IMessage msg = new NetworkMessage(packet);
        Bids.network.sendToServer(msg);
    }

    public static <T extends Packet> void sendToTileEntity(T packet, TileEntity tileEntity) {
        IMessage msg = new TileEntityMessage(packet, tileEntity);

        if (!tileEntity.getWorldObj().isRemote) {
            Bids.network.sendToAllAround(msg, getTileEntityTargetPoint(tileEntity));
        } else {
            Bids.network.sendToServer(msg);
        }
    }

    static void consumePacket(Packet packet, PacketContext context) {
        consumers.stream()
            .filter(c -> c.type == packet.getClass())
            .filter(c -> !context.handled)
            .forEach(c -> c.consume(packet, context));
    }

    static int getPacketTypeDiscriminator(Class<? extends Packet> type) {
        return packets.stream()
            .filter(d -> d.type == type)
            .findAny()
            .map(d -> d.discriminator)
            .orElse(-1);
    }

    static Packet createPacketInstance(int discriminator) {
        Class<? extends Packet> type = packets.stream()
            .filter(d -> d.discriminator == discriminator)
            .findAny()
            .map(d -> d.type)
            .orElseThrow(() -> new RuntimeException("Cannot create instance of an unknown packet type"));

        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(MessageFormat.format("Failed to create an instance of packet type {0}: {1}",
                type.getCanonicalName(), ex.getMessage()), ex);
        }
    }

    private static NetworkRegistry.TargetPoint getTileEntityTargetPoint(TileEntity tileEntity) {
        return new NetworkRegistry.TargetPoint(tileEntity.getWorldObj().provider.dimensionId,
            tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, 255);
    }

}
