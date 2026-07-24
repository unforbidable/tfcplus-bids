package com.unforbidable.tfc.bids.features.food.cookedmeal;

import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingMixture;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepIngredient;
import com.unforbidable.tfc.bids.api.features.cookingprep.CookingPrepRecipe;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import com.unforbidable.tfc.bids.features.device.cookingprep.CookingPrepRegistry;
import com.unforbidable.tfc.bids.features.food.cookedmeal.item.ItemCookedMeal;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@FeatureName("cookedMeal")
public class CookedMeal extends Feature {


    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.STEW, ItemCookedMeal::new)
            .meta("Stew.Bean", "Stew.Meat", "Stew.Fish", "Stew.Vegetable");
        init.item(ItemNames.SOUP, ItemCookedMeal::new)
            .meta("Soup.Bean", "Soup.Meat", "Soup.Fish", "Soup.Vegetable");
        init.item(ItemNames.PORRIDGE, ItemCookedMeal::new)
            .meta("Porridge.Water", "Porridge.Milk");
        init.item(ItemNames.OMELET, ItemCookedMeal::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FoodItemRenderer())
            .item(BidsItems.stew)
            .item(BidsItems.soup)
            .item(BidsItems.porridge)
            .item(BidsItems.omelet);

        client.nei()
            .hide(BidsItems.stew)
            .hide(BidsItems.soup)
            .hide(BidsItems.porridge)
            .hide(BidsItems.omelet);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        CookingMixture beanMixture = new CookingMixture("bean", 0x99420f);
        CookingMixture beanWaterMixture = new CookingMixture("beanWater", 0x996140);
        CookingMixture beanStewMixture = new CookingMixture("beanStew", 0x693111,
            new ItemStack(BidsItems.stew, 1, 0));
        CookingMixture beanSoupMixture = new CookingMixture("beanSoup", 0x633f2a,
            new ItemStack(BidsItems.soup, 1, 0));

        CookingMixture meatMixture = new CookingMixture("meat", 0xc2230e);
        CookingMixture meatWaterMixture = new CookingMixture("meatWater", 0xc44b3b);
        CookingMixture meatStewMixture = new CookingMixture("meatStew", 0x6b150a,
            new ItemStack(BidsItems.stew, 1, 1));
        CookingMixture meatSoupMixture = new CookingMixture("meatSoup", 0x702b22,
            new ItemStack(BidsItems.soup, 1, 1));

        CookingMixture fishMixture = new CookingMixture("fish", 0xcc5047);
        CookingMixture fishWaterMixture = new CookingMixture("fishWater", 0xc96f69);
        CookingMixture fishStewMixture = new CookingMixture("fishStew", 0x732721,
            new ItemStack(BidsItems.stew, 1, 2));
        CookingMixture fishSoupMixture = new CookingMixture("fishSoup", 0x78413d,
            new ItemStack(BidsItems.soup, 1, 2));

        CookingMixture vegetableMixture = new CookingMixture("vegetable", 0x516b15);
        CookingMixture vegetableWaterMixture = new CookingMixture("vegetableWater", 0x5d6e38);
        CookingMixture vegetableStewMixture = new CookingMixture("vegetableStew", 0x32330c,
            new ItemStack(BidsItems.stew, 1, 3));
        CookingMixture vegetableSoupMixture = new CookingMixture("vegetableSoup", 0x3f4021,
            new ItemStack(BidsItems.soup, 1, 3));

        CookingMixture cerealMixture = new CookingMixture("cereal", 0xaba557);
        CookingMixture cerealWaterMixture = new CookingMixture("cerealWater", 0xb3af7d);
        CookingMixture cerealMilkMixture = new CookingMixture("cerealMilk", 0xe0ddb1);
        CookingMixture porridgeWaterMixture = new CookingMixture("porridgeWater", 0xa19e81,
            new ItemStack(BidsItems.porridge, 1, 0));
        CookingMixture porridgeMilkMixture = new CookingMixture("porridgeMilk", 0xd1d0ba,
            new ItemStack(BidsItems.porridge, 1, 1));

        CookingMixture eggMixture = new CookingMixture("egg", 0xe0d44a);
        CookingMixture omeletMixture = new CookingMixture("omelet", 0xdbc386,
            new ItemStack(BidsItems.omelet, 1, 0));

        setup.ores("foodBeans")
            .add(TFCItems.soybean);
