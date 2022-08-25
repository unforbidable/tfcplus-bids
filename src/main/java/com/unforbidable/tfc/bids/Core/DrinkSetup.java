package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Drinks.Drink;
import com.unforbidable.tfc.bids.Core.Drinks.DrinkHelper;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.DrinkRegistry;

public class DrinkSetup extends BidsItems {

    public static void preInit() {
        registerDrinks();
    }

    public static void postInit() {
        registerFluidContainers();
    }

    private static void registerFluidContainers() {
        Bids.LOG.info("Register fluit containers for drinks");

        DrinkHelper.registerFluidContainers(drinkingGlass, 250, true, 0, 40, 80, 100);
        DrinkHelper.registerFluidContainers(glassJug, 2000, true, 0, 11, 22, 33, 44, 55, 66, 77, 88, 100);
        DrinkHelper.registerFluidContainers(shotGlass, 50, false);
        DrinkHelper.registerPotteryFluidContainers(clayMug, 200, true, 0, 100);
        DrinkHelper.registerFluidContainers(birchBarkCup, 250, true, 0, 100);
    }

    private static void registerDrinks() {
        Bids.LOG.info("Register drinks");

        DrinkRegistry.registerDrink(new Drink(TFCFluids.FRESHWATER, "FreshWater")
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.MILK, "Milk")
                .setCalories(0.584f).setFoodGroup(EnumFoodGroup.Dairy)
                .setWaterRestoreRatio(1));

        DrinkRegistry.registerDrink(new Drink(TFCFluids.GRAPEJUICE, "GrapeJuice")
                .setCalories(152 / 250f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.AGAVEJUICE, "AgaveJuice")
                .setCalories(50 / 250f).setFoodGroup(EnumFoodGroup.Vegetable)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.CANEJUICE, "CaneJuice")
                .setCalories(269 / 250f)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.APPLEJUICE, "AppleJuice")
                .setCalories(113 / 250f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.ORANGEJUICE, "OrangeJuice")
                .setCalories(111 / 250f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.LEMONJUICE, "LemonJuice")
                .setCalories(53 / 250f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.CHERRYJUICE, "CherryJuice")
                .setCalories(119 / 250f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PLUMJUICE, "PlumJuice")
                .setCalories(182 / 250f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PEACHJUICE, "PeachJuice")
                .setCalories(134 / 250f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.DATEJUICE, "DateJuice")
                .setCalories(152 / 250f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PAPAYAJUICE, "PapayaJuice")
                .setCalories(142 / 250f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.FIGJUICE, "FigJuice")
                .setCalories(132 / 250f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.BERRYJUICE, "BerryJuice")
                .setCalories(106 / 250f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.COCONUTJUICE, "CoconutJuice")
                .setCalories(0.18f).setFoodGroup(EnumFoodGroup.Fruit)
                .setWaterRestoreRatio(1));

        DrinkRegistry.registerDrink(new Drink(TFCFluids.BEER, "Beer")
                .setCalories(107 / 250f).setFoodGroup(EnumFoodGroup.Grain)
                .setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.05f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.WHEATBEER, "WheatBeer")
                .setCalories(107 / 250f).setFoodGroup(EnumFoodGroup.Grain)
                .setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.05f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.RYEBEER, "RyeBeer")
                .setCalories(107 / 250f).setFoodGroup(EnumFoodGroup.Grain)
                .setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.05f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.CORNBEER, "CornBeer")
                .setCalories(107 / 250f).setFoodGroup(EnumFoodGroup.Grain)
                .setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.05f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.RICEBEER, "RiceBeer")
                .setCalories(107 / 250f).setFoodGroup(EnumFoodGroup.Grain)
                .setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.05f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.BARLEYWINE, "BarleyWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.WHEATWINE, "WheatWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.RYEWINE, "RyeWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.CORNWINE, "CornWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.CANEWINE, "CaneWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.AGAVEWINE, "AgaveWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.POTATOWINE, "PotatoWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.BERRYWINE, "BerryWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.11f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.FRUITWINE, "FruitWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PLUMWINE, "PlumWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PEACHWINE, "PeachWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.ORANGEWINE, "OrangeWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.LEMONWINE, "LemonWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.DATEWINE, "DateWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PAPAYAWINE, "PapayaWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.FIGWINE, "FigWine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.WINE, "Wine")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.14f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.SAKE, "Sake")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.25f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.MEAD, "Mead")
                .setCalories(123 / 250f)
                .setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.13f).setAlcoholTier(1));

        DrinkRegistry.registerDrink(new Drink(TFCFluids.APPLEJACK, "Applejack")
                .setAlcoholContent(0.2f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.RUM, "Rum")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.RYEWHISKEY, "RyeWhiskey")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.RICEWHISKEY, "RiceWhiskey")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.BARLEYWHISKEY, "BarleyWhiskey")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.SHOCHU, "Shochu")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.VODKA, "Vodka")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.TEQUILA, "Tequila")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.BERRYBRANDY, "BerryBrandy")
                .setAlcoholContent(0.35f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.FRUITBRANDY, "FruitBrandy")
                .setAlcoholContent(0.35f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PLUMBRANDY, "PlumBrandy")
                .setAlcoholContent(0.34f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PEACHBRANDY, "PeachBrandy")
                .setAlcoholContent(0.36f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.DATEBRANDY, "DateBrandy")
                .setAlcoholContent(0.35f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PAPAYABRANDY, "PapayaBrandy")
                .setAlcoholContent(0.34f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.FIGBRANDY, "FigBrandy")
                .setAlcoholContent(0.34f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.ORANGEBRANDY, "OrangeBrandy")
                .setAlcoholContent(0.36f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.LEMONBRANDY, "LemonBrandy")
                .setAlcoholContent(0.35f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.WHISKEY, "Whiskey")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.CORNWHISKEY, "CornWhiskey")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.HONEYBRANDY, "HoneyBrandy")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
    }

}
