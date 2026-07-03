package com.unforbidable.tfc.bids.features.material.fishoil;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.pressing.StonePressRecipe;
import com.unforbidable.tfc.bids.api.names.FluidNames;
import com.unforbidable.tfc.bids.api.util.food.BidsFood;
import com.unforbidable.tfc.bids.common.fluid.FluidCommon;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemBowlFluid;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemBucketFluid;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemGlassBottleFluid;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemPotteryFluid;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingHelper;
import com.unforbidable.tfc.bids.features.device.lamp.LampRegistry;
import com.unforbidable.tfc.bids.features.device.saddlequern.StonePressConfig;
import com.unforbidable.tfc.bids.features.device.saddlequern.StonePressRegistry;
import com.unforbidable.tfc.bids.features.material.fishoil.fuel.FuelFishOil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.BOTTLE_FISH_OIL;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BOTTLE_OILY_FISH_WATER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BOWL_FISH_OIL;
import static com.unforbidable.tfc.bids.api.names.ItemNames.CERAMIC_BUCKET_OILY_FISH_WATER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.JUG_FISH_OIL;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOODEN_BUCKET_OILY_FISH_WATER;

@FeatureName("fishOil")
public class FishOil extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.fluid(FluidNames.OILY_FISH_WATER, FluidCommon::new)
            .color(0x124220);
        init.fluid(FluidNames.FISH_OIL, FluidCommon::new)
            .color(0xa1a36f);

        init.item(BOTTLE_OILY_FISH_WATER, ItemGlassBottleFluid::new);
        init.item(WOODEN_BUCKET_OILY_FISH_WATER, () -> new ItemBucketFluid(false));
        init.item(CERAMIC_BUCKET_OILY_FISH_WATER, () -> new ItemBucketFluid(true));

        init.item(BOTTLE_FISH_OIL, ItemGlassBottleFluid::new);
        init.item(JUG_FISH_OIL, ItemPotteryFluid::new);
        init.item(BOWL_FISH_OIL, ItemBowlFluid::new)
            .meta("PotteryBowl", "Bowl");
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.fluid(BidsFluids.oilyFishWater)
            .container(BidsItems.oilyFishWaterBottle, 1000, true, TFCItems.glassBottle)
            .container(BidsItems.woodenBucketOilyFishWater, 1000, false, TFCItems.woodenBucketEmpty)
            .container(BidsItems.ceramicBucketOilyFishWater, 1000, false, TFCItems.clayBucketEmpty, 1);

        setup.fluid(BidsFluids.fishOil)
            .container(BidsItems.fishOilBottle, 1000, true, TFCItems.glassBottle)
            .container(BidsItems.potteryJugFishOil, 1000, true, TFCItems.potteryJug, 1)
            .container(BidsItems.fishOilBowl, 0, 250, false, TFCItems.potteryBowl, 1)
            .container(BidsItems.fishOilBowl, 1, 250, false, TFCItems.potteryBowl, 2);

        float inputRatio = 1 / StonePressConfig.efficiency; // input multiplier (for non-food input)
        ItemStack steamedFish = BidsFood.setSteamed(ItemFoodTFC.createTag(new ItemStack(TFCItems.fishRaw), 0.5f * inputRatio), true);
        // Require fish to be steamed to medium level
        Food.setCooked(steamedFish, CookingHelper.getTempForItemStackCookedLevel(steamedFish, 3));
        setup.registry(StonePressRegistry.recipes)
            .add(new StonePressRecipe(new FluidStack(BidsFluids.oilyFishWater, 10), steamedFish));

        setup.registry(CookingRegistry.recipes).add(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.oilyFishWater, 1000))
            .produces(new FluidStack(TFCFluids.FRESHWATER, 950), new FluidStack(BidsFluids.fishOil, 50))
            .withoutHeat()
            .inFixedTime(48000)
            .build());

        setup.registry(LampRegistry.fuel)
            .add(BidsFluids.fishOil, new FuelFishOil());
    }

}
