package com.unforbidable.tfc.bids.core.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeFactory {

    public static IRecipe createShapeless(ItemStack output, Object ...input) {
        if (isShapelessOreRecipeInput(input)) {
            return new ShapelessOreRecipe(output, input);
        } else {
            return createShapelessRecipe(output, input);
        }
    }

    public static IRecipe createShaped(ItemStack output, Object ...input) {
        if (isShapedOreRecipeInput(input)) {
            return new ShapedOreRecipe(output, input);
        } else {
            return createShapedRecipe(output, input);
        }
    }

    private static boolean isShapelessOreRecipeInput(Object[] input) {
        for (Object o : input) {
            if (o instanceof String) {
                return true;
            }
        }
        return false;
    }

    private static boolean isShapedOreRecipeInput(Object[] input) {
        int i = 0;

        if (input[i] instanceof String[]) {
            i++;
        } else {
            while (input[i] instanceof String) {
                i++;
            }
        }

        while (i + 1 < input.length) {
            Object o = input[i + 1];
            if (o instanceof String) {
                return true;
            }

            i += 2;
        }

        return false;
    }

    private static ShapelessRecipes createShapelessRecipe(ItemStack output, Object[] input) {
        // Input is prepared much like in CraftingManager::addShapelessRecipe()
        List<Object> list = new ArrayList<>();
        for (Object o : input) {
            if (o instanceof Item) {
                list.add(new ItemStack((Item) o));
            } else if (o instanceof ItemStack) {
                list.add(((ItemStack) o).copy());
            } else if (o instanceof Block) {
                list.add(new ItemStack((Block) o));
            } else {
                throw new RuntimeException("Invalid shapeless recipe");
            }
        }

        return new ShapelessRecipes(output, list);
    }

    @SuppressWarnings({"rawtypes", "unchecked" })
    private static ShapedRecipes createShapedRecipe(ItemStack output, Object[] input) {
        // Input is prepared much like in CraftingManager::addRecipe()
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (input[i] instanceof String[])
        {
            String[] astring = (String[])((String[])input[i++]);

            for (int l = 0; l < astring.length; ++l)
            {
                String s1 = astring[l];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        }
        else
        {
            while (input[i] instanceof String)
            {
                String s2 = (String)input[i++];
                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = new HashMap(); i < input.length; i += 2)
        {
            Character character = (Character)input[i];
            ItemStack itemstack1 = null;

            if (input[i + 1] instanceof Item)
            {
                itemstack1 = new ItemStack((Item)input[i + 1]);
            }
            else if (input[i + 1] instanceof Block)
            {
                itemstack1 = new ItemStack((Block)input[i + 1], 1, 32767);
            }
            else if (input[i + 1] instanceof ItemStack)
            {
                itemstack1 = (ItemStack)input[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1)
        {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(Character.valueOf(c0)))
            {
                aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
            }
            else
            {
                aitemstack[i1] = null;
            }
        }

        return new ShapedRecipes(j, k, aitemstack, output);
    }

}
