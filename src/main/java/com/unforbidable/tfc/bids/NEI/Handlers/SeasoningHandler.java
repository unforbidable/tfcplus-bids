package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.unforbidable.tfc.bids.Core.Seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningManager;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.awt.*;

public class SeasoningHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "woodpileseasoning";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_seasoning.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Seasoning");
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
        if (outputId.equals(HANDLER_ID) && getClass() == SeasoningHandler.class) {
            for (SeasoningRecipe recipe : SeasoningManager.getRecipes()) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getCraftingResult(input);
                arecipes.add(new CachedSeasoningRecipe(input, result, recipe.getDuration()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (SeasoningRecipe recipe : SeasoningManager.getRecipes()) {
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getCraftingResult(input);
            if (ItemStack.areItemStacksEqual(result, output)) {
                arecipes.add(new CachedSeasoningRecipe(input, output, recipe.getDuration()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (SeasoningRecipe recipe : SeasoningManager.getRecipes()) {
            if (recipe.matches(ingredient)) {
                final ItemStack input = new ItemStack(ingredient.getItem(), 1, ingredient.getItemDamage());
                final ItemStack result = recipe.getCraftingResult(ingredient);
                arecipes.add(new CachedSeasoningRecipe(input, result, recipe.getDuration()));
            }
        }
    }

    @Override
    public void drawExtras(int recipe) {
        CachedRecipe crecipe = arecipes.get(recipe);
        if (crecipe instanceof CachedSeasoningRecipe) {
            final CachedSeasoningRecipe cachedSeasoningRecipe = (CachedSeasoningRecipe) crecipe;

            drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                    BidsBlocks.woodPile.getLocalizedName(), 83, 8, 0x820093);
            drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                    cachedSeasoningRecipe.getDurationString(), 83, 49, 0x555555);
        }
    }

    private static void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        return new HandlerInfo(BidsBlocks.woodPile);
    }

    public class CachedSeasoningRecipe extends CachedRecipe {

        final ItemStack ingred;
        final ItemStack result;
        final int duration;

        public CachedSeasoningRecipe(ItemStack ingred, ItemStack result, int duration) {
            this.ingred = ingred;
            this.result = result;
            this.duration = duration;
        }

        public String getDurationString() {
            final int hours = (int) (duration * BidsOptions.WoodPile.seasoningDurationMultiplier);
            final float days = hours / 24f;

            return String.format("%.01f %s", days, StatCollector.translateToLocal("gui.Days").toLowerCase());
        }

        @Override
        public PositionedStack getResult() {
            return new PositionedStack(result, 111, 24);
        }

        @Override
        public PositionedStack getIngredient() {
            final int i = cycleticks % 200;
            final ItemStack is = ingred.copy();
            final float f = Math.min(i / 200f, 1);
            SeasoningHelper.setItemSeasoningTag(is, f);
            return new PositionedStack(is, 39, 24);
        }

    }

}
