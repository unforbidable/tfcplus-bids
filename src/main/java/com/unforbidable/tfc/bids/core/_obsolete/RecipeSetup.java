package com.unforbidable.tfc.bids.core._obsolete;

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
import com.unforbidable.tfc.bids.api._obsolete.BidsCookingMixtures;
import com.unforbidable.tfc.bids.api._obsolete.BidsFluids;
import com.unforbidable.tfc.bids.api._obsolete.BidsOptions;
import com.unforbidable.tfc.bids.api._obsolete.BidsRegistry;
import com.unforbidable.tfc.bids.api.features.quern.SaddleQuernRecipe;
import com.unforbidable.tfc.bids.api.features.pressing.StonePressRecipe;
import com.unforbidable.tfc.bids.api.util.food.BidsFood;
import com.unforbidable.tfc.bids.features.device.saddlequern.SaddleQuernConfig;
import com.unforbidable.tfc.bids.features.device.saddlequern.StonePressConfig;
import com.unforbidable.tfc.bids.features.material.unfinishedanvil.block.BlockUnfinishedAnvil;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingHelper;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingMixtureHelper;
import com.unforbidable.tfc.bids.core.crafting.RecipeManager;
import com.unforbidable.tfc.bids.core.crafting.RecipeManagerSession;
import com.unforbidable.tfc.bids.api.*;
import com.unforbidable.tfc.bids.api._obsolete.Crafting.*;
import com.unforbidable.tfc.bids.compat.tfc._obsolete.RecipeHelper;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.BarrelRecipeBuilder;
import com.unforbidable.tfc.bids.compat.tfc._obsolete.TFC.BarrelRecipeManager;
import com.unforbidable.tfc.bids.core.schemes.stone.EnumStoneItemType;
import com.unforbidable.tfc.bids.core.schemes.stone.StoneIndex;
import com.unforbidable.tfc.bids.core.schemes.stone.StoneScheme;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.features.player.achievements.eventhandler.CraftingHandler;
import com.unforbidable.tfc.bids.features.device.cookingpot.recipe.RecipeEmptyCookingPot;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api._obsolete.Enums.EnumCookingHeatLevel;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;
import static com.unforbidable.tfc.bids.core.crafting.actions.ExtraDrop.extraDrop;

public class RecipeSetup {

    public static void init() {
        registerRecipes();
        registerCustomRecipes();
        registerCarvingRecipes();
        registerSaddleQuernRecipes();
        registerStonePressRecipes();
        registerScrewPressRecipes();
        registerDryingRecipes();
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
//        addFoodDoughRecipe(TFCItems.wheatGround, BidsItems.wheatDoughUnshaped);
//        addFoodDoughRecipe(TFCItems.barleyGround, BidsItems.barleyDoughUnshaped);
//        addFoodDoughRecipe(TFCItems.ryeGround, BidsItems.ryeDoughUnshaped);
//        addFoodDoughRecipe(TFCItems.oatGround, BidsItems.oatDoughUnshaped);
//        addFoodDoughRecipe(TFCItems.riceGround, BidsItems.riceDoughUnshaped);
//        addFoodDoughRecipe(TFCItems.cornmealGround, BidsItems.cornmealDoughUnshaped);
//
//        addFoodDoughRecipe(BidsItems.wheatCrushed, BidsItems.wheatDoughFlatbread);
//        addFoodDoughRecipe(BidsItems.barleyCrushed, BidsItems.barleyFlatbread);
//        addFoodDoughRecipe(BidsItems.ryeCrushed, BidsItems.ryeDoughFlatbread);
//        addFoodDoughRecipe(BidsItems.oatCrushed, BidsItems.oatDoughFlatbread);
//        addFoodDoughRecipe(BidsItems.riceCrushed, BidsItems.riceDoughFlatbread);
//        addFoodDoughRecipe(BidsItems.cornmealCrushed, BidsItems.cornmealDoughFlatbread);
    }

    private static void addFoodDoughRecipe(Item foodInput, Item foodOutput) {
        RecipeManagerSession recipes = RecipeManager.getSession();

        recipes.addRecipe(new ShapelessOreRecipe(ItemFoodTFC.createTag(new ItemStack(foodOutput, 1)),
            ItemFoodTFC.createTag(new ItemStack(foodInput, 1)), "itemLargeBowlWater"));

        recipes.close();
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

        RecipeManagerSession recipes = RecipeManager.getSession();

        // TODO register with net.minecraftforge.oredict.RecipeSorter
        recipes.addRecipe(new RecipeEmptyCookingPot());

        recipes.close();
    }

    private static void registerRecipes() {
        Bids.LOG.info("Register standard recipes");

        RecipeManagerSession recipes = RecipeManager.getSession();

//        for (int i = 0; i < Global.ORE_METAL.length; i++) {
//            ItemStack small = new ItemStack(TFCItems.smallOreChunk, 1, i);
//            ItemStack poor = new ItemStack(TFCItems.oreChunk, 1, Global.oreGrade2Offset + i);
//            ItemStack normal = new ItemStack(TFCItems.oreChunk, 1, i);
//            ItemStack rich = new ItemStack(TFCItems.oreChunk, 1, Global.oreGrade1Offset + i);
//
//            if (CrucibleHelper.isOreIron(small)) {
//                recipes.addShapelessRecipe(new ItemStack(BidsItems.oreBit, 2, i),
//                        small, "itemHammerIronBits")
//                    .action(damageTool("itemHammerIronBits", 10));
//                recipes.addShapelessRecipe(new ItemStack(BidsItems.oreBit, 3, i),
//                        poor, "itemHammerIronBits")
//                    .action(damageTool("itemHammerIronBits", 20));
//                recipes.addShapelessRecipe(new ItemStack(BidsItems.oreBit, 5, i),
//                        normal, "itemHammerIronBits")
//                    .action(damageTool("itemHammerIronBits", 30));
//                recipes.addShapelessRecipe(new ItemStack(BidsItems.oreBit, 7, i),
//                        rich, "itemHammerIronBits")
//                    .action(damageTool("itemHammerIronBits", 40));
//            } else {
//                recipes.addShapelessRecipe(new ItemStack(BidsItems.oreBit, 2, i),
//                        small, "itemHammer")
//                    .action(damageTool("itemHammer", 1));
//                recipes.addShapelessRecipe(new ItemStack(BidsItems.oreBit, 3, i),
//                        poor, "itemHammer")
//                    .action(damageTool("itemHammer", 2));
//                recipes.addShapelessRecipe(new ItemStack(BidsItems.oreBit, 5, i),
//                        normal, "itemHammer")
//                    .action(damageTool("itemHammer", 3));
//                recipes.addShapelessRecipe(new ItemStack(BidsItems.oreBit, 7, i),
//                        rich, "itemHammer")
//                    .action(damageTool("itemHammer", 4));
//            }
//        }

        // This recipe is meant to upgrade an obsolete version 0.5.0 metal blowpipe
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.metalBlowpipe, 1, 1),
//            new ItemStack(BidsItems.metalBlowpipe, 1, 0));

//        recipes.addShapelessRecipe(new ItemStack(BidsItems.clayPipe),
//            TFCItems.clayTile, TFCItems.clayTile);

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
//            recipes.addShapedRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY, 2),
//                "PB", "BB", 'P', new ItemStack(BidsItems.clayPipe, 1, 1),
//                'B', stone.items.getItem(EnumStoneItemType.MUD_BRICK));
//            recipes.addShapedRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY, 2),
//                "PB", "BB", 'P', new ItemStack(TFCItems.logs, 1, 48), // Bamboo
//                'B', stone.items.getItem(EnumStoneItemType.MUD_BRICK));
//
//            recipes.addShapedRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE, 4),
//                    "SA", "  ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze")
//                .action(damageTool("itemAdze"));
//            recipes.addShapedRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE, 4),
//                    "AS", "  ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze")
//                .action(damageTool("itemAdze"));
//
//            recipes.addShapedRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_TILES),
//                "BB", "  ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE));
//            recipes.addShapelessRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE, 2),
//                stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_TILES));
//
//            recipes.addShapedRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK, 4),
//                    "S ", "A ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze")
//                .action(damageTool("itemAdze"));
//            recipes.addShapedRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK, 4),
//                    "A ", "S ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze")
//                .action(damageTool("itemAdze"));
//
//            recipes.addShapedRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_BRICKS),
//                "BB", "  ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK));
//            recipes.addShapelessRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK, 2),
//                stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_BRICKS));
//
//            recipes.addShapelessRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.SMOOTH_STONE, 2),
//                    stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), "itemChisel")
//                .action(damageTool("itemChisel"));
//
//            recipes.addShapedRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_BRICK_FENCE, 2),
//                "B ", "B ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK));
//            recipes.addShapelessRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK),
//                stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_BRICK_FENCE, 2));
//
//            recipes.addShapedRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_TILE_FENCE, 2),
//                "B ", "B ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE));
//            recipes.addShapelessRecipe(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE),
//                stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_TILE_FENCE, 2));
//
//            recipes.addShapelessRecipe(stone.items.getItem(EnumStoneItemType.STONE_BRICK),
//                    stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK), "itemChisel")
//                .action(damageTool("itemChisel"));
        }

        recipes.addShapelessRecipe(new ItemStack(BidsItems.whorl),
            "itemRock", "itemDrillHead");

        recipes.addShapelessRecipe(new ItemStack(BidsItems.spindle),
            "itemWhorl", "stickWood");

        recipes.addShapelessRecipe(new ItemStack(BidsItems.primitiveRopeMaker),
                "stickWood", "stickWood", "materialBindingStrong", "itemKnife")
            .action(damageTool("itemKnife"));

        recipes.addShapelessRecipe(new ItemStack(BidsItems.thornCard),
            BidsItems.thornBunch, BidsItems.thornBunch, TFCItems.resin, BidsItems.woodenCombPaddle);

        recipes.addShapelessRecipe(new ItemStack(BidsItems.boneHeckle),
            BidsItems.boneKnifeHead, BidsItems.boneKnifeHead, TFCItems.resin, "materialBindingDecent");

//        recipes.addShapelessRecipe(new ItemStack(BidsItems.igInStoneDrill),
//            BidsItems.igInStoneDrillHead, "stickWood", TFCItems.bow);
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.sedStoneDrill),
//            BidsItems.sedStoneDrillHead, "stickWood", TFCItems.bow);
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.igExStoneDrill),
//            BidsItems.igExStoneDrillHead, "stickWood", TFCItems.bow);
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.mMStoneDrill),
//            BidsItems.mMStoneDrillHead, "stickWood", TFCItems.bow);

//        recipes.addShapedRecipe(new ItemStack(BidsItems.igInStoneAdze),
//            "1", "2", '1', BidsItems.igInStoneAdzeHead, '2', "stickWood");
//        recipes.addShapedRecipe(new ItemStack(BidsItems.sedStoneAdze),
//            "1", "2", '1', BidsItems.sedStoneAdzeHead, '2', "stickWood");
//        recipes.addShapedRecipe(new ItemStack(BidsItems.igExStoneAdze),
//            "1", "2", '1', BidsItems.igExStoneAdzeHead, '2', "stickWood");
//        recipes.addShapedRecipe(new ItemStack(BidsItems.mMStoneAdze),
//            "1", "2", '1', BidsItems.mMStoneAdzeHead, '2', "stickWood");
//
//        recipes.addShapedRecipe(new ItemStack(BidsItems.igInStoneAdze),
//            "1", "2", '1', BidsItems.igInStoneAdzeHead, '2', TFCItems.bone);
//        recipes.addShapedRecipe(new ItemStack(BidsItems.sedStoneAdze),
//            "1", "2", '1', BidsItems.sedStoneAdzeHead, '2', TFCItems.bone);
//        recipes.addShapedRecipe(new ItemStack(BidsItems.igExStoneAdze),
//            "1", "2", '1', BidsItems.igExStoneAdzeHead, '2', TFCItems.bone);
//        recipes.addShapedRecipe(new ItemStack(BidsItems.mMStoneAdze),
//            "1", "2", '1', BidsItems.mMStoneAdzeHead, '2', TFCItems.bone);

