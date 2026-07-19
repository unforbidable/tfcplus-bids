package com.unforbidable.tfc.bids.core.crafting.actions;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.crafting.CraftingContext;
import com.unforbidable.tfc.bids.features.device.woodpile.main.seasoning.SeasoningHelper;
import java.util.function.Consumer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CopySeasoning extends HandleTagCompound {

    protected CopySeasoning(Item item) {
        super(item);
    }

    public static Consumer<CraftingContext> copySeasoning(Item item) {
        return context -> new CopySeasoning(item).onItemCrafted(context);
    }

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
