package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.Enums.EnumRegion;
import com.unforbidable.tfc.bids.Core.Crops.*;
import com.unforbidable.tfc.bids.api.BidsCrops;
import com.unforbidable.tfc.bids.api.BidsItems;

public class CropSetup {

    public static void preInit() {
        registerCrops();
    }

    private static void registerCrops() {
        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.SEEBEAT, "seebeet")
            .grows(1, 16, 4, 4, 5, -2)
            .withNutrientUsage(0.25f)
            .generates(new EnumRegion[]{ EnumRegion.EUROPE, EnumRegion.AFRICA, EnumRegion.ASIA },
                150, 3000, 1, 18, EnumCropCoastal.COAST_ONLY)
            .withCommonness(7)
            .giveSkillWildHarvestChance(25)
            .dropsSeed(BidsItems.seedsSeaBeet, 50)
            .dropsOutput(BidsItems.seaBeet, 8f)
            .canBeCultivated(new CropCultivation(BidsItems.seedsBeetroot, 1, 0.05f))
            .renders(new CropRenderer(EnumCropRenderType.CROSSED_SQUARES, 0.40, 0.8, "Sea Beet"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.BEETROOT, "beetroot")
            .grows(1, 24, 4, 3, 5, -5)
            .withNutrientUsage(0.9f)
            .goesDormantInFrost()
            .dropsSeed(BidsItems.seedsBeetroot)
            .dropsOutput(BidsItems.beetroot, 20f)
            .canBeCultivated(new CropCultivation(BidsItems.seedsSugarBeet, 2, 0.1f))
            .renders(new CropRenderer(EnumCropRenderType.CROSSED_SQUARES, 0.45, 1, "Beetroot"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.SUGARBEET, "sugarbeet")
            .grows(1, 28, 4, 3, 5, -5)
            .withNutrientUsage(1f)
            .goesDormantInFrost()
            .dropsSeed(BidsItems.seedsSugarBeet)
            .dropsOutput(BidsItems.sugarBeet, 32f)
            .renders(new CropRenderer(EnumCropRenderType.CROSSED_SQUARES, 0.45, 1, "Sugar Beet"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.WILDBEANS, "wildbeans")
            .grows(1, 18, 7, 4, 5, 0)
            .withNutrientUsage(0.4f)
            .restoresNutrients(5, 0, 5)
            .generates(new EnumRegion[]{ EnumRegion.AFRICA, EnumRegion.ASIA },
                120, 1200, 3, 18, EnumCropCoastal.INLAND_ONLY)
            .dropsSeed(BidsItems.seedsWildBeans)
            .dropsOutput(BidsItems.wildBeans, 10f)
            .canBeCultivated(new CropCultivation(BidsItems.seedsBroadBeans, 1, 0.1f))
            .renders(new CropRenderer(EnumCropRenderType.CROSSED_SQUARES, 0.40, 0.8, "Wild Beans"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.BROADBEANS, "broadbeens")
            .grows(1, 24, 7, 4, 5, -5)
            .withNutrientUsage(0.8f)
            .restoresNutrients(10, 0, 10)
            .requiresPole()
            .goesDormantInFrost()
            .dropsSeed(BidsItems.seedsBroadBeans)
            .dropsOutput(BidsItems.broadBeans, 20f)
            .renders(new CropRenderer(EnumCropRenderType.CROSSED_SQUARES, 0.45, 1, "Broad Beans"))
            .build());
    }

}
