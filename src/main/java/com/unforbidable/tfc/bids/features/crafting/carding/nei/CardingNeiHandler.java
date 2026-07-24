package com.unforbidable.tfc.bids.features.crafting.carding.nei;

import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.handwork.CardingRecipe;
import com.unforbidable.tfc.bids.compat.nei.HandlerInfo;
import com.unforbidable.tfc.bids.features.crafting.carding.CardingConfig;
import com.unforbidable.tfc.bids.features.crafting.carding.CardingRegistry;
import com.unforbidable.tfc.bids.features.crafting.handwork.nei.HandworkNeiHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class CardingNeiHandler extends HandworkNeiHandler {

    static final String HANDLER_ID = "carding";

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Carding");
    }

    @Override
    public String getOverlayIdentifier() {
        return HANDLER_ID;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(HANDLER_ID) && getClass() == CardingNeiHandler.class) {
            for (CardingRecipe recipe : CardingRegistry.recipes) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedHandworkRecipe(input, result, recipe.getDuration() * CardingConfig.cardingDurationMultiplier));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (CardingRecipe recipe : CardingRegistry.recipes) {
            final ItemStack output2 = output.copy();
            output2.stackSize = 1;
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getResult(input);
            final ItemStack result2 = result.copy();
            result2.stackSize = 1;
            if (ItemStack.areItemStacksEqual(result2, output2)) {
                arecipes.add(new CachedHandworkRecipe(input, result, recipe.getDuration() * CardingConfig.cardingDurationMultiplier));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (CardingRecipe recipe : CardingRegistry.recipes) {
            if (recipe.matchesIngredient(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedHandworkRecipe(input, result, recipe.getDuration() * CardingConfig.cardingDurationMultiplier));
            }
        }
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(BidsItems.thornCard);
        info.addCatalyst(BidsItems.thornCard);
        return info;
    }

}
