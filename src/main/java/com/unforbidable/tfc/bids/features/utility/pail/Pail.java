package com.unforbidable.tfc.bids.features.utility.pail;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.utility.pail.item.ItemPailEmpty;
import com.unforbidable.tfc.bids.features.utility.pail.item.ItemPailFluid;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.WOODEN_PAIL;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOODEN_PAIL_FRESH_WATER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOODEN_PAIL_GOAT_MILK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOODEN_PAIL_MILK;

@FeatureName("pail")
public class Pail extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(WOODEN_PAIL, ItemPailEmpty::new);
        init.item(WOODEN_PAIL_MILK, ItemPailFluid::new);
        init.item(WOODEN_PAIL_FRESH_WATER, ItemPailFluid::new);
        init.item(WOODEN_PAIL_GOAT_MILK, ItemPailFluid::new);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.fluid(TFCFluids.MILK)
            .container(BidsItems.woodenPailMilk, 4000, true, BidsItems.woodenPailEmpty);

        setup.fluid(TFCFluids.FRESHWATER)
            .container(BidsItems.woodenPailFreshWater, 4000, true, BidsItems.woodenPailEmpty);

        setup.fluid(BidsFluids.goatMilk)
            .container(BidsItems.woodenPailGoatMilk, 4000, true, BidsItems.woodenPailEmpty);

        setup.ores("itemMilkingContainer")
            .add(BidsItems.woodenPailEmpty)
            .add(BidsItems.woodenPailMilk)
            .add(BidsItems.woodenPailGoatMilk);

        // Also used for screw press basket
        setup.ores("plateToolMetal")
            .add(TFCItems.copperSheet)
            .add(TFCItems.bronzeSheet)
            .add(TFCItems.bismuthBronzeSheet)
            .add(TFCItems.blackBronzeSheet)
            .add(TFCItems.wroughtIronSheet)
            .add(TFCItems.steelSheet)
            .add(TFCItems.blackSteelSheet)
            .add(TFCItems.redSteelSheet)
            .add(TFCItems.blueSteelSheet);

        setup.recipes().addShaped(new ItemStack(BidsItems.woodenPailEmpty),
            "w  ", "wxw", " w ", 'w', "woodLumber", 'x', "plateToolMetal");
    }

}
