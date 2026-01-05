package com.unforbidable.tfc.bids.api.Interfaces;

public interface IWoodworkingActionSpec {

    int getOriginX();
    int getOriginY();
    IWoodworkingShape[] getCutoutShapes();
    IWoodworkingShape[] getClearanceShapes();
    IWoodworkingShape[] getMarginShapes();

}
