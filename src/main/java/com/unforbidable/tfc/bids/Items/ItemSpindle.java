package com.unforbidable.tfc.bids.Items;

import com.unforbidable.tfc.bids.Core.Handwork.HandworkProgress;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.BidsRegistry;
import com.unforbidable.tfc.bids.api.Crafting.HandworkRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemSpindle extends ItemHandworkTool {

    public ItemSpindle(ToolMaterial material) {
        super(material);
    }

    @Override
    protected float getActualMaxItemDuration(float duration) {
        return duration * BidsOptions.Crafting.spinningDurationMultiplier;
    }

    @Override
    protected int getNumStages() {
        return 10;
    }

    @Override
    protected HandworkRecipe tryMatchIngredient(ItemStack is) {
        return BidsRegistry.SPINNING_RECIPES.findMatchingRecipe(is);
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
