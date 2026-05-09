package com.unforbidable.tfc.bids.features.crafting.ropemaking.nei;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.compat.nei.HandlerInfo;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api._obsolete.BidsOptions;
import com.unforbidable.tfc.bids.api._obsolete.BidsRegistry;
import com.unforbidable.tfc.bids.api._obsolete.Crafting.RopeMakingRecipe;
import com.unforbidable.tfc.bids.features.crafting.handwork.nei.HandworkNeiHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class RopeMakingNeiHandler extends HandworkNeiHandler {

    static final String HANDLER_ID = "ropemaking";

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.RopeMaking");
    }

    @Override
    public String getOverlayIdentifier() {
        return HANDLER_ID;
    }
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(HANDLER_ID) && getClass() == RopeMakingNeiHandler.class) {
            for (RopeMakingRecipe recipe : BidsRegistry.ROPEMAKING_RECIPES) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.ropeMakingDurationMultiplier));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (RopeMakingRecipe recipe : BidsRegistry.ROPEMAKING_RECIPES) {
            final ItemStack output2 = output.copy();
            output2.stackSize = 1;
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getResult(input);
            final ItemStack result2 = result.copy();
            result2.stackSize = 1;
            if (ItemStack.areItemStacksEqual(result2, output2)) {
                arecipes.add(new CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.ropeMakingDurationMultiplier));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (RopeMakingRecipe recipe : BidsRegistry.ROPEMAKING_RECIPES) {
            if (recipe.matchesIngredient(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.ropeMakingDurationMultiplier));
            }
        }
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(TFCItems.rope);
        info.addCatalyst(BidsItems.primitiveRopeMaker);
        return info;
    }

}
