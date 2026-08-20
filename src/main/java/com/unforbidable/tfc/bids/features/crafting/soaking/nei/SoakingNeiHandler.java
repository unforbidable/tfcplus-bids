package com.unforbidable.tfc.bids.features.crafting.soaking.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.features.soaking.SoakingSurfaceRecipe;
import com.unforbidable.tfc.bids.compat.nei.HandlerInfo;
import com.unforbidable.tfc.bids.compat.nei.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.features.crafting.soaking.SoakingConfig;
import com.unforbidable.tfc.bids.features.device.soakingsurface.SoakingSurfaceRegistry;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import com.unforbidable.tfc.bids.util.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

public class SoakingNeiHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "soakingsurface";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
        "textures/gui/nei/gui_soakingsurface.png");

    public static ResourceLocation guiTextureIcons = new ResourceLocation(Tags.MOD_ID,
        "textures/gui/nei/gui_soakingsurface_fluid.png");

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
        if (outputId.equals(HANDLER_ID) && getClass() == SoakingNeiHandler.class) {
            for (SoakingSurfaceRecipe recipe : SoakingSurfaceRegistry.recipes) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedSoakingSurfaceRecipe(input, result, recipe.getFluid(), recipe.getTicks()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (SoakingSurfaceRecipe recipe : SoakingSurfaceRegistry.recipes) {
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getResult(input);
            if (result.getItem() == output.getItem() && result.getItemDamage() == output.getItemDamage()) {
                arecipes.add(new CachedSoakingSurfaceRecipe(input, result, recipe.getFluid(), recipe.getTicks()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (SoakingSurfaceRecipe recipe : SoakingSurfaceRegistry.recipes) {
            if (recipe.matchesInput(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedSoakingSurfaceRecipe(input, result, recipe.getFluid(), recipe.getTicks()));
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
            } else {
                drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                    StatCollector.translateToLocal("gui.Instant"), 83, 49, 0x555555);
            }

            // Because TFC water blocks don't have items
            // they cannot be rendered in guy as neatly as vanilla water blocks
            // the soaking block is always rendered as "water"
            // and the tooltip below shows the fluid name
//            GuiContainerManager.drawItems.renderItemIntoGUI(Minecraft.getMinecraft().fontRenderer, GuiDraw.renderEngine, new ItemStack(Blocks.water), 19, 24);

            FluidStack fluid = ((CachedSoakingSurfaceRecipe) crecipe).fluid;
            if (fluid != null) {
                int i = cycleticks % 32;
                GuiHelper.drawIconCustomColor(guiTextureIcons, 19, 24, 0, i * 16, 16, 16, fluid.getFluid().getColor(fluid));
            }
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
            FluidStack fluid = ((CachedSoakingSurfaceRecipe) irecipe).fluid;
            if (rect.contains(relMouse) && fluid != null) {
                currenttip.add(fluid.getLocalizedName());
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
        final FluidStack fluid;
        final long duration;

        public CachedSoakingSurfaceRecipe(ItemStack ingred, ItemStack result, FluidStack fluid, long duration) {
            this.ingred = ingred.copy();
            this.result = result.copy();
            this.fluid = fluid;
            this.duration = (long) (duration * SoakingConfig.soakingDurationMultiplier / TFC_Time.HOUR_LENGTH);
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
