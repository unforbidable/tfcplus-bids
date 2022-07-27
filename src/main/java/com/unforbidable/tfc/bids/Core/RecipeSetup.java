package com.unforbidable.tfc.bids.Core;

import java.util.Arrays;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.Crafting.AnvilRecipe;
import com.dunk.tfc.api.Crafting.AnvilReq;
import com.dunk.tfc.api.Crafting.CraftingManagerTFC;
import com.dunk.tfc.api.Crafting.KilnCraftingManager;
import com.dunk.tfc.api.Crafting.KilnRecipe;
import com.dunk.tfc.api.Crafting.PlanRecipe;
import com.dunk.tfc.api.Enums.RuleEnum;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeManager;
import com.unforbidable.tfc.bids.Core.Recipes.Actions.ActionToolDamageOreBit;
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.Core.Recipes.Actions.ActionDamageTool;
import com.unforbidable.tfc.bids.Handlers.CraftingHandler;
import com.unforbidable.tfc.bids.Recipes.RecipeCrucibleConversion;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeSetup {

    public static void init() {
        registerOre();
        registerRecipes();
        registerCustomRecipes();
        registerHandlers();
    }

    public static void postInit() {
        registerKnappingRecipes();
        registerKilnRecipes();
    }

    public static void onServerWorldLoad() {
        registerAnvilRecipes();
    }

    public static void onClientWorldInit() {
        registerAnvilRecipes();
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

        for (int i = 0; i < Global.WOOD_ALL.length; i++) {
            int j = i % 16;

            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.peeledLog, 1, i),
                    new ItemStack(TFCItems.logs, 1, i * 2), "itemAdze"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.peeledLog, 1, i),
                    new ItemStack(TFCItems.logs, 1, i * 2 + 1), "itemAdze", "itemAxe"));

            if (WoodHelper.canBuildLogWall(i)) {
                if (i < 16) {
                    Block logWall = WoodHelper.getDefaultLogWallBlock(0);
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWall, 1, j),
                            "A ", "11", '1', new ItemStack(BidsItems.peeledLog, 1, i),
                            'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLog, 2, i),
                            Arrays.asList(new ItemStack(logWall, 1, j))));
                } else if (i < 32) {
                    Block logWall = WoodHelper.getDefaultLogWallBlock(16);
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWall, 1, j),
                            "A ", "11", '1', new ItemStack(BidsItems.peeledLog, 1, i),
                            'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLog, 2, i),
                            Arrays.asList(new ItemStack(logWall, 1, j))));
                } else if (i < 48) {
                    Block logWall = WoodHelper.getDefaultLogWallBlock(32);
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(logWall, 1, j),
                            "A ", "11", '1', new ItemStack(BidsItems.peeledLog, 1, i),
                            'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(new ItemStack(BidsItems.peeledLog, 2, i),
                            Arrays.asList(new ItemStack(logWall, 1, j))));
                }
            }

            // Copies of TFC recipes for items made logs
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 1),
                    new ItemStack(BidsItems.peeledLog, 1, i), "itemKnife"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    "X ", "LX", 'L', new ItemStack(BidsItems.peeledLog, 1, i), 'X', "lumpClay"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
                    " X", "XL", 'L', new ItemStack(BidsItems.peeledLog, 1, i), 'X', "lumpClay"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.paddle, 1),
                    new ItemStack(TFCItems.pole, 1), new ItemStack(BidsItems.peeledLog, 1, i), "itemKnife"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.singlePlank, 8, i),
                    new ItemStack(BidsItems.peeledLog), "itemSaw"));

            // Copies of TFC recipes for block made from logs
            if (i < 16) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLog, 1, i), 'A', "itemSaw"));
            } else if (i < 32) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV2, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLog, 1, i), 'A', "itemSaw"));
            } else if (i < 48) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.woodSupportV3, 8, j),
                        "A2", " 2", '2', new ItemStack(BidsItems.peeledLog, 1, i), 'A', "itemSaw"));
            }
        }

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAdze", "itemAxe")
                .matchCraftingItem(BidsItems.peeledLog));

        for (int i = 0; i < 3; i++) {
            Block logWall = WoodHelper.getDefaultLogWallBlock(i * 16);
            RecipeManager.addAction(new ActionDamageTool(2)
                    .addTools("itemAdze")
                    .matchCraftingBlock(logWall));
        }
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
