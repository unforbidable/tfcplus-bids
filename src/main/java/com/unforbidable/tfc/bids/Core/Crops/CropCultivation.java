package com.unforbidable.tfc.bids.Core.Crops;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Constant.Global;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class CropCultivation {

    private final Item cultivatedSeedItem;
    private final int minimumSkillRank;
    private final float baseChance;

    public CropCultivation(Item cultivatedSeedItem, int minimumSkillRank, float baseChance) {
        this.cultivatedSeedItem = cultivatedSeedItem;
        this.minimumSkillRank = minimumSkillRank;
        this.baseChance = baseChance;
    }

    public Item getCultivatedSeedItem() {
        return cultivatedSeedItem;
    }

    public float getPlayerCultivationChance(CropAccess crop, EntityPlayer player) {
        int skill = TFC_Core.getSkillStats(player).getSkillRank(Global.SKILL_AGRICULTURE).ordinal();

        if (skill < minimumSkillRank) {
            return 0;
        }

        return baseChance + (skill - minimumSkillRank) * baseChance * 2;
    }

}
