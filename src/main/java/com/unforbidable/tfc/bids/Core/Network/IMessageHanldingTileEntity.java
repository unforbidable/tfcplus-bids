package com.unforbidable.tfc.bids.Core.Network;

public interface IMessageHanldingTileEntity<T extends TileEntityMessageBase> {

    void onTileEntityMessage(T message);

}
