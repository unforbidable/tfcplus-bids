package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Crafting.DryingManager;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.awt.*;

public class DryingRackHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "dryingrack";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_drying.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Drying");
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
        if (outputId.equals(HANDLER_ID) && getClass() == DryingRackHandler.class) {
            for (DryingRecipe recipe : DryingManager.getRecipes()) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getCraftingResult(input);
                arecipes.add(new CachedDryingRecipe(input, result, recipe.getDuration()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (DryingRecipe recipe : DryingManager.getRecipes()) {
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getCraftingResult(input);
            if (ItemStack.areItemStacksEqual(result, output)) {
                arecipes.add(new CachedDryingRecipe(input, output, recipe.getDuration()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (DryingRecipe recipe : DryingManager.getRecipes()) {
            if (recipe.matches(ingredient)) {
                final ItemStack input = ingredient.copy();
                input.stackSize = recipe.getInput().stackSize;
                if (input.getItem() instanceof ItemFoodTFC) {
                    input.setTagCompound(recipe.getInput().getTagCompound());
                }

                final ItemStack result = recipe.getCraftingResult(input);
                arecipes.add(new CachedDryingRecipe(input, result, recipe.getDuration()));
            }
        }
    }

    @Override
    public void drawExtras(int recipe) {
        CachedRecipe crecipe = arecipes.get(recipe);
        if (crecipe instanceof CachedDryingRecipe) {
            final CachedDryingRecipe cachedDryingRecipe = (CachedDryingRecipe) crecipe;

            drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                    BidsBlocks.dryingRack.getLocalizedName(), 83, 8, 0x820093);
            drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                    cachedDryingRecipe.getDurationString(), 83, 49, 0x555555);
        }
    }

    private static void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(BidsBlocks.dryingRack);
        info.addCatalyst(BidsBlocks.dryingRack);
        return info;
    }

    public class CachedDryingRecipe extends CachedRecipe {

        final ItemStack ingred;
        final ItemStack result;
        final int duration;

        public CachedDryingRecipe(ItemStack ingred, ItemStack result, int duration) {
            this.ingred = ingred.copy();
            this.result = result.copy();
            this.duration = duration;

            if (ingred.getItem() instanceof ItemFoodTFC) {
                Food.setWeight(this.ingred, 160);
                Food.setWeight(this.result, 160);
            }
        }

        public String getDurationString() {
            return String.format("%d %s", duration, StatCollector.translateToLocal("gui.Hours").toLowerCase());
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
