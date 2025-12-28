package com.unforbidable.tfc.bids.NEI.Handlers;

import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Crafting.CardingRecipe;
import com.unforbidable.tfc.bids.api.Crafting.HandworkManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class CardingHandler extends HandworkHandler {

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
        if (outputId.equals(HANDLER_ID) && getClass() == CardingHandler.class) {
            for (CardingRecipe recipe : HandworkManager.getRecipes(CardingRecipe.class)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedHandworkRecipe(input, result, recipe.getDuration()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (CardingRecipe recipe : HandworkManager.getRecipes(CardingRecipe.class)) {
            final ItemStack output2 = output.copy();
            output2.stackSize = 1;
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getResult(input);
            final ItemStack result2 = result.copy();
            result2.stackSize = 1;
            if (ItemStack.areItemStacksEqual(result2, output2)) {
                arecipes.add(new CachedHandworkRecipe(input, result, recipe.getDuration()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (CardingRecipe recipe : HandworkManager.getRecipes(CardingRecipe.class)) {
            if (recipe.matchesIngredient(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedHandworkRecipe(input, result, recipe.getDuration()));
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
