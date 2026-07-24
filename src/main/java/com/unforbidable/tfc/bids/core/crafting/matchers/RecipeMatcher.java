package com.unforbidable.tfc.bids.core.crafting.matchers;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.crafting.RecipeAccessor;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.item.ItemStack;

public class RecipeMatcher {

    private final RecipeAccessor recipe;

    public RecipeMatcher(RecipeAccessor recipe) {
        this.recipe = recipe;
    }

    public ObjectMatcher getOutputMatcher() {
        ItemStack is = recipe.getOutput();
        if (is != null) {
            return new ItemStackMatcher(is);
        } else {
            return new NullMatcher();
        }
    }

    public RecipeInputMatcher getInputMatchers() {
        List<ObjectMatcher> inputItemMatchers = recipe.getInputItems().stream()
            .filter(Objects::nonNull)
            .map(this::getInputItemMatcher)
            .collect(Collectors.toList());

        return new RecipeInputMatcher(inputItemMatchers);
    }

    @SuppressWarnings({"unchecked" })
    private ObjectMatcher getInputItemMatcher(Object o) {
        if (o instanceof List<?>) {
            return new ItemStackListMatcher((List<ItemStack>) o);
        } else if (o instanceof ItemStack) {
            return new ItemStackMatcher((ItemStack) o);
        } else {
            Bids.LOG.warn("Invalid recipe input item type {} will be ignored", o.getClass());

            return new NullMatcher();
        }
    }

}
