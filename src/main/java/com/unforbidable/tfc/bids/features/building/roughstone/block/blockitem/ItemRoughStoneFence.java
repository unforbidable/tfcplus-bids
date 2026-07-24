package com.unforbidable.tfc.bids.features.building.roughstone.block.blockitem;

import com.unforbidable.tfc.bids.features.building.roughstone.block.BlockRoughStone;
import com.unforbidable.tfc.bids.features.building.roughstone.block.BlockRoughStoneFence;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemRoughStoneFence extends ItemRoughStone {

    public ItemRoughStoneFence(Block block) {
        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        Block block = Block.getBlockFromItem(this);
        if (block instanceof BlockRoughStoneFence && ((BlockRoughStoneFence) block).materialBlock instanceof BlockRoughStone) {
            String[] names = ((BlockRoughStone)((BlockRoughStoneFence) block).materialBlock).getMetaNames();
            if (names != null && is.getItemDamage() < names.length) {
                return getUnlocalizedName().concat("." + names[is.getItemDamage()]);
            }
        }

        return super.getUnlocalizedName(is);
    }

}
