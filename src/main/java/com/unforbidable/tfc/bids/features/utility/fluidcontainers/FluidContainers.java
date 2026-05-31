package com.unforbidable.tfc.bids.features.utility.fluidcontainers;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemBowlFluid;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemGlassBottleFluid;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemPotteryFluid;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;

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
        init.item(BOTTLE_OLIVE_OIL, ItemGlassBottleFluid::new);
        init.item(BOTTLE_VINEGAR, ItemGlassBottleFluid::new);
        init.item(BOTTLE_BRINE, ItemGlassBottleFluid::new);
        init.item(BOTTLE_HONEY, ItemGlassBottleFluid::new);
        init.item(BOTTLE_SALT_WATER, ItemGlassBottleFluid::new);

        init.item(JUG_OLIVE_OIL, ItemPotteryFluid::new);
        init.item(JUG_VINEGAR, ItemPotteryFluid::new);

        init.item(BOWL_OLIVE_OIL, ItemBowlFluid::new)
            .meta("PotteryBowl", "Bowl");
        init.item(BOWL_VINEGAR, ItemBowlFluid::new)
            .meta("PotteryBowl", "Bowl");
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.fluid(TFCFluids.OLIVEOIL)
            .container(BidsItems.oliveOilBottle, 1000, true, TFCItems.glassBottle)
            .container(BidsItems.potteryJugOliveOil, 1000, true, TFCItems.potteryJug, 1)
            .container(BidsItems.oliveOilBowl, 0, 250, false, TFCItems.potteryBowl, 1)
            .container(BidsItems.oliveOilBowl, 1, 250, false, TFCItems.potteryBowl, 2);

        setup.fluid(TFCFluids.VINEGAR)
            .container(BidsItems.vinegarBottle, 1000, true, TFCItems.glassBottle)
            .container(BidsItems.potteryJugVinegar, 1000, true, TFCItems.potteryJug, 1)
            .container(BidsItems.vinegarBowl, 0, 250, false, TFCItems.potteryBowl, 1)
            .container(BidsItems.vinegarBowl, 1, 250, false, TFCItems.potteryBowl, 2);

        setup.fluid(TFCFluids.BRINE)
            .container(BidsItems.brineBottle, 1000, true, TFCItems.glassBottle);

        setup.fluid(TFCFluids.HONEY)
            .container(BidsItems.honeyBottle, 1000, true, TFCItems.glassBottle);

        setup.fluid(TFCFluids.SALTWATER)
            .container(BidsItems.saltWaterBottle, 1000, true, TFCItems.glassBottle);
    }

}
