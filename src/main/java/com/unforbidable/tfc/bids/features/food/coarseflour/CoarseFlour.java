package com.unforbidable.tfc.bids.features.food.coarseflour;

import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.quern.SaddleQuernRecipe;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.saddlequern.SaddleQuernRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.BARLEY_CRUSHED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.CORN_CRUSHED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.OAT_CRUSHED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RICE_CRUSHED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RYE_CRUSHED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WHEAT_CRUSHED;

@FeatureName("coarseFour")
public class CoarseFlour extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(WHEAT_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.wheatWhole));
        init.item(BARLEY_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.barleyWhole));
        init.item(OAT_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.oatWhole));
        init.item(RYE_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.ryeWhole));
        init.item(RICE_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.riceWhole));
        init.item(CORN_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.maizeEar));

        // TODO porridge made by cooking coarse flour in water finally discontinued?
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.wheatCrushed)
            .item(BidsItems.barleyCrushed)
            .item(BidsItems.oatCrushed)
            .item(BidsItems.ryeCrushed)
            .item(BidsItems.riceCrushed)
            .item(BidsItems.cornmealCrushed);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // Used for cooking ingredients
        setup.ores("foodGrainCrushed")
            .add(BidsItems.wheatCrushed)
            .add(BidsItems.barleyCrushed)
            .add(BidsItems.oatCrushed)
            .add(BidsItems.ryeCrushed)
            .add(BidsItems.riceCrushed)
            .add(BidsItems.cornmealCrushed);

        setup.registry(SaddleQuernRegistry.recipes)
            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.wheatCrushed), new ItemStack(TFCItems.wheatGrain)))
            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.barleyCrushed), new ItemStack(TFCItems.barleyGrain)))
            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.oatCrushed), new ItemStack(TFCItems.oatGrain)))
            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.ryeCrushed), new ItemStack(TFCItems.ryeGrain)))
            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.riceCrushed), new ItemStack(TFCItems.riceGrain)))
            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.cornmealCrushed), new ItemStack(TFCItems.maizeEar)));
    }

}
