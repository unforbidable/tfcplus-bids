package com.unforbidable.tfc.bids.Core.DryingRack;

import com.unforbidable.tfc.bids.Core.Network.TileEntityMessageBase;

public class DryingRackMessage extends TileEntityMessageBase {

    public DryingRackMessage() {
        super(0, 0, 0, 0);
    }

    public DryingRackMessage(int x, int y, int z, int action) {
        super(x, y, z, action);
    }

    public static class ClientHandler extends ClientHandlerBase<DryingRackMessage> {
    }

    public static class ServerHandler extends ServerHandlerBase<DryingRackMessage> {
    }

}
