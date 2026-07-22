package com.unforbidable.tfc.bids.api.features.cooking;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import java.util.Collections;
import java.util.List;

public class CookingRecipe {

    private final FluidStack inputFluidStack;
    private final FluidStack secondaryInputFluidStack;
    private final FluidStack outputFluidStack;
    private final FluidStack secondaryOutputFluidStack;
    private final ItemStack inputItemStack;
    private final ItemStack outputItemStack;
    private final CookingAccessory accessory;
    private final CookingLidUsage lidUsage;
    private final CookingHeatLevel minHeatLevel;
    private final CookingHeatLevel maxHeatLevel;
    private final float time;
    private final boolean fixedTime;

    public CookingRecipe(FluidStack inputFluidStack, FluidStack secondaryInputFluidStack, FluidStack outputFluidStack, FluidStack secondaryOutputFluidStack, ItemStack inputItemStack, ItemStack outputItemStack, CookingAccessory accessory, CookingLidUsage lidUsage, CookingHeatLevel minHeatLevel, CookingHeatLevel maxHeatLevel, float time, boolean fixedTime) {
        this.inputFluidStack = inputFluidStack;
        this.secondaryInputFluidStack = secondaryInputFluidStack;
        this.outputFluidStack = outputFluidStack;
        this.secondaryOutputFluidStack = secondaryOutputFluidStack;
        this.inputItemStack = inputItemStack;
        this.outputItemStack = outputItemStack;
        this.accessory = accessory;
        this.lidUsage = lidUsage;
        this.minHeatLevel = minHeatLevel;
        this.maxHeatLevel = maxHeatLevel;
        this.time = time;
        this.fixedTime = fixedTime;
    }

    public FluidStack getInputFluidStack() {
        return inputFluidStack;
    }

    public FluidStack getSecondaryInputFluidStack() {
        return secondaryInputFluidStack;
    }

    public FluidStack getOutputFluidStack() {
        return outputFluidStack;
    }

    public FluidStack getSecondaryOutputFluidStack() {
        return secondaryOutputFluidStack;
    }

    public ItemStack getInputItemStack() {
        return inputItemStack;
    }

    public List<ItemStack> getInputItemStacks() {
        if (inputItemStack != null) {
            return Collections.singletonList(inputItemStack);
        } else {
            return Collections.emptyList();
        }
    }

    public ItemStack getOutputItemStack() {
        return outputItemStack;
    }

    public CookingAccessory getAccessory() {
        return accessory;
    }

    public CookingLidUsage getLidUsage() {
        return lidUsage;
    }

    public CookingHeatLevel getMinHeatLevel() {
        return minHeatLevel;
    }

    public CookingHeatLevel getMaxHeatLevel() {
        return maxHeatLevel;
    }

    public float getTime() {
        return time;
    }

    public boolean isFixedTime() {
        return fixedTime;
    }

    public boolean matchesTemplate(CookingRecipeInputTemplate template) {
        return doesInputFluidMatch(template) &&
            doesSecondaryInputFluidMatch(template) &&
            doesInputItemMatch(template) &&
            doesMinHeatLevelMatch(template) &&
            doesMaxHeatLevelMatch(template) &&
            doesLidUsageMatch(template) &&
            doesAccessoryMatch(template);
    }

    protected boolean doesInputFluidMatch(CookingRecipeInputTemplate template) {
        boolean match = getInputFluidStack() == null && template.getInputFluidStack() == null ||
            getInputFluidStack() != null && template.getInputFluidStack() != null && areFluidsEqual(template.getInputFluidStack(), getInputFluidStack()) ||
            getInputFluidStack() == null && template.getInputFluidStack() != null && areFluidsEqual(template.getInputFluidStack(), getOutputFluidStack());
        //Bids.LOG.info("doesInputFluidMatch: " + match);

        return match;
    }

    protected boolean areFluidsEqual(FluidStack one, FluidStack other) {
        if (one.getFluid() instanceof CookingMixtureFluid) {
            // For cooking fluids we use custom method
            return ((CookingMixtureFluid) one.getFluid()).areCookingFluidsEqual(one, other);
        } else {
            return one.isFluidEqual(other);
        }
    }

