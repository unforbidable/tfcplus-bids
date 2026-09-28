package com.unforbidable.tfc.bids.features.material.wax;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.DilutableRecipeOutputFluid;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonMisc;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@FeatureName("wax")
public class Wax extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.WAX, ItemCommonMisc::new);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(TFCItems.emptyHoneycomb))
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 300))
                .produces(new FluidStack(TFCFluids.WAX, 300))
                .withHeat()
                .inTime(750)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(BidsItems.wax))
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 50))
                .produces(new FluidStack(TFCFluids.WAX, 50))
                .withHeat()
                .inTime(70)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.WAX, 50))
                .produces(new ItemStack(BidsItems.wax))
                .withoutHeat()
                .inFixedTime(1000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.WAX, 200), "materialString")
                .produces(new ItemStack(TFCBlocks.candleOff, 1))
                .withHeat()
                .build());

        setup.registry(CookingRegistry.dilutable)
            .add(TFCFluids.WAX, new DilutableRecipeOutputFluid(1000));
    }

}
