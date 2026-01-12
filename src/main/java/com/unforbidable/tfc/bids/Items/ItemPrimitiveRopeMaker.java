package com.unforbidable.tfc.bids.Items;

import com.unforbidable.tfc.bids.Core.Handwork.HandworkProgress;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.HandworkManager;
import com.unforbidable.tfc.bids.api.Crafting.HandworkRecipe;
import com.unforbidable.tfc.bids.api.Crafting.RopeMakingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemPrimitiveRopeMaker extends ItemHandworkTool {

    public ItemPrimitiveRopeMaker(ToolMaterial material) {
        super(material);
    }

    @Override
    protected int getNumStages() {
        return 10;
    }


    @Override
    protected float getActualMaxItemDuration(float duration) {
        return duration * BidsOptions.Crafting.ropeMakingDurationMultiplier;
    }

    @Override
    protected HandworkRecipe tryMatchIngredient(ItemStack is) {
        return HandworkManager.getMatchingRecipe(is, RopeMakingRecipe.class);
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
