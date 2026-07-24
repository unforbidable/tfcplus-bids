package com.unforbidable.tfc.bids.core.features.setup.eventhandler;

import java.util.ArrayList;
import java.util.List;

public class EventHandlerSpecCollector {

    private final List<EventHandlerSpec> handlers = new ArrayList<>();

    public EventHandlerSpecCollector handler(Object handler) {
        handlers.add(new EventHandlerSpec(handler));

        return this;
    }

    public List<EventHandlerSpec> build() {
        return handlers;
    }

}
