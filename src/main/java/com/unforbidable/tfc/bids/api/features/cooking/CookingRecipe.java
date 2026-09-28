package com.unforbidable.tfc.bids.api.features.cooking;

import com.unforbidable.tfc.bids.api.util.fluid.DualFluid;
import java.util.Collections;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CookingRecipe {

    private final FluidStack inputFluidStack;
    private final FluidStack secondaryInputFluidStack;
    private final FluidStack outputFluidStack;
    private final ItemStack inputItemStack;
    private final ItemStack outputItemStack;
    private final CookingAccessory accessory;
    private final CookingLidUsage lidUsage;
    private final CookingHeatLevel minHeatLevel;
    private final CookingHeatLevel maxHeatLevel;
    private final float time;
    private final boolean fixedTime;

    public CookingRecipe(FluidStack inputFluidStack, FluidStack secondaryInputFluidStack, FluidStack outputFluidStack, ItemStack inputItemStack, ItemStack outputItemStack, CookingAccessory accessory, CookingLidUsage lidUsage, CookingHeatLevel minHeatLevel, CookingHeatLevel maxHeatLevel, float time, boolean fixedTime) {
        this.inputFluidStack = inputFluidStack;
        this.secondaryInputFluidStack = secondaryInputFluidStack;
        this.outputFluidStack = outputFluidStack;
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
        // No input require and no input present
        if (getInputFluidStack() == null && template.getInputFluidStack() == null) {
            return true;
        }

        // Input is required so input must match the current fluid
        if (getInputFluidStack() != null && template.getInputFluidStack() != null) {
            if (DualFluid.isDualFluidStack(template.getInputFluidStack())) {
                // Match recipe with either of dual fluids
                return DualFluid.of(template.getInputFluidStack()).isEqual(getInputFluidStack());
            } else {
                return areFluidsEqual(template.getInputFluidStack(), getInputFluidStack());
            }
        }

        // When no input is required but input is still present, it must match the recipe output
        if (getInputFluidStack() == null && template.getInputFluidStack() != null && getOutputFluidStack() != null) {
            if (DualFluid.isDualFluidStack(template.getInputFluidStack()) && DualFluid.isDualFluidStack(getOutputFluidStack())) {
                // Match recipe with the primary fluid, only if recipe output is also dual fluid
                // This prevents ambiguity with some recipes such as resin -> pitch VS resin + water -> diluted pitch
                return DualFluid.of(template.getInputFluidStack()).isPrimaryFluidEqual(getOutputFluidStack());
            } else {
                return areFluidsEqual(template.getInputFluidStack(), getOutputFluidStack());
            }
        }

        return false;
    }

    protected boolean areFluidsEqual(FluidStack templateFluid, FluidStack recipeFluid) {
        if (templateFluid.getFluid() instanceof CookingMixtureFluid) {
            // For cooking fluids we use custom method
            return ((CookingMixtureFluid) templateFluid.getFluid()).areCookingFluidsEqual(templateFluid, recipeFluid);
        } else {
            return templateFluid.isFluidEqual(recipeFluid);
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

            return new CookingRecipeCraftingResult(fs, getOutputItemStack());
        }

        return new CookingRecipeCraftingResult(getOutputFluidStack(), getOutputItemStack());
    }

    public boolean matchesOutput(FluidStack fluidStack) {
        return getOutputFluidStack() != null && getOutputFluidStack().isFluidEqual(fluidStack);
    }

    public boolean matchesOutput(ItemStack itemStack) {
        return getOutputItemStack() != null && getOutputItemStack().isItemEqual(itemStack)
            && getOutputItemStack().getItemDamage() == itemStack.getItemDamage();
    }

    public boolean matchesInput(FluidStack fluidStack) {
        return getInputFluidStack() != null && getInputFluidStack().isFluidEqual(fluidStack) ||
            getSecondaryInputFluidStack() != null && getSecondaryInputFluidStack().isFluidEqual(fluidStack) ||
            getInputFluidStack() != null && DualFluid.isDualFluidStack(fluidStack) && DualFluid.of(fluidStack).isEqual(getInputFluidStack());
    }

    public boolean matchesInput(ItemStack itemStack) {
        return getInputItemStack() != null && getInputItemStack().isItemEqual(itemStack)
            && getInputItemStack().getItemDamage() == itemStack.getItemDamage();
    }

    public static CookingRecipeBuilder builder() {
        return new CookingRecipeBuilder();
    }

}
