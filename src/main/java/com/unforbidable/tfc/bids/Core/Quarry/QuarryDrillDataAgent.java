package com.unforbidable.tfc.bids.Core.Quarry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

public class QuarryDrillDataAgent {

    static final Map<EntityPlayer, QuarryDrillData> data = new HashMap<EntityPlayer, QuarryDrillData>();

    public static void setPlayerData(EntityPlayer player, int duration) {
        data.put(player, new QuarryDrillData(duration));
    }

    public static void clearPlayerData(EntityPlayer player) {
        data.remove(player);
    }

    public static boolean hasPlayerData(EntityPlayer player) {
        return data.containsKey(player);
    }

    public static int getDuration(EntityPlayer player) {
        return data.get(player).getDuration();
    }

}
