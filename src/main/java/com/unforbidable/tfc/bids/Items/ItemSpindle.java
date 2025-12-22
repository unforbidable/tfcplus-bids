package com.unforbidable.tfc.bids.Items;

import com.unforbidable.tfc.bids.Core.Handwork.HandworkProgress;
import com.unforbidable.tfc.bids.api.Crafting.SpinningManager;
import com.unforbidable.tfc.bids.api.Crafting.SpinningRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemSpindle extends ItemHandworkTool {

    public ItemSpindle() {
    }

    @Override
    protected int getNumStages() {
        return 9;
    }

    @Override
    protected HandworkProgress tryStartProcessUsingIngredient(ItemStack is) {
        SpinningRecipe recipe = SpinningManager.getMatchingRecipe(is);
        if (recipe != null) {
            ItemStack outputItem = recipe.getResult(is);
            int duration = recipe.getDuration();
            return new HandworkProgress(outputItem, duration, 0);
        } else {
            return null;
        }
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
