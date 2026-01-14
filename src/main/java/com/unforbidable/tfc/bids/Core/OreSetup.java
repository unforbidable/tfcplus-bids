package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.Core.Recipes;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Stone.EnumStoneBlockType;
import com.unforbidable.tfc.bids.Core.Stone.StoneIndex;
import com.unforbidable.tfc.bids.Core.Stone.StoneScheme;
import com.unforbidable.tfc.bids.Core.Wood.WoodIndex;
import com.unforbidable.tfc.bids.Core.Wood.WoodScheme;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreSetup {

    public static void preInit() {
        registerOre();
    }

    private static void registerOre() {
        Bids.LOG.info("Register item ores");

        final int WILD = OreDictionary.WILDCARD_VALUE;

        final Item[] drillHeads = new Item[]{BidsItems.sedStoneDrillHead, BidsItems.mMStoneDrillHead, BidsItems.igInStoneDrillHead, BidsItems.igExStoneDrillHead,
            BidsItems.copperDrillHead, BidsItems.bronzeDrillHead, BidsItems.bismuthBronzeDrillHead, BidsItems.blackBronzeDrillHead, BidsItems.wroughtIronDrillHead};
        for (Item drillHead : drillHeads) {
            OreDictionary.registerOre("itemDrillHead", new ItemStack(drillHead, 1, WILD));
        }

        final Item[] drills = new Item[]{BidsItems.sedStoneDrill, BidsItems.mMStoneDrill, BidsItems.igInStoneDrill, BidsItems.igExStoneDrill,
            BidsItems.copperDrill, BidsItems.bronzeDrill, BidsItems.bismuthBronzeDrill, BidsItems.blackBronzeDrill, BidsItems.wroughtIronDrill};
        for (int i = 0; i < drills.length; i++) {
            OreDictionary.registerOre("itemDrill", new ItemStack(drills[i], 1, WILD));

            if (i < 4) {
                OreDictionary.registerOre("itemDrillStone", new ItemStack(drills[i], 1, WILD));
            } else {
                OreDictionary.registerOre("itemDrillMetal", new ItemStack(drills[i], 1, WILD));
            }
        }

        final Item[] adzes = new Item[]{BidsItems.sedStoneAdze, BidsItems.mMStoneAdze, BidsItems.igInStoneAdze, BidsItems.igExStoneAdze,
            BidsItems.copperAdze, BidsItems.bronzeAdze, BidsItems.bismuthBronzeAdze, BidsItems.blackBronzeAdze, BidsItems.wroughtIronAdze};
        for (int i = 0; i < adzes.length; i++) {
            OreDictionary.registerOre("itemAdze", new ItemStack(adzes[i], 1, WILD));

            if (i < 4) {
                OreDictionary.registerOre("itemAdzeStone", new ItemStack(adzes[i], 1, WILD));
            } else {
                OreDictionary.registerOre("itemAdzeMetal", new ItemStack(adzes[i], 1, WILD));
            }
        }

        final Item[] handAxes = new Item[] { BidsItems.sedHandAxe, BidsItems.mMHandAxe, BidsItems.igInHandAxe, BidsItems.igExHandAxe };
        for (Item handAxe : handAxes) {
            OreDictionary.registerOre("itemHandAxe", new ItemStack(handAxe, 1, WILD));

            // Registering "itemKnifeStone" allow straw harvestable with a hand axe
            // but no usable in recipes where "itemKnife" is used
            OreDictionary.registerOre("itemKnifeStone", new ItemStack(handAxe, 1, WILD));

            // Registering "itemAxeStone" allow bushes (and trees) harvestable with a hand axe
            // but no usable in recipes where "itemAxe" is used
            OreDictionary.registerOre("itemAxeStone", new ItemStack(handAxe, 1, WILD));

            // Use for scrapping with speed penalty
            OreDictionary.registerOre("itemScrapingTool", new ItemStack(handAxe, 1, WILD));
            OreDictionary.registerOre("itemPrimitiveTool", new ItemStack(handAxe, 1, WILD));
        }

        for (int i = 0; i < 6; i++) {
            OreDictionary.registerOre("itemPlugAndFeather", new ItemStack(BidsItems.plugAndFeather, 1, i));
        }

        final Item[] logs = new Item[]{BidsItems.logsSeasoned, BidsItems.peeledLog, BidsItems.peeledLogSeasoned};
        for (Item log : logs) {
            OreDictionary.registerOre("itemLogExtra", new ItemStack(log, 1, WILD));
        }

        // Proper steaming mesh
        for (Item item : new Item[]{ BidsItems.steamingMeshCloth }) {
            OreDictionary.registerOre("itemCookingPotAccessory", new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
            OreDictionary.registerOre("itemCookingPotAccessorySteamingMesh", new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
        }

        // Items that are automatically consumed as a vessel from the cooking prep storage slots
        OreDictionary.registerOre("itemCookingPrepVessel",  new ItemStack(TFCItems.potteryBowl, 1, 1));
        OreDictionary.registerOre("itemCookingPrepVessel",  new ItemStack(TFCItems.potteryBowl, 1, 2));

        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsBeetroot));
        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsSugarBeet));
        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsBroadBeans));
        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsWinterBarley));
        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsWinterOat));
        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsWinterRye));
        OreDictionary.registerOre("seedCultivated", new ItemStack(BidsItems.seedsWinterWheat));

        OreDictionary.registerOre("seedWinterCereal", new ItemStack(BidsItems.seedsWinterBarley));
        OreDictionary.registerOre("seedWinterCereal", new ItemStack(BidsItems.seedsWinterOat));
        OreDictionary.registerOre("seedWinterCereal", new ItemStack(BidsItems.seedsWinterRye));
        OreDictionary.registerOre("seedWinterCereal", new ItemStack(BidsItems.seedsWinterWheat));

        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(TFCItems.clayBucketEmpty));
        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(TFCItems.woodenBucketEmpty));
        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(BidsItems.largeClayBowl, 1, 1));
        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(BidsItems.woodenPailEmpty));
        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(BidsItems.woodenPailMilk, 1, WILD));
        OreDictionary.registerOre("itemMilkingContainer", new ItemStack(BidsItems.woodenPailGoatMilk, 1, WILD));

        OreDictionary.registerOre("itemDecorativeSurface", new ItemStack(TFCItems.fur, 1, WILD));
        OreDictionary.registerOre("itemDecorativeSurface", new ItemStack(TFCItems.furScrap, 1, WILD));
        OreDictionary.registerOre("itemDecorativeSurface", new ItemStack(TFCItems.wolfFur, 1, WILD));
        OreDictionary.registerOre("itemDecorativeSurface", new ItemStack(TFCItems.wolfFurScrap, 1, WILD));
        OreDictionary.registerOre("itemDecorativeSurface", new ItemStack(TFCItems.bearFur, 1, WILD));
        OreDictionary.registerOre("itemDecorativeSurface", new ItemStack(TFCItems.bearFurScrap, 1, WILD));
        OreDictionary.registerOre("itemDecorativeSurface", new ItemStack(TFCItems.hide, 1, WILD));
        OreDictionary.registerOre("itemDecorativeSurface", new ItemStack(TFCItems.sheepSkin, 1, 0));
        OreDictionary.registerOre("itemDecorativeSurface", new ItemStack(TFCItems.sheepSkin, 1, 1));
        OreDictionary.registerOre("itemDecorativeSurface", new ItemStack(TFCItems.sheepSkin, 1, 2));

        // Hammers that are able to break iron ores into bits
        // You could realstically break iron ore with a stone hammer
        // so this is for ballance only
        // Also breaking iron ore will cause more damage to the hammer
        OreDictionary.registerOre("itemHammerIronBits", new ItemStack(TFCItems.steelHammer, 1, WILD));
        OreDictionary.registerOre("itemHammerIronBits", new ItemStack(TFCItems.blackSteelHammer, 1, WILD));
        OreDictionary.registerOre("itemHammerIronBits", new ItemStack(TFCItems.blueSteelHammer, 1, WILD));
        OreDictionary.registerOre("itemHammerIronBits", new ItemStack(TFCItems.redSteelHammer, 1, WILD));

        for (Item item : new Item[] { TFCItems.cottonYarn, TFCItems.woolYarn, TFCItems.silkString }) {
            OreDictionary.registerOre("materialBinding", item);
        }

        if (BidsOptions.Crafting.enableGrassCordageAsToolBinding) {
            OreDictionary.registerOre("materialBinding", TFCItems.grassCordage);
        }

        for (Item item : new Item[] { TFCItems.sinew }) {
            OreDictionary.registerOre("materialBinding", item);
            OreDictionary.registerOre("materialBindingDecent", item);
        }

        for (Item item : new Item[] { TFCItems.linenString, BidsItems.barkCordage, BidsItems.sisalTwine, BidsItems.juteTwine }) {
            OreDictionary.registerOre("materialBinding", item);
            OreDictionary.registerOre("materialBindingDecent", item);
            OreDictionary.registerOre("materialBindingStrong", item);
            OreDictionary.registerOre("materialBowstring", item);
        }

        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.blocks.hasChoppingBlock()) {
                OreDictionary.registerOre("blockChoppingBlock", wood.blocks.getChoppingBlock());
            }

            if (wood.blocks.hasLogWall()) {
                OreDictionary.registerOre("blockLogWall", wood.blocks.getLogWall());
                OreDictionary.registerOre("blockLogWall", wood.blocks.getLogWallVert());
            }

            if (wood.blocks.hasThickLog()) {
                OreDictionary.registerOre("blockScrapingSurface", wood.blocks.getThickLog());
                OreDictionary.registerOre("blockScrapingSurface", wood.blocks.getThickLogAlt());

                OreDictionary.registerOre("blockFlaxWorkingSurface", wood.blocks.getThickVert());
            }

            if (wood.blocks.hasStackedLogs()) {
                OreDictionary.registerOre("blockScrapingSurface", wood.blocks.getStackedLogs());
                OreDictionary.registerOre("blockScrapingSurface", wood.blocks.getStackedLogsAlt());
            }
        }

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            OreDictionary.registerOre("stoneRough", stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE));
            OreDictionary.registerOre("stoneRoughBricks",  stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_BRICKS));
            OreDictionary.registerOre("stoneRoughTiles",  stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_TILES));

            OreDictionary.registerOre("blockScrapingSurface", stone.blocks.getBlockStack(EnumStoneBlockType.RAW));
        }

        OreDictionary.registerOre("blockFreshWater", TFCBlocks.freshWater);
        OreDictionary.registerOre("blockFreshWater", TFCBlocks.freshWaterStationary);

        OreDictionary.registerOre("itemHoneycomb", TFCItems.honeycomb);
        OreDictionary.registerOre("itemHoneycomb", TFCItems.fertileHoneycomb);

        // Used for mixing flour and water into unshaped dough or flatbread dough
        OreDictionary.registerOre("itemLargeBowlWater", BidsItems.freshWaterLargeBowl);

        OreDictionary.registerOre("itemNeedleAndThread", new ItemStack(TFCItems.boneNeedleStrung, 1, WILD));
        OreDictionary.registerOre("itemNeedleAndThread", new ItemStack(TFCItems.ironNeedleStrung, 1, WILD));

        OreDictionary.registerOre("logWood", new ItemStack(BidsItems.peeledLog, 1, WILD));
        OreDictionary.registerOre("logWood", new ItemStack(BidsItems.peeledLogSeasoned, 1, WILD));
        OreDictionary.registerOre("logWood", new ItemStack(BidsItems.logsSeasoned, 1, WILD));
        OreDictionary.registerOre("logWoodPeeledSeasoned", new ItemStack(BidsItems.peeledLogSeasoned, 1, WILD));

        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.items.hasLog()) {
                OreDictionary.registerOre("logWoodAny", wood.items.getLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWood"), wood.items.getLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWoodFresh"), wood.items.getLog());
            }

            if (wood.items.hasChoppedLog()) {
                OreDictionary.registerOre("logWoodAny", wood.items.getChoppedLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWood"), wood.items.getChoppedLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWoodFresh"), wood.items.getChoppedLog());
            }

            if (wood.items.hasSeasonedLog()) {
                OreDictionary.registerOre("logWoodAny", wood.items.getSeasonedLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWood"), wood.items.getSeasonedLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWoodSeasoned"), wood.items.getSeasonedLog());
            }

            if (wood.items.hasSeasonedChoppedLog()) {
                OreDictionary.registerOre("logWoodAny", wood.items.getSeasonedChoppedLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWood"), wood.items.getSeasonedChoppedLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWoodSeasoned"), wood.items.getSeasonedChoppedLog());
            }

            if (wood.items.hasPeeledLog()) {
                OreDictionary.registerOre("logWoodAny", wood.items.getPeeledLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWood"), wood.items.getPeeledLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWoodFresh"), wood.items.getPeeledLog());
            }

            if (wood.items.hasSeasonedPeeledLog()) {
                OreDictionary.registerOre("logWoodAny", wood.items.getSeasonedPeeledLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWood"), wood.items.getSeasonedPeeledLog());
                OreDictionary.registerOre(wood.getOreWithSuffix("logWoodSeasoned"), wood.items.getSeasonedPeeledLog());

                if (wood.hardwood) {
                    OreDictionary.registerOre("logWoodPlugAndFeather", wood.items.getSeasonedPeeledLog());
                }
            }

            if (wood.hasBarkFibers) {
                OreDictionary.registerOre("itemBarkHasFibers", wood.items.getBark());
            }

            if (wood.items.hasBoard()) {
                OreDictionary.registerOre("woodBoard", wood.items.getBoard());
            }

            if (wood.items.hasShaft()) {
                OreDictionary.registerOre("woodShaft", wood.items.getShaft());
            }
        }

        OreDictionary.registerOre("supportWood", new ItemStack(TFCBlocks.woodSupportH, 1, WILD));
        OreDictionary.registerOre("supportWood", new ItemStack(TFCBlocks.woodSupportH2, 1, WILD));
        OreDictionary.registerOre("supportWood", new ItemStack(TFCBlocks.woodSupportH3, 1, WILD));
        OreDictionary.registerOre("supportWood", new ItemStack(TFCBlocks.woodSupportV, 1, WILD));
        OreDictionary.registerOre("supportWood", new ItemStack(TFCBlocks.woodSupportV2, 1, WILD));
        OreDictionary.registerOre("supportWood", new ItemStack(TFCBlocks.woodSupportV3, 1, WILD));

        OreDictionary.registerOre("plateToolMetal", new ItemStack(TFCItems.copperSheet));
        OreDictionary.registerOre("plateToolMetal", new ItemStack(TFCItems.bronzeSheet));
        OreDictionary.registerOre("plateToolMetal", new ItemStack(TFCItems.bismuthBronzeSheet));
        OreDictionary.registerOre("plateToolMetal", new ItemStack(TFCItems.blackBronzeSheet));
        OreDictionary.registerOre("plateToolMetal", new ItemStack(TFCItems.wroughtIronSheet));
        OreDictionary.registerOre("plateToolMetal", new ItemStack(TFCItems.steelSheet));
        OreDictionary.registerOre("plateToolMetal", new ItemStack(TFCItems.blackSteelSheet));
        OreDictionary.registerOre("plateToolMetal", new ItemStack(TFCItems.redSteelSheet));
        OreDictionary.registerOre("plateToolMetal", new ItemStack(TFCItems.blueSteelSheet));

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            if (stone.quern) {
                OreDictionary.registerOre("stoneQuern", stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE));
            }
        }

        for (Item knife : Recipes.knives) {
            OreDictionary.registerOre("itemScrapingTool", new ItemStack(knife, 1, WILD));
        }

        for (Item item : new Item[] { TFCItems.pole, TFCItems.stick } ) {
            OreDictionary.registerOre("itemFlaxBreakingTool", new ItemStack(item, 1, WILD));
            OreDictionary.registerOre("itemFlaxScutchingTool", new ItemStack(item, 1, WILD));
            OreDictionary.registerOre("itemPrimitiveTool", new ItemStack(item, 1, WILD));
        }

        OreDictionary.registerOre("itemFlaxBreakingTool", new ItemStack(BidsItems.woodenMallet, 1, WILD));

        OreDictionary.registerOre("itemFlaxScutchingTool", new ItemStack(BidsItems.scutchingKnife, 1, WILD));

        OreDictionary.registerOre("itemWhorl", new ItemStack(BidsItems.whorl, 1, WILD));

        OreDictionary.registerOre("itemSpindle", new ItemStack(BidsItems.spindle));

        OreDictionary.registerOre("foodBeans", new ItemStack(TFCItems.soybean));
        OreDictionary.registerOre("foodBeans", new ItemStack(BidsItems.wildBeans));
        OreDictionary.registerOre("foodBeans", new ItemStack(BidsItems.broadBeans));

        OreDictionary.registerOre("foodMeatRed", new ItemStack(TFCItems.beefRaw));
        OreDictionary.registerOre("foodMeatRed", new ItemStack(TFCItems.porkchopRaw));
        OreDictionary.registerOre("foodMeatRed", new ItemStack(TFCItems.muttonRaw));
        OreDictionary.registerOre("foodMeatRed", new ItemStack(TFCItems.venisonRaw));
        OreDictionary.registerOre("foodMeatRed", new ItemStack(TFCItems.horseMeatRaw));

        OreDictionary.registerOre("foodMeatPoultry", new ItemStack(TFCItems.chickenRaw));

        OreDictionary.registerOre("foodMeatFish", new ItemStack(TFCItems.fishRaw));
        OreDictionary.registerOre("foodMeatFish", new ItemStack(TFCItems.scallopRaw));
        OreDictionary.registerOre("foodMeatFish", new ItemStack(TFCItems.seastarRaw));
        OreDictionary.registerOre("foodMeatFish", new ItemStack(TFCItems.calamariRaw));

        OreDictionary.registerOre("foodGrainGround", new ItemStack(TFCItems.barleyGround));
        OreDictionary.registerOre("foodGrainGround", new ItemStack(TFCItems.oatGround));
        OreDictionary.registerOre("foodGrainGround", new ItemStack(TFCItems.ryeGround));
        OreDictionary.registerOre("foodGrainGround", new ItemStack(TFCItems.riceGround));
        OreDictionary.registerOre("foodGrainGround", new ItemStack(TFCItems.wheatGround));
        OreDictionary.registerOre("foodGrainGround", new ItemStack(TFCItems.cornmealGround));

        OreDictionary.registerOre("foodGrainCrushed", new ItemStack(BidsItems.barleyCrushed));
        OreDictionary.registerOre("foodGrainCrushed", new ItemStack(BidsItems.oatCrushed));
        OreDictionary.registerOre("foodGrainCrushed", new ItemStack(BidsItems.ryeCrushed));
        OreDictionary.registerOre("foodGrainCrushed", new ItemStack(BidsItems.riceCrushed));
        OreDictionary.registerOre("foodGrainCrushed", new ItemStack(BidsItems.wheatCrushed));
        OreDictionary.registerOre("foodGrainCrushed", new ItemStack(BidsItems.cornmealCrushed));

        OreDictionary.registerOre("foodFruitBerry", new ItemStack(TFCItems.blackberry));
        OreDictionary.registerOre("foodFruitBerry", new ItemStack(TFCItems.blueberry));
        OreDictionary.registerOre("foodFruitBerry", new ItemStack(TFCItems.wintergreenBerry));
        OreDictionary.registerOre("foodFruitBerry", new ItemStack(TFCItems.bunchberry));
        OreDictionary.registerOre("foodFruitBerry", new ItemStack(TFCItems.cranberry));
        OreDictionary.registerOre("foodFruitBerry", new ItemStack(TFCItems.raspberry));
        OreDictionary.registerOre("foodFruitBerry", new ItemStack(TFCItems.gooseberry));
        OreDictionary.registerOre("foodFruitBerry", new ItemStack(TFCItems.elderberry));
        OreDictionary.registerOre("foodFruitBerry", new ItemStack(TFCItems.cloudberry));
        OreDictionary.registerOre("foodFruitBerry", new ItemStack(TFCItems.snowberry));
        OreDictionary.registerOre("foodFruitBerry", new ItemStack(TFCItems.strawberry));

        OreDictionary.registerOre("foodEgg", new ItemStack(TFCItems.egg));

        OreDictionary.registerOre("foodMushroom", new ItemStack(TFCItems.mushroomFoodB));
        OreDictionary.registerOre("foodMushroom", new ItemStack(TFCItems.mushroomFoodR));

        OreDictionary.registerOre("foodBread", new ItemStack(TFCItems.wheatBread));
        OreDictionary.registerOre("foodBread", new ItemStack(TFCItems.barleyBread));
        OreDictionary.registerOre("foodBread", new ItemStack(TFCItems.oatBread));
        OreDictionary.registerOre("foodBread", new ItemStack(TFCItems.ryeBread));
        OreDictionary.registerOre("foodBread", new ItemStack(TFCItems.cornBread));
        OreDictionary.registerOre("foodBread", new ItemStack(TFCItems.riceBread));
        OreDictionary.registerOre("foodBread", new ItemStack(BidsItems.wheatFlatbread));
        OreDictionary.registerOre("foodBread", new ItemStack(BidsItems.barleyFlatbread));
        OreDictionary.registerOre("foodBread", new ItemStack(BidsItems.oatFlatbread));
        OreDictionary.registerOre("foodBread", new ItemStack(BidsItems.ryeFlatbread));
        OreDictionary.registerOre("foodBread", new ItemStack(BidsItems.cornmealFlatbread));
        OreDictionary.registerOre("foodBread", new ItemStack(BidsItems.riceFlatbread));

        OreDictionary.registerOre("foodHardtack", new ItemStack(BidsItems.wheatHardtack));
        OreDictionary.registerOre("foodHardtack", new ItemStack(BidsItems.barleyHardtack));
        OreDictionary.registerOre("foodHardtack", new ItemStack(BidsItems.oatHardtack));
        OreDictionary.registerOre("foodHardtack", new ItemStack(BidsItems.ryeHardtack));
        OreDictionary.registerOre("foodHardtack", new ItemStack(BidsItems.cornmealHardtack));
        OreDictionary.registerOre("foodHardtack", new ItemStack(BidsItems.riceHardtack));
    }

}
