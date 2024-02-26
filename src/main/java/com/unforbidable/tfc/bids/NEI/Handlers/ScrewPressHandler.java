package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Crafting.ScrewPressManager;
import com.unforbidable.tfc.bids.api.Crafting.ScrewPressRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ScrewPressHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "screwpress";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_screwpress.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.ScrewPress");
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
        transferRects.add(new RecipeTransferRect(new Rectangle(61, 23, 24, 18), HANDLER_ID));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(HANDLER_ID) && getClass() == ScrewPressHandler.class) {
            for (ScrewPressRecipe recipe : ScrewPressManager.getRecipes()) {
                final ItemStack input = recipe.getInput().copy();
                final FluidStack result = recipe.getFluidCraftingResult().copy();
                if ((input.getItem() instanceof ItemFood)) {
                    ItemFoodTFC.createTag(input);
                }
                arecipes.add(new CachedScrewPressRecipe(input, result, recipe.getResistance()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (ScrewPressRecipe recipe : ScrewPressManager.getRecipes()) {
            final ItemStack input = recipe.getInput().copy();
            final FluidStack result = recipe.getFluidCraftingResult().copy();
            if (result.isFluidEqual(output)) {
                arecipes.add(new CachedScrewPressRecipe(input, result, recipe.getResistance()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (ScrewPressRecipe recipe : ScrewPressManager.getRecipes()) {
            if (recipe.matches(ingredient)) {
                final ItemStack input = ingredient.copy();
                input.stackSize = recipe.getInput().stackSize;
                if (input.getItem() instanceof ItemFoodTFC) {
                    input.setTagCompound(recipe.getInput().getTagCompound());
                }

                final FluidStack result = recipe.getFluidCraftingResult().copy();
                arecipes.add(new CachedScrewPressRecipe(input, result, recipe.getResistance()));
            }
        }
    }

    @Override
    public void drawExtras(int recipe) {
        CachedRecipe crecipe = arecipes.get(recipe);
        if (crecipe instanceof CachedScrewPressRecipe) {
            CachedScrewPressRecipe cachedScrewPressRecipe = (CachedScrewPressRecipe) crecipe;

            if (cachedScrewPressRecipe.getFluidResult() != null) {
                drawFluidInRect(cachedScrewPressRecipe.getFluidResult().getFluid(), recipeOutFluidRect());
            }

            drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                cachedScrewPressRecipe.getResistanceString(), 73, 49, 0x555555);
        }
    }

    private static void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        CachedRecipe irecipe = arecipes.get(recipe);
        if (irecipe instanceof CachedScrewPressRecipe) {
            Point mousepos = GuiDraw.getMousePosition();
            Point offset = gui.getRecipePosition(recipe);
            Point relMouse = new Point(mousepos.x - gui.guiLeft - offset.x, mousepos.y - gui.guiTop - offset.y);
            if (recipeOutFluidRect().contains(relMouse) && (((CachedScrewPressRecipe) irecipe).getFluidResult() != null)) {
                currenttip.add(tooltipForFluid(((CachedScrewPressRecipe) irecipe).getFluidResult()));
            }
        }
        return currenttip;
    }

    @Override
    public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe) {
        if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe")) {
            if (transferFluid(gui, recipe, false))
                return true;
        } else if (keyCode == NEIClientConfig.getKeyBinding("gui.usage")) {
            if (transferFluid(gui, recipe, true))
                return true;
        }

        return super.keyTyped(gui, keyChar, keyCode, recipe);
    }

    @Override
    public boolean mouseClicked(GuiRecipe gui, int button, int recipe) {
        if (button == 0)
        {
            if (transferFluid(gui, recipe, false)) return true;
        }
        else if (button == 1)
        {
            if (transferFluid(gui, recipe, true)) return true;
        }

        return super.mouseClicked(gui, button, recipe);
    }

    private static String tooltipForFluid(FluidStack fluidStack) {
        return fluidStack.getLocalizedName() + " (" + fluidStack.amount + "mB)";
    }

    private boolean transferFluid(GuiRecipe gui, int recipe, boolean usage) {
        CachedRecipe crecipe = arecipes.get(recipe);
        if (crecipe instanceof CachedScrewPressRecipe)
        {
            Point mousepos = GuiDraw.getMousePosition();
            Point offset = gui.getRecipePosition(recipe);
            Point relMouse = new Point(mousepos.x - gui.guiLeft - offset.x, mousepos.y - gui.guiTop - offset.y);
            ItemStack fluidStack = null;
            if (recipeOutFluidRect().contains(relMouse) && (((CachedScrewPressRecipe) crecipe).getFluidResult() != null)) {
                fluidStack = getItemStacksForFluid(((CachedScrewPressRecipe) crecipe).getFluidResult())[0];
            }
            if (fluidStack != null) {
                return usage
                    ? GuiUsageRecipe.openRecipeGui("item", fluidStack)
                    : GuiCraftingRecipe.openRecipeGui("item", fluidStack);
            }
        }
        return false;
    }

    private static ItemStack[] getItemStacksForFluid(FluidStack fluidStack)
    {
        if (fluidStack == null) return null;

        List<ItemStack> itemStacks = new ArrayList<ItemStack>();
        for (FluidContainerRegistry.FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData())
        {
            if (data.fluid.isFluidEqual(fluidStack))
            {
                ItemStack itemStack = data.filledContainer.copy();
                int cap = FluidContainerRegistry.getContainerCapacity(data.fluid, data.emptyContainer);
                if (cap == 0) itemStack.stackSize = 0;
                else itemStack.stackSize = fluidStack.amount / cap;
                itemStacks.add(itemStack);
            }
        }
        if (itemStacks.size() == 0)
        {
            ItemStack itemStack = new ItemStack(fluidStack.getFluid().getBlock(), fluidStack.amount / FluidContainerRegistry.BUCKET_VOLUME);
            if (itemStack.getItem() == null)
            {
                itemStack = new ItemStack(Blocks.sponge, itemStack.stackSize).setStackDisplayName(fluidStack.getLocalizedName());
                itemStack.getTagCompound().setString("FLUID", fluidStack.getFluid().getName());
            }
            itemStacks.add(itemStack);
        }
        return itemStacks.toArray(new ItemStack[itemStacks.size()]);
    }

    private static void drawFluidInRect(Fluid fluid, Rectangle rect) {
        IIcon fluidIcon = fluid.getIcon();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        int color = fluid.getColor();
        GL11.glColor4ub((byte) ((color >> 16) & 255), (byte) ((color >> 8) & 255), (byte) (color & 255), (byte) (0xaa & 255));
        GuiDraw.gui.drawTexturedModelRectFromIcon(rect.x, rect.y, fluidIcon, rect.width, rect.height);
    }

    private static Rectangle recipeOutFluidRect() {
        return new Rectangle(127, 7, 8, 50);
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(BidsBlocks.screwPressBarrel, 4);
        info.addCatalyst(BidsBlocks.screwPressRackBottom);
        info.addCatalyst(BidsBlocks.screwPressRackBridge);
        info.addCatalyst(BidsBlocks.screwPressBarrel);
        info.addCatalyst(BidsBlocks.screwPressDisc);
        info.addCatalyst(BidsBlocks.screwPressLever);
        return info;
    }

    public class CachedScrewPressRecipe extends CachedRecipe {

        final ItemStack ingred;
        final FluidStack result;
        final float resistance;

        public CachedScrewPressRecipe(ItemStack ingred, FluidStack result, float resistance) {
            this.ingred = ingred;
            this.result = result;
            this.resistance = resistance;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public PositionedStack getIngredient() {
            return new PositionedStack(ingred, 29, 24);
        }

        public FluidStack getFluidResult() {
            return result;
        }

        public String getResistanceString() {
            return String.format("%s: %d%%", StatCollector.translateToLocal("gui.Resistance"), Math.round(resistance * 100));
        }

    }

}
