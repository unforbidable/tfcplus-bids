package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Crafting.*;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingAccessory;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingLidUsage;
import jdk.nashorn.internal.objects.Global;
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
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CookingPotHandler extends TemplateRecipeHandler {

    static final String HANDLER_ID = "cookingpot";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_cookingpot.png");

    @Override
    public String getRecipeName() {
        return "CookingPot";
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
        if (outputId.equals(HANDLER_ID) && getClass() == CookingPotHandler.class) {
            for (CookingRecipe recipe : CookingManager.getRecipes()) {
                CookingRecipe template = new CookingRecipe(
                    recipe.getInputFluidStack(), recipe.getSecondaryInputFluidStack(), recipe.getOutputFluidStack(), recipe.getSecondaryOutputFluidStack(),
                    recipe.getInputItemStack(), recipe.getOutputItemStack(),
                    recipe.getAccessory(), recipe.getLidUsage(), recipe.getMinHeatLevel(), recipe.getMaxHeatLevel(), recipe.getTime(), recipe.isFixedTime()
                );
                arecipes.add(new CachedCookingRecipe(template));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (CookingRecipe recipe : CookingManager.getRecipes()) {
            if (recipe.getOutputItemStack() != null && areItemStacksEqual(recipe.getOutputItemStack(), output)) {
                // Item matches
                arecipes.add(new CachedCookingRecipe(recipe));
            } else if (recipe.getOutputFluidStack() != null && recipe.getOutputFluidStack().isFluidEqual(output)) {
                // Fluid matches
                arecipes.add(new CachedCookingRecipe(recipe));
            } else if (recipe.getOutputFluidStack() != null && recipe.getOutputFluidStack().isFluidEqual(output)) {
                // Secondary fluid matches
                arecipes.add(new CachedCookingRecipe(recipe));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (CookingRecipe recipe : CookingManager.getRecipes()) {
            if (recipe.getInputItemStack() != null && areItemStacksEqual(recipe.getInputItemStack(), ingredient)) {
                ItemStack inputItem = ingredient.copy();
                inputItem.stackSize = recipe.getInputItemStack().stackSize;
                CookingRecipe template = new CookingRecipe(
                    recipe.getInputFluidStack(), recipe.getSecondaryInputFluidStack(), recipe.getOutputFluidStack(), recipe.getSecondaryOutputFluidStack(),
                    inputItem, recipe.getOutputItemStack(),
                    recipe.getAccessory(), recipe.getLidUsage(), recipe.getMinHeatLevel(), recipe.getMaxHeatLevel(), recipe.getTime(), recipe.isFixedTime()
                );
                arecipes.add(new CachedCookingRecipe(template));
            } else if (recipe.getInputFluidStack() != null && recipe.getInputFluidStack().isFluidEqual(ingredient)) {
                arecipes.add(new CachedCookingRecipe(recipe));
            } else if (recipe.getSecondaryInputFluidStack() != null && recipe.getSecondaryInputFluidStack().isFluidEqual(ingredient)) {
                arecipes.add(new CachedCookingRecipe(recipe));
            }
        }
    }

    private boolean areItemStacksEqual(ItemStack one, ItemStack two) {
        return two.isItemEqual(one) && two.getItemDamage() == two.getItemDamage();
    }

    @Override
    public void drawExtras(int recipe) {
        CachedRecipe cr = arecipes.get(recipe);
        if (cr instanceof CachedCookingRecipe) {
            CachedCookingRecipe cookingRecipe = (CachedCookingRecipe) cr;

            if (cookingRecipe.getInputFluids().size() > 0) {
                drawFluidsInRect(cookingRecipe.getInputFluids(), recipeInFluidRect());
            }

            if (cookingRecipe.getOutputFluids().size() > 0) {
                drawFluidsInRect(cookingRecipe.getOutputFluids(), recipeOutFluidRect());
            }

            drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                cookingRecipe.getDuration(), 83, 49, 0x555555);
        }
    }


    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        CachedRecipe cr = arecipes.get(recipe);
        if (cr instanceof CachedCookingRecipe) {
            CachedCookingRecipe cookingRecipe = (CachedCookingRecipe) cr;

            Point mousepos = GuiDraw.getMousePosition();
            Point offset = gui.getRecipePosition(recipe);
            Point relMouse = new Point(mousepos.x - gui.guiLeft - offset.x, mousepos.y - gui.guiTop - offset.y);

            if (cookingRecipe.getInputFluids().size() > 0) {
                FluidStack fluid = cookingRecipe.getInputFluids().getFluidStackInRectAt(recipeInFluidRect(), relMouse);
                if (fluid != null) {
                    currenttip.add(tooltipForFluid(fluid));
                }
            }

            if (cookingRecipe.getOutputFluids().size() > 0) {
                FluidStack fluid = cookingRecipe.getOutputFluids().getFluidStackInRectAt(recipeOutFluidRect(), relMouse);
                if (fluid != null) {
                    currenttip.add(tooltipForFluid(fluid));
                }
            }
        }

        return currenttip;
    }

    @Override
    public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe) {
        if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe")) {
            if (transferFluid(gui, recipe, false)) return true;
        } else if (keyCode == NEIClientConfig.getKeyBinding("gui.usage")) {
            if (transferFluid(gui, recipe, true)) return true;
        }

        return super.keyTyped(gui, keyChar, keyCode, recipe);
    }

    @Override
    public boolean mouseClicked(GuiRecipe gui, int button, int recipe) {
        if (button == 0) {
            if (transferFluid(gui, recipe, false)) return true;
        } else if (button == 1) {
            if (transferFluid(gui, recipe, true)) return true;
        }

        return super.mouseClicked(gui, button, recipe);
    }

    private static String tooltipForFluid(FluidStack fluidStack) {
        return fluidStack.getLocalizedName() + " (" + fluidStack.amount + "mB)";
    }

    private boolean transferFluid(GuiRecipe gui, int recipe, boolean usage) {
        CachedRecipe cr = arecipes.get(recipe);
        if (cr instanceof CachedCookingRecipe) {
            CachedCookingRecipe cookingRecipe = (CachedCookingRecipe) cr;

            Point mousepos = GuiDraw.getMousePosition();
            Point offset = gui.getRecipePosition(recipe);
            Point relMouse = new Point(mousepos.x - gui.guiLeft - offset.x, mousepos.y - gui.guiTop - offset.y);
            ItemStack fluidStack = null;

            if (cookingRecipe.getInputFluids().size() > 0) {
                FluidStack fs = cookingRecipe.getInputFluids().getFluidStackInRectAt(recipeInFluidRect(), relMouse);
                if (fs != null) {
                    fluidStack = getItemStacksForFluid(fs)[0];
                }
            }

            if (cookingRecipe.getOutputFluids().size() > 0) {
                FluidStack fs = cookingRecipe.getOutputFluids().getFluidStackInRectAt(recipeOutFluidRect(), relMouse);
                if (fs != null) {
                    fluidStack = getItemStacksForFluid(fs)[0];
                }
            }

            if (fluidStack != null) {
                return usage
                    ? GuiUsageRecipe.openRecipeGui("item", fluidStack)
                    : GuiCraftingRecipe.openRecipeGui("item", fluidStack);
            }
        }
        return false;
    }

    private static ItemStack[] getItemStacksForFluid(FluidStack fluidStack) {
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

    private static void drawFluidsInRect(FluidStackGroup fluids, Rectangle rect) {
        int pos = 0;
        for (int i = 0; i < fluids.size(); i++) {
            Fluid fluid = fluids.getFluidStack(i).getFluid();
            IIcon fluidIcon = fluid.getIcon();
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            int color = fluid.getColor();
            GL11.glColor4ub((byte) ((color >> 16) & 255), (byte) ((color >> 8) & 255), (byte) (color & 255), (byte) (0xaa & 255));

            int part = Math.round(rect.height * fluids.getRatio(i));
            int y = rect.y + (rect.height - part) - pos;
            pos += part;

            GuiDraw.gui.drawTexturedModelRectFromIcon(rect.x, y, fluidIcon, rect.width, part);
        }
    }

    private static Rectangle recipeInFluidRect() {
        return new Rectangle(18, 8, 8, 50);
    }

    private static Rectangle recipeOutFluidRect() {
        return new Rectangle(141, 8, 8, 50);
    }

    private static void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    public class CachedCookingRecipe extends CachedRecipe {
        private final ItemStack inputItemStack;
        private final ItemStack outputItemStack;
        private final FluidStackGroup inputFluids;
        private final FluidStackGroup outputFluids;

        private final boolean lid;
        private final boolean steamingMesh;
        private final boolean heat;
        private String duration;

        public CachedCookingRecipe(CookingRecipe template) {
            CookingRecipeCraftingResult result = template.getCraftingResult(template);

            inputFluids = new FluidStackGroup(template.getInputFluidStack(), template.getSecondaryInputFluidStack());
            outputFluids = new FluidStackGroup(result.getOutputFluidStack(), result.getSecondaryOutputFluidStack());

            inputItemStack = template.getInputItemStack() != null ? template.getInputItemStack().copy() : null;
            outputItemStack = result.getOutputItemStack() != null ? result.getOutputItemStack().copy() : null;

            int runs = 1;
            if ((inputItemStack == null || inputItemStack.getItem() instanceof ItemFoodTFC) &&
                (outputItemStack == null || outputItemStack.getItem() instanceof ItemFoodTFC)) {
                if (inputFluids.getTotalAmount() > 0) {
                    runs = (int) Math.floor(5000f / inputFluids.getTotalAmount());
                } else if (outputFluids.getTotalAmount() > 0) {
                    runs = (int)Math.floor(5000f / outputFluids.getTotalAmount());
                } else if (inputItemStack != null && inputItemStack.getItem() instanceof ItemFoodTFC) {
                    runs = (int)Math.floor(160f / Food.getWeight(inputItemStack));
                } else if (outputItemStack != null && outputItemStack.getItem() instanceof ItemFoodTFC) {
                    runs = (int)Math.floor(160f / Food.getWeight(outputItemStack));
                }
            }

            if (runs > 0) {
                if (inputItemStack != null && inputItemStack.getItem() instanceof ItemFoodTFC) {
                    Food.setWeight(inputItemStack, Food.getWeight(inputItemStack) * runs);
                }

                if (outputItemStack != null && outputItemStack.getItem() instanceof ItemFoodTFC) {
                    Food.setWeight(outputItemStack, Food.getWeight(outputItemStack) * runs);
                }

                inputFluids.multiplyAmounts(runs);
                outputFluids.multiplyAmounts(runs);
            }

            lid = template.getLidUsage() != null && template.getLidUsage() == EnumCookingLidUsage.ON;
            steamingMesh = template.getAccessory() != null && template.getAccessory() == EnumCookingAccessory.STEAMING_MESH;
            heat = template.getMinHeatLevel() != null && template.getMinHeatLevel() != EnumCookingHeatLevel.NONE;

            if (template.getTime() > 0) {
                float hours = template.getTime() / (float)TFC_Time.HOUR_LENGTH;
                duration = String.format("%.2f %s", hours, StatCollector.translateToLocal("gui.Hours").toLowerCase());
            } else {
                duration = "Instant";
            }
        }

        @Override
        public PositionedStack getResult() {
            if (outputItemStack != null) {
                return new PositionedStack(outputItemStack, 112, 25);
            }

            return null;
        }

        @Override
        public PositionedStack getIngredient() {
            if (inputItemStack != null) {
                return new PositionedStack(inputItemStack, 39, 25);
            }

            return null;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return super.getIngredients();
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            List<PositionedStack> list = new ArrayList<PositionedStack>();

            List<Object> topItems = new ArrayList<Object>();
            if (isHeat()) {
                topItems.add(new ItemStack(Blocks.fire, 1, 1));
            }

            if (isLid()) {
                topItems.add(new ItemStack(BidsBlocks.cookingPotLid, 1, 1));
            }

            if (isSteamingMesh()) {
                List<ItemStack> steamingMeshes = OreDictionary.getOres("itemCookingPotAccessorySteamingMesh", false);
                final int i = cycleticks % (20 * steamingMeshes.size());
                topItems.add(steamingMeshes.get(i / 20));
            }

            int x = 84 - topItems.size() * 10;
            for (Object o: topItems) {
                list.add(new PositionedStack(o, x, 5));
                x += 20;
            }

            return list;
        }

        public FluidStackGroup getInputFluids() {
            return inputFluids;
        }

        public FluidStackGroup getOutputFluids() {
            return outputFluids;
        }

        public boolean isLid() {
            return lid;
        }

        public boolean isSteamingMesh() {
            return steamingMesh;
        }

        public boolean isHeat() {
            return heat;
        }

        public String getDuration() {
            return duration;
        }
    }

    public static class FluidStackGroup {
        private final List<FluidStack> fluidStacks = new ArrayList<FluidStack>();
        private int totalAmount;

        public FluidStackGroup(FluidStack... fluidStacks) {
            int amount = 0;
            for (int i = 0; i < fluidStacks.length; i++) {
                if (fluidStacks[i] != null) {
                    this.fluidStacks.add(fluidStacks[i].copy());
                    amount += fluidStacks[i].amount;
                }
            }
            totalAmount = amount;
        }

        public int size() {
            return fluidStacks.size();
        }

        public float getRatio(int index) {
            return fluidStacks.get(index).amount / (float)totalAmount;
        }

        public FluidStack getFluidStack(int index) {
            return fluidStacks.get(index);
        }

        public FluidStack getFluidStackInRectAt(Rectangle rect, Point point) {
            if (rect.contains(point)) {
                if (size() == 1) {
                    return fluidStacks.get(0);
                } else {
                    int pos = 0;
                    for (int i = 0; i < size(); i++) {
                        float ratio = getRatio(i);
                        int part = Math.round(ratio * rect.height);
                        int y = rect.y + (rect.height - part) - pos;
                        pos += part;
                        Rectangle fluidRect = new Rectangle(rect.x, y, rect.width, part);
                        if (fluidRect.contains(point)) {
                            return fluidStacks.get(i);
                        }
                    }
                }
            }

            return null;
        }

        public void multiplyAmounts(int runs) {
            for (FluidStack fs : fluidStacks) {
                fs.amount *= runs;
            }
            totalAmount *= runs;
        }

        public int getTotalAmount() {
            return totalAmount;
        }
    }

}
