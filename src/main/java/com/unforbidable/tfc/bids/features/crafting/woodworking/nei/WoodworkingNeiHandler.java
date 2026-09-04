package com.unforbidable.tfc.bids.features.crafting.woodworking.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterial;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingPlan;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingRecipe;
import com.unforbidable.tfc.bids.compat.nei.HandlerInfo;
import com.unforbidable.tfc.bids.compat.nei.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.features.crafting.woodworking.WoodworkingRegistry;
import com.unforbidable.tfc.bids.features.crafting.woodworking.gui.GuiWoodworking;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.WoodworkingHelper;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace.WorkspaceClient;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.workspace.WorkspacePlan;
import com.unforbidable.tfc.bids.util.GuiHelper;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class WoodworkingNeiHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

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
        if (outputId.equals(HANDLER_ID) && getClass() == WoodworkingNeiHandler.class) {
            for (WoodworkingRecipe recipe : WoodworkingRegistry.recipes) {
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
        for (WoodworkingRecipe recipe : WoodworkingRegistry.recipes) {
            final List<ItemStack> input = recipe.getIngredients();
            if (input.size() > 0) {
                final ItemStack result = recipe.getResult(input.get(0));
                output.stackSize = result.stackSize;
                if (result.isItemStackDamageable() && result.getItem() == output.getItem() || ItemStack.areItemStacksEqual(result, output)) {
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
        for (WoodworkingRecipe recipe : WoodworkingRegistry.recipes) {
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
        WoodworkingMaterial material = WoodworkingHelper.findMaterial(recipe.getIngredients());

        WoodworkingPlan plan = WoodworkingRegistry.plans.get(p -> p.getName().equals(recipe.getPlanName()));

        if (material != null && plan != null) {
            List<WorkspacePlan> plans = new ArrayList<WorkspacePlan>();
            plans.add(new WorkspacePlan(plan, null, null));

            WorkspaceClient workspaceClient = new WorkspaceClient(material, plans);
            workspaceClient.selectPlan(0);
            workspaceClient.initGui(27,4, 69, 96);

            return workspaceClient;
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
