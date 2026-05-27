package com.unforbidable.tfc.bids.features.food.hardtack;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.HeatValue;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.BARLEY_DOUGH_HARDTACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BARLEY_HARDTACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.CORN_DOUGH_HARDTACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.CORN_HARDTACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.OAT_DOUGH_HARDTACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.OAT_HARDTACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RICE_DOUGH_HARDTACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RICE_HARDTACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RYE_DOUGH_HARDTACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RYE_HARDTACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WHEAT_DOUGH_HARDTACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WHEAT_HARDTACK;

@FeatureName("hardtack")
public class Hardtack extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(WHEAT_DOUGH_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(3f, 0.7f, false, false);
        init.item(BARLEY_DOUGH_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20))
            .food(3f, 0.7f, false, false);
        init.item(OAT_DOUGH_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(3f, 0.7f, false, false);
        init.item(RYE_DOUGH_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20))
            .food(3f, 0.7f, false, false);
        init.item(RICE_DOUGH_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(3f, 0.7f, false, false);
        init.item(CORN_DOUGH_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20))
            .food(3f, 0.7f, false, false);

        init.item(WHEAT_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(0.02f)
            .apply(ItemExtraFood::setNutritionAsIfCooked);
        init.item(BARLEY_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20))
            .food(0.02f)
            .apply(ItemExtraFood::setNutritionAsIfCooked);
        init.item(OAT_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(0.02f)
            .apply(ItemExtraFood::setNutritionAsIfCooked);
        init.item(RYE_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20))
            .food(0.02f)
            .apply(ItemExtraFood::setNutritionAsIfCooked);
        init.item(RICE_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(0.02f)
            .apply(ItemExtraFood::setNutritionAsIfCooked);
        init.item(CORN_HARDTACK, () -> new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20))
            .food(0.02f)
            .apply(ItemExtraFood::setNutritionAsIfCooked);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.wheatDoughHardtack)
            .item(BidsItems.barleyDoughHardtack)
            .item(BidsItems.oatDoughHardtack)
            .item(BidsItems.ryeDoughHardtack)
            .item(BidsItems.riceDoughHardtack)
            .item(BidsItems.cornmealDoughHardtack)
            .item(BidsItems.wheatHardtack)
            .item(BidsItems.barleyHardtack)
            .item(BidsItems.oatHardtack)
            .item(BidsItems.ryeHardtack)
            .item(BidsItems.riceHardtack)
            .item(BidsItems.cornmealHardtack);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // Used for cooking ingredients
        setup.ores("foodHardtack")
            .add(BidsItems.wheatHardtack)
            .add(BidsItems.barleyHardtack)
            .add(BidsItems.oatHardtack)
            .add(BidsItems.ryeHardtack)
            .add(BidsItems.riceHardtack)
            .add(BidsItems.cornmealHardtack);

        setup.registry(TfcRegistry.Recipes.knapping)
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatDoughHardtack), 160),
                "#####", "# # #", "#####", "# # #", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 0)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyDoughHardtack), 160),
                "#####", "# # #", "#####", "# # #", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 1)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.oatDoughHardtack), 160),
                "#####", "# # #", "#####", "# # #", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 2)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.ryeDoughHardtack), 160),
                "#####", "# # #", "#####", "# # #", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 3)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.riceDoughHardtack), 160),
                "#####", "# # #", "#####", "# # #", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 4)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealDoughHardtack), 160),
                "#####", "# # #", "#####", "# # #", "#####", '#', new ItemStack(BidsItems.flatDough, 1, 5)));

        setup.registry(TfcRegistry.Values.heat)
            .add(HeatValue.add(new ItemStack(BidsItems.wheatDoughHardtack), 1, 88,
                new ItemStack(BidsItems.wheatHardtack), true))
            .add(HeatValue.add(new ItemStack(BidsItems.barleyDoughHardtack), 1, 88,
                new ItemStack(BidsItems.barleyHardtack), true))
            .add(HeatValue.add(new ItemStack(BidsItems.oatDoughHardtack), 1, 88,
                new ItemStack(BidsItems.oatHardtack), true))
            .add(HeatValue.add(new ItemStack(BidsItems.ryeDoughHardtack), 1, 88,
                new ItemStack(BidsItems.ryeHardtack), true))
            .add(HeatValue.add(new ItemStack(BidsItems.riceDoughHardtack), 1, 88,
                new ItemStack(BidsItems.riceHardtack), true))
            .add(HeatValue.add(new ItemStack(BidsItems.cornmealDoughHardtack), 1, 88,
                new ItemStack(BidsItems.cornmealHardtack), true));

        setup.registry(TfcRegistry.Values.heat)
            .add(HeatValue.add(new ItemStack(BidsItems.wheatHardtack), 1, 177, null))
            .add(HeatValue.add(new ItemStack(BidsItems.barleyHardtack), 1, 177, null))
            .add(HeatValue.add(new ItemStack(BidsItems.oatHardtack), 1, 177, null))
            .add(HeatValue.add(new ItemStack(BidsItems.ryeHardtack), 1, 177, null))
            .add(HeatValue.add(new ItemStack(BidsItems.riceHardtack), 1, 177, null))
            .add(HeatValue.add(new ItemStack(BidsItems.cornmealHardtack), 1, 177, null));
    }

}