//        recipes.addShapelessRecipe(new ItemStack(BidsItems.copperAdzeHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 2)));
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.bronzeAdzeHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 3)));
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.bismuthBronzeAdzeHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 4)));
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.blackBronzeAdzeHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 5)));

//        recipes.addShapelessRecipe(new ItemStack(BidsItems.copperDrillHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 2)));
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.bronzeDrillHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 3)));
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.bismuthBronzeDrillHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 4)));
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.blackBronzeDrillHead), getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 5)));

//        recipes.addShapedRecipe(new ItemStack(BidsItems.copperAdze, 1),
//            "#", "I", '#', BidsItems.copperAdzeHead, 'I', "stickWood");
//        recipes.addShapedRecipe(new ItemStack(BidsItems.bronzeAdze, 1),
//            "#", "I", '#', BidsItems.bronzeAdzeHead, 'I', "stickWood");
//        recipes.addShapedRecipe(new ItemStack(BidsItems.bismuthBronzeAdze, 1),
//            "#", "I", '#', BidsItems.bismuthBronzeAdzeHead, 'I', "stickWood");
//        recipes.addShapedRecipe(new ItemStack(BidsItems.blackBronzeAdze, 1),
//            "#", "I", '#', BidsItems.blackBronzeAdzeHead, 'I', "stickWood");
//        recipes.addShapedRecipe(new ItemStack(BidsItems.wroughtIronAdze, 1),
//            "#", "I", '#', BidsItems.wroughtIronAdzeHead, 'I', "stickWood");

//        recipes.addShapelessRecipe(new ItemStack(BidsItems.copperDrill, 1),
//            BidsItems.copperDrillHead, "stickWood", TFCItems.bow);
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.bronzeDrill, 1),
//            BidsItems.bronzeDrillHead, "stickWood", TFCItems.bow);
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.bismuthBronzeDrill, 1),
//            BidsItems.bismuthBronzeDrillHead, "stickWood", TFCItems.bow);
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.blackBronzeDrill, 1),
//            BidsItems.blackBronzeDrillHead, "stickWood", TFCItems.bow);
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.wroughtIronDrill, 1),
//            BidsItems.wroughtIronDrillHead, "stickWood", TFCItems.bow);

        recipes.addShapelessRecipe(new ItemStack(BidsItems.smallStickBundle),
            "stickWood", "stickWood", "stickWood");
        recipes.addShapelessRecipe(new ItemStack(TFCItems.stick, 3),
            BidsItems.smallStickBundle);

        recipes.addShapelessRecipe(new ItemStack(BidsItems.kindling),
            "stickWood", "stickWood", "stickWood", TFCItems.straw);
        recipes.addShapelessRecipe(new ItemStack(BidsItems.kindling),
            BidsItems.smallStickBundle, TFCItems.straw);

        recipes.addShapelessRecipe(new ItemStack(BidsItems.barkFibreKindling),
            "stickWood", "stickWood", "stickWood", BidsItems.barkFibreCoarse);
        recipes.addShapelessRecipe(new ItemStack(BidsItems.barkFibreKindling),
            BidsItems.smallStickBundle, BidsItems.barkFibreCoarse);

//        recipes.addShapelessRecipe(new ItemStack(BidsItems.birchBarkKindling),
//            "stickWood", "stickWood", "stickWood", BidsItems.birchBarkStrap);
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.birchBarkKindling),
//            BidsItems.smallStickBundle, BidsItems.birchBarkStrap);
//
        recipes.addShapelessRecipe(new ItemStack(BidsItems.tiedStickBundle),
            BidsItems.smallStickBundle, new ItemStack(BidsItems.smallStickBundle),
            BidsItems.smallStickBundle, TFCItems.grassCordage);
        recipes.addShapelessRecipe(new ItemStack(BidsItems.tiedStickBundle),
            TFCItems.stickBundle, TFCItems.grassCordage);

        recipes.addShapelessRecipe(new ItemStack(TFCItems.stick, 9),
            BidsItems.tiedStickBundle);

        recipes.addShapelessRecipe(new ItemStack(BidsItems.barkFibre),
                "itemBarkHasFibers", "itemKnife")
            .action(damageTool("itemKnife"));

        recipes.addShapelessRecipe(new ItemStack(BidsItems.juteStalk),
                TFCItems.jute, "itemKnife")
            .action(damageTool("itemKnife"));

        recipes.addShapelessRecipe(new ItemStack(BidsItems.flaxStalk),
                TFCItems.flax, "itemKnife")
            .action(damageTool("itemKnife"))
            .action(extraDrop(ItemFoodTFC.createTag(new ItemStack(BidsItems.flaxSeeds), 6)));

        recipes.addShapelessRecipe(new ItemStack(BidsItems.cottonBollRefined),
                BidsItems.cottonBoll, "itemKnife")
            .action(damageTool("itemKnife"));

        // Refining TFC cotton in case it has not been converted
        recipes.addShapelessRecipe(new ItemStack(BidsItems.cottonBollRefined),
                TFCItems.cotton, "itemKnife")
            .action(damageTool("itemKnife"));

//        recipes.addShapelessRecipe(new ItemStack(BidsItems.birchBarkCup, 1),
//            BidsItems.birchBarkCupUnfinished, Items.slime_ball);
//
//        recipes.addShapelessRecipe(new ItemStack(BidsItems.birchBarkSheet, 1),
//                new ItemStack(BidsItems.bark, 1, 2), "itemKnife")
//            .action(damageTool("itemKnife"));

//        recipes.addShapedRecipe(new ItemStack(BidsBlocks.wattleGate),
//            "PW", "  ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle);
//        recipes.addShapedRecipe(new ItemStack(BidsBlocks.wattleGate),
//            "WP", "  ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle);
//
//        recipes.addShapedRecipe(new ItemStack(BidsBlocks.wattleTrapdoor),
//            "P ", "W ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle);
//        recipes.addShapedRecipe(new ItemStack(BidsBlocks.wattleTrapdoor),
//            "W ", "P ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle);
//
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

        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
//            if (wood.items.hasPeeledLog()) {
//                recipes.addShapelessRecipe(wood.items.getPeeledLog(),
//                        wood.items.getLog(), "itemAdze")
//                    .action(damageTool("itemAdze"))
//                    .action(extraDrop(wood.items.getBark(), BidsOptions.Bark.dropPeelingChance))
//                    .action(copySeasoning(TFCItems.logs));
//
//                BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
//                    wood.items.getPeeledLog(), wood.items.getLog()));
//
//                if (wood.items.hasChoppedLog()) {
//                    recipes.addShapelessRecipe(wood.items.getPeeledLog(),
//                            wood.items.getChoppedLog(), "itemAdze")
//                        .action(damageTool("itemAdze"))
//                        .action(extraDrop(wood.items.getBark(), BidsOptions.Bark.dropPeelingChance));
//
//                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
//                        wood.items.getPeeledLog(), wood.items.getChoppedLog()));
//                }
//            }

//            if (wood.items.hasSeasonedPeeledLog()) {
//                recipes.addShapelessRecipe(wood.items.getSeasonedPeeledLog(),
//                        wood.items.getSeasonedLog(), "itemAdze")
//                    .action(damageTool("itemAdze"))
//                    .action(extraDrop(wood.items.getBark(), BidsOptions.Bark.dropPeelingSeasonedChance));
//
//                BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
//                    wood.items.getSeasonedPeeledLog(), wood.items.getSeasonedLog()));
//
//                if (wood.items.hasSeasonedChoppedLog()) {
//                    recipes.addShapelessRecipe(wood.items.getSeasonedPeeledLog(),
//                            wood.items.getSeasonedChoppedLog(), "itemAdze")
//                        .action(damageTool("itemAdze"))
//                        .action(extraDrop(wood.items.getBark(), BidsOptions.Bark.dropPeelingSeasonedChance));
//
//                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
//                        wood.items.getSeasonedPeeledLog(), wood.items.getSeasonedChoppedLog()));
//                }
//
////                BidsRegistry.SEASONING_RECIPES.register(new SeasoningRecipe(wood.items.getSeasonedPeeledLog(),
////                    wood.items.getPeeledLog(),
////                    SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.PEELED_LOG)));
//            }

            if (wood.items.hasSeasonedLog()) {
//                BidsRegistry.SEASONING_RECIPES.register(new SeasoningRecipe(wood.items.getSeasonedLog(),
//                    wood.items.getLog(),
//                    SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.LOG)));
//
//                if (wood.items.hasChoppedLog()) {
//                    BidsRegistry.SEASONING_RECIPES.register(new SeasoningRecipe(wood.items.getSeasonedChoppedLog(),
//                        wood.items.getChoppedLog(),
//                        SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.CHOPPED_LOG)));
//                }
            }

//            if (wood.items.hasFirewood()) {
//                recipes.addShapelessRecipe(wood.items.getFirewood(),
//                        wood.getOreWithSuffix("logWoodFresh"), "itemAxe")
//                    .action(damageTool("itemAxe"))
//                    .action(extraDrop(wood.items.getBark(), BarkConfig.dropSplittingChance))
//                    .action(copySeasoning(TFCItems.logs))
//                    .action(copySeasoning(BidsItems.peeledLog));
//
//                BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
//                    wood.items.getFirewood(),
//                    wood.items.getLog()));
//
//                if (wood.items.hasChoppedLog()) {
//                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
//                        wood.items.getFirewood(),
//                        wood.items.getChoppedLog()));
//                }
//
//                if (wood.items.hasPeeledLog()) {
//                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
//                        wood.items.getFirewood(),
//                        wood.items.getPeeledLog()));
//                }
//            }
//
//            if (wood.items.hasSeasonedFirewood()) {
//                recipes.addShapelessRecipe(wood.items.getSeasonedFirewood(),
//                        wood.getOreWithSuffix("logWoodSeasoned"), "itemAxe")
//                    .action(damageTool("itemAze"))
//                    .action(extraDrop(wood.items.getBark(), BarkConfig.dropSplittingSeasonedChance));
//
//                if (wood.items.hasSeasonedLog()) {
//                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
//                        wood.items.getSeasonedFirewood(),
//                        wood.items.getSeasonedLog()));
//                }
//
//                if (wood.items.hasSeasonedChoppedLog()) {
//                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
//                        wood.items.getSeasonedFirewood(),
//                        wood.items.getSeasonedChoppedLog()));
//                }
//
//                if (wood.items.hasSeasonedPeeledLog()) {
//                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
//                        wood.items.getSeasonedFirewood(),
//                        wood.items.getSeasonedPeeledLog()));
//                }
//
////                BidsRegistry.SEASONING_RECIPES.register(new SeasoningRecipe(wood.items.getSeasonedFirewood(),
////                    wood.items.getFirewood(),
////                    SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.FIREWOOD)));
//            }

            if (wood.blocks.hasLogWall()) {
                if (wood.items.hasSeasonedPeeledLog()) {
                    recipes.addShapedRecipe(wood.blocks.getLogWall(),
                            "A ", "11", '1', wood.getOreWithSuffix("logWoodSeasoned"), 'A', "itemAdze")
                        .action(damageTool("itemAdze"));
                    recipes.addShapelessRecipe(wood.items.getSeasonedPeeledLog(2),
                        wood.blocks.getLogWall());

                    recipes.addShapedRecipe(wood.blocks.getLogWallVert(),
                            "A1", " 1", '1', wood.getOreWithSuffix("logWoodSeasoned"), 'A', "itemAdze")
                        .action(damageTool("itemAdze"));
                    recipes.addShapelessRecipe(wood.items.getSeasonedPeeledLog(2),
                        wood.blocks.getLogWallVert());
                } else {
                    recipes.addShapedRecipe(wood.blocks.getLogWall(),
                            "A ", "11", '1', wood.getOreWithSuffix("logWood"), 'A', "itemAdze")
                        .action(damageTool("itemAdze"));
                    recipes.addShapelessRecipe(wood.items.getLog(2),
                        wood.blocks.getLogWall());

                    recipes.addShapedRecipe(wood.blocks.getLogWallVert(),
                            "A1", " 1", '1', wood.getOreWithSuffix("logWood"), 'A', "itemAdze")
                        .action(damageTool("itemAdze"));
                    recipes.addShapelessRecipe(wood.items.getLog(2),
                        wood.blocks.getLogWallVert());
                }
            }

            if (wood.blocks.hasPalisade()) {
                recipes.addShapedRecipe(wood.blocks.getPalisade(2),
                        "A1", " 1", '1', wood.getOreWithSuffix("logWood"), 'A', "itemAxe")
                    .action(damageTool("itemAxe"));
            }

