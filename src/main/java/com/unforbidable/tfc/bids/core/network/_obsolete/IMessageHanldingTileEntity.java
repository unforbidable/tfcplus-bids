package com.unforbidable.tfc.bids.core.network._obsolete;

public interface IMessageHanldingTileEntity<T extends TileEntityMessageBase> {

    void onTileEntityMessage(T message);

}
