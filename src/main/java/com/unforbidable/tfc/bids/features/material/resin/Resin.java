package com.unforbidable.tfc.bids.features.material.resin;

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

@FeatureName("resin")
public class Resin extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.SOLIDIFIED_PITCH, ItemCommonMisc::new);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(TFCItems.resin))
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 50))
                .produces(new FluidStack(TFCFluids.PITCH, 50))
                .withHeat()
                .inTime(150)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(BidsItems.solidifiedPitch))
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 50))
                .produces(new FluidStack(TFCFluids.PITCH, 50))
                .withHeat()
                .inTime(70)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.PITCH, 50))
                .produces(new ItemStack(BidsItems.solidifiedPitch))
                .withoutHeat()
                .inFixedTime(1000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.PITCH, 50), new ItemStack(TFCItems.stick))
                .produces(new ItemStack(TFCBlocks.torchOff, 1))
                .build());

        setup.registry(CookingRegistry.dilutable)
            .add(TFCFluids.PITCH, new DilutableRecipeOutputFluid(1000));
    }

}
