package com.unforbidable.tfc.bids.api.features.processing;

import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.api.util.food.BidsFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ProcessingSurfaceRecipe {

    private final ItemStack output;
    private final ItemStack input;
    private final String toolOreName;
    private final String surfaceBlockOreName;

    private final float effort;
    private final boolean partial;

    public ProcessingSurfaceRecipe(ItemStack input, ItemStack output, String toolOreName, String surfaceBlockOreName, float effort, boolean partial) {
        this.output = output;
        this.input = input;
        this.toolOreName = toolOreName;
        this.surfaceBlockOreName = surfaceBlockOreName;
        this.effort = effort;
        this.partial = partial;
    }

    public ProcessingSurfaceRecipe(ItemStack input, ItemStack output, String toolOreName, String surfaceBlockOreName, float effort) {
        this(input, output, toolOreName, surfaceBlockOreName, effort, false);
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getInput() {
        return input;
    }

    public String getToolOreName() {
        return toolOreName;
    }

    public String getSurfaceBlockOreName() {
        return surfaceBlockOreName;
    }

    public float getEffort(ItemStack itemStack) {
        return effort;
    }

    public boolean isPartial() {
        return partial;
    }

    public boolean matchesInput(ItemStack ingredient) {
        return input.getItem() == ingredient.getItem() && (input.getItemDamage() == ingredient.getItemDamage()
            || input.getItemDamage() == OreDictionary.WILDCARD_VALUE) &&
            (!(input.getItem() instanceof IFood) || matchesInputFood(ingredient));
    }

    private boolean matchesInputFood(ItemStack ingredient) {
        // if input is dried, boiled and so on, expect the same of the ingredient
        return (!Food.isDried(input) || Food.isDried(ingredient)) &&
            (!BidsFood.isBoiled(input) || BidsFood.isBoiled(ingredient));
    }

    public boolean matchesTool(ItemStack tool) {
        int requiredToolOreNameId = OreDictionary.getOreID(toolOreName);
        for (int id : OreDictionary.getOreIDs(tool)) {
            if (id == requiredToolOreNameId) {
                return true;
            }
        }

        return false;
    }

    public boolean matchesSurface(ItemStack surface) {
        for (ItemStack item : OreDictionary.getOres(surfaceBlockOreName, false)) {
            if (surface.getItem() == item.getItem()
                && (surface.getItemDamage() == item.getItemDamage()
                || item.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                return true;
            }
        }

        return false;
    }

    public ItemStack getResult(ItemStack ingredient) {
        return getOutput().copy();
    }

}
