package com.unforbidable.tfc.bids.features.crafting.threshing.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.threshing.ThreshingRecipe;
import com.unforbidable.tfc.bids.compat.nei.HandlerInfo;
import com.unforbidable.tfc.bids.compat.nei.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.features.crafting.threshing.ThreshingRegistry;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

public class ThreshingNeiHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "threshing";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_threshing.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Threshing");
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
        if (outputId.equals(HANDLER_ID) && getClass() == ThreshingNeiHandler.class) {
            for (ThreshingRecipe recipe : ThreshingRegistry.recipes) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getResult(input);
                final ItemStack extra = recipe.getExtraResult(input);
                List<ItemStack> tools = OreDictionary.getOres("itemThreshingTool", false);
                arecipes.add(new CachedThreshingRecipe(input, result, extra, tools));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (ThreshingRecipe recipe : ThreshingRegistry.recipes) {
            final ItemStack output2 = output.copy();
            output2.stackSize = 1;
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getResult(input);
            final ItemStack extra = recipe.getExtraResult(input);
            List<ItemStack> tools = OreDictionary.getOres("itemThreshingTool", false);
            if (ItemStack.areItemStacksEqual(result, output2) ||
                result.getItem() instanceof IFood && result.getItem() == output2.getItem() &&
                    Food.areEqual(ItemFoodTFC.createTag(result.copy()), output2)) {
                arecipes.add(new CachedThreshingRecipe(input, result, extra, tools));
            }
            if (ItemStack.areItemStacksEqual(extra, output2)) {
                arecipes.add(new CachedThreshingRecipe(input, result, extra, tools));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (ThreshingRecipe recipe : ThreshingRegistry.recipes) {
            if (recipe.matches(ingredient)) {
                final ItemStack input = ingredient.copy();
                input.stackSize = 1;
                final ItemStack result = recipe.getResult(input);
                final ItemStack extra = recipe.getExtraResult(input);
                List<ItemStack> tools = OreDictionary.getOres("itemThreshingTool", false);
                arecipes.add(new CachedThreshingRecipe(input, result, extra, tools));
            }
        }
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo handler = new HandlerInfo(BidsItems.woodenFlail, 0);
        for (ItemStack is : OreDictionary.getOres("itemThreshingTool", false)) {
            handler.addCatalyst(is.getItem(), is.getItemDamage());
        }

        return handler;
    }

    public class CachedThreshingRecipe extends CachedRecipe {

        final ItemStack ingred;
        final ItemStack result;
        final ItemStack extra;
        final List<ItemStack> tools;

        public CachedThreshingRecipe(ItemStack ingred, ItemStack result, ItemStack extra, List<ItemStack> tools) {
            this.ingred = ingred.copy();
            this.result = result.copy();
            this.extra = extra != null ? extra.copy() : null;
            this.tools = tools;
        }

        @Override
        public PositionedStack getResult() {
            return new PositionedStack(result, 111, 24);
        }

        @Override
        public PositionedStack getIngredient() {
            return new PositionedStack(ingred, 39, 24);
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            List<PositionedStack> list = new ArrayList<PositionedStack>();
            final int i = cycleticks % (20 * tools.size());
            list.add(new PositionedStack(tools.get(i / 20), 39, 6));
            if (extra != null) {
                list.add(new PositionedStack(extra, 129, 24));
            }
            return list;
        }

    }

}
