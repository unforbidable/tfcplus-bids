package com.unforbidable.tfc.bids.Items;

import com.unforbidable.tfc.bids.Core.Wood.WoodIndex;
import net.minecraft.item.ItemStack;

public class ItemPeeledLogSeasoned extends ItemPeeledLog {

    @Override
    protected boolean hasSubItem(WoodIndex wood) {
        return wood.items.hasSeasonedPeeledLog();
    }

    @Override
    protected ItemStack getSubItem(WoodIndex wood) {
        return wood.items.getSeasonedPeeledLog();
    }

}
