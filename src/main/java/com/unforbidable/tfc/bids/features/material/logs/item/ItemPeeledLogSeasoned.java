package com.unforbidable.tfc.bids.features.material.logs.item;

import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
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