//            .add(BidsItems.wildBeans)
//            .add(BidsItems.broadBeans);

        setup.ores("foodMeatRed")
            .add(TFCItems.beefRaw)
            .add(TFCItems.porkchopRaw)
            .add(TFCItems.muttonRaw)
            .add(TFCItems.venisonRaw)
            .add(TFCItems.horseMeatRaw);

        setup.ores("foodMeatPoultry")
            .add(TFCItems.chickenRaw);

        setup.ores("foodMeatFish")
            .add(TFCItems.fishRaw)
            .add(TFCItems.scallopRaw)
            .add(TFCItems.seastarRaw)
            .add(TFCItems.calamariRaw);

        setup.ores("foodGrainGround")
            .add(TFCItems.barleyGround)
            .add(TFCItems.oatGround)
            .add(TFCItems.ryeGround)
            .add(TFCItems.riceGround)
            .add(TFCItems.wheatGround)
            .add(TFCItems.cornmealGround);

        setup.ores("foodFruitBerry")
            .add(TFCItems.blackberry)
            .add(TFCItems.blueberry)
            .add(TFCItems.wintergreenBerry)
            .add(TFCItems.bunchberry)
            .add(TFCItems.cranberry)
            .add(TFCItems.raspberry)
            .add(TFCItems.gooseberry)
            .add(TFCItems.elderberry)
            .add(TFCItems.cloudberry)
            .add(TFCItems.snowberry)
            .add(TFCItems.strawberry);

        setup.ores("foodEgg")
            .add(TFCItems.egg);

        setup.ores("foodMushroom")
            .add(TFCItems.mushroomFoodB)
            .add(TFCItems.mushroomFoodR);

        setup.ores("foodBread")
            .add(TFCItems.wheatBread)
            .add(TFCItems.barleyBread)
            .add(TFCItems.oatBread)
            .add(TFCItems.ryeBread)
            .add(TFCItems.cornBread)
            .add(TFCItems.riceBread);

        setup.registry(CookingRegistry.mixtures)
            .add(beanMixture).add(beanWaterMixture).add(beanStewMixture).add(beanSoupMixture)
            .add(meatMixture).add(meatWaterMixture).add(meatStewMixture).add(meatSoupMixture)
            .add(fishMixture).add(fishWaterMixture).add(fishStewMixture).add(fishSoupMixture)
            .add(vegetableMixture).add(vegetableWaterMixture).add(vegetableStewMixture).add(vegetableSoupMixture)
            .add(cerealMixture).add(cerealWaterMixture).add(cerealMilkMixture).add(porridgeWaterMixture).add(porridgeMilkMixture)
            .add(eggMixture).add(omeletMixture);

        CookingPrepIngredient vesselLargeBowl = CookingPrepIngredient.builder()
            .allow(BidsItems.largeClayBowl, 1)
            .build();

        CookingPrepIngredient beans = CookingPrepIngredient.builder()
            .allow("foodBeans")
            .build();

        CookingPrepIngredient meatNoFish = CookingPrepIngredient.builder()
            .allow("foodMeatRed")
            .allow("foodMeatPoultry")
            .build();

        CookingPrepIngredient meatFish = CookingPrepIngredient.builder()
            .allow("foodMeatFish")
            .build();

        CookingPrepIngredient vegetable = CookingPrepIngredient.builder()
            .allow(EnumFoodGroup.Vegetable)
            .build();

        CookingPrepIngredient grainPorridge = CookingPrepIngredient.builder()
            .allow(TFCItems.maizeEar)
            .allow(TFCItems.riceGrain)
            .allow("foodGrainGround")
            .allow("foodGrainCrushed")
            .build();

        CookingPrepIngredient foodNoFruitNoBread = CookingPrepIngredient.builder()
            .allow(EnumFoodGroup.Dairy)
            .allow(EnumFoodGroup.Protein)
            .allow(EnumFoodGroup.Vegetable)
            .allow(TFCItems.maizeEar)
            .allow(TFCItems.riceGrain)
            .allow("foodGrainGround")
            .allow("foodGrainCrushed")
            .allow("foodHardtack")
            .build();

        CookingPrepIngredient porridgeIngredients = CookingPrepIngredient.builder()
            .allow(EnumFoodGroup.Fruit)
            .allow(EnumFoodGroup.Protein)
            .allow(EnumFoodGroup.Vegetable)
            .build();

        CookingPrepIngredient omeletIngredients = CookingPrepIngredient.builder()
            .allow(EnumFoodGroup.Dairy)
            .allow(EnumFoodGroup.Protein)
            .allow(EnumFoodGroup.Vegetable)
            .build();

        CookingPrepIngredient foodEgg = CookingPrepIngredient.builder()
            .allow("foodEgg")
            .build();

        setup.registry(CookingPrepRegistry.recipes)
            .add(new CookingPrepRecipe(beanMixture.asItemStack(),
                vesselLargeBowl.toSpec(), beans.toSpec(20, true),
                foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)))
            .add(new CookingPrepRecipe(meatMixture.asItemStack(),
                vesselLargeBowl.toSpec(), meatNoFish.toSpec(20, true),
                foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)))
            .add(new CookingPrepRecipe(fishMixture.asItemStack(),
                vesselLargeBowl.toSpec(), meatFish.toSpec(20, true),
                foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)))
            .add(new CookingPrepRecipe(vegetableMixture.asItemStack(),
                vesselLargeBowl.toSpec(), vegetable.toSpec(20, true),
                foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)));

        setup.registry(CookingPrepRegistry.recipes)
            .add(new CookingPrepRecipe(cerealMixture.asItemStack(),
                vesselLargeBowl.toSpec(), grainPorridge.toSpec(20, true),
                porridgeIngredients.toSpec(8, true), porridgeIngredients.toSpec(8), porridgeIngredients.toSpec(4)));

        setup.registry(CookingPrepRegistry.recipes)
            .add(new CookingPrepRecipe(eggMixture.asItemStack(),
                vesselLargeBowl.toSpec(), foodEgg.toSpec(20, true),
                omeletIngredients.toSpec(8, true), omeletIngredients.toSpec(8), omeletIngredients.toSpec(4)));

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(beanMixture.asFluidStack(500), new FluidStack(TFCFluids.FRESHWATER, 500))
                .produces(beanWaterMixture.asFluidStack(1000))
                .build())
            .add(CookingRecipe.builder()
                .consumes(meatMixture.asFluidStack(500), new FluidStack(TFCFluids.FRESHWATER, 500))
                .produces(meatWaterMixture.asFluidStack(1000))
                .build())
            .add(CookingRecipe.builder()
                .consumes(fishMixture.asFluidStack(500), new FluidStack(TFCFluids.FRESHWATER, 500))
                .produces(fishWaterMixture.asFluidStack(1000))
                .build())
            .add(CookingRecipe.builder()
                .consumes(vegetableMixture.asFluidStack(500), new FluidStack(TFCFluids.FRESHWATER, 500))
                .produces(vegetableWaterMixture.asFluidStack(1000))
                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(cerealMixture.asFluidStack(500), new FluidStack(TFCFluids.FRESHWATER, 500))
                .produces(cerealWaterMixture.asFluidStack(1000))
                .build())
            .add(CookingRecipe.builder()
                .consumes(cerealMixture.asFluidStack(500), new FluidStack(TFCFluids.MILK, 500))
                .produces(cerealMilkMixture.asFluidStack(1000))
                .build())
            .add(CookingRecipe.builder()
                .consumes(cerealMixture.asFluidStack(500), new FluidStack(BidsFluids.goatMilk, 500))
                .produces(cerealMilkMixture.asFluidStack(1000))
                .build());
