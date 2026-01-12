package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.Common.GuiHelper;
import com.unforbidable.tfc.bids.Core.Woodworking.Plans.PlanInstance;
import com.unforbidable.tfc.bids.Core.Woodworking.Workspace.WorkspaceClient;
import com.unforbidable.tfc.bids.Gui.GuiWoodworking;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.Crafting.WoodworkingManager;
import com.unforbidable.tfc.bids.api.Crafting.WoodworkingRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingMaterial;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingPlan;
import com.unforbidable.tfc.bids.api.WoodworkingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WoodworkingHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "woodworking";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_woodworking.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Woodworking");
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
        transferRects.add(new RecipeTransferRect(new Rectangle(115, 38, 18, 28), HANDLER_ID));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(HANDLER_ID) && getClass() == WoodworkingHandler.class) {
            for (WoodworkingRecipe recipe : WoodworkingManager.getRecipes()) {
                final List<ItemStack> input = recipe.getIngredients();
                if (input.size() > 0) {
                    final ItemStack result = recipe.getResult(input.get(0));
                    WorkspaceClient workspaceClient = createWorkspaceClientForRecipe(recipe);
                    if (workspaceClient != null) {
                        arecipes.add(new CachedWoodworkingRecipe(input, result, workspaceClient));
                    }
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (WoodworkingRecipe recipe : WoodworkingManager.getRecipes()) {
            final List<ItemStack> input = recipe.getIngredients();
            if (input.size() > 0) {
                final ItemStack result = recipe.getResult(input.get(0));
                output.stackSize = result.stackSize;
                if (ItemStack.areItemStacksEqual(result, output)) {
                    WorkspaceClient workspaceClient = createWorkspaceClientForRecipe(recipe);
                    if (workspaceClient != null) {
                        arecipes.add(new CachedWoodworkingRecipe(input, result, workspaceClient));
                    }
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (WoodworkingRecipe recipe : WoodworkingManager.getRecipes()) {
            if (recipe.matches(ingredient)) {
                final List<ItemStack> input = recipe.getIngredients();
                if (input.size() > 0) {
                    final ItemStack result = recipe.getResult(input.get(0));
                    WorkspaceClient workspaceClient = createWorkspaceClientForRecipe(recipe);
                    if (workspaceClient != null) {
                        arecipes.add(new CachedWoodworkingRecipe(input, result, workspaceClient));
                    }
                }
            }
        }
    }

    private WorkspaceClient createWorkspaceClientForRecipe(WoodworkingRecipe recipe) {
        IWoodworkingMaterial material = findMaterial(recipe.getIngredients());

        IWoodworkingPlan plan = WoodworkingRegistry.getPlanByName(recipe.getPlanName());

        if (material != null && plan != null) {
            List<PlanInstance> plans = new ArrayList<PlanInstance>();
            plans.add(new PlanInstance(recipe.getPlanName(), plan, null));

            WorkspaceClient workspaceClient = new WorkspaceClient(material, plans);
            workspaceClient.selectPlan(0);
            workspaceClient.initGui(27,4, 69, 96);

            return workspaceClient;
        }

        return null;
    }

    private IWoodworkingMaterial findMaterial(List<ItemStack> ingredients) {
        // Find an item that is a registered material
        // in case the ore recipe contains invalid materials
        for (ItemStack ingredient : ingredients) {
            IWoodworkingMaterial material = WoodworkingRegistry.findMaterialForItem(ingredient.getItem());
            if (material != null) {
                return material;
            }
        }

        return null;
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 160, 120);
    }

    @Override
    public void drawExtras(int recipe) {
        if (arecipes.get(recipe) instanceof CachedWoodworkingRecipe) {
            CachedWoodworkingRecipe crecipe = (CachedWoodworkingRecipe) arecipes.get(recipe);

            GuiHelper.drawRect(crecipe.workspaceClient.getBorder(), GuiWoodworking.WORKSPACE_BORDER_COLOR);
            GuiHelper.drawRect(crecipe.workspaceClient.getWorkspaceRect(), GuiWoodworking.WORKSPACE_COLOR);
            GuiHelper.drawTriangles(crecipe.workspaceClient.getSelectedPlanCutout(), GuiWoodworking.WORKSPACE_PLAN_CUTOUT_COLOR);
        }
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo handlerInfo = new HandlerInfo(TFCItems.stoneKnife);
        handlerInfo.setHeight(107);
        return handlerInfo;
    }

    public class CachedWoodworkingRecipe extends CachedRecipe {

        final List<ItemStack> inputs;
        final ItemStack result;

        final WorkspaceClient workspaceClient;

        public CachedWoodworkingRecipe(List<ItemStack> ingred, ItemStack result, WorkspaceClient workspaceClient) {
            this.result = result.copy();
            this.inputs = ingred;
            this.workspaceClient = workspaceClient;
        }

        @Override
        public PositionedStack getResult() {
            return new PositionedStack(result, 119, 71);
        }

        @Override
        public PositionedStack getIngredient() {
            final int i = cycleticks % (20 * inputs.size());
            return new PositionedStack(inputs.get(i / 20), 119, 16);
        }

    }

}
