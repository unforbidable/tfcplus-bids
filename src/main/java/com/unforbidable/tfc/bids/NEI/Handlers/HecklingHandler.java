package com.unforbidable.tfc.bids.NEI.Handlers;

import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.BidsRegistry;
import com.unforbidable.tfc.bids.api.Crafting.HecklingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class HecklingHandler extends HandworkHandler {

    static final String HANDLER_ID = "heckling";

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Heckling");
    }

    @Override
    public String getOverlayIdentifier() {
        return HANDLER_ID;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(HANDLER_ID) && getClass() == HecklingHandler.class) {
            for (HecklingRecipe recipe : BidsRegistry.HECKLING_RECIPES) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new HandworkHandler.CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.hecklingDurationMultiplier));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (HecklingRecipe recipe : BidsRegistry.HECKLING_RECIPES) {
            final ItemStack output2 = output.copy();
            output2.stackSize = 1;
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getResult(input);
            final ItemStack result2 = result.copy();
            result2.stackSize = 1;
            if (ItemStack.areItemStacksEqual(result2, output2)) {
                arecipes.add(new HandworkHandler.CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.hecklingDurationMultiplier));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (HecklingRecipe recipe : BidsRegistry.HECKLING_RECIPES) {
            if (recipe.matchesIngredient(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new HandworkHandler.CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.hecklingDurationMultiplier));
            }
        }
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(BidsItems.boneHeckle);
        info.addCatalyst(BidsItems.boneHeckle);
        return info;
    }

}
