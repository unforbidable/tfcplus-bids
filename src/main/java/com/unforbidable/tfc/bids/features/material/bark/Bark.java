package com.unforbidable.tfc.bids.features.material.bark;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.DilutableRecipeOutputFluid;
import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import com.unforbidable.tfc.bids.api.names.ItemNames;
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
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitRegistry;
import com.unforbidable.tfc.bids.features.material.bark.item.ItemBark;
import net.minecraftforge.fluids.FluidStack;

@FeatureName("bark")
public class Bark extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(BarkConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.BARK, ItemBark::new)
            .meta(Global.WOOD_ALL);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            // Extracting tannin from bark
            if (wood.hasBarkTannin) {
                setup.registry(TfcRegistry.Barrel.recipes)
                    .add(BarrelRecipe.addItemDemanding(builder -> builder
                        .consumes(wood.items.getBark(), new FluidStack(TFCFluids.FRESHWATER, 625))
                        .produces(new FluidStack(TFCFluids.TANNIN, 500))
                        .withMinTechLevel(0)
                    ));

                // Originally making tannin consumed 625 units of Fresh Water
                // which is an arbitrary amount that allows converting a stack of bark/logs into a full barrel of tannin
                // since tannin is dilutable in the cooking recipe, it matters no more so use a nice rounded amount of 500 units
                setup.registry(CookingRegistry.recipes)
                    .add(CookingRecipe.builder()
                        .consumes(wood.items.getBark())
                        .consumes(new FluidStack(TFCFluids.FRESHWATER, 500))
                        .produces(new FluidStack(TFCFluids.TANNIN, 500))
                        .inFixedTime(20000)
                        .build());
            }

            setup.registry(CookingRegistry.dilutable)
                .add(TFCFluids.TANNIN, new DilutableRecipeOutputFluid(0));
        }

        setup.registry(FirepitRegistry.fuel)
            .add(BidsItems.bark, (FirepitFuelMaterial) BidsItems.bark);
    }

}
