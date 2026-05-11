package com.unforbidable.tfc.bids.core.features.client.eventhandler;

import java.util.ArrayList;
import java.util.List;

public class EventHandlerClientSpecCollector {

    private List<EventHandlerClientSpec> handlers = new ArrayList<>();

    public EventHandlerClientSpecCollector handler(Object handler) {
        handlers.add(new EventHandlerClientSpec(handler));

        return this;
    }

    public List<EventHandlerClientSpec> build() {
        return handlers;
    }

}
