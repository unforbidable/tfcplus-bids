package com.unforbidable.tfc.bids.compat.tfc;

import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.carvable.CarvableBrick;
import com.unforbidable.tfc.bids.compat.tfc.carvable.CarvableFireBrick;
import com.unforbidable.tfc.bids.compat.tfc.carvable.CarvableMudBrick;
import com.unforbidable.tfc.bids.compat.tfc.carvable.CarvablePlanks;
import com.unforbidable.tfc.bids.compat.tfc.carvable.CarvableRawStone;
import com.unforbidable.tfc.bids.compat.tfc.carvable.CarvableSmoothStone;
import com.unforbidable.tfc.bids.compat.tfc.carvable.CarvableStackedLogs;
import com.unforbidable.tfc.bids.compat.tfc.carvable.CarvableStoneBrick;
import com.unforbidable.tfc.bids.compat.tfc.carvable.CarvableStoneLargeBrick;
import com.unforbidable.tfc.bids.compat.tfc.carvable.CarvableWoodVert;
import com.unforbidable.tfc.bids.core.crafting.RecipeManager;
import com.unforbidable.tfc.bids.core.crafting.RecipeManagerSession;
import com.unforbidable.tfc.bids.core.drink.DrinkRegistry;
import com.unforbidable.tfc.bids.core.drink.registry.DrinkFluid;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.features.building.carving.CarvingRegistry;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

public class TfcSetup {

