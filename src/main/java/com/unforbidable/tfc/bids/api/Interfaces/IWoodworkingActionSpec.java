package com.unforbidable.tfc.bids.api.Interfaces;

public interface IWoodworkingActionSpec {

    float getOriginX();
    float getOriginY();
    IWoodworkingShape[] getCutoutShapes();
    IWoodworkingShape[] getClearanceShapes();
    IWoodworkingShape[] getMarginShapes();

}
