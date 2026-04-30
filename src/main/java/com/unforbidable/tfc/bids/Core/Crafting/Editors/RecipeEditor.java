package com.unforbidable.tfc.bids.Core.Crafting.Editors;

import com.unforbidable.tfc.bids.Core.Crafting.Matchers.ItemStackListMatcher;
import com.unforbidable.tfc.bids.Core.Crafting.Matchers.ItemStackMatcher;
import com.unforbidable.tfc.bids.Core.Crafting.Matchers.ObjectMatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class RecipeEditor {
    protected ItemStack output;
    protected Object[] items;

    protected RecipeEditor(ItemStack output, Object[] items) {
        this.output = output;
        this.items = items;
    }

    @SuppressWarnings({"unchecked"})
    private static ObjectMatcher getMatcherForItem(Object item) {
        if (item instanceof List<?>) {
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
            ObjectMatcher matcher = RecipeEditor.getMatcherForItem(items[i]);
            if (predicate.test(matcher)) {
                items[i] = null;
            }
        }
    }

    public void replaceInputItem(Predicate<ObjectMatcher> predicate, Object item) {
        for (int i = 0; i < items.length; i++) {
            ObjectMatcher matcher = RecipeEditor.getMatcherForItem(items[i]);
            if (predicate.test(matcher)) {
                items[i] = item;
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
                String ore = findLikelyOreName((List<ItemStack>) copy[i]);
                if (ore != null) {
                    copy[i] = ore;
                } else {
                    throw new RuntimeException(MessageFormat.format("Unable to find suitable ore name for item stack list: {}",
                        ((List<ItemStack>) copy[i]).stream()
                            .map(ItemStack::toString)
                            .collect(Collectors.joining(","))));
                }
            }
        }

        return copy;
    }

    private static String findLikelyOreName(List<ItemStack> itemStacks) {
        for (String ore : OreDictionary.getOreNames()) {
            List<ItemStack> oreItemStacks = OreDictionary.getOres(ore, false);
            if (itemStackListsMatch(itemStacks, oreItemStacks)) {
                return ore;
            }
        }

        return null;
    }

    private static boolean itemStackListsMatch(List<ItemStack> list1, List<ItemStack> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        Iterator<ItemStack> it1 = list1.iterator();
        Iterator<ItemStack> it2 = list2.iterator();
        while (it1.hasNext()) {
            ItemStack is1 = it1.next();
            ItemStack is2 = it2.next();
            if (!OreDictionary.itemMatches(is1, is2, false)) {
                return false;
            }
        }

        return true;
    }

}
