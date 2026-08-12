package com.unforbidable.tfc.bids.features.resource.straw;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.resource.straw.eventhandler.StrawEventHandler;

@FeatureName("straw")
public class Straw extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(StrawConfig::load, "harvest");
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new StrawEventHandler());

        setup.run(() -> {
            TFCItems.stoneFlake.setHarvestLevel("knife", 1);

            TFCItems.stoneKnife.setHarvestLevel("knife", 1);
            TFCItems.copperKnife.setHarvestLevel("knife", 1);
            TFCItems.bronzeKnife.setHarvestLevel("knife", 2);
            TFCItems.blackBronzeKnife.setHarvestLevel("knife", 2);
            TFCItems.bismuthBronzeKnife.setHarvestLevel("knife", 2);
            TFCItems.wroughtIronKnife.setHarvestLevel("knife", 3);
            TFCItems.steelKnife.setHarvestLevel("knife", 4);
            TFCItems.blackSteelKnife.setHarvestLevel("knife", 5);
            TFCItems.blueSteelKnife.setHarvestLevel("knife", 6);
            TFCItems.redSteelKnife.setHarvestLevel("knife", 6);

            TFCItems.copperScythe.setHarvestLevel("knife", 1);
            TFCItems.bronzeScythe.setHarvestLevel("knife", 2);
            TFCItems.bismuthBronzeScythe.setHarvestLevel("knife", 2);
            TFCItems.blackBronzeScythe.setHarvestLevel("knife", 2);
            TFCItems.wroughtIronScythe.setHarvestLevel("knife", 3);
            TFCItems.steelScythe.setHarvestLevel("knife", 4);
            TFCItems.blackSteelScythe.setHarvestLevel("knife", 5);
            TFCItems.blueSteelScythe.setHarvestLevel("knife", 6);
            TFCItems.redSteelScythe.setHarvestLevel("knife", 6);

            TFCBlocks.tallGrass.setHarvestLevel("knife", 0);
            TFCBlocks.crops.setHarvestLevel("knife", 0);

            TFCBlocks.leafLitter.setHarvestLevel("axe", 0);

            if (StrawConfig.tallGrassBlockHardness > 0) {
                TFCBlocks.tallGrass.setHardness(StrawConfig.tallGrassBlockHardness);
            }

            if (StrawConfig.cropBlockHardness > 0) {
                TFCBlocks.crops.setHardness(StrawConfig.cropBlockHardness);
            }
        });
    }

}
