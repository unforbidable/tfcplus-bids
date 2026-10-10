package com.unforbidable.tfc.bids.features.food.fern;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.DilutableRecipeOutputFluid;
import com.unforbidable.tfc.bids.api.features.cooking.DoughRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.features.quern.QuernRecipe;
import com.unforbidable.tfc.bids.api.names.FluidNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.fluid.FluidCommon;
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
import net.minecraftforge.fluids.FluidStack;

@FeatureName("fern")
public class Fern extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.fluid(FluidNames.FERN_STARCH, FluidCommon::new)
            .color(0xf5aa7f);

        init.item(ItemNames.FERN_RHIZOME, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 10, 0, 0, 20, 0))
            .food(1f, 0.5f, false, false);
        init.item(ItemNames.FERN_RHIZOME_CRUSHED, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 10, 0, 0, 20, 0))
            .food(3f, 0.5f, false, false);
        init.item(ItemNames.FERN_DOUGH, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 10, 0, 0, 20, 0))
            .food(1, 1, false, false);
        init.item(ItemNames.FERN_FLOUR, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 10, 0, 0, 20, 0))
            .food(0.5f, 0, false, true)
            .apply(ItemExtraFood::setNutritionAsIfCooked);
        init.item(ItemNames.FERN_CAKE, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 10, 0, 0, 20, 0))
            .food(0.25f, 0)
            .apply(ItemExtraFood::setNutritionAsIfCooked);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.fernRhizome)
            .item(BidsItems.fernRhizomeCrushed)
            .item(BidsItems.fernDough)
            .item(BidsItems.fernFlour)
            .item(BidsItems.fernCake);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // Used as Pemmican additive
        setup.ores("foodStarch")
            .add(BidsItems.fernFlour);

        // Used with soups and stews like a hardtack
        setup.ores("foodHardtack")
            .add(BidsItems.fernCake);

        setup.registry(TfcRegistry.Heat.values)
            .add(HeatValue.add(new ItemStack(BidsItems.fernDough), 1, 88,
                new ItemStack(BidsItems.fernCake), true))
            .add(HeatValue.add(new ItemStack(BidsItems.fernCake), 1, 177, null));

        // Rhizome -> Crushed
        setup.registry(QuernRegistry.recipes)
            .add(new QuernRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.fernRhizome)),
                ItemFoodTFC.createTag(new ItemStack(BidsItems.fernRhizomeCrushed)), 1f));

        // Crushed rhizome -> Starch -> Dough
        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.fernRhizomeCrushed), Global.FOOD_MAX_WEIGHT / 4000))
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 1))
                .produces(new FluidStack(BidsFluids.fernStarch, 1))
                .withHeat(CookingHeatLevel.LOW)
                .inTime(4000 / 5000f)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.fernStarch, 1))
                .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.fernDough), Global.FOOD_MAX_WEIGHT / 4000))
                .withoutHeat()
                .inFixedTime(1000)
                .build());

        setup.registry(CookingRegistry.dilutable)
            .add(BidsFluids.fernStarch, new DilutableRecipeOutputFluid(1000));

        // Fern flour + water -> dough
        setup.registry(CookingRegistry.dough)
            .add(new DoughRecipe(new ItemStack(BidsItems.fernFlour), new ItemStack(BidsItems.fernDough)));

        // Dough -> Dried
        setup.registry(DryingSurfaceRegistry.recipes)
            .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .dry()
                .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.fernDough), 1))
                .hours(8)
                .build());

        // Dried Fern Dough -> Flour
        ItemStack driedFernDough = ItemFoodTFC.createTag(new ItemStack(BidsItems.fernDough));
        Food.setDried(driedFernDough, Food.DRYHOURS);
        setup.registry(QuernRegistry.recipes)
            .add(new QuernRecipe(driedFernDough, ItemFoodTFC.createTag(new ItemStack(BidsItems.fernFlour)), 1f));
    }

}
