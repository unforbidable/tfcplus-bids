package com.unforbidable.tfc.bids.Items.ItemBlocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemQuarry extends ItemBlock {

    public ItemQuarry(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int dmg) {
        return dmg;
    }

}
