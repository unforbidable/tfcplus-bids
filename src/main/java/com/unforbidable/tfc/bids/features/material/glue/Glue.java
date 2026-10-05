package com.unforbidable.tfc.bids.features.material.glue;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.DilutableRecipeOutputFluid;
import com.unforbidable.tfc.bids.api.meta.GlueMeta;
import com.unforbidable.tfc.bids.api.meta.MoreHideMeta;
import com.unforbidable.tfc.bids.api.names.FluidNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.fluid.FluidCommon;
import com.unforbidable.tfc.bids.common.item.ItemCommonMisc;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.BarrelRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@FeatureName("glue")
public class Glue extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.fluid(FluidNames.HIDE_GLUE, FluidCommon::new)
            .color(0x6b3b0c);
        init.fluid(FluidNames.PITCH_GLUE, FluidCommon::new)
            .color(0x2e2421);

        init.item(ItemNames.GLUE, ItemCommonMisc::new)
            .meta("Hide", "Pitch");
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("materialGlue")
            .add(new ItemStack(BidsItems.glue, 1, GlueMeta.HIDE))
            .add(new ItemStack(BidsItems.glue, 1, GlueMeta.PITCH));

        setup.ores("materialHideScrap")
            .add(new ItemStack(BidsItems.moreHide, 1, MoreHideMeta.HIDE_SCRAP))
            .add(new ItemStack(BidsItems.moreHide, 1, MoreHideMeta.RUINED_HIDE_SCRAP));

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes("materialHideScrap")
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 50))
                .produces(new FluidStack(BidsFluids.hideGlue, 50))
                .withHeat(CookingHeatLevel.LOW)
                .inTime(200)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(BidsItems.glue, 1, GlueMeta.HIDE))
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 50))
                .produces(new FluidStack(BidsFluids.hideGlue, 50))
                .withHeat(CookingHeatLevel.LOW)
                .inFixedTime(1000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.hideGlue, 50))
                .produces(new ItemStack(BidsItems.glue, 1, GlueMeta.HIDE))
                .withoutHeat()
                .inFixedTime(500)
                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(BidsItems.wax))
                .consumes(new FluidStack(TFCFluids.PITCH, 50))
                .produces(new FluidStack(BidsFluids.pitchGlue, 50))
                .withHeat(CookingHeatLevel.LOW)
                .inTime(100)
                .build())
            .add(CookingRecipe.builder()
                .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.tallow), 1))
                .consumes(new FluidStack(TFCFluids.PITCH, 50))
                .produces(new FluidStack(BidsFluids.pitchGlue, 50))
                .withHeat(CookingHeatLevel.LOW)
                .inTime(100)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(BidsItems.glue, 1, GlueMeta.PITCH))
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 50))
                .produces(new FluidStack(BidsFluids.pitchGlue, 50))
                .withHeat(CookingHeatLevel.LOW)
                .inFixedTime(1000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.pitchGlue, 50))
                .produces(new ItemStack(BidsItems.glue, 1, GlueMeta.PITCH))
                .withoutHeat()
                .inFixedTime(500)
                .build());

        setup.registry(CookingRegistry.dilutable)
            .add(BidsFluids.hideGlue, new DilutableRecipeOutputFluid(1000))
            .add(BidsFluids.pitchGlue, new DilutableRecipeOutputFluid(1000));
    }

}
