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
import com.unforbidable.tfc.bids.Core.Cooking.CookingMixtureHelper;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;
import com.unforbidable.tfc.bids.Core.Recipes.Actions.*;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeHelper;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeManager;
import com.unforbidable.tfc.bids.Core.Recipes.TFC.BarrelRecipeBuilder;
import com.unforbidable.tfc.bids.Core.Recipes.TFC.BarrelRecipeManager;
import com.unforbidable.tfc.bids.Core.Seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.Core.Stone.EnumStoneBlockType;
import com.unforbidable.tfc.bids.Core.Stone.EnumStoneItemType;
import com.unforbidable.tfc.bids.Core.Stone.StoneIndex;
import com.unforbidable.tfc.bids.Core.Stone.StoneScheme;
import com.unforbidable.tfc.bids.Core.Wood.EnumWoodItemType;
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.Core.Wood.WoodIndex;
import com.unforbidable.tfc.bids.Core.Wood.WoodScheme;
import com.unforbidable.tfc.bids.Handlers.CraftingHandler;
import com.unforbidable.tfc.bids.Recipes.RecipeCrucibleConversion;
import com.unforbidable.tfc.bids.Recipes.RecipeEmptyCookingPot;
import com.unforbidable.tfc.bids.api.*;
import com.unforbidable.tfc.bids.api.Crafting.*;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Collections;

import static com.dunk.tfc.Core.Recipes.getStackNoTemp;

public class RecipeSetup {

    public static void init() {
        registerRecipes();
        registerCustomRecipes();
        registerCarvingRecipes();
        registerSaddleQuernRecipes();
        registerStonePressRecipes();
        registerScrewPressRecipes();
        registerCookingRecipes();
        registerPrepRecipes();
        registerChurningRecipes();
        registerProcessingSurfaceRecipes();
        registerSoakingSurfaceRecipes();
        registerHandworkRecipes();
        registerHandlers();
    }

    public static void postInit() {
        registerDoughRecipes();
        registerKnappingRecipes();
        registerSewingRecipes();
        registerKilnRecipes();
        registerBarrelRecipes();
        registerLoomRecipes();
    }

    private static void registerDoughRecipes() {
        addFoodDoughRecipe(TFCItems.wheatGround, BidsItems.wheatDoughUnshaped);
        addFoodDoughRecipe(TFCItems.barleyGround, BidsItems.barleyDoughUnshaped);
        addFoodDoughRecipe(TFCItems.ryeGround, BidsItems.ryeDoughUnshaped);
        addFoodDoughRecipe(TFCItems.oatGround, BidsItems.oatDoughUnshaped);
        addFoodDoughRecipe(TFCItems.riceGround, BidsItems.riceDoughUnshaped);
        addFoodDoughRecipe(TFCItems.cornmealGround, BidsItems.cornmealDoughUnshaped);

        addFoodDoughRecipe(BidsItems.wheatCrushed, BidsItems.wheatDoughFlatbread);
        addFoodDoughRecipe(BidsItems.barleyCrushed, BidsItems.barleyFlatbread);
        addFoodDoughRecipe(BidsItems.ryeCrushed, BidsItems.ryeDoughFlatbread);
        addFoodDoughRecipe(BidsItems.oatCrushed, BidsItems.oatDoughFlatbread);
        addFoodDoughRecipe(BidsItems.riceCrushed, BidsItems.riceDoughFlatbread);
        addFoodDoughRecipe(BidsItems.cornmealCrushed, BidsItems.cornmealDoughFlatbread);
    }