//            // Copies of TFC recipes for items made logs
//            if (wood.items.hasLumber()) {
//                recipes.addShapelessRecipe(wood.items.getLumber(8),
//                        wood.getOreWithSuffix("logWoodSeasoned"), "itemSaw")
//                    .action(damageTool("itemSaw"));
//            }
//
//            // Copies of TFC recipes for block made from logs
//            if (wood.items.hasPeeledLog() || wood.items.hasSeasonedLog()) {
//                recipes.addShapedRecipe(wood.blocks.getWoodSupport(8),
//                        "A2", " 2", '2', wood.getOreWithSuffix("logWood"), 'A', "itemSaw")
//                    .action(damageTool("itemSaw"));
//
//                recipes.addShapedRecipe(wood.blocks.getFence(6),
//                    "LPL", "LPL", 'L', wood.getOreWithSuffix("logWood"), 'P', wood.items.getLumber());
//            }
        }

        recipes.addShapelessRecipe(new ItemStack(TFCItems.woodenSpear, 1),
                TFCItems.pole, "itemHandAxe")
            .action(damageTool("itemHandAxe"));

//        recipes.addShapelessRecipe(new ItemStack(BidsItems.plugAndFeather, 4),
//                "logWoodPlugAndFeather", "itemAdze")
//            .action(damageTool("itemAdze"));

        recipes.addShapelessRecipe(new ItemStack(TFCItems.hide),
                new ItemStack(BidsItems.moreHide, 1, 0), new ItemStack(BidsItems.moreHide, 1, 0), "itemNeedleAndThread")
            .action(damageTool("itemNeedleAndThread", 10));

        recipes.addShapelessRecipe(new ItemStack(TFCItems.hide, 1, 1),
                new ItemStack(TFCItems.hide, 1, 0), new ItemStack(TFCItems.hide, 1, 0), "itemNeedleAndThread")
            .action(damageTool("itemNeedleAndThread", 20));

        recipes.addShapelessRecipe(new ItemStack(TFCItems.hide, 1, 2),
                new ItemStack(TFCItems.hide, 1, 1), new ItemStack(TFCItems.hide, 1, 1), "itemNeedleAndThread")
            .action(damageTool("itemNeedleAndThread", 40));

        recipes.addShapelessRecipe(new ItemStack(BidsItems.moreHide, 2),
                new ItemStack(TFCItems.hide, 1, 0), "itemKnife")
            .action(damageTool("itemKnife"));

//        // Copies of TFC recipes for generic wood items made logs
//        recipes.addShapelessRecipe(new ItemStack(TFCItems.pole),
//                "logWoodAny", "itemKnife")
//            .action(damageTool("itemKnife"));
//        recipes.addShapedRecipe(new ItemStack(TFCItems.clayTile),
//                " X", "XL", 'L', "logWoodAny", 'X', "lumpClay")
//            .action(keepItem("logWoodAny"));
//        recipes.addShapelessRecipe(new ItemStack(TFCItems.paddle),
//                TFCItems.pole, "logWoodAny", "itemKnife")
//            .action(damageTool("itemKnife"));
//
        recipes.addShapedRecipe(new ItemStack(TFCItems.quern),
            "  W", "PPP", 'P', "stoneQuern", 'W', "stickWood");
        recipes.addShapedRecipe(new ItemStack(TFCItems.millstone),
            "PPP", "P P", "PPP", 'P', "stoneQuern");

        recipes.addShapedRecipe(new ItemStack(BidsBlocks.woodAxleWallBearing),
            "LSL", "L L", "LSL", 'L', "woodLumber", 'S', "supportWood");

        recipes.addShapelessRecipe(new ItemStack(BidsBlocks.woodScrew),
                TFCBlocks.woodAxle, "itemChisel")
            .action(damageTool("itemChisel"));

        recipes.addShapedRecipe(new ItemStack(BidsBlocks.screwPressRackBottom),
            "SLS", "S S", "SLS", 'L', "woodLumber", 'S', "supportWood");
        recipes.addShapedRecipe(new ItemStack(BidsBlocks.screwPressRackBridge),
            "SSS", "L L", "SSS", 'L', "woodLumber", 'S', "supportWood");
        recipes.addShapedRecipe(new ItemStack(BidsBlocks.screwPressBarrel),
            "LTL", "LLL", "LPL", 'T', "plateToolMetal", 'L', "woodLumber", 'P', "plankWood");
        recipes.addShapedRecipe(new ItemStack(BidsBlocks.screwPressDisc),
            "L L", "LPL", "   ", 'L', "woodLumber", 'P', "plankWood");
        recipes.addShapedRecipe(new ItemStack(BidsBlocks.screwPressLever),
                "LTL", " L ", " L ", 'T', "itemSaw", 'L', "logWoodPeeledSeasoned")
            .action(damageTool("itemSaw"));

        recipes.addShapedRecipe(new ItemStack(BidsItems.woodenPailEmpty),
            "w  ", "wxw", " w ", 'w', "woodLumber", 'x', "plateToolMetal");

//        recipes.addShapedRecipe(new ItemStack(BidsBlocks.clayLamp),
//            "S ", "B ", 'S', "materialString",
//            'B', new ItemStack(TFCItems.potteryBowl, 1, 1));

        recipes.addShapelessRecipe(new ItemStack(BidsBlocks.wallHook),
            "stickWood", TFCItems.resin);

//        recipes.addShapelessRecipe(new ItemStack(BidsItems.honeyLargeBowl),
//                "itemHoneycomb", "itemHoneycomb", "itemKnife", new ItemStack(BidsItems.largeClayBowl, 1, 1))
//            .action(damageTool("itemKnife"))
//            .action(extraDrop(new ItemStack(TFCItems.emptyHoneycomb, 2)));

        recipes.addShapelessRecipe(new ItemStack(BidsItems.ceramicBucketRope),
            TFCItems.rope, TFCItems.clayBucketEmpty);
        recipes.addShapelessRecipe(new ItemStack(BidsItems.woodenBucketRope),
            TFCItems.rope, TFCItems.woodenBucketEmpty);

        recipes.addShapelessRecipe(new ItemStack(TFCItems.clayBucketEmpty),
                BidsItems.ceramicBucketRope)
            .action(extraDrop(new ItemStack(TFCItems.rope)));

        recipes.addShapelessRecipe(new ItemStack(TFCItems.woodenBucketEmpty),
                BidsItems.woodenBucketRope)
            .action(extraDrop(new ItemStack(TFCItems.rope)));

        recipes.addShapelessRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.butter, 1)),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.butter, 1)), new ItemStack(TFCItems.powder, 1, 9));

//        recipes.addShapelessRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.bambooShoot), 2.5f),
//                new ItemStack(TFCBlocks.sapling2, 1, 8), "itemKnife")
//            .action(damageTool("itemKnife"));

        // Manual seed conversion
        recipes.addShapelessRecipe(new ItemStack(TFCItems.seedsBarley), BidsItems.seedsNewBarley);
        recipes.addShapelessRecipe(new ItemStack(TFCItems.seedsOat), BidsItems.seedsNewOat);
        recipes.addShapelessRecipe(new ItemStack(TFCItems.seedsRye), BidsItems.seedsNewRye);
        recipes.addShapelessRecipe(new ItemStack(TFCItems.seedsWheat), BidsItems.seedsNewWheat);
        recipes.addShapelessRecipe(new ItemStack(TFCItems.seedsOnion), BidsItems.seedsNewOnion);
        recipes.addShapelessRecipe(new ItemStack(TFCItems.seedsCabbage), BidsItems.seedsNewCabbage);
        recipes.addShapelessRecipe(new ItemStack(TFCItems.seedsGarlic), BidsItems.seedsNewGarlic);
        recipes.addShapelessRecipe(new ItemStack(TFCItems.seedsCarrot), BidsItems.seedsNewCarrot);

        // Reverse manual seed conversion
        recipes.addShapelessRecipe(new ItemStack(BidsItems.seedsNewBarley), TFCItems.seedsBarley);
        recipes.addShapelessRecipe(new ItemStack(BidsItems.seedsNewOat), TFCItems.seedsOat);
        recipes.addShapelessRecipe(new ItemStack(BidsItems.seedsNewRye), TFCItems.seedsRye);
        recipes.addShapelessRecipe(new ItemStack(BidsItems.seedsNewWheat), TFCItems.seedsWheat);
        recipes.addShapelessRecipe(new ItemStack(BidsItems.seedsNewOnion), TFCItems.seedsOnion);
        recipes.addShapelessRecipe(new ItemStack(BidsItems.seedsNewCabbage), TFCItems.seedsCabbage);
        recipes.addShapelessRecipe(new ItemStack(BidsItems.seedsNewGarlic), TFCItems.seedsGarlic);
        recipes.addShapelessRecipe(new ItemStack(BidsItems.seedsNewCarrot), TFCItems.seedsCarrot);

        recipes.addShapedRecipe(new ItemStack(BidsBlocks.fireBrickChimney, 2),
            "P P", "X X", "P P", 'P', new ItemStack(TFCItems.fireBrick, 1, 1),
            'X', new ItemStack(TFCItems.mortar, 1));

        recipes.close();

