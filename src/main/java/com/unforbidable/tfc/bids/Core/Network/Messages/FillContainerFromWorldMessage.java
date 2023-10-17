package com.unforbidable.tfc.bids.Core.Network.Messages;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Drinks.FluidHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.List;
import java.util.UUID;

public class FillContainerFromWorldMessage implements IMessage {

    private int blockX;
    private int blockY;
    private int blockZ;
    private int sideHit;

    private UUID playerUUID;
    private int entityId;
    private ItemStack containerItemStack;

    public FillContainerFromWorldMessage() {
    }

    public FillContainerFromWorldMessage(ItemStack itemStack, EntityPlayer player, MovingObjectPosition mop) {
        this.containerItemStack = itemStack;
        this.playerUUID = player.getUniqueID();

        this.blockX = mop.blockX;
        this.blockY = mop.blockY;
        this.blockZ = mop.blockZ;
        this.sideHit = mop.sideHit;
        this.entityId = mop.entityHit != null ? mop.entityHit.getEntityId() : 0;
    }

    public ItemStack getContainerItemStack() {
        return containerItemStack;
    }

    @SuppressWarnings("unchecked")
    public EntityPlayer getPlayer() {
        if (playerUUID != null) {
            for (EntityPlayerMP player : (List<EntityPlayerMP>) MinecraftServer.getServer()
                    .getConfigurationManager().playerEntityList) {
                if (player.getUniqueID().equals(playerUUID)) {
                    return player;
                }
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public MovingObjectPosition getMovingObjectPosition() {
        if (entityId != 0) {
            Entity entity = getPlayer().worldObj.getEntityByID(entityId);
            if (entity != null) {
                return new MovingObjectPosition(entity);
            }

            Bids.LOG.warn("Unable to find entity UUID: " + entityId);

            return null;
        } else {
            return new MovingObjectPosition(blockX, blockY, blockZ, sideHit, Vec3.createVectorHelper(0, 0, 0), true);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockX = buf.readInt();
        blockY = buf.readInt();
        blockZ = buf.readInt();
        sideHit = buf.readInt();
        entityId = buf.readInt();

        {
            final long mostSignificantBits = buf.readLong();
            final long leastSignificantBits = buf.readLong();
            playerUUID = mostSignificantBits != 0 || leastSignificantBits != 0
                    ? new UUID(mostSignificantBits, leastSignificantBits)
                    : null;
        }

        containerItemStack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(blockX);
        buf.writeInt(blockY);
        buf.writeInt(blockZ);
        buf.writeInt(sideHit);
        buf.writeInt(entityId);

        if (playerUUID != null) {
            buf.writeLong(playerUUID.getMostSignificantBits());
            buf.writeLong(playerUUID.getLeastSignificantBits());
        } else {
            buf.writeLong(0);
            buf.writeLong(0);
        }

        ByteBufUtils.writeItemStack(buf, containerItemStack);
    }

    public static class ServerHandler implements IMessageHandler<FillContainerFromWorldMessage, IMessage> {

        @Override
        public IMessage onMessage(FillContainerFromWorldMessage message, MessageContext ctx) {
            Bids.LOG.info("Received FillContainerFromMobMessage");
            FluidHelper.onContainerFilledFromWorld(message.containerItemStack, message.getPlayer(), message.getMovingObjectPosition());

            return null;
        }
    }

    public static class ClientHandler implements IMessageHandler<FillContainerFromWorldMessage, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(FillContainerFromWorldMessage message, MessageContext ctx) {
            return null;
        }
    }

}
