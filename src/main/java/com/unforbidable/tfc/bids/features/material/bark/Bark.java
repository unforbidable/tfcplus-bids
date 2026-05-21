package com.unforbidable.tfc.bids.features.material.bark;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.BarrelRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitRegistry;
import com.unforbidable.tfc.bids.features.material.bark.item.ItemBark;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import static com.unforbidable.tfc.bids.api.names.ItemNames.BARK;

@FeatureName("bark")
public class Bark extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(BarkConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(BARK, ItemBark::new)
            .meta(Global.WOOD_ALL);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.hasBarkFibers) {
                setup.ores("itemBarkHasFibers")
                    .add(wood.items.getBark());
            }

            // Extracting tannin from bark
            if (wood.hasBarkTannin) {
                setup.registry(TfcRegistry.Recipes.barrel)
                    .add(BarrelRecipe.addItemDemanding(builder -> builder
                        .consumes(wood.items.getBark(), new FluidStack(TFCFluids.FRESHWATER, 625))
                        .produces(new FluidStack(TFCFluids.TANNIN, 500))
                        .withMinTechLevel(0)
                    ));
            }
        }

        setup.registry(FirepitRegistry.fuel)
            .add(BidsItems.bark, (FirepitFuelMaterial) BidsItems.bark);
    }

}