//        RecipeHelper.handleCompositeToolRecipes();
        RecipeHelper.handleSpindleSpinningRecipes();
        RecipeHelper.handleRopeMakingRecipes();
        RecipeHelper.handleLoomRecipes();
    }

    private static void registerCarvingRecipes() {
        Bids.LOG.info("Register carving recipes");

//        CarvingRecipePattern choppingBlockPattern = new CarvingRecipePattern()
//            .carveEntireLayer();
//
//        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
//            if (wood.blocks.hasChoppingBlock()) {
//                BidsRegistry.CARVING_RECIPES.register(new CarvingRecipe(wood.blocks.getChoppingBlock(),
//                    wood.blocks.getWoodVert(), choppingBlockPattern));
//            }
//        }
//
//        CarvingRecipePattern saddleQuernPattern = new CarvingRecipePattern()
//            .carveLayer("    ", " ## ", " ## ", " ## ");
//
//        CarvingRecipePattern[] handstonePatterns = {
//            new CarvingRecipePattern()
//                .carveEntireLayer()
//                .carveEntireLayer()
//                .carveLayer("####", "#  #", "#  #", "#  #")
//                .carveLayer("####", "#  #", "#  #", "#  #"),
//            new CarvingRecipePattern()
//                .carveEntireLayer()
//                .carveEntireLayer()
//                .carveLayer("####", "  ##", "  ##", "  ##")
//                .carveLayer("####", "  ##", "  ##", "  ##"),
//            new CarvingRecipePattern()
//                .carveEntireLayer()
//                .carveEntireLayer()
//                .carveLayer("####", "##  ", "##  ", "##  ")
//                .carveLayer("####", "##  ", "##  ", "##  ")
//        };
//
//        CarvingRecipePattern pressingStonePattern = new CarvingRecipePattern()
//            .carveEntireLayer()
//            .carveEntireLayer()
//            .carveLayer("####", "#   ", "#   ", "#   ")
//            .carveLayer("####", "#   ", "#   ", "#   ");
//
//
//        CarvingRecipePattern weightStonePattern = new CarvingRecipePattern()
//            .carveEntireLayer()
//            .carveLayer("####", "#   ", "#   ", "#   ")
//            .carveLayer("####", "#   ", "#   ", "#   ")
//            .carveLayer("####", "#   ", "#   ", "#   ");
//
//        CarvingRecipePattern chimneyPattern = new CarvingRecipePattern()
//            .carveLayer("    ", " ## ", " ## ", "    ")
//            .carveLayer("    ", " ## ", " ## ", "    ")
//            .carveLayer("    ", " ## ", " ## ", "    ")
//            .carveLayer("    ", " ## ", " ## ", "    ");
//
//
//        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
//            if (stone.soft) {
//                BidsRegistry.CARVING_RECIPES.register(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.SADDLE_QUERN),
//                    stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), saddleQuernPattern));
//
//                for (int j = 0; j < handstonePatterns.length; j++) {
//                    BidsRegistry.CARVING_RECIPES.register(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.HAND_STONE),
//                        stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), handstonePatterns[j]));
//                }
//
//                BidsRegistry.CARVING_RECIPES.register(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.PRESSING_STONE),
//                    stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), pressingStonePattern));
//
//                BidsRegistry.CARVING_RECIPES.register(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.WEIGHT_STONE),
//                    stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), weightStonePattern));
//            }

//            BidsRegistry.CARVING_RECIPES.register(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY),
//                stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICKS), chimneyPattern));
//        }
    }

    private static void registerSaddleQuernRecipes() {
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(BidsItems.wheatCrushed),
//            new ItemStack(TFCItems.wheatGrain)));
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(BidsItems.barleyCrushed),
//            new ItemStack(TFCItems.barleyGrain)));
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(BidsItems.oatCrushed),
//            new ItemStack(TFCItems.oatGrain)));
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(BidsItems.riceCrushed),
//            new ItemStack(TFCItems.riceGrain)));
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(BidsItems.ryeCrushed),
//            new ItemStack(TFCItems.ryeGrain)));
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(BidsItems.cornmealCrushed),
//            new ItemStack(TFCItems.maizeEar)));
//
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(BidsItems.appleCrushed),
//            new ItemStack(TFCItems.greenApple)));
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(BidsItems.appleCrushed),
//            new ItemStack(TFCItems.redApple)));
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(BidsItems.oliveCrushed),
//            new ItemStack(TFCItems.olive)));
//
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 2, 9), // Salt
//            new ItemStack(TFCItems.looseRock, 1, 5)));
//
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(TFCItems.dye, 1, 15), // Bone Meal
//            new ItemStack(TFCItems.bone, 1)));
//        BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(TFCItems.dye, 1, 15), // Bone Meal
//            new ItemStack(TFCItems.boneFragment, 1)));
//
//        if (SaddleQuernConfig.allowGrindHematite) {
//            BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 1, 5), // Hematite
//                new ItemStack(TFCItems.smallOreChunk, 1, 3)));
//        }
//        if (SaddleQuernConfig.allowGrindLimonite) {
//            BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 1, 7), // Limonite
//                new ItemStack(TFCItems.smallOreChunk, 1, 11)));
//        }
//        if (SaddleQuernConfig.allowGrindMalachite) {
//            BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 1, 8), // Malachite
//                new ItemStack(TFCItems.smallOreChunk, 1, 9)));
//        }
//        if (SaddleQuernConfig.allowGrindLapisLazuli) {
//            BidsRegistry.SADDLE_QUERN_RECIPES.register(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 2, 6), // Lapis Lazuli
//                new ItemStack(TFCItems.oreChunk, 1, 318)));
//        }
    }

    private static void registerStonePressRecipes() {
        // Stone press efficiency affects the recipe input or output volume
//        float inputMult = 1 / StonePressConfig.efficiency; // input multiplier (for non-food input)
//        float outputMult = StonePressConfig.efficiency; // output multiplier (for food input)
//
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.OLIVEOIL, 10),
//            ItemFoodTFC.createTag(new ItemStack(BidsItems.oliveCrushed), 0.64f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.APPLEJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(BidsItems.appleCrushed), 0.7f * inputMult)));
//
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.GRAPEJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.grapes), 0.5f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.CANEJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.sugarcane), 0.8f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.LEMONJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.lemon), 0.65f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.ORANGEJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.orange), 0.5f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.PEACHJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.peach), 0.55f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.PLUMJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.plum), 0.65f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.FIGJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.fig), 0.5f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.CHERRYJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.cherry), 0.7f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.DATEJUICE, 6),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.date), 0.8f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.PAPAYAJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.papaya), 0.6f * inputMult)));
//
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.strawberry), 0.65f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.blackberry), 0.61f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.blueberry), 0.6f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.bunchberry), 0.68f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.cranberry), 0.7f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.elderberry), 0.58f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.gooseberry), 0.6f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.raspberry), 0.6f * inputMult)));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
//            ItemFoodTFC.createTag(new ItemStack(TFCItems.snowberry), 0.66f * inputMult)));
//
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(TFCFluids.AGAVEJUICE, Math.round(40 * outputMult)),
//            new ItemStack(TFCItems.agave, 1)));
//
//        ItemStack steamedFish = BidsFood.setSteamed(ItemFoodTFC.createTag(new ItemStack(TFCItems.fishRaw), 0.5f * inputMult), true);
//        // Require fish to be steamed to medium level
//        Food.setCooked(steamedFish, CookingHelper.getTempForItemStackCookedLevel(steamedFish, 3));
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(BidsFluids.OILYFISHWATER, 10), steamedFish));
//
//        BidsRegistry.STONE_PRESS_RECIPES.register(new StonePressRecipe(new FluidStack(BidsFluids.FLAXSEEDOIL, 10),
//            ItemFoodTFC.createTag(new ItemStack(BidsItems.flaxSeeds), 0.8f * inputMult)));
    }

    private static void registerScrewPressRecipes() {
        // Screw press efficiency affects the recipe input or output volume
        float inputMult = 1 / BidsOptions.ScrewPress.efficiency; // input multiplier (for non-food input)
        float outputMult = BidsOptions.ScrewPress.efficiency; // output multiplier (for food input)

        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.OLIVEOIL, 10),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.oliveCrushed), 0.64f * inputMult), 0.5f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.APPLEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.appleCrushed), 0.7f * inputMult), 0.5f));

        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.OLIVEOIL, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.olive), 0.64f * inputMult), 1f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.APPLEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.redApple), 0.7f * inputMult), 1f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.APPLEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.greenApple), 0.7f * inputMult), 1f));

        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.GRAPEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.grapes), 0.5f * inputMult), 0.5f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.CANEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.sugarcane), 0.8f * inputMult), 0.8f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.LEMONJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.lemon), 0.65f * inputMult), 0.65f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.ORANGEJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.orange), 0.5f * inputMult), 0.65f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.PEACHJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.peach), 0.55f * inputMult), 0.8f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.PLUMJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.plum), 0.65f * inputMult), 0.8f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.FIGJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.fig), 0.5f * inputMult), 0.8f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.CHERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.cherry), 0.7f * inputMult), 0.8f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.DATEJUICE, 6),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.date), 0.8f * inputMult), 0.8f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.PAPAYAJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.papaya), 0.6f * inputMult), 0.8f));

        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.strawberry), 0.65f * inputMult), 0.5f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.blackberry), 0.61f * inputMult), 0.5f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.blueberry), 0.6f * inputMult), 0.5f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.bunchberry), 0.68f * inputMult), 0.5f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.cranberry), 0.7f * inputMult), 0.5f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.elderberry), 0.58f * inputMult), 0.5f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.gooseberry), 0.6f * inputMult), 0.5f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.raspberry), 0.6f * inputMult), 0.5f));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
            ItemFoodTFC.createTag(new ItemStack(TFCItems.snowberry), 0.66f * inputMult), 0.5f));

        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(TFCFluids.AGAVEJUICE, Math.round(40 * outputMult)),
            new ItemStack(TFCItems.agave, 1), 0.8f));

        ItemStack steamedFish = BidsFood.setSteamed(ItemFoodTFC.createTag(new ItemStack(TFCItems.fishRaw), 0.5f * inputMult), true);
        // Require fish to be steamed to medium level
        Food.setCooked(steamedFish, CookingHelper.getTempForItemStackCookedLevel(steamedFish, 3));
        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(BidsFluids.OILYFISHWATER, 10), steamedFish, 0.65f));

        BidsRegistry.SCREW_PRESS_RECIPES.register(new ScrewPressRecipe(new FluidStack(BidsFluids.FLAXSEEDOIL, 10),
            ItemFoodTFC.createTag(new ItemStack(BidsItems.flaxSeeds), 0.8f * inputMult), 0.25f));
    }

    private static void registerDryingRecipes() {
        Bids.LOG.info("Register drying recipes");
//
//        BidsRegistry.DRYING_RACK_RECIPES.register((DryingRackRecipe) DryingRackRecipe.builder()
//            .consumes(new ItemStack(BidsItems.barkFibre))
//            .produces(new ItemStack(BidsItems.barkFibreCoarse))
//            .dry()
//            .hours(12)
//            .build());
//        BidsRegistry.DRYING_RACK_RECIPES.register((DryingRackRecipe) DryingRackRecipe.builder()
//            .consumes(new ItemStack(BidsItems.sisalFiberRinsed))
//            .produces(new ItemStack(BidsItems.sisalFiberCoarse))
//            .dry()
//            .hours(8)
//            .build());
//        BidsRegistry.DRYING_RACK_RECIPES.register((DryingRackRecipe) DryingRackRecipe.builder()
//            .consumes(new ItemStack(TFCItems.juteFiber))
//            .produces(new ItemStack(BidsItems.juteFiberCoarse))
//            .dry()
//            .hours(12)
//            .build());
//        BidsRegistry.DRYING_RACK_RECIPES.register((DryingRackRecipe) DryingRackRecipe.builder()
//            .consumes(new ItemStack(BidsItems.flaxStalkRetted))
//            .produces(new ItemStack(BidsItems.flaxStalkDried))
//            .dry()
//            .hours(16)
//            .build());
//        BidsRegistry.DRYING_RACK_RECIPES.register((DryingRackRecipe) DryingRackRecipe.builder()
//            .consumes(new ItemStack(BidsItems.woolRinsed))
//            .produces(new ItemStack(BidsItems.woolDried))
//            .dry()
//            .hours(2)
//            .build());
//
//        // Meat and cheese drying from TFC
//        final Item[] foodToDry = new Item[]{TFCItems.venisonRaw, TFCItems.beefRaw, TFCItems.chickenRaw,
//            TFCItems.porkchopRaw, TFCItems.fishRaw, TFCItems.seastarRaw, TFCItems.scallopRaw,
//            TFCItems.calamariRaw, TFCItems.muttonRaw, TFCItems.horseMeatRaw, TFCItems.cheese,
//            BidsItems.goatCheese};
//        for (Item food : foodToDry) {
//            BidsRegistry.DRYING_RACK_RECIPES.register((DryingRackFoodRecipe) DryingRackFoodRecipe.builder()
//                .smoke(12)
//                .tied()
//                .consumes(ItemFoodTFC.createTag(new ItemStack(food), 1))
//                .dry()
//                .hours(16)
//                .build());
//        }
//
//        // Extra food drying
//        BidsRegistry.DRYING_RACK_RECIPES.register((DryingRackFoodRecipe) DryingRackFoodRecipe.builder()
//            .consumes(ItemFoodTFC.createTag(new ItemStack(TFCItems.seaWeed), 1))
//            .dry()
//            .hours(16)
//            .build());

        BidsRegistry.DRYING_SURFACE_RECIPES.register((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
            .consumes(new ItemStack(BidsItems.barkFibre))
            .produces(new ItemStack(BidsItems.barkFibreCoarse))
            .dry()
            .hours(12)
            .build());
        BidsRegistry.DRYING_SURFACE_RECIPES.register((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
            .consumes(new ItemStack(BidsItems.flaxStalk))
            .produces(new ItemStack(BidsItems.flaxStalkRetted))
            .wet()
            .warm()
            .hours(20)
            .build());
        BidsRegistry.DRYING_SURFACE_RECIPES.register((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
            .consumes(new ItemStack(BidsItems.flaxStalkRetted))
            .produces(new ItemStack(BidsItems.flaxStalkDried))
            .dry()
            .hours(20)
            .build());
        BidsRegistry.DRYING_SURFACE_RECIPES.register((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
            .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.uncuredSoap), 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.soap), 1))
            .dry()
            .cover()
            .hours(40)
            .build());

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            BidsRegistry.DRYING_SURFACE_RECIPES.register((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .consumes(stone.items.getItem(EnumStoneItemType.MUD_BRICK_WET))
                .produces(stone.items.getItem(EnumStoneItemType.MUD_BRICK_DRYING), stone.items.getItem(EnumStoneItemType.MUD))
                .dry()
                .notWet()
                .hours(20)
                .build());
            BidsRegistry.DRYING_SURFACE_RECIPES.register((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .consumes(stone.items.getItem(EnumStoneItemType.MUD_BRICK_DRYING))
                .produces(stone.items.getItem(EnumStoneItemType.MUD_BRICK), stone.items.getItem(EnumStoneItemType.MUD))
                .dry()
                .notWet()
                .hours(10)
                .build());
        }
    }

    private static void registerCookingRecipes() {
        Bids.LOG.info("Register cooking recipes");

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 500), new ItemStack(TFCItems.powder, 1, 9))
            .produces(new FluidStack(TFCFluids.SALTWATER, 500))
            .inTime(20)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.SALTWATER, 500))
            .produces(new ItemStack(TFCItems.powder, 1, 9))
            .withHeat()
            .withoutLid()
            .inTime(750)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new ItemStack(Items.snowball))
            .produces(new FluidStack(TFCFluids.FRESHWATER, 200))
            .withHeat()
            .inTime(200)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new ItemStack(Items.snowball))
            .produces(new FluidStack(TFCFluids.FRESHWATER, 200))
            .withoutHeat()
            .inTime(1000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 200), new ItemStack(TFCItems.powder, 1, 13))
            .produces(new FluidStack(BidsFluids.WEAKWOODASHLYE, 200))
            .inFixedTime(20000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.WEAKWOODASHLYE, 2))
            .produces(new FluidStack(BidsFluids.WOODASHLYE, 1))
            .withHeat()
            .withoutLid()
            .inTime(1000 / 500f)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 1), new FluidStack(BidsFluids.WOODASHLYE, 1))
            .produces(new FluidStack(BidsFluids.WEAKWOODASHLYE, 2))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.TALLOW, 5), new FluidStack(BidsFluids.WOODASHLYE, 4))
            .produces(new FluidStack(BidsFluids.TALLOWWOODASHLYE, 9))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.TALLOWWOODASHLYE, 1))
            .produces(new FluidStack(BidsFluids.SOAP, 1))
            .withHeat(EnumCookingHeatLevel.LOW)
            .inFixedTime(2000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.OLIVEOIL, 4), new FluidStack(BidsFluids.WEAKWOODASHLYE, 1))
            .produces(new FluidStack(BidsFluids.OLIVEOILWEAKWOODASHLYE, 5))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.OLIVEOILWEAKWOODASHLYE, 1))
            .produces(new FluidStack(BidsFluids.UNCUREDSOAP, 1))
            .withHeat(EnumCookingHeatLevel.LOW)
            .inFixedTime(3000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.FISHOIL, 4), new FluidStack(BidsFluids.WEAKWOODASHLYE, 1))
            .produces(new FluidStack(BidsFluids.FISHOILWEAKWOODASHLYE, 5))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.FISHOILWEAKWOODASHLYE, 1))
            .produces(new FluidStack(BidsFluids.UNCUREDSOAP, 1))
            .withHeat(EnumCookingHeatLevel.LOW)
            .inFixedTime(3000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.FLAXSEEDOIL, 4), new FluidStack(BidsFluids.WEAKWOODASHLYE, 1))
            .produces(new FluidStack(BidsFluids.FLAXSEEDOILWEAKWOODASHLYE, 5))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.FLAXSEEDOILWEAKWOODASHLYE, 1))
            .produces(new FluidStack(BidsFluids.UNCUREDSOAP, 1))
            .withHeat(EnumCookingHeatLevel.LOW)
            .inFixedTime(3000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.SOAP, 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.soap), Global.FOOD_MAX_WEIGHT / 10000))
            .withoutHeat()
            .inTime(1000 / 5000f)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.UNCUREDSOAP, 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.uncuredSoap), Global.FOOD_MAX_WEIGHT / 10000))
            .withoutHeat()
            .inTime(500 / 5000f)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 1), ItemFoodTFC.createTag(new ItemStack(BidsItems.soap), 1f / 500))
            .produces(new FluidStack(BidsFluids.SOAPYWATER, 1))
            .inTime(20 / 1000f)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 1), ItemFoodTFC.createTag(new ItemStack(BidsItems.uncuredSoap), 1f / 250))
            .produces(new FluidStack(BidsFluids.SOAPYWATER, 1))
            .inTime(20 / 1000f)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.SOAPYWATER, 100), new ItemStack(TFCItems.wool))
            .produces(new ItemStack(BidsItems.woolWashed, 1))
            .withHeat()
            .inTime(50)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new ItemStack(TFCItems.resin))
            .produces(new FluidStack(TFCFluids.PITCH, 50))
            .withHeat()
            .inTime(50)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new ItemStack(TFCItems.emptyHoneycomb))
            .produces(new FluidStack(TFCFluids.WAX, 300))
            .withHeat()
            .inTime(750)
            .build());

        for (Item stringItem : new Item[]{TFCItems.silkString, TFCItems.woolYarn, TFCItems.linenString, TFCItems.cottonYarn, BidsItems.juteTwine, BidsItems.sisalTwine}) {
            BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
                .consumes(new FluidStack(TFCFluids.WAX, 200), new ItemStack(stringItem))
                .produces(new ItemStack(TFCBlocks.candleOff, 1))
                .withHeat()
                .build());

            BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.TALLOW, 200), new ItemStack(stringItem))
                .produces(new ItemStack(TFCBlocks.candleOff, 1))
                .build());
        }

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.PITCH, 50), new ItemStack(TFCItems.stick))
            .produces(new ItemStack(TFCBlocks.torchOff, 1))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.PITCH, 250), new ItemStack(TFCItems.leatherBag))
            .produces(new ItemStack(TFCItems.pitchBag, 1))
            .inTime(100)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.FRESHWATER, 9), new FluidStack(TFCFluids.HONEY, 1))
            .produces(new FluidStack(TFCFluids.HONEYWATER, 10))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.SALTWATER, 9), new FluidStack(TFCFluids.VINEGAR, 1))
            .produces(new FluidStack(TFCFluids.BRINE, 10))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.MILK, 9), new FluidStack(TFCFluids.VINEGAR, 1))
            .produces(new FluidStack(TFCFluids.MILKVINEGAR, 10))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.MILKVINEGAR, 1))
            .produces(new FluidStack(TFCFluids.MILKCURDLED, 1))
            .withoutHeat()
            .withLid()
            .inFixedTime(8000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingCheeseRecipe.builder()
            .allowingInfusion()
            .consumes(new FluidStack(TFCFluids.MILKCURDLED, 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(TFCItems.cheese), Global.FOOD_MAX_WEIGHT / 10000))
            .withoutHeat()
            .withLid()
            .inFixedTime(8000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.GOATMILK, 9), new FluidStack(TFCFluids.VINEGAR, 1))
            .produces(new FluidStack(BidsFluids.GOATMILKVINEGAR, 10))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.GOATMILKVINEGAR, 1))
            .produces(new FluidStack(BidsFluids.GOATMILKCURDLED, 1))
            .withoutHeat()
            .withLid()
            .inFixedTime(8000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingCheeseRecipe.builder()
            .allowingInfusion()
            .consumes(new FluidStack(BidsFluids.GOATMILKCURDLED, 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.goatCheese), Global.FOOD_MAX_WEIGHT / 10000))
            .withoutHeat()
            .withLid()
            .inFixedTime(8000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.SKIMMEDMILK, 9), new FluidStack(TFCFluids.VINEGAR, 1))
            .produces(new FluidStack(BidsFluids.SKIMMEDMILKVINEGAR, 10))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.SKIMMEDMILKVINEGAR, 1))
            .produces(new FluidStack(BidsFluids.SKIMMEDMILKCURDLED, 1))
            .withoutHeat()
            .withLid()
            .inFixedTime(8000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingCheeseRecipe.builder()
            .allowingInfusion()
            .consumes(new FluidStack(BidsFluids.SKIMMEDMILKCURDLED, 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.hardCheese), Global.FOOD_MAX_WEIGHT / 10000))
            .withoutHeat()
            .withLid()
            .inFixedTime(8000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.OILYFISHWATER, 1000))
            .produces(new FluidStack(TFCFluids.FRESHWATER, 950), new FluidStack(BidsFluids.FISHOIL, 50))
            .withoutHeat()
            .inFixedTime(48000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(TFCFluids.MILK, 500))
            .produces(new FluidStack(BidsFluids.SKIMMEDMILK, 450), new FluidStack(BidsFluids.CREAM, 50))
            .withoutHeat()
            .inFixedTime(24000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.GOATMILK, 500))
            .produces(new FluidStack(BidsFluids.SKIMMEDMILK, 450), new FluidStack(BidsFluids.CREAM, 50))
            .withoutHeat()
            .inFixedTime(24000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.suet), Global.FOOD_MAX_WEIGHT / 8000))
            .produces(new FluidStack(BidsFluids.TALLOW, 1))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inTime(4000 / 5000f)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(new FluidStack(BidsFluids.TALLOW, 1))
            .produces(ItemFoodTFC.createTag(new ItemStack(BidsItems.tallow), Global.FOOD_MAX_WEIGHT / 10000))
            .withoutHeat()
            .inFixedTime(1000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(ItemFoodTFC.createTag(new ItemStack(BidsItems.tallow), Global.FOOD_MAX_WEIGHT / 10000))
            .produces(new FluidStack(BidsFluids.TALLOW, 1))
            .withHeat()
            .inTime(250 / 5000f)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN, 500), new FluidStack(TFCFluids.FRESHWATER, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN_WATER, 1000))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT, 500), new FluidStack(TFCFluids.FRESHWATER, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT_WATER, 1000))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH, 500), new FluidStack(TFCFluids.FRESHWATER, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH_WATER, 1000))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE, 500), new FluidStack(TFCFluids.FRESHWATER, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE_WATER, 1000))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL, 500), new FluidStack(TFCFluids.FRESHWATER, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_WATER, 1000))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL, 500), new FluidStack(TFCFluids.MILK, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_MILK, 1000))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL, 500), new FluidStack(BidsFluids.GOATMILK, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_MILK, 1000))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL, 500), new FluidStack(BidsFluids.SKIMMEDMILK, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_MILK, 1000))
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN_STEW, 500))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT_STEW, 500))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1500)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH_STEW, 500))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1500)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE, 500))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE_STEW, 500))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN_WATER, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.BEAN_SOUP, 1000))
            .withHeat(EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.MEDIUM)
            .withLid()
            .inFixedTime(1500)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT_WATER, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.MEAT_SOUP, 1000))
            .withHeat(EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.MEDIUM)
            .withLid()
            .inFixedTime(2000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH_WATER, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.FISH_SOUP, 1000))
            .withHeat(EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.MEDIUM)
            .withLid()
            .inFixedTime(2000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE_WATER, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.VEGETABLE_SOUP, 1000))
            .withHeat(EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.MEDIUM)
            .withLid()
            .inFixedTime(1500)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_WATER, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.PORRIDGE_WATER, 1000))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
            .consumes(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.CEREAL_MILK, 1000))
            .produces(CookingMixtureHelper.createCookingMixtureFluidStack(BidsCookingMixtures.PORRIDGE_MILK, 1000))
            .withHeat(EnumCookingHeatLevel.LOW)
            .withLid()
            .inFixedTime(1000)
            .build());

        BidsRegistry.COOKING_RECIPES.register(CookingRecipe.builder()
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

        Item[] breads = new Item[]{TFCItems.wheatBread, TFCItems.oatBread, TFCItems.barleyBread, TFCItems.ryeBread, TFCItems.cornBread, TFCItems.riceBread};
        for (int i = 0; i < breads.length; i++) {
            BidsRegistry.PREP_RECIPES.register(new PrepRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.sandwich, 1, i)), new PrepIngredientSpec[]{
                PrepIngredient.from(breads[i]).toSpec(2),
                foodNoGrain.toSpec(3), foodNoGrain.toSpec(2), foodNoGrain.toSpec(2), foodNoGrain.toSpec(1)
            }, 7));
        }

        BidsRegistry.PREP_RECIPES.register(new PrepSaladRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.salad)), new PrepIngredientSpec[]{
            vesselBowl.toSpec(), foodNoGrainExceptRice.toSpec(10), foodNoGrainExceptRice.toSpec(4), foodNoGrainExceptRice.toSpec(4), foodNoGrainExceptRice.toSpec(2)
        }, 14));

        Item[] peppers = new Item[]{TFCItems.greenBellPepper, TFCItems.yellowBellPepper, TFCItems.redBellPepper};
        for (int i = 0; i < peppers.length; i++) {
            BidsRegistry.PREP_RECIPES.register(new PrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.stuffedPepper, 1, i)), new PrepIngredientSpec[]{
                PrepIngredient.from(peppers[i]).toSpec(3),
                foodNoGrainExceptRiceAndBread.toSpec(6), foodNoGrainExceptRiceAndBread.toSpec(4), foodNoGrainExceptRiceAndBread.toSpec(2), foodNoGrainExceptRiceAndBread.toSpec(1)
            }, 10));
        }

        BidsRegistry.PREP_RECIPES.register(new PrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.stuffedMushroom)), new PrepIngredientSpec[]{
            PrepIngredient.from(TFCItems.mushroomFoodB).toSpec(2),
            foodNoGrainExceptRiceAndBread.toSpec(3), foodNoGrainExceptRiceAndBread.toSpec(2), foodNoGrainExceptRiceAndBread.toSpec(2), foodNoGrainExceptRiceAndBread.toSpec(1)
        }, 7));

        Item[] flatbread = new Item[]{BidsItems.wheatFlatbread, BidsItems.oatFlatbread, BidsItems.barleyFlatbread, BidsItems.ryeFlatbread, BidsItems.cornmealFlatbread, BidsItems.riceFlatbread};
        for (int i = 0; i < breads.length; i++) {
            BidsRegistry.PREP_RECIPES.register(new PrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.wrap, 1, i)), new PrepIngredientSpec[]{
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

        BidsRegistry.PREP_RECIPES.register(new PrepRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.pemmican)), new PrepIngredientSpec[]{
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

        BidsRegistry.PREP_RECIPES.register(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.BEAN), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            beans.toSpec(20, true), foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)
        }));

        BidsRegistry.PREP_RECIPES.register(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.MEAT), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            meatNoFish.toSpec(20, true), foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)
        }));

        BidsRegistry.PREP_RECIPES.register(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.FISH), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            meatFish.toSpec(20, true), foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)
        }));

        BidsRegistry.PREP_RECIPES.register(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.VEGETABLE), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            vegetable.toSpec(20, true), foodNoFruitNoBread.toSpec(8, true), foodNoFruitNoBread.toSpec(8), foodNoFruitNoBread.toSpec(4)
        }));

        BidsRegistry.PREP_RECIPES.register(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.CEREAL), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            grainPorridge.toSpec(20, true), foodNoDairyNoGrain.toSpec(8, true), foodNoDairyNoGrain.toSpec(8), foodNoDairyNoGrain.toSpec(4)
        }));

        BidsRegistry.PREP_RECIPES.register(new PrepRecipe(CookingMixtureHelper.createCookingMixtureItemStack(BidsCookingMixtures.EGG), new PrepIngredientSpec[]{
            vesselLargeBowl.toSpec(),
            foodEgg.toSpec(20, true), foodNoFruitNoGrain.toSpec(8, true), foodNoFruitNoGrain.toSpec(8), foodNoFruitNoGrain.toSpec(4)
        }));
    }

    private static void registerChurningRecipes() {
        Bids.LOG.info("Register churning recipes");

        BidsRegistry.CHURNING_RECIPES.register(new ChurningRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.butter), Global.FOOD_MAX_WEIGHT / 4000),
            new FluidStack(BidsFluids.CREAM, 1), 1));
    }

    private static void registerProcessingSurfaceRecipes() {
        Bids.LOG.info("Register processing surface recipes");

        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.scrapedHide, 1, 0),
            new ItemStack(TFCItems.soakedHide, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.scrapedHide, 1, 1),
            new ItemStack(TFCItems.soakedHide, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.scrapedHide, 1, 2),
            new ItemStack(TFCItems.soakedHide, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));

        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.fur, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.fur, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.fur, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.furScrap, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.furScrap, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.furScrap, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));

        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.wolfFur, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.wolfFur, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.wolfFur, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.wolfFurScrap, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.wolfFurScrap, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.wolfFurScrap, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));

        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.bearFur, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.bearFur, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.bearFur, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.bearFurScrap, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.bearFurScrap, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.bearFurScrap, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));

        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 0),
            new ItemStack(TFCItems.sheepSkin, 1, 0),
            "itemScrapingTool", "blockScrapingSurface", 1));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 1),
            new ItemStack(TFCItems.sheepSkin, 1, 1),
            "itemScrapingTool", "blockScrapingSurface", 2));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.hide, 1, 2),
            new ItemStack(TFCItems.sheepSkin, 1, 2),
            "itemScrapingTool", "blockScrapingSurface", 4));

        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(BidsItems.flaxStalkBroken),
            new ItemStack(BidsItems.flaxStalkDried),
            "itemFlaxBreakingTool", "blockFlaxWorkingSurface", 0.25f));
        BidsRegistry.PROCESSING_SURFACE_RECIPES.register(new ProcessingSurfaceRecipe(new ItemStack(BidsItems.flaxFiberCoarse),
            new ItemStack(BidsItems.flaxStalkBroken),
            "itemFlaxScutchingTool", "blockFlaxWorkingSurface", 0.25f));
    }

    private static void registerSoakingSurfaceRecipes() {
        Bids.LOG.info("Register soaking surface recipes");

        BidsRegistry.SOAKING_SURFACE_RECIPES.register(new SoakingSurfaceRecipe(new ItemStack(BidsItems.flaxStalkRetted, 1, 0),
            new ItemStack(BidsItems.flaxStalk, 1, 0), "blockFreshWater", 20));

        BidsRegistry.SOAKING_SURFACE_RECIPES.register(new SoakingSurfaceRecipe(new ItemStack(BidsItems.juteStalkRetted, 1, 0),
            new ItemStack(BidsItems.juteStalk, 1, 0), "blockFreshWater", 20));

        BidsRegistry.SOAKING_SURFACE_RECIPES.register(new SoakingSurfaceRecipe(new ItemStack(BidsItems.sisalFiberRinsed, 1, 0),
            new ItemStack(TFCItems.sisalFiber, 1, 0), "blockFreshWater", 0));

        // Washing wool can be skipped however the wool needs to be rinsed for a extended period of time
        BidsRegistry.SOAKING_SURFACE_RECIPES.register(new SoakingSurfaceRecipe(new ItemStack(BidsItems.woolRinsed, 1, 0),
            new ItemStack(TFCItems.wool, 1, 0), "blockFreshWater", 20));

        BidsRegistry.SOAKING_SURFACE_RECIPES.register(new SoakingSurfaceRecipe(new ItemStack(BidsItems.woolRinsed, 1, 0),
            new ItemStack(BidsItems.woolWashed, 1, 0), "blockFreshWater", 0));
    }

    private static void registerHandworkRecipes() {
        Bids.LOG.info("Register handwork recipes");

        BidsRegistry.HANDWORK_RECIPES.register(new HandworkRecipe(new ItemStack(BidsItems.barkFibreSmooth), new ItemStack(BidsItems.barkFibreCoarse), 80));
        BidsRegistry.HANDWORK_RECIPES.register(new HandworkRecipe(new ItemStack(TFCItems.juteFiber), new ItemStack(BidsItems.juteStalkRetted), 80));
        BidsRegistry.HANDWORK_RECIPES.register(new HandworkRecipe(new ItemStack(BidsItems.flaxStalkBroken), new ItemStack(BidsItems.flaxStalkDried), 240));
        BidsRegistry.HANDWORK_RECIPES.register(new HandworkRecipe(new ItemStack(BidsItems.flaxFiberCoarse), new ItemStack(BidsItems.flaxStalkBroken), 240));
        BidsRegistry.HANDWORK_RECIPES.register(new HandworkRecipe(new ItemStack(BidsItems.cottonFiberCoarse), new ItemStack(BidsItems.cottonBollRefined), 60));
        BidsRegistry.HANDWORK_RECIPES.register(new HandworkRecipe(new ItemStack(BidsItems.woolFiberCoarse), new ItemStack(BidsItems.woolDried), 60));

        // Flax fiber spinning recipe is preserved as a way to convert TFC+ Flax fibers to string
        // since unlike other fibers it is not used as an intermediate material in the new extended textile processing
        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(TFCItems.linenString, 4), new ItemStack(TFCItems.flaxFiber), 120));

        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(TFCItems.linenString, 4), new ItemStack(BidsItems.flaxFiberRefined), 120));
        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(TFCItems.cottonYarn, 6), new ItemStack(BidsItems.cottonFiberRefined), 120));
        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(TFCItems.woolYarn, 8), new ItemStack(BidsItems.woolFiberRefined), 120));
        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(BidsItems.barkCordage, 2), new ItemStack(BidsItems.barkFibreSmooth), 80));
        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(BidsItems.sisalTwine, 2), new ItemStack(BidsItems.sisalFiberRefined), 120));
        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(BidsItems.juteTwine, 2), new ItemStack(BidsItems.juteFiberRefined), 120));

        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(TFCItems.linenString, 4), new ItemStack(BidsItems.flaxFiberCoarse), 120 * 4));
        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(TFCItems.cottonYarn, 6), new ItemStack(BidsItems.cottonFiberCoarse), 120 * 4));
        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(TFCItems.woolYarn, 8), new ItemStack(BidsItems.woolFiberCoarse), 120 * 4));
        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(BidsItems.sisalTwine, 2), new ItemStack(BidsItems.sisalFiberCoarse), 120 * 4));
        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(BidsItems.juteTwine, 2), new ItemStack(BidsItems.juteFiberCoarse), 120 * 4));
        BidsRegistry.SPINNING_RECIPES.register(new SpinningRecipe(new ItemStack(BidsItems.barkCordage, 2), new ItemStack(BidsItems.barkFibreCoarse), 80 * 4));

        BidsRegistry.ROPEMAKING_RECIPES.register(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(TFCItems.linenString, 16), 600));
        BidsRegistry.ROPEMAKING_RECIPES.register(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(BidsItems.barkCordage, 12), 600));
        BidsRegistry.ROPEMAKING_RECIPES.register(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(BidsItems.sisalTwine, 8), 600));
        BidsRegistry.ROPEMAKING_RECIPES.register(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(BidsItems.juteTwine, 12), 600));

        BidsRegistry.CARDING_RECIPES.register(new CardingRecipe(new ItemStack(BidsItems.sisalFiberRefined), new ItemStack(BidsItems.sisalFiberCoarse), 80));
        BidsRegistry.CARDING_RECIPES.register(new CardingRecipe(new ItemStack(BidsItems.cottonFiberRefined), new ItemStack(BidsItems.cottonFiberCoarse), 80));
        BidsRegistry.CARDING_RECIPES.register(new CardingRecipe(new ItemStack(BidsItems.woolFiberRefined), new ItemStack(BidsItems.woolFiberCoarse), 80));

        BidsRegistry.HECKLING_RECIPES.register(new HecklingRecipe(new ItemStack(BidsItems.juteFiberRefined), new ItemStack(BidsItems.juteFiberCoarse), 120));
        BidsRegistry.HECKLING_RECIPES.register(new HecklingRecipe(new ItemStack(BidsItems.flaxFiberRefined), new ItemStack(BidsItems.flaxFiberCoarse), 120));
    }

    private static void registerKnappingRecipes() {
        Bids.LOG.info("Register TFC knapping recipes");

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
//            CraftingManagerTFC.getInstance().addRecipe(stone.items.getItem(EnumStoneItemType.DRILL_HEAD),
//                new Object[]{"     ", " ### ", "#####", " ### ", "  #  ",
//                    '#', stone.items.getItem(EnumStoneItemType.FLAT_ROCK)});
//            CraftingManagerTFC.getInstance().addRecipe(stone.items.getItem(EnumStoneItemType.ADZE_HEAD),
//                new Object[]{"#####", "#  ##", "#    ", "     ", "     ",
//                    '#', stone.items.getItem(EnumStoneItemType.FLAT_ROCK)});
//            CraftingManagerTFC.getInstance().addRecipe(stone.items.getItem(EnumStoneItemType.HAND_AXE),
//                new Object[]{"  #  ", " ### ", " ### ", "#####", " ### ",
//                    '#', stone.items.getItem(EnumStoneItemType.FLAT_ROCK)});
        }

