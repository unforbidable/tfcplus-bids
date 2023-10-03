package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.api.Enums.EnumCookingAccessory;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingLidUsage;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CookingRecipe {

    private final FluidStack inputFluidStack;
    private final FluidStack outputFluidStack;
    private final FluidStack secondaryOutputFluidStack;
    private final ItemStack inputItemStack;
    private final ItemStack outputItemStack;
    private final EnumCookingAccessory accessory;
    private final EnumCookingLidUsage lidUsage;
    private final EnumCookingHeatLevel minHeatLevel;
    private final EnumCookingHeatLevel maxHeatLevel;
    private final long time;

    public CookingRecipe(FluidStack inputFluidStack, FluidStack outputFluidStack, FluidStack secondaryOutputFluidStack, ItemStack inputItemStack, ItemStack outputItemStack, EnumCookingAccessory accessory, EnumCookingLidUsage lidUsage, EnumCookingHeatLevel minHeatLevel, EnumCookingHeatLevel maxHeatLevel, long time) {
        this.inputFluidStack = inputFluidStack;
        this.outputFluidStack = outputFluidStack;
        this.secondaryOutputFluidStack = secondaryOutputFluidStack;
        this.inputItemStack = inputItemStack;
        this.outputItemStack = outputItemStack;
        this.accessory = accessory;
        this.lidUsage = lidUsage;
        this.minHeatLevel = minHeatLevel;
        this.maxHeatLevel = maxHeatLevel;
        this.time = time;
    }

    public FluidStack getInputFluidStack() {
        return inputFluidStack;
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

    public ItemStack getOutputItemStack() {
        return outputItemStack;
    }

    public EnumCookingAccessory getAccessory() {
        return accessory;
    }

    public EnumCookingLidUsage getLidUsage() {
        return lidUsage;
    }

    public EnumCookingHeatLevel getMinHeatLevel() {
        return minHeatLevel;
    }

    public EnumCookingHeatLevel getMaxHeatLevel() {
        return maxHeatLevel;
    }

    public long getTime() {
        return time;
    }

    public boolean matchesTemplate(CookingRecipe template) {
        return doesInputFluidMatch(template) &&
            doesInputItemMatch(template) &&
            doesMinHeatLevelMatch(template) &&
            doesMaxHeatLevelMatch(template) &&
            doesLidUsageMatch(template) &&
            doesAccessoryMatch(template);
    }

    private boolean doesInputFluidMatch(CookingRecipe template) {
        boolean match = getInputFluidStack() == null && template.getInputFluidStack() == null ||
            getInputFluidStack() != null && template.getInputFluidStack() != null && getInputFluidStack().isFluidEqual(template.getInputFluidStack()) ||
            getInputFluidStack() == null && template.getInputFluidStack() != null && getOutputFluidStack().isFluidEqual(template.getInputFluidStack());
        //Bids.LOG.info("doesInputFluidMatch: " + match);

        return match;
    }

    private boolean doesInputItemMatch(CookingRecipe template) {
        boolean match = getInputItemStack() == null && template.getInputItemStack() == null ||
            getInputItemStack() != null && template.getInputItemStack() != null && getInputItemStack().isItemEqual(template.getInputItemStack())
                && getInputItemStack().getItemDamage() == template.getInputItemStack().getItemDamage();
        //Bids.LOG.info("doesInputItemMatch: " + match);

        return match;
    }

    private boolean doesMinHeatLevelMatch(CookingRecipe template) {
        boolean match = getMinHeatLevel() == null || getMinHeatLevel().compareTo(template.getMinHeatLevel()) <= 0;
        //Bids.LOG.info("doesMinHeatLevelMatch: " + match);

        return match;
    }

    private boolean doesMaxHeatLevelMatch(CookingRecipe template) {
        boolean match = getMaxHeatLevel() == null || getMaxHeatLevel().compareTo(template.getMaxHeatLevel()) >= 0;
        //Bids.LOG.info("doesMaxHeatLevelMatch: " + match);

        return match;
    }

    private boolean doesLidUsageMatch(CookingRecipe template) {
        boolean match = getLidUsage() == null || getLidUsage() == template.getLidUsage();
        //Bids.LOG.info("doesLidUsageMatch: " + match);

        return match;
    }

    private boolean doesAccessoryMatch(CookingRecipe template) {
        boolean match = getAccessory() == null || getAccessory() == template.getAccessory();
        //Bids.LOG.info("doesAccessoryMatch: " + match);

        return match;
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
        return getInputFluidStack() != null && getInputFluidStack().isFluidEqual(fluidStack);
    }

    public boolean matchesInput(ItemStack itemStack) {
        return getInputItemStack() != null && getInputItemStack().isItemEqual(itemStack)
            && getInputItemStack().getItemDamage() == itemStack.getItemDamage();
    }
}
