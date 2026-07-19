package com.unforbidable.tfc.bids.features.material.hide;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.material.hide.eventhandler.HideLivingDropsEventHandler;
import com.unforbidable.tfc.bids.features.material.hide.item.ItemMoreRawhide;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.MORE_HIDE;
import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("hide")
public class Hide extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(MORE_HIDE, ItemMoreRawhide::new);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new HideLivingDropsEventHandler());

        setup.ores("itemNeedleStrung")
            .add(TFCItems.boneNeedleStrung)
            .add(TFCItems.ironNeedleStrung);

        setup.recipes().addShapeless(new ItemStack(TFCItems.hide),
                new ItemStack(BidsItems.moreHide, 1, 0),
                new ItemStack(BidsItems.moreHide, 1, 0), "itemNeedleStrung")
            .action(damageTool("itemNeedleStrung", 10));

        setup.recipes().addShapeless(new ItemStack(TFCItems.hide, 1, 1),
                new ItemStack(TFCItems.hide, 1, 0),
                new ItemStack(TFCItems.hide, 1, 0), "itemNeedleStrung")
            .action(damageTool("itemNeedleStrung", 20));

        setup.recipes().addShapeless(new ItemStack(TFCItems.hide, 1, 2),
                new ItemStack(TFCItems.hide, 1, 1),
                new ItemStack(TFCItems.hide, 1, 1), "itemNeedleStrung")
            .action(damageTool("itemNeedleStrung", 40));

        setup.recipes().addShapeless(new ItemStack(BidsItems.moreHide, 2),
                new ItemStack(TFCItems.hide, 1, 0), "itemKnife")
            .action(damageTool("itemKnife"));
    }

}