//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.clayMoldAdze, 1),
//            new Object[]{"     ", "#####", "#  ##", "#    ", "     ",
//                '#', new ItemStack(TFCItems.flatClay, 1, 1)});
//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.clayMoldDrill, 1),
//            new Object[]{"  #  ", "  #  ", "  #  ", " ### ", "  #  ",
//                '#', new ItemStack(TFCItems.flatClay, 1, 1)});

//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.clayMug, 2),
//            new Object[]{"#####", "#####", "    #", "   # ", "    #", '#',
//                new ItemStack(TFCItems.flatClay, 1, 1)});

//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.largeClayBowl, 1),
//            new Object[]{"#####", " ### ", " ### ", "#   #", "#####", '#',
//                new ItemStack(TFCItems.flatClay, 1, 1)});

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.cookingPot, 1, 0),
            new Object[]{" ### ", " ### ", " ### ", " ### ", "#   #", '#',
                new ItemStack(TFCItems.flatClay, 1, 1)});
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.cookingPotLid, 1, 0),
            new Object[]{"## ##", "     ", "#####", "#####", "#####", '#',
                new ItemStack(TFCItems.flatClay, 1, 1)});

//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(TFCItems.glassBottle, 1),
//            new Object[]{" # # ", " # # ", "#   #", "#   #", " ### ", '#',
//                new ItemStack(BidsItems.flatGlass, 1)});
//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.drinkingGlass, 2),
//            new Object[]{"     ", "     ", "#   #", "#   #", "#####", '#',
//                new ItemStack(BidsItems.flatGlass, 1)});
//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.shotGlass, 4),
//            new Object[]{"     ", "     ", " # # ", " # # ", " ### ", '#',
//                new ItemStack(BidsItems.flatGlass, 1)});
//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.glassJug, 1),
//            new Object[]{" #   ", "# ## ", "# # #", "# ## ", "###  ", '#',
//                new ItemStack(BidsItems.flatGlass, 1)});

