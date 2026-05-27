package com.unforbidable.tfc.bids.features.food.flatbread;

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

import static com.unforbidable.tfc.bids.api.names.ItemNames.BARLEY_DOUGH_FLATBREAD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BARLEY_FLATBREAD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.CORN_DOUGH_FLATBREAD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.CORN_FLATBREAD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.OAT_DOUGH_FLATBREAD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.OAT_FLATBREAD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RICE_DOUGH_FLATBREAD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RICE_FLATBREAD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RYE_DOUGH_FLATBREAD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.RYE_FLATBREAD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WHEAT_DOUGH_FLATBREAD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WHEAT_FLATBREAD;

@FeatureName("flatbread")
public class Flatbread extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(WHEAT_DOUGH_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(3f, 0.7f, false, false);
        init.item(BARLEY_DOUGH_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20))
            .food(3f, 0.7f, false, false);
        init.item(OAT_DOUGH_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(3f, 0.7f, false, false);
        init.item(RYE_DOUGH_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20))
            .food(3f, 0.7f, false, false);
        init.item(RICE_DOUGH_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .food(3f, 0.7f, false, false);
        init.item(CORN_DOUGH_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20))
            .food(3f, 0.7f, false, false);

        init.item(WHEAT_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .apply(ItemExtraFood::setNutritionAsIfCooked);
        init.item(BARLEY_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 5, 20))
            .apply(ItemExtraFood::setNutritionAsIfCooked);
        init.item(OAT_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .apply(ItemExtraFood::setNutritionAsIfCooked);
        init.item(RYE_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 15, 0, 0, 20))
            .apply(ItemExtraFood::setNutritionAsIfCooked);
        init.item(RICE_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 10, 0, 0, 0, 20))
            .apply(ItemExtraFood::setNutritionAsIfCooked);
        init.item(CORN_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Grain, 25, 0, 0, 0, 20))
            .apply(ItemExtraFood::setNutritionAsIfCooked);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.wheatDoughFlatbread)
            .item(BidsItems.barleyDoughFlatbread)
            .item(BidsItems.oatDoughFlatbread)
            .item(BidsItems.ryeDoughFlatbread)
            .item(BidsItems.riceDoughFlatbread)
            .item(BidsItems.cornmealDoughFlatbread)
            .item(BidsItems.wheatFlatbread)
            .item(BidsItems.barleyFlatbread)
            .item(BidsItems.oatFlatbread)
            .item(BidsItems.ryeFlatbread)
            .item(BidsItems.riceFlatbread)
            .item(BidsItems.cornmealFlatbread);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // Used for cooking ingredients
        setup.ores("foodBread")
            .add(BidsItems.wheatFlatbread)
            .add(BidsItems.barleyFlatbread)
            .add(BidsItems.oatFlatbread)
            .add(BidsItems.ryeFlatbread)
            .add(BidsItems.riceFlatbread)
            .add(BidsItems.cornmealFlatbread);

        // Coarse flour + water -> flatbread dough
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatDoughFlatbread)),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatCrushed)), "itemLargeBowlWater");
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyDoughFlatbread)),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyFlatbread)), "itemLargeBowlWater");
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.oatDoughFlatbread)),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.oatFlatbread)), "itemLargeBowlWater");
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.ryeDoughFlatbread)),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.ryeFlatbread)), "itemLargeBowlWater");
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.riceDoughFlatbread)),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.riceFlatbread)), "itemLargeBowlWater");
        setup.recipes().addShapeless(ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealDoughFlatbread)),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealFlatbread)), "itemLargeBowlWater");

        // unshaped dough -> flatbread dough
        setup.registry(TfcRegistry.Recipes.knapping)
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatDoughFlatbread), 160),
                " ### ", "#####", "#####", "#####", " ### ", '#', new ItemStack(BidsItems.flatDough, 1, 0)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyDoughFlatbread), 160),
                " ### ", "#####", "#####", "#####", " ### ", '#', new ItemStack(BidsItems.flatDough, 1, 1)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.oatDoughFlatbread), 160),
                " ### ", "#####", "#####", "#####", " ### ", '#', new ItemStack(BidsItems.flatDough, 1, 2)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.ryeDoughFlatbread), 160),
                " ### ", "#####", "#####", "#####", " ### ", '#', new ItemStack(BidsItems.flatDough, 1, 3)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.riceDoughFlatbread), 160),
                " ### ", "#####", "#####", "#####", " ### ", '#', new ItemStack(BidsItems.flatDough, 1, 4)))
            .add(KnappingRecipe.add(ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealDoughFlatbread), 160),
                " ### ", "#####", "#####", "#####", " ### ", '#', new ItemStack(BidsItems.flatDough, 1, 5)));

        setup.registry(TfcRegistry.Values.heat)
            .add(HeatValue.add(new ItemStack(BidsItems.wheatDoughFlatbread), 1, 88,
                new ItemStack(BidsItems.wheatFlatbread), true))
            .add(HeatValue.add(new ItemStack(BidsItems.barleyDoughFlatbread), 1, 88,
                new ItemStack(BidsItems.barleyFlatbread), true))
            .add(HeatValue.add(new ItemStack(BidsItems.oatDoughFlatbread), 1, 88,
                new ItemStack(BidsItems.oatFlatbread), true))
            .add(HeatValue.add(new ItemStack(BidsItems.ryeDoughFlatbread), 1, 88,
                new ItemStack(BidsItems.ryeFlatbread), true))
            .add(HeatValue.add(new ItemStack(BidsItems.riceDoughFlatbread), 1, 88,
                new ItemStack(BidsItems.riceFlatbread), true))
            .add(HeatValue.add(new ItemStack(BidsItems.cornmealDoughFlatbread), 1, 88,
                new ItemStack(BidsItems.cornmealFlatbread), true));

        setup.registry(TfcRegistry.Values.heat)
            .add(HeatValue.add(new ItemStack(BidsItems.wheatFlatbread), 1, 177, null))
            .add(HeatValue.add(new ItemStack(BidsItems.barleyFlatbread), 1, 177, null))
            .add(HeatValue.add(new ItemStack(BidsItems.oatFlatbread), 1, 177, null))
            .add(HeatValue.add(new ItemStack(BidsItems.ryeFlatbread), 1, 177, null))
            .add(HeatValue.add(new ItemStack(BidsItems.riceFlatbread), 1, 177, null))
            .add(HeatValue.add(new ItemStack(BidsItems.cornmealFlatbread), 1, 177, null));
    }

}
