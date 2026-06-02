package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class BidsItems {

    // Ore
    public static final Item oreBit = item(ItemNames.ORE_BIT);

    // Crucible
    public static final Item glassLump = item(ItemNames.GLASS_LUMP);

    // Pottery
    public static final Item clayPipe = item(ItemNames.CLAY_PIPE);
    public static final Item clayMug = item(ItemNames.CLAY_MUG);

    // Mudbrick
    public static final Item dryingMudBrick = item(ItemNames.DRYING_MUD_BRICK);

    // Glassblowing
    public static final Item metalBlowpipe = item(ItemNames.METAL_BLOWPIPE);
    public static final Item brassBlowpipe = item(ItemNames.BRASS_BLOWPIPE);
    public static final Item flatGlass = item(ItemNames.FLAT_GLASS);

    // Glass
    public static final Item drinkingGlass = item(ItemNames.DRINKING_GLASS);
    public static final Item glassJug = item(ItemNames.GLASS_JUG);
    public static final Item shotGlass = item(ItemNames.SHOT_GLASS);

    // Drill
    public static final Item igInStoneDrillHead = item(ItemNames.DRILL_HEAD_STONE_IG_IN);
    public static final Item sedStoneDrillHead = item(ItemNames.DRILL_HEAD_STONE_SED);
    public static final Item igExStoneDrillHead = item(ItemNames.DRILL_HEAD_STONE_IG_EX);
    public static final Item mMStoneDrillHead = item(ItemNames.DRILL_HEAD_STONE_MM);
    public static final Item igInStoneDrill = item(ItemNames.DRILL_STONE_IG_IN);
    public static final Item sedStoneDrill = item(ItemNames.DRILL_STONE_SED);
    public static final Item igExStoneDrill = item(ItemNames.DRILL_STONE_IG_EX);
    public static final Item mMStoneDrill = item(ItemNames.DRILL_STONE_MM);
    public static final Item clayMoldDrill = item(ItemNames.DRILL_MOLD);
    public static final Item copperDrillHead = item(ItemNames.DRILL_HEAD_COPPER);
    public static final Item bronzeDrillHead = item(ItemNames.DRILL_HEAD_BRONZE);
    public static final Item bismuthBronzeDrillHead = item(ItemNames.DRILL_HEAD_BISMUTH_BRONZE);
    public static final Item blackBronzeDrillHead = item(ItemNames.DRILL_HEAD_BLACK_BRONZE);
    public static final Item wroughtIronDrillHead = item(ItemNames.DRILL_HEAD_WROUGHT_IRON);
    public static final Item copperDrill = item(ItemNames.DRILL_COPPER);
    public static final Item bronzeDrill = item(ItemNames.DRILL_BRONZE);
    public static final Item bismuthBronzeDrill = item(ItemNames.DRILL_BISMUTH_BRONZE);
    public static final Item blackBronzeDrill = item(ItemNames.DRILL_BLACK_BRONZE);
    public static final Item wroughtIronDrill = item(ItemNames.DRILL_WROUGHT_IRON);

    // Adze
    public static final Item igInStoneAdzeHead = item(ItemNames.ADZE_HEAD_STONE_IG_IN);
    public static final Item sedStoneAdzeHead = item(ItemNames.ADZE_HEAD_STONE_SED);
    public static final Item igExStoneAdzeHead = item(ItemNames.ADZE_HEAD_STONE_IG_EX);
    public static final Item mMStoneAdzeHead = item(ItemNames.ADZE_HEAD_STONE_MM);
    public static final Item igInStoneAdze = item(ItemNames.ADZE_STONE_IG_IN);
    public static final Item sedStoneAdze = item(ItemNames.ADZE_STONE_SED);
    public static final Item igExStoneAdze = item(ItemNames.ADZE_STONE_IG_EX);
    public static final Item mMStoneAdze = item(ItemNames.ADZE_STONE_MM);
    public static final Item clayMoldAdze = item(ItemNames.ADZE_MOLD);
    public static final Item copperAdzeHead = item(ItemNames.ADZE_HEAD_COPPER);
    public static final Item bronzeAdzeHead = item(ItemNames.ADZE_HEAD_BRONZE);
    public static final Item bismuthBronzeAdzeHead = item(ItemNames.ADZE_HEAD_BISMUTH_BRONZE);
    public static final Item blackBronzeAdzeHead = item(ItemNames.ADZE_HEAD_BLACK_BRONZE);
    public static final Item wroughtIronAdzeHead = item(ItemNames.ADZE_HEAD_WROUGHT_IRON);
    public static final Item copperAdze = item(ItemNames.ADZE_COPPER);
    public static final Item bronzeAdze = item(ItemNames.ADZE_BRONZE);
    public static final Item bismuthBronzeAdze = item(ItemNames.ADZE_BISMUTH_BRONZE);
    public static final Item blackBronzeAdze = item(ItemNames.ADZE_BLACK_BRONZE);
    public static final Item wroughtIronAdze = item(ItemNames.ADZE_WROUGHT_IRON);

    // Quarry
    public static final Item plugAndFeather = item(ItemNames.PLUG_AND_FEATHER);

    // Firepit
    public static final Item smallStickBundle = item(ItemNames.STICK_BUNDLE_SMALL);
    public static final Item tiedStickBundle = item(ItemNames.STICK_BUNDLE_TIED);
    public static final Item kindling = item(ItemNames.KINDLING);

    // Rough Stone
    public static final Item roughStoneBrick = item(ItemNames.ROUGH_STONE_BRICK);
    public static final Item roughStoneTile = item(ItemNames.ROUGH_STONE_TILE);

    // Logs
    public static final Item logsSeasoned = item(ItemNames.LOG_SEASONED);
    public static final Item peeledLog = item(ItemNames.PEELED_LOG);
    public static final Item peeledLogSeasoned = item(ItemNames.PEELED_LOG_SEASONED);

    // Bark
    public static final Item bark = item(ItemNames.BARK);

    // Firewood
    public static final Item firewood = item(ItemNames.FIREWOOD);
    public static final Item firewoodSeasoned = item(ItemNames.FIREWOOD_SEASONED);

    // Clothing
    public static final Item extraStrap = item(ItemNames.EXTRA_STRAP);
    public static final Item extraRepairPatch = item(ItemNames.EXTRA_REPAIR_PATCH);
    public static final Item extraBagPiece = item(ItemNames.EXTRA_BAG_PIECE);
    public static final Item extraCoatBodyFront = item(ItemNames.EXTRA_COAT_BODY_FRONT);
    public static final Item extraCoatBodyBack = item(ItemNames.EXTRA_COAT_BODY_BACK);
    public static final Item cupPiece = item(ItemNames.CUP_PIECE);

    // Birch bark
    public static final Item flatBirchBark = item(ItemNames.FLAT_BIRCH_BARK);
    public static final Item birchBarkSheet = item(ItemNames.BIRCH_BARK_SHEET);
    public static final Item birchBarkBag = item(ItemNames.BIRCH_BARK_BAG);
    public static final Item birchBarkCupUnfinished = item(ItemNames.BIRCH_BARK_CUP_UNFINISHED);
    public static final Item birchBarkCup = item(ItemNames.BIRCH_BARK_CUP);
    public static final Item birchBarkShoes = item(ItemNames.BIRCH_BARK_SHOES);
    public static final Item birchBarkKindling = item(ItemNames.BIRCH_BARK_KINDLING);

    // Leatherwear
    public static final Item leatherCoat = item(ItemNames.LEATHER_COAT);

    // Fluid containers
    public static final Item oliveOilBottle = item(ItemNames.BOTTLE_OLIVE_OIL);
    public static final Item saltWaterBottle = item(ItemNames.BOTTLE_SALT_WATER);
    public static final Item vinegarBottle = item(ItemNames.BOTTLE_VINEGAR);
    public static final Item brineBottle = item(ItemNames.BOTTLE_BRINE);
    public static final Item honeyBottle = item(ItemNames.BOTTLE_HONEY);
    public static final Item potteryJugVinegar = item(ItemNames.JUG_VINEGAR);
    public static final Item potteryJugOliveOil = item(ItemNames.JUG_OLIVE_OIL);
    public static final Item vinegarBowl = item(ItemNames.BOWL_VINEGAR);
    public static final Item oliveOilBowl = item(ItemNames.BOWL_OLIVE_OIL);

    // Large bowl
    public static final Item largeClayBowl = item(ItemNames.LARGE_BOWL);
    public static final Item freshWaterLargeBowl = item(ItemNames.LARGE_BOWL_FRESH_WATER);
    public static final Item saltWaterLargeBowl = item(ItemNames.LARGE_BOWL_SALT_WATER);
    public static final Item vinegarLargeBowl = item(ItemNames.LARGE_BOWL_VINEGAR);
    public static final Item milkLargeBowl = item(ItemNames.LARGE_BOWL_MILK);
    public static final Item honeyLargeBowl = item(ItemNames.LARGE_BOWL_HONEY);

    // Hand Axe
    public static final Item igInHandAxe = item(ItemNames.HAND_AXE_IG_IN);
    public static final Item sedHandAxe = item(ItemNames.HAND_AXE_SED);
    public static final Item igExHandAxe = item(ItemNames.HAND_AXE_IG_EX);
    public static final Item mMHandAxe = item(ItemNames.HAND_AXE_MM);

    // Crushed fruit
    public static final Item appleCrushed = item(ItemNames.APPLE_CRUSHED);
    public static final Item oliveCrushed = item(ItemNames.OLIVE_CRUSHED);

    // Bamboo
    public static final Item bambooShoot = item(ItemNames.BAMBOO_SHOOT);

    // Coarse flour
    public static final Item wheatCrushed = item(ItemNames.WHEAT_CRUSHED);
    public static final Item barleyCrushed = item(ItemNames.BARLEY_CRUSHED);
    public static final Item oatCrushed = item(ItemNames.OAT_CRUSHED);
    public static final Item ryeCrushed = item(ItemNames.RYE_CRUSHED);
    public static final Item riceCrushed = item(ItemNames.RICE_CRUSHED);
    public static final Item cornmealCrushed = item(ItemNames.CORN_CRUSHED);

    // Flatbread
    public static final  Item wheatDoughFlatbread = item(ItemNames.WHEAT_DOUGH_FLATBREAD);
    public static final  Item barleyDoughFlatbread = item(ItemNames.BARLEY_DOUGH_FLATBREAD);
    public static final  Item oatDoughFlatbread = item(ItemNames.OAT_DOUGH_FLATBREAD);
    public static final  Item ryeDoughFlatbread = item(ItemNames.RYE_DOUGH_FLATBREAD);
    public static final  Item riceDoughFlatbread = item(ItemNames.RICE_DOUGH_FLATBREAD);
    public static final  Item cornmealDoughFlatbread = item(ItemNames.CORN_DOUGH_FLATBREAD);

    public static final  Item wheatFlatbread = item(ItemNames.WHEAT_FLATBREAD);
    public static final  Item barleyFlatbread = item(ItemNames.BARLEY_FLATBREAD);
    public static final  Item oatFlatbread = item(ItemNames.OAT_FLATBREAD);
    public static final  Item ryeFlatbread = item(ItemNames.RYE_FLATBREAD);
    public static final  Item riceFlatbread = item(ItemNames.RICE_FLATBREAD);
    public static final  Item cornmealFlatbread = item(ItemNames.CORN_FLATBREAD);

    // Dough
    public static final Item flatDough = item(ItemNames.FLAT_DOUGH);
    public static final Item wheatDoughUnshaped = item(ItemNames.WHEAT_DOUGH_UNSHAPED);
    public static final Item barleyDoughUnshaped = item(ItemNames.BARLEY_DOUGH_UNSHAPED);
    public static final Item oatDoughUnshaped = item(ItemNames.OAT_DOUGH_UNSHAPED);
    public static final Item ryeDoughUnshaped = item(ItemNames.RYE_DOUGH_UNSHAPED);
    public static final Item riceDoughUnshaped = item(ItemNames.RICE_DOUGH_UNSHAPED);
    public static final Item cornmealDoughUnshaped = item(ItemNames.CORN_DOUGH_UNSHAPED);

    // Hardtack
    public static final Item wheatDoughHardtack = item(ItemNames.WHEAT_DOUGH_HARDTACK);
    public static final Item barleyDoughHardtack = item(ItemNames.BARLEY_DOUGH_HARDTACK);
    public static final Item oatDoughHardtack = item(ItemNames.OAT_DOUGH_HARDTACK);
    public static final Item ryeDoughHardtack = item(ItemNames.RYE_DOUGH_HARDTACK);
    public static final Item riceDoughHardtack = item(ItemNames.RICE_DOUGH_HARDTACK);
    public static final Item cornmealDoughHardtack = item(ItemNames.CORN_DOUGH_HARDTACK);

    public static final Item wheatHardtack = item(ItemNames.WHEAT_HARDTACK);
    public static final Item barleyHardtack = item(ItemNames.BARLEY_HARDTACK);
    public static final Item oatHardtack = item(ItemNames.OAT_HARDTACK);
    public static final Item ryeHardtack = item(ItemNames.RYE_HARDTACK);
    public static final Item riceHardtack = item(ItemNames.RICE_HARDTACK);
    public static final Item cornmealHardtack = item(ItemNames.CORN_HARDTACK);

    // Well
    public static final Item woodenBucketRope = item(ItemNames.WOODEN_BUCKET_AND_ROPE);
    public static final Item woodenBucketRopeWater = item(ItemNames.WOODEN_BUCKET_AND_ROPE_FRESH_WATER);
    public static final Item ceramicBucketRope = item(ItemNames.CERAMIC_BUCKET_AND_ROPE);
    public static final Item ceramicBucketRopeWater = item(ItemNames.CERAMIC_BUCKET_AND_ROPE_FRESH_WATER);

    // Pail
    public static final Item woodenPailEmpty = item(ItemNames.WOODEN_PAIL);
    public static final Item woodenPailFreshWater = item(ItemNames.WOODEN_PAIL_FRESH_WATER);
    public static final Item woodenPailMilk = item(ItemNames.WOODEN_PAIL_MILK);

    // Milk
    public static final Item goatMilkBottle = item(ItemNames.BOTTLE_GOAT_MILK);
    public static final Item potteryJugGoatMilk = item(ItemNames.JUG_GOAT_MILK);
    public static final Item woodenBucketGoatMilk = item(ItemNames.WOODEN_BUCKET_GOAT_MILK);
    public static final Item ceramicBucketGoatMilk = item(ItemNames.CERAMIC_BUCKET_GOAT_MILK);
    public static final Item goatMilkLargeBowl = item(ItemNames.LARGE_BOWL_GOAT_MILK);
    public static final Item woodenPailGoatMilk = item(ItemNames.WOODEN_PAIL_GOAT_MILK);

    // Spear
    public static final Item hardenedWoodenSpear = item(ItemNames.HARDENED_WOODEN_SPEAR);

    // Cooking
    public static final Item cookingMixture = item(ItemNames.COOKING_MIXTURE);

    // Cooking Pot
    public static final Item steamingMeshCloth = item(ItemNames.STEAMING_MESH_CLOTH);

    // Cooked Meal
    public static final Item stew = item(ItemNames.STEW);
    public static final Item soup = item(ItemNames.SOUP);
    public static final Item porridge = item(ItemNames.PORRIDGE);
    public static final Item omelet = item(ItemNames.OMELET);

    // Sandwich
    public static final Item stuffedPepper = item(ItemNames.STUFFED_PEPPER);
    public static final Item stuffedMushroom = item(ItemNames.STUFFED_MUSHROOM);
    public static final Item wrap = item(ItemNames.WRAP);





    public static Item board;
    public static Item shaft;

    public static Item barkFibre;
    public static Item barkFibreCoarse;
    public static Item barkFibreSmooth;
    // OBSOLETE
    public static Item barkFibreStrip;
    public static Item barkCordage;
    public static Item barkFibreKindling;

    public static Item sisalFiberRinsed;
    public static Item sisalFiberCoarse;
    public static Item sisalFiberRefined;
    public static Item sisalTwine;

    public static Item juteStalk;
    public static Item juteStalkRetted;
    public static Item juteFiberCoarse;
    public static Item juteFiberRefined;
    public static Item juteTwine;

    public static Item flaxStalk;
    public static Item flaxStalkRetted;
    public static Item flaxStalkDried;
    public static Item flaxStalkBroken;
    public static Item flaxFiberCoarse;
    public static Item flaxFiberRefined;

    public static Item cottonBoll;
    public static Item cottonBollRefined;
    public static Item cottonFiberCoarse;
    public static Item cottonFiberRefined;

    public static Item woolWashed;
    public static Item woolRinsed;
    public static Item woolDried;
    public static Item woolFiberCoarse;
    public static Item woolFiberRefined;

    public static Item wheatPorridge;
    public static Item barleyPorridge;
    public static Item oatPorridge;
    public static Item ryePorridge;
    public static Item ricePorridge;
    public static Item cornmealPorridge;

    public static Item honeyWaterBottle;
    public static Item milkVinegarBottle;
    public static Item fishOilBottle;
    public static Item oilyFishWaterBottle;
    public static Item skimmedMilkBottle;
    public static Item creamBottle;
    public static Item weakWoodAshLyeBottle;
    public static Item woodAshLyeBottle;
    public static Item soapyWaterBottle;
    public static Item flaxSeedOilBottle;

    public static Item waterskinCream;

    public static Item potteryJugFishOil;
    public static Item potteryJugSkimmedMilk;

    public static Item fishOilBowl;
    public static Item flaxSeedOilBowl;

    public static Item skimmedMilkLargeBowl;
    public static Item creamLargeBowl;
    public static Item weakWoodAshLyeLargeBowl;
    public static Item woodAshLyeLargeBowl;

    public static Item woodenBucketBrine;
    public static Item woodenBucketHoneyWater;
    public static Item woodenBucketMilkVinegar;
    public static Item woodenBucketOilyFishWater;

    public static Item ceramicBucketBrine;
    public static Item ceramicBucketHoneyWater;
    public static Item ceramicBucketMilkVinegar;
    public static Item ceramicBucketOilyFishWater;

    public static Item hardCheese;
    public static Item goatCheese;

    public static Item butter;

    public static Item pemmican;
    public static Item seaBeet;
    public static Item beetroot;
    public static Item sugarBeet;
    public static Item wildBeans;
    public static Item broadBeans;

    public static Item flaxSeeds;

    public static Item seedsSeaBeet;
    public static Item seedsBeetroot;
    public static Item seedsSugarBeet;
    public static Item seedsWildBeans;
    public static Item seedsBroadBeans;

    public static Item seedsNewBarley;
    public static Item seedsNewOat;
    public static Item seedsNewRye;
    public static Item seedsNewWheat;

    public static Item seedsWinterBarley;
    public static Item seedsWinterOat;
    public static Item seedsWinterRye;
    public static Item seedsWinterWheat;

    public static Item seedsNewOnion;
    public static Item seedsNewCabbage;
    public static Item seedsNewGarlic;
    public static Item seedsNewCarrot;

    public static Item suet;
    public static Item tallow;

    public static Item moreHide;
    public static Item morePowder;

    public static Item whorl;
    public static Item spindle;
    public static Item primitiveRopeMaker;
    public static Item thornBunch;
    public static Item thornCard;
    public static Item boneHeckle;
    public static Item woodenMallet;
    public static Item scutchingKnife;
    public static Item woodenCombPaddle;
    public static Item boneKnifeHead;

    public static Item soap;
    public static Item uncuredSoap;

    private static Item item(String name) {
        Item item = GameRegistry.findItem(Tags.MOD_ID, name);
        if (item == null) {
            Bids.LOG.error("Item not found in game registry: {}", name);
        }

        return item;
    }

}
