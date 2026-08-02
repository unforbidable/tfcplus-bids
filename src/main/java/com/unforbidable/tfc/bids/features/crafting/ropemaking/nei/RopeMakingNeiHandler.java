package com.unforbidable.tfc.bids.features.crafting.ropemaking.nei;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.handwork.RopeMakingRecipe;
import com.unforbidable.tfc.bids.compat.nei.HandlerInfo;
import com.unforbidable.tfc.bids.features.crafting.handwork.nei.HandworkNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.ropemaking.RopeMakingConfig;
import com.unforbidable.tfc.bids.features.crafting.ropemaking.RopeMakingRegistry;
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
            for (RopeMakingRecipe recipe : RopeMakingRegistry.recipes) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                final ItemStack extra = recipe.getExtraResult(input);
                arecipes.add(new CachedHandworkRecipe(input, result, extra, recipe.getDuration() * RopeMakingConfig.ropeMakingDurationMultiplier));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (RopeMakingRecipe recipe : RopeMakingRegistry.recipes) {
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
                arecipes.add(new CachedHandworkRecipe(input, result, extra, recipe.getDuration() * RopeMakingConfig.ropeMakingDurationMultiplier));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (RopeMakingRecipe recipe : RopeMakingRegistry.recipes) {
            if (recipe.matchesIngredient(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                final ItemStack extra = recipe.getExtraResult(input);
                arecipes.add(new CachedHandworkRecipe(input, result, extra, recipe.getDuration() * RopeMakingConfig.ropeMakingDurationMultiplier));
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
