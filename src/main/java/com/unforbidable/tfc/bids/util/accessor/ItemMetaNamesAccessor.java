package com.unforbidable.tfc.bids.util.accessor;

import net.minecraft.item.Item;

public interface ItemMetaNamesAccessor {

    Item setMetaNames(String[] names);

    String[] getMetaNames();

}
