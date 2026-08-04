package com.unforbidable.tfc.bids.features.crafting.cooking;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingCheeseRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.names.FluidNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.compat.tfc.meta.Powder;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.cooking.eventhandler.CookedFoodTooltipHandler;
import com.unforbidable.tfc.bids.features.crafting.cooking.fluid.FluidCookingMixture;
import com.unforbidable.tfc.bids.features.crafting.cooking.item.ItemCookingMixture;
import com.unforbidable.tfc.bids.features.crafting.cooking.nei.CookingNeiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@FeatureName("cooking")
public class Cooking extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(CookingConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.fluid(FluidNames.COOKING_MIXTURE, FluidCookingMixture::new)
            .color(0xf58442);

        init.item(ItemNames.COOKING_MIXTURE, ItemCookingMixture::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.nei()
            .handler(new CookingNeiHandler())
            .hide(BidsItems.cookingMixture);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(CookingRegistry.ingredientOverrides)
            .add(TFCItems.barleyGrain, TFCItems.barleyWhole)
            .add(TFCItems.oatGrain, TFCItems.oatWhole)
            .add(TFCItems.ryeGrain, TFCItems.ryeWhole)
            .add(TFCItems.wheatGrain, TFCItems.wheatWhole)
            .add(TFCItems.riceGrain, TFCItems.riceWhole)
            .add(TFCItems.barleyGround, TFCItems.barleyWhole)
            .add(TFCItems.oatGround, TFCItems.oatWhole)
            .add(TFCItems.ryeGround, TFCItems.ryeWhole)
            .add(TFCItems.wheatGround, TFCItems.wheatWhole)
            .add(TFCItems.riceGround, TFCItems.riceWhole);

        // TFC recipes adapted from cooking pot
        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 500), new ItemStack(TFCItems.powder, 1, Powder.SALT))
                .produces(new FluidStack(TFCFluids.SALTWATER, 500))
                .inTime(20)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.SALTWATER, 500))
                .produces(new ItemStack(TFCItems.powder, 1, Powder.SALT))
                .withHeat()
                .withoutLid()
                .inTime(750)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(Items.snowball))
                .produces(new FluidStack(TFCFluids.FRESHWATER, 200))
                .withHeat()
                .inTime(200)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(Items.snowball))
                .produces(new FluidStack(TFCFluids.FRESHWATER, 200))
                .withoutHeat()
                .inTime(1000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(TFCItems.resin))
                .produces(new FluidStack(TFCFluids.PITCH, 50))
                .withHeat()
                .inTime(50)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(TFCItems.emptyHoneycomb))
                .produces(new FluidStack(TFCFluids.WAX, 300))
                .withHeat()
                .inTime(750)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.PITCH, 50), new ItemStack(TFCItems.stick))
                .produces(new ItemStack(TFCBlocks.torchOff, 1))
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.PITCH, 250), new ItemStack(TFCItems.leatherBag))
                .produces(new ItemStack(TFCItems.pitchBag, 1))
                .inTime(100)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 9), new FluidStack(TFCFluids.HONEY, 1))
                .produces(new FluidStack(TFCFluids.HONEYWATER, 10))
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.SALTWATER, 9), new FluidStack(TFCFluids.VINEGAR, 1))
                .produces(new FluidStack(TFCFluids.BRINE, 10))
                .build());

        // TFC Cheese
        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.MILK, 9), new FluidStack(TFCFluids.VINEGAR, 1))
                .produces(new FluidStack(TFCFluids.MILKVINEGAR, 10))
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.MILKVINEGAR, 1))
                .produces(new FluidStack(TFCFluids.MILKCURDLED, 1))
                .withoutHeat()
                .withLid()
                .inFixedTime(8000)
                .build())
            .add(CookingCheeseRecipe.builder()
                .allowingInfusion()
                .consumes(new FluidStack(TFCFluids.MILKCURDLED, 1))
                .produces(ItemFoodTFC.createTag(new ItemStack(TFCItems.cheese), Global.FOOD_MAX_WEIGHT / 10000))
                .withoutHeat()
                .withLid()
                .inFixedTime(8000)
                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.WAX, 200), "materialString")
                .produces(new ItemStack(TFCBlocks.candleOff, 1))
                .withHeat()
                .build());

        setup.event()
            .handler(new CookedFoodTooltipHandler());
    }

}
