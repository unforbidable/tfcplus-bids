package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.unforbidable.tfc.bids.Blocks.BlockRoughStoneFence;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemRoughStoneFence extends ItemRoughStone {

    public ItemRoughStoneFence(Block block) {
        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        Block block = Block.getBlockFromItem(this);
        if (block instanceof BlockRoughStoneFence) {
            String[] names = ((BlockRoughStoneFence) block).materialBlock.getNames();
            if (names != null && is.getItemDamage() < names.length) {
                return getUnlocalizedName().concat("." + names[is.getItemDamage()]);
            }
        }

        return super.getUnlocalizedName(is);
    }

}
