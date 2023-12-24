package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.Enums.EnumRegion;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.Crops.*;
import com.unforbidable.tfc.bids.api.BidsCrops;
import com.unforbidable.tfc.bids.api.BidsItems;

public class CropSetup {

    public static void preInit() {
        registerCrops();
    }

    private static void registerCrops() {
        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.SEEBEAT, "seebeet")
            .grows(1, 16, 4, 4, 5, 0, 0.25f)
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
            .grows(1, 24, 4, 3, 5, 0, 0.9f)
            .goesDormantInFrost()
            .dropsSeed(BidsItems.seedsBeetroot)
            .dropsOutput(BidsItems.beetroot, 20f)
            .canBeCultivated(new CropCultivation(BidsItems.seedsSugarBeet, 2, 0.05f))
            .renders(new CropRenderer(EnumCropRenderType.CROSSED_SQUARES, 0.45, 1, "Beetroot"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.SUGARBEET, "sugarbeet")
            .grows(1, 28, 4, 3, 5, 0, 1f)
            .goesDormantInFrost()
            .dropsSeed(BidsItems.seedsSugarBeet)
            .dropsOutput(BidsItems.sugarBeet, 32f)
            .renders(new CropRenderer(EnumCropRenderType.CROSSED_SQUARES, 0.45, 1, "Sugar Beet"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.WILDBEANS, "wildbeans")
            .grows(1, 18, 7, 4, 5, 0, 0.4f)
            .generates(new EnumRegion[]{ EnumRegion.AFRICA, EnumRegion.ASIA },
                120, 1200, 3, 18, EnumCropCoastal.INLAND_ONLY)
            .dropsSeed(BidsItems.seedsWildBeans)
            .dropsOutput(BidsItems.wildBeans, 10f)
            .canBeCultivated(new CropCultivation(BidsItems.seedsBroadBeans, 1, 0.1f))
            .renders(new CropRenderer(EnumCropRenderType.CROSSED_SQUARES, 0.40, 0.8, "Wild Beans"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.BROADBEANS, "broadbeens")
            .grows(1, 24, 7, 4, 5, 0, 0.8f)
            .requiresPole()
            .goesDormantInFrost()
            .dropsSeed(BidsItems.seedsBroadBeans)
            .dropsOutput(BidsItems.broadBeans, 16f)
            .renders(new CropRenderer(EnumCropRenderType.CROSSED_SQUARES, 0.45, 1, "Broad Beans"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.BARLEY, "barley")
            .grows(0, 33, 7, 4, 4, 0, 0.85f)
            .dropsSeed(BidsItems.seedsNewBarley)
            .dropsOutput(TFCItems.barleyWhole, 14.0f * 2)
            .canBeCultivated(new CropCultivationTemp(BidsItems.seedsWinterBarley, 1, 0.1f, 10f))
            .renders(new CropRendererTFC(EnumCropRenderType.BLOCK, 0.5, 1, "Barley"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.OAT, "oat")
            .grows(0, 32, 7, 4, 4, 0, 0.9f)
            .dropsSeed(BidsItems.seedsNewOat)
            .dropsOutput(TFCItems.oatWhole, 14.0f * 2)
            .canBeCultivated(new CropCultivationTemp(BidsItems.seedsWinterOat, 1, 0.1f, 10f))
            .renders(new CropRendererTFC(EnumCropRenderType.BLOCK, 0.5, 1, "Oat"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.RYE, "rye")
            .grows(0, 32, 7, 4, 4, 0, 0.9f)
            .dropsSeed(BidsItems.seedsNewRye)
            .dropsOutput(TFCItems.ryeWhole, 14.0f * 2)
            .canBeCultivated(new CropCultivationTemp(BidsItems.seedsWinterRye, 1, 0.1f, 10f))
            .renders(new CropRendererTFC(EnumCropRenderType.BLOCK, 0.5, 1, "Rye"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.WHEAT, "wheat")
            .grows(0, 32, 7, 4, 4, 0, 0.9f)
            .dropsSeed(BidsItems.seedsNewWheat)
            .dropsOutput(TFCItems.wheatWhole, 14.0f * 2)
            .canBeCultivated(new CropCultivationTemp(BidsItems.seedsWinterWheat, 1, 0.1f, 10f))
            .renders(new CropRendererTFC(EnumCropRenderType.BLOCK, 0.5, 1, "Wheat"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.WINTERBARLEY, "winterbarley")
            .grows(0, 33, 7, 4, 4, 0, 0.85f)
            .goesDormantInFrost()
            .dropsSeed(BidsItems.seedsWinterBarley)
            .dropsOutput(TFCItems.barleyWhole, 14.0f * 2)
            .renders(new CropRendererTFC(EnumCropRenderType.BLOCK, 0.5, 1, "Barley"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.WINTEROAT, "winteroat")
            .grows(0, 32, 7, 4, 4, 0, 0.9f)
            .goesDormantInFrost()
            .dropsSeed(BidsItems.seedsWinterOat)
            .dropsOutput(TFCItems.oatWhole, 14.0f * 2)
            .renders(new CropRendererTFC(EnumCropRenderType.BLOCK, 0.5, 1, "Oat"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.WINTERRYE, "winterrye")
            .grows(0, 32, 7, 4, 4, 0, 0.9f)
            .goesDormantInFrost()
            .dropsSeed(BidsItems.seedsWinterRye)
            .dropsOutput(TFCItems.ryeWhole, 14.0f * 2)
            .renders(new CropRendererTFC(EnumCropRenderType.BLOCK, 0.5, 1, "Rye"))
            .build());

        BidsCropManager.registerCrop(BidsCropIndex.builder(BidsCrops.WINTERWHEAT, "winterwheat")
            .grows(0, 32, 7, 4, 4, 0, 0.9f)
            .goesDormantInFrost()
            .dropsSeed(BidsItems.seedsWinterWheat)
            .dropsOutput(TFCItems.wheatWhole, 14.0f * 2)
            .renders(new CropRendererTFC(EnumCropRenderType.BLOCK, 0.5, 1, "Wheat"))
            .build());
    }

}
