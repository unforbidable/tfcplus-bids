package com.unforbidable.tfc.bids.Items;

import com.unforbidable.tfc.bids.Core.Handwork.HandworkProgress;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.HandworkManager;
import com.unforbidable.tfc.bids.api.Crafting.HandworkRecipe;
import com.unforbidable.tfc.bids.api.Crafting.HecklingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemHeckle extends ItemHandworkTool {

    public ItemHeckle() {
    }

    @Override
    protected int getNumStages() {
        return 5;
    }

    @Override
    protected float getActualMaxItemDuration(float duration) {
        return duration * BidsOptions.Crafting.hecklingDurationMultiplier;
    }

    @Override
    protected HandworkRecipe tryMatchIngredient(ItemStack is) {
        return HandworkManager.getMatchingRecipe(is, HecklingRecipe.class);
    }

    @Override
    protected String getHandworkStartHelpString() {
        return StatCollector.translateToLocal("gui.Help.Heckling.Start");
    }

    @Override
    protected String getHandworkProcessHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Heckling.Process") +
            progress.inputItem.getDisplayName() +
            StatCollector.translateToLocal("gui.Help.Heckling.Process2");
    }

    @Override
    protected String getHandworkFinishHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Heckling.Finish") +
            progress.outputItem.getDisplayName() +
            StatCollector.translateToLocal("gui.Help.Heckling.Finish2");
    }

}
