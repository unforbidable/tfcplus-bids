package com.unforbidable.tfc.bids.Core.Woodworking.Network;

import com.unforbidable.tfc.bids.Core.Network.ContainerMessageBase;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public class WoodworkingMessage extends ContainerMessageBase {

    public static final int EVENT_PERFORM_ACTION = 1;
    public static final int EVENT_RESET_WORKSPACE = 2;

    private int event;
    private int damage;

    private final List<NetworkAction> actions = new ArrayList<NetworkAction>();

    public WoodworkingMessage() {
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public List<NetworkAction> getActions() {
        return actions;
    }

    public void addAction(NetworkAction networkAction) {
        actions.add(networkAction);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        event = buf.readByte();
        if (event == EVENT_PERFORM_ACTION) {
            damage = buf.readByte();

            byte nActions = buf.readByte();
            for (int i = 0; i < nActions; i++) {
                NetworkAction action = new NetworkAction();
                action.fromBytes(buf);
                actions.add(action);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        buf.writeByte(event);

        if (event == EVENT_PERFORM_ACTION) {
            buf.writeByte(damage);

            buf.writeByte(actions.size());
            for (NetworkAction action : actions) {
                action.toBytes(buf);
            }
        }
    }

    public static class ClientHandler extends ClientHandlerBase<WoodworkingMessage> {
    }

    public static class ServerHandler extends ServerHandlerBase<WoodworkingMessage> {
    }

}
