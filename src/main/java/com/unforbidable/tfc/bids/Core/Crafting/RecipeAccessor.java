package com.unforbidable.tfc.bids.Core.Crafting;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Crafting.Editors.NullRecipeEditor;
import com.unforbidable.tfc.bids.Core.Crafting.Editors.RecipeEditor;
import com.unforbidable.tfc.bids.Core.Crafting.Editors.ShapedRecipeEditor;
import com.unforbidable.tfc.bids.Core.Crafting.Editors.ShapelessRecipeEditor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecipeAccessor {

    public final IRecipe instance;

    private RecipeAccessor(IRecipe instance) {
        this.instance = instance;
    }

    public static boolean isSupportedRecipe(IRecipe recipe) {
        return recipe instanceof ShapelessRecipes ||
            recipe instanceof ShapelessOreRecipe ||
            recipe instanceof ShapedRecipes ||
            recipe instanceof ShapedOreRecipe;
    }

    public static RecipeAccessor of(IRecipe recipe) {
        if (isSupportedRecipe(recipe)) {
            return new RecipeAccessor(recipe);
        } else {
            throw new RuntimeException(MessageFormat.format("Unsupported recipe type {}", recipe.getClass()));
        }
    }

    public ItemStack getOutput() {
        return instance.getRecipeOutput();
    }

    @SuppressWarnings({"unchecked"})
    public List<Object> getInputItems() {
        if (instance instanceof ShapelessRecipes) {
            return (List<Object>) ((ShapelessRecipes) instance).recipeItems;
        } else if (instance instanceof ShapelessOreRecipe) {
            return ((ShapelessOreRecipe) instance).getInput();
        } else if (instance instanceof ShapedRecipes) {
            return Arrays.stream(((ShapedRecipes) instance).recipeItems).collect(Collectors.toList());
        } else if (instance instanceof ShapedOreRecipe) {
            return Arrays.asList(((ShapedOreRecipe) instance).getInput());
        } else {
            return new ArrayList<>();
        }
    }

    public RecipeEditor getEditor() {
        try {
            if (instance instanceof ShapelessRecipes) {
                return ShapelessRecipeEditor.of((ShapelessRecipes) instance);
            } else if (instance instanceof ShapelessOreRecipe) {
                return ShapelessRecipeEditor.of((ShapelessOreRecipe) instance);
            } else if (instance instanceof ShapedRecipes) {
                return ShapedRecipeEditor.of((ShapedRecipes) instance);
            } else if (instance instanceof ShapedOreRecipe) {
                return ShapedRecipeEditor.of((ShapedOreRecipe) instance);
            }

            Bids.LOG.debug("Skipping unsupported recipe type {} for editing", instance.getClass());
        } catch (Exception ex) {
            Bids.LOG.warn("Skipping recipe type {} for editing due to error: {}", instance.getClass(), ex.getMessage(), ex);
        }

        return new NullRecipeEditor();
    }

    @Override
    public String toString() {
        String items = getInputItems().stream()
            .filter(Objects::nonNull)
            .map(RecipeAccessor::inputItemToString)
            .collect(Collectors.joining(", "));
        return "{" +
            "output=" + instance.getRecipeOutput() +
            ", input=[" + items + "]" +
            '}';
    }

    private static String inputItemToString(Object o) {
        if (o instanceof List<?>) {
            // Create new arraylist for streaming
            List<Object> list = new ArrayList<>((List<?>) o);
            String items = list.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
            return "[" + items + "]";
        } else {
            return o.toString();
        }
    }

}