    private static void addFoodDoughRecipe(Item foodInput, Item foodOutput) {
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemFoodTFC.createTag(new ItemStack(foodOutput, 1)),
            ItemFoodTFC.createTag(new ItemStack(foodInput, 1)), "itemLargeBowlWater"));
    }

    public static void onServerWorldLoad() {
        registerAnvilRecipes();
        registerSewingRepairRecipes();
    }

    public static void onClientWorldInit() {
        registerAnvilRecipes();
        registerSewingRepairRecipes();
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

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            GameRegistry.addRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY, 2),
                "PB", "BB", 'P', new ItemStack(BidsItems.clayPipe, 1, 1),
                'B', stone.items.getItem(EnumStoneItemType.MUD_BRICK));
            GameRegistry.addRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY, 2),
                "PB", "BB", 'P', new ItemStack(TFCItems.logs, 1, 48), // Bamboo
                'B', stone.items.getItem(EnumStoneItemType.MUD_BRICK));

            GameRegistry.addRecipe(new ShapedOreRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE, 4),
                "SA", "  ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze"));
            GameRegistry.addRecipe(new ShapedOreRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE, 4),
                "AS", "  ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze"));

            GameRegistry.addRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_TILES),
                "BB", "  ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE));
            GameRegistry.addRecipe(new ShapelessRecipes(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE, 2),
                Collections.singletonList(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_TILES))));

            GameRegistry.addRecipe(new ShapedOreRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK, 4),
                "S ", "A ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze"));
            GameRegistry.addRecipe(new ShapedOreRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK, 4),
                "A ", "S ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze"));

            GameRegistry.addRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_BRICKS),
                "BB", "  ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK));
            GameRegistry.addRecipe(new ShapelessRecipes(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK, 2),
                Collections.singletonList(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_BRICKS))));

            GameRegistry.addRecipe(new ShapelessOreRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.SMOOTH_STONE, 2),
                stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), "itemChisel"));

            GameRegistry.addRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_BRICK_FENCE, 2),
                "B ", "B ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK));
            GameRegistry.addShapelessRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK),
                stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_BRICK_FENCE, 2));

            GameRegistry.addRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_TILE_FENCE, 2),
                "B ", "B ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE));
            GameRegistry.addShapelessRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE),
                stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_TILE_FENCE, 2));

            GameRegistry.addRecipe(new ShapelessOreRecipe(stone.items.getItem(EnumStoneItemType.STONE_BRICK),
                stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK), "itemChisel"));
        }

        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemAdze")
            .matchCraftingItem(BidsItems.roughStoneBrick));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemAdze")
            .matchCraftingItem(BidsItems.roughStoneTile));

        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemChisel")
            .matchCraftingBlock(TFCBlocks.stoneSedSmooth));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemChisel")
            .matchCraftingBlock(TFCBlocks.stoneMMSmooth));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemChisel")
            .matchCraftingBlock(TFCBlocks.stoneIgInSmooth));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemChisel")
            .matchCraftingBlock(TFCBlocks.stoneIgExSmooth));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.whorl, 1, 0),
            "itemRock", "itemDrillHead"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.spindle, 1, 0),
            "itemWhorl", "stickWood"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.primitiveRopeMaker, 1, 0),
            "stickWood", "stickWood", "materialBindingStrong", "itemKnife"));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemKnife")
            .matchCraftingItem(BidsItems.primitiveRopeMaker));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.thornCard, 1, 0),
            BidsItems.thornBunch, BidsItems.thornBunch, TFCItems.resin, BidsItems.woodenCombPaddle));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.boneHeckle, 1, 0),
            BidsItems.boneKnifeHead, BidsItems.boneKnifeHead, TFCItems.resin, "materialBindingDecent"));

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
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.wroughtIronAdze, 1), "#", "I", '#', new ItemStack(BidsItems.wroughtIronAdzeHead, 1, 0), 'I', "stickWood"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.copperDrill, 1, 0),
            BidsItems.copperDrillHead, "stickWood", TFCItems.bow));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.bronzeDrill, 1, 0),
            BidsItems.bronzeDrillHead, "stickWood", TFCItems.bow));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.bismuthBronzeDrill, 1, 0),
            BidsItems.bismuthBronzeDrillHead, "stickWood", TFCItems.bow));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.blackBronzeDrill, 1, 0),
            BidsItems.blackBronzeDrillHead, "stickWood", TFCItems.bow));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.wroughtIronDrill, 1, 0),
            BidsItems.wroughtIronDrillHead, "stickWood", TFCItems.bow));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.smallStickBundle),
                "stickWood", "stickWood", "stickWood"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.stick, 3, 0),
                new ItemStack(BidsItems.smallStickBundle)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.kindling),
                "stickWood", "stickWood", "stickWood", new ItemStack(TFCItems.straw)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.kindling),
                new ItemStack(BidsItems.smallStickBundle), new ItemStack(TFCItems.straw)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.barkFibreKindling),
                "stickWood", "stickWood", "stickWood", new ItemStack(BidsItems.barkFibreCoarse, 1, 1)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.barkFibreKindling),
                new ItemStack(BidsItems.smallStickBundle), new ItemStack(BidsItems.barkFibreCoarse, 1, 1)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.birchBarkKindling),
                "stickWood", "stickWood", "stickWood", new ItemStack(BidsItems.birchBarkStrap)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.birchBarkKindling),
                new ItemStack(BidsItems.smallStickBundle), new ItemStack(BidsItems.birchBarkStrap)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.tiedStickBundle),
                new ItemStack(BidsItems.smallStickBundle), new ItemStack(BidsItems.smallStickBundle),
                new ItemStack(BidsItems.smallStickBundle), TFCItems.grassCordage));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.tiedStickBundle),
                new ItemStack(TFCItems.stickBundle), TFCItems.grassCordage));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.stick, 9, 0),
                new ItemStack(BidsItems.tiedStickBundle)));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.dryingRack),
                "PP", "SS", 'P', new ItemStack(TFCItems.pole),
                'S', "materialBindingStrong"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 2, 0),
                new ItemStack(BidsBlocks.dryingRack)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.barkFibre),
                "itemBarkHasFibers", "itemKnife"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.juteStalk),
            TFCItems.jute, "itemKnife"));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemKnife")
            .matchCraftingItem(BidsItems.juteStalk));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.flaxStalk),
            TFCItems.flax, "itemKnife"));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemKnife")
            .matchCraftingItem(BidsItems.flaxStalk));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.cottonBollRefined),
            BidsItems.cottonBoll, "itemKnife"));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemKnife")
            .matchCraftingItem(BidsItems.cottonBollRefined));

        // Refining TFC cotton in case it has not been converted
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.cottonBollRefined),
            TFCItems.cotton, "itemKnife"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.birchBarkCup, 1, 0),
                BidsItems.birchBarkCupUnfinished, Items.slime_ball));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.birchBarkSheet, 1, 0),
                new ItemStack(BidsItems.bark, 1, 2), "itemKnife"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.wattleGate),
                "PW", "  ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.wattleGate),
                "WP", "  ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.wattleTrapdoor),
                "P ", "W ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.wattleTrapdoor),
                "W ", "P ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle));

        // Select TFC recipes where new cordage and twines can be used
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.primitiveLoom, 1, 0),
                "LS", "SL", 'L', "stickWood", 'S', "materialBindingStrong"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCBlocks.primitiveLoom, 1, 0),
                "LS", "SL", 'S', "stickWood", 'L', "materialBindingStrong"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.unstrungBow, 1),
                new ItemStack(TFCItems.pole), "itemKnife", "materialBindingStrong"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.bow, 1),
                new ItemStack(TFCItems.unstrungBow), "materialBindingStrong"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.splint, 1),
                TFCItems.stick, "materialBindingStrong"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.compositeBow),
                TFCItems.unstrungCompositeBow, "materialBindingStrong"));

        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.items.hasPeeledLog()) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(wood.items.getPeeledLog(),
                    wood.items.getLog(), "itemAdze"));
                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
                    wood.items.getPeeledLog(), wood.items.getLog()));

                if (wood.items.hasChoppedLog()) {
                    GameRegistry.addRecipe(new ShapelessOreRecipe(wood.items.getPeeledLog(),
                        wood.items.getChoppedLog(), "itemAdze"));
                    ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
                        wood.items.getPeeledLog(), wood.items.getChoppedLog()));
                }
            }

            if (wood.items.hasSeasonedPeeledLog()) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(wood.items.getSeasonedPeeledLog(),
                    wood.items.getSeasonedLog(), "itemAdze"));
                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
                    wood.items.getSeasonedPeeledLog(), wood.items.getSeasonedLog()));

                if (wood.items.hasSeasonedChoppedLog()) {
                    GameRegistry.addRecipe(new ShapelessOreRecipe(wood.items.getSeasonedPeeledLog(),
                        wood.items.getSeasonedChoppedLog(), "itemAdze"));
                    ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
                        wood.items.getSeasonedPeeledLog(), wood.items.getSeasonedChoppedLog()));
                }

                SeasoningManager.addRecipe(new SeasoningRecipe(wood.items.getSeasonedPeeledLog(),
                    wood.items.getPeeledLog(),
                    SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.PEELED_LOG)));
            }

            if (wood.items.hasSeasonedLog()) {
                SeasoningManager.addRecipe(new SeasoningRecipe(wood.items.getSeasonedLog(),
                    wood.items.getLog(),
                    SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.LOG)));

                if (wood.items.hasChoppedLog()) {
                    SeasoningManager.addRecipe(new SeasoningRecipe(wood.items.getSeasonedChoppedLog(),
                        wood.items.getChoppedLog(),
                        SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.CHOPPED_LOG)));
                }
            }

            if (wood.items.hasFirewood()) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(wood.items.getFirewood(),wood.getOreWithSuffix("logWoodFresh"), "itemAxe"));

                ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                    wood.items.getFirewood(),
                    wood.items.getLog()));

                if (wood.items.hasChoppedLog()) {
                    ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        wood.items.getFirewood(),
                        wood.items.getChoppedLog()));
                }

                if (wood.items.hasPeeledLog()) {
                    ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        wood.items.getFirewood(),
                        wood.items.getPeeledLog()));
                }
            }

            if (wood.items.hasSeasonedFirewood()) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(wood.items.getSeasonedFirewood(), wood.getOreWithSuffix("logWoodSeasoned"), "itemAxe"));

                if (wood.items.hasSeasonedLog()) {
                    ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        wood.items.getSeasonedFirewood(),
                        wood.items.getSeasonedLog()));
                }

                if (wood.items.hasSeasonedChoppedLog()) {
                    ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        wood.items.getSeasonedFirewood(),
                        wood.items.getSeasonedChoppedLog()));
                }

                if (wood.items.hasSeasonedPeeledLog()) {
                    ChoppingBlockManager.addRecipe(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        wood.items.getSeasonedFirewood(),
                        wood.items.getSeasonedPeeledLog()));
                }

                SeasoningManager.addRecipe(new SeasoningRecipe(wood.items.getSeasonedFirewood(),
                    wood.items.getFirewood(),
                    SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.FIREWOOD)));
            }

            if (wood.blocks.hasLogWall()) {
                if (wood.items.hasSeasonedPeeledLog()) {
                    GameRegistry.addRecipe(new ShapedOreRecipe(wood.blocks.getLogWall(),
                        "A ", "11", '1', wood.getOreWithSuffix("logWoodSeasoned"),
                        'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(wood.items.getSeasonedPeeledLog(2),
                        Collections.singletonList(wood.blocks.getLogWall())));

                    GameRegistry.addRecipe(new ShapedOreRecipe(wood.blocks.getLogWallVert(),
                        "A1", " 1", '1', wood.getOreWithSuffix("logWoodSeasoned"),
                        'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(wood.items.getSeasonedPeeledLog(2),
                        Collections.singletonList(wood.blocks.getLogWallVert())));
                } else {
                    GameRegistry.addRecipe(new ShapedOreRecipe(wood.blocks.getLogWall(),
                        "A ", "11", '1', wood.getOreWithSuffix("logWood"),
                        'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(wood.items.getLog(2),
                        Collections.singletonList(wood.blocks.getLogWall())));

                    GameRegistry.addRecipe(new ShapedOreRecipe(wood.blocks.getLogWallVert(),
                        "A1", " 1", '1', wood.getOreWithSuffix("logWood"),
                        'A', "itemAdze"));
                    GameRegistry.addRecipe(new ShapelessRecipes(wood.items.getLog(2),
                        Collections.singletonList(wood.blocks.getLogWallVert())));
                }
            }

            if (wood.blocks.hasPalisade()) {
                GameRegistry.addRecipe(new ShapedOreRecipe(wood.blocks.getPalisade(2),
                    "A1", " 1", '1', wood.getOreWithSuffix("logWood"),
                    'A', "itemAxe"));
            }

            // Copies of TFC recipes for items made logs
            if (wood.items.hasLumber()) {
                GameRegistry.addRecipe(new ShapelessOreRecipe(wood.items.getLumber(8), wood.getOreWithSuffix("logWoodSeasoned"), "itemSaw"));
            }

            // Copies of TFC recipes for block made from logs
            if (wood.items.hasPeeledLog() || wood.items.hasSeasonedLog()) {
                GameRegistry.addRecipe(new ShapedOreRecipe(wood.blocks.getWoodSupport(8),"A2", " 2",
                    '2', wood.getOreWithSuffix("logWood"),
                    'A', "itemSaw"));

                GameRegistry.addRecipe(new ShapedOreRecipe(wood.blocks.getFence(6),"LPL", "LPL",
                    'L', wood.getOreWithSuffix("logWood"),
                    'P', wood.items.getLumber()));
            }
        }

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.woodenSpear, 1),
            TFCItems.pole, "itemHandAxe"));

        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemHandAxe")
            .matchCraftingItem(TFCItems.woodenSpear));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.plugAndFeather, 4),
            "logWoodPlugAndFeather", "itemAdze"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(BidsItems.moreHide, 1, 0), new ItemStack(BidsItems.moreHide, 1, 0),
            "itemNeedleAndThread"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.hide, 1, 0), new ItemStack(TFCItems.hide, 1, 0),
            "itemNeedleAndThread"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.hide, 1, 1), new ItemStack(TFCItems.hide, 1, 1),
            "itemNeedleAndThread"));

        RecipeManager.addAction(new ActionDamageTool(10)
            .addTools("itemNeedleAndThread")
            .matchCraftingItem(TFCItems.hide, 0));

        RecipeManager.addAction(new ActionDamageTool(20)
            .addTools("itemNeedleAndThread")
            .matchCraftingItem(TFCItems.hide, 1));

        RecipeManager.addAction(new ActionDamageTool(40)
            .addTools("itemNeedleAndThread")
            .matchCraftingItem(TFCItems.hide, 2));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.moreHide, 2),
            new ItemStack(TFCItems.hide, 1, 0), "itemKnife"));

        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemKnife")
            .matchCraftingItem(BidsItems.moreHide, 0));

        // Copies of TFC recipes for generic wood items made logs
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.pole, 1),
            "logWoodAny", "itemKnife"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
            " X", "XL", 'L', "logWoodAny", 'X', "lumpClay"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.clayTile, 1, 0),
            "X ", "LX", 'L', "logWoodAny", 'X', "lumpClay"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.paddle, 1),
            new ItemStack(TFCItems.pole, 1), "logWoodAny", "itemKnife"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.quern, 1), "  W", "PPP", 'P', "stoneQuern", 'W', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCItems.millstone, 1), "PPP", "P P", "PPP", 'P', "stoneQuern"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.woodAxleWallBearing), "LSL", "L L", "LSL",
            'L', "woodLumber", 'S', "supportWood"));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsBlocks.woodScrew),
            new ItemStack(TFCBlocks.woodAxle, 1), "itemChisel"));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemChisel")
            .matchCraftingBlock(BidsBlocks.woodScrew));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.screwPressRackBottom), "SLS", "S S", "SLS",
            'L', "woodLumber", 'S', "supportWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.screwPressRackBridge), "SSS", "L L", "SSS",
            'L', "woodLumber", 'S', "supportWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.screwPressBarrel), "LTL", "LLL", "LPL",
            'T', "plateToolMetal", 'L', "woodLumber", 'P', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.screwPressDisc), "L L", "LPL", "   ",
            'L', "woodLumber", 'P', "plankWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsBlocks.screwPressLever), "LTL", " L ", " L ",
            'T', "itemSaw", 'L', "logWoodPeeledSeasoned"));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemSaw")
            .matchCraftingBlock(BidsBlocks.screwPressLever));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BidsItems.woodenPailEmpty, 1),
            "w  ", "wxw", " w ", 'w', "woodLumber", 'x', "plateToolMetal"));

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

        GameRegistry.addShapelessRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.butter, 1)),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.butter, 1)), new ItemStack(TFCItems.powder, 1, 9));

        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.bambooShoot), 2.5f),
            new ItemStack(TFCBlocks.sapling2, 1, 8), "itemKnife"));
        RecipeManager.addAction(new ActionDamageTool(1)
            .addTools("itemKnife")
            .matchCraftingItem(BidsItems.bambooShoot));

        // Manual seed conversion
        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.seedsBarley), new ItemStack(BidsItems.seedsNewBarley));
        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.seedsOat), new ItemStack(BidsItems.seedsNewOat));
        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.seedsRye), new ItemStack(BidsItems.seedsNewRye));
        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.seedsWheat), new ItemStack(BidsItems.seedsNewWheat));
        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.seedsOnion), new ItemStack(BidsItems.seedsNewOnion));
        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.seedsCabbage), new ItemStack(BidsItems.seedsNewCabbage));
        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.seedsGarlic), new ItemStack(BidsItems.seedsNewGarlic));
        GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.seedsCarrot), new ItemStack(BidsItems.seedsNewCarrot));

        // Reverse manual seed conversion
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.seedsNewBarley), new ItemStack(TFCItems.seedsBarley));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.seedsNewOat), new ItemStack(TFCItems.seedsOat));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.seedsNewRye), new ItemStack(TFCItems.seedsRye));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.seedsNewWheat), new ItemStack(TFCItems.seedsWheat));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.seedsNewOnion), new ItemStack(TFCItems.seedsOnion));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.seedsNewCabbage), new ItemStack(TFCItems.seedsCabbage));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.seedsNewGarlic), new ItemStack(TFCItems.seedsGarlic));
        GameRegistry.addShapelessRecipe(new ItemStack(BidsItems.seedsNewCarrot), new ItemStack(TFCItems.seedsCarrot));

        DryingManager.addRecipe(new DryingRecipe(new ItemStack(BidsItems.barkFibreCoarse),
                new ItemStack(BidsItems.barkFibre), 12, false));
        DryingManager.addRecipe(new DryingRecipe(new ItemStack(BidsItems.sisalFiberCoarse),
                new ItemStack(BidsItems.sisalFiberRinsed), 12, false));
        DryingManager.addRecipe(new DryingRecipe(new ItemStack(BidsItems.juteFiberCoarse),
                new ItemStack(TFCItems.juteFiber), 12, false));
        DryingManager.addRecipe(new DryingRecipe(new ItemStack(BidsItems.flaxStalkDried),
                new ItemStack(BidsItems.flaxStalkRetted), 18, false));
        DryingManager.addRecipe(new DryingRecipe(new ItemStack(BidsItems.woolDried),
                new ItemStack(BidsItems.woolRinsed), 8, false));

        GameRegistry.addRecipe(new ItemStack(BidsBlocks.fireBrickChimney, 2, 0), "P P", "X X", "P P",
            'P', new ItemStack(TFCItems.fireBrick, 1, 1),
            'X', new ItemStack(TFCItems.mortar, 1));

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
        DryingManager.registerTyingEquipment(BidsItems.sisalTwine, false, Blocks.wool, 1);
        DryingManager.registerTyingEquipment(BidsItems.juteTwine, false, Blocks.wool, 1);

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
            RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAdze")
                .matchIngredient(TFCItems.logs)
                .matchIngredient(TFCItems.logs)
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
            RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAdze")
                .matchIngredient(TFCItems.logs)
                .matchIngredient(TFCItems.logs)
                .matchCraftingBlock(logWallVert));
        }

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAxe")
                .matchCraftingBlock(BidsBlocks.palisade));
        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAxe")
                .matchCraftingBlock(BidsBlocks.palisade2));
        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAxe")
                .matchCraftingBlock(BidsBlocks.palisade3));

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemKnife")
                .matchCraftingItem(BidsItems.barkFibre));

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemKnife")
                .matchCraftingItem(BidsItems.birchBarkSheet, 0));

        RecipeManager.addAction(new ActionDamageTool(1)
                .addTools("itemAdze")
                .matchCraftingItem(BidsItems.plugAndFeather));

        RecipeManager.addAction(new ActionKeepItem()
            .addItems("itemLogExtra")
            .matchCraftingItem(TFCItems.clayTile));

        RecipeHelper.handleCompositeToolRecipes();
        RecipeHelper.handleSpindleSpinningRecipes();
        RecipeHelper.handleRopeMakingRecipes();
        RecipeHelper.handleLoomRecipes();
    }

    private static void registerCarvingRecipes() {
        Bids.LOG.info("Register carving recipes");

        CarvingRecipePattern choppingBlockPattern = new CarvingRecipePattern()
            .carveEntireLayer();

        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.blocks.hasChoppingBlock()) {
                CarvingManager.addRecipe(new CarvingRecipe(wood.blocks.getChoppingBlock(),
                    wood.blocks.getWoodVert(), choppingBlockPattern));
            }
        }

        CarvingRecipePattern saddleQuernPattern = new CarvingRecipePattern()
            .carveLayer("    ", " ## ", " ## ", " ## ");

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

        CarvingRecipePattern pressingStonePattern =  new CarvingRecipePattern()
            .carveEntireLayer()
            .carveEntireLayer()
            .carveLayer("####", "#   ", "#   ", "#   ")
            .carveLayer("####", "#   ", "#   ", "#   ");


        CarvingRecipePattern weightStonePattern =  new CarvingRecipePattern()
            .carveEntireLayer()
            .carveLayer("####", "#   ", "#   ", "#   ")
            .carveLayer("####", "#   ", "#   ", "#   ")
            .carveLayer("####", "#   ", "#   ", "#   ");

        CarvingRecipePattern chimneyPattern =  new CarvingRecipePattern()
            .carveLayer("    ", " ## ", " ## ", "    ")
            .carveLayer("    ", " ## ", " ## ", "    ")
            .carveLayer("    ", " ## ", " ## ", "    ")
            .carveLayer("    ", " ## ", " ## ", "    ");


        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            if (stone.soft) {
                CarvingManager.addRecipe(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.SADDLE_QUERN),
                    stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), saddleQuernPattern));

                for (int j = 0; j < handstonePatterns.length; j++) {
                    CarvingManager.addRecipe(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.HAND_STONE),
                        stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), handstonePatterns[j]));
                }

                CarvingManager.addRecipe(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.PRESSING_STONE),
                    stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), pressingStonePattern));

                CarvingManager.addRecipe(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.WEIGHT_STONE),
                    stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), weightStonePattern));
            }

            CarvingManager.addRecipe(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY),
                stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICKS), chimneyPattern));
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

        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 2, 9), // Salt
            new ItemStack(TFCItems.looseRock, 1, 5)));

        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(TFCItems.dye, 1, 15), // Bone Meal
            new ItemStack(TFCItems.bone, 1)));
        SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(TFCItems.dye, 1, 15), // Bone Meal
            new ItemStack(TFCItems.boneFragment, 1)));

        if (BidsOptions.SaddleQuern.allowGrindHematite) {
            SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 1, 5), // Hematite
                new ItemStack(TFCItems.smallOreChunk, 1, 3)));
        }
        if (BidsOptions.SaddleQuern.allowGrindLimonite) {
            SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 1, 7), // Limonite
                new ItemStack(TFCItems.smallOreChunk, 1, 11)));
        }
        if (BidsOptions.SaddleQuern.allowGrindMalachite) {
            SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 1, 8), // Malachite
                new ItemStack(TFCItems.smallOreChunk, 1, 9)));
        }
        if (BidsOptions.SaddleQuern.allowGrindLapisLazuli) {
            SaddleQuernManager.addRecipe(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 2, 6), // Lapis Lazuli
                new ItemStack(TFCItems.oreChunk, 1, 318)));
        }
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
        // Require fish to be steamed to medium level
        Food.setCooked(steamedFish, CookingHelper.getTempForItemStackCookedLevel(steamedFish, 3));
        StonePressManager.addRecipe(new StonePressRecipe(new FluidStack(BidsFluids.OILYFISHWATER, 10), steamedFish));
    }

    private static void registerScrewPressRecipes() {
        // Screw press efficiency affects the recipe input or output volume
        float inputMult = 1 / BidsOptions.ScrewPress.efficiency; // input multiplier (for non-food input)
        float outputMult = BidsOptions.ScrewPress.efficiency; // output multiplier (for food input)

        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.OLIVEOIL, 10),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.oliveCrushed), 0.64f * inputMult), 0.5f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.APPLEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.appleCrushed), 0.7f * inputMult), 0.5f));

        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.OLIVEOIL, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.olive), 0.64f * inputMult), 1f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.APPLEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.redApple), 0.7f * inputMult), 1f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.APPLEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.greenApple), 0.7f * inputMult), 1f));

        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.GRAPEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.grapes), 0.5f * inputMult), 0.5f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.CANEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.sugarcane), 0.8f * inputMult), 0.8f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.LEMONJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.lemon), 0.65f * inputMult), 0.65f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.ORANGEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.orange), 0.5f * inputMult), 0.65f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.PEACHJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.peach), 0.55f * inputMult), 0.8f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.PLUMJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.plum), 0.65f * inputMult), 0.8f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.FIGJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.fig), 0.5f * inputMult), 0.8f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.CHERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.cherry), 0.7f * inputMult), 0.8f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.DATEJUICE, 6),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.date), 0.8f * inputMult), 0.8f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.PAPAYAJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.papaya), 0.6f * inputMult), 0.8f));

        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.strawberry), 0.65f * inputMult), 0.5f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.blackberry), 0.61f * inputMult), 0.5f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.blueberry), 0.6f * inputMult), 0.5f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.bunchberry), 0.68f * inputMult), 0.5f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.cranberry), 0.7f * inputMult), 0.5f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.elderberry), 0.58f * inputMult), 0.5f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.gooseberry), 0.6f * inputMult), 0.5f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.raspberry), 0.6f * inputMult), 0.5f));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.snowberry), 0.66f * inputMult), 0.5f));

        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(TFCFluids.AGAVEJUICE, Math.round(40 * outputMult)),
            new ItemStack(TFCItems.agave, 1), 0.8f));

        ItemStack steamedFish = BidsFood.setSteamed(ItemFoodTFC.createTag(new ItemStack(TFCItems.fishRaw), 0.5f * inputMult), true);
        // Require fish to be steamed to medium level
        Food.setCooked(steamedFish, CookingHelper.getTempForItemStackCookedLevel(steamedFish, 3));
        ScrewPressManager.addRecipe(new ScrewPressRecipe(new FluidStack(BidsFluids.OILYFISHWATER, 10), steamedFish, 0.65f));
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
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 200), new ItemStack(TFCItems.powder, 1, 13))
            .produces(new FluidStack(BidsFluids.WEAKWOODASHLYE, 200))
            .inFixedTime(20000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.WEAKWOODASHLYE, 2))
            .produces(new FluidStack(BidsFluids.WOODASHLYE, 1))
            .withHeat()
            .withoutLid()
            .inTime(1000 / 500f)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 1), new FluidStack(BidsFluids.WOODASHLYE, 1))
            .produces(new FluidStack(BidsFluids.WEAKWOODASHLYE, 2))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.TALLOW, 5), new FluidStack(BidsFluids.WOODASHLYE, 4))
            .produces(new FluidStack(BidsFluids.TALLOWWOODASHLYE, 9))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.TALLOWWOODASHLYE, 1))
            .produces(new FluidStack(BidsFluids.SOAP, 1))
            .withHeat(EnumCookingHeatLevel.LOW)
            .inFixedTime(2000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.SOAP, 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.soap), Global.FOOD_MAX_WEIGHT / 10000))
            .withoutHeat()
            .inTime(1000 / 5000f)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.soap), Global.FOOD_MAX_WEIGHT / 10000))
            .produces(new FluidStack(BidsFluids.SOAP, 1))
            .withHeat()
            .inTime(500 / 5000f)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 500), ItemFoodTFC.createTag(new ItemStack(BidsItems.soap), 1f))
            .produces(new FluidStack(BidsFluids.SOAPYWATER, 500))
            .inTime(20)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.SOAPYWATER, 100), new ItemStack(TFCItems.wool))
            .produces(new ItemStack(BidsItems.woolWashed, 1))
            .withHeat()
            .inTime(50)
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

        for (Item stringItem : new Item[] { TFCItems.silkString, TFCItems.woolYarn, TFCItems.linenString, TFCItems.cottonYarn, BidsItems.juteTwine, BidsItems.sisalTwine } ) {
            CookingManager.addRecipe(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.WAX, 200), new ItemStack(stringItem))
                .produces(new ItemStack(TFCBlocks.candleOff, 1))
                .withHeat()
                .build());

            CookingManager.addRecipe(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.TALLOW, 200), new ItemStack(stringItem))
                .produces(new ItemStack(TFCBlocks.candleOff, 1))
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
            .consumes(new FluidStack(BidsFluids.SKIMMEDMILK, 9), new FluidStack(TFCFluids.VINEGAR, 1))
            .produces(new FluidStack(BidsFluids.SKIMMEDMILKVINEGAR, 10))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.SKIMMEDMILKVINEGAR, 1))
            .produces(new FluidStack(BidsFluids.SKIMMEDMILKCURDLED, 1))
            .withoutHeat()
            .withLid()
            .inFixedTime(8000)
            .build());

        CookingManager.addRecipe(CookingCheeseRecipe.builder()
            .allowingInfusion()
            .consumes(new FluidStack(BidsFluids.SKIMMEDMILKCURDLED, 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.hardCheese), Global.FOOD_MAX_WEIGHT / 10000))
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

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.MILK, 500))
            .produces(new FluidStack(BidsFluids.SKIMMEDMILK, 450), new FluidStack(BidsFluids.CREAM, 50))
            .withoutHeat()
            .inFixedTime(24000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.GOATMILK, 500))
            .produces(new FluidStack(BidsFluids.SKIMMEDMILK, 450), new FluidStack(BidsFluids.CREAM, 50))
            .withoutHeat()
            .inFixedTime(24000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.suet), Global.FOOD_MAX_WEIGHT / 8000))
            .produces(new FluidStack(BidsFluids.TALLOW, 1))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inTime(4000 / 5000f)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.TALLOW, 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.tallow), Global.FOOD_MAX_WEIGHT / 10000))
            .withoutHeat()
            .inFixedTime(1000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.tallow), Global.FOOD_MAX_WEIGHT / 10000))
            .produces(new FluidStack(BidsFluids.TALLOW, 1))
            .withHeat()
            .inTime(250 / 5000f)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN, 500), new FluidStack(TFCFluids.FRESHWATER, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN_WATER, 1000))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT, 500), new FluidStack(TFCFluids.FRESHWATER, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT_WATER, 1000))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH, 500), new FluidStack(TFCFluids.FRESHWATER, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH_WATER, 1000))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE, 500), new FluidStack(TFCFluids.FRESHWATER, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE_WATER, 1000))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL, 500), new FluidStack(TFCFluids.FRESHWATER, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_WATER, 1000))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL, 500), new FluidStack(TFCFluids.MILK, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_MILK, 1000))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL, 500), new FluidStack(BidsFluids.GOATMILK, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_MILK, 1000))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL, 500), new FluidStack(BidsFluids.SKIMMEDMILK, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_MILK, 1000))
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN_STEW, 500))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT_STEW, 500))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1500)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH_STEW, 500))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1500)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE_STEW, 500))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN_WATER, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN_SOUP, 1000))
            .withHeat(EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.MEDIUM)
            .withLid()
            .inFixedTime(1500)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT_WATER, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT_SOUP, 1000))
            .withHeat(EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.MEDIUM)
            .withLid()
            .inFixedTime(2000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH_WATER, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH_SOUP, 1000))
            .withHeat(EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.MEDIUM)
            .withLid()
            .inFixedTime(2000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE_WATER, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE_SOUP, 1000))
            .withHeat(EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.MEDIUM)
            .withLid()
            .inFixedTime(1500)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_WATER, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.PORRIDGE_WATER, 1000))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_MILK, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.PORRIDGE_MILK, 1000))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1000)
            .build());

        CookingManager.addRecipe(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.EGG, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.OMELET, 500))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(250)
            .build());
    }

    private static void registerPrepRecipes() {
        Bids.LOG.info("Register prep recipes");

        // Classic bread sandwich - no grains at all
        PrepIngredient foodNoGrain = PrepIngredient.builder()
            .allow(EnumFoodGroup.Fruit)
            .allow(EnumFoodGroup.Vegetable)
            .allow(EnumFoodGroup.Dairy)
            .allow(EnumFoodGroup.Protein)
            .build();

        // Flatbread wrap - no grain except rice (burrito?)
        PrepIngredient foodNoGrainExceptRice = PrepIngredient.builder()
            .allow(EnumFoodGroup.Fruit)
            .allow(EnumFoodGroup.Vegetable)
            .allow(EnumFoodGroup.Dairy)
            .allow(EnumFoodGroup.Protein)
            .allow(TFCItems.riceGrain)
            .build();

        PrepIngredient foodNoGrainExceptRiceAndBread = PrepIngredient.builder()
            .allow(EnumFoodGroup.Fruit)
            .allow(EnumFoodGroup.Vegetable)
            .allow(EnumFoodGroup.Dairy)
            .allow(EnumFoodGroup.Protein)
            .allow(TFCItems.riceGrain)
            .allow("foodBread")
            .build();

        PrepIngredient vesselBowl = PrepIngredient.builder()
            .allow(TFCItems.potteryBowl, 1)
            .allow(TFCItems.potteryBowl, 2)
            .build();

        Item[] breads = new Item[] { TFCItems.wheatBread, TFCItems.oatBread, TFCItems.barleyBread, TFCItems.ryeBread, TFCItems.cornBread, TFCItems.riceBread };
        for (int i = 0; i < breads.length; i++) {
            PrepManager.addRecipe(new PrepRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.sandwich, 1, i)), new PrepIngredientSpec[]{
                PrepIngredient.from(breads[i]).toSpec(2),
                foodNoGrain.toSpec(3), foodNoGrain.toSpec(2), foodNoGrain.toSpec(2), foodNoGrain.toSpec(1)
            }, 7));
        }

        PrepManager.addRecipe(new PrepSaladRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.salad)), new PrepIngredientSpec[] {
            vesselBowl.toSpec(), foodNoGrainExceptRice.toSpec(10), foodNoGrainExceptRice.toSpec(4), foodNoGrainExceptRice.toSpec(4), foodNoGrainExceptRice.toSpec(2)
        }, 14));

        Item[] peppers = new Item[] { TFCItems.greenBellPepper, TFCItems.yellowBellPepper, TFCItems.redBellPepper };
        for (int i = 0; i < peppers.length; i++) {
            PrepManager.addRecipe(new PrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.stuffedPepper, 1, i)), new PrepIngredientSpec[]{
                PrepIngredient.from(peppers[i]).toSpec(3),
                foodNoGrainExceptRiceAndBread.toSpec(6), foodNoGrainExceptRiceAndBread.toSpec(4), foodNoGrainExceptRiceAndBread.toSpec(2), foodNoGrainExceptRiceAndBread.toSpec(1)
            }, 10));
        }

        PrepManager.addRecipe(new PrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.stuffedMushroom)), new PrepIngredientSpec[]{
            PrepIngredient.from(TFCItems.mushroomFoodB).toSpec(2),
            foodNoGrainExceptRiceAndBread.toSpec(3), foodNoGrainExceptRiceAndBread.toSpec(2), foodNoGrainExceptRiceAndBread.toSpec(2), foodNoGrainExceptRiceAndBread.toSpec(1)
        }, 7));

        Item[] flatbread = new Item[] { BidsItems.wheatFlatbread, BidsItems.oatFlatbread, BidsItems.barleyFlatbread, BidsItems.ryeFlatbread, BidsItems.cornmealFlatbread, BidsItems.riceFlatbread };
        for (int i = 0; i < breads.length; i++) {
            PrepManager.addRecipe(new PrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.wrap, 1, i)), new PrepIngredientSpec[]{
                PrepIngredient.from(flatbread[i]).toSpec(2),
                foodNoGrainExceptRice.toSpec(3), foodNoGrainExceptRice.toSpec(2), foodNoGrainExceptRice.toSpec(2), foodNoGrainExceptRice.toSpec(1)
            }, 7));
        }

        PrepIngredient leanMeat = PrepIngredient.builder()
            .allow(TFCItems.beefRaw)
            .allow(TFCItems.venisonRaw)
            .allow(TFCItems.muttonRaw)
            .allow(TFCItems.horseMeatRaw)
            .build();

        PrepIngredient tallow = PrepIngredient.builder()
            .allow(BidsItems.tallow)
            .build();

        PrepIngredient berriesAndFlours = PrepIngredient.builder()
            .allow("foodFruitBerry")
            .allow("foodGrainGround")
            .allow("foodGrainCrushed")
            .build();

        PrepManager.addRecipe(new PrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.pemmican)), new PrepIngredientSpec[]{
            PrepIngredient.from(BidsItems.moreHide, 0).toSpec(),
            leanMeat.toSpec(40, true), tallow.toSpec(20, true), berriesAndFlours.toSpec(10), berriesAndFlours.toSpec(10)
        }));

        PrepIngredient vesselLargeBowl = PrepIngredient.builder()
            .allow(BidsItems.largeClayBowl, 1)
            .build();

        PrepIngredient beans = PrepIngredient.builder()
            .allow("foodBeans")
            .build();

        PrepIngredient meatNoFish = PrepIngredient.builder()
            .allow("foodMeatRed")
            .allow("foodMeatPoultry")
            .build();

        PrepIngredient meatFish = PrepIngredient.builder()
            .allow("foodMeatFish")
            .build();

        PrepIngredient vegetable = PrepIngredient.builder()
            .allow(EnumFoodGroup.Vegetable)
            .build();

        PrepIngredient grainPorridge = PrepIngredient.builder()
            .allow(TFCItems.maizeEar)
            .allow(TFCItems.riceGrain)
            .allow("foodGrainGround")
            .allow("foodGrainCrushed")
            .build();

        PrepIngredient foodNoFruitNoBread = PrepIngredient.builder()
            .allow(EnumFoodGroup.Dairy)
            .allow(EnumFoodGroup.Protein)
            .allow(EnumFoodGroup.Vegetable)
            .allow(TFCItems.maizeEar)
            .allow(TFCItems.riceGrain)
            .allow("foodGrainGround")
            .allow("foodGrainCrushed")
            .allow("foodHardtack")
            .build();

        PrepIngredient foodNoDairyNoGrain = PrepIngredient.builder()
            .allow(EnumFoodGroup.Fruit)
            .allow(EnumFoodGroup.Protein)
            .allow(EnumFoodGroup.Vegetable)
            .build();

        PrepIngredient foodNoFruitNoGrain = PrepIngredient.builder()
            .allow(EnumFoodGroup.Dairy)
            .allow(EnumFoodGroup.Protein)
            .allow(EnumFoodGroup.Vegetable)
            .build();

        PrepIngredient foodEgg = PrepIngredient.builder()
            .allow("foodEgg")
            .build();

        PrepManager.addRecipe(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.BEAN), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            beans.toSpec(20, true), foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)
        }));

        PrepManager.addRecipe(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.MEAT), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            meatNoFish.toSpec(20, true), foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)
        }));

        PrepManager.addRecipe(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.FISH), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            meatFish.toSpec(20, true), foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)
        }));

        PrepManager.addRecipe(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.VEGETABLE), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            vegetable.toSpec(20, true), foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)
        }));

        PrepManager.addRecipe(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.CEREAL), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            grainPorridge.toSpec(20, true), foodNoDairyNoGrain.toSpec(8, true), foodNoDairyNoGrain.toSpec(8), foodNoDairyNoGrain.toSpec(4)
        }));

        PrepManager.addRecipe(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.EGG), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            foodEgg.toSpec(20, true), foodNoFruitNoGrain.toSpec(8, true), foodNoFruitNoGrain.toSpec(8), foodNoFruitNoGrain.toSpec(4)
        }));
    }

    private static void registerChurningRecipes() {
        Bids.LOG.info("Register churning recipes");

        ChurningManager.addRecipe(new ChurningRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.butter), Global.FOOD_MAX_WEIGHT / 4000),
            new FluidStack(BidsFluids.CREAM, 1), 1));
    }

    private static void registerProcessingSurfaceRecipes() {
        Bids.LOG.info("Register processing surface recipes");

        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.scrapedHide, 1, 0),
            new ItemStack(TFCItems.soakedHide, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.scrapedHide, 1, 1),
            new ItemStack(TFCItems.soakedHide, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.scrapedHide, 1, 2),
            new ItemStack(TFCItems.soakedHide, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));

        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.fur, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.fur, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.fur, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.furScrap, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.furScrap, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.furScrap, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));

        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.wolfFur, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.wolfFur, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.wolfFur, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.wolfFurScrap, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.wolfFurScrap, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.wolfFurScrap, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));

        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.bearFur, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.bearFur, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.bearFur, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.bearFurScrap, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.bearFurScrap, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.bearFurScrap, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));

        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.sheepSkin, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.sheepSkin, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.sheepSkin, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));

        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(BidsItems.flaxStalkBroken),
            new ItemStack(BidsItems.flaxStalkDried),
            "itemFlaxBreakingTool", "blockFlaxWorkingSurface", 0.25f));
        ProcessingSurfaceManager.addRecipe(new ProcessingSurfaceRecipe(new ItemStack(BidsItems.flaxFiberCoarse),
            new ItemStack(BidsItems.flaxStalkBroken),
            "itemFlaxScutchingTool", "blockFlaxWorkingSurface", 0.25f));
    }

    private static void registerSoakingSurfaceRecipes() {
        Bids.LOG.info("Register soaking surface recipes");

        SoakingSurfaceManager.addRecipe(new SoakingSurfaceRecipe(new ItemStack(BidsItems.flaxStalkRetted, 1, 0),
            new ItemStack(BidsItems.flaxStalk, 1, 0), "blockFreshWater", 20));

        SoakingSurfaceManager.addRecipe(new SoakingSurfaceRecipe(new ItemStack(BidsItems.juteStalkRetted, 1, 0),
            new ItemStack(BidsItems.juteStalk, 1, 0), "blockFreshWater", 20));

        SoakingSurfaceManager.addRecipe(new SoakingSurfaceRecipe(new ItemStack(BidsItems.sisalFiberRinsed, 1, 0),
            new ItemStack(TFCItems.sisalFiber, 1, 0), "blockFreshWater", 0));

        // Washing wool can be skipped however the wool needs to be rinsed for a extended period of time
        SoakingSurfaceManager.addRecipe(new SoakingSurfaceRecipe(new ItemStack(BidsItems.woolRinsed, 1, 0),
            new ItemStack(TFCItems.wool, 1, 0), "blockFreshWater", 20));

        SoakingSurfaceManager.addRecipe(new SoakingSurfaceRecipe(new ItemStack(BidsItems.woolRinsed, 1, 0),
            new ItemStack(BidsItems.woolWashed, 1, 0), "blockFreshWater", 0));
    }

    private static void registerHandworkRecipes() {
        Bids.LOG.info("Register handwork recipes");

        HandworkManager.addRecipe(new HandworkRecipe(new ItemStack(BidsItems.barkFibreSmooth), new ItemStack(BidsItems.barkFibreCoarse), 80));
        HandworkManager.addRecipe(new HandworkRecipe(new ItemStack(TFCItems.juteFiber), new ItemStack(BidsItems.juteStalkRetted), 80));
        HandworkManager.addRecipe(new HandworkRecipe(new ItemStack(BidsItems.flaxStalkBroken), new ItemStack(BidsItems.flaxStalkDried), 240));
        HandworkManager.addRecipe(new HandworkRecipe(new ItemStack(BidsItems.flaxFiberCoarse), new ItemStack(BidsItems.flaxStalkBroken), 240));
        HandworkManager.addRecipe(new HandworkRecipe(new ItemStack(BidsItems.cottonFiberCoarse), new ItemStack(BidsItems.cottonBollRefined), 60));
        HandworkManager.addRecipe(new HandworkRecipe(new ItemStack(BidsItems.woolFiberCoarse), new ItemStack(BidsItems.woolDried), 60));

        // Flax fiber spinning recipe is preserved as a way to convert TFC+ Flax fibers to string
        // since unlike other fibers it is not used as an intermediate material in the new extended textile processing
        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(TFCItems.linenString, 4), new ItemStack(TFCItems.flaxFiber), 120));

        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(TFCItems.linenString, 4), new ItemStack(BidsItems.flaxFiberRefined), 120));
        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(TFCItems.cottonYarn, 6), new ItemStack(BidsItems.cottonFiberRefined), 120));
        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(TFCItems.woolYarn, 8), new ItemStack(BidsItems.woolFiberRefined), 120));
        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(BidsItems.barkCordage, 2), new ItemStack(BidsItems.barkFibreSmooth), 80));
        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(BidsItems.sisalTwine, 2), new ItemStack(BidsItems.sisalFiberRefined), 120));
        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(BidsItems.juteTwine, 2), new ItemStack(BidsItems.juteFiberRefined), 120));

        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(TFCItems.linenString, 4), new ItemStack(BidsItems.flaxFiberCoarse), 120 * 4));
        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(TFCItems.cottonYarn, 6), new ItemStack(BidsItems.cottonFiberCoarse), 120 * 4));
        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(TFCItems.woolYarn, 8), new ItemStack(BidsItems.woolFiberCoarse), 120 * 4));
        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(BidsItems.sisalTwine, 2), new ItemStack(BidsItems.sisalFiberCoarse), 120 * 4));
        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(BidsItems.juteTwine, 2), new ItemStack(BidsItems.juteFiberCoarse), 120 * 4));
        HandworkManager.addRecipe(new SpinningRecipe(new ItemStack(BidsItems.barkCordage, 2), new ItemStack(BidsItems.barkFibreCoarse), 80 * 4));

        HandworkManager.addRecipe(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(TFCItems.linenString, 16), 600));
        HandworkManager.addRecipe(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(BidsItems.barkCordage, 12), 600));
        HandworkManager.addRecipe(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(BidsItems.sisalTwine, 8), 600));
        HandworkManager.addRecipe(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(BidsItems.juteTwine, 12), 600));

        HandworkManager.addRecipe(new CardingRecipe(new ItemStack(BidsItems.sisalFiberRefined), new ItemStack(BidsItems.sisalFiberCoarse), 80));
        HandworkManager.addRecipe(new CardingRecipe(new ItemStack(BidsItems.cottonFiberRefined), new ItemStack(BidsItems.cottonFiberCoarse), 80));
        HandworkManager.addRecipe(new CardingRecipe(new ItemStack(BidsItems.woolFiberRefined), new ItemStack(BidsItems.woolFiberCoarse), 80));

        HandworkManager.addRecipe(new HecklingRecipe(new ItemStack(BidsItems.juteFiberRefined), new ItemStack(BidsItems.juteFiberCoarse), 120));
        HandworkManager.addRecipe(new HecklingRecipe(new ItemStack(BidsItems.flaxFiberRefined), new ItemStack(BidsItems.flaxFiberCoarse), 120));
    }

    private static void registerKnappingRecipes() {
        Bids.LOG.info("Register TFC knapping recipes");

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            CraftingManagerTFC.getInstance().addRecipe(stone.items.getItem(EnumStoneItemType.DRILL_HEAD),
                    new Object[] { "     ", " ### ", "#####", " ### ", "  #  ",
                            '#', stone.items.getItem(EnumStoneItemType.FLAT_ROCK) });
            CraftingManagerTFC.getInstance().addRecipe(stone.items.getItem(EnumStoneItemType.ADZE_HEAD),
                    new Object[] { "#####", "#  ##", "#    ", "     ", "     ",
                            '#', stone.items.getItem(EnumStoneItemType.FLAT_ROCK) });
            CraftingManagerTFC.getInstance().addRecipe(stone.items.getItem(EnumStoneItemType.HAND_AXE),
                    new Object[] { "  #  ", " ### ", " ### ", "#####", " ### ",
                        '#', stone.items.getItem(EnumStoneItemType.FLAT_ROCK) });
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

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.strawNest, 1),
            new Object[] { "     ", "#   #", "#   #", " ### ", "     ", '#',
                new ItemStack(TFCItems.flatStraw, 1) });

        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatDoughHardtack, 1), 160),
            new Object[] { "#####", "# # #", "#####", "# # #", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 0) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyDoughHardtack, 1), 160),
            new Object[] { "#####", "# # #", "#####", "# # #", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 1) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.oatDoughHardtack, 1), 160),
            new Object[] { "#####", "# # #", "#####", "# # #", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 2) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.riceDoughHardtack, 1), 160),
            new Object[] { "#####", "# # #", "#####", "# # #", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 3) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.ryeDoughHardtack, 1), 160),
            new Object[] { "#####", "# # #", "#####", "# # #", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 4) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealDoughHardtack, 1), 160),
            new Object[] { "#####", "# # #", "#####", "# # #", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 5) });

        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.wheatDough, 1), 160),
            new Object[] { "     ", " ### ", "#####", "#####", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 0) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.barleyDough, 1), 160),
            new Object[] { "     ", " ### ", "#####", "#####", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 1) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.oatDough, 1), 160),
            new Object[] { "     ", " ### ", "#####", "#####", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 2) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.riceDough, 1), 160),
            new Object[] { "     ", " ### ", "#####", "#####", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 3) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.ryeDough, 1), 160),
            new Object[] { "     ", " ### ", "#####", "#####", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 4) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.cornmealDough, 1), 160),
            new Object[] { "     ", " ### ", "#####", "#####", "#####", '#',
                new ItemStack(BidsItems.flatDough, 1, 5) });

        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatDoughFlatbread, 1), 160),
            new Object[] { " ### ", "#####", "#####", "#####", " ### ", '#',
                new ItemStack(BidsItems.flatDough, 1, 0) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyDoughFlatbread, 1), 160),
            new Object[] { " ### ", "#####", "#####", "#####", " ### ", '#',
                new ItemStack(BidsItems.flatDough, 1, 1) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.oatDoughFlatbread, 1), 160),
            new Object[] { " ### ", "#####", "#####", "#####", " ### ", '#',
                new ItemStack(BidsItems.flatDough, 1, 2) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.riceDoughFlatbread, 1), 160),
            new Object[] { " ### ", "#####", "#####", "#####", " ### ", '#',
                new ItemStack(BidsItems.flatDough, 1, 3) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.ryeDoughFlatbread, 1), 160),
            new Object[] { " ### ", "#####", "#####", "#####", " ### ", '#',
                new ItemStack(BidsItems.flatDough, 1, 4) });
        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealDoughFlatbread, 1), 160),
            new Object[] { " ### ", "#####", "#####", "#####", " ### ", '#',
                new ItemStack(BidsItems.flatDough, 1, 5) });

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

        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            // Extracting tannin from bark
            if (wood.hasBarkTannin) {
                BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asItemDemanding()
                    .consumes(wood.items.getBark(), new FluidStack(TFCFluids.FRESHWATER, 625))
                    .produces(new FluidStack(TFCFluids.TANNIN, 500))
                    .withMinTechLevel(0)
                );
            }
        }

        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asAlcohol()
            .consumes(ItemFoodTFC.createTag(new ItemStack(TFCItems.riceGerm), 80, true), new FluidStack(TFCFluids.FRESHWATER, 5000))
            .produces(new FluidStack(TFCFluids.RICEBEER, 5000))
            .withMinTechLevel(0).requiringCooked(true));
        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asAlcohol()
            .consumes(ItemFoodTFC.createTag(new ItemStack(TFCItems.wheatGerm), 80, true), new FluidStack(TFCFluids.FRESHWATER, 5000))
            .produces(new FluidStack(TFCFluids.WHEATBEER, 5000))
            .withMinTechLevel(0).requiringCooked(true));
        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asAlcohol()
            .consumes(ItemFoodTFC.createTag(new ItemStack(TFCItems.ryeGerm), 80, true), new FluidStack(TFCFluids.FRESHWATER, 5000))
            .produces(new FluidStack(TFCFluids.RYEBEER, 5000))
            .withMinTechLevel(0).requiringCooked(true));
        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asAlcohol()
            .consumes(ItemFoodTFC.createTag(new ItemStack(TFCItems.barleyGerm), 80, true), new FluidStack(TFCFluids.FRESHWATER, 5000))
            .produces(new FluidStack(TFCFluids.BEER, 5000))
            .withMinTechLevel(0).requiringCooked(true));
        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asAlcohol()
            .consumes(ItemFoodTFC.createTag(new ItemStack(TFCItems.cornGerm), 80, true), new FluidStack(TFCFluids.FRESHWATER, 5000))
            .produces(new FluidStack(TFCFluids.CORNBEER, 5000))
            .withMinTechLevel(0).requiringCooked(true));

        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asLiquidToLiquid()
            .consumes(new FluidStack(TFCFluids.SALTWATER, 4500), new FluidStack(TFCFluids.VINEGAR, 500))
            .produces(new FluidStack(TFCFluids.BRINE, 5000))
            .withSealTime(0).withMinTechLevel(0).beingSealed(false).removingLiquid(false));
        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asLiquidToLiquid()
            .consumes(new FluidStack(TFCFluids.MILK, 4500), new FluidStack(TFCFluids.VINEGAR, 500))
            .produces(new FluidStack(TFCFluids.MILKVINEGAR, 5000))
            .withSealTime(0).withMinTechLevel(0).beingSealed(false).removingLiquid(false));
        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asLiquidToLiquid()
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 4500), new FluidStack(TFCFluids.HONEY, 500))
            .produces(new FluidStack(TFCFluids.HONEYWATER, 5000))
            .withSealTime(0).withMinTechLevel(0).beingSealed(false).removingLiquid(false));

        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asSimple()
            .consumes(new ItemStack(TFCItems.sisalFiber), new FluidStack(TFCFluids.FRESHWATER, 100))
            .produces(new ItemStack(BidsItems.sisalFiberRinsed), new FluidStack(TFCFluids.FRESHWATER, 100))
            .withSealTime(0).withMinTechLevel(0).beingSealed(false));
        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asSimple()
            .consumes(new ItemStack(BidsItems.juteStalk), new FluidStack(TFCFluids.FRESHWATER, 200))
            .produces(new ItemStack(BidsItems.juteStalkRetted), new FluidStack(TFCFluids.FRESHWATER, 200))
            .withMinTechLevel(0).beingSealed(false));
        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asSimple()
            .consumes(new ItemStack(BidsItems.flaxStalk), new FluidStack(TFCFluids.FRESHWATER, 200))
            .produces(new ItemStack(BidsItems.flaxStalkRetted), new FluidStack(TFCFluids.FRESHWATER, 200))
            .withMinTechLevel(0).beingSealed(false));
        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asSimple()
            .consumes(new ItemStack(TFCItems.wool), new FluidStack(TFCFluids.FRESHWATER, 200))
            .produces(new ItemStack(BidsItems.woolRinsed), new FluidStack(TFCFluids.FRESHWATER, 200))
            .withSealTime(16).withMinTechLevel(0).beingSealed(false));
        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asSimple()
            .consumes(new ItemStack(BidsItems.woolWashed), new FluidStack(TFCFluids.FRESHWATER, 100))
            .produces(new ItemStack(BidsItems.woolRinsed), new FluidStack(TFCFluids.FRESHWATER, 100))
            .withSealTime(0).withMinTechLevel(0).beingSealed(false));

        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asMultiItem()
            .consumes(new ItemStack(BidsItems.sisalTwine), new FluidStack(TFCFluids.WAX, 200))
            .produces(new ItemStack(TFCBlocks.candleOff), new FluidStack(TFCFluids.WAX, 200))
            .keepingStackSize(false).withSealTime(0).beingSealed(false).withMinTechLevel(0));
        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asMultiItem()
            .consumes(new ItemStack(BidsItems.juteTwine), new FluidStack(TFCFluids.WAX, 200))
            .produces(new ItemStack(TFCBlocks.candleOff), new FluidStack(TFCFluids.WAX, 200))
            .keepingStackSize(false).withSealTime(0).beingSealed(false).withMinTechLevel(0));

        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asMultiItem()
            .consumes(new ItemStack(BidsItems.cottonBollRefined), new FluidStack(TFCFluids.AMMONIUMCHLORIDE, 250))
            .produces(new ItemStack(TFCItems.ammoniumChlorideBall), new FluidStack(TFCFluids.AMMONIUMCHLORIDE, 250))
            .keepingStackSize(false).withSealTime(0).beingSealed(false).withMinTechLevel(0));

        BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asItemDemanding()
            .consumes(new ItemStack(TFCItems.powder, 1, 13), new FluidStack(TFCFluids.FRESHWATER, 200))
            .produces(new FluidStack(BidsFluids.WEAKWOODASHLYE, 200))
            .withMinTechLevel(0).withSealTime(20)
        );
    }

    private static void registerLoomRecipes() {
        Bids.LOG.info("Register TFC loom recipes");

        LoomManager.getInstance().addRecipe(new LoomRecipe(new ItemStack(BidsItems.sisalTwine, 20), new ItemStack(TFCItems.burlapCloth, 1)),
            new ResourceLocation("terrafirmacraftplus", "textures/blocks/Rope.png"));
        LoomManager.getInstance().addRecipe(new LoomRecipe(new ItemStack(BidsItems.juteTwine, 16), new ItemStack(TFCItems.burlapCloth, 1)),
            new ResourceLocation("terrafirmacraftplus", "textures/blocks/Rope.png"));
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
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null,
                "adze", AnvilReq.WROUGHTIRON, new ItemStack(BidsItems.wroughtIronAdzeHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));

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
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null,
                "drill", AnvilReq.WROUGHTIRON, new ItemStack(BidsItems.wroughtIronDrillHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));

            Bids.LOG.info("Registering plug and feather anvil plan and recipes");
            AnvilManager.getInstance().addPlan("plugandfeather", new PlanRecipe(new RuleEnum[]{RuleEnum.HITLAST, RuleEnum.BENDSECONDFROMLAST, RuleEnum.SHRINKTHIRDFROMLAST }));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null,
                "plugandfeather", AnvilReq.COPPER, new ItemStack(BidsItems.plugAndFeather, 8, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot), null,
                "plugandfeather", AnvilReq.BRONZE, new ItemStack(BidsItems.plugAndFeather, 8, 2)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot), null,
                "plugandfeather", AnvilReq.BISMUTHBRONZE, new ItemStack(BidsItems.plugAndFeather, 8, 3)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot), null,
                "plugandfeather", AnvilReq.BLACKBRONZE, new ItemStack(BidsItems.plugAndFeather, 8, 4)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null,
                "plugandfeather", AnvilReq.WROUGHTIRON, new ItemStack(BidsItems.plugAndFeather, 8, 5)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));

            AnvilManager.getInstance().addPlan("anvil", new PlanRecipe(new RuleEnum[]{RuleEnum.HITLAST, RuleEnum.HITSECONDFROMLAST, RuleEnum.HITTHIRDFROMLAST}));

            registerUnfinishedAnvilRecipeHelper(1, TFCItems.copperIngot2x, AnvilReq.STONE);
            registerUnfinishedAnvilRecipeHelper(2, TFCItems.bronzeIngot2x, AnvilReq.COPPER);
            registerUnfinishedAnvilRecipeHelper(3, TFCItems.wroughtIronIngot2x, AnvilReq.BRONZE);
            registerUnfinishedAnvilRecipeHelper(4, TFCItems.steelIngot2x, AnvilReq.WROUGHTIRON);
            registerUnfinishedAnvilRecipeHelper(5, TFCItems.blackSteelIngot2x, AnvilReq.STEEL);
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
            req, BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 5)));

        AnvilManager.getInstance().addRecipe(new AnvilRecipe(BlockUnfinishedAnvil.getUnfinishedAnvil(mat, 5), null,
            "anvil", req, BlockUnfinishedAnvil.getFinishedAnvil(mat)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));

    }

    private static void registerHandlers() {
        Bids.LOG.info("Register crafting handlers");

        FMLCommonHandler.instance().bus().register(new CraftingHandler());
    }

}
