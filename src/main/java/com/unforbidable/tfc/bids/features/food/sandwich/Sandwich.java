package com.unforbidable.tfc.bids.features.food.sandwich;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepIngredient;
import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepRecipe;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.cookingprep.CookingPrepRegistry;
import com.unforbidable.tfc.bids.features.food.sandwich.item.ItemMoreSandwich;
import com.unforbidable.tfc.bids.features.food.sandwich.item.ItemWrap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.STUFFED_MUSHROOM;
import static com.unforbidable.tfc.bids.api.names.ItemNames.STUFFED_PEPPER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WRAP;

@FeatureName("sandwich")
public class Sandwich extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(STUFFED_PEPPER, () -> new ItemMoreSandwich(new float[]{3, 6, 4, 2, 1}))
            .meta("Stuffed Pepper.Green", "Stuffed Pepper.Yellow", "Stuffed Pepper.Red");
        init.item(STUFFED_MUSHROOM, () -> new ItemMoreSandwich(new float[]{2, 3, 2, 2, 1}))
            .meta("Stuffed Mushroom.Brown");
        init.item(WRAP, () -> new ItemWrap(new float[]{3, 6, 4, 2, 1}))
            .meta("Wrap.Wheat", "Wrap.Barley", "Wrap.Oat", "Wrap.Rye", "Wrap.Corn", "Wrap.Rice");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.stuffedPepper)
            .item(BidsItems.stuffedMushroom)
            .item(BidsItems.wrap);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        CookingPrepIngredient stuffingIngredients = CookingPrepIngredient.builder()
            .allow(EnumFoodGroup.Fruit)
            .allow(EnumFoodGroup.Vegetable)
            .allow(EnumFoodGroup.Dairy)
            .allow(EnumFoodGroup.Protein)
            .allow(TFCItems.riceGrain)
            .allow("foodBread")
            .build();

        Item[] peppers = new Item[]{TFCItems.greenBellPepper, TFCItems.yellowBellPepper, TFCItems.redBellPepper};
        for (int i = 0; i < peppers.length; i++) {
            setup.registry(CookingPrepRegistry.recipes)
                .add(new CookingPrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.stuffedPepper, 1, i)), 10,
                    CookingPrepIngredient.from(peppers[i]).toSpec(3),
                    stuffingIngredients.toSpec(6), stuffingIngredients.toSpec(4), stuffingIngredients.toSpec(2), stuffingIngredients.toSpec(1)));
        }

        setup.registry(CookingPrepRegistry.recipes)
            .add(new CookingPrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.stuffedMushroom)), 7,
                CookingPrepIngredient.from(TFCItems.mushroomFoodB).toSpec(2),
                stuffingIngredients.toSpec(3), stuffingIngredients.toSpec(2), stuffingIngredients.toSpec(2), stuffingIngredients.toSpec(1)));

        CookingPrepIngredient wrapIngredients = CookingPrepIngredient.builder()
            .allow(EnumFoodGroup.Fruit)
            .allow(EnumFoodGroup.Vegetable)
            .allow(EnumFoodGroup.Dairy)
            .allow(EnumFoodGroup.Protein)
            .allow(TFCItems.riceGrain)
            .build();
        Item[] flatbread = new Item[]{BidsItems.wheatFlatbread, BidsItems.oatFlatbread, BidsItems.barleyFlatbread, BidsItems.ryeFlatbread, BidsItems.cornmealFlatbread, BidsItems.riceFlatbread};
        for (int i = 0; i < flatbread.length; i++) {
            setup.registry(CookingPrepRegistry.recipes)
                .add(new CookingPrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.wrap, 1, i)), 7,
                    CookingPrepIngredient.from(flatbread[i]).toSpec(2),
                    wrapIngredients.toSpec(3), wrapIngredients.toSpec(2), wrapIngredients.toSpec(2), wrapIngredients.toSpec(1)));
        }
    }

}
