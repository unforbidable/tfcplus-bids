package com.unforbidable.tfc.bids.features.utility.hidevessel;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackRecipe;
import com.unforbidable.tfc.bids.api.meta.HideVesselMeta;
import com.unforbidable.tfc.bids.api.meta.MoreHideMeta;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.dryingrack.DryingRackRegistry;
import com.unforbidable.tfc.bids.features.utility.hidevessel.eventhandler.HideVesselEventHandler;
import com.unforbidable.tfc.bids.features.utility.hidevessel.item.ItemHideVessel;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.HideVesselHelper;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("hideVessel")
public class HideVessel extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.HIDE_VESSEL, ItemHideVessel::new)
            .apply(i -> i.setMaxDamage(HideVesselHelper.HIDE_VESSEL_CAPACITY));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new HideVesselEventHandler());

        setup.registry(DryingRackRegistry.recipes)
            .add((DryingRackRecipe) DryingRackRecipe.builder()
                .consumes(new ItemStack(BidsItems.hideVessel, 1, HideVesselMeta.EMPTY))
                .produces(new ItemStack(TFCItems.hideBag))
                .dry()
                .hours(2)
                .build());

        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.moreHide, 16, MoreHideMeta.RUINED_HIDE_SCRAP),
                new ItemStack(BidsItems.hideVessel, 1, HideVesselMeta.RUINED), "itemKnife")
            .action(damageTool("itemKnife"));

        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.moreHide, 16, MoreHideMeta.RUINED_HIDE_SCRAP),
                new ItemStack(BidsItems.hideVessel, 1, HideVesselMeta.EMPTY), "itemKnife")
            .action(damageTool("itemKnife"));
    }

}
