package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SoakingSurfaceRecipe {

    private final ItemStack output;
    private final ItemStack input;
    private final String fluidBlockOreName;

    private final long hours;

    public SoakingSurfaceRecipe(ItemStack output, ItemStack input, String fluidBlockOreName, long hours) {
        this.output = output;
        this.input = input;
        this.fluidBlockOreName = fluidBlockOreName;
        this.hours = hours;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getInput() {
        return input;
    }

    public String getFluidBlockOreName() {
        return fluidBlockOreName;
    }

    public long getHours() {
        return hours;
    }

    public boolean matchesInput(ItemStack ingredient) {
        return input.getItem() == ingredient.getItem() && (input.getItemDamage() == ingredient.getItemDamage()
            || input.getItemDamage() == OreDictionary.WILDCARD_VALUE);
    }

    public boolean matchesSurface(ItemStack surface) {
        for (ItemStack item : OreDictionary.getOres(fluidBlockOreName, false)) {
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
