package com.unforbidable.tfc.bids.core.gui.provider;

@FunctionalInterface
public
interface SpecialGuiFunction<I, T, W, X, Y, Z, G> {

    G apply(I inventory, T is, W world, X x, Y y, Z z);

}
