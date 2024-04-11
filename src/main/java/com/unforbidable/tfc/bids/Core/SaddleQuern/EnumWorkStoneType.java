package com.unforbidable.tfc.bids.Core.SaddleQuern;

import com.unforbidable.tfc.bids.Blocks.BlockWorkStone;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public enum EnumWorkStoneType {
    NONE,
    SADDLE_QUERN_CRUSHING,
    SADDLE_QUERN_PRESSING;

    public static EnumWorkStoneType getWorkStoneType(ItemStack itemStack) {
        if (itemStack != null) {
            Block block = Block.getBlockFromItem(itemStack.getItem());
            if (block instanceof BlockWorkStone) {
                return ((BlockWorkStone) block).getWorkStoneType();
            }
        }

        return EnumWorkStoneType.NONE;
    }

}
