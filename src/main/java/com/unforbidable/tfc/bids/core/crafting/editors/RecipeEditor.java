package com.unforbidable.tfc.bids.core.crafting.editors;

import com.unforbidable.tfc.bids.core.crafting.matchers.ItemStackListMatcher;
import com.unforbidable.tfc.bids.core.crafting.matchers.ItemStackMatcher;
import com.unforbidable.tfc.bids.core.crafting.matchers.ObjectMatcher;
import java.text.MessageFormat;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.unforbidable.tfc.bids.core.crafting.matchers.OreMatcher;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public abstract class RecipeEditor {
    protected ItemStack output;
    protected Object[] items;

    protected RecipeEditor(ItemStack output, Object[] items) {
        this.output = output;
        this.items = items;
    }

    @SuppressWarnings({"unchecked"})
    private static ObjectMatcher getMatcherForItem(Object item) {
        if (item instanceof String) {
            return new OreMatcher((String) item);
        } else if (item instanceof List<?>) {
            return new ItemStackListMatcher((List<ItemStack>) item);
        } else {
            return new ItemStackMatcher((ItemStack) item);
        }
    }

    public void setOutput(ItemStack itemStack) {
        output = itemStack;
    }

    public void removeInputItem(Predicate<ObjectMatcher> predicate) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                ObjectMatcher matcher = RecipeEditor.getMatcherForItem(items[i]);
                if (predicate.test(matcher)) {
                    items[i] = null;
                }
            }
        }
    }

    public void replaceInputItem(Predicate<ObjectMatcher> predicate, Object item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                ObjectMatcher matcher = RecipeEditor.getMatcherForItem(items[i]);
                if (predicate.test(matcher)) {
                    items[i] = item;
                }
            }
        }
    }

    public boolean addInputItem(Object item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = item;

                return true;
            }
        }

        // Try to enlarge recipe, and try again
        if (tryUpsize()) {
            return addInputItem(item);
        }

        return false;
    }

    protected abstract boolean tryUpsize();

    public abstract IRecipe build();

    protected boolean isOreRecipe() {
        for (Object item : items) {
            if (item instanceof String) {
                return true;
            }
        }

        return false;
    }


    @SuppressWarnings({"unchecked"})
    protected static Object[] getOreRecipeInput(Object[] input) {
        // Ore recipes contain list of items of given ore, rather than the ore name
        // However, when creating ore recipes, the ore name is expected
        // The purpose of this is to find the original ore name for any ore list
        // The requirement is that given ore has not been changed since the creation of the edited recipe
        // which normally should not happen as ores get initialized during the preInit phase
        // and recipes are added in the later phases
        Object[] copy = input.clone();
        for (int i = 0; i < copy.length; i++) {
            if (copy[i] instanceof List<?>) {
                String ore = OreDictionaryHelper.findLikelyOreName((List<ItemStack>) copy[i]);
                if (ore != null) {
                    copy[i] = ore;
                } else {
                    throw new RuntimeException(MessageFormat.format("Unable to find suitable ore name for item stack list: {0}",
                        ((List<ItemStack>) copy[i]).stream()
                            .map(ItemStack::toString)
                            .collect(Collectors.joining(","))));
                }
            }
        }

        return copy;
    }

}