    protected boolean doesSecondaryInputFluidMatch(CookingRecipeInputTemplate template) {
        boolean match = getSecondaryInputFluidStack() == null && template.getSecondaryInputFluidStack() == null ||
            getSecondaryInputFluidStack() != null && template.getSecondaryInputFluidStack() != null && getSecondaryInputFluidStack().isFluidEqual(template.getSecondaryInputFluidStack());
        //Bids.LOG.info("doesSecondaryInputFluidMatch: " + match);

        return match;
    }

    protected boolean doesInputItemMatch(CookingRecipeInputTemplate template) {
        boolean match = getInputItemStack() == null && template.getInputItemStack() == null ||
            getInputItemStack() != null && template.getInputItemStack() != null && getInputItemStack().isItemEqual(template.getInputItemStack())
                && getInputItemStack().getItemDamage() == template.getInputItemStack().getItemDamage();
        //Bids.LOG.info("doesInputItemMatch: " + match);

        return match;
    }

    protected boolean doesMinHeatLevelMatch(CookingRecipeInputTemplate template) {
        boolean match = getMinHeatLevel() == null || getMinHeatLevel().compareTo(template.getMinHeatLevel()) <= 0;
        //Bids.LOG.info("doesMinHeatLevelMatch: " + match);

        return match;
    }

    protected boolean doesMaxHeatLevelMatch(CookingRecipeInputTemplate template) {
        boolean match = getMaxHeatLevel() == null || getMaxHeatLevel().compareTo(template.getMaxHeatLevel()) >= 0;
        //Bids.LOG.info("doesMaxHeatLevelMatch: " + match);

        return match;
    }

    protected boolean doesLidUsageMatch(CookingRecipeInputTemplate template) {
        boolean match = getLidUsage() == null || getLidUsage() == template.getLidUsage();
        //Bids.LOG.info("doesLidUsageMatch: " + match);

        return match;
    }

    protected boolean doesAccessoryMatch(CookingRecipeInputTemplate template) {
        boolean match = getAccessory() == null || getAccessory() == template.getAccessory();
        //Bids.LOG.info("doesAccessoryMatch: " + match);

        return match;
    }

    public CookingRecipeCraftingResult getCraftingResult(CookingRecipeInputTemplate template) {
        if (getOutputFluidStack() != null && template.getInputFluidStack() != null &&
            template.getInputFluidStack().getFluid() instanceof CookingMixtureFluid) {
            CookingMixtureFluid cookingFluid = (CookingMixtureFluid) template.getInputFluidStack().getFluid();

            FluidStack fs = getOutputFluidStack().copy();
            if (template.getSecondaryInputFluidStack() != null) {
                // Cooking fluid was merged with another
                cookingFluid.onCookingFluidMerged(fs, template.getInputFluidStack(), template.getSecondaryInputFluidStack());
            } else {
                // Cooking fluid was cooked or otherwise processed
                cookingFluid.onCookingFluidCooked(fs, template.getInputFluidStack());
            }

            return new CookingRecipeCraftingResult(fs, getSecondaryOutputFluidStack(), getOutputItemStack());
        }

        return new CookingRecipeCraftingResult(getOutputFluidStack(), getSecondaryOutputFluidStack(), getOutputItemStack());
    }

    public boolean matchesOutput(FluidStack fluidStack) {
        return getOutputFluidStack() != null && getOutputFluidStack().isFluidEqual(fluidStack)
            || getSecondaryOutputFluidStack() != null && getSecondaryOutputFluidStack().isFluidEqual(fluidStack);
    }

    public boolean matchesOutput(ItemStack itemStack) {
        return getOutputItemStack() != null && getOutputItemStack().isItemEqual(itemStack)
            && getOutputItemStack().getItemDamage() == itemStack.getItemDamage();
    }

    public boolean matchesInput(FluidStack fluidStack) {
        return getInputFluidStack() != null && getInputFluidStack().isFluidEqual(fluidStack) ||
            getSecondaryInputFluidStack() != null && getSecondaryInputFluidStack().isFluidEqual(fluidStack);
    }

    public boolean matchesInput(ItemStack itemStack) {
        return getInputItemStack() != null && getInputItemStack().isItemEqual(itemStack)
            && getInputItemStack().getItemDamage() == itemStack.getItemDamage();
    }

    public static CookingRecipeBuilder builder() {
        return new CookingRecipeBuilder();
    }

}
