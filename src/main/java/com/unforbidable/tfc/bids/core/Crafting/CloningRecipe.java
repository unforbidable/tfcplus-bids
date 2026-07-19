package com.unforbidable.tfc.bids.core.crafting;

import com.unforbidable.tfc.bids.core.crafting.editors.RecipeEditor;
import com.unforbidable.tfc.bids.core.crafting.matchers.ObjectMatcher;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class CloningRecipe {

    public final RecipeAccessor recipe;
    private final RecipeEditor editor;

    private Consumer<CraftingContext> action;

    public CloningRecipe(RecipeAccessor recipe) {
        this.recipe = recipe;
        this.editor = recipe.getEditor();
    }

    public CloningRecipe setOutput(ItemStack itemStack) {
        editor.setOutput(itemStack);

        return this;
    }

    public CloningRecipe removeInput(Predicate<ObjectMatcher> predicate) {
        editor.removeInputItem(predicate);

        return this;
    }

    public CloningRecipe replaceInput(Predicate<ObjectMatcher> predicate, ItemStack itemStack) {
        editor.replaceInputItem(predicate, itemStack);

        return this;
    }

    public CloningRecipe replaceInput(Predicate<ObjectMatcher> predicate, Item item) {
        editor.replaceInputItem(predicate, new ItemStack(item));

        return this;
    }

    public CloningRecipe replaceInput(Predicate<ObjectMatcher> predicate, Block block) {
        editor.replaceInputItem(predicate, new ItemStack(block));

        return this;
    }

    public CloningRecipe replaceInput(Predicate<ObjectMatcher> predicate, String ore) {
        editor.replaceInputItem(predicate, ore);

        return this;
    }

    public CloningRecipe addInput(Object newInput) {
        editor.addInputItem(newInput);

        return this;
    }

    public ActionableRecipe build() {
        IRecipe recipe = editor.build();
        return new ActionableRecipe(recipe, action);
    }

    public void action(Consumer<CraftingContext> action) {
        if (this.action != null) {
            this.action = this.action.andThen(action);
        } else {
            this.action = action;
        }
    }

}
