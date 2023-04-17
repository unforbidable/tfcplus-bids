package com.unforbidable.tfc.bids.Core.Network.Messages;

import com.unforbidable.tfc.bids.Core.Network.TileEntityMessageBase;

public class TileEntityUpdateMessage extends TileEntityMessageBase {

    public TileEntityUpdateMessage() {
        super(0, 0, 0, 0);
    }

    public TileEntityUpdateMessage(int x, int y, int z, int action) {
        super(x, y, z, 0);
    }

    public static class ClientHandler extends TileEntityMessageBase.ClientHandlerBase<TileEntityUpdateMessage> {
    }

    public static class ServerHandler extends TileEntityMessageBase.ServerHandlerBase<TileEntityUpdateMessage> {
    }
}
