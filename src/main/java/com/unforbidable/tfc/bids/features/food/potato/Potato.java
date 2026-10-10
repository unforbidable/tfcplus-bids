package com.unforbidable.tfc.bids.features.food.potato;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.DoughRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.features.quern.QuernRecipe;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.api.util.food.BidsFood;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.HeatValue;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import com.unforbidable.tfc.bids.features.crafting.quern.QuernRegistry;
import com.unforbidable.tfc.bids.features.device.dryingsurface.DryingSurfaceRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

@FeatureName("potato")
public class Potato extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.POTATO_DOUGH, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 0, 0, 10, 15, 20))
            .food(1, 0.7f, false, false);
        init.item(ItemNames.POTATO_FLOUR, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 0, 0, 10, 15, 20))
            .food(0.5f, 0, false, true);
        init.item(ItemNames.POTATO_FLATBREAD, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 0, 0, 10, 15, 20))
            .food(1, 0.5f)
            .apply(ItemExtraFood::setNutritionAsIfCooked);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.potatoDough)
            .item(BidsItems.potatoFlour)
            .item(BidsItems.potatoFlatbread);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("foodStarch")
            .add(BidsItems.potatoFlour);

        setup.registry(TfcRegistry.Heat.values)
            .add(HeatValue.add(new ItemStack(BidsItems.potatoDough), 1, 88,
                new ItemStack(BidsItems.potatoFlatbread), true))
            .add(HeatValue.add(new ItemStack(BidsItems.potatoFlatbread), 1, 177, null));

        // Boiled potatoes -> Dough
        ItemStack boiledPotato = BidsFood.setBoiled(ItemFoodTFC.createTag(new ItemStack(TFCItems.potato)), true);
        setup.registry(QuernRegistry.recipes)
            .add(new QuernRecipe(boiledPotato, ItemFoodTFC.createTag(new ItemStack(BidsItems.potatoDough)), 0.25f));

        // Potato flour + water -> dough
        setup.registry(CookingRegistry.dough)
            .add(new DoughRecipe(new ItemStack(BidsItems.potatoFlour), new ItemStack(BidsItems.potatoDough)));

        // Dough -> Dried
        setup.registry(DryingSurfaceRegistry.recipes)
            .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .dry()
                .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.potatoDough), 1))
                .hours(8)
                .build());

        // Dried Potato Dough -> Flour
        ItemStack driedPotatoMash = ItemFoodTFC.createTag(new ItemStack(BidsItems.potatoDough));
        Food.setDried(driedPotatoMash, Food.DRYHOURS);
        setup.registry(QuernRegistry.recipes)
            .add(new QuernRecipe(driedPotatoMash, ItemFoodTFC.createTag(new ItemStack(BidsItems.potatoFlour)), 1f));
    }

}
