package com.unforbidable.tfc.bids.Items;

import com.unforbidable.tfc.bids.Core.Handwork.HandworkProgress;
import com.unforbidable.tfc.bids.api.Crafting.HandworkRecipe;
import com.unforbidable.tfc.bids.api.Crafting.SpinningManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemSpindle extends ItemHandworkTool {

    public ItemSpindle() {
    }

    @Override
    protected int getNumStages() {
        return 10;
    }

    @Override
    protected HandworkRecipe tryMatchIngredient(ItemStack is) {
        return SpinningManager.getMatchingRecipe(is);
    }

    @Override
    protected String getHandworkStartHelpString() {
        return StatCollector.translateToLocal("gui.Help.Spinning.Start");
    }

    @Override
    protected String getHandworkProcessHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Spinning.Process") +
            progress.outputItem.getDisplayName() +
            StatCollector.translateToLocal("gui.Help.Spinning.Process2");
    }

    @Override
    protected String getHandworkFinishHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Spinning.Finish") +
            progress.outputItem.getDisplayName() +
            StatCollector.translateToLocal("gui.Help.Spinning.Finish2");
    }

}
