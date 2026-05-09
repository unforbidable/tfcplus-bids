package com.unforbidable.tfc.bids.core.network.packet;

import java.util.function.Consumer;

public class PacketConsumer<T extends Packet> {

    public final Class<T> type;
    private final Consumer<PacketConsumable<T>> consumer;

    public PacketConsumer(Class<T> type, Consumer<PacketConsumable<T>> consumer) {
        this.type = type;
        this.consumer = consumer;
    }

    public void consume(Packet packet, PacketContext context) {
        consumer.accept(new PacketConsumable<>(type.cast(packet), context));
    }

}
