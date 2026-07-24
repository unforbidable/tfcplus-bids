package com.unforbidable.tfc.bids.api.features.woodworking;

public interface WoodworkingAction {

    String getName();
    WoodworkingActionSpec getSpec();
    WoodworkingActionSide getSide();

}
