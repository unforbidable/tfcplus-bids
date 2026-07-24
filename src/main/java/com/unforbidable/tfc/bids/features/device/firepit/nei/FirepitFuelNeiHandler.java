package com.unforbidable.tfc.bids.features.device.firepit.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import com.unforbidable.tfc.bids.compat.nei.HandlerInfo;
import com.unforbidable.tfc.bids.compat.nei.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitRegistry;
import com.unforbidable.tfc.bids.util.registry.Entry;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class FirepitFuelNeiHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "firepitfuel";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_firepitfuel.png");

    @Override
    public String getRecipeName() {
        return BidsBlocks.newFirepit.getLocalizedName() + " " + StatCollector.translateToLocal("gui.Fuel");
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
        if (outputId.equals(HANDLER_ID) && getClass() == FirepitFuelNeiHandler.class) {
            for (Entry<Item, FirepitFuelMaterial> entry : FirepitRegistry.fuel) {
                // Ensure any sub items are valid fuel
                List<ItemStack> temp = getValidFuelSubItems(entry.value, entry.key);
                if (temp.size() > 0) {
                    arecipes.add(new CachedFirepitFuelRecipe(entry.value, entry.key));
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    private static List<ItemStack> getValidFuelSubItems(FirepitFuelMaterial fuel, Item item) {
        final List<ItemStack> temp = new ArrayList<ItemStack>();
        item.getSubItems(item, null, temp);

        final List<ItemStack> list = new ArrayList<ItemStack>();
        for (ItemStack is : temp) {
            if (fuel.isFuelValid(is)) {
                list.add(is);
            }
        }

        return list;
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (Entry<Item, FirepitFuelMaterial> entry : FirepitRegistry.fuel) {
            if (ingredient.getItem() == entry.key) {
                if (entry.value.isFuelValid(ingredient)) {
                    arecipes.add(new CachedFirepitFuelRecipe(entry.value, entry.key));
                }
            }
        }
    }

    @Override
    public void drawExtras(int recipe) {
        CachedRecipe crecipe = arecipes.get(recipe);
        if (crecipe instanceof CachedFirepitFuelRecipe) {
            final CachedFirepitFuelRecipe cachedFirepitFuelRecipe = (CachedFirepitFuelRecipe) crecipe;

            drawCenteredString(Minecraft.getMinecraft().fontRenderer,
                    cachedFirepitFuelRecipe.getKindlingString(), 83, 49, 0x555555);
        }
    }

    private static void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        return new HandlerInfo(BidsBlocks.newFirepit);
    }

    public class CachedFirepitFuelRecipe extends CachedRecipe {

        final FirepitFuelMaterial fuel;
        final Item ingred;
        final List<ItemStack> ingreds = new ArrayList<ItemStack>();

        public CachedFirepitFuelRecipe(FirepitFuelMaterial fuel, Item ingred) {
            this.fuel = fuel;
            this.ingred = ingred;

            ingreds.addAll(getValidFuelSubItems(fuel, ingred));
        }

        public String getKindlingString() {
            float kindlingQuality = fuel.getFuelKindlingQuality(new ItemStack(ingred));
            return kindlingQuality > 0
                    ? String.format("%s: %d%%", StatCollector.translateToLocal("gui.KindlingQuality"), Math.round(kindlingQuality * 100))
                    : "";
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public PositionedStack getIngredient() {
            if (ingreds.size() > 0) {
                final int i = (cycleticks % (ingreds.size() * 20)) / 20;
                return new PositionedStack(ingreds.get(i), 39, 24);
            }

            return super.getIngredient();
        }

    }

}
