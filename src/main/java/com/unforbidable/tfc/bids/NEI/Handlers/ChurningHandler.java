package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.ChurningManager;
import com.unforbidable.tfc.bids.api.Crafting.ChurningRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
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

public class ChurningHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "churning";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_churning.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Churning");
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
        if (outputId.equals(HANDLER_ID) && getClass() == ChurningHandler.class) {
            for (ChurningRecipe recipe : ChurningManager.getRecipes()) {
                final FluidStack input = recipe.getInput().copy();
                input.amount = 4000;
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedChurningRecipe(input, result, recipe.getTotalDuration(input) * BidsOptions.Churning.churningDurationMultiplier));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (ChurningRecipe recipe : ChurningManager.getRecipes()) {
            final FluidStack input = recipe.getInput().copy();
            input.amount = 4000;
            final ItemStack result = recipe.getResult(input);
            if (ItemStack.areItemStacksEqual(result, output) || result.getItem() instanceof IFood && result.getItem() == output.getItem()) {
                arecipes.add(new CachedChurningRecipe(input, result, recipe.getTotalDuration(input) * BidsOptions.Churning.churningDurationMultiplier));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (ChurningRecipe recipe : ChurningManager.getRecipes()) {
            FluidStack input = FluidContainerRegistry.getFluidForFilledItem(ingredient);
            if (input != null && recipe.matches(input)) {
                input.amount = 4000;
                final ItemStack result = recipe.getResult(input);
                arecipes.add(new CachedChurningRecipe(input, result, recipe.getTotalDuration(input) * BidsOptions.Churning.churningDurationMultiplier));
            }
        }
    }

    @Override
    public void drawExtras(int recipe) {
        CachedRecipe crecipe = arecipes.get(recipe);
        if (crecipe instanceof CachedChurningRecipe) {
            final CachedChurningRecipe cachedChurningRecipe = (CachedChurningRecipe) crecipe;

            if (cachedChurningRecipe.getFluidIngredient() != null) {
                drawFluidInRect(cachedChurningRecipe.getFluidIngredient().getFluid(), recipeInFluidRect());
            }

            drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                    cachedChurningRecipe.getDurationString(), 93, 49, 0x555555);
        }
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

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        CachedRecipe irecipe = arecipes.get(recipe);
        if (irecipe instanceof CachedChurningRecipe) {
            Point mousepos = GuiDraw.getMousePosition();
            Point offset = gui.getRecipePosition(recipe);
            Point relMouse = new Point(mousepos.x - gui.guiLeft - offset.x, mousepos.y - gui.guiTop - offset.y);
            if (recipeInFluidRect().contains(relMouse) && (((CachedChurningRecipe) irecipe).getFluidIngredient() != null)) {
                currenttip.add(tooltipForFluid(((CachedChurningRecipe) irecipe).getFluidIngredient()));
            }
        }
        return currenttip;
    }

    private static String tooltipForFluid(FluidStack fluidStack) {
        return fluidStack.getLocalizedName() + " (" + fluidStack.amount + "mB)";
    }
    private boolean transferFluid(GuiRecipe gui, int recipe, boolean usage) {
        CachedRecipe crecipe = arecipes.get(recipe);
        if (crecipe instanceof CachedChurningRecipe)
        {
            Point mousepos = GuiDraw.getMousePosition();
            Point offset = gui.getRecipePosition(recipe);
            Point relMouse = new Point(mousepos.x - gui.guiLeft - offset.x, mousepos.y - gui.guiTop - offset.y);
            ItemStack fluidStack = null;
            if (recipeInFluidRect().contains(relMouse) && (((CachedChurningRecipe) crecipe).getFluidIngredient() != null)) {
                fluidStack = getItemStacksForFluid(((CachedChurningRecipe) crecipe).getFluidIngredient())[0];
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

    private static Rectangle recipeInFluidRect() {
        return new Rectangle(43, 7, 8, 50);
    }

    private static void drawFluidInRect(Fluid fluid, Rectangle rect) {
        IIcon fluidIcon = fluid.getIcon();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        int color = fluid.getColor();
        GL11.glColor4ub((byte) ((color >> 16) & 255), (byte) ((color >> 8) & 255), (byte) (color & 255), (byte) (0xaa & 255));
        GuiDraw.gui.drawTexturedModelRectFromIcon(rect.x, rect.y, fluidIcon, rect.width, rect.height);
    }

    private static void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(TFCItems.waterskinEmpty);
        info.addCatalyst(TFCItems.waterskinEmpty);
        return info;
    }

    public class CachedChurningRecipe extends CachedRecipe {

        final FluidStack ingred;
        final ItemStack result;
        final float duration;

        public CachedChurningRecipe(FluidStack ingred, ItemStack result, float duration) {
            this.ingred = ingred.copy();
            this.result = result.copy();
            this.duration = duration;

            if (result.getItem() instanceof ItemFoodTFC) {
                Food.setWeight(this.result, 160);
            }
        }

        public String getDurationString() {
            return String.format("%.1f %s", duration / 1000, StatCollector.translateToLocal("gui.Hours").toLowerCase());
        }

        @Override
        public PositionedStack getResult() {
            return new PositionedStack(result, 112, 24);
        }

        public FluidStack getFluidIngredient() {
            return ingred;
        }

    }

}
