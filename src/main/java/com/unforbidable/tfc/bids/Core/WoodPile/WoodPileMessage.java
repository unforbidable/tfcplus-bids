package com.unforbidable.tfc.bids.Core.WoodPile;

import com.unforbidable.tfc.bids.Core.Network.TileEntityMessageBase;

public class WoodPileMessage extends TileEntityMessageBase {

    public WoodPileMessage() {
        super(0, 0, 0, 0);
    }

    public WoodPileMessage(int x, int y, int z, int action) {
        super(x, y, z, action);
    }

    public static class ClientHandler extends ClientHandlerBase<WoodPileMessage> {
    }

    public static class ServerHandler extends ServerHandlerBase<WoodPileMessage> {
    }

}
