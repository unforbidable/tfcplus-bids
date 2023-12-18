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

public class PrepRecipe {

    public static final int INGREDIENT_COUNT = 5;
    private static final float REQUIRED_WEIGHT_TOLERANCE = 0.95f;

    protected final ItemStack output;
    private final PrepIngredientSpec[] ingredients;
    private final float minWeight;

    public PrepRecipe(ItemStack output, PrepIngredientSpec[] ingredients) {
        this(output, ingredients, 0);
    }

    public PrepRecipe(ItemStack output, PrepIngredientSpec[] ingredients, float minWeight) {
        if (ingredients.length != INGREDIENT_COUNT) {
            throw new IllegalArgumentException("Expected array of ingredients of size " + INGREDIENT_COUNT);
        }

        this.output = output;
        this.ingredients = ingredients;
        this.minWeight = minWeight;
    }

    public ItemStack getOutput() {
        return output;
    }

    public PrepIngredientSpec[] getIngredients() {
        return ingredients;
    }

    public ItemStack getResult(ItemStack[] input, boolean consumeIngredients) {
        if (input.length != INGREDIENT_COUNT) {
            throw new IllegalArgumentException("Expected array of ingredients of size " + INGREDIENT_COUNT);
        }

        ItemStack result = getOutput().copy();

        // Stack size is 0 if output tag was created without weight
        result.stackSize = 1;

        ItemStack[] consumedIngredients = getConsumedIngredients(input, consumeIngredients);

        float weight = getTotalConsumedIngredientWeight(consumedIngredients);
        ItemFoodTFC.createTag(result, weight);

        Bids.LOG.debug("Result weight: " + weight);

        int[] foodGroups = getIngredientFoodGroups(consumedIngredients);
        Food.setFoodGroups(result, foodGroups);

        Bids.LOG.debug("Result food groups: " + Arrays.toString(foodGroups));

        int[] tastes = getCombinedIngredientTastes(consumedIngredients);
        result.getTagCompound().setInteger("tasteSweet", tastes[0]);
        result.getTagCompound().setInteger("tasteSour", tastes[1]);
        result.getTagCompound().setInteger("tasteSalty", tastes[2]);
        result.getTagCompound().setInteger("tasteBitter", tastes[3]);
        result.getTagCompound().setInteger("tasteUmami", tastes[4]);

        Bids.LOG.debug("Result tastes: " + Arrays.toString(tastes));

        return result;
    }

    public boolean matches(ItemStack[] ingredients) {
        if (ingredients.length != 5) {
            throw new IllegalArgumentException("Expected array of ingredients of size 5");
        }

        return ingredientsMatch(ingredients) &&
            areIngredientsSufficient(ingredients) &&
            areIngredientsUnique(ingredients);
    }

    private boolean areIngredientsSufficient(ItemStack[] ingredients) {
        ItemStack[] consumedIngredients = getConsumedIngredients(ingredients, false);

        // First slot must always be consumed if food
        if (this.ingredients[0] != null && this.ingredients[0].getWeight() > 0 && consumedIngredients[0] == null) {
            return false;
        }

        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            if (this.ingredients[i] != null && this.ingredients[i].isRequired() && consumedIngredients[i] == null) {
                return false;
            }
        }

        float totalConsumedWeight = getTotalConsumedIngredientWeight(consumedIngredients);
        return totalConsumedWeight >= getMinWeight();
    }

    public float getMinWeight() {
        return minWeight;
    }

    public boolean doesVesselMatch(ItemStack is) {
        return ingredients[0].getIngredient().matches(is);
    }

    public boolean doesVesselOrIngredientMatch(ItemStack is) {
        return doesAnyIngredientMatch(is);
    }

    private boolean doesAnyIngredientMatch(ItemStack is) {
        for (PrepIngredientSpec ingredient : ingredients) {
            if (ingredient.getIngredient().matches(is)) {
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
            } else if (i == 0 && this.ingredients[i] != null) {
                // Vessel is not optional
                return false;
            }
        }

        return true;
    }

    protected boolean ingredientMatches(ItemStack ingredient, int slot) {
        return ingredients[slot] != null && ingredients[slot].getIngredient().matches(ingredient);
    }

    protected float getTotalConsumedIngredientWeight(ItemStack[] consumedIngredients) {
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

    protected ItemStack[] getConsumedIngredients(ItemStack[] ingredients, boolean consumeIngredients) {
        ItemStack[] consumed = new ItemStack[INGREDIENT_COUNT];

        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            if (ingredients[i] != null) {
                if (this.ingredients[i].getWeight() > 0) {
                    float weight = Food.getWeight(ingredients[i]);
                    float decay = Math.max(Food.getDecay(ingredients[i]), 0);
                    float availableWeight = weight - decay;
                    // Allow some tolerance so that slightly less food than required is enough
                    if (availableWeight >= this.ingredients[i].getWeight() * REQUIRED_WEIGHT_TOLERANCE) {
                        // Consume available weight but no more than required weight
                        float consumedWeight = Math.min(availableWeight, this.ingredients[i].getWeight());
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

    protected int[] getCombinedIngredientTastes(ItemStack[] consumedIngredients) {
        int[] tastes = { 0, 0, 0, 0, 0 };
        float totalWeight = getTotalConsumedIngredientWeight(consumedIngredients);
        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            if (consumedIngredients[i] != null) {
                float weightMult = this.ingredients[i].getWeight() / totalWeight;
                tastes[0] += ((IFood)consumedIngredients[i].getItem()).getTasteSweet(consumedIngredients[i]) * weightMult;
                tastes[1] += ((IFood)consumedIngredients[i].getItem()).getTasteSour(consumedIngredients[i]) * weightMult;
                tastes[2] += ((IFood)consumedIngredients[i].getItem()).getTasteSalty(consumedIngredients[i]) * weightMult;
                tastes[3] += ((IFood)consumedIngredients[i].getItem()).getTasteBitter(consumedIngredients[i]) * weightMult;
                tastes[4] += ((IFood)consumedIngredients[i].getItem()).getTasteSavory(consumedIngredients[i]) * weightMult;
            }
        }

        return tastes;
    }

    protected int[] getIngredientFoodGroups(ItemStack[] consumedIngredients) {
        int[] fg = new int[INGREDIENT_COUNT];

        int j = 0;
        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            // Skipping slots with no weight
            if (this.ingredients[i].getWeight() > 0) {
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

    public float[] getIngredientWeights() {
        float[] weights = new float[INGREDIENT_COUNT];

        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            weights[i] = ingredients[i] != null ? ingredients[i].getWeight() : 0;
        }

        return weights;
    }
}
