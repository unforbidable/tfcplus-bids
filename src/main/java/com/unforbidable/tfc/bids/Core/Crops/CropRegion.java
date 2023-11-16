package com.unforbidable.tfc.bids.Core.Crops;

import java.util.ArrayList;
import java.util.List;

public class CropRegion {

    private final List<BidsCropIndex> inlandCrops = new ArrayList<BidsCropIndex>();
    private final List<BidsCropIndex> coastalCrops = new ArrayList<BidsCropIndex>();

    public List<BidsCropIndex> getCoastalCrops() {
        return coastalCrops;
    }

    public List<BidsCropIndex> getInlandCrops() {
        return inlandCrops;
    }

}
