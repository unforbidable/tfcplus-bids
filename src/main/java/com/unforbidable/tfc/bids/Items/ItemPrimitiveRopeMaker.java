package com.unforbidable.tfc.bids.Items;

import com.unforbidable.tfc.bids.Core.Handwork.HandworkProgress;
import com.unforbidable.tfc.bids.api.Crafting.HandworkRecipe;
import com.unforbidable.tfc.bids.api.Crafting.RopeMakingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemPrimitiveRopeMaker extends ItemHandworkTool {

    @Override
    protected int getNumStages() {
        return 10;
    }

    @Override
    protected HandworkRecipe tryMatchIngredient(ItemStack is) {
        return RopeMakingManager.getMatchingRecipe(is);
    }

    @Override
    protected String getHandworkStartHelpString() {
        return StatCollector.translateToLocal("gui.Help.Twisting.Start");
    }

    @Override
    protected String getHandworkProcessHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Twisting.Process");
    }

    @Override
    protected String getHandworkFinishHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Twisting.Finish");
    }

}
