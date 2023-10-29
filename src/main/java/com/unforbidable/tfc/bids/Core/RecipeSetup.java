package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Crafting.*;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Enums.RuleEnum;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Blocks.BlockUnfinishedAnvil;
import com.unforbidable.tfc.bids.Core.Cooking.CookingHelper;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;
import com.unforbidable.tfc.bids.Core.Recipes.Actions.*;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeManager;
import com.unforbidable.tfc.bids.Core.Recipes.TFC.BarrelItemDemandingRecipe;
import com.unforbidable.tfc.bids.Core.Seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.Handlers.CraftingHandler;
import com.unforbidable.tfc.bids.Recipes.RecipeCrucibleConversion;
import com.unforbidable.tfc.bids.Recipes.RecipeEmptyCookingPot;
import com.unforbidable.tfc.bids.api.*;
import com.unforbidable.tfc.bids.api.Crafting.*;
import com.unforbidable.tfc.bids.api.Enums.EnumWoodHardness;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Arrays;

import static com.dunk.tfc.Core.Recipes.getStackNoTemp;

public class RecipeSetup {

    public static void init() {
        registerOre();
        registerRecipes();
        registerCustomRecipes();
        registerCarvingRecipes();
        registerSaddleQuernRecipes();
        registerStonePressRecipes();
        registerCookingRecipes();
        registerPrepRecipes();
        registerHandlers();
    }

    public static void postInit() {
        registerKnappingRecipes();
        registerSewingRecipes();
        registerKilnRecipes();
        registerBarrelRecipes();
    }

    public static void onServerWorldLoad() {
        registerAnvilRecipes();
        registerSewingRepairRecipes();
    }

    public static void onClientWorldInit() {
        registerAnvilRecipes();
        registerSewingRepairRecipes();
    }

    private static void registerOre() {
        Bids.LOG.info("Register recipe ores");

        final int WILD = OreDictionary.WILDCARD_VALUE;

        // Hammers that are able to break iron ores into bits
        // You could realstically break iron ore with a stone hammer
        // so this is for ballance only
        // Also breaking iron ore will cause more damage to the hammer
        OreDictionary.registerOre("itemHammerIronBits", new ItemStack(TFCItems.steelHammer, 1, WILD));
        OreDictionary.registerOre("itemHammerIronBits", new ItemStack(TFCItems.blackSteelHammer, 1, WILD));
        OreDictionary.registerOre("itemHammerIronBits", new ItemStack(TFCItems.blueSteelHammer, 1, WILD));
        OreDictionary.registerOre("itemHammerIronBits", new ItemStack(TFCItems.redSteelHammer, 1, WILD));

        for (ItemStack is : OreDictionary.getOres("materialString")) {
            OreDictionary.registerOre("materialStringAny", is);
            OreDictionary.registerOre("materialStringDecent", is);
            OreDictionary.registerOre("materialStringStrong", is);
        }

        OreDictionary.registerOre("materialStringAny", TFCItems.grassCordage);
        OreDictionary.registerOre("materialStringAny", TFCItems.sinew);
        OreDictionary.registerOre("materialStringAny", new ItemStack(BidsItems.barkFibreStrip, 1, 0));
        OreDictionary.registerOre("materialStringAny", new ItemStack(BidsItems.barkFibreStrip, 1, 1));
        OreDictionary.registerOre("materialStringAny", BidsItems.barkCordage);

        OreDictionary.registerOre("materialStringDecent", TFCItems.sinew);
        OreDictionary.registerOre("materialStringDecent", BidsItems.barkCordage);

        OreDictionary.registerOre("materialStringStrong", BidsItems.barkCordage);

        for (int i = 0; i < Global.WOOD_ALL.length; i++) {
            int j = i % 16;
            int o = i / 16 * 16;

            if (WoodHelper.canMakeChoppingBlock(i)) {
                if (i < 16) {
                    OreDictionary.registerOre("blockChoppingBlock", new ItemStack(BidsBlocks.choppingBlock, 1, j));
                } else if (i < 32) {
                    OreDictionary.registerOre("blockChoppingBlock", new ItemStack(BidsBlocks.choppingBlock2, 1, j));
                } else {
                    OreDictionary.registerOre("blockChoppingBlock", new ItemStack(BidsBlocks.choppingBlock3, 1, j));
                }
            }

            if (WoodHelper.canBuildLogWall(i)) {
                OreDictionary.registerOre("blockLogWall",
                        new ItemStack(WoodHelper.getDefaultLogWallBlock(o), 1, j));
                OreDictionary.registerOre("blockLogWall",
                        new ItemStack(WoodHelper.getDefaultLogWallVertBlock(o), 1, j));
            }

        }

        OreDictionary.registerOre("itemHoneycomb", TFCItems.honeycomb);
        OreDictionary.registerOre("itemHoneycomb", TFCItems.fertileHoneycomb);
    }

    private static void registerCustomRecipes() {
        Bids.LOG.info("Register custom recipes");

        // TODO: register with net.minecraftforge.oredict.RecipeSorter
        GameRegistry.addRecipe(new RecipeCrucibleConversion(true));
        GameRegistry.addRecipe(new RecipeCrucibleConversion(false));
        GameRegistry.addRecipe(new RecipeEmptyCookingPot());
    }

