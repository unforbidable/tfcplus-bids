package com.unforbidable.tfc.bids.core.network._obsolete;

public class NetworkHelper {

    static int nextId = 0;

    public static int getNextAvailableMessageId() {
        return nextId++;
    }

}
