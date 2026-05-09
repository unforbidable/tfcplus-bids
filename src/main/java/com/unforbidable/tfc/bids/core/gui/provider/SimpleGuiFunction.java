package com.unforbidable.tfc.bids.core.gui.provider;

@FunctionalInterface
public
interface SimpleGuiFunction<I, W, X, Y, Z, G> {

    G apply(I inventory, W world, X x, Y y, Z z);

}
