package com.unforbidable.tfc.bids.Core.Network;

public class NetworkHelper {

    static int nextId = 0;

    public static int getNextAvailableMessageId() {
        return nextId++;
    }

}
