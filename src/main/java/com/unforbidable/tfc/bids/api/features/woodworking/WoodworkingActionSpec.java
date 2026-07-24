package com.unforbidable.tfc.bids.api.features.woodworking;

import java.awt.geom.Area;

public interface WoodworkingActionSpec {

    float getOriginX();
    float getOriginY();
    Area getCutout();
    Area getClearance();
    Area getMargin();

}
