package com.unforbidable.tfc.bids.features.utility.hidevessel;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackRecipe;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.dryingrack.DryingRackRegistry;
import com.unforbidable.tfc.bids.features.utility.hidevessel.eventhandler.HideVesselEventHandler;
import com.unforbidable.tfc.bids.features.utility.hidevessel.item.ItemHideVessel;
import com.unforbidable.tfc.bids.features.utility.hidevessel.item.ItemHideVesselFluid;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.HideVesselHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@FeatureName("hideVessel")
public class HideVessel extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.HIDE_VESSEL, ItemHideVessel::new);
        init.item(ItemNames.HIDE_VESSEL_FRESH_WATER, ItemHideVesselFluid::new);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        for (int i = 0; i < HideVesselHelper.HIDE_VESSEL_MAX_DAMAGE; i++) {
            setup.fluid(TFCFluids.FRESHWATER)
                .container(BidsItems.hideVesselFreshWater, i, HideVesselHelper.HIDE_VESSEL_CAPACITY, false, BidsItems.hideVessel, i);
        }

        setup.event()
            .handler(new HideVesselEventHandler());

        setup.registry(DryingRackRegistry.recipes)
            .add((DryingRackRecipe) DryingRackRecipe.builder()
                .consumes(new ItemStack(BidsItems.hideVessel, 1, OreDictionary.WILDCARD_VALUE))
                .produces(new ItemStack(TFCItems.hideBag))
                .dry()
                .hours(2)
                .build());

        setup.ores("itemHideBagUndamaged")
            .add(new ItemStack(BidsItems.hideVessel, 1, 0));

        for (int i = 1; i <= TFCItems.hideBag.getMaxDamage(); i++) {
            setup.ores("itemHideBagDamaged")
                .add(new ItemStack(BidsItems.hideVessel, 1, i));
        }
    }

}
