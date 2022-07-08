package com.unforbidable.tfc.bids;

import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRaw;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Crafting.CraftingManagerTFC;
import com.dunk.tfc.api.Crafting.KilnCraftingManager;
import com.dunk.tfc.api.Crafting.KilnRecipe;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.unforbidable.tfc.bids.Blocks.BlockClayCrucible;
import com.unforbidable.tfc.bids.Blocks.BlockFireClayCrucible;
import com.unforbidable.tfc.bids.Blocks.BlockMudChimney;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;
import com.unforbidable.tfc.bids.Core.Drinks.Drink;
import com.unforbidable.tfc.bids.Core.Drinks.DrinkHelper;
import com.unforbidable.tfc.bids.Handlers.ConfigHandler;
import com.unforbidable.tfc.bids.Handlers.CraftingHandler;
import com.unforbidable.tfc.bids.Handlers.GuiHandler;
import com.unforbidable.tfc.bids.Handlers.WorldEventHandler;
import com.unforbidable.tfc.bids.Items.ItemOreBit;
import com.unforbidable.tfc.bids.Items.ItemFlatGlass;
import com.unforbidable.tfc.bids.Items.ItemGenericPottery;
import com.unforbidable.tfc.bids.Items.ItemDrinkingGlass;
import com.unforbidable.tfc.bids.Items.ItemMetalBlowpipe;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemClayCrucible;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemFireClayCrucible;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemMudChimney;
import com.unforbidable.tfc.bids.Recipes.RecipeCrucibleConversion;
import com.unforbidable.tfc.bids.TileEntities.TileEntityChimney;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayCrucible;
import com.unforbidable.tfc.bids.TileEntities.TileEntityFireClayCrucible;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.DrinkRegistry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new ConfigHandler(event.getModConfigurationDirectory()));

        GameRegistry.registerTileEntity(TileEntityClayCrucible.class, "BidsClayCrucible");
        GameRegistry.registerTileEntity(TileEntityFireClayCrucible.class, "BidsFireClayCrucible");
        GameRegistry.registerTileEntity(TileEntityChimney.class, "BidsChimney");

        BidsBlocks.clayCrucible = new BlockClayCrucible().setBlockName("ClayCrucible")
                .setHardness(BidsOptions.Crucible.enableClayHandBreakable ? 0.5f : 4.0f);
        BidsBlocks.fireClayCrucible = new BlockFireClayCrucible().setBlockName("FireClayCrucible")
                .setHardness(BidsOptions.Crucible.enableFireClayHandBreakable ? 0.5f : 4.0f);
        BidsBlocks.mudBrickChimney = new BlockMudChimney(0).setDirt(TFCBlocks.dirt)
                .setBlockName("MudBrickChimney");
        BidsBlocks.mudBrickChimney2 = new BlockMudChimney(16).setDirt(TFCBlocks.dirt2)
                .setBlockName("MudBrickChimney2");

        GameRegistry.registerBlock(BidsBlocks.clayCrucible, ItemClayCrucible.class, "ClayCrucible");
        GameRegistry.registerBlock(BidsBlocks.fireClayCrucible, ItemFireClayCrucible.class, "FireClayCrucible");
        GameRegistry.registerBlock(BidsBlocks.mudBrickChimney, ItemMudChimney.class, "MudBrickChimney");
        GameRegistry.registerBlock(BidsBlocks.mudBrickChimney2, ItemMudChimney.class, "MudBrickChimney2");

        BidsItems.oreBit = new ItemOreBit().setUnlocalizedName("Ore Bit");
        BidsItems.metalBlowpipe = new ItemMetalBlowpipe().setUnlocalizedName("Metal Blowpipe");
        BidsItems.flatGlass = new ItemFlatGlass().setUnlocalizedName("Flat Glass");
        BidsItems.drinkingGlass = new ItemDrinkingGlass().setGlassReturnAmount(40).setMaxStackSize(4)
                .setUnlocalizedName("Drinking Glass");
        BidsItems.glassJug = new ItemDrinkingGlass().setGlassReturnAmount(80).setMaxStackSize(4)
                .setUnlocalizedName("Glass Jug");
        BidsItems.shotGlass = new ItemDrinkingGlass().setGlassReturnAmount(20).setMaxStackSize(4)
                .setUnlocalizedName("Shot Glass");
        BidsItems.clayPipe = new ItemGenericPottery(true).setUnlocalizedName("Pottery Pipe");

        GameRegistry.registerItem(BidsItems.oreBit, BidsItems.oreBit.getUnlocalizedName());
        GameRegistry.registerItem(BidsItems.metalBlowpipe, BidsItems.metalBlowpipe.getUnlocalizedName());
        GameRegistry.registerItem(BidsItems.flatGlass, BidsItems.flatGlass.getUnlocalizedName());
        GameRegistry.registerItem(BidsItems.drinkingGlass, BidsItems.drinkingGlass.getUnlocalizedName());
        GameRegistry.registerItem(BidsItems.glassJug, BidsItems.glassJug.getUnlocalizedName());
        GameRegistry.registerItem(BidsItems.shotGlass, BidsItems.shotGlass.getUnlocalizedName());
        GameRegistry.registerItem(BidsItems.clayPipe, BidsItems.clayPipe.getUnlocalizedName());

        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new GuiHandler());

        FMLCommonHandler.instance().bus().register(new CraftingHandler());

        DrinkRegistry.registerDrink(new Drink(TFCFluids.FRESHWATER, "FreshWater")
                .setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.GRAPEJUICE, "GrapeJuice")
                .setCalories(152 / 250f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.AGAVEJUICE, "AgaveJuice")
                .setCalories(50 / 250f).setFoodGroup(EnumFoodGroup.Vegetable).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.CANEJUICE, "CaneJuice")
                .setCalories(269 / 250f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.APPLEJUICE, "AppleJuice")
                .setCalories(113 / 250f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.ORANGEJUICE, "OrangeJuice")
                .setCalories(111 / 250f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.LEMONJUICE, "LemonJuice")
                .setCalories(53 / 250f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.CHERRYJUICE, "CherryJuice")
                .setCalories(119 / 250f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PLUMJUICE, "PlumJuice")
                .setCalories(182 / 250f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PEACHJUICE, "PeachJuice")
                .setCalories(134 / 250f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.DATEJUICE, "DateJuice")
                .setCalories(152 / 250f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PAPAYAJUICE, "PapayaJuice")
                .setCalories(142 / 250f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.FIGJUICE, "FigJuice")
                .setCalories(132 / 250f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.BERRYJUICE, "BerryJuice")
                .setCalories(106 / 250f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.BEER, "Beer")
                .setCalories(107 / 250f).setFoodGroup(EnumFoodGroup.Grain).setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.05f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.WHEATBEER, "WheatBeer")
                .setCalories(107 / 250f).setFoodGroup(EnumFoodGroup.Grain).setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.05f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.RYEBEER, "RyeBeer")
                .setCalories(107 / 250f).setFoodGroup(EnumFoodGroup.Grain).setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.05f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.CORNBEER, "CornBeer")
                .setCalories(107 / 250f).setFoodGroup(EnumFoodGroup.Grain).setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.05f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.RICEBEER, "RiceBeer")
                .setCalories(107 / 250f).setFoodGroup(EnumFoodGroup.Grain).setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.05f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.BARLEYWINE, "BarleyWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.WHEATWINE, "WheatWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.RYEWINE, "RyeWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.CORNWINE, "CornWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.CANEWINE, "CaneWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.AGAVEWINE, "AgaveWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.POTATOWINE, "PotatoWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.08f).setAlcoholTier(1));
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
        DrinkRegistry.registerDrink(new Drink(TFCFluids.SAKE, "Sake")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.25f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.SHOCHU, "Shochu")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.VODKA, "Vodka")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.TEQUILA, "Tequila")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.BERRYWINE, "BerryWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.11f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.FRUITWINE, "FruitWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PLUMWINE, "PlumWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PEACHWINE, "PeachWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.ORANGEWINE, "OrangeWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.LEMONWINE, "LemonWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.DATEWINE, "DateWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.PAPAYAWINE, "PapayaWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.FIGWINE, "FigWine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.12f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.WINE, "Wine")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.375f)
                .setAlcoholContent(0.14f).setAlcoholTier(1));
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
        DrinkRegistry.registerDrink(new Drink(TFCFluids.MEAD, "Mead")
                .setCalories(123 / 250f).setWaterRestoreRatio(0.5f)
                .setAlcoholContent(0.13f).setAlcoholTier(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.HONEYBRANDY, "HoneyBrandy")
                .setAlcoholContent(0.4f).setAlcoholTier(2));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.MILK, "Milk")
                .setCalories(0.584f).setFoodGroup(EnumFoodGroup.Dairy).setWaterRestoreRatio(1));
        DrinkRegistry.registerDrink(new Drink(TFCFluids.COCONUTJUICE, "CoconutJuice")
                .setCalories(0.18f).setFoodGroup(EnumFoodGroup.Fruit).setWaterRestoreRatio(1));
    }

    public void init(FMLInitializationEvent event) {
        FMLInterModComms.sendMessage("Waila", "register",
                "com.unforbidable.tfc.bids.WAILA.WailaCrucible.callbackRegister");

        DrinkHelper.registerFluidContainers(BidsItems.drinkingGlass, 250, true);
        DrinkHelper.registerFluidContainers(BidsItems.glassJug, 2000, true);
        DrinkHelper.registerFluidContainers(BidsItems.shotGlass, 50, false);

        // Hammers that are able to break iron ores into bits
        // You could realstically break iron ore with a stone hammer
        // so this is for ballance only
        // Also breaking iron ore will cause more damage to the hammer
        OreDictionary.registerOre("itemHammerIronBits",
                new ItemStack(TFCItems.steelHammer, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("itemHammerIronBits",
                new ItemStack(TFCItems.blackSteelHammer, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("itemHammerIronBits",
                new ItemStack(TFCItems.blueSteelHammer, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("itemHammerIronBits",
                new ItemStack(TFCItems.redSteelHammer, 1, OreDictionary.WILDCARD_VALUE));

        GameRegistry.addRecipe(new RecipeCrucibleConversion(true));
        GameRegistry.addRecipe(new RecipeCrucibleConversion(false));

        for (int i = 0; i < Global.ORE_METAL.length; i++) {
            ItemStack small = new ItemStack(TFCItems.smallOreChunk, 1, i);
            ItemStack poor = new ItemStack(TFCItems.oreChunk, 1, Global.oreGrade2Offset + i);
            ItemStack normal = new ItemStack(TFCItems.oreChunk, 1, i);
            ItemStack rich = new ItemStack(TFCItems.oreChunk, 1, Global.oreGrade1Offset + i);

            if (CrucibleHelper.isOreIron(small)) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 2, i),
                        small, "itemHammerIronBits"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 3, i),
                        poor, "itemHammerIronBits"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 5, i),
                        normal, "itemHammerIronBits"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 7, i),
                        rich, "itemHammerIronBits"));
            } else {
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 2, i),
                        small, "itemHammer"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 3, i),
                        poor, "itemHammer"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 5, i),
                        normal, "itemHammer"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 7, i),
                        rich, "itemHammer"));
            }
        }

        // This recipe is meant to upgrade an obsolete version 0.5.0 metal blowpipe
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.metalBlowpipe, 1, 1),
                new ItemStack(BidsItems.metalBlowpipe, 1, 0)));

        ItemStack clayTile = new ItemStack(TFCItems.clayTile, 1, 0);
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.clayPipe, 1, 0), clayTile, clayTile);

        for (int j = 0; j < Global.STONE_ALL.length; j++) {
            GameRegistry.addRecipe(
                    new ItemStack(j < 16 ? BidsBlocks.mudBrickChimney : BidsBlocks.mudBrickChimney2, 2, j % 16),
                    "PB", "BB", 'P', new ItemStack(BidsItems.clayPipe, 1, 1),
                    'B', new ItemStack(TFCItems.mudBrick, 1, j));
            GameRegistry.addRecipe(
                    new ItemStack(j < 16 ? BidsBlocks.mudBrickChimney : BidsBlocks.mudBrickChimney2, 2, j % 16),
                    "PB", "BB", 'P', new ItemStack(TFCItems.logs, 1, 48), // Bamboo
                    'B', new ItemStack(TFCItems.mudBrick, 1, j));
        }

    }

    public void postInit(FMLPostInitializationEvent event) {
        HeatRegistry manager = HeatRegistry.getInstance();

        for (int i = 0; i < Global.ORE_METAL.length; i++) {
            ItemStack smallOre = new ItemStack(TFCItems.smallOreChunk, 1, i);
            HeatIndex smallOreIndex = manager.findMatchingIndex(smallOre);
            if (smallOreIndex != null) {
                HeatRaw raw = new HeatRaw(smallOreIndex.specificHeat, smallOreIndex.meltTemp);

                ItemStack oreBit = new ItemStack(BidsItems.oreBit, 1, i);
                manager.addIndex(new HeatIndex(oreBit, raw, new ItemStack(smallOreIndex.getOutputItem(), 1)));
                Bids.LOG.info("Registered heat index for: " + oreBit.getDisplayName());
            }
        }

        if (BidsOptions.Crucible.enableClassicHandBreakable) {
            // Lower the hardness of the classic TFC crucible
            // The original value is 4.0f
            Bids.LOG.info("Classic TFC crucible hardness reduced");
            TFCBlocks.crucible.setHardness(0.5f);
        }

        Bids.LOG.info("Registering metal blowpipe as valid glass mold");
        Global.GLASS.addValidPartialMold(BidsItems.metalBlowpipe, 2, BidsItems.metalBlowpipe, 1, 2);

        Bids.LOG.info("Registering crucible clay knapping recipes");
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.clayCrucible, 1),
                new Object[] { "#####", " ### ", " ### ", " ### ", "     ", '#',
                        new ItemStack(TFCItems.flatClay, 1, 1) });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.fireClayCrucible, 1),
                new Object[] { "#####", " ### ", " ### ", " ### ", "     ", '#',
                        new ItemStack(TFCItems.flatClay, 1, 3) });

        Bids.LOG.info("Registering glass knapping recipes");
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(TFCItems.glassBottle, 1),
                new Object[] { " # # ", " # # ", "#   #", "#   #", " ### ", '#',
                        new ItemStack(BidsItems.flatGlass, 1) });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.drinkingGlass, 2),
                new Object[] { "     ", "     ", "#   #", "#   #", "#####", '#',
                        new ItemStack(BidsItems.flatGlass, 1) });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.shotGlass, 4),
                new Object[] { "     ", "     ", " # # ", " # # ", " ### ", '#',
                        new ItemStack(BidsItems.flatGlass, 1) });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.glassJug, 1),
                new Object[] { " #   ", "# ## ", "# # #", "# ## ", "###  ", '#',
                        new ItemStack(BidsItems.flatGlass, 1) });

        KilnCraftingManager.getInstance().addRecipe(
                new KilnRecipe(new ItemStack(BidsItems.clayPipe, 1, 0), 0,
                        new ItemStack(BidsItems.clayPipe, 1, 1)));

        // Anvil recipes are registered when world loads
        // ideally after TFC initialized its AnvilManager
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
    }

}