//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkStrap, 3),
//            new Object[]{"# # #", "# # #", "# # #", "# # #", "# # #", '#', BidsItems.flatBirchBark});
//
//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkBagPiece, 2, 0),
//            new Object[]{" ### ", " ### ", "     ", " ### ", " ### ", '#', BidsItems.flatBirchBark});
//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkBagPiece, 2, 0),
//            new Object[]{"     ", "## ##", "## ##", "## ##", "     ", '#', BidsItems.flatBirchBark});
//
//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkRepairPatch, 4, 0),
//            new Object[]{"## ##", "## ##", "     ", "## ##", "## ##", '#', BidsItems.flatBirchBark});
//
//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkCupPiece, 1),
//            new Object[]{"     ", "     ", "#### ", "### #", "#### ", '#',
//                new ItemStack(BidsItems.flatBirchBark, 1)});

//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.birchBarkShoes, 1),
//            new Object[]{"  ###", "   ##", "     ", "##   ", "###  ", '#',
//                new ItemStack(BidsItems.flatBirchBark, 1)});

//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.coatBodyFrontLeather, 1, 0),
//            new Object[]{"#   #", "## ##", "## ##", "## ##", "## ##", '#', TFCItems.flatLeather});
//        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.coatBodyBackLeather, 1, 0),
//            new Object[]{"## ##", "#####", "#####", "#####", "#####", '#', TFCItems.flatLeather});
//
        for (Item flatItem : new Item[]{TFCItems.flatLinen, TFCItems.flatWool, TFCItems.flatSilk, TFCItems.flatCotton, TFCItems.flatBurlap}) {
            CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsItems.steamingMeshCloth, 1),
                new Object[]{"#####", "# # #", "#####", "# # #", "#####", '#', flatItem});
        }

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.strawNest, 1),
            new Object[]{"     ", "#   #", "#   #", " ### ", "     ", '#',
                new ItemStack(TFCItems.flatStraw, 1)});

