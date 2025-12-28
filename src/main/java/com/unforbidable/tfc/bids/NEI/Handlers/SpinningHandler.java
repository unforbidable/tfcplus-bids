package com.unforbidable.tfc.bids.NEI.Handlers;

import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.HandworkManager;
import com.unforbidable.tfc.bids.api.Crafting.SpinningRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

public class SpinningHandler extends HandworkHandler {

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
        if (outputId.equals(HANDLER_ID) && getClass() == SpinningHandler.class) {
            for (SpinningRecipe recipe : HandworkManager.getRecipes(SpinningRecipe.class)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new HandworkHandler.CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.spinningDurationMultiplier));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (SpinningRecipe recipe : HandworkManager.getRecipes(SpinningRecipe.class)) {
            final ItemStack output2 = output.copy();
            output2.stackSize = 1;
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getResult(input);
            final ItemStack result2 = result.copy();
            result2.stackSize = 1;
            if (ItemStack.areItemStacksEqual(result2, output2)) {
                arecipes.add(new HandworkHandler.CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.spinningDurationMultiplier));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (SpinningRecipe recipe : HandworkManager.getRecipes(SpinningRecipe.class)) {
            if (recipe.matchesIngredient(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new HandworkHandler.CachedHandworkRecipe(input, result, recipe.getDuration() * BidsOptions.Crafting.spinningDurationMultiplier));
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
