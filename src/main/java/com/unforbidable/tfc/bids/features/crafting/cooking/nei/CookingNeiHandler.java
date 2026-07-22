package com.unforbidable.tfc.bids.features.crafting.cooking.nei;

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
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingAccessory;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingLidUsage;
import com.unforbidable.tfc.bids.api.features.cooking.CookingOreRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeCraftingResult;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeInputTemplate;
import com.unforbidable.tfc.bids.compat.nei.HandlerInfo;
import com.unforbidable.tfc.bids.compat.nei.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.compat.nei.NeiHelper;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingHelper;
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

public class CookingNeiHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "cookingpot";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_cookingpot.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Cooking");
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
        if (outputId.equals(HANDLER_ID) && getClass() == CookingNeiHandler.class) {
            for (CookingRecipe recipe : CookingRegistry.recipes) {
                arecipes.add(new CachedCookingRecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (CookingRecipe recipe : CookingRegistry.recipes) {
            if (recipe.getOutputItemStack() != null && areItemStacksEqual(recipe.getOutputItemStack(), output)) {
                // Item matches
                arecipes.add(new CachedCookingRecipe(recipe));
            } else if (recipe.getOutputFluidStack() != null && NeiHelper.isFluidEqual(recipe.getOutputFluidStack(), output)) {
                // Fluid matches
                arecipes.add(new CachedCookingRecipe(recipe));
            } else if (recipe.getSecondaryOutputFluidStack() != null && NeiHelper.isFluidEqual(recipe.getSecondaryOutputFluidStack(), output)) {
                // Secondary fluid matches
                arecipes.add(new CachedCookingRecipe(recipe));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        // TODO match cooking mixes in bowls

        for (CookingRecipe recipe : CookingRegistry.recipes) {
            if (recipe.matchesInput(ingredient)) {
                ItemStack inputItem = ingredient.copy();

                if (recipe instanceof CookingOreRecipe) {
                    CookingRecipe flatRecipe = CookingHelper.flattenCookingOreRecipe((CookingOreRecipe)recipe, ingredient);

                    inputItem.stackSize = flatRecipe.getInputItemStack().stackSize;
                    if (inputItem.getItem() instanceof ItemFoodTFC) {
                        Food.setWeight(inputItem, Food.getWeight(flatRecipe.getInputItemStack()));
                    }
                } else {
                    inputItem.stackSize = recipe.getInputItemStack().stackSize;
                    if (inputItem.getItem() instanceof ItemFoodTFC) {
                        Food.setWeight(inputItem, Food.getWeight(recipe.getInputItemStack()));
                    }
                }

                CookingRecipe sizedRecipe = new CookingRecipe(
                    recipe.getInputFluidStack(), recipe.getSecondaryInputFluidStack(), recipe.getOutputFluidStack(), recipe.getSecondaryOutputFluidStack(),
                    inputItem, recipe.getOutputItemStack(),
                    recipe.getAccessory(), recipe.getLidUsage(), recipe.getMinHeatLevel(), recipe.getMaxHeatLevel(), recipe.getTime(), recipe.isFixedTime()
                );
                arecipes.add(new CachedCookingRecipe(sizedRecipe));
            } else if (recipe.getInputFluidStack() != null && NeiHelper.isFluidEqual(recipe.getInputFluidStack(), ingredient)) {
                arecipes.add(new CachedCookingRecipe(recipe));
            } else if (recipe.getSecondaryInputFluidStack() != null && NeiHelper.isFluidEqual(recipe.getSecondaryInputFluidStack(), ingredient)) {
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
            int color = fluid.getColor(fluids.getFluidStack(i));
            GL11.glColor4ub((byte) ((color >> 16) & 255), (byte) ((color >> 8) & 255), (byte) (color & 255), (byte) (0xaa & 255));

            int part = Math.round(rect.height * fluids.getRatio(i));
            int y = Math.max(rect.y + (rect.height - part) - pos, rect.y);
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

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(BidsBlocks.cookingPot, 9);
        info.addCatalyst(BidsBlocks.cookingPot, 1);
        info.addCatalyst(BidsBlocks.cookingPotLid, 1);
        info.addCatalyst(BidsItems.steamingMeshCloth);
        return info;
    }

    public class CachedCookingRecipe extends CachedRecipe {
        private final List<ItemStack> inputItemStacks;
        private final ItemStack outputItemStack;
        private final FluidStackGroup inputFluids;
        private final FluidStackGroup outputFluids;

        private final boolean lid;
        private final boolean steamingMesh;
        private final boolean heat;
        private String duration;

        public CachedCookingRecipe(CookingRecipe recipe) {
            CookingRecipeInputTemplate template = new CookingRecipeInputTemplate(
                recipe.getInputFluidStack(), recipe.getSecondaryInputFluidStack(),
                recipe instanceof CookingOreRecipe ? recipe.getInputItemStacks().get(0) : recipe.getInputItemStack(),
                recipe.getAccessory(), recipe.getLidUsage(), recipe.getMinHeatLevel(), recipe.getMaxHeatLevel()
            );

            CookingRecipeCraftingResult result = recipe.getCraftingResult(template);

            inputFluids = new FluidStackGroup(recipe.getInputFluidStack(), recipe.getSecondaryInputFluidStack());
            outputFluids = new FluidStackGroup(result.getOutputFluidStack(), result.getSecondaryOutputFluidStack());

            inputItemStacks = recipe.getInputItemStacks();
            outputItemStack = result.getOutputItemStack() != null ? result.getOutputItemStack().copy() : null;

            int runs = getRuns();
            if (runs > 0) {
                for (ItemStack input : inputItemStacks) {
                    if (input.getItem() instanceof ItemFoodTFC) {
                        Food.setWeight(input, Food.getWeight(input) * runs);
                    }
                }

                if (outputItemStack != null && outputItemStack.getItem() instanceof ItemFoodTFC) {
                    Food.setWeight(outputItemStack, Food.getWeight(outputItemStack) * runs);
                }

                inputFluids.multiplyAmounts(runs);
                outputFluids.multiplyAmounts(runs);
            }

            lid = recipe.getLidUsage() != null && recipe.getLidUsage() == CookingLidUsage.ON;
            steamingMesh = recipe.getAccessory() != null && recipe.getAccessory() == CookingAccessory.STEAMING_MESH;
            heat = recipe.getMinHeatLevel() != null && recipe.getMinHeatLevel() != CookingHeatLevel.NONE;

            if (recipe.getTime() > 0) {
                float hours = recipe.getTime() / (float)TFC_Time.HOUR_LENGTH;

                if (!inputItemStacks.isEmpty() && inputItemStacks.get(0).getItem() instanceof ItemFoodTFC) {
                    // For input items that are food, the total time is determined by the number of runs
                    hours *= runs;
                }

                int roundHours = Math.round(hours);
                if (roundHours == hours) {
                    duration = String.format("%d %s", roundHours, StatCollector.translateToLocal("gui.Hours").toLowerCase());
                } else {
                    duration = String.format("%.2f %s", hours, StatCollector.translateToLocal("gui.Hours").toLowerCase());
                }
            } else {
                duration = StatCollector.translateToLocal("gui.Instant");
            }
        }

        private int getRuns() {
            double runs = 0;
            ItemStack inputItemStack = !inputItemStacks.isEmpty() ? inputItemStacks.get(0) : null;
            if ((inputItemStack == null || inputItemStack.getItem() instanceof ItemFoodTFC) &&
                (outputItemStack == null || outputItemStack.getItem() instanceof ItemFoodTFC)) {
                if (inputFluids.getTotalAmount() > 0) {
                    runs = 5000f / inputFluids.getTotalAmount();
                } else if (outputFluids.getTotalAmount() > 0) {
                    runs = 5000f / outputFluids.getTotalAmount();
                } else if (inputItemStack != null && inputItemStack.getItem() instanceof ItemFoodTFC) {
                    runs = 160f / Food.getWeight(inputItemStack);
                } else if (outputItemStack != null && outputItemStack.getItem() instanceof ItemFoodTFC) {
                    runs = 160f / Food.getWeight(outputItemStack);
                }
            }

            if (runs > 0) {
                int flooredRuns = (int) Math.floor(runs);
                if (flooredRuns == runs) {
                    return flooredRuns;
                } else {
                    // if runs don't fit 5000mB or 160oz perfectly
                    // use factor of 10 that is lower and that is closest to the amount
                    int n = 1000;
                    while (n > 1) {
                        if (runs > n) {
                            return n;
                        }
                        n = n / 10;
                    }
                }
            }

            return 1;
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
            return super.getIngredient();
        }

        @Override
        public List<PositionedStack> getIngredients() {
            if (!inputItemStacks.isEmpty()) {
                int n = cycleticks % (20 * inputItemStacks.size());
                return Collections.singletonList(new PositionedStack(inputItemStacks.get(n / 20), 39, 25));
            } else {
                return Collections.emptyList();
            }
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            List<PositionedStack> list = new ArrayList<>();

            List<Object> topItems = new ArrayList<>();
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
