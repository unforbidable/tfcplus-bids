package com.unforbidable.tfc.bids.api.features.cookingprep;

public class CookingPrepIngredientSpec {

    private final CookingPrepIngredient ingredient;
    private final float weight;
    private final boolean required;

    public CookingPrepIngredientSpec(CookingPrepIngredient ingredient) {
        this(ingredient, 0, false);
    }

    public CookingPrepIngredientSpec(CookingPrepIngredient ingredient, float weight) {
        this(ingredient, weight, false);
    }

    public CookingPrepIngredientSpec(CookingPrepIngredient ingredient, float weight, boolean required) {
        this.ingredient = ingredient;
        this.weight = weight;
        this.required = required;
    }

    public CookingPrepIngredient getIngredient() {
        return ingredient;
    }

    public float getWeight() {
        return weight;
    }

    public boolean isRequired() {
        return required;
    }

}
