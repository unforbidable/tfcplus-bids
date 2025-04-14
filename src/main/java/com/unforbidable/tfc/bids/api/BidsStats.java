package com.unforbidable.tfc.bids.api;

import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ChatComponentTranslation;

import java.text.DecimalFormat;

public class BidsStats {

    public static final DecimalFormat VOLUME_DECIMAL_FORMAT = new DecimalFormat("########0.0");

    private static final IStatType VOLUME_STAT_TYPE = new IStatType() {
        @Override
        public String format(int value) {
            float valueB = value / 1000f;
            return VOLUME_DECIMAL_FORMAT.format(valueB) + " B";
        }
    };

    private static final IStatType WEIGHT_STAT_TYPE = new IStatType() {
        @Override
        public String format(int value) {
            return VOLUME_DECIMAL_FORMAT.format(value) + " oz";
        }
    };

    public static final StatBase BLOCKS_QUARRIED = new StatBasic("stat.bidsBlocksQuarried", new ChatComponentTranslation("stat.bidsBlocksQuarried"))
        .initIndependentStat();

    public static final StatBase SADDLE_QUERN_USED = new StatBasic("stat.bidsSaddleQuernUsed", new ChatComponentTranslation("stat.bidsSaddleQuernUsed"))
        .initIndependentStat();

    public static final StatBase MEALS_COOKED = new StatBasic("stat.bidsMealsCooked", new ChatComponentTranslation("stat.bidsMealsCooked"))
        .initIndependentStat();

    public static final StatBase FIREWOOD_CHOPPED = new StatBasic("stat.bidsFirewoodChopped", new ChatComponentTranslation("stat.bidsFirewoodChopped"))
        .initIndependentStat();

    public static final StatBase MILK_MILKED = new StatBasic("stat.bidsMilkMilked", new ChatComponentTranslation("stat.bidsMilkMilked"), VOLUME_STAT_TYPE)
        .initIndependentStat();

    public static final StatBase BUTTER_CHURNED = new StatBasic("stat.bidsButterChurned", new ChatComponentTranslation("stat.bidsButterChurned"), WEIGHT_STAT_TYPE)
        .initIndependentStat();

    public static final StatBase MATERIAL_SCRAPED = new StatBasic("stat.bidsMaterialScraped", new ChatComponentTranslation("stat.bidsMaterialScraped"))
        .initIndependentStat();

}
