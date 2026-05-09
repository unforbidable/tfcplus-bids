package com.unforbidable.tfc.bids.features.crafting.spinning.nei;

import com.unforbidable.tfc.bids.compat.nei.HandlerInfo;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api._obsolete.BidsOptions;
import com.unforbidable.tfc.bids.api._obsolete.BidsRegistry;
import com.unforbidable.tfc.bids.api._obsolete.Crafting.SpinningRecipe;
import com.unforbidable.tfc.bids.features.crafting.handwork.nei.HandworkNeiHandler;
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
            for (SpinningRecipe recipe : BidsRegistry.SPINNING_RECIPES) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new HandworkNeiHandler.CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.spinningDurationMultiplier));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (SpinningRecipe recipe : BidsRegistry.SPINNING_RECIPES) {
            final ItemStack output2 = output.copy();
            output2.stackSize = 1;
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getResult(input);
            final ItemStack result2 = result.copy();
            result2.stackSize = 1;
            if (ItemStack.areItemStacksEqual(result2, output2)) {
                arecipes.add(new HandworkNeiHandler.CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.spinningDurationMultiplier));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (SpinningRecipe recipe : BidsRegistry.SPINNING_RECIPES) {
            if (recipe.matchesIngredient(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new HandworkNeiHandler.CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.spinningDurationMultiplier));
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