    public static void setupDrinks() {
        Bids.LOG.info("Register TFC drinks");

        DrinkRegistry.drinks.add(new DrinkFluid("FreshWater", TFCFluids.FRESHWATER, 1));

        DrinkRegistry.drinks.add(new DrinkFluid("Milk", TFCFluids.MILK,
            1, 0.584f, EnumFoodGroup.Dairy));

        DrinkRegistry.drinks.add(new DrinkFluid("GrapeJuice", TFCFluids.GRAPEJUICE,
            1, 152 / 250f, EnumFoodGroup.Fruit));
        DrinkRegistry.drinks.add(new DrinkFluid("AgaveJuice", TFCFluids.AGAVEJUICE,
            1, 50 / 250f, EnumFoodGroup.Vegetable));
        DrinkRegistry.drinks.add(new DrinkFluid("CaneJuice", TFCFluids.CANEJUICE,
            1, 269 / 250f));
        DrinkRegistry.drinks.add(new DrinkFluid("AppleJuice", TFCFluids.APPLEJUICE,
            1, 113 / 250f, EnumFoodGroup.Fruit));
        DrinkRegistry.drinks.add(new DrinkFluid("OrangeJuice", TFCFluids.ORANGEJUICE,
            1, 111 / 250f, EnumFoodGroup.Fruit));
        DrinkRegistry.drinks.add(new DrinkFluid("LemonJuice", TFCFluids.LEMONJUICE,
            1, 53 / 250f, EnumFoodGroup.Fruit));
        DrinkRegistry.drinks.add(new DrinkFluid("CherryJuice", TFCFluids.CHERRYJUICE,
            1, 119 / 250f, EnumFoodGroup.Fruit));
        DrinkRegistry.drinks.add(new DrinkFluid("PlumJuice", TFCFluids.PLUMJUICE,
            1, 182 / 250f, EnumFoodGroup.Fruit));
        DrinkRegistry.drinks.add(new DrinkFluid("PeachJuice", TFCFluids.PEACHJUICE,
            1, 134 / 250f, EnumFoodGroup.Fruit));
        DrinkRegistry.drinks.add(new DrinkFluid("DateJuice", TFCFluids.DATEJUICE,
            1, 152 / 250f, EnumFoodGroup.Fruit));
        DrinkRegistry.drinks.add(new DrinkFluid("PapayaJuice", TFCFluids.PAPAYAJUICE,
            1, 142 / 250f, EnumFoodGroup.Fruit));
        DrinkRegistry.drinks.add(new DrinkFluid("FigJuice", TFCFluids.FIGJUICE,
            1, 132 / 250f, EnumFoodGroup.Fruit));
        DrinkRegistry.drinks.add(new DrinkFluid("BerryJuice", TFCFluids.BERRYJUICE,
            1, 106 / 250f, EnumFoodGroup.Fruit));
        DrinkRegistry.drinks.add(new DrinkFluid("CoconutJuice", TFCFluids.COCONUTJUICE,
            1, 0.18f, EnumFoodGroup.Fruit));

        DrinkRegistry.drinks.add(new DrinkFluid("Beer", TFCFluids.BEER,
            0.5f, 107 / 250f, EnumFoodGroup.Grain, 1, 0.05f));
        DrinkRegistry.drinks.add(new DrinkFluid("WheatBeer", TFCFluids.WHEATBEER,
            0.5f, 107 / 250f, EnumFoodGroup.Grain, 1, 0.05f));
        DrinkRegistry.drinks.add(new DrinkFluid("RyeBeer", TFCFluids.RYEBEER,
            0.5f, 107 / 250f, EnumFoodGroup.Grain, 1, 0.05f));
        DrinkRegistry.drinks.add(new DrinkFluid("CornBeer", TFCFluids.CORNBEER,
            0.5f, 107 / 250f, EnumFoodGroup.Grain, 1, 0.05f));
        DrinkRegistry.drinks.add(new DrinkFluid("RiceBeer", TFCFluids.RICEBEER,
            0.5f, 107 / 250f, EnumFoodGroup.Grain, 1, 0.05f));

        DrinkRegistry.drinks.add(new DrinkFluid("BarleyWine", TFCFluids.BARLEYWINE,
            0.375f, 123 / 250f, 1, 0.08f));
        DrinkRegistry.drinks.add(new DrinkFluid("WheatWine", TFCFluids.WHEATWINE,
            0.375f, 123 / 250f, 1, 0.08f));
        DrinkRegistry.drinks.add(new DrinkFluid("RyeWine", TFCFluids.RYEWINE,
            0.375f, 123 / 250f, 1, 0.08f));
        DrinkRegistry.drinks.add(new DrinkFluid("CornWine", TFCFluids.CORNWINE,
            0.375f, 123 / 250f, 1, 0.08f));
        DrinkRegistry.drinks.add(new DrinkFluid("CaneWine", TFCFluids.CANEWINE,
            0.375f, 123 / 250f, 1, 0.08f));
        DrinkRegistry.drinks.add(new DrinkFluid("AgaveWine", TFCFluids.AGAVEWINE,
            0.375f, 123 / 250f, 1, 0.08f));
        DrinkRegistry.drinks.add(new DrinkFluid("PotatoWine", TFCFluids.POTATOWINE,
            0.375f, 123 / 250f, 1, 0.08f));
        DrinkRegistry.drinks.add(new DrinkFluid("BerryWine", TFCFluids.BERRYWINE,
            0.375f, 123 / 250f, 1, 0.11f));
        DrinkRegistry.drinks.add(new DrinkFluid("FruitWine", TFCFluids.FRUITWINE,
            0.375f, 123 / 250f, 1, 0.12f));
        DrinkRegistry.drinks.add(new DrinkFluid("PlumWine", TFCFluids.PLUMWINE,
            0.375f, 123 / 250f, 1, 0.12f));
        DrinkRegistry.drinks.add(new DrinkFluid("PeachWine", TFCFluids.PEACHWINE,
            0.375f, 123 / 250f, 1, 0.12f));
        DrinkRegistry.drinks.add(new DrinkFluid("OrangeWine", TFCFluids.ORANGEWINE,
            0.375f, 123 / 250f, 1, 0.12f));
        DrinkRegistry.drinks.add(new DrinkFluid("LemonWine", TFCFluids.LEMONWINE,
            0.375f, 123 / 250f, 1, 0.12f));
        DrinkRegistry.drinks.add(new DrinkFluid("DateWine", TFCFluids.DATEWINE,
            0.375f, 123 / 250f, 1, 0.12f));
        DrinkRegistry.drinks.add(new DrinkFluid("PapayaWine", TFCFluids.PAPAYAWINE,
            0.375f, 123 / 250f, 1, 0.12f));
        DrinkRegistry.drinks.add(new DrinkFluid("FigWine", TFCFluids.FIGWINE,
            0.375f, 123 / 250f, 1, 0.12f));
        DrinkRegistry.drinks.add(new DrinkFluid("Wine", TFCFluids.WINE,
            0.375f, 123 / 250f, 1, 0.14f));
        DrinkRegistry.drinks.add(new DrinkFluid("Sake", TFCFluids.SAKE,
            0.375f, 123 / 250f, 1, 0.12f));
        DrinkRegistry.drinks.add(new DrinkFluid("Mead", TFCFluids.MEAD,
            0.375f, 123 / 250f, 1, 0.13f));

        DrinkRegistry.drinks.add(new DrinkFluid("Applejack", TFCFluids.APPLEJACK,
            2, 0.2f));
        DrinkRegistry.drinks.add(new DrinkFluid("Rum", TFCFluids.RUM,
            2, 0.4f));
        DrinkRegistry.drinks.add(new DrinkFluid("RyeWhiskey", TFCFluids.RYEWHISKEY,
            2, 0.4f));
        DrinkRegistry.drinks.add(new DrinkFluid("RiceWhiskey", TFCFluids.RICEWHISKEY,
            2, 0.4f));
        DrinkRegistry.drinks.add(new DrinkFluid("BarleyWhiskey", TFCFluids.BARLEYWHISKEY,
            2, 0.4f));
        DrinkRegistry.drinks.add(new DrinkFluid("Shochu", TFCFluids.SHOCHU,
            2, 0.4f));
        DrinkRegistry.drinks.add(new DrinkFluid("Vodka", TFCFluids.VODKA,
            2, 0.4f));
        DrinkRegistry.drinks.add(new DrinkFluid("Tequila", TFCFluids.TEQUILA,
            2, 0.4f));
        DrinkRegistry.drinks.add(new DrinkFluid("BerryBrandy", TFCFluids.BERRYBRANDY,
            2, 0.35f));
        DrinkRegistry.drinks.add(new DrinkFluid("FruitBrandy", TFCFluids.FRUITBRANDY,
            2, 0.35f));
        DrinkRegistry.drinks.add(new DrinkFluid("PlumBrandy", TFCFluids.PLUMBRANDY,
            2, 0.34f));
        DrinkRegistry.drinks.add(new DrinkFluid("PeachBrandy", TFCFluids.PEACHBRANDY,
            2, 0.36f));
        DrinkRegistry.drinks.add(new DrinkFluid("DateBrandy", TFCFluids.DATEBRANDY,
            2, 0.35f));
        DrinkRegistry.drinks.add(new DrinkFluid("PapayaBrandy", TFCFluids.PAPAYABRANDY,
            2, 0.34f));
        DrinkRegistry.drinks.add(new DrinkFluid("FigBrandy", TFCFluids.FIGBRANDY,
            2, 0.34f));
        DrinkRegistry.drinks.add(new DrinkFluid("OrangeBrandy", TFCFluids.ORANGEBRANDY,
            2, 0.36f));
        DrinkRegistry.drinks.add(new DrinkFluid("LemonBrandy", TFCFluids.LEMONBRANDY,
            2, 0.35f));
        DrinkRegistry.drinks.add(new DrinkFluid("Whiskey", TFCFluids.WHISKEY,
            2, 0.4f));
        DrinkRegistry.drinks.add(new DrinkFluid("CornWhiskey", TFCFluids.CORNWHISKEY,
            2, 0.4f));
        DrinkRegistry.drinks.add(new DrinkFluid("HoneyBrandy", TFCFluids.HONEYBRANDY,
            2, 0.4f));
    }

