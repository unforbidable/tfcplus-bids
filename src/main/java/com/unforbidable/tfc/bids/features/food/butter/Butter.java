package com.unforbidable.tfc.bids.features.food.butter;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.churning.ChurningRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.names.FluidNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.fluid.FluidCommon;
import com.unforbidable.tfc.bids.common.item.ItemCommonDrink;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemGlassBottleFluid;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.compat.tfc.meta.Powder;
import com.unforbidable.tfc.bids.core.drink.DrinkRegistry;
import com.unforbidable.tfc.bids.core.drink.registry.DrinkFluid;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.churning.ChurningRegistry;
import com.unforbidable.tfc.bids.features.crafting.churning.item.ItemWaterskinChurn;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@FeatureName("butter")
public class Butter extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.fluid(FluidNames.SKIMMED_MILK, FluidCommon::new)
            .color(0xffffff);

        init.fluid(FluidNames.CREAM, FluidCommon::new)
            .color(0xfffdd0);

        init.item(ItemNames.BOTTLE_SKIMMED_MILK, () -> new ItemCommonDrink(1000, false, 0, 20, 40, 60, 80, 100))
            .apply(i -> i.setCanDrinkInParts(true)
                .setFoodGroup(EnumFoodGroup.Dairy)
                .setCalories(0.321f)
                .setWaterRestoreRatio(1f));
        init.item(ItemNames.JUG_SKIMMED_MILK, () -> new ItemCommonDrink(1000, true))
            .apply(i -> i.setCanDrinkInParts(true)
                .setFoodGroup(EnumFoodGroup.Dairy)
                .setCalories(0.321f)
                .setWaterRestoreRatio(1f));

        init.item(ItemNames.BOTTLE_CREAM, ItemGlassBottleFluid::new);

        init.item(ItemNames.WATERSKIN_CREAM, ItemWaterskinChurn::new);

        init.item(ItemNames.BUTTER, () -> new ItemExtraFood(EnumFoodGroup.Dairy, 35, 0, 10, 0, 50))
            .food(1f)
            .apply(ItemExtraFood::setNutritionAsIfCooked);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.butter);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.fluid(BidsFluids.cream)
            .container(BidsItems.creamBottle, 1000, true, TFCItems.glassBottle)
            .container(BidsItems.waterskinCream, 1000, true, TFCItems.waterskinEmpty);

        setup.fluid(BidsFluids.skimmedMilk)
            .container(BidsItems.skimmedMilkBottle, 1000, true, TFCItems.glassBottle)
            .container(BidsItems.potteryJugSkimmedMilk, 1000, true, TFCItems.potteryJug, 1);

        setup.registry(DrinkRegistry.drinks)
            .add(new DrinkFluid("SkimmedMilk", BidsFluids.skimmedMilk, 1, 0.321f, EnumFoodGroup.Dairy));

        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.butter, 1)),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.butter, 1)), new ItemStack(TFCItems.powder, 1, Powder.SALT));

        setup.registry(ChurningRegistry.recipes)
            .add(new ChurningRecipe(new FluidStack(BidsFluids.cream, 1), ItemFoodTFC.createTag(new ItemStack(BidsItems.butter), Global.FOOD_MAX_WEIGHT / 4000),
                1));

        setup.registry(CookingRegistry.recipes).add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.MILK, 500))
                .produces(new FluidStack(BidsFluids.skimmedMilk, 450), new FluidStack(BidsFluids.cream, 50))
                .withoutHeat()
                .inFixedTime(24000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.goatMilk, 500))
                .produces(new FluidStack(BidsFluids.skimmedMilk, 450), new FluidStack(BidsFluids.cream, 50))
                .withoutHeat()
                .inFixedTime(24000)
                .build());

    }

}
