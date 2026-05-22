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


    public static Item igInHandAxe;
    public static Item sedHandAxe;
    public static Item igExHandAxe;
    public static Item mMHandAxe;

    public static Item hardenedWoodenSpear;

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

    public static Item flatBirchBark;
    public static Item birchBarkSheet;
    public static Item birchBarkStrap;
    public static Item birchBarkRepairPatch;
    public static Item birchBarkBagPiece;
    public static Item birchBarkCupPiece;
    public static Item birchBarkBag;
    public static Item birchBarkCupUnfinished;
    public static Item birchBarkCup;
    public static Item birchBarkShoes;
    public static Item birchBarkKindling;

    public static Item coatBodyFrontLeather;
    public static Item coatBodyBackLeather;

    public static Item leatherCoat;

    public static Item wheatCrushed;
    public static Item barleyCrushed;
    public static Item oatCrushed;
    public static Item ryeCrushed;
    public static Item riceCrushed;
    public static Item cornmealCrushed;

    public static Item wheatPorridge;
    public static Item barleyPorridge;
    public static Item oatPorridge;
    public static Item ryePorridge;
    public static Item ricePorridge;
    public static Item cornmealPorridge;

    public static Item wheatDoughUnshaped;
    public static Item barleyDoughUnshaped;
    public static Item oatDoughUnshaped;
    public static Item ryeDoughUnshaped;
    public static Item riceDoughUnshaped;
    public static Item cornmealDoughUnshaped;

    public static Item wheatDoughFlatbread;
    public static Item barleyDoughFlatbread;
    public static Item oatDoughFlatbread;
    public static Item ryeDoughFlatbread;
    public static Item riceDoughFlatbread;
    public static Item cornmealDoughFlatbread;

    public static Item wheatDoughHardtack;
    public static Item barleyDoughHardtack;
    public static Item oatDoughHardtack;
    public static Item ryeDoughHardtack;
    public static Item riceDoughHardtack;
    public static Item cornmealDoughHardtack;

    public static Item wheatHardtack;
    public static Item barleyHardtack;
    public static Item oatHardtack;
    public static Item ryeHardtack;
    public static Item riceHardtack;
    public static Item cornmealHardtack;

    public static Item wheatFlatbread;
    public static Item barleyFlatbread;
    public static Item oatFlatbread;
    public static Item ryeFlatbread;
    public static Item riceFlatbread;
    public static Item cornmealFlatbread;

    public static Item flatDough;

    public static Item appleCrushed;
    public static Item oliveCrushed;

    public static Item oliveOilBottle;
    public static Item saltWaterBottle;
    public static Item vinegarBottle;
    public static Item brineBottle;
    public static Item honeyBottle;
    public static Item honeyWaterBottle;
    public static Item milkVinegarBottle;
    public static Item fishOilBottle;
    public static Item oilyFishWaterBottle;
    public static Item goatMilkBottle;
    public static Item skimmedMilkBottle;
    public static Item creamBottle;
    public static Item weakWoodAshLyeBottle;
    public static Item woodAshLyeBottle;
    public static Item soapyWaterBottle;
    public static Item flaxSeedOilBottle;

    public static Item waterskinCream;

    public static Item potteryJugVinegar;
    public static Item potteryJugOliveOil;
    public static Item potteryJugFishOil;
    public static Item potteryJugGoatMilk;
    public static Item potteryJugSkimmedMilk;

    public static Item vinegarBowl;
    public static Item oliveOilBowl;
    public static Item fishOilBowl;
    public static Item flaxSeedOilBowl;

    public static Item largeClayBowl;
    public static Item freshWaterLargeBowl;
    public static Item saltWaterLargeBowl;
    public static Item vinegarLargeBowl;
    public static Item milkLargeBowl;
    public static Item honeyLargeBowl;
    public static Item goatMilkLargeBowl;
    public static Item skimmedMilkLargeBowl;
    public static Item creamLargeBowl;
    public static Item weakWoodAshLyeLargeBowl;
    public static Item woodAshLyeLargeBowl;

    public static Item woodenPailEmpty;
    public static Item woodenPailFreshWater;
    public static Item woodenPailMilk;
    public static Item woodenPailGoatMilk;

    public static Item woodenBucketBrine;
    public static Item woodenBucketHoneyWater;
    public static Item woodenBucketMilkVinegar;
    public static Item woodenBucketOilyFishWater;
    public static Item woodenBucketGoatMilk;

    public static Item ceramicBucketBrine;
    public static Item ceramicBucketHoneyWater;
    public static Item ceramicBucketMilkVinegar;
    public static Item ceramicBucketOilyFishWater;
    public static Item ceramicBucketGoatMilk;

    public static Item woodenBucketRope;
    public static Item woodenBucketRopeWater;
    public static Item ceramicBucketRope;
    public static Item ceramicBucketRopeWater;

    public static Item steamingMeshCloth;

    public static Item hardCheese;
    public static Item goatCheese;

    public static Item butter;

    public static Item stuffedPepper;
    public static Item stuffedMushroom;
    public static Item pemmican;
    public static Item wrap;

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

    public static Item bambooShoot;

    public static Item cookingMixture;

    public static Item stew;
    public static Item soup;
    public static Item porridge;
    public static Item omelet;

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
