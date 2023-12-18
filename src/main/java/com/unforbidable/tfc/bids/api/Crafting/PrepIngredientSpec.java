package com.unforbidable.tfc.bids.api.Crafting;

public class PrepIngredientSpec {

    private final PrepIngredient ingredient;
    private final float weight;
    private final boolean required;

    public PrepIngredientSpec(PrepIngredient ingredient) {
        this(ingredient, 0, false);
    }

    public PrepIngredientSpec(PrepIngredient ingredient, float weight) {
        this(ingredient, weight, false);
    }

    public PrepIngredientSpec(PrepIngredient ingredient, float weight, boolean required) {
        this.ingredient = ingredient;
        this.weight = weight;
        this.required = required;
    }

    public PrepIngredient getIngredient() {
        return ingredient;
    }

    public float getWeight() {
        return weight;
    }

    public boolean isRequired() {
        return required;
    }

}
