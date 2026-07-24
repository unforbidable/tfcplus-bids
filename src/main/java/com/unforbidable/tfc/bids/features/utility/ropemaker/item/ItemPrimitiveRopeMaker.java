package com.unforbidable.tfc.bids.features.utility.ropemaker.item;

import com.unforbidable.tfc.bids.api.features.handwork.HandworkRecipe;
import com.unforbidable.tfc.bids.features.crafting.handwork.item.ItemHandworkTool;
import com.unforbidable.tfc.bids.features.crafting.handwork.main.HandworkProgress;
import com.unforbidable.tfc.bids.features.crafting.ropemaking.RopeMakingConfig;
import com.unforbidable.tfc.bids.features.crafting.ropemaking.RopeMakingRegistry;
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
        return duration * RopeMakingConfig.ropeMakingDurationMultiplier;
    }

    @Override
    protected HandworkRecipe tryMatchIngredient(ItemStack is) {
        return RopeMakingRegistry.recipes.findMatchingRecipe(is);
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
