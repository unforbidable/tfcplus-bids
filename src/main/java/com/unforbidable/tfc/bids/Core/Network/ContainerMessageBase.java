package com.unforbidable.tfc.bids.Core.Network;

import com.unforbidable.tfc.bids.Bids;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;

public abstract class ContainerMessageBase implements IMessage {

    public ContainerMessageBase() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @SuppressWarnings("unchecked")
    public static abstract class ServerHandlerBase<T extends ContainerMessageBase>
            implements IMessageHandler<T, IMessage> {

        @Override
        public IMessage onMessage(T message, MessageContext ctx) {
            Container container = ctx.getServerHandler().playerEntity.openContainer;

            IMessageHandlingContainer<T> typedContainer = null;
            try {
                typedContainer = (IMessageHandlingContainer<T>) container;
            } catch (ClassCastException e) {
                Bids.LOG.warn(String.format(
                        "Message cannot be delivered to container %s because it does not implement interface %s<%s>",
                        container.getClass().getCanonicalName(), IMessageHandlingContainer.class.getCanonicalName(),
                        message.getClass().getCanonicalName()));
            }

            if (typedContainer != null) {
                typedContainer.onContainerMessage(message);
            }

            return null;
        }

    }

    @SuppressWarnings("unchecked")
    public static abstract class ClientHandlerBase<T extends ContainerMessageBase>
            implements IMessageHandler<T, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(T message, MessageContext ctx) {
            Container container = Minecraft.getMinecraft().thePlayer.openContainer;

            IMessageHandlingContainer<T> typedContainer = null;
            try {
                typedContainer = (IMessageHandlingContainer<T>) container;
            } catch (ClassCastException e) {
                Bids.LOG.warn(String.format(
                    "Message cannot be delivered to container %s because it does not implement interface %s<%s>",
                    container.getClass().getCanonicalName(), IMessageHandlingContainer.class.getCanonicalName(),
                    message.getClass().getCanonicalName()));
            }

            if (typedContainer != null) {
                typedContainer.onContainerMessage(message);
            }

            return null;
        }

    }

}
