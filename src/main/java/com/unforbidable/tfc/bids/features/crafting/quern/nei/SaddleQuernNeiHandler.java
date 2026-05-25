package com.unforbidable.tfc.bids.features.crafting.quern.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.quern.SaddleQuernRecipe;
import com.unforbidable.tfc.bids.compat.nei.HandlerInfo;
import com.unforbidable.tfc.bids.compat.nei.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.features.device.saddlequern.SaddleQuernRegistry;
import java.awt.Rectangle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class SaddleQuernNeiHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "saddlequern";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_saddlequern.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.SaddleQuern");
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
        if (outputId.equals(HANDLER_ID) && getClass() == SaddleQuernNeiHandler.class) {
            for (SaddleQuernRecipe recipe : SaddleQuernRegistry.recipes) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getCraftingResult();
                arecipes.add(new CachedSaddleQuernRecipe(input, result));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (SaddleQuernRecipe recipe : SaddleQuernRegistry.recipes) {
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getCraftingResult();
            if (ItemStack.areItemStacksEqual(result, output)
                    || result.getItem() instanceof IFood && result.getItem() == output.getItem()
                            && Food.areEqual(ItemFoodTFC.createTag(result.copy()), output)) {
                arecipes.add(new CachedSaddleQuernRecipe(input, output));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (SaddleQuernRecipe recipe : SaddleQuernRegistry.recipes) {
            if (recipe.matches(ingredient)) {
                final ItemStack input = ingredient.copy();
                input.stackSize = recipe.getInput().stackSize;
                if (input.getItem() instanceof ItemFoodTFC) {
                    input.setTagCompound(recipe.getInput().getTagCompound());
                }

                final ItemStack result = recipe.getCraftingResult();
                arecipes.add(new CachedSaddleQuernRecipe(input, result));
            }
        }
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(BidsBlocks.saddleQuernHandstoneSed, 4);
        for (int i = 0; i < Global.STONE_SED.length; i++) {
            info.addCatalyst(BidsBlocks.saddleQuernBaseSed, i);
            info.addCatalyst(BidsBlocks.saddleQuernHandstoneSed, i);
        }
        return info;
    }

    public class CachedSaddleQuernRecipe extends CachedRecipe {

        final ItemStack ingred;
        final ItemStack result;

        public CachedSaddleQuernRecipe(ItemStack ingred, ItemStack result) {
            this.ingred = ingred.getItem() instanceof IFood ? ItemFoodTFC.createTag(ingred.copy(), 4) : ingred.copy();
            this.result = ingred.getItem() instanceof IFood ? ItemFoodTFC.createTag(result.copy(), 4) : result.copy();
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
