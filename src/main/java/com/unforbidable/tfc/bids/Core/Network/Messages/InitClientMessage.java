package com.unforbidable.tfc.bids.Core.Network.Messages;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.RecipeSetup;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class InitClientMessage implements IMessage {

    private int retry = 0;

    public InitClientMessage() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        retry = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(retry);
    }

    public static class ServerHandler implements IMessageHandler<InitClientMessage, IMessage> {

        @Override
        public IMessage onMessage(InitClientMessage message, MessageContext ctx) {
            return null;
        }
    }

    public static class ClientHandler implements IMessageHandler<InitClientMessage, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(InitClientMessage message, MessageContext ctx) {
            Bids.LOG.info("Client attempts to initialize: " + ++message.retry);

            RecipeSetup.onClientWorldInit();

            return null;
        }
    }

}
