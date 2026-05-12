package com.unforbidable.tfc.bids.features.device.woodpile.block.blockitem;

import com.dunk.tfc.Items.ItemBlocks.ItemStone;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockCrackedStoneIgEx;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockCrackedStoneIgIn;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockCrackedStoneMM;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockCrackedStoneSed;
import net.minecraft.block.Block;

public class ItemCrackedStone extends ItemStone {

    public ItemCrackedStone(Block b) {
        super(b);
        if (b instanceof BlockCrackedStoneSed) {
            this.metaNames = Global.STONE_SED;
        } else if (b instanceof BlockCrackedStoneMM) {
            this.metaNames = Global.STONE_MM;
        } else if (b instanceof BlockCrackedStoneIgIn) {
            this.metaNames = Global.STONE_IGIN;
        } else if (b instanceof BlockCrackedStoneIgEx) {
            this.metaNames = Global.STONE_IGEX;
        }
    }

}
