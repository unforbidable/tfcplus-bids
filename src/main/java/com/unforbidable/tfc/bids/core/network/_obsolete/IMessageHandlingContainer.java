package com.unforbidable.tfc.bids.core.network._obsolete;

public interface IMessageHandlingContainer<T extends ContainerMessageBase> {

    void onContainerMessage(T message);

}
