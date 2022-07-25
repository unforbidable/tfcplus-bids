package com.unforbidable.tfc.bids.Core.Network;

import com.unforbidable.tfc.bids.Bids;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TileEntityMessageBase implements IMessage {

    private int xCoord;
    private int yCoord;
    private int zCoord;

    private int action;

    public TileEntityMessageBase(int x, int y, int z, int action) {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public int getZCoord() {
        return zCoord;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        xCoord = buf.readInt();
        yCoord = buf.readInt();
        zCoord = buf.readInt();

        action = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(xCoord);
        buf.writeInt(yCoord);
        buf.writeInt(zCoord);

        buf.writeByte(action);
    }

    @SuppressWarnings("unchecked")
    public static abstract class ServerHandlerBase<T extends TileEntityMessageBase>
            implements IMessageHandler<T, IMessage> {

        @Override
        public IMessage onMessage(T message, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            TileEntity te = world.getTileEntity(message.getXCoord(), message.getYCoord(), message.getZCoord());

            IMessageHanldingTileEntity<T> tem = null;
            try {
                tem = (IMessageHanldingTileEntity<T>) te;
            } catch (ClassCastException e) {
                Bids.LOG.warn(String.format(
                        "Message cannot be delivered to tile entity %s because it does not implement interface %s<%s>",
                        te.getClass().getCanonicalName(), IMessageHanldingTileEntity.class.getCanonicalName(),
                        message.getClass().getCanonicalName()));
            }

            if (tem != null) {
                tem.onTileEntityMessage(message);
            }

            return null;
        }

    }

    @SuppressWarnings("unchecked")
    public static abstract class ClientHandlerBase<T extends TileEntityMessageBase>
            implements IMessageHandler<T, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(T message, MessageContext ctx) {
            World world = Minecraft.getMinecraft().theWorld;
            TileEntity te = world.getTileEntity(message.getXCoord(), message.getYCoord(), message.getZCoord());

            IMessageHanldingTileEntity<T> tem = null;
            try {
                tem = (IMessageHanldingTileEntity<T>) te;
            } catch (ClassCastException e) {
                Bids.LOG.warn(String.format(
                        "Message cannot be delivered to tile entity %s because it does not implement interface %s<%s>",
                        te.getClass().getCanonicalName(), IMessageHanldingTileEntity.class.getCanonicalName(),
                        message.getClass().getCanonicalName()));
            }

            if (tem != null) {
                tem.onTileEntityMessage(message);
            }

            return null;
        }

    }

}
