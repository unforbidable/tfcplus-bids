package com.unforbidable.tfc.bids.Core.Crops;

import com.dunk.tfc.Food.CropManager;
import com.dunk.tfc.api.Enums.EnumRegion;

import java.util.*;

public class BidsCropManager {

    private static final List<BidsCropIndex> crops = new ArrayList<BidsCropIndex>();
    private static final Map<Integer, CropRegion> regionalCrops = new HashMap<Integer, CropRegion>();

    private static final Map<Integer, BidsCropIndex> cropsById = new HashMap<Integer, BidsCropIndex>();
    private static final Map<String, BidsCropIndex> cropsByName = new HashMap<String, BidsCropIndex>();

    static {
        for (EnumRegion region : EnumRegion.values()) {
            regionalCrops.put(region.ordinal(), new CropRegion());
        }
    }

    public static void registerCrop(BidsCropIndex cropIndex) {
        if (cropsById.containsKey(cropIndex.cropId)) {
            throw new IllegalArgumentException("Crop ID already used to register crop " + cropsById.get(cropIndex.cropId).cropName);
        }
        if (cropsByName.containsKey(cropIndex.cropName)) {
            throw new IllegalArgumentException("Crop name already used to register crop ID " + cropsByName.get(cropIndex.cropName).cropId);
        }

        crops.add(cropIndex);
        cropsById.put(cropIndex.cropId, cropIndex);
        cropsByName.put(cropIndex.cropName, cropIndex);

        if (cropIndex.regions != null && cropIndex.commonness > 0) {
            List<BidsCropIndex> weighted = new ArrayList<BidsCropIndex>();
            for (int i = 0; i < cropIndex.commonness; i++) {
                weighted.add(cropIndex);
            }

            List<EnumRegion> cropRegions = Arrays.asList(cropIndex.regions);

            for (EnumRegion region : EnumRegion.values()) {
                if (cropRegions.contains(region)) {
                    if (cropIndex.coastal == EnumCropCoastal.COAST_ONLY) {
                        regionalCrops.get(region.ordinal()).getCoastalCrops().addAll(weighted);
                    } else if (cropIndex.coastal == EnumCropCoastal.INLAND_ONLY) {
                        regionalCrops.get(region.ordinal()).getInlandCrops().addAll(weighted);
                    } else {
                        regionalCrops.get(region.ordinal()).getCoastalCrops().addAll(weighted);
                        regionalCrops.get(region.ordinal()).getInlandCrops().addAll(weighted);
                    }
                }
            }
        }

        // Registering crop indices will not affect crop generation by TFC
        // because they are not added to the data for regions
        // which is intentional
        // This still allows the new crop to be known to TFC in other places
        CropManager.getInstance().addIndex(cropIndex);
    }

    public static List<BidsCropIndex> getCrops() {
        return crops;
    }

    public static List<BidsCropIndex> getValidCrops(int region, boolean coast) {
        if (coast) {
            return regionalCrops.get(region).getCoastalCrops();
        } else {
            return regionalCrops.get(region).getInlandCrops();
        }
    }

    public static BidsCropIndex findCropById(int id) {
        return cropsById.get(id);
    }

    public static BidsCropIndex findCropByName(String name) {
        return cropsByName.get(name);
    }

}