    private static void registerRecipes() {
        Bids.LOG.info("Register standard recipes");

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

        RecipeManager.addAction(new ActionToolDamageOreBit()
                .addTools("itemHammer")
                .matchCraftingItem(BidsItems.oreBit));

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

        for (int j = 0; j < Global.STONE_SED.length; j++) {
            if (j == 0 || j == 4) {
                // Shale and Sandstone can split into tiles
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneTile, 4, Global.STONE_SED_START + j),
                    "SA", "  ", 'S', new ItemStack(BidsBlocks.roughStoneSed, 1, j), 'A', "itemAdze"));
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneTile, 4, Global.STONE_SED_START + j),
                    "AS", "  ", 'S', new ItemStack(BidsBlocks.roughStoneSed, 1, j), 'A', "itemAdze"));

                GameRegistry.addRecipe(
                    new ItemStack(BidsBlocks.roughStoneTileSed, 2, j),
                    "BB", "BB", 'B', new ItemStack(BidsItems.roughStoneTile, 1, Global.STONE_SED_START + j));
            }

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneBrick, 4, Global.STONE_SED_START + j),
                "A ", "S ", 'S', new ItemStack(BidsBlocks.roughStoneSed, 1, j), 'A', "itemAdze"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneBrick, 4, Global.STONE_SED_START + j),
                "S ", "A ", 'S', new ItemStack(BidsBlocks.roughStoneSed, 1, j), 'A', "itemAdze"));

            GameRegistry.addRecipe(
                new ItemStack(BidsBlocks.roughStoneBrickSed, 2, j),
                "BB", "BB", 'B', new ItemStack(BidsItems.roughStoneBrick, 1, Global.STONE_SED_START + j));

            // Convert obsolete sedimentary rough bricks
            GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.roughStoneBrick, 1, Global.STONE_SED_START + j), new ItemStack(BidsItems.sedRoughStoneLooseBrick, 1, j));
        }

        for (int j = 0; j < Global.STONE_MM.length; j++) {
            if (j == 1) {
                // Slate can split into tiles
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneTile, 4, Global.STONE_MM_START + j),
                    "SA", "  ", 'S', new ItemStack(BidsBlocks.roughStoneMM, 1, j), 'A', "itemAdzeMetal"));
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneTile, 4, Global.STONE_MM_START + j),
                    "AS", "  ", 'S', new ItemStack(BidsBlocks.roughStoneMM, 1, j), 'A', "itemAdzeMetal"));

                GameRegistry.addRecipe(
                    new ItemStack(BidsBlocks.roughStoneTileMM, 2, j),
                    "BB", "BB", 'B', new ItemStack(BidsItems.roughStoneTile, 1, Global.STONE_MM_START + j));
            }

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneBrick, 4, Global.STONE_MM_START + j),
                "A ", "S ", 'S', new ItemStack(BidsBlocks.roughStoneMM, 1, j), 'A', "itemAdzeMetal"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneBrick, 4, Global.STONE_MM_START + j),
                "S ", "A ", 'S', new ItemStack(BidsBlocks.roughStoneMM, 1, j), 'A', "itemAdzeMetal"));

            GameRegistry.addRecipe(
                new ItemStack(BidsBlocks.roughStoneBrickMM, 2, j),
                "BB", "BB", 'B', new ItemStack(BidsItems.roughStoneBrick, 1, Global.STONE_MM_START + j));
        }

        for (int j = 0; j < Global.STONE_IGIN.length; j++) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneBrick, 4, Global.STONE_IGIN_START + j),
                "A ", "S ", 'S', new ItemStack(BidsBlocks.roughStoneIgIn, 1, j), 'A', "itemAdzeMetal"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneBrick, 4, Global.STONE_IGIN_START + j),
                "S ", "A ", 'S', new ItemStack(BidsBlocks.roughStoneIgIn, 1, j), 'A', "itemAdzeMetal"));

            GameRegistry.addRecipe(
                new ItemStack(BidsBlocks.roughStoneBrickIgIn, 2, j),
                "BB", "BB", 'B', new ItemStack(BidsItems.roughStoneBrick, 1, Global.STONE_IGIN_START + j));
        }

        for (int j = 0; j < Global.STONE_IGEX.length; j++) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneBrick, 4, Global.STONE_IGEX_START + j),
                "A ", "S ", 'S', new ItemStack(BidsBlocks.roughStoneIgEx, 1, j), 'A', "itemAdzeMetal"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.roughStoneBrick, 4, Global.STONE_IGEX_START + j),
                "S ", "A ", 'S', new ItemStack(BidsBlocks.roughStoneIgEx, 1, j), 'A', "itemAdzeMetal"));

            GameRegistry.addRecipe(
                new ItemStack(BidsBlocks.roughStoneBrickIgEx, 2, j),
                "BB", "BB", 'B', new ItemStack(BidsItems.roughStoneBrick, 1, Global.STONE_IGEX_START + j));
        }

        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemAdze")
            .matchCraftingItem(BidsItems.roughStoneBrick));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemAdze")
            .matchCraftingItem(BidsItems.roughStoneTile));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.igInStoneDrill, 1, 0),
                BidsItems.igInStoneDrillHead, "stickWood", TFCItems.bow));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.sedStoneDrill, 1, 0),
                BidsItems.sedStoneDrillHead, "stickWood", TFCItems.bow));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.igExStoneDrill, 1, 0),
                BidsItems.igExStoneDrillHead, "stickWood", TFCItems.bow));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.mMStoneDrill, 1, 0),
                BidsItems.mMStoneDrillHead, "stickWood", TFCItems.bow));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.igInStoneAdze, 1, 0),
                "1", "2", '1', BidsItems.igInStoneAdzeHead, '2', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.sedStoneAdze, 1, 0),
                "1", "2", '1', BidsItems.sedStoneAdzeHead, '2', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.igExStoneAdze, 1, 0),
                "1", "2", '1', BidsItems.igExStoneAdzeHead, '2', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.mMStoneAdze, 1, 0),
                "1", "2", '1', BidsItems.mMStoneAdzeHead, '2', "stickWood"));

        GameRegistry.addRecipe(new ItemStack(BidsItems.igInStoneAdze, 1, 0),
                "1", "2", '1', BidsItems.igInStoneAdzeHead, '2', new ItemStack(TFCItems.bone));
        GameRegistry.addRecipe(new ItemStack(BidsItems.sedStoneAdze, 1, 0),
                "1", "2", '1', BidsItems.sedStoneAdzeHead, '2', new ItemStack(TFCItems.bone));
        GameRegistry.addRecipe(new ItemStack(BidsItems.igExStoneAdze, 1, 0),
                "1", "2", '1', BidsItems.igExStoneAdzeHead, '2', new ItemStack(TFCItems.bone));
        GameRegistry.addRecipe(new ItemStack(BidsItems.mMStoneAdze, 1, 0),
                "1", "2", '1', BidsItems.mMStoneAdzeHead, '2', new ItemStack(TFCItems.bone));

        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.copperAdzeHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 2)));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.bronzeAdzeHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 3)));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.bismuthBronzeAdzeHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 4)));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.blackBronzeAdzeHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 5)));

        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.copperDrillHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 2)));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.bronzeDrillHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 3)));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.bismuthBronzeDrillHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 4)));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.blackBronzeDrillHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 5)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.copperAdze, 1), "#", "I", '#', new ItemStack(BidsItems.copperAdzeHead, 1, 0), 'I', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.bronzeAdze, 1), "#", "I", '#', new ItemStack(BidsItems.bronzeAdzeHead, 1, 0), 'I', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.bismuthBronzeAdze, 1), "#", "I", '#', new ItemStack(BidsItems.bismuthBronzeAdzeHead, 1, 0), 'I', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.blackBronzeAdze, 1), "#", "I", '#', new ItemStack(BidsItems.blackBronzeAdzeHead, 1, 0), 'I', "stickWood"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.copperDrill, 1, 0),
            BidsItems.copperDrillHead, "stickWood", TFCItems.bow));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.bronzeDrill, 1, 0),
            BidsItems.bronzeDrillHead, "stickWood", TFCItems.bow));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.bismuthBronzeDrill, 1, 0),
            BidsItems.bismuthBronzeDrillHead, "stickWood", TFCItems.bow));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.blackBronzeDrill, 1, 0),
            BidsItems.blackBronzeDrillHead, "stickWood", TFCItems.bow));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.smallStickBundle),
                "stickWood", "stickWood", "stickWood"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.stick, 3, 0),
                new ItemStack(BidsItems.smallStickBundle)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.kindling),
                "stickWood", "stickWood", "stickWood", new ItemStack(TFCItems.straw)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.kindling),
                new ItemStack(BidsItems.smallStickBundle), new ItemStack(TFCItems.straw)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.barkFibreKindling),
                "stickWood", "stickWood", "stickWood", new ItemStack(BidsItems.barkFibreStrip, 1, 1)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.barkFibreKindling),
                new ItemStack(BidsItems.smallStickBundle), new ItemStack(BidsItems.barkFibreStrip, 1, 1)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.birchBarkKindling),
                "stickWood", "stickWood", "stickWood", new ItemStack(BidsItems.birchBarkStrap)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.birchBarkKindling),
                new ItemStack(BidsItems.smallStickBundle), new ItemStack(BidsItems.birchBarkStrap)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.tiedStickBundle),
                new ItemStack(BidsItems.smallStickBundle), new ItemStack(BidsItems.smallStickBundle),
                new ItemStack(BidsItems.smallStickBundle), "materialStringAny"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.tiedStickBundle),
                new ItemStack(TFCItems.stickBundle), "materialStringAny"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.stick, 9, 0),
                new ItemStack(BidsItems.tiedStickBundle)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.dryingRack),
                "PP", "SS", 'P', new ItemStack(TFCItems.pole),
                'S', "materialStringDecent"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 2, 0),
                new ItemStack(BidsBlocks.dryingRack)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.barkFibreStrip, 4, 0),
                new ItemStack(BidsItems.barkFibre, 1, 0), "itemKnife"));

        GameRegistry.addRecipe(new ItemStack(TFCItems.rope),
                "11", "11", '1', BidsItems.barkCordage);

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.birchBarkCup, 1, 0),
                BidsItems.birchBarkCupUnfinished, Items.slime_ball));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.birchBarkSheet, 1, 0),
                new ItemStack(BidsItems.bark, 1, 2), "itemKnife"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.wattleTrapdoor),
                "PW", "  ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.wattleTrapdoor),
                "WP", "  ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.wattleTrapdoor),
                "  ", "PW", 'P', TFCItems.pole, 'W', TFCBlocks.wattle));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.wattleTrapdoor),
                "  ", "WP", 'P', TFCItems.pole, 'W', TFCBlocks.wattle));

        // Select TFC recipes where bark cordage and bark fiber strips can be used
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.primitiveLoom, 1, 0),
                "LS", "SL", 'L', "stickWood", 'S', BidsItems.barkCordage));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.primitiveLoom, 1, 0),
                "LS", "SL", 'S', "stickWood", 'L', BidsItems.barkCordage));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.unstrungBow, 1),
                new ItemStack(TFCItems.pole), "itemKnife", new ItemStack(BidsItems.barkCordage)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.bow, 1),
                new ItemStack(TFCItems.unstrungBow), new ItemStack(BidsItems.barkCordage)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.splint, 1),
                TFCItems.stick, BidsItems.barkCordage));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.splint, 1),
                TFCItems.stick, new ItemStack(BidsItems.barkFibreStrip, 1, 0)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.splint, 1),
                TFCItems.stick, new ItemStack(BidsItems.barkFibreStrip, 1, 1)));

        for (int i = 0; i < Global.WOOD_ALL.length; i++) {
            int j = i % 16;

            if (WoodHelper.canPeelLog(i)) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.peeledLog, 1, i),
                        new ItemStack(TFCItems.logs, 1, i * 2), "itemAdze"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.peeledLog, 1, i),
                        new ItemStack(TFCItems.logs, 1, i * 2 + 1), "itemAdze"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                        new ItemStack(BidsItems.logsSeasoned, 1, i * 2), "itemAdze"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                        new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1), "itemAdze"));

                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
                        new ItemStack(BidsItems.peeledLog, 1, i),
                        new ItemStack(TFCItems.logs, 1, i * 2)));
                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
                        new ItemStack(BidsItems.peeledLog, 1, i),
                        new ItemStack(TFCItems.logs, 1, i * 2 + 1)));
                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
                        new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                        new ItemStack(BidsItems.logsSeasoned, 1, i * 2)));
                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
                        new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                        new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1)));

                SeasoningManager.addRecipe(new SeasoningRecipe(new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                        new ItemStack(BidsItems.peeledLog, 1, i),
                        SeasoningHelper.getWoodSeasoningDuration(i) - 2));
                SeasoningManager.addRecipe(new SeasoningRecipe(new ItemStack(BidsItems.logsSeasoned, 1, i * 2),
                        new ItemStack(TFCItems.logs, 1, i * 2),
                        SeasoningHelper.getWoodSeasoningDuration(i)));
                SeasoningManager.addRecipe(new SeasoningRecipe(new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1),
                        new ItemStack(TFCItems.logs, 1, i * 2 + 1),
                        SeasoningHelper.getWoodSeasoningDuration(i)));
            }

            if (WoodHelper.canMakeFirewood(i)) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.firewood, 1, i),
                        new ItemStack(TFCItems.logs, 1, i * 2), "itemAxe"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.firewood, 1, i),
                        new ItemStack(TFCItems.logs, 1, i * 2 + 1), "itemAxe"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.firewood, 1, i),
                        new ItemStack(BidsItems.peeledLog, 1, i), "itemAxe"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.firewoodSeasoned, 1, i),
                        new ItemStack(BidsItems.logsSeasoned, 1, i * 2), "itemAxe"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.firewoodSeasoned, 1, i),
                        new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1), "itemAxe"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.firewoodSeasoned, 1, i),
                        new ItemStack(BidsItems.peeledLogSeasoned, 1, i), "itemAxe"));

                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        new ItemStack(BidsItems.firewood, 1, i),
                        new ItemStack(TFCItems.logs, 1, i * 2)));
                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        new ItemStack(BidsItems.firewood, 1, i),
                        new ItemStack(TFCItems.logs, 1, i * 2 + 1)));
                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        new ItemStack(BidsItems.firewood, 1, i),
                        new ItemStack(BidsItems.peeledLog, 1, i)));
                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        new ItemStack(BidsItems.firewoodSeasoned, 1, i),
                        new ItemStack(BidsItems.logsSeasoned, 1, i * 2)));
                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        new ItemStack(BidsItems.firewoodSeasoned, 1, i),
                        new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1)));
                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        new ItemStack(BidsItems.firewoodSeasoned, 1, i),
                        new ItemStack(BidsItems.peeledLogSeasoned, 1, i)));

                SeasoningManager.addRecipe(new SeasoningRecipe(new ItemStack(BidsItems.firewoodSeasoned, 1, i),
                        new ItemStack(BidsItems.firewood, 1, i),
                        SeasoningHelper.getWoodSeasoningDuration(i) - 4));
            }

            if (WoodHelper.canBuildLogWall(i)) {
                if (i < 16) {
                    Block logWall = WoodHelper.getDefaultLogWallBlock(0);
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWall, 1, j),
                            "A ", "11", '1', new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                            'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWall, 1, j),
                        "A ", "11", '1', new ItemStack(BidsItems.logsSeasoned, 1, i * 2),
                        'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWall, 1, j),
                        "A ", "11", '1', new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1),
                        'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLogSeasoned, 2, i),
                            Arrays.asList(new ItemStack(logWall, 1, j))));

                    Block logWallVert = WoodHelper.getDefaultLogWallVertBlock(0);
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWallVert, 1, j),
                            "1A", "1 ", '1', new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                            'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWallVert, 1, j),
                        "1A", "1 ", '1', new ItemStack(BidsItems.logsSeasoned, 1, i * 2),
                        'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWallVert, 1, j),
                        "1A", "1 ", '1', new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1),
                        'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLogSeasoned, 2, i),
                            Arrays.asList(new ItemStack(logWallVert, 1, j))));
                } else if (i < 32) {
                    Block logWall = WoodHelper.getDefaultLogWallBlock(16);
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWall, 1, j),
                            "A ", "11", '1', new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                            'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLogSeasoned, 2, i),
                            Arrays.asList(new ItemStack(logWall, 1, j))));

                    Block logWallVert = WoodHelper.getDefaultLogWallVertBlock(16);
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWallVert, 1, j),
                            "1A", "1 ", '1', new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                            'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLogSeasoned, 2, i),
                            Arrays.asList(new ItemStack(logWallVert, 1, j))));
                } else if (i < 48) {
                    Block logWall = WoodHelper.getDefaultLogWallBlock(32);
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWall, 1, j),
                            "A ", "11", '1', new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                            'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLogSeasoned, 2, i),
                            Arrays.asList(new ItemStack(logWall, 1, j))));

                    Block logWallVert = WoodHelper.getDefaultLogWallVertBlock(32);
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWallVert, 1, j),
                            "1A", "1 ", '1', new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                            'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLogSeasoned, 2, i),
                            Arrays.asList(new ItemStack(logWallVert, 1, j))));
                }
            }

            if (EnumWoodHardness.fromDamage(i) == EnumWoodHardness.HARD) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.plugAndFeather, 4),
                        new ItemStack(BidsItems.peeledLogSeasoned, 1, i), "itemAdze"));
            }

            // Copies of TFC recipes for items made logs
            if (WoodHelper.canPeelLog(i)) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 1),
                    new ItemStack(BidsItems.peeledLog, 1, i), "itemKnife"));

                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    " X", "XL", 'L', new ItemStack(BidsItems.peeledLog, 1, i), 'X', "lumpClay"));

                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.paddle, 1),
                    new ItemStack(TFCItems.pole, 1), new ItemStack(BidsItems.peeledLog, 1, i), "itemKnife"));

                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 1),
                    new ItemStack(BidsItems.peeledLogSeasoned, 1, i), "itemKnife"));

                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    " X", "XL", 'L', new ItemStack(BidsItems.peeledLogSeasoned, 1, i), 'X', "lumpClay"));

                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.paddle, 1),
                    new ItemStack(TFCItems.pole, 1), new ItemStack(BidsItems.peeledLogSeasoned, 1, i), "itemKnife"));

                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.singlePlank, 8, i),
                    new ItemStack(BidsItems.peeledLogSeasoned, 1, i), "itemSaw"));

                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 1),
                    new ItemStack(BidsItems.logsSeasoned, 1, i * 2), "itemKnife"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 1),
                    new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1), "itemKnife"));

                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    "X ", "LX", 'L', new ItemStack(BidsItems.logsSeasoned, 1, i * 2), 'X', "lumpClay"));
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    " X", "XL", 'L', new ItemStack(BidsItems.logsSeasoned, 1, i * 2), 'X', "lumpClay"));

                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.paddle, 1),
                    new ItemStack(TFCItems.pole, 1), new ItemStack(BidsItems.logsSeasoned, 1, i * 2), "itemKnife"));

                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.singlePlank, 8, i),
                    new ItemStack(BidsItems.logsSeasoned, 1, i * 2), "itemSaw"));
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.singlePlank, 8, i),
                    new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1), "itemSaw"));
            }

            // Copies of TFC recipes for block made from logs
            if (i < 16) {
                if (WoodHelper.canPeelLog(i)) {
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLog, 1, i), 'A', "itemSaw"));

                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLogSeasoned, 1, i), 'A', "itemSaw"));

                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.logsSeasoned, 1, i * 2), 'A', "itemSaw"));
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1), 'A', "itemSaw"));
                }
            } else if (i < 32) {
                if (WoodHelper.canPeelLog(i)) {
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV2, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLog, 1, i), 'A', "itemSaw"));

                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV2, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLogSeasoned, 1, i), 'A', "itemSaw"));

                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV2, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.logsSeasoned, 1, i * 2), 'A', "itemSaw"));
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV2, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1), 'A', "itemSaw"));
                }
            } else if (i < 48) {
                if (WoodHelper.canPeelLog(i)) {
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV3, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLog, 1, i), 'A', "itemSaw"));

                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV3, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLogSeasoned, 1, i), 'A', "itemSaw"));

                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV3, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.logsSeasoned, 1, i * 2), 'A', "itemSaw"));
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV3, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1), 'A', "itemSaw"));
                }
            }
        }

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.clayLamp),
            "S ", "B ", 'S', "materialString",
            'B', new ItemStack(TFCItems.potteryBowl, 1, 1)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsBlocks.wallHook, 1, 0),
            "stickWood", TFCItems.resin));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.honeyLargeBowl, 1, 0),
            "itemHoneycomb", "itemHoneycomb", "itemKnife", new ItemStack(BidsItems.largeClayBowl, 1, 1)));

        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemKnife")
            .matchCraftingItem(BidsItems.honeyLargeBowl));
        RecipeManager.addAction(new ActionExtraDrop()
            .addExtraDrop(new ItemStack(TFCItems.emptyHoneycomb, 2, 0))
            .matchIngredient("itemHoneycomb")
            .matchIngredient("itemHoneycomb")
            .matchIngredient("itemKnife")
            .matchIngredient(BidsItems.largeClayBowl)
            .matchCraftingItem(BidsItems.honeyLargeBowl));

        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.ceramicBucketRope),
            new ItemStack(TFCItems.rope, 1, 0), new ItemStack(TFCItems.clayBucketEmpty, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.woodenBucketRope),
            new ItemStack(TFCItems.rope, 1, 0), new ItemStack(TFCItems.woodenBucketEmpty, 1, 0));

        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.clayBucketEmpty), new ItemStack(BidsItems.ceramicBucketRope, 1, 0));
        RecipeManager.addAction(new ActionExtraDrop()
            .addExtraDrop(new ItemStack(TFCItems.rope, 1, 0))
            .matchCraftingItem(TFCItems.clayBucketEmpty)
            .matchIngredient(BidsItems.ceramicBucketRope));

        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.woodenBucketEmpty), new ItemStack(BidsItems.woodenBucketRope, 1, 0));
        RecipeManager.addAction(new ActionExtraDrop()
            .addExtraDrop(new ItemStack(TFCItems.rope, 1, 0))
            .matchCraftingItem(TFCItems.woodenBucketEmpty)
            .matchIngredient(BidsItems.woodenBucketRope));

        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.saltWaterBottle),
            new ItemStack(TFCItems.powder, 1, 9), new ItemStack(TFCItems.powder, 1, 9),
            new ItemStack(TFCItems.waterBottle), new ItemStack(TFCItems.glassBottle));

        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.woodenBucketSaltWater),
            new ItemStack(TFCItems.powder, 1, 9), new ItemStack(TFCItems.powder, 1, 9),
            new ItemStack(TFCItems.woodenBucketWater), new ItemStack(TFCItems.woodenBucketEmpty));

        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.clayBucketSaltWater),
            new ItemStack(TFCItems.powder, 1, 9), new ItemStack(TFCItems.powder, 1, 9),
            new ItemStack(TFCItems.clayBucketWater), new ItemStack(TFCItems.clayBucketEmpty));

        DryingManager.addRecipe(new DryingRecipe(new ItemStack(BidsItems.barkFibreStrip, 1, 1),
                new ItemStack(BidsItems.barkFibreStrip, 1, 0), 12, false));

        // Meat and cheese drying from TFC
        final Item[] foodToDry = new Item[] { TFCItems.venisonRaw, TFCItems.beefRaw, TFCItems.chickenRaw,
                TFCItems.porkchopRaw, TFCItems.fishRaw, TFCItems.seastarRaw, TFCItems.scallopRaw,
                TFCItems.calamariRaw, TFCItems.muttonRaw, TFCItems.horseMeatRaw, TFCItems.cheese,
                BidsItems.goatCheese };
        for (Item food : foodToDry) {
            DryingManager.addRecipe(new FoodDryingRecipe(ItemFoodTFC.createTag(new ItemStack(food)), 12, true));
        }

        // Extra food drying
        DryingManager.addRecipe(new FoodDryingRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.seaWeed)), 12, true));

        // And now tying equipment
        DryingManager.registerTyingEquipment(BidsItems.barkCordage, false, Blocks.wool, 1);
        DryingManager.registerTyingEquipment(TFCItems.woolYarn, false, Blocks.wool, 0);
        DryingManager.registerTyingEquipment(TFCItems.linenString, false, Blocks.wool, 0);
        DryingManager.registerTyingEquipment(TFCItems.cottonYarn, false, Blocks.wool, 0);
        DryingManager.registerTyingEquipment(TFCItems.silkString, false, Blocks.wool, 0);

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAdze", "itemAxe")
                .matchCraftingItem(BidsItems.peeledLog));

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAdze", "itemAxe")
                .matchCraftingItem(BidsItems.peeledLogSeasoned));

        RecipeManager.addAction(new ActionExtraDrop()
                .addExtraDrop(new ItemStack(BidsItems.bark, 1, OreDictionary.WILDCARD_VALUE),
                        BidsOptions.Bark.dropPeelingChance)
                .matchIngredient("itemAdze")
                .matchCraftingItem(BidsItems.peeledLog));

        RecipeManager.addAction(new ActionExtraDrop()
                .addExtraDrop(new ItemStack(BidsItems.bark, 1, OreDictionary.WILDCARD_VALUE),
                        BidsOptions.Bark.dropPeelingSeasonedChance)
                .matchIngredient("itemAdze")
                .matchCraftingItem(BidsItems.peeledLogSeasoned));

        RecipeManager.addAction(new ActionExtraDrop()
                .addExtraDrop(new ItemStack(BidsItems.bark, 1, OreDictionary.WILDCARD_VALUE),
                        BidsOptions.Bark.dropSplittingChance)
                .matchIngredient(TFCItems.logs)
                .matchIngredient("itemAxe")
                .matchCraftingItem(BidsItems.firewood));

        RecipeManager.addAction(new ActionExtraDrop()
                .addExtraDrop(new ItemStack(BidsItems.bark, 1, OreDictionary.WILDCARD_VALUE),
                        BidsOptions.Bark.dropSplittingSeasonedChance)
                .matchIngredient(BidsItems.logsSeasoned)
                .matchIngredient("itemAxe")
                .matchCraftingItem(BidsItems.firewoodSeasoned));

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAxe")
                .matchCraftingItem(BidsItems.firewood));

        RecipeManager.addAction(new ActionCopySeasoning()
                .addHandledItem(TFCItems.logs)
                .matchCraftingItem(BidsItems.peeledLog));

        RecipeManager.addAction(new ActionCopySeasoning()
                .addHandledItem(TFCItems.logs)
                .addHandledItem(BidsItems.peeledLog)
                .matchCraftingItem(BidsItems.firewood));

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAxe")
                .matchCraftingItem(BidsItems.firewoodSeasoned));

        for (int i = 0; i < 3; i++) {
            Block logWall = WoodHelper.getDefaultLogWallBlock(i * 16);
            RecipeManager.addAction(new ActionDamageTool(1)
                    .addTools("itemAdze")
                    .matchIngredient(BidsItems.peeledLogSeasoned)
                    .matchIngredient(BidsItems.peeledLogSeasoned)
                    .matchCraftingBlock(logWall));
            RecipeManager.addAction(new ActionDamageTool(2)
                    .addTools("itemAdze")
                    .matchIngredient(BidsItems.logsSeasoned)
                    .matchIngredient(BidsItems.logsSeasoned)
                    .matchCraftingBlock(logWall));

            Block logWallVert = WoodHelper.getDefaultLogWallVertBlock(i * 16);
            RecipeManager.addAction(new ActionDamageTool(1)
                    .addTools("itemAdze")
                    .matchIngredient(BidsItems.peeledLogSeasoned)
                    .matchIngredient(BidsItems.peeledLogSeasoned)
                    .matchCraftingBlock(logWallVert));
            RecipeManager.addAction(new ActionDamageTool(2)
                    .addTools("itemAdze")
                    .matchIngredient(BidsItems.logsSeasoned)
                    .matchIngredient(BidsItems.logsSeasoned)
                    .matchCraftingBlock(logWallVert));
        }

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemKnife")
                .matchCraftingItem(BidsItems.barkFibreStrip, 0));

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemKnife")
                .matchCraftingItem(BidsItems.birchBarkSheet, 0));

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAdze")
                .matchCraftingItem(BidsItems.plugAndFeather));

        RecipeManager.addAction(new ActionKeepItem()
            .addItems("itemLogExtra")
            .matchCraftingItem(TFCItems.clayTile));
    }

    private static void registerCarvingRecipes() {
        Bids.LOG.info("Register carving recipes");

        CarvingRecipePattern choppingBlockPattern = new CarvingRecipePattern()
                .carveEntireLayer();

        for (int i = 0; i < Global.WOOD_ALL.length; i++) {
            int j = i % 16;

            if (WoodHelper.canMakeChoppingBlock(i)) {
                if (i < 16) {
                    CarvingManager.addRecipe(new CarvingRecipe(new ItemStack(BidsBlocks.choppingBlock, 1, i % 16),
                            new ItemStack(TFCBlocks.woodVert, 1, j), choppingBlockPattern));
                } else if (i < 32) {
                    CarvingManager.addRecipe(new CarvingRecipe(new ItemStack(BidsBlocks.choppingBlock2, 1, i % 16),
                            new ItemStack(TFCBlocks.woodVert2, 1, j), choppingBlockPattern));
                } else {
                    CarvingManager.addRecipe(new CarvingRecipe(new ItemStack(BidsBlocks.choppingBlock3, 1, i % 16),
                            new ItemStack(TFCBlocks.woodVert3, 1, j), choppingBlockPattern));
                }
            }
        }

        CarvingRecipePattern saddleQuernPattern = new CarvingRecipePattern()
                .carveLayer("    ", " ## ", " ## ", " ## ");

        for (int i = 0; i < Global.STONE_SED.length; i++) {
            CarvingManager.addRecipe(new CarvingRecipe(new ItemStack(BidsBlocks.saddleQuernBaseSed, 1, i),
                    new ItemStack(BidsBlocks.roughStoneSed, 1, i), saddleQuernPattern));
        }

        CarvingRecipePattern[] handstonePatterns = {
                new CarvingRecipePattern()
                        .carveEntireLayer()
                        .carveEntireLayer()
                        .carveLayer("####", "#  #", "#  #", "#  #")
                        .carveLayer("####", "#  #", "#  #", "#  #"),
                new CarvingRecipePattern()
                        .carveEntireLayer()
                        .carveEntireLayer()
                        .carveLayer("####", "  ##", "  ##", "  ##")
                        .carveLayer("####", "  ##", "  ##", "  ##"),
                new CarvingRecipePattern()
                        .carveEntireLayer()
                        .carveEntireLayer()
                        .carveLayer("####", "##  ", "##  ", "##  ")
                        .carveLayer("####", "##  ", "##  ", "##  ")
        };

        for (int j = 0; j < handstonePatterns.length; j++) {
            for (int i = 0; i < Global.STONE_SED.length; i++) {
                CarvingManager.addRecipe(new CarvingRecipe(new ItemStack(BidsBlocks.saddleQuernHandstoneSed, 1, i),
                        new ItemStack(BidsBlocks.roughStoneSed, 1, i), handstonePatterns[j]));
            }
        }

        CarvingRecipePattern pressingStonePattern =  new CarvingRecipePattern()
                .carveEntireLayer()
                .carveEntireLayer()
                .carveLayer("####", "#   ", "#   ", "#   ")
                .carveLayer("####", "#   ", "#   ", "#   ");

        for (int i = 0; i < Global.STONE_SED.length; i++) {
            CarvingManager.addRecipe(new CarvingRecipe(new ItemStack(BidsBlocks.saddleQuernPressingStoneSed, 1, i),
                new ItemStack(BidsBlocks.roughStoneSed, 1, i), pressingStonePattern));
        }

        CarvingRecipePattern weightStonePattern =  new CarvingRecipePattern()
            .carveEntireLayer()
            .carveLayer("####", "#   ", "#   ", "#   ")
            .carveLayer("####", "#   ", "#   ", "#   ")
            .carveLayer("####", "#   ", "#   ", "#   ");

        for (int i = 0; i < Global.STONE_SED.length; i++) {
            CarvingManager.addRecipe(new CarvingRecipe(new ItemStack(BidsBlocks.stonePressWeightSed, 1, i),
                new ItemStack(BidsBlocks.roughStoneSed, 1, i), weightStonePattern));
        }
    }

    private static void registerSaddleQuernRecipes() {
        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(BidsItems.wheatCrushed),
                new ItemStack(TFCItems.wheatGrain)));
        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(BidsItems.barleyCrushed),
                new ItemStack(TFCItems.barleyGrain)));
        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(BidsItems.oatCrushed),
                new ItemStack(TFCItems.oatGrain)));
        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(BidsItems.riceCrushed),
                new ItemStack(TFCItems.riceGrain)));
        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(BidsItems.ryeCrushed),
                new ItemStack(TFCItems.ryeGrain)));
        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(BidsItems.cornmealCrushed),
                new ItemStack(TFCItems.maizeEar)));

        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(BidsItems.appleCrushed),
                new ItemStack(TFCItems.greenApple)));
        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(BidsItems.appleCrushed),
                new ItemStack(TFCItems.redApple)));
        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(BidsItems.oliveCrushed),
                new ItemStack(TFCItems.olive)));
    }

    private static void registerStonePressRecipes() {
        // Stone press efficiency affects the recipe input or output volume
        float inputMult = 1 / BidsOptions.StonePress.efficiency; // input multiplier (for non-food input)
        float outputMult = BidsOptions.StonePress.efficiency; // output multiplier (for food input)

        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.OLIVEOIL, 10),
                ItemFoodTFC.createTag(new ItemStack(BidsItems.oliveCrushed), 0.64f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.APPLEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(BidsItems.appleCrushed), 0.7f * inputMult)));

        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.GRAPEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.grapes), 0.5f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.CANEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.sugarcane), 0.8f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.LEMONJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.lemon), 0.65f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.ORANGEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.orange), 0.5f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.PEACHJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.peach), 0.55f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.PLUMJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.plum), 0.65f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.FIGJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.fig), 0.5f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.CHERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.cherry), 0.7f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.DATEJUICE, 6),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.date), 0.8f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.PAPAYAJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.papaya), 0.6f * inputMult)));

        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.strawberry), 0.65f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.blackberry), 0.61f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.blueberry), 0.6f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.bunchberry), 0.68f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.cranberry), 0.7f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.elderberry), 0.58f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.gooseberry), 0.6f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.raspberry), 0.6f * inputMult)));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.snowberry), 0.66f * inputMult)));

        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(TFCFluids.AGAVEJUICE, Math.round(40 * outputMult)),
                new ItemStack(TFCItems.agave, 1)));

        ItemStack steamedFish = BidsFood.setSteamed(ItemFoodTFC.createTag(new ItemStack(TFCItems.fishRaw), 0.5f * inputMult), true);
        // Require fish to be cooked (steamed) to medium level
        Food.setCooked(steamedFish, CookingHelper.getTempForItemStackCookedLevel(steamedFish, 3));
        // Set infusion to show item with "(Steamed)" text
        // It does not affect recipe matching
        Food.setInfusion(steamedFish, "infusion.steamed");
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(BidsFluids.OILYFISHWATER, 10), steamedFish));
    }

    private static void registerCookingRecipes() {
        Bids.LOG.info("Register cooking recipes");

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 500), new ItemStack(TFCItems.powder, 1, 9))
            .produces(new FluidStack(TFCFluids.SALTWATER, 500))
            .inTime(20)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.SALTWATER, 500))
            .produces(new ItemStack(TFCItems.powder, 1, 9))
            .withHeat()
            .withoutLid()
            .inTime(750)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new ItemStack(Items.snowball))
            .produces(new FluidStack(TFCFluids.FRESHWATER, 200))
            .withHeat()
            .inTime(200)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new ItemStack(Items.snowball))
            .produces(new FluidStack(TFCFluids.FRESHWATER, 200))
            .withoutHeat()
            .inTime(1000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new ItemStack(TFCItems.resin))
            .produces(new FluidStack(TFCFluids.PITCH, 50))
            .withHeat()
            .inTime(50)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new ItemStack(TFCItems.emptyHoneycomb))
            .produces(new FluidStack(TFCFluids.WAX, 300))
            .withHeat()
            .inTime(750)
            .build());

        for (Item stringItem : new Item[] { TFCItems.silkString, TFCItems.woolYarn, TFCItems.linenString, TFCItems.cottonYarn, TFCItems.juteFiber, TFCItems.sisalFiber }) {
            CookingManager.addRecipe(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.WAX, 200), new ItemStack(stringItem))
                .produces(new ItemStack(TFCBlocks.candleOff, 1))
                .withHeat()
                .build());
        }

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.PITCH, 50), new ItemStack(TFCItems.stick))
            .produces(new ItemStack(TFCBlocks.torchOff, 1))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.PITCH, 250), new ItemStack(TFCItems.leatherBag))
            .produces(new ItemStack(TFCItems.pitchBag, 1))
            .inTime(100)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 9), new FluidStack(TFCFluids.HONEY, 1))
            .produces(new FluidStack(TFCFluids.HONEYWATER, 10))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.SALTWATER, 9), new FluidStack(TFCFluids.VINEGAR, 1))
            .produces(new FluidStack(TFCFluids.BRINE, 10))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.MILK, 9), new FluidStack(TFCFluids.VINEGAR, 1))
            .produces(new FluidStack(TFCFluids.MILKVINEGAR, 10))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.MILKVINEGAR, 1))
            .produces(new FluidStack(TFCFluids.MILKCURDLED, 1))
            .withoutHeat()
            .withLid()
            .inFixedTime(8000)
            .build());

        CookingManager.addRecipe(CookingCheeseRecipe.builder()
            .allowingInfusion()
            .consumes(new FluidStack(TFCFluids.MILKCURDLED, 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(TFCItems.cheese), Global.FOOD_MAX_WEIGHT / 10000))
            .withoutHeat()
            .withLid()
            .inFixedTime(8000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.GOATMILK, 9), new FluidStack(TFCFluids.VINEGAR, 1))
            .produces(new FluidStack(BidsFluids.GOATMILKVINEGAR, 10))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.GOATMILKVINEGAR, 1))
            .produces(new FluidStack(BidsFluids.GOATMILKCURDLED, 1))
            .withoutHeat()
            .withLid()
            .inFixedTime(8000)
            .build());

        CookingManager.addRecipe(CookingCheeseRecipe.builder()
            .allowingInfusion()
            .consumes(new FluidStack(BidsFluids.GOATMILKCURDLED, 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.goatCheese), Global.FOOD_MAX_WEIGHT / 10000))
            .withoutHeat()
            .withLid()
            .inFixedTime(8000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.OILYFISHWATER, 1000))
            .produces(new FluidStack(TFCFluids.FRESHWATER, 950), new FluidStack(BidsFluids.FISHOIL, 50))
            .withoutHeat()
            .inFixedTime(48000)
            .build());
    }

    private static void registerPrepRecipes() {
        Bids.LOG.info("Register prep recipes");

        PrepIngredient foodAny = PrepIngredient.builder()
            .build();

        PrepIngredient foodAllButGrain = PrepIngredient.builder()
            .deny(EnumFoodGroup.Grain)
            .build();

        PrepIngredient vesselBowl = PrepIngredient.builder()
            .allow(TFCItems.potteryBowl, 1)
            .allow(TFCItems.potteryBowl, 2)
            .build();

        Item[] breads = new Item[] { TFCItems.wheatBread, TFCItems.oatBread, TFCItems.barleyBread, TFCItems.ryeBread, TFCItems.cornBread, TFCItems.riceBread };
        for (int i = 0; i < breads.length; i++) {
            PrepManager.addRecipe(new PrepSandwichRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.sandwich, 1, i)), new PrepIngredient[]{
                PrepIngredient.from(breads[i]), foodAllButGrain, foodAllButGrain, foodAllButGrain, foodAllButGrain
            }));
        }

        PrepManager.addRecipe(new PrepSaladRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.salad)), new PrepIngredient[] {
            vesselBowl, foodAny, foodAny, foodAny, foodAny
        }));

        Item[] peppers = new Item[] { TFCItems.greenBellPepper, TFCItems.yellowBellPepper, TFCItems.redBellPepper };
        for (int i = 0; i < peppers.length; i++) {
            PrepManager.addRecipe(new PrepMoreSandwichRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.stuffedPepper, 1, i)), new PrepIngredient[]{
                PrepIngredient.from(peppers[i]), foodAny, foodAny, foodAny, foodAny
            }));
        }

        PrepManager.addRecipe(new PrepMoreSandwichRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.stuffedMushroom)), new PrepIngredient[]{
            PrepIngredient.from(TFCItems.mushroomFoodB), foodAny, foodAny, foodAny, foodAny
        }));
    }

    private static void registerKnappingRecipes() {
        Bids.LOG.info("Register TFC knapping recipes");

        for (int i = 0; i < Global.STONE_IGIN.length; i++) {
            CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.igInStoneDrillHead, 1),
                    new Object[] { "     ", " ### ", "#####", " ### ", "  #  ",
                            '#', new ItemStack(TFCItems.flatRock, 1, i + Global.STONE_IGIN_START) });
            CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.igInStoneAdzeHead, 1),
                    new Object[] { "#####", "#  ##", "#    ", "     ", "     ",
                            '#', new ItemStack(TFCItems.flatRock, 1, i + Global.STONE_IGIN_START) });
        }

        for (int i = 0; i < Global.STONE_SED.length; i++) {
            CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.sedStoneDrillHead, 1),
                    new Object[] { "     ", " ### ", "#####", " ### ", "  #  ",
                            '#', new ItemStack(TFCItems.flatRock, 1, i + Global.STONE_SED_START) });
            CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.igInStoneAdzeHead, 1),
                    new Object[] { "#####", "#  ##", "#    ", "     ", "     ",
                            '#', new ItemStack(TFCItems.flatRock, 1, i + Global.STONE_SED_START) });
        }

        for (int i = 0; i < Global.STONE_IGEX.length; i++) {
            CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.igExStoneDrillHead, 1),
                    new Object[] { "     ", " ### ", "#####", " ### ", "  #  ",
                            '#', new ItemStack(TFCItems.flatRock, 1, i + Global.STONE_IGEX_START) });
            CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.igInStoneAdzeHead, 1),
                    new Object[] { "#####", "#  ##", "#    ", "     ", "     ",
                            '#', new ItemStack(TFCItems.flatRock, 1, i + Global.STONE_IGEX_START) });
        }

        for (int i = 0; i < Global.STONE_MM.length; i++) {
            CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.mMStoneDrillHead, 1),
                    new Object[] { "     ", " ### ", "#####", " ### ", "  #  ",
                            '#', new ItemStack(TFCItems.flatRock, 1, i + Global.STONE_MM_START) });
            CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.igInStoneAdzeHead, 1),
                    new Object[] { "#####", "#  ##", "#    ", "     ", "     ",
                            '#', new ItemStack(TFCItems.flatRock, 1, i + Global.STONE_MM_START) });
        }

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.clayMoldAdze, 1),
            new Object[] { "     ", "#####", "#  ##", "#    ", "     ",
                '#', new ItemStack(TFCItems.flatClay, 1, 1) });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.clayMoldDrill, 1),
            new Object[] { "  #  ", "  #  ", "  #  ", " ### ", "  #  ",
                '#', new ItemStack(TFCItems.flatClay, 1, 1) });

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.clayCrucible, 1, 1),
                new Object[] { "#####", " ### ", " ### ", " ### ", "     ", '#',
                        new ItemStack(TFCItems.flatClay, 1, 1) });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.fireClayCrucible, 1),
                new Object[] { "#####", " ### ", " ### ", " ### ", "     ", '#',
                        new ItemStack(TFCItems.flatClay, 1, 3) });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.clayMug, 2),
                new Object[] { "#####", "#####", "    #", "   # ", "    #", '#',
                        new ItemStack(TFCItems.flatClay, 1, 1) });

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.largeClayBowl, 1),
            new Object[] { "#####", " ### ", " ### ", "#   #", "#####", '#',
                new ItemStack(TFCItems.flatClay, 1, 1) });

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.cookingPot, 1, 0),
            new Object[] { " ### ", " ### ", " ### ", " ### ", "#   #", '#',
                new ItemStack(TFCItems.flatClay, 1, 1) });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.cookingPotLid, 1, 0),
            new Object[] { "## ##", "     ", "#####", "#####", "#####", '#',
                new ItemStack(TFCItems.flatClay, 1, 1) });

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

        Object[][] cordagePatterns = new Object[][] {
                new Object[] { "#####", "    #", "### #", "#   #", "#####", '#', null },
                new Object[] { "### #", "# # #", "# # #", "#   #", "#####", '#', null },
                new Object[] { "#####", "#   #", "# ###", "#    ", "#####", '#', null },
                new Object[] { "#####", "#   #", "# # #", "# # #", "# ###", '#', null },
                new Object[] { "#####", "#    ", "# ###", "#   #", "#####", '#', null },
                new Object[] { "# ###", "# # #", "# # #", "#   #", "#####", '#', null },
                new Object[] { "#####", "#   #", "### #", "    #", "#####", '#', null },
                new Object[] { "#####", "#   #", "# # #", "# # #", "### #", '#', null } };
        for (Object[] pattern : cordagePatterns) {
            pattern[6] = new ItemStack(BidsItems.flatBarkFibre, 1, 1);
            CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.barkCordage), pattern);
        }

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkStrap, 3),
                new Object[] { "# # #", "# # #", "# # #", "# # #", "# # #", '#', BidsItems.flatBirchBark });

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkBagPiece, 2, 0),
                new Object[] { " ### ", " ### ", "     ", " ### ", " ### ", '#', BidsItems.flatBirchBark });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkBagPiece, 2, 0),
                new Object[] { "     ", "## ##", "## ##", "## ##", "     ", '#', BidsItems.flatBirchBark });

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkRepairPatch, 4, 0),
                new Object[] { "## ##", "## ##", "     ", "## ##", "## ##", '#', BidsItems.flatBirchBark });

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkCupPiece, 1),
                new Object[] { "     ", "     ", "#### ", "### #", "#### ", '#',
                        new ItemStack(BidsItems.flatBirchBark, 1) });

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkShoes, 1),
                new Object[] { "  ###", "   ##", "     ", "##   ", "###  ", '#',
                        new ItemStack(BidsItems.flatBirchBark, 1) });

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.coatBodyFrontLeather, 1, 0),
            new Object[] { "#   #", "## ##", "## ##", "## ##", "## ##", '#', TFCItems.flatLeather });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.coatBodyBackLeather, 1, 0),
            new Object[] { "## ##", "#####", "#####", "#####", "#####", '#', TFCItems.flatLeather });

        for (Item flatItem : new Item[] { TFCItems.flatLinen, TFCItems.flatWool, TFCItems.flatSilk, TFCItems.flatCotton, TFCItems.flatBurlap }) {
            CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.steamingMeshCloth, 1),
                new Object[] { "#####", "# # #", "#####", "# # #", "#####", '#', flatItem });
        }
    }

    private static void registerSewingRecipes() {
        Bids.LOG.info("Register TFC sewing recipes");

        int[][][] bagSewing = new int[][][] { {
                { 25, 21 },
                { 11, 74 },
                { 19, 87 },
                { 79, 87 },
                { 87, 74 },
                { 73, 21 }
        } };

        ClothingManager.getInstance().addRecipe(new SewingRecipe(
                new SewingPattern(new ItemStack(BidsItems.birchBarkBag, 1), bagSewing, true),
                new ItemStack[] {
                        new ItemStack(BidsItems.birchBarkBagPiece, 1, 0),
                        new ItemStack(BidsItems.birchBarkBagPiece, 1, 0),
                        new ItemStack(BidsItems.birchBarkStrap, 1, 0)
                }));

        int[][][] cupSewing = new int[][][] { {
                { 11, 74 },
                { 19, 87 },
                { 64, 87 },
                { 72, 74 },
                { 72, 37 },
                { 60, 40 },
                { 21, 40 },
                { 11, 37 }
        } };

        ClothingManager.getInstance().addRecipe(new SewingRecipe(
                new SewingPattern(new ItemStack(BidsItems.birchBarkCupUnfinished, 1), cupSewing, true),
                new ItemStack[] {
                        new ItemStack(BidsItems.birchBarkCupPiece, 1, 0),
                        new ItemStack(BidsItems.birchBarkStrap, 1, 0)
                }));

        int[][][] coatSewing = new int[][][] {
            // the left side of the coat and underarm
            { { 24, 86 }, { 27, 33 }, { 24, 33 }, { 18, 71 } },
            // the outer left arm and shoulder
            { { 8, 71 }, { 11, 33 }, { 16, 19 }, { 22, 13 },  { 37, 12 } },
            // the arm attached to the sleeve
            {  { 25, 33 }, { 21, 15 } },
            // the right side of the coat and underarm
             { { 97 - 24, 86 }, { 97 - 27, 33 }, { 97 - 24, 33 },  { 97 - 18, 71 } },
            // the outer right arm and shoulder
            { { 97 - 8, 71 }, { 97 - 11, 33 },{ 97 - 16, 19 }, { 97 - 22, 13 },  { 97 - 37, 12 } },
            // the right arm attached to the sleeve
            { { 97 - 25, 33 }, { 97 - 21, 15 } }
        };

        ClothingManager.getInstance().addRecipe(new SewingRecipe(
            new SewingPattern(new ItemStack(BidsItems.leatherCoat, 1), coatSewing, true),
            new ItemStack[] {
                new ItemStack(BidsItems.coatBodyFrontLeather, 1, 0),
                new ItemStack(BidsItems.coatBodyBackLeather, 1, 0),
                new ItemStack(TFCItems.shirtSleeves, 1, 2),
                new ItemStack(TFCItems.shirtSleeves, 1, 2),
            }));
    }

    private static void registerSewingRepairRecipes() {
        Bids.LOG.info("Register TFC sewing repair recipes");

        ClothingManager.getInstance().addRecipe(new SewingRecipe(
                new SewingPattern(new ItemStack(BidsItems.birchBarkBag, 1), true),
                new ItemStack[] {
                        new ItemStack(BidsItems.birchBarkBag, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(BidsItems.birchBarkRepairPatch, 1, 0)
                }).setRepairRecipe());

        ClothingManager.getInstance().addRecipe(new SewingRecipe(
                new SewingPattern(new ItemStack(BidsItems.birchBarkShoes, 1), true),
                new ItemStack[] {
                        new ItemStack(BidsItems.birchBarkShoes, 1, OreDictionary.WILDCARD_VALUE),
                        new ItemStack(BidsItems.birchBarkStrap, 1, 0)
                }).setRepairRecipe());

        ClothingManager.getInstance().addRecipe(new SewingRecipe(
            new SewingPattern(new ItemStack(BidsItems.leatherCoat, 1), true),
            new ItemStack[] {
                new ItemStack(BidsItems.leatherCoat, 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(TFCItems.repairPatch, 1, 2)
            }).setRepairRecipe());

        if (BidsOptions.Crafting.craftingAddMissingLeatherRepairRecipes) {
            // Adding missing TFC+ recipe for repairing leather boots
            ClothingManager.getInstance().addRecipe(new SewingRecipe(
                new SewingPattern(new ItemStack(TFCItems.leatherBoots, 1), true),
                new ItemStack[] {
                    new ItemStack(TFCItems.leatherBoots, 1, OreDictionary.WILDCARD_VALUE),
                    new ItemStack(TFCItems.repairPatch, 1, 2)
                }).setRepairRecipe());

            // Adding missing TFC+ recipe for repairing leather cap
            ClothingManager.getInstance().addRecipe(new SewingRecipe(
                new SewingPattern(new ItemStack(TFCItems.leatherCoif, 1), true),
                new ItemStack[] {
                    new ItemStack(TFCItems.leatherCoif, 1, OreDictionary.WILDCARD_VALUE),
                    new ItemStack(TFCItems.repairPatch, 1, 2)
                }).setRepairRecipe());

            // Adding missing TFC+ recipe for repairing leather shorts
            ClothingManager.getInstance().addRecipe(new SewingRecipe(
                new SewingPattern(new ItemStack(TFCItems.leatherShorts, 1), true),
                new ItemStack[] {
                    new ItemStack(TFCItems.leatherShorts, 1, OreDictionary.WILDCARD_VALUE),
                    new ItemStack(TFCItems.repairPatch, 1, 2)
                }).setRepairRecipe());
        }
    }

    private static void registerKilnRecipes() {
        Bids.LOG.info("Register TFC kiln recipes");

        KilnCraftingManager.getInstance().addRecipe(
                new KilnRecipe(new ItemStack(BidsItems.clayPipe, 1, 0), 0,
                        new ItemStack(BidsItems.clayPipe, 1, 1)));

        KilnCraftingManager.getInstance().addRecipe(
                new KilnRecipe(new ItemStack(BidsItems.clayMug, 1, 0), 0,
                        new ItemStack(BidsItems.clayMug, 1, 1)));

        KilnCraftingManager.getInstance().addRecipe(
                new KilnRecipe(new ItemStack(BidsBlocks.clayCrucible, 1, 1), 0,
                        new ItemStack(BidsBlocks.clayCrucible, 1, 0)));

        KilnCraftingManager.getInstance().addRecipe(
            new KilnRecipe(new ItemStack(BidsItems.largeClayBowl, 1, 0), 0,
                new ItemStack(BidsItems.largeClayBowl, 1, 1)));

        KilnCraftingManager.getInstance().addRecipe(
            new KilnRecipe(new ItemStack(BidsBlocks.cookingPot, 1, 0), 0,
                new ItemStack(BidsBlocks.cookingPot, 1, 1)));
        KilnCraftingManager.getInstance().addRecipe(
            new KilnRecipe(new ItemStack(BidsBlocks.cookingPotLid, 1, 0), 0,
                new ItemStack(BidsBlocks.cookingPotLid, 1, 1)));

        KilnCraftingManager.getInstance().addRecipe(
            new KilnRecipe(new ItemStack(BidsItems.clayMoldAdze, 1, 0), 0,
                new ItemStack(BidsItems.clayMoldAdze, 1, 1)));
        KilnCraftingManager.getInstance().addRecipe(
            new KilnRecipe(new ItemStack(BidsItems.clayMoldDrill, 1, 0), 0,
                new ItemStack(BidsItems.clayMoldDrill, 1, 1)));
    }

    private static void registerBarrelRecipes() {
        Bids.LOG.info("Register TFC barrel recipes");

        for (int i = 0; i < Global.WOOD_ALL.length; i++) {
            // Retting fibers from bark
            if (WoodHelper.canBarkMakeFibers(i)) {
                BarrelManager.getInstance().addRecipe(new BarrelMultiItemRecipe(
                        new ItemStack(BidsItems.bark, 1, i), new FluidStack(TFCFluids.FRESHWATER, 625),
                        new ItemStack(BidsItems.barkFibre, 1, 0), new FluidStack(TFCFluids.FRESHWATER, 500))
                        .setSealTime(8).setMinTechLevel(0));
            }

            // Extracting tannin from bark
            if (WoodHelper.canBarkMakeTannin(i)) {
                BarrelManager.getInstance().addRecipe(new BarrelItemDemandingRecipe(
                        new ItemStack(BidsItems.bark, 1, i), new FluidStack(TFCFluids.FRESHWATER, 625),
                        null, new FluidStack(TFCFluids.TANNIN, 500))
                        .setMinTechLevel(0));
            }
        }

        BarrelManager.getInstance().addRecipe(new BarrelAlcoholRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.riceGerm), 80, true), new FluidStack(TFCFluids.FRESHWATER, 5000),
            null, new FluidStack(TFCFluids.RICEBEER, 5000)).setMinTechLevel(0).setRequiresCooked(true));
        BarrelManager.getInstance().addRecipe(new BarrelAlcoholRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.wheatGerm), 80, true), new FluidStack(TFCFluids.FRESHWATER, 5000),
            null, new FluidStack(TFCFluids.WHEATBEER, 5000)).setMinTechLevel(0).setRequiresCooked(true));
        BarrelManager.getInstance().addRecipe(new BarrelAlcoholRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.ryeGerm), 80, true), new FluidStack(TFCFluids.FRESHWATER, 5000),
            null, new FluidStack(TFCFluids.RYEBEER, 5000)).setMinTechLevel(0).setRequiresCooked(true));
        BarrelManager.getInstance().addRecipe(new BarrelAlcoholRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.barleyGerm), 80, true), new FluidStack(TFCFluids.FRESHWATER, 5000),
            null, new FluidStack(TFCFluids.BEER, 5000)).setMinTechLevel(0).setRequiresCooked(true));
        BarrelManager.getInstance().addRecipe(new BarrelAlcoholRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.cornGerm), 80, true), new FluidStack(TFCFluids.FRESHWATER, 5000),
            null, new FluidStack(TFCFluids.CORNBEER, 5000)).setMinTechLevel(0).setRequiresCooked(true));

        BarrelManager.getInstance().addRecipe(new BarrelLiquidToLiquidRecipe(new FluidStack(TFCFluids.SALTWATER, 4500), new FluidStack(TFCFluids.VINEGAR, 500), new FluidStack(TFCFluids.BRINE, 5000))
                .setSealTime(0).setSealedRecipe(false).setMinTechLevel(0).setRemovesLiquid(false));
        BarrelManager.getInstance().addRecipe(new BarrelLiquidToLiquidRecipe(new FluidStack(TFCFluids.MILK, 4500), new FluidStack(TFCFluids.VINEGAR, 500), new FluidStack(TFCFluids.MILKVINEGAR, 5000))
            .setSealTime(0).setSealedRecipe(false).setMinTechLevel(0).setRemovesLiquid(false));
        BarrelManager.getInstance().addRecipe(new BarrelLiquidToLiquidRecipe(new FluidStack(TFCFluids.FRESHWATER, 4500), new FluidStack(TFCFluids.HONEY, 500), new FluidStack(TFCFluids.HONEYWATER, 5000))
                .setSealTime(0).setSealedRecipe(false).setMinTechLevel(0).setRemovesLiquid(false));

    }

    private static void registerAnvilRecipes() {
        Bids.LOG.info("Register TFC anvil recipes");

        // AnvilManager.world needs to have been initialized
        if (AnvilManager.world == null) {
            throw new RuntimeException("AnvilManager not initialized, did we try to add recipes before TFC has?");
        }

        if (AnvilManager.getInstance().getPlan("blowpipe") == null) {
            Bids.LOG.info("Registering blowpipe anvil plan and recipes");
            AnvilManager.getInstance().addPlan("blowpipe", new PlanRecipe(new RuleEnum[]{
                RuleEnum.BENDLAST, RuleEnum.BENDSECONDFROMLAST, RuleEnum.ANY}));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronSheet), null,
                "blowpipe", AnvilReq.WROUGHTIRON, new ItemStack(BidsItems.metalBlowpipe, 1, 1)));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.brassSheet), null,
                "blowpipe", AnvilReq.BRONZE, new ItemStack(BidsItems.brassBlowpipe, 1, 1)));

            Bids.LOG.info("Registering adze anvil plan and recipes");
            AnvilManager.getInstance().addPlan("adze", new PlanRecipe(new RuleEnum[]{RuleEnum.PUNCHLAST, RuleEnum.PUNCHSECONDFROMLAST, RuleEnum.HITTHIRDFROMLAST}));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null,
                "adze", AnvilReq.COPPER, new ItemStack(BidsItems.copperAdzeHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot), null,
                "adze", AnvilReq.BRONZE, new ItemStack(BidsItems.bronzeAdzeHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot), null,
                "adze", AnvilReq.BISMUTHBRONZE, new ItemStack(BidsItems.bismuthBronzeAdzeHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot), null,
                "adze", AnvilReq.BLACKBRONZE, new ItemStack(BidsItems.blackBronzeAdzeHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));

            Bids.LOG.info("Registering drill anvil plan and recipes");
            AnvilManager.getInstance().addPlan("drill", new PlanRecipe(new RuleEnum[]{RuleEnum.HITLAST, RuleEnum.PUNCHNOTLAST, RuleEnum.DRAWNOTLAST }));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null,
                "drill", AnvilReq.COPPER, new ItemStack(BidsItems.copperDrillHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot), null,
                "drill", AnvilReq.BRONZE, new ItemStack(BidsItems.bronzeDrillHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot), null,
                "drill", AnvilReq.BISMUTHBRONZE, new ItemStack(BidsItems.bismuthBronzeDrillHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot), null,
                "drill", AnvilReq.BLACKBRONZE, new ItemStack(BidsItems.blackBronzeDrillHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));

            registerUnfinishedAnvilRecipeHelper(1, TFCItems.copperIngot2x, AnvilReq.STONE);
            registerUnfinishedAnvilRecipeHelper(2, TFCItems.bronzeIngot2x, AnvilReq.COPPER);
            registerUnfinishedAnvilRecipeHelper(3, TFCItems.wroughtIronIngot2x, AnvilReq.BRONZE);
            registerUnfinishedAnvilRecipeHelper(4, TFCItems.steelIngot2x, AnvilReq.WROUGHTIRON);
            registerUnfinishedAnvilRecipeHelper(5, TFCItems.blackBronzeIngot2x, AnvilReq.STEEL);
            registerUnfinishedAnvilRecipeHelper(6, TFCItems.blueSteelIngot2x, AnvilReq.BLACKSTEEL);
            registerUnfinishedAnvilRecipeHelper(7, TFCItems.redSteelIngot2x, AnvilReq.BLACKSTEEL);
            registerUnfinishedAnvilRecipeHelper(8, TFCItems.roseGoldIngot2x, AnvilReq.COPPER);
            registerUnfinishedAnvilRecipeHelper(9, TFCItems.bismuthBronzeIngot2x, AnvilReq.COPPER);
            registerUnfinishedAnvilRecipeHelper(10, TFCItems.blackBronzeIngot2x, AnvilReq.COPPER);
        }
    }

    private static void registerUnfinishedAnvilRecipeHelper(int mat, Item ingot2x, AnvilReq req) {
        AnvilManager.getInstance().addWeldRecipe(new AnvilRecipe(new ItemStack(ingot2x), new ItemStack(ingot2x),
            req, BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 0)));
        AnvilManager.getInstance().addWeldRecipe(new AnvilRecipe(new ItemStack(ingot2x), BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 0),
            req, BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 1)));
        AnvilManager.getInstance().addWeldRecipe(new AnvilRecipe(new ItemStack(ingot2x), BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 1),
            req, BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 2)));
        AnvilManager.getInstance().addWeldRecipe(new AnvilRecipe(new ItemStack(ingot2x), BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 2),
            req, BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 3)));
        AnvilManager.getInstance().addWeldRecipe(new AnvilRecipe(new ItemStack(ingot2x), BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 3),
            req, BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 4)));
        AnvilManager.getInstance().addWeldRecipe(new AnvilRecipe(new ItemStack(ingot2x), BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 4),
            req, BlockUnfinishedAnvil.getFinishedAnvil(mat)));
    }

    private static void registerHandlers() {
        Bids.LOG.info("Register crafting handlers");

        FMLCommonHandler.instance().bus().register(new CraftingHandler());
    }

}
