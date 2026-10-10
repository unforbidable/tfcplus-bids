package com.unforbidable.tfc.bids.features.food.coarseflour;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.quern.QuernRecipe;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.quern.QuernRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

@FeatureName("coarseFour")
public class CoarseFlour extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.WHEAT_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.wheatWhole));
        init.item(ItemNames.BARLEY_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.barleyWhole));
        init.item(ItemNames.OAT_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.oatWhole));
        init.item(ItemNames.RYE_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.ryeWhole));
        init.item(ItemNames.RICE_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.riceWhole));
        init.item(ItemNames.CORN_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20))
            .food(1.5f, false, false)
            .apply(i -> i.setIngredientOverride(TFCItems.maizeEar));
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

        setup.registry(QuernRegistry.recipes)
            .add(new QuernRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.wheatGrain)), ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatCrushed)), 1))
            .add(new QuernRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.barleyGrain)), ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyCrushed)), 1))
            .add(new QuernRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.oatGrain)), ItemFoodTFC.createTag(new ItemStack(BidsItems.oatCrushed)), 1))
            .add(new QuernRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.ryeGrain)), ItemFoodTFC.createTag(new ItemStack(BidsItems.ryeCrushed)), 1))
            .add(new QuernRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.riceGrain)), ItemFoodTFC.createTag(new ItemStack(BidsItems.riceCrushed)), 1))
            .add(new QuernRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.maizeEar)), ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealCrushed)), 1));
    }

}
