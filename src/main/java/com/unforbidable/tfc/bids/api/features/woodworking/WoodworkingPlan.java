package com.unforbidable.tfc.bids.api.features.woodworking;

import java.awt.geom.Area;

public interface WoodworkingPlan {

    String getName();
    Area getCutout();
    boolean matches(Area cutout);

}
