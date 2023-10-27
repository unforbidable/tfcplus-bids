package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.Bids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class PrepRecipe {

    private final int INGREDIENT_COUNT = 5;

    private final ItemStack output;
    private final PrepIngredient[] ingredients;

    public PrepRecipe(ItemStack output, PrepIngredient[] ingredients) {
        if (ingredients.length != INGREDIENT_COUNT) {
            throw new IllegalArgumentException("Expected array of ingredients of size " + INGREDIENT_COUNT);
        }

        this.output = output;
        this.ingredients = ingredients;
    }

    public ItemStack getResult(ItemStack[] input, boolean consumeIngredients) {
        if (input.length != INGREDIENT_COUNT) {
            throw new IllegalArgumentException("Expected array of ingredients of size " + INGREDIENT_COUNT);
        }

        ItemStack result = output.copy();

        // Stack size is 0 if output tag was created without weight
        result.stackSize = 1;

        ItemStack[] consumedIngredients = getConsumedIngredients(input, consumeIngredients);

        float weight = getTotalConsumedIngredientWeight(consumedIngredients);
        ItemFoodTFC.createTag(result, weight);

        Bids.LOG.info("Result weight: " + weight);

        int[] foodGroups = getIngredientFoodGroups(consumedIngredients);
        Food.setFoodGroups(result, foodGroups);

        Bids.LOG.info("Result food groups: " + Arrays.toString(foodGroups));

        int[] tastes = getCombinedIngredientTastes(consumedIngredients);
        result.getTagCompound().setInteger("tasteSweet", tastes[0]);
        result.getTagCompound().setInteger("tasteSour", tastes[1]);
        result.getTagCompound().setInteger("tasteSalty", tastes[2]);
        result.getTagCompound().setInteger("tasteBitter", tastes[3]);
        result.getTagCompound().setInteger("tasteUmami", tastes[4]);

        Bids.LOG.info("Result tastes: " + Arrays.toString(tastes));

        return result;
    }

    public boolean matches(ItemStack[] ingredients) {
        if (ingredients.length != 5) {
            throw new IllegalArgumentException("Expected array of ingredients of size 5");
        }

        return ingredients[0] != null &&
            ingredientsMatch(ingredients) &&
            isTotalIngredientsWeightSufficient(ingredients) &&
            areIngredientsUnique(ingredients);
    }

    private boolean isTotalIngredientsWeightSufficient(ItemStack[] ingredients) {
        ItemStack[] consumedIngredients = getConsumedIngredients(ingredients, false);
        float totalConsumedWeight = getTotalConsumedIngredientWeight(consumedIngredients);
        return totalConsumedWeight >= getMinWeight();
    }

    public int[] getIngredientWeights() {
        return new int[0];
    }

    public int getMinWeight() {
        return 0;
    }

    public boolean doesVesselMatch(ItemStack is) {
        return ingredients[0].matches(is);
    }

    public boolean doesVesselOrIngredientMatch(ItemStack is) {
        return doesAnyIngredientMatch(is);
    }

    private boolean doesAnyIngredientMatch(ItemStack is) {
        for (PrepIngredient ingredient : ingredients) {
            if (ingredient.matches(is)) {
                return true;
            }
        }

        return false;
    }

    protected boolean ingredientsMatch(ItemStack[] ingredients) {
        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            if (ingredients[i] != null) {
                // Slot has ingredient
                if (!ingredientMatches(ingredients[i], i)) {
                    // No match
                    return false;
                }
            }
        }

        return true;
    }

    protected boolean ingredientMatches(ItemStack ingredient, int slot) {
        return this.ingredients[slot].matches(ingredient);
    }

    private float getTotalConsumedIngredientWeight(ItemStack[] consumedIngredients) {
        float totalAvailableWeight = 0;

        for (ItemStack consumedIngredient : consumedIngredients) {
            if (consumedIngredient != null) {
                float weight = Food.getWeight(consumedIngredient);
                totalAvailableWeight += weight;
            }
        }

        return totalAvailableWeight;
    }

    private boolean areIngredientsUnique(ItemStack[] ingredients) {
        List<Item> items = new ArrayList<Item>();
        for (ItemStack ingredient : ingredients) {
            if (ingredient != null) {
                if (items.contains(ingredient.getItem())) {
                    // Not unique
                    return false;
                }

                items.add(ingredient.getItem());
            }
        }

        return true;
    }

    private ItemStack[] getConsumedIngredients(ItemStack[] ingredients, boolean consumeIngredients) {
        int[] weights = getIngredientWeights();
        ItemStack[] consumed = new ItemStack[INGREDIENT_COUNT];

        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            if (ingredients[i] != null) {
                if (weights[i] > 0) {
                    float weight = Food.getWeight(ingredients[i]);
                    float decay = Food.getDecay(ingredients[i]);
                    float availableWeight = weight - decay;
                    if (availableWeight > weights[i]) {
                        float consumedWeight = weights[i];
                        consumed[i] = ingredients[i].copy();
                        Food.setWeight(consumed[i], consumedWeight);

                        if (consumeIngredients) {
                            float remainingWeight = weight - consumedWeight;
                            Food.setWeight(ingredients[i], remainingWeight);
                        }
                    }
                } else {
                    if (consumeIngredients) {
                        ingredients[i].stackSize--;
                    }
                }
            }
        }

        return consumed;
    }

    private int[] getCombinedIngredientTastes(ItemStack[] consumedIngredients) {
        int[] tastes = { 0, 0, 0, 0, 0 };
        float totalWeight = getTotalConsumedIngredientWeight(consumedIngredients);
        int[] weights = getIngredientWeights();

        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            if (consumedIngredients[i] != null) {
                float weightMult = weights[i] / totalWeight;
                tastes[0] += ((IFood)consumedIngredients[i].getItem()).getTasteSweet(consumedIngredients[i]) * weightMult;
                tastes[1] += ((IFood)consumedIngredients[i].getItem()).getTasteSour(consumedIngredients[i]) * weightMult;
                tastes[2] += ((IFood)consumedIngredients[i].getItem()).getTasteSalty(consumedIngredients[i]) * weightMult;
                tastes[3] += ((IFood)consumedIngredients[i].getItem()).getTasteBitter(consumedIngredients[i]) * weightMult;
                tastes[4] += ((IFood)consumedIngredients[i].getItem()).getTasteSavory(consumedIngredients[i]) * weightMult;
            }
        }

        return tastes;
    }

    private int[] getIngredientFoodGroups(ItemStack[] consumedIngredients) {
        int[] weights = getIngredientWeights();
        int[] fg = new int[INGREDIENT_COUNT];

        int j = 0;
        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            // Skipping slots with no weight
            if (weights[i] > 0) {
                if (consumedIngredients[i] != null) {
                    fg[j] = ((IFood) consumedIngredients[i].getItem()).getFoodID();
                } else {
                    fg[j] = -1;
                }
                j++;
            }
        }

        if (j < INGREDIENT_COUNT) {
            // If we skipped any slots trim the array accordingly
            return Arrays.copyOfRange(fg, 0, j);
        } else {
            return fg;
        }
    }

}
