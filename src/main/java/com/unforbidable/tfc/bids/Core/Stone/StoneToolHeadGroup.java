package com.unforbidable.tfc.bids.Core.Stone;

import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.api.BidsItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class StoneToolHeadGroup {

    public static final StoneToolHeadGroup ADZE_HEAD = new StoneToolHeadGroup(BidsItems.sedStoneAdzeHead, BidsItems.mMStoneAdzeHead, BidsItems.igExStoneAdzeHead, BidsItems.igInStoneAdzeHead);
    public static final StoneToolHeadGroup DRILL_HEAD = new StoneToolHeadGroup(BidsItems.sedStoneDrillHead, BidsItems.mMStoneDrillHead, BidsItems.igExStoneDrillHead, BidsItems.igInStoneDrillHead);

    private final Item sed;
    private final Item mm;
    private final Item igex;
    private final Item igin;

    public StoneToolHeadGroup(Item sed, Item mm, Item igex, Item igin) {
        this.sed = sed;
        this.mm = mm;
        this.igex = igex;
        this.igin = igin;
    }

    public ItemStack getToolHead(int damage, int stackSize) {
        if (igin != null && damage >= Global.STONE_IGIN_START && damage < Global.STONE_IGIN_START + Global.STONE_IGIN.length) {
            return new ItemStack(igin, stackSize, 0);
        } else if (sed != null && damage >= Global.STONE_SED_START && damage < Global.STONE_SED_START + Global.STONE_SED.length) {
            return new ItemStack(sed, stackSize, 0);
        } else if (igex != null && damage >= Global.STONE_IGEX_START && damage < Global.STONE_IGEX_START + Global.STONE_IGEX.length) {
            return new ItemStack(igex, stackSize, 0);
        } else if (mm != null && damage >= Global.STONE_MM_START && damage < Global.STONE_MM_START + Global.STONE_MM.length) {
            return new ItemStack(mm, stackSize, 0);
        }

        return null;
    }

}
