package com.unforbidable.tfc.bids.Core;

import java.util.Arrays;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.Crafting.AnvilRecipe;
import com.dunk.tfc.api.Crafting.AnvilReq;
import com.dunk.tfc.api.Crafting.BarrelFireRecipe;
import com.dunk.tfc.api.Crafting.BarrelManager;
import com.dunk.tfc.api.Crafting.BarrelMultiItemRecipe;
import com.dunk.tfc.api.Crafting.ClothingManager;
import com.dunk.tfc.api.Crafting.CraftingManagerTFC;
import com.dunk.tfc.api.Crafting.KilnCraftingManager;
import com.dunk.tfc.api.Crafting.KilnRecipe;
import com.dunk.tfc.api.Crafting.PlanRecipe;
import com.dunk.tfc.api.Crafting.SewingPattern;
import com.dunk.tfc.api.Crafting.SewingRecipe;
import com.dunk.tfc.api.Enums.RuleEnum;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeManager;
import com.unforbidable.tfc.bids.Core.Recipes.Actions.ActionToolDamageOreBit;
import com.unforbidable.tfc.bids.Core.Recipes.TFC.BarrelItemDemandingRecipe;
import com.unforbidable.tfc.bids.Core.Seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.Core.Recipes.Actions.ActionCopySeasoning;
import com.unforbidable.tfc.bids.Core.Recipes.Actions.ActionDamageTool;
import com.unforbidable.tfc.bids.Core.Recipes.Actions.ActionExtraDrop;
import com.unforbidable.tfc.bids.Handlers.CraftingHandler;
import com.unforbidable.tfc.bids.Recipes.RecipeCrucibleConversion;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.DryingManager;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Crafting.FoodDryingRecipe;
import com.unforbidable.tfc.bids.api.Crafting.SaddleQuernManager;
import com.unforbidable.tfc.bids.api.Crafting.SaddleQuernRecipe;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningManager;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningRecipe;
import com.unforbidable.tfc.bids.api.Enums.EnumWoodHardness;
import com.unforbidable.tfc.bids.api.Crafting.CarvingManager;
import com.unforbidable.tfc.bids.api.Crafting.CarvingRecipe;
import com.unforbidable.tfc.bids.api.Crafting.CarvingRecipePattern;
import com.unforbidable.tfc.bids.api.Crafting.ChoppingBlockManager;
import com.unforbidable.tfc.bids.api.Crafting.ChoppingBlockRecipe;

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

public class RecipeSetup {

