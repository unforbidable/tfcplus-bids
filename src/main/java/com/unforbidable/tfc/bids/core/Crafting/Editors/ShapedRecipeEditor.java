package com.unforbidable.tfc.bids.core.crafting.editors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedRecipeEditor extends RecipeEditor {

    private int width;
    private int height;

    private ShapedRecipeEditor(ItemStack output, int width, int height, Object[] items) {
        super(output, items);
        this.width = width;
        this.height = height;
    }

    public static ShapedRecipeEditor of(ShapedRecipes recipe) {
        return new ShapedRecipeEditor(recipe.getRecipeOutput(), recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems);
    }

    public static ShapedRecipeEditor of(ShapedOreRecipe recipe) {
        ItemStack output = recipe.getRecipeOutput();
        int width = getShapedOreRecipeWidth(recipe);
        int height = recipe.getRecipeSize() / width;
        Object[] input = getOreRecipeInput(recipe.getInput());

        return new ShapedRecipeEditor(output, width, height, input);
    }

    private static int getShapedOreRecipeWidth(ShapedOreRecipe recipe) {
        try {
            Field field = recipe.getClass().getDeclaredField("width");
            field.setAccessible(true);
            return (int) field.get(recipe);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean tryUpsize() {
        // increase width or height, if one is lesser
        // increase width if they are equal
        // up to the limit of 3
        if (width < 3 || height < 3) {
            if (height < width) {
                // add new row
                int newHeight = height + 1;
                Object[] newItems = new Object[width * newHeight];

                System.arraycopy(items, 0, newItems, 0, items.length);

                height = newHeight;
                items = newItems;
            } else {
                // add new column
                int newWidth = width + 1;
                Object[] newItems = new Object[newWidth * height];

                for (int i = 0; i < height; i++) {
                    System.arraycopy(items, i * width, newItems, i * newWidth, width);
                }

                width = newWidth;
                items = newItems;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public IRecipe build() {
        if (isOreRecipe()) {
            // Unlike normal shaped recipe, shaped ore recipe does only provide constructor
            // that requires the shape as when a shaped ore recipe is set up
            // The purpose of this is to recreate the shape and char mapping
            // as if a new recipe was being set up
            List<Object> input = new ArrayList<>();

            String[] pattern = new String[height];
            input.add(pattern);

            char[] row = new char[width];
            for (int i = 0; i < items.length; i++) {
                int j = i % width;
                int k = i / width;

                if (items[i] != null) {
                    char c = (char) (((int) 'A') + i);
                    input.add(c);
                    input.add(items[i]);

                    row[j] = c;
                } else {
                    row[j] = ' ';
                }

                if ((i + 1) % width == 0) {
                    pattern[k] = new String(row);
                }
            }

            return new ShapedOreRecipe(output, input.toArray());
        } else {
            // Normal shaped recipe can be constructed by specifying the dimensions
            // however a typed array of ItemStack is needed
            ItemStack[] input = Arrays.stream(items)
                .map(i -> (ItemStack) i)
                .toArray(ItemStack[]::new);

            return new ShapedRecipes(width, height, input, output);
        }
    }

}
