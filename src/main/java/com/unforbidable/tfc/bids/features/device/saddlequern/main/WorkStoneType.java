package com.unforbidable.tfc.bids.features.device.saddlequern.main;

import com.unforbidable.tfc.bids.features.device.saddlequern.block.BlockWorkStone;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public enum WorkStoneType {
    NONE,
    SADDLE_QUERN_CRUSHING,
    SADDLE_QUERN_PRESSING;

    public static WorkStoneType getWorkStoneType(ItemStack itemStack) {
        if (itemStack != null) {
            Block block = Block.getBlockFromItem(itemStack.getItem());
            if (block instanceof BlockWorkStone) {
                return ((BlockWorkStone) block).getWorkStoneType();
            }
        }

        return WorkStoneType.NONE;
    }

}
