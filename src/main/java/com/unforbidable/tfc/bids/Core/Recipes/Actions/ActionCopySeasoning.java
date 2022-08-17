package com.unforbidable.tfc.bids.Core.Recipes.Actions;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Seasoning.SeasoningHelper;

import net.minecraft.item.ItemStack;

public class ActionCopySeasoning extends ActionHandleTagCompound {

    @Override
    protected boolean handleTagCompound(ItemStack output, ItemStack ingredient) {
        if (ingredient.hasTagCompound()) {
            final float seasoning = SeasoningHelper.getItemSeasoningTag(ingredient);
            if (seasoning > 0) {
                Bids.LOG.debug("Copying seasoning progress: " + seasoning);

                SeasoningHelper.setItemSeasoningTag(output, seasoning);
            }
        }

        return true;
    }

}
