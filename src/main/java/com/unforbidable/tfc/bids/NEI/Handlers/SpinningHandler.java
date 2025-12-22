package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Crafting.SpinningManager;
import com.unforbidable.tfc.bids.api.Crafting.SpinningRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;

public class SpinningHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "spinning";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_spinning.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Spinning");
    }

    @Override
    public String getGuiTexture() {
        return guiTexture.toString();
    }

    @Override
    public String getOverlayIdentifier() {
        return HANDLER_ID;
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(71, 23, 24, 18), HANDLER_ID));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(HANDLER_ID) && getClass() == SpinningHandler.class) {
            for (SpinningRecipe recipe : SpinningManager.getRecipes()) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedHandcraftRecipe(input, result));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (SpinningRecipe recipe : SpinningManager.getRecipes()) {
            final ItemStack output2 = output.copy();
            output2.stackSize = 1;
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getResult(input);
            final ItemStack result2 = result.copy();
            result2.stackSize = 1;
            if (ItemStack.areItemStacksEqual(result2, output2)) {
                arecipes.add(new CachedHandcraftRecipe(input, result));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (SpinningRecipe recipe : SpinningManager.getRecipes()) {
            if (recipe.matchesIngredient(ingredient)) {
                final ItemStack input = new ItemStack(ingredient.getItem(), 1, ingredient.getItemDamage());
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedHandcraftRecipe(input, result));
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

    public class CachedHandcraftRecipe extends CachedRecipe {

        final ItemStack ingred;
        final ItemStack result;

        public CachedHandcraftRecipe(ItemStack ingred, ItemStack result) {
            this.ingred = ingred.copy();
            this.result = result.copy();
        }

        @Override
        public PositionedStack getResult() {
            return new PositionedStack(result, 111, 24);
        }

        @Override
        public PositionedStack getIngredient() {
            return new PositionedStack(ingred, 39, 24);
        }

    }

}
