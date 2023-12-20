package com.unforbidable.tfc.bids.Core.Crops;

import com.dunk.tfc.api.Enums.EnumRegion;
import net.minecraft.item.Item;

import java.util.ArrayList;

public class CropBuilder {

    private final BidsCropIndex cropIndex;

    public CropBuilder(BidsCropIndex cropIndex) {
        this.cropIndex = cropIndex;
    }

    public CropBuilder grows(int cycleType, int growthTime, int numGrowthStages, int minWaterDistance, float minGrowthTemp, float minAliveTemp, float nutrientUsageMult) {
        cropIndex.cycleType = cycleType;
        cropIndex.growthTime = growthTime;
        cropIndex.numGrowthStages = numGrowthStages;
        cropIndex.minWaterDistance = minWaterDistance;
        cropIndex.minGrowthTemp = minGrowthTemp;
        cropIndex.minAliveTemp = minAliveTemp;
        cropIndex.nutrientUsageMult = nutrientUsageMult;

        return this;
    }

    public CropBuilder goesDormantInFrost() {
        cropIndex.dormantInFrost = true;

        return this;
    }

    public CropBuilder requiresPole() {
        cropIndex.requiresPole = true;

        return this;
    }

    public CropBuilder dropsSeed(Item seedItem) {
        cropIndex.seedItem = seedItem;

        return this;
    }

    public CropBuilder dropsSeed(Item seedItem, int chance) {
        cropIndex.seedItem = seedItem;
        cropIndex.giveSeedChanceWild = chance;

        return this;
    }

    public CropBuilder giveSkillWildHarvestChance(int chance) {
        cropIndex.giveSkillChanceWild = chance;

        return this;
    }

    public CropBuilder dropsOutput(Item output1, float output1Avg) {
        cropIndex.output1 = output1;
        cropIndex.output1Avg = output1Avg;

        return this;
    }

    public CropBuilder generates(EnumRegion[] regions, int minimumNaturalRain, int maximumNaturalRain, int minimumNaturalBioTemp, int maximumNaturalBioTemp) {
        cropIndex.regions = regions;
        cropIndex.minimumNaturalRain = minimumNaturalRain;
        cropIndex.maximumNaturalRain = maximumNaturalRain;
        cropIndex.minimumNaturalBioTemp = minimumNaturalBioTemp;
        cropIndex.maximumNaturalBioTemp = maximumNaturalBioTemp;

        return this;
    }

    public CropBuilder generates(EnumRegion[] regions, int minimumNaturalRain, int maximumNaturalRain, int minimumNaturalBioTemp, int maximumNaturalBioTemp, EnumCropCoastal coastal) {
        generates(regions, minimumNaturalRain, maximumNaturalRain, minimumNaturalBioTemp, maximumNaturalBioTemp);

        cropIndex.coastal = coastal;

        return this;
    }

    public CropBuilder withCommonness(int commonness) {
        cropIndex.commonness = commonness;

        return this;
    }

    public CropBuilder renders(CropRenderer cropRenderer) {
        cropIndex.cropRenderer = cropRenderer;

        return this;
    }

    public CropBuilder canBeCultivated(CropCultivation cropCultivation) {
        if (cropIndex.cropCultivation == null) {
            cropIndex.cropCultivation = new ArrayList<CropCultivation>();
        }

        cropIndex.cropCultivation.add(cropCultivation);

        return this;
    }

    public BidsCropIndex build() {
        return cropIndex;
    }

}
