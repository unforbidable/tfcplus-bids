package com.unforbidable.tfc.bids.Items;

import com.unforbidable.tfc.bids.Core.Handwork.HandworkProgress;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.CardingRecipe;
import com.unforbidable.tfc.bids.api.Crafting.HandworkManager;
import com.unforbidable.tfc.bids.api.Crafting.HandworkRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemCard extends ItemHandworkTool {

    public ItemCard() {
    }

    @Override
    protected int getNumStages() {
        return 5;
    }

    @Override
    protected float getActualMaxItemDuration(float duration) {
        return duration * BidsOptions.Crafting.cardingDurationMultiplier;
    }

    @Override
    protected HandworkRecipe tryMatchIngredient(ItemStack is) {
        return HandworkManager.getMatchingRecipe(is, CardingRecipe.class);
    }

    @Override
    protected String getHandworkStartHelpString() {
        return StatCollector.translateToLocal("gui.Help.Carding.Start");
    }

    @Override
    protected String getHandworkProcessHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Carding.Process") +
            progress.inputItem.getDisplayName() +
            StatCollector.translateToLocal("gui.Help.Carding.Process2");
    }

    @Override
    protected String getHandworkFinishHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Carding.Finish") +
            progress.outputItem.getDisplayName() +
            StatCollector.translateToLocal("gui.Help.Carding.Finish2");
    }

}
