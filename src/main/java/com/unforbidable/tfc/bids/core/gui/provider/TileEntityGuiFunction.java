package com.unforbidable.tfc.bids.core.gui.provider;

@FunctionalInterface
public
interface TileEntityGuiFunction<I, T, W, X, Y, Z, G> {

    G apply(I inventory, T tileEntity, W world, X x, Y y, Z z);

}
