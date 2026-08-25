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
    public static final Item skimmedMilkLargeBowl = item(ItemNames.LARGE_BOWL_SKIMMED_MILK);
    public static final Item creamLargeBowl = item(ItemNames.LARGE_BOWL_CREAM);

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
    public static final Item wheatDoughFlatbread = item(ItemNames.WHEAT_DOUGH_FLATBREAD);
    public static final Item barleyDoughFlatbread = item(ItemNames.BARLEY_DOUGH_FLATBREAD);
    public static final Item oatDoughFlatbread = item(ItemNames.OAT_DOUGH_FLATBREAD);
    public static final Item ryeDoughFlatbread = item(ItemNames.RYE_DOUGH_FLATBREAD);
    public static final Item riceDoughFlatbread = item(ItemNames.RICE_DOUGH_FLATBREAD);
    public static final Item cornmealDoughFlatbread = item(ItemNames.CORN_DOUGH_FLATBREAD);

    public static final Item wheatFlatbread = item(ItemNames.WHEAT_FLATBREAD);
    public static final Item barleyFlatbread = item(ItemNames.BARLEY_FLATBREAD);
    public static final Item oatFlatbread = item(ItemNames.OAT_FLATBREAD);
    public static final Item ryeFlatbread = item(ItemNames.RYE_FLATBREAD);
    public static final Item riceFlatbread = item(ItemNames.RICE_FLATBREAD);
    public static final Item cornmealFlatbread = item(ItemNames.CORN_FLATBREAD);

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

    // Tallow
    public static final Item suet = item(ItemNames.SUET);
    public static final Item tallow = item(ItemNames.TALLOW);

    // More hide
    public static final Item moreHide = item(ItemNames.MORE_HIDE);

    // Pemmican
    public static final Item pemmican = item(ItemNames.PEMMICAN);

    // Butter
    public static final Item skimmedMilkBottle = item(ItemNames.BOTTLE_SKIMMED_MILK);
    public static final Item potteryJugSkimmedMilk = item(ItemNames.JUG_SKIMMED_MILK);
    public static final Item creamBottle = item(ItemNames.BOTTLE_CREAM);
    public static final Item waterskinCream = item(ItemNames.WATERSKIN_CREAM);
    public static final Item butter = item(ItemNames.BUTTER);

    // Cheese
    public static final Item goatCheese = item(ItemNames.GOAT_CHEESE);
    public static final Item hardCheese = item(ItemNames.HARD_CHEESE);

    // Fish Oil
    public static final Item oilyFishWaterBottle = item(ItemNames.BOTTLE_OILY_FISH_WATER);
    public static final Item woodenBucketOilyFishWater = item(ItemNames.WOODEN_BUCKET_OILY_FISH_WATER);
    public static final Item ceramicBucketOilyFishWater = item(ItemNames.CERAMIC_BUCKET_OILY_FISH_WATER);
    public static final Item fishOilBottle = item(ItemNames.BOTTLE_FISH_OIL);
    public static final Item potteryJugFishOil = item(ItemNames.JUG_FISH_OIL);
    public static final Item fishOilBowl = item(ItemNames.BOWL_FISH_OIL);

    // Crop
    public static final Item seaBeet = item(ItemNames.SEA_BEET);
    public static final Item beetroot = item(ItemNames.BEETROOT);
    public static final Item sugarBeet = item(ItemNames.SUGAR_BEET);
    public static final Item wildBeans = item(ItemNames.WILD_BEANS);
    public static final Item broadBeans = item(ItemNames.BROAD_BEANS);
    public static final Item seedsSeaBeet = item(ItemNames.SEEDS_SEA_BEET);
    public static final Item seedsBeetroot = item(ItemNames.SEEDS_BEETROOT);
    public static final Item seedsSugarBeet = item(ItemNames.SEEDS_SUGAR_BEET);
    public static final Item seedsWildBeans = item(ItemNames.SEEDS_WILD_BEAN);
    public static final Item seedsBroadBeans = item(ItemNames.SEEDS_BROAD_BEAN);

    // Winter cereals
    public static final Item seedsNewBarley = item(ItemNames.SEEDS_NEW_BARLEY);
    public static final Item seedsNewOat = item(ItemNames.SEEDS_NEW_OAT);
    public static final Item seedsNewRye = item(ItemNames.SEEDS_NEW_RYE);
    public static final Item seedsNewWheat = item(ItemNames.SEEDS_NEW_WHEAT);
    public static final Item seedsWinterBarley = item(ItemNames.SEEDS_WINTER_BARLEY);
    public static final Item seedsWinterOat = item(ItemNames.SEEDS_WINTER_OAT);
    public static final Item seedsWinterRye = item(ItemNames.SEEDS_WINTER_RYE);
    public static final Item seedsWinterWheat = item(ItemNames.SEEDS_WINTER_WHEAT);

    // Hardy crop
    public static final Item seedsNewOnion = item(ItemNames.SEEDS_NEW_ONION);
    public static final Item seedsNewCabbage = item(ItemNames.SEEDS_NEW_CABBAGE);
    public static final Item seedsNewGarlic = item(ItemNames.SEEDS_NEW_GARLIC);
    public static final Item seedsNewCarrot = item(ItemNames.SEEDS_NEW_BARLEY);

    // Powder
    public static final Item morePowder = item(ItemNames.MORE_POWDER);

    // Woodworking
    public static final Item board = item(ItemNames.BOARD);
    public static final Item shaft = item(ItemNames.SHAFT);

    // Spindle
    public static final Item whorl = item(ItemNames.WHORL);
    public static final Item spindle = item(ItemNames.SPINDLE);

    // Rope Maker
    public static final Item primitiveRopeMaker = item(ItemNames.PRIMITIVE_ROPE_MAKER);

    // Card
    public static final Item thornBunch = item(ItemNames.THORN_BUNCH);
    public static final Item thornCard = item(ItemNames.THORN_CARD);
    public static final Item woodenCombPaddle = item(ItemNames.WOODEN_COMB_PADDLE);

    // Heckle
    public static final Item boneHeckle = item(ItemNames.BONE_HECKLE);
    public static final Item boneKnifeHead = item(ItemNames.BONE_KNIFE_BLADE);

    // Textile
    public static final Item barkFiber = item(ItemNames.BARK_FIBER);
    public static final Item barkFiberCoarse = item(ItemNames.BARK_FIBER_COARSE);
    public static final Item barkFiberSmooth = item(ItemNames.BARK_FIBER_SMOOTH);
    public static final Item barkCordage = item(ItemNames.BARK_CORDAGE);
    public static final Item sisalFiberRinsed = item(ItemNames.SISAL_FIBER_RINSED);
    public static final Item sisalFiberCoarse = item(ItemNames.SISAL_FIBER_COARSE);
    public static final Item sisalFiberRefined = item(ItemNames.SISAL_FIBER_REFINED);
    public static final Item sisalTwine = item(ItemNames.SISAL_TWINE);
    public static final Item juteStalk = item(ItemNames.JUTE_STALK);
    public static final Item juteStalkRetted = item(ItemNames.JUTE_STALK_RETTED);
    public static final Item juteFiberCoarse = item(ItemNames.JUTE_FIBER_COARSE);
    public static final Item juteFiberRefined = item(ItemNames.JUTE_FIBER_REFINED);
    public static final Item juteTwine = item(ItemNames.JUTE_TWINE);
    public static final Item flaxStalk = item(ItemNames.FLAX_STALK);
    public static final Item flaxStalkRetted = item(ItemNames.FLAX_STALK_RETTED);
    public static final Item flaxStalkDried = item(ItemNames.FLAX_STALK_DRIED);
    public static final Item flaxStalkBroken = item(ItemNames.FLAX_STALK_BROKEN);
    public static final Item flaxFiberCoarse = item(ItemNames.FLAX_FIBER_COARSE);
    public static final Item flaxFiberRefined = item(ItemNames.FLAX_FIBER_REFINED);
    public static final Item cottonBoll = item(ItemNames.COTTON_BOLL);
    public static final Item cottonBollRefined = item(ItemNames.COTTON_BOLL_REFINED);
    public static final Item cottonFiberCoarse = item(ItemNames.COTTON_FIBER_COARSE);
    public static final Item cottonFiberRefined = item(ItemNames.COTTON_FIBER_REFINED);
    public static final Item woolWashed = item(ItemNames.WOOL_WASHED);
    public static final Item woolRinsed = item(ItemNames.WOOL_RINSED);
    public static final Item woolDried = item(ItemNames.WOOL_DRIED);
    public static final Item woolFiberCoarse = item(ItemNames.WOOL_FIBER_COARSE);
    public static final Item woolFiberRefined = item(ItemNames.WOOL_FIBER_REFINED);
    public static final Item tow = item(ItemNames.TOW);

    // Mallet
    public static final Item woodenMallet = item(ItemNames.WOODEN_MALLET);

    // Scutching Knife
    public static final Item scutchingKnife = item(ItemNames.SCUTCHING_KNIFE);

    // Linseed
    public static final Item flaxSeeds = item(ItemNames.FLAX_SEEDS);
    public static final Item flaxSeedOilBottle = item(ItemNames.BOTTLE_FLAX_SEED_OIL);
    public static final Item flaxSeedOilBowl = item(ItemNames.BOWL_FLAX_SEED_OIL);
    public static final Item potteryJugFlaxSeedOil = item(ItemNames.JUG_FLAX_SEED_OIL);

    // Soap
    public static final Item soap = item(ItemNames.SOAP);
    public static final Item uncuredSoap = item(ItemNames.SOAP_UNCURED);
    public static final Item weakWoodAshLyeBottle = item(ItemNames.BOTTLE_WEAK_WOOD_ASH_LYE);
    public static final Item weakWoodAshLyeLargeBowl = item(ItemNames.LARGE_BOWL_WEAK_WOOD_ASH_LYE);
    public static final Item woodAshLyeBottle = item(ItemNames.BOTTLE_WOOD_ASH_LYE);
    public static final Item woodAshLyeLargeBowl = item(ItemNames.LARGE_BOWL_WOOD_ASH_LYE);
    public static final Item soapyWaterBottle = item(ItemNames.BOTTLE_SOAPY_WATER);

    // Flail
    public static final Item woodenFlail = item(ItemNames.WOODEN_FLAIL);

    // Firestarter
    public static final Item firePlowFirestarter = item(ItemNames.FIRE_PLOW);
    public static final Item handDrillFirestarter = item(ItemNames.HAND_DRILL);
    public static final Item bowDrillFirestarter = item(ItemNames.BOW_DRILL);
    public static final Item tinder = item(ItemNames.TINDER);

    // Nettle
    public static final Item nettle = item(ItemNames.NETTLE);
    public static final Item nettleStalk = item(ItemNames.NETTLE_STALK);
    public static final Item nettleStalkRetted = item(ItemNames.NETTLE_STALK_RETTED);
    public static final Item nettleFiber = item(ItemNames.NETTLE_FIBER);
    public static final Item nettleFiberCoarse = item(ItemNames.NETTLE_FIBER_COARSE);
    public static final Item nettleFiberRefined = item(ItemNames.NETTLE_FIBER_REFINED);
    public static final Item nettleTwine = item(ItemNames.NETTLE_TWINE);

    // Skins
    public static final Item genericSkin = item(ItemNames.GENERIC_SKIN);
    public static final Item genericFur = item(ItemNames.GENERIC_FUR);
    public static final Item wolfFur = item(ItemNames.WOLF_FUR);
    public static final Item bearFur = item(ItemNames.BEAR_FUR);
    public static final Item sheepSkin = item(ItemNames.SHEEP_SKIN);
    public static final Item dehairedSkin = item(ItemNames.DEHAIRED_SKIN);
    public static final Item rawhide = item(ItemNames.RAWHIDE);
    public static final Item leather = item(ItemNames.LEATHER);

    // Digging Stick
    public static final Item diggingStick = item(ItemNames.DIGGING_STICK);
    public static final Item hardenedDiggingStick = item(ItemNames.HARDENED_DIGGING_STICK);

    // Smoother
    public static final Item boneSmoother = item(ItemNames.BONE_SMOOTHER);

    private static Item item(String name) {
        Item item = GameRegistry.findItem(Tags.MOD_ID, "item." + name);
        if (item == null) {
            Bids.LOG.error("Item not found in game registry: {}", name);
        }

        return item;
    }

}
