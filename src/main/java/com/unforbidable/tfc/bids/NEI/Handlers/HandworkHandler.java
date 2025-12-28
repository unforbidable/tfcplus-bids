package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.Crafting.HandworkManager;
import com.unforbidable.tfc.bids.api.Crafting.HandworkRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.awt.*;

public class HandworkHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "handwork";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_handwork.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Handwork");
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
        transferRects.add(new RecipeTransferRect(new Rectangle(71, 23, 24, 18), getOverlayIdentifier()));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(HANDLER_ID) && getClass() == HandworkHandler.class) {
            for (HandworkRecipe recipe : HandworkManager.getRecipes()) {
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
        for (HandworkRecipe recipe : HandworkManager.getRecipes()) {
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
        for (HandworkRecipe recipe : HandworkManager.getRecipes()) {
            if (recipe.matchesIngredient(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedHandworkRecipe(input, result, recipe.getDuration()));
            }
        }
    }

    @Override
    public void drawExtras(int recipe) {
        CachedRecipe crecipe = arecipes.get(recipe);
        if (crecipe instanceof CachedHandworkRecipe) {
            final CachedHandworkRecipe cachedSoakingSurfaceRecipe = (CachedHandworkRecipe) crecipe;

            drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                cachedSoakingSurfaceRecipe.getDurationString(), 83, 49, 0x555555);
        }
    }

    private static void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        return new HandlerInfo(TFCBlocks.invisibleBlock);
    }

    public class CachedHandworkRecipe extends CachedRecipe {

        final ItemStack ingred;
        final ItemStack result;
        final float duration;

        public CachedHandworkRecipe(ItemStack ingred, ItemStack result, float duration) {
            this.ingred = ingred.copy();
            this.result = result.copy();
            this.duration = duration;
        }

        @Override
        public PositionedStack getResult() {
            return new PositionedStack(result, 111, 24);
        }

        @Override
        public PositionedStack getIngredient() {
            return new PositionedStack(ingred, 39, 24);
        }

        public String getDurationString() {
            return String.format("%d %s", Math.round(duration / 20), StatCollector.translateToLocal("gui.Seconds").toLowerCase());
        }

    }

}
