package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.Crafting.SoakingSurfaceManager;
import com.unforbidable.tfc.bids.api.Crafting.SoakingSurfaceRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SoakingSurfaceHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "soakingsurface";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_soakingsurface.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.SoakingSurface");
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
        if (outputId.equals(HANDLER_ID) && getClass() == SoakingSurfaceHandler.class) {
            for (SoakingSurfaceRecipe recipe : SoakingSurfaceManager.getRecipes()) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                List<ItemStack> blocks = OreDictionary.getOres(recipe.getFluidBlockOreName(), false);
                arecipes.add(new CachedSoakingSurfaceRecipe(input, result, blocks, recipe.getHours()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (SoakingSurfaceRecipe recipe : SoakingSurfaceManager.getRecipes()) {
            final ItemStack output2 = output.copy();
            output2.stackSize = 1;
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getResult(input);
            if (ItemStack.areItemStacksEqual(result, output2)) {
                List<ItemStack> blocks = OreDictionary.getOres(recipe.getFluidBlockOreName(), false);
                arecipes.add(new CachedSoakingSurfaceRecipe(input, output2, blocks, recipe.getHours()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (SoakingSurfaceRecipe recipe : SoakingSurfaceManager.getRecipes()) {
            if (recipe.matchesInput(ingredient)) {
                final ItemStack input = new ItemStack(ingredient.getItem(), 1, ingredient.getItemDamage());
                final ItemStack result = recipe.getResult(input);
                List<ItemStack> blocks = OreDictionary.getOres(recipe.getFluidBlockOreName(), false);
                arecipes.add(new CachedSoakingSurfaceRecipe(input, result, blocks, recipe.getHours()));
            } else if (recipe.matchesSurface(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                List<ItemStack> blocks = new ArrayList<ItemStack>();
                blocks.add(ingredient);
                arecipes.add(new CachedSoakingSurfaceRecipe(input, result, blocks, recipe.getHours()));
            }
        }
    }

    @Override
    public void drawExtras(int recipe) {
        CachedRecipe crecipe = arecipes.get(recipe);
        if (crecipe instanceof CachedSoakingSurfaceRecipe) {
            final CachedSoakingSurfaceRecipe cachedSoakingSurfaceRecipe = (CachedSoakingSurfaceRecipe) crecipe;

            if (cachedSoakingSurfaceRecipe.duration > 0) {
                drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                    cachedSoakingSurfaceRecipe.getDurationString(), 83, 49, 0x555555);
            }

            // Because TFC water blocks don't have items
            // they cannot be rendered in guy as neatly as vanilla water blocks
            // the soaking block is always rendered as "water"
            // and only the tooltip below shows the actual block name(s)
            GuiContainerManager.drawItems.renderItemIntoGUI(Minecraft.getMinecraft().fontRenderer, GuiDraw.renderEngine, new ItemStack(Blocks.water), 19, 24);
        }
    }

    private static void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        CachedRecipe irecipe = arecipes.get(recipe);
        if (irecipe instanceof CachedSoakingSurfaceRecipe) {
            Point mousepos = GuiDraw.getMousePosition();
            Point offset = gui.getRecipePosition(recipe);
            Point relMouse = new Point(mousepos.x - gui.guiLeft - offset.x, mousepos.y - gui.guiTop - offset.y);
            Rectangle rect = new Rectangle(19, 24, 16, 16);
            List<ItemStack> blocks = ((CachedSoakingSurfaceRecipe) irecipe).blocks;
            if (rect.contains(relMouse) && blocks.size() > 0) {
                int i = (cycleticks / 20) % (blocks.size());
                currenttip.add(blocks.get(i).getDisplayName());
            }
        }
        return currenttip;
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        return new HandlerInfo(Blocks.water, 0);
    }

    public class CachedSoakingSurfaceRecipe extends CachedRecipe {

        final ItemStack ingred;
        final ItemStack result;
        final List<ItemStack> blocks;
        final long duration;

        public CachedSoakingSurfaceRecipe(ItemStack ingred, ItemStack result, List<ItemStack> blocks, long duration) {
            this.ingred = ingred.copy();
            this.result = result.copy();
            this.blocks = blocks;
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
            return String.format("%d %s", duration, StatCollector.translateToLocal("gui.Hours").toLowerCase());
        }
    }

}