    public static void init() {
        registerOre();
        registerRecipes();
        registerCustomRecipes();
        registerCarvingRecipes();
        registerSaddleQuernRecipes();
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

            if (WoodHelper.canMakeChoppingBlock(i)) {
                if (i < 16) {
                    OreDictionary.registerOre("blockChoppingBlock", new ItemStack(BidsBlocks.choppingBlock, 1, j));
                } else if (i < 32) {
                    OreDictionary.registerOre("blockChoppingBlock", new ItemStack(BidsBlocks.choppingBlock2, 1, j));
                } else {
                    OreDictionary.registerOre("blockChoppingBlock", new ItemStack(BidsBlocks.choppingBlock3, 1, j));
                }
            }
        }

    }

    private static void registerCustomRecipes() {
        Bids.LOG.info("Register custom recipes");

        // TODO: register with net.minecraftforge.oredict.RecipeSorter
        GameRegistry.addRecipe(new RecipeCrucibleConversion(true));
        GameRegistry.addRecipe(new RecipeCrucibleConversion(false));
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
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.sedRoughStoneLooseBrick, 4, j),
                    new ItemStack(BidsBlocks.roughStoneSed, 1, j), "itemAdze"));
            GameRegistry.addRecipe(
                    new ItemStack(BidsBlocks.roughStoneBrickSed, 1, j),
                    "BB", "BB", 'B', new ItemStack(BidsItems.sedRoughStoneLooseBrick, 1, j));
        }

        RecipeManager.addAction(new ActionDamageTool(4)
                .addTools("itemAdze")
                .matchCraftingItem(BidsItems.sedRoughStoneLooseBrick));

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
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLogSeasoned, 2, i),
                            Arrays.asList(new ItemStack(logWall, 1, j))));
                } else if (i < 32) {
                    Block logWall = WoodHelper.getDefaultLogWallBlock(16);
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWall, 1, j),
                            "A ", "11", '1', new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                            'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLogSeasoned, 2, i),
                            Arrays.asList(new ItemStack(logWall, 1, j))));
                } else if (i < 48) {
                    Block logWall = WoodHelper.getDefaultLogWallBlock(32);
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWall, 1, j),
                            "A ", "11", '1', new ItemStack(BidsItems.peeledLogSeasoned, 1, i),
                            'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLogSeasoned, 2, i),
                            Arrays.asList(new ItemStack(logWall, 1, j))));
                }
            }

            if (EnumWoodHardness.fromDamage(i) == EnumWoodHardness.HARD) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.plugAndFeather, 4),
                        new ItemStack(BidsItems.peeledLogSeasoned, 1, i), "itemAdze"));
            }

            // Copies of TFC recipes for items made logs
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 1),
                    new ItemStack(BidsItems.peeledLog, 1, i), "itemKnife"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 1),
                    new ItemStack(BidsItems.peeledLogSeasoned, 1, i), "itemKnife"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 1),
                    new ItemStack(BidsItems.logsSeasoned, 1, i * 2), "itemKnife"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 1),
                    new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1), "itemKnife"));

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    "X ", "LX", 'L', new ItemStack(BidsItems.peeledLog, 1, i), 'X', "lumpClay"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    "X ", "LX", 'L', new ItemStack(BidsItems.peeledLogSeasoned, 1, i), 'X', "lumpClay"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    "X ", "LX", 'L', new ItemStack(BidsItems.logsSeasoned, 1, i * 2), 'X', "lumpClay"));

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    " X", "XL", 'L', new ItemStack(BidsItems.peeledLog, 1, i), 'X', "lumpClay"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    " X", "XL", 'L', new ItemStack(BidsItems.peeledLogSeasoned, 1, i), 'X', "lumpClay"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    " X", "XL", 'L', new ItemStack(BidsItems.logsSeasoned, 1, i * 2), 'X', "lumpClay"));

            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.paddle, 1),
                    new ItemStack(TFCItems.pole, 1), new ItemStack(BidsItems.peeledLog, 1, i), "itemKnife"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.paddle, 1),
                    new ItemStack(TFCItems.pole, 1), new ItemStack(BidsItems.peeledLogSeasoned, 1, i), "itemKnife"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.paddle, 1),
                    new ItemStack(TFCItems.pole, 1), new ItemStack(BidsItems.logsSeasoned, 1, i * 2), "itemKnife"));

            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.singlePlank, 8, i),
                    new ItemStack(BidsItems.logsSeasoned, 1, i * 2), "itemSaw"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.singlePlank, 8, i),
                    new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1), "itemSaw"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.singlePlank, 8, i),
                    new ItemStack(BidsItems.peeledLogSeasoned, 1, i), "itemSaw"));

            // Copies of TFC recipes for block made from logs
            if (i < 16) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLog, 1, i), 'A', "itemSaw"));
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLogSeasoned, 1, i), 'A', "itemSaw"));
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.logsSeasoned, 1, i * 2), 'A', "itemSaw"));
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1), 'A', "itemSaw"));
            } else if (i < 32) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV2, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLog, 1, i), 'A', "itemSaw"));
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV2, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLogSeasoned, 1, i), 'A', "itemSaw"));
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV2, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.logsSeasoned, 1, i * 2), 'A', "itemSaw"));
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV2, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.logsSeasoned, 1, i * 2 + 1), 'A', "itemSaw"));
            } else if (i < 48) {
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

        DryingManager.addRecipe(new DryingRecipe(new ItemStack(BidsItems.barkFibreStrip, 1, 1),
                new ItemStack(BidsItems.barkFibreStrip, 1, 0), 24, false));

        // Meat and cheese drying from TFC
        final Item[] foodToDry = new Item[] { TFCItems.venisonRaw, TFCItems.beefRaw, TFCItems.chickenRaw,
                TFCItems.porkchopRaw, TFCItems.fishRaw, TFCItems.seastarRaw, TFCItems.scallopRaw,
                TFCItems.calamariRaw, TFCItems.muttonRaw, TFCItems.horseMeatRaw, TFCItems.cheese };
        for (Item food : foodToDry) {
            DryingManager.addRecipe(new FoodDryingRecipe(new ItemStack(food), 24, true));
        }

        // Extra food drying
        DryingManager.addRecipe(new FoodDryingRecipe(new ItemStack(TFCItems.seaWeed), 12, true));

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
                .matchCraftingItem(BidsItems.peeledLog));

        RecipeManager.addAction(new ActionExtraDrop()
                .addExtraDrop(new ItemStack(BidsItems.bark, 1, OreDictionary.WILDCARD_VALUE),
                        BidsOptions.Bark.dropPeelingSeasonedChance)
                .matchCraftingItem(BidsItems.peeledLogSeasoned));

        RecipeManager.addAction(new ActionExtraDrop()
                .addExtraDrop(new ItemStack(BidsItems.bark, 1, OreDictionary.WILDCARD_VALUE),
                        BidsOptions.Bark.dropSplittingChance)
                .matchIngredient(TFCItems.logs)
                .matchCraftingItem(BidsItems.firewood));

        RecipeManager.addAction(new ActionExtraDrop()
                .addExtraDrop(new ItemStack(BidsItems.bark, 1, OreDictionary.WILDCARD_VALUE),
                        BidsOptions.Bark.dropSplittingSeasonedChance)
                .matchIngredient(BidsItems.logsSeasoned)
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
            RecipeManager.addAction(new ActionDamageTool(2)
                    .addTools("itemAdze")
                    .matchCraftingBlock(logWall));
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

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.clayCrucible, 1, 1),
                new Object[] { "#####", " ### ", " ### ", " ### ", "     ", '#',
                        new ItemStack(TFCItems.flatClay, 1, 1) });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.fireClayCrucible, 1),
                new Object[] { "#####", " ### ", " ### ", " ### ", "     ", '#',
                        new ItemStack(TFCItems.flatClay, 1, 3) });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.clayMug, 2),
                new Object[] { "#####", "#####", "    #", "   # ", "    #", '#',
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
    }

    private static void registerBarrelRecipes() {
        Bids.LOG.info("Register TFC barrel recipes");

        for (int i = 0; i < Global.WOOD_ALL.length; i++) {
            // Retting fibers from bark
            if (WoodHelper.canBarkMakeFibers(i)) {
                BarrelManager.getInstance().addRecipe(new BarrelMultiItemRecipe(
                        new ItemStack(BidsItems.bark, 1, i), new FluidStack(TFCFluids.FRESHWATER, 625),
                        new ItemStack(BidsItems.barkFibre, 1, 0), new FluidStack(TFCFluids.FRESHWATER, 500))
                        .setSealTime(48).setMinTechLevel(0));
            }

            // Extracting tannin from bark
            if (WoodHelper.canBarkMakeTannin(i)) {
                BarrelManager.getInstance().addRecipe(new BarrelItemDemandingRecipe(
                        new ItemStack(BidsItems.bark, 1, i), new FluidStack(TFCFluids.FRESHWATER, 625),
                        null, new FluidStack(TFCFluids.TANNIN, 500))
                        .setMinTechLevel(0));
            }
        }

        BarrelManager.getInstance().addRecipe(new BarrelFireRecipe(
                ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatCrushed), 5),
                new FluidStack(TFCFluids.FRESHWATER, 100),
                ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatPorridge), 6),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4, 4000));
        BarrelManager.getInstance().addRecipe(new BarrelFireRecipe(
                ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyCrushed), 5),
                new FluidStack(TFCFluids.FRESHWATER, 100),
                ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyPorridge), 6),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4, 4000));
        BarrelManager.getInstance().addRecipe(new BarrelFireRecipe(
                ItemFoodTFC.createTag(new ItemStack(BidsItems.oatCrushed), 5),
                new FluidStack(TFCFluids.FRESHWATER, 100),
                ItemFoodTFC.createTag(new ItemStack(BidsItems.oatPorridge), 6),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4, 4000));
        BarrelManager.getInstance().addRecipe(new BarrelFireRecipe(
                ItemFoodTFC.createTag(new ItemStack(BidsItems.riceCrushed), 5),
                new FluidStack(TFCFluids.FRESHWATER, 100),
                ItemFoodTFC.createTag(new ItemStack(BidsItems.ricePorridge), 6),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4, 4000));
        BarrelManager.getInstance().addRecipe(new BarrelFireRecipe(
                ItemFoodTFC.createTag(new ItemStack(BidsItems.ryeCrushed), 5),
                new FluidStack(TFCFluids.FRESHWATER, 100),
                ItemFoodTFC.createTag(new ItemStack(BidsItems.ryePorridge), 6),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4, 4000));
        BarrelManager.getInstance().addRecipe(new BarrelFireRecipe(
                ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealCrushed), 5),
                new FluidStack(TFCFluids.FRESHWATER, 100),
                ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealPorridge), 6),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4, 4000));
    }

    private static void registerAnvilRecipes() {
        Bids.LOG.info("Register TFC anvil recipes");

        // AnvilManager.world needs to have been initialized
        if (AnvilManager.world == null) {
            throw new RuntimeException("AnvilManager not initialized, did we try to add recipes before TFC has?");
        }

        if (AnvilManager.getInstance().getPlan("blowpipe") == null) {
            Bids.LOG.info("Registering blowpipe anvil plan and recipe");
            AnvilManager.getInstance().addPlan("blowpipe", new PlanRecipe(new RuleEnum[] {
                    RuleEnum.BENDLAST, RuleEnum.BENDSECONDFROMLAST, RuleEnum.ANY }));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronSheet), null,
                    "blowpipe", AnvilReq.WROUGHTIRON, new ItemStack(BidsItems.metalBlowpipe, 1, 1)));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.brassSheet), null,
                    "blowpipe", AnvilReq.BRONZE, new ItemStack(BidsItems.brassBlowpipe, 1, 1)));
        }
    }

    private static void registerHandlers() {
        Bids.LOG.info("Register crafting handlers");

        FMLCommonHandler.instance().bus().register(new CraftingHandler());
    }

}