    public static void setupGlassblowing() {
        Bids.LOG.info("Setup glassblowing for TFC");

        TfcRegistry.Recipes.knapping.add(KnappingRecipe.add(new ItemStack(TFCItems.glassBottle),
            " # # ", " # # ", "#   #", "#   #", " ### ", '#', new ItemStack(BidsItems.flatGlass)));
    }

    public static void setupCarving() {
        Bids.LOG.info("Setup carving for TFC blocks");

        CarvingRegistry.carvable.add(new CarvableRawStone());
        CarvingRegistry.carvable.add(new CarvableStackedLogs());
        CarvingRegistry.carvable.add(new CarvableWoodVert());
        CarvingRegistry.carvable.add(new CarvablePlanks());
        CarvingRegistry.carvable.add(new CarvableStoneBrick());
        CarvingRegistry.carvable.add(new CarvableStoneLargeBrick());
        CarvingRegistry.carvable.add(new CarvableSmoothStone());
        CarvingRegistry.carvable.add(new CarvableBrick());
        CarvingRegistry.carvable.add(new CarvableMudBrick());
        CarvingRegistry.carvable.add(new CarvableFireBrick());
    }

    public static void setupRecipes() {
        RecipeManagerSession recipes = RecipeManager.getSession();

        // Select TFC recipes where new cordage and twines can be used
        recipes.addShapedRecipe(new ItemStack(TFCBlocks.primitiveLoom),
            "LS", "SL", 'L', "stickWood", 'S', "materialBindingStrong");
        recipes.addShapedRecipe(new ItemStack(TFCBlocks.primitiveLoom),
            "LS", "SL", 'S', "stickWood", 'L', "materialBindingStrong");
        recipes.addShapelessRecipe(new ItemStack(TFCItems.unstrungBow),
                TFCItems.pole, "itemKnife", "materialBindingStrong")
            .action(damageTool("itemKnife"));
        recipes.addShapelessRecipe(new ItemStack(TFCItems.bow),
            TFCItems.unstrungBow, "materialBindingStrong");
        recipes.addShapelessRecipe(new ItemStack(TFCItems.splint),
            TFCItems.stick, "materialBindingStrong");
        recipes.addShapelessRecipe(new ItemStack(TFCItems.compositeBow),
            TFCItems.unstrungCompositeBow, "materialBindingStrong");

        recipes.flush();
    }

}
