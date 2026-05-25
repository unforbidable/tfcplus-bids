package com.unforbidable.tfc.bids.util.accessor;

import net.minecraft.block.Block;

public interface BlockMetaNamesAccessor {

    Block setMetaNames(String[] names);

    String[] getMetaNames();

}
