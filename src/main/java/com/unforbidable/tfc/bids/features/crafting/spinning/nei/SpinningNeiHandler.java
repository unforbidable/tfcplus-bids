package com.unforbidable.tfc.bids.features.crafting.spinning.nei;

import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.handwork.SpinningRecipe;
import com.unforbidable.tfc.bids.compat.nei.HandlerInfo;
import com.unforbidable.tfc.bids.features.crafting.handwork.nei.HandworkNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.spinning.SpinningConfig;
import com.unforbidable.tfc.bids.features.crafting.spinning.SpinningRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

public class SpinningNeiHandler extends HandworkNeiHandler {

    static final String HANDLER_ID = "spinning";

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Spinning");
    }

    @Override
    public String getOverlayIdentifier() {
        return HANDLER_ID;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(HANDLER_ID) && getClass() == SpinningNeiHandler.class) {
            for (SpinningRecipe recipe : SpinningRegistry.recipes) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                final ItemStack extra = recipe.getExtraResult(input);
                arecipes.add(new HandworkNeiHandler.CachedHandworkRecipe(input, result, extra, recipe.getDuration() * SpinningConfig.spinningDurationMultiplier));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (SpinningRecipe recipe : SpinningRegistry.recipes) {
            final ItemStack output2 = output.copy();
            output2.stackSize = 1;
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getResult(input);
            final ItemStack result2 = result.copy();
            result2.stackSize = 1;
            final ItemStack extra = recipe.getExtraResult(input);
            final ItemStack extra2 = extra != null ? extra.copy() : null;
            if (extra2 != null) {
                extra2.stackSize = 1;
            }
            if (ItemStack.areItemStacksEqual(result2, output2) || extra2 != null && ItemStack.areItemStacksEqual(extra2, output2)) {
                arecipes.add(new HandworkNeiHandler.CachedHandworkRecipe(input, result, extra, recipe.getDuration() * SpinningConfig.spinningDurationMultiplier));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (SpinningRecipe recipe : SpinningRegistry.recipes) {
            if (recipe.matchesIngredient(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                final ItemStack extra = recipe.getExtraResult(input);
                arecipes.add(new HandworkNeiHandler.CachedHandworkRecipe(input, result, extra, recipe.getDuration() * SpinningConfig.spinningDurationMultiplier));
            }
        }
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(BidsItems.spindle);
        for (ItemStack is : OreDictionary.getOres("itemSpindle", false)) {
            info.addCatalyst(is.getItem());
        }
        return info;
    }

}
