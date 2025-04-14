package com.unforbidable.tfc.bids.Core;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsAchievements;
import com.unforbidable.tfc.bids.api.BidsStats;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraftforge.common.AchievementPage;

public class AchievementSetup {

    public static void init() {
        Bids.LOG.info("Setup achievements and stats");

        Achievement[] achievements = new Achievement[] {
            BidsAchievements.STONE_ADZE,
            BidsAchievements.BARK,
            BidsAchievements.BARK_CORDAGE,
            BidsAchievements.BARK_ROPE,
            BidsAchievements.STONE_DRILL,
            BidsAchievements.ROUGH_STONE,
            BidsAchievements.METAL_DRILL,
            BidsAchievements.HARD_QUERN,
            BidsAchievements.SADDLE_QUERN,
            BidsAchievements.HAND_STONE,
            BidsAchievements.STONE_PRESS_LEVER,
            BidsAchievements.PRESSING_STONE_USE,
            BidsAchievements.WEIGHT_STONE_USE,
            BidsAchievements.CERAMIC_CRUCIBLE,
            BidsAchievements.CRUCIBLE_INGOT,
            BidsAchievements.WELDED_ANVIL,
            BidsAchievements.CRUCIBLE_GLASS,
            BidsAchievements.COOKING_POT,
            BidsAchievements.COOKED_MEAL,
            BidsAchievements.MUSHROOM_STEW,
            BidsAchievements.MILK_PORRIDGE,
            BidsAchievements.BUTTER,
            BidsAchievements.AQUIFER,
            BidsAchievements.CULTIVATED_SEED,
            BidsAchievements.WINTER_CEREAL_SEED
        };

        for (Achievement a : achievements) {
            a.registerStat();
        }

        AchievementPage.registerAchievementPage(new AchievementPage("Bids", achievements));

        StatBase[] stats = new StatBase[] {
            BidsStats.BLOCKS_QUARRIED,
            BidsStats.SADDLE_QUERN_USED,
            BidsStats.MEALS_COOKED,
            BidsStats.FIREWOOD_CHOPPED,
            BidsStats.MILK_MILKED,
            BidsStats.BUTTER_CHURNED,
            BidsStats.MATERIAL_SCRAPED
        };

        for (StatBase s : stats) {
            s.registerStat();
        }
    }

}
