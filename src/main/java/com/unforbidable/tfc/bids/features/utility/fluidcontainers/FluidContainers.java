package com.unforbidable.tfc.bids.features.utility.fluidcontainers;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemBowlFluid;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemGlassBottleFluid;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemPotteryFluid;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;

import static com.unforbidable.tfc.bids.api.names.ItemNames.BOTTLE_BRINE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BOTTLE_HONEY;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BOTTLE_OLIVE_OIL;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BOTTLE_SALT_WATER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BOTTLE_VINEGAR;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BOWL_OLIVE_OIL;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BOWL_VINEGAR;
import static com.unforbidable.tfc.bids.api.names.ItemNames.JUG_OLIVE_OIL;
import static com.unforbidable.tfc.bids.api.names.ItemNames.JUG_VINEGAR;

/**
 * <li>various additional TFC containers for TFC fluids</li>
 */
@FeatureName("fluidContainers")
public class FluidContainers extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(BOTTLE_OLIVE_OIL, ItemGlassBottleFluid::new)
            .container(TFCItems.glassBottle)
            .fluid(1000, TFCFluids.OLIVEOIL, true);
        init.item(BOTTLE_VINEGAR, ItemGlassBottleFluid::new)
            .container(TFCItems.glassBottle)
            .fluid(1000, TFCFluids.VINEGAR, true);
        init.item(BOTTLE_BRINE, ItemGlassBottleFluid::new)
            .container(TFCItems.glassBottle)
            .fluid(1000, TFCFluids.BRINE, true);
        init.item(BOTTLE_HONEY, ItemGlassBottleFluid::new)
            .container(TFCItems.glassBottle)
            .fluid(1000, TFCFluids.HONEY, true);
        init.item(BOTTLE_SALT_WATER, ItemGlassBottleFluid::new)
            .container(TFCItems.glassBottle)
            .fluid(1000, TFCFluids.SALTWATER, true);

        init.item(JUG_OLIVE_OIL, ItemPotteryFluid::new)
            .container(TFCItems.potteryJug, 1)
            .fluid(1000, TFCFluids.OLIVEOIL, true);
        init.item(JUG_VINEGAR, ItemPotteryFluid::new)
            .container(TFCItems.potteryJug, 1)
            .fluid(1000, TFCFluids.VINEGAR, true);

        init.item(BOWL_OLIVE_OIL, ItemBowlFluid::new)
            .meta("PotteryBowl", "Bowl")
            .container(TFCItems.potteryBowl, 1)
            .fluid(250, TFCFluids.OLIVEOIL);
        init.item(BOWL_VINEGAR, ItemBowlFluid::new)
            .meta("PotteryBowl", "Bowl")
            .container(TFCItems.potteryBowl, 1)
            .fluid(250, TFCFluids.VINEGAR);
    }

}
