package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Crafting.CarvingManager;
import com.unforbidable.tfc.bids.api.Crafting.CarvingRecipe;
import com.unforbidable.tfc.bids.api.Crafting.CarvingRecipePattern;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class CarvingHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "carving";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_carving.png");
    public static ResourceLocation bitsTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_carving_bits.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Carving");
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
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public void drawBackground(int recipe)
    {
        GL11.glColor4f(1, 1, 1, 1);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 135, 120);
    }

    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);

        CachedRecipe crecipe = arecipes.get(recipe);
        if (crecipe instanceof CachedCarvingRecipe) {
            final CachedCarvingRecipe cachedCarvingRecipe = (CachedCarvingRecipe) crecipe;
            final CarvingRecipePattern pattern = cachedCarvingRecipe.pattern;

            GuiDraw.changeTexture(bitsTexture);

            int state = (cycleticks / 12) % 8;
            int maxY = Math.min(4, state + 1);

            for (int y = 0; y < maxY; y++) {
                for (int x = 3; x >= 0; x--) {
                    for (int z = 0; z < 4; z++) {
                        boolean isCarved = pattern.testBit(x, y, z);

                        int texY = 0;

                        if (isCarved) {
                            texY += 16;
                        }

                        if (state >= 4) {
                            texY += 32;
                        }

                        drawPatternBit(x, 3 - y, z, texY);
                    }
                }
            }
        }

    }

    private void drawPatternBit(int x, int y, int z, int texY) {
        int posX = x * 6 + z * 6;
        int posY = z * 3 - x * 3 + y * 6;
        GuiDraw.drawTexturedModalRect(57 + posX, 63 + posY, 0, texY, 16, 16);
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(71, 23, 24, 18), HANDLER_ID));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(HANDLER_ID) && getClass() == CarvingHandler.class) {
            for (CarvingRecipe recipe : CarvingManager.getRecipes()) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getCraftingResult();
                arecipes.add(new CachedCarvingRecipe(input, result, recipe.getPattern()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (CarvingRecipe recipe : CarvingManager.getRecipes()) {
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getCraftingResult();
            if (ItemStack.areItemStacksEqual(result, output)) {
                arecipes.add(new CachedCarvingRecipe(input, output, recipe.getPattern()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (CarvingRecipe recipe : CarvingManager.getRecipes()) {
            if (recipe.matchCarving(ingredient)) {
                final ItemStack input = ingredient;
                final ItemStack result = recipe.getCraftingResult();
                arecipes.add(new CachedCarvingRecipe(input, result, recipe.getPattern()));
            }
        }
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(BidsItems.sedStoneAdze);
        for (ItemStack is : OreDictionary.getOres("itemAdze", false)) {
            info.addCatalyst(is.getItem());
        }
        return info;
    }

    public class CachedCarvingRecipe extends CachedRecipe {

        final ItemStack ingred;
        final ItemStack result;
        final CarvingRecipePattern pattern;

        public CachedCarvingRecipe(ItemStack ingred, ItemStack result, CarvingRecipePattern pattern) {
            this.ingred = ingred.copy();
            this.result = result.copy();
            this.pattern = pattern;
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
