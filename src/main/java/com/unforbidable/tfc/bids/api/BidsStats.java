package com.unforbidable.tfc.bids.api;

import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.ChatComponentTranslation;

public class BidsStats {

    public static final StatBase BLOCKS_QUARRIED = new StatBasic("stat.bidsBlocksQuarried", new ChatComponentTranslation("stat.bidsBlocksQuarried"))
        .initIndependentStat();

    public static final StatBase SADDLE_QUERN_USED = new StatBasic("stat.bidsSaddleQuernUsed", new ChatComponentTranslation("stat.bidsSaddleQuernUsed"))
        .initIndependentStat();

    public static final StatBase MEALS_COOKED = new StatBasic("stat.bidsMealsCooked", new ChatComponentTranslation("stat.bidsMealsCooked"))
        .initIndependentStat();

    public static final StatBase FIREWOOD_CHOPPED = new StatBasic("stat.bidsFirewoodChopped", new ChatComponentTranslation("stat.bidsFirewoodChopped"))
        .initIndependentStat();

}
