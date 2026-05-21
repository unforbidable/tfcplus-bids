package com.unforbidable.tfc.bids.features.utility.compositetools;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.utility.compositetools.eventhandler.CompositeToolTooltipHandler;

import static com.unforbidable.tfc.bids.core.crafting.actions.ToolBinding.toolBinding;

@FeatureName("compositeTools")
public class CompositeTools extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(CompositeToolsConfig::load, "crafting");
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("materialBinding")
            .add(TFCItems.cottonYarn, TFCItems.woolYarn, TFCItems.silkString)
            .add(TFCItems.sinew)
            .add(TFCItems.linenString);

        if (CompositeToolsConfig.enableGrassCordageAsToolBinding) {
            setup.ores("materialBinding")
                .add(TFCItems.grassCordage);
        }

        setup.ores("materialBindingDecent")
            .add(TFCItems.sinew)
            .add(TFCItems.linenString);

        // Also used for drying rack construction
        setup.ores("materialBindingStrong")
            .add(TFCItems.linenString);

        setup.ores("materialBowstring")
            .add(TFCItems.linenString);

        setup.recipes()
            .match(r -> r.output.isAny("itemAxeStone", "itemHammerStone", "itemKnifeStone",
                "itemShovelStone", "itemHoeStone", "itemJavelinStone",
                "itemAdzeStone", "itemDrillStone"))
            .edit(r -> r.clone(CompositeToolsConfig.removeOriginalStoneToolRecipes)
                .addInput("materialBinding")
                .action(toolBinding()));

        setup.event()
            .handler(new CompositeToolTooltipHandler());
    }

}
