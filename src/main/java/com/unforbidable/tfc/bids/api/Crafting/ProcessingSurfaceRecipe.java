package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ProcessingSurfaceRecipe {

    private final ItemStack output;
    private final ItemStack input;
    private final String toolOreName;
    private final String surfaceBlockOreName;

    private final float effort;

    public ProcessingSurfaceRecipe(ItemStack output, ItemStack input, String toolOreName, String surfaceBlockOreName, float effort) {
        this.output = output;
        this.input = input;
        this.toolOreName = toolOreName;
        this.surfaceBlockOreName = surfaceBlockOreName;
        this.effort = effort;
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

    public float getEffort() {
        return effort;
    }

    public boolean matchesInput(ItemStack ingredient) {
        return input.getItem() == ingredient.getItem() && (input.getItemDamage() == ingredient.getItemDamage()
            || input.getItemDamage() == OreDictionary.WILDCARD_VALUE);
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
        return getOutput();
    }

}
