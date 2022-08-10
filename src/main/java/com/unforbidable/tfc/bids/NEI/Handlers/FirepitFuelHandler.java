package com.unforbidable.tfc.bids.NEI.Handlers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.FirepitRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class FirepitFuelHandler extends TemplateRecipeHandler {

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
        if (outputId.equals(HANDLER_ID) && getClass() == FirepitFuelHandler.class) {
            for (Item item : FirepitRegistry.getFuels()) {
                IFirepitFuelMaterial fuel = FirepitRegistry.findFuel(item);
                arecipes.add(new CachedFirepitFuelRecipe(fuel, item));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (Item item : FirepitRegistry.getFuels()) {
            if (ingredient.getItem() == item) {
                IFirepitFuelMaterial fuel = FirepitRegistry.findFuel(item);
                if (fuel.isFuelValid(ingredient)) {
                    arecipes.add(new CachedFirepitFuelRecipe(fuel, item));
                }
            }
        }
    }

    public class CachedFirepitFuelRecipe extends CachedRecipe {

        final IFirepitFuelMaterial fuel;
        final Item ingred;
        final List<ItemStack> ingreds = new ArrayList<ItemStack>();

        public CachedFirepitFuelRecipe(IFirepitFuelMaterial fuel, Item ingred) {
            this.fuel = fuel;
            this.ingred = ingred;

            final List<ItemStack> temp = new ArrayList<ItemStack>();
            ingred.getSubItems(ingred, null, temp);

            for (ItemStack is : temp) {
                if (fuel.isFuelValid(is)) {
                    ingreds.add(is);
                }
            }
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
