package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.Items.ItemBlocks.ItemStone;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;

public class ItemCrackedStone extends ItemStone {

    public ItemCrackedStone(Block b) {
        super(b);

        if (b == BidsBlocks.crackedStoneSed) {
            this.metaNames = Global.STONE_SED;
        } else if (b == BidsBlocks.crackedStoneMM) {
            this.metaNames = Global.STONE_MM;
        } else if (b == BidsBlocks.crackedStoneIgIn) {
            this.metaNames = Global.STONE_IGIN;
        } else if (b == BidsBlocks.crackedStoneIgEx) {
            this.metaNames = Global.STONE_IGEX;
        }
    }

}