//            .add(CookingRecipe.builder()
//                .consumes(cerealMixture.asFluidStack(500), new FluidStack(BidsFluids.SKIMMEDMILK, 500))
//                .produces(cerealMilkMixture.asFluidStack(1000))
//                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(beanMixture.asFluidStack(500))
                .produces(beanStewMixture.asFluidStack(500))
                .withHeat(CookingHeatLevel.LOW)
                .withLid()
                .inFixedTime(1000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(meatMixture.asFluidStack(500))
                .produces(meatStewMixture.asFluidStack(500))
                .withHeat(CookingHeatLevel.LOW)
                .withLid()
                .inFixedTime(1500)
                .build())
            .add(CookingRecipe.builder()
                .consumes(fishMixture.asFluidStack(500))
                .produces(fishStewMixture.asFluidStack(500))
                .withHeat(CookingHeatLevel.LOW)
                .withLid()
                .inFixedTime(1500)
                .build())
            .add(CookingRecipe.builder()
                .consumes(vegetableMixture.asFluidStack(500))
                .produces(vegetableStewMixture.asFluidStack(500))
                .withHeat(CookingHeatLevel.LOW)
                .withLid()
                .inFixedTime(1000)
                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(beanWaterMixture.asFluidStack(1000))
                .produces(beanSoupMixture.asFluidStack(1000))
                .withHeat(CookingHeatLevel.LOW, CookingHeatLevel.MEDIUM)
                .withLid()
                .inFixedTime(1500)
                .build())
            .add(CookingRecipe.builder()
                .consumes(meatWaterMixture.asFluidStack(1000))
                .produces(meatSoupMixture.asFluidStack(1000))
                .withHeat(CookingHeatLevel.LOW, CookingHeatLevel.MEDIUM)
                .withLid()
                .inFixedTime(2000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(fishWaterMixture.asFluidStack(1000))
                .produces(fishSoupMixture.asFluidStack(1000))
                .withHeat(CookingHeatLevel.LOW, CookingHeatLevel.MEDIUM)
                .withLid()
                .inFixedTime(2000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(vegetableWaterMixture.asFluidStack(1000))
                .produces(vegetableSoupMixture.asFluidStack(1000))
                .withHeat(CookingHeatLevel.LOW, CookingHeatLevel.MEDIUM)
                .withLid()
                .inFixedTime(1500)
                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(cerealWaterMixture.asFluidStack(1000))
                .produces(porridgeWaterMixture.asFluidStack(1000))
                .withHeat(CookingHeatLevel.LOW)
                .withLid()
                .inFixedTime(1000)
                .build())
            .add(CookingRecipe.builder()
                .consumes(cerealMilkMixture.asFluidStack(1000))
                .produces(porridgeMilkMixture.asFluidStack(1000))
                .withHeat(CookingHeatLevel.LOW)
                .withLid()
                .inFixedTime(1000)
                .build());

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(eggMixture.asFluidStack(500))
                .produces(omeletMixture.asFluidStack(500))
                .withHeat(CookingHeatLevel.LOW)
                .withLid()
                .inFixedTime(250)
                .build());
    }

}
