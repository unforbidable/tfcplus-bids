package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.Items.ItemBlocks.ItemStone;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemFireBrickChimney extends ItemStone {

    public ItemFireBrickChimney(Block b) {
        super(b);

        metaNames = new String[]{"FireBrick"};
    }

    @Override
    public boolean getHasSubtypes() {
        return false;
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 32;
    }

}
