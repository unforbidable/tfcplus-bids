package com.unforbidable.tfc.bids.Core.Network;

public interface IMessageHandlingContainer<T extends ContainerMessageBase> {

    void onContainerMessage(T message);

}
