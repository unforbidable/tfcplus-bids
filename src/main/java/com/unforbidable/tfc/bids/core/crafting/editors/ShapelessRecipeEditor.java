package com.unforbidable.tfc.bids.core.crafting.editors;

import com.unforbidable.tfc.bids.core.crafting.RecipeFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessRecipeEditor extends RecipeEditor {

    private ShapelessRecipeEditor(ItemStack output, Object[] items) {
        super(output, items);
    }

    public static ShapelessRecipeEditor of(ShapelessRecipes recipes) {
        return new ShapelessRecipeEditor(recipes.getRecipeOutput(), recipes.recipeItems.toArray());
    }

    public static ShapelessRecipeEditor of(ShapelessOreRecipe recipe) {
        Object[] input = getOreRecipeInput(recipe.getInput().toArray());

        return new ShapelessRecipeEditor(recipe.getRecipeOutput(), input);
    }

    @Override
    protected boolean tryUpsize() {
        // extend the array
        // up to the limit of 9
        if (items.length < 9) {
            int newLength = items.length + 1;
            Object[] newItems = new Object[newLength];

            System.arraycopy(items, 0, newItems, 0, items.length);

            items = newItems;

            return true;
        }

        return false;
    }

    @Override
    public IRecipe build() {
        return RecipeFactory.createShapeless(output, items);
    }

}
