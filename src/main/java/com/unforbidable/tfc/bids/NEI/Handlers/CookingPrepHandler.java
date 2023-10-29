package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Crafting.PrepIngredient;
import com.unforbidable.tfc.bids.api.Crafting.PrepManager;
import com.unforbidable.tfc.bids.api.Crafting.PrepRecipe;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CookingPrepHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "cookingprep";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_cookingprep.png");

    @Override
    public String getRecipeName() {
        return BidsBlocks.cookingPrep.getLocalizedName();
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
        transferRects.add(new RecipeTransferRect(new Rectangle(109, 23, 24, 18), HANDLER_ID));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(HANDLER_ID) && getClass() == CookingPrepHandler.class) {
            for (PrepRecipe recipe : PrepManager.getRecipes()) {
                arecipes.add(new CachedPrepRecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (PrepRecipe recipe : PrepManager.getRecipes()) {
            final ItemStack result = recipe.getOutput();
            if (OreDictionary.itemMatches(result, output, true)) {
                arecipes.add(new CachedPrepRecipe(recipe));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (PrepRecipe recipe : PrepManager.getRecipes()) {
            if (recipe.doesVesselOrIngredientMatch(ingredient)) {
                arecipes.add(new CachedPrepRecipe(recipe));
            }
        }
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(BidsBlocks.cookingPrep, 0);
        info.addCatalyst(BidsItems.largeClayBowl, 1);
        return info;
    }

    @Override
    public void drawForeground(int index) {
        if (arecipes.get(index) instanceof CachedPrepRecipe) {
            CachedPrepRecipe recipe = (CachedPrepRecipe) arecipes.get(index);
            float[] weights = recipe.weights;
            int[] xs = { 18, 40, 58, 76, 94 };
            for (int i = 0; i < 5; i++) {
                if (weights[i] > 0) {
                    drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                        String.valueOf(Math.round(weights[i])), xs[i], 14, 0x555555);
                }
            }
        }
    }

    private static void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    public List<ItemStack> getAllIngredients(PrepIngredient prepIngredient) {
        List<ItemStack> list = new ArrayList<ItemStack>();

        for (ItemStack is : prepIngredient.getAllowedItemStacks()) {
            if (prepIngredient.matches(is)) {
                list.add(is);
            }
        }

        if (prepIngredient.getAllowedOreNames().size() > 0) {
            for (String oreName : prepIngredient.getAllowedOreNames()) {
                for (ItemStack is : OreDictionary.getOres(oreName, false)) {
                    if (prepIngredient.matches(is)) {
                        list.add(is);
                    }
                }
            }
        }

        if (prepIngredient.getAllowedFoodGroups().size() > 0 || list.size() == 0) {
            for (Item item : GameData.getItemRegistry().typeSafeIterable()) {
                if (item instanceof IFood) {
                    ItemStack is = new ItemStack(item);
                    if (prepIngredient.matches(is)) {
                        list.add(is);
                    }
                }
            }
        }

        return list;
    }

    public class CachedPrepRecipe extends CachedRecipe {

        final float[] weights;
        final PositionedStack result;
        final List<List<PositionedStack>> inputs;

        public CachedPrepRecipe(PrepRecipe recipe) {
            weights = recipe.getIngredientWeights();
            inputs = new ArrayList<List<PositionedStack>>(PrepRecipe.INGREDIENT_COUNT);

            int[] xs = { 10, 32, 50, 68, 86 };

            int totalWeight = 0;
            for (int i = 0; i < PrepRecipe.INGREDIENT_COUNT; i++) {
                totalWeight += weights[i];
                PrepIngredient ingredient = recipe.getIngredients()[i];

                List<PositionedStack> slot = new ArrayList<PositionedStack>();
                for (ItemStack is : getAllIngredients(ingredient)) {
                    if (i > 0 && recipe.doesVesselMatch(is)) {
                        // Skip items that can also be used as the vessel
                        continue;
                    }

                    if (is.getItem() instanceof IFood) {
                        ItemFoodTFC.createTag(is, weights[i]);
                    }

                    slot.add(new PositionedStack(is, xs[i], 24));
                }
                inputs.add(slot);
            }

            ItemStack output = recipe.getOutput().copy();
            if (output.getItem() instanceof IFood) {
                ItemFoodTFC.createTag(output, totalWeight);
            }
            result = new PositionedStack(output, 137, 24);
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            List<PositionedStack> list = new ArrayList<PositionedStack>(5);

            int shift = 0;
            for (List<PositionedStack> slot : inputs) {
                final int i = (cycleticks + shift * 20) % (20 * slot.size());
                list.add(slot.get(i / 20));

                shift++;
            }

            return list;
        }

    }

}
