package com.unforbidable.tfc.bids.api;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public class BidsAchievements {

    public static final Achievement STONE_ADZE = new Achievement("achievement.bidsStoneAdze", "bidsStoneAdze",
        0, 0, new ItemStack(BidsItems.sedStoneAdze), null);

    public static final Achievement BARK = new Achievement("achievement.bidsBark", "bidsBark",
        -2, 0, new ItemStack(BidsItems.bark, 1, 0), STONE_ADZE);

    public static final Achievement BARK_CORDAGE = new Achievement("achievement.bidsBarkCordage", "bidsBarkCordage",
        -4, 0, new ItemStack(BidsItems.barkCordage), BARK);

    public static final Achievement BARK_ROPE = new Achievement("achievement.bidsBarkRope", "bidsBarkRope",
        -6, 0, new ItemStack(TFCItems.rope), BARK_CORDAGE).setSpecial();

    public static final Achievement STONE_DRILL = new Achievement("achievement.bidsStoneDrill", "bidsStoneDrill",
        2, 0, new ItemStack(BidsItems.sedStoneDrill), STONE_ADZE);

    public static final Achievement ROUGH_STONE = new Achievement("achievement.bidsRoughStone", "bidsRoughStone",
        4, 0, new ItemStack(BidsBlocks.roughStoneSed, 1, 6), STONE_DRILL);

    public static final Achievement METAL_DRILL = new Achievement("achievement.bidsMetalDrill", "bidsMetalDrill",
        6, -3, new ItemStack(BidsItems.bronzeDrill, 1, 0), ROUGH_STONE);

    public static final Achievement HARD_QUERN = new Achievement("achievement.bidsHardQuern", "bidsHardQuern",
        8, -3, new ItemStack(TFCItems.quern), METAL_DRILL).setSpecial();

    public static final Achievement SADDLE_QUERN = new Achievement("achievement.bidsSaddleQuern", "bidsSaddleQuern",
        6, 0, new ItemStack(BidsBlocks.saddleQuernBaseSed, 1, 6), ROUGH_STONE);

    public static final Achievement HAND_STONE = new Achievement("achievement.bidsHandStone", "bidsHandStone",
        10, -1, new ItemStack(BidsItems.wheatCrushed, 1, 6), SADDLE_QUERN).setSpecial();

    public static final Achievement PRESSING_STONE_USE = new Achievement("achievement.bidsPressingStoneUse", "bidsPressingStoneUse",
        8, 0, new ItemStack(Items.feather), SADDLE_QUERN);

    public static final Achievement STONE_PRESS_LEVER = new Achievement("achievement.bidsStonePressLever", "bidsStonePressLever",
        8, 1, new ItemStack(BidsItems.peeledLog, 1, 0), SADDLE_QUERN);

    public static final Achievement WEIGHT_STONE_USE = new Achievement("achievement.bidsWeightStoneUse", "bidsWeightStoneUse",
        10, 1, new ItemStack(Items.glass_bottle), STONE_PRESS_LEVER).setSpecial();

    public static final Achievement CERAMIC_CRUCIBLE = new Achievement("achievement.bidsCeramicCrucible", "bidsCeramicCrucible",
        0, 4, new ItemStack(BidsBlocks.clayCrucible, 1, 0), null);

    public static final Achievement CRUCIBLE_INGOT = new Achievement("achievement.bidsCrucibleIngot", "bidsCrucibleIngot",
        -2, 3, new ItemStack(TFCItems.copperUnshaped), CERAMIC_CRUCIBLE);

    public static final Achievement WELDED_ANVIL = new Achievement("achievement.bidsWeldedAnvil", "bidsWeldedAnvil",
        -4, 3, new ItemStack(TFCBlocks.anvil, 1, 1), CRUCIBLE_INGOT).setSpecial();

    public static final Achievement CRUCIBLE_GLASS = new Achievement("achievement.bidsCrucibleGlass", "bidsCrucibleGlass",
        -2, 5, new ItemStack(BidsItems.glassLump), CERAMIC_CRUCIBLE);

    public static final Achievement COOKING_POT = new Achievement("achievement.bidsCookingPot", "bidsCookingPot",
        2, 4, new ItemStack(BidsBlocks.cookingPot, 1, 9), null);

    public static final Achievement COOKED_MEAL = new Achievement("achievement.bidsCookedMeal", "bidsCookedMeal",
        4, 4, new ItemStack(BidsItems.soup, 1, 1), COOKING_POT);

    public static final Achievement MUSHROOM_STEW = new Achievement("achievement.bidsMushroomStew", "bidsMushroomStew",
        6, 3, new ItemStack(BidsItems.stew), COOKED_MEAL).setSpecial();

    public static final Achievement MILK_PORRIDGE = new Achievement("achievement.bidsMilkPorridge", "bidsMilkPorridge",
        6, 5, new ItemStack(BidsItems.porridge), COOKED_MEAL).setSpecial();

    public static final Achievement AQUIFER = new Achievement("achievement.bidsAquifer", "bidsAquifer",
        0, 7, new ItemStack(Items.water_bucket), null);

    public static final Achievement CULTIVATED_SEED = new Achievement("achievement.bidsCultivatedSeed", "bidsCultivatedSeed",
        2, 7, new ItemStack(BidsItems.seedsBeetroot), null).setSpecial();

    public static final Achievement WINTER_CEREAL_SEED = new Achievement("achievement.bidsWinterCerealSeed", "bidsWinterCerealSeed",
        4, 7, new ItemStack(BidsItems.seedsWinterWheat), null).setSpecial();

}
