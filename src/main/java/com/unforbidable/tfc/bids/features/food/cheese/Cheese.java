package com.unforbidable.tfc.bids.features.food.cheese;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingCheeseRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackFoodRecipe;
import com.unforbidable.tfc.bids.api.names.FluidNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.fluid.FluidCommon;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import com.unforbidable.tfc.bids.features.device.dryingrack.DryingRackRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * <b>Adds new cheeses and fluids to make them</b>
 * <li><b>goat cheese</b> - a cheese from goat milk</li>
 * <li><b>hard cheese</b> - a cheese from skimmed milk</li>
 */
@FeatureName("cheese")
public class Cheese extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.fluid(FluidNames.GOAT_MILK_VINEGAR, FluidCommon::new)
            .color(0xfffbe8);
        init.fluid(FluidNames.GOAT_MILK_CURDLED, FluidCommon::new)
            .color(0xfffbe8);
        init.fluid(FluidNames.SKIMMED_MILK_VINEGAR, FluidCommon::new)
            .color(0xfffbe8);
        init.fluid(FluidNames.SKIMMED_MILK_CURDLED, FluidCommon::new)
            .color(0xfffbe8);

        init.item(ItemNames.GOAT_CHEESE, () -> new ItemExtraFood(EnumFoodGroup.Dairy, 0, 35, 20, 0, 20))
            .food(0.5f, 0.6f)
            .apply(ItemFoodTFC::setCanSmoke)
            .apply(i -> i.setSmokeAbsorbMultiplier(1));
        init.item(ItemNames.HARD_CHEESE, () -> new ItemExtraFood(EnumFoodGroup.Dairy, 0, 35, 20, 0, 20))
            .food(0.3f, 0.25f)
            .apply(ItemFoodTFC::setCanSmoke)
            .apply(i -> i.setSmokeAbsorbMultiplier(0.5f));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.goatCheese)
            .item(BidsItems.hardCheese);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.goatMilk, 9), new FluidStack(TFCFluids.VINEGAR, 1))
                .produces(new FluidStack(BidsFluids.goatMilkVinegar, 10))
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.goatMilkVinegar, 1))
                .produces(new FluidStack(BidsFluids.goatMilkCurdled, 1))
                .withoutHeat()
                .withLid()
                .inFixedTime(8000)
                .build())
            .add(CookingCheeseRecipe.builder()
                .allowingInfusion()
                .consumes(new FluidStack(BidsFluids.goatMilkCurdled, 1))
                .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.goatCheese), Global.FOOD_MAX_WEIGHT / 10000))
                .withoutHeat()
                .withLid()
                .inFixedTime(8000)
                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.skimmedMilk, 9), new FluidStack(TFCFluids.VINEGAR, 1))
                .produces(new FluidStack(BidsFluids.skimmedMilkVinegar, 10))
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.skimmedMilkVinegar, 1))
                .produces(new FluidStack(BidsFluids.skimmedMilkCurdled, 1))
                .withoutHeat()
                .withLid()
                .inFixedTime(8000)
                .build())
            .add(CookingCheeseRecipe.builder()
                .allowingInfusion()
                .consumes(new FluidStack(BidsFluids.skimmedMilkCurdled, 1))
                .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.hardCheese), Global.FOOD_MAX_WEIGHT / 10000))
                .withoutHeat()
                .withLid()
                .inFixedTime(8000)
                .build());

        for (Item food : new Item[] { BidsItems.goatCheese }) {
            setup.registry(DryingRackRegistry.recipes)
                .add((DryingRackFoodRecipe) DryingRackFoodRecipe.builder()
                    .smoke(12)
                    .tied()
                    .consumes(ItemFoodTFC.createTag(new ItemStack(food), 1))
                    .dry()
                    .hours(16)
                    .build());
        }
    }

}
