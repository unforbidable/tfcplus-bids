package com.unforbidable.tfc.bids.NEI.Handlers;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.HandworkRecipe;
import com.unforbidable.tfc.bids.api.Crafting.RopeMakingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class RopeMakingHandler extends HandworkHandler {

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
        if (outputId.equals(HANDLER_ID) && getClass() == RopeMakingHandler.class) {
            for (HandworkRecipe recipe : RopeMakingManager.getRecipes()) {
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
        for (HandworkRecipe recipe : RopeMakingManager.getRecipes()) {
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
        for (HandworkRecipe recipe : RopeMakingManager.getRecipes()) {
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
