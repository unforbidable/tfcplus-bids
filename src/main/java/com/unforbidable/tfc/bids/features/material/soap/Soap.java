package com.unforbidable.tfc.bids.features.material.soap;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.names.FluidNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.fluid.FluidCommon;
import com.unforbidable.tfc.bids.common.item.ItemFoodLike;
import com.unforbidable.tfc.bids.common.item.filledcontainer.ItemGlassBottleFluid;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.meta.Powder;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.BarrelRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import com.unforbidable.tfc.bids.features.device.dryingsurface.DryingSurfaceRegistry;
import com.unforbidable.tfc.bids.features.device.dryingsurface.main.rendering.SoapRenderInfo;
import com.unforbidable.tfc.bids.features.material.soap.item.ItemSoap;
import com.unforbidable.tfc.bids.features.utility.largebowl.item.ItemLargeBowlFluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@FeatureName("soap")
public class Soap extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(SoapConfig::load, "miscellaneous");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.fluid(FluidNames.WEAK_WOOD_ASH_LYE, FluidCommon::new)
            .color(0xffc075);
        init.fluid(FluidNames.WOOD_ASH_LYE, FluidCommon::new)
            .color(0xd88a10);
        init.fluid(FluidNames.TALLOW_WOOD_ASH_LYE, FluidCommon::new)
            .color(0xcc9258);
        init.fluid(FluidNames.OLIVE_OIL_WEAK_WOOD_ASH_LYE, FluidCommon::new)
            .color(0xcda55f);
        init.fluid(FluidNames.FISH_OIL_WEAK_WOOD_ASH_LYE, FluidCommon::new)
            .color(0xcda55f);
        init.fluid(FluidNames.FLAX_SEED_OIL_WEAK_WOOD_ASH_LYE, FluidCommon::new)
            .color(0xcda55f);
        init.fluid(FluidNames.SOAP, FluidCommon::new)
            .color(0xecc29b);
        init.fluid(FluidNames.UNCURED_SOAP, FluidCommon::new)
            .color(0xdeb186);
        init.fluid(FluidNames.SOAPY_WATER, FluidCommon::new)
            .color(0x305090);

        init.item(ItemNames.SOAP, ItemSoap::new);
        init.item(ItemNames.SOAP_UNCURED, ItemFoodLike::new);

        init.item(ItemNames.BOTTLE_WEAK_WOOD_ASH_LYE, ItemGlassBottleFluid::new);
        init.item(ItemNames.BOTTLE_WOOD_ASH_LYE, ItemGlassBottleFluid::new);
        init.item(ItemNames.BOTTLE_SOAPY_WATER, ItemGlassBottleFluid::new);
        init.item(ItemNames.LARGE_BOWL_WEAK_WOOD_ASH_LYE, ItemLargeBowlFluid::new);
        init.item(ItemNames.LARGE_BOWL_WOOD_ASH_LYE, ItemLargeBowlFluid::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.soap)
            .item(BidsItems.uncuredSoap);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.fluid(BidsFluids.weakWoodAshLye)
            .container(BidsItems.weakWoodAshLyeBottle, 1000, true, TFCItems.glassBottle);

        setup.fluid(BidsFluids.woodAshLye)
            .container(BidsItems.woodAshLyeBottle, 1000, true, TFCItems.glassBottle);

        setup.fluid(BidsFluids.soapyWater)
            .container(BidsItems.soapyWaterBottle, 1000, true, TFCItems.glassBottle);

        setup.registry(TfcRegistry.Barrel.recipes)
            .add(BarrelRecipe.addItemDemanding(builder -> builder
                .consumes(new ItemStack(TFCItems.powder, 1, Powder.ASH), new FluidStack(TFCFluids.FRESHWATER, 200))
                .produces(new FluidStack(BidsFluids.weakWoodAshLye, 200))
                .withMinTechLevel(0).withSealTime(20)
            ));

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 200), new ItemStack(TFCItems.powder, 1, Powder.ASH))
                .produces(new FluidStack(BidsFluids.weakWoodAshLye, 200))
                .inFixedTime(20000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.weakWoodAshLye, 2))
                .produces(new FluidStack(BidsFluids.woodAshLye, 1))
                .withHeat()
                .withoutLid()
                .inTime(1000 / 500f)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 1), new FluidStack(BidsFluids.woodAshLye, 1))
                .produces(new FluidStack(BidsFluids.weakWoodAshLye, 2))
                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.tallow, 5), new FluidStack(BidsFluids.woodAshLye, 4))
                .produces(new FluidStack(BidsFluids.tallowWoodAshLye, 9))
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.tallowWoodAshLye, 1))
                .produces(new FluidStack(BidsFluids.soap, 1))
                .withHeat(CookingHeatLevel.LOW)
                .inFixedTime(2000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.OLIVEOIL, 4), new FluidStack(BidsFluids.weakWoodAshLye, 1))
                .produces(new FluidStack(BidsFluids.oliveOilWeakWoodAshLye, 5))
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.oliveOilWeakWoodAshLye, 1))
                .produces(new FluidStack(BidsFluids.uncuredSoap, 1))
                .withHeat(CookingHeatLevel.LOW)
                .inFixedTime(3000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.fishOil, 4), new FluidStack(BidsFluids.weakWoodAshLye, 1))
                .produces(new FluidStack(BidsFluids.fishOilWeakWoodAshLye, 5))
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.fishOilWeakWoodAshLye, 1))
                .produces(new FluidStack(BidsFluids.uncuredSoap, 1))
                .withHeat(CookingHeatLevel.LOW)
                .inFixedTime(3000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.flaxSeedOil, 4), new FluidStack(BidsFluids.weakWoodAshLye, 1))
                .produces(new FluidStack(BidsFluids.flaxSeedOilWeakWoodAshLye, 5))
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.flaxSeedOilWeakWoodAshLye, 1))
                .produces(new FluidStack(BidsFluids.uncuredSoap, 1))
                .withHeat(CookingHeatLevel.LOW)
                .inFixedTime(3000)
                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.soap, 1))
                .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.soap), Global.FOOD_MAX_WEIGHT / 10000))
                .withoutHeat()
                .inTime(1000 / 5000f)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.uncuredSoap, 1))
                .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.uncuredSoap), Global.FOOD_MAX_WEIGHT / 10000))
                .withoutHeat()
                .inTime(500 / 5000f)
                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 1), ItemFoodTFC.createTag(new ItemStack(BidsItems.soap), 1f / 500))
                .produces(new FluidStack(BidsFluids.soapyWater, 1))
                .inTime(20 / 1000f)
                .build())
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.FRESHWATER, 1), ItemFoodTFC.createTag(new ItemStack(BidsItems.uncuredSoap), 1f / 250))
                .produces(new FluidStack(BidsFluids.soapyWater, 1))
                .inTime(20 / 1000f)
                .build());

        setup.registry(DryingSurfaceRegistry.recipes)
            .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.uncuredSoap), 1))
                .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.soap), 1))
                .dry()
                .cover()
                .hours(40)
                .build());

        setup.registry(DryingSurfaceRegistry.render)
            .add(BidsItems.soap, new SoapRenderInfo(true))
            .add(BidsItems.uncuredSoap, new SoapRenderInfo(false));
    }

}
