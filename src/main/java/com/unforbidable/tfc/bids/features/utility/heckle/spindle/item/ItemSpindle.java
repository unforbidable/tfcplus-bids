package com.unforbidable.tfc.bids.features.utility.heckle.spindle.item;

import com.unforbidable.tfc.bids.api.features.handwork.HandworkRecipe;
import com.unforbidable.tfc.bids.features.crafting.handwork.item.ItemHandworkTool;
import com.unforbidable.tfc.bids.features.crafting.handwork.main.HandworkProgress;
import com.unforbidable.tfc.bids.features.crafting.spinning.SpinningConfig;
import com.unforbidable.tfc.bids.features.crafting.spinning.SpinningRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemSpindle extends ItemHandworkTool {

    public ItemSpindle(ToolMaterial material) {
        super(material);
    }

    @Override
    protected float getActualMaxItemDuration(float duration) {
        return duration * SpinningConfig.spinningDurationMultiplier;
    }

    @Override
    protected int getNumStages() {
        return 10;
    }

    @Override
    protected HandworkRecipe tryMatchIngredient(ItemStack is) {
        return SpinningRegistry.recipes.findMatchingRecipe(is);
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