//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatDoughHardtack, 1), 160),
//            new Object[]{"#####", "# # #", "#####", "# # #", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 0)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyDoughHardtack, 1), 160),
//            new Object[]{"#####", "# # #", "#####", "# # #", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 1)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.oatDoughHardtack, 1), 160),
//            new Object[]{"#####", "# # #", "#####", "# # #", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 2)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.riceDoughHardtack, 1), 160),
//            new Object[]{"#####", "# # #", "#####", "# # #", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 3)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.ryeDoughHardtack, 1), 160),
//            new Object[]{"#####", "# # #", "#####", "# # #", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 4)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealDoughHardtack, 1), 160),
//            new Object[]{"#####", "# # #", "#####", "# # #", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 5)});
//
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.wheatDough, 1), 160),
//            new Object[]{"     ", " ### ", "#####", "#####", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 0)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.barleyDough, 1), 160),
//            new Object[]{"     ", " ### ", "#####", "#####", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 1)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.oatDough, 1), 160),
//            new Object[]{"     ", " ### ", "#####", "#####", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 2)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.riceDough, 1), 160),
//            new Object[]{"     ", " ### ", "#####", "#####", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 3)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.ryeDough, 1), 160),
//            new Object[]{"     ", " ### ", "#####", "#####", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 4)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(TFCItems.cornmealDough, 1), 160),
//            new Object[]{"     ", " ### ", "#####", "#####", "#####", '#',
//                new ItemStack(BidsItems.flatDough, 1, 5)});
//
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.wheatDoughFlatbread, 1), 160),
//            new Object[]{" ### ", "#####", "#####", "#####", " ### ", '#',
//                new ItemStack(BidsItems.flatDough, 1, 0)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.barleyDoughFlatbread, 1), 160),
//            new Object[]{" ### ", "#####", "#####", "#####", " ### ", '#',
//                new ItemStack(BidsItems.flatDough, 1, 1)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.oatDoughFlatbread, 1), 160),
//            new Object[]{" ### ", "#####", "#####", "#####", " ### ", '#',
//                new ItemStack(BidsItems.flatDough, 1, 2)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.riceDoughFlatbread, 1), 160),
//            new Object[]{" ### ", "#####", "#####", "#####", " ### ", '#',
//                new ItemStack(BidsItems.flatDough, 1, 3)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.ryeDoughFlatbread, 1), 160),
//            new Object[]{" ### ", "#####", "#####", "#####", " ### ", '#',
//                new ItemStack(BidsItems.flatDough, 1, 4)});
//        CraftingManagerTFC.getInstance().addRecipe(ItemFoodTFC.createTag(new ItemStack(BidsItems.cornmealDoughFlatbread, 1), 160),
//            new Object[]{" ### ", "#####", "#####", "#####", " ### ", '#',
//                new ItemStack(BidsItems.flatDough, 1, 5)});

    }

    private static void registerSewingRecipes() {
        Bids.LOG.info("Register TFC sewing recipes");

//        int[][][] bagSewing = new int[][][]{{
//            {25, 21},
//            {11, 74},
//            {19, 87},
//            {79, 87},
//            {87, 74},
//            {73, 21}
//        }};
//
//        ClothingManager.getInstance().addRecipe(new SewingRecipe(
//            new SewingPattern(new ItemStack(BidsItems.birchBarkBag, 1), bagSewing, true),
//            new ItemStack[]{
//                new ItemStack(BidsItems.birchBarkBagPiece, 1, 0),
//                new ItemStack(BidsItems.birchBarkBagPiece, 1, 0),
//                new ItemStack(BidsItems.birchBarkStrap, 1, 0)
//            }));
//
//        int[][][] cupSewing = new int[][][]{{
//            {11, 74},
//            {19, 87},
//            {64, 87},
//            {72, 74},
//            {72, 37},
//            {60, 40},
//            {21, 40},
//            {11, 37}
//        }};
//
//        ClothingManager.getInstance().addRecipe(new SewingRecipe(
//            new SewingPattern(new ItemStack(BidsItems.birchBarkCupUnfinished, 1), cupSewing, true),
//            new ItemStack[]{
//                new ItemStack(BidsItems.birchBarkCupPiece, 1, 0),
//                new ItemStack(BidsItems.birchBarkStrap, 1, 0)
//            }));

//        int[][][] coatSewing = new int[][][]{
//            // the left side of the coat and underarm
//            {{24, 86}, {27, 33}, {24, 33}, {18, 71}},
//            // the outer left arm and shoulder
//            {{8, 71}, {11, 33}, {16, 19}, {22, 13}, {37, 12}},
//            // the arm attached to the sleeve
//            {{25, 33}, {21, 15}},
//            // the right side of the coat and underarm
//            {{97 - 24, 86}, {97 - 27, 33}, {97 - 24, 33}, {97 - 18, 71}},
//            // the outer right arm and shoulder
//            {{97 - 8, 71}, {97 - 11, 33}, {97 - 16, 19}, {97 - 22, 13}, {97 - 37, 12}},
//            // the right arm attached to the sleeve
//            {{97 - 25, 33}, {97 - 21, 15}}
//        };
//
//        ClothingManager.getInstance().addRecipe(new SewingRecipe(
//            new SewingPattern(new ItemStack(BidsItems.leatherCoat, 1), coatSewing, true),
//            new ItemStack[]{
//                new ItemStack(BidsItems.coatBodyFrontLeather, 1, 0),
//                new ItemStack(BidsItems.coatBodyBackLeather, 1, 0),
//                new ItemStack(TFCItems.shirtSleeves, 1, 2),
//                new ItemStack(TFCItems.shirtSleeves, 1, 2),
//            }));
    }

    private static void registerSewingRepairRecipes() {
        Bids.LOG.info("Register TFC sewing repair recipes");

//        ClothingManager.getInstance().addRecipe(new SewingRecipe(
//            new SewingPattern(new ItemStack(BidsItems.birchBarkBag, 1), true),
//            new ItemStack[]{
//                new ItemStack(BidsItems.birchBarkBag, 1, OreDictionary.WILDCARD_VALUE),
//                new ItemStack(BidsItems.birchBarkRepairPatch, 1, 0)
//            }).setRepairRecipe());
//
//        ClothingManager.getInstance().addRecipe(new SewingRecipe(
//            new SewingPattern(new ItemStack(BidsItems.birchBarkShoes, 1), true),
//            new ItemStack[]{
//                new ItemStack(BidsItems.birchBarkShoes, 1, OreDictionary.WILDCARD_VALUE),
//                new ItemStack(BidsItems.birchBarkStrap, 1, 0)
//            }).setRepairRecipe());

//        ClothingManager.getInstance().addRecipe(new SewingRecipe(
//            new SewingPattern(new ItemStack(BidsItems.leatherCoat, 1), true),
//            new ItemStack[]{
//                new ItemStack(BidsItems.leatherCoat, 1, OreDictionary.WILDCARD_VALUE),
//                new ItemStack(TFCItems.repairPatch, 1, 2)
//            }).setRepairRecipe());

        if (BidsOptions.Crafting.craftingAddMissingLeatherRepairRecipes) {
            // Adding missing TFC+ recipe for repairing leather boots
            ClothingManager.getInstance().addRecipe(new SewingRecipe(
                new SewingPattern(new ItemStack(TFCItems.leatherBoots, 1), true),
                new ItemStack[]{
                    new ItemStack(TFCItems.leatherBoots, 1, OreDictionary.WILDCARD_VALUE),
                    new ItemStack(TFCItems.repairPatch, 1, 2)
                }).setRepairRecipe());

            // Adding missing TFC+ recipe for repairing leather cap
            ClothingManager.getInstance().addRecipe(new SewingRecipe(
                new SewingPattern(new ItemStack(TFCItems.leatherCoif, 1), true),
                new ItemStack[]{
                    new ItemStack(TFCItems.leatherCoif, 1, OreDictionary.WILDCARD_VALUE),
                    new ItemStack(TFCItems.repairPatch, 1, 2)
                }).setRepairRecipe());

            // Adding missing TFC+ recipe for repairing leather shorts
            ClothingManager.getInstance().addRecipe(new SewingRecipe(
                new SewingPattern(new ItemStack(TFCItems.leatherShorts, 1), true),
                new ItemStack[]{
                    new ItemStack(TFCItems.leatherShorts, 1, OreDictionary.WILDCARD_VALUE),
                    new ItemStack(TFCItems.repairPatch, 1, 2)
                }).setRepairRecipe());
        }
    }

    private static void registerKilnRecipes() {
        Bids.LOG.info("Register TFC kiln recipes");

//        KilnCraftingManager.getInstance().addRecipe(
//            new KilnRecipe(new ItemStack(BidsItems.clayPipe, 1, 0), 0,
//                new ItemStack(BidsItems.clayPipe, 1, 1)));

//        KilnCraftingManager.getInstance().addRecipe(
//            new KilnRecipe(new ItemStack(BidsItems.clayMug, 1, 0), 0,
//                new ItemStack(BidsItems.clayMug, 1, 1)));

//        KilnCraftingManager.getInstance().addRecipe(
//            new KilnRecipe(new ItemStack(BidsBlocks.clayCrucible, 1, 1), 0,
//                new ItemStack(BidsBlocks.clayCrucible, 1, 0)));

//        KilnCraftingManager.getInstance().addRecipe(
//            new KilnRecipe(new ItemStack(BidsItems.largeClayBowl, 1, 0), 0,
//                new ItemStack(BidsItems.largeClayBowl, 1, 1)));

        KilnCraftingManager.getInstance().addRecipe(
            new KilnRecipe(new ItemStack(BidsBlocks.cookingPot, 1, 0), 0,
                new ItemStack(BidsBlocks.cookingPot, 1, 1)));
        KilnCraftingManager.getInstance().addRecipe(
            new KilnRecipe(new ItemStack(BidsBlocks.cookingPotLid, 1, 0), 0,
                new ItemStack(BidsBlocks.cookingPotLid, 1, 1)));

        KilnCraftingManager.getInstance().addRecipe(
            new KilnRecipe(new ItemStack(BidsItems.clayMoldAdze, 1, 0), 0,
                new ItemStack(BidsItems.clayMoldAdze, 1, 1)));
//        KilnCraftingManager.getInstance().addRecipe(
//            new KilnRecipe(new ItemStack(BidsItems.clayMoldDrill, 1, 0), 0,
//                new ItemStack(BidsItems.clayMoldDrill, 1, 1)));
    }

    private static void registerBarrelRecipes() {
        Bids.LOG.info("Register TFC barrel recipes");

//        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
//            // Extracting tannin from bark
//            if (wood.hasBarkTannin) {
//                BarrelRecipeManager.addRecipe(BarrelRecipeBuilder.asItemDemanding()
//                    .consumes(wood.items.getBark(), new FluidStack(TFCFluids.FRESHWATER, 625))
//                    .produces(new FluidStack(TFCFluids.TANNIN, 500))
//                    .withMinTechLevel(0)
//                );
//            }
//        }
//
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
//            AnvilManager.getInstance().addPlan("blowpipe", new PlanRecipe(new RuleEnum[]{
//                RuleEnum.BENDLAST, RuleEnum.BENDSECONDFROMLAST, RuleEnum.ANY}));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronSheet), null,
//                "blowpipe", AnvilReq.WROUGHTIRON, new ItemStack(BidsItems.metalBlowpipe, 1, 1)));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.brassSheet), null,
//                "blowpipe", AnvilReq.BRONZE, new ItemStack(BidsItems.brassBlowpipe, 1, 1)));

//            Bids.LOG.info("Registering adze anvil plan and recipes");
//            AnvilManager.getInstance().addPlan("adze", new PlanRecipe(new RuleEnum[]{RuleEnum.PUNCHLAST, RuleEnum.PUNCHSECONDFROMLAST, RuleEnum.HITTHIRDFROMLAST}));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null,
//                "adze", AnvilReq.COPPER, new ItemStack(BidsItems.copperAdzeHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot), null,
//                "adze", AnvilReq.BRONZE, new ItemStack(BidsItems.bronzeAdzeHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot), null,
//                "adze", AnvilReq.BISMUTHBRONZE, new ItemStack(BidsItems.bismuthBronzeAdzeHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot), null,
//                "adze", AnvilReq.BLACKBRONZE, new ItemStack(BidsItems.blackBronzeAdzeHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null,
//                "adze", AnvilReq.WROUGHTIRON, new ItemStack(BidsItems.wroughtIronAdzeHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
//
//            Bids.LOG.info("Registering drill anvil plan and recipes");
//            AnvilManager.getInstance().addPlan("drill", new PlanRecipe(new RuleEnum[]{RuleEnum.HITLAST, RuleEnum.PUNCHNOTLAST, RuleEnum.DRAWNOTLAST}));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null,
//                "drill", AnvilReq.COPPER, new ItemStack(BidsItems.copperDrillHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot), null,
//                "drill", AnvilReq.BRONZE, new ItemStack(BidsItems.bronzeDrillHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot), null,
//                "drill", AnvilReq.BISMUTHBRONZE, new ItemStack(BidsItems.bismuthBronzeDrillHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot), null,
//                "drill", AnvilReq.BLACKBRONZE, new ItemStack(BidsItems.blackBronzeDrillHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null,
//                "drill", AnvilReq.WROUGHTIRON, new ItemStack(BidsItems.wroughtIronDrillHead, 1)).addRecipeSkill(Global.SKILL_TOOLSMITH));

//            Bids.LOG.info("Registering plug and feather anvil plan and recipes");
//            AnvilManager.getInstance().addPlan("plugandfeather", new PlanRecipe(new RuleEnum[]{RuleEnum.HITLAST, RuleEnum.BENDSECONDFROMLAST, RuleEnum.SHRINKTHIRDFROMLAST}));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null,
//                "plugandfeather", AnvilReq.COPPER, new ItemStack(BidsItems.plugAndFeather, 8, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot), null,
//                "plugandfeather", AnvilReq.BRONZE, new ItemStack(BidsItems.plugAndFeather, 8, 2)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot), null,
//                "plugandfeather", AnvilReq.BISMUTHBRONZE, new ItemStack(BidsItems.plugAndFeather, 8, 3)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot), null,
//                "plugandfeather", AnvilReq.BLACKBRONZE, new ItemStack(BidsItems.plugAndFeather, 8, 4)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
//            AnvilManager.getInstance().addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null,
//                "plugandfeather", AnvilReq.WROUGHTIRON, new ItemStack(BidsItems.plugAndFeather, 8, 5)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));

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
