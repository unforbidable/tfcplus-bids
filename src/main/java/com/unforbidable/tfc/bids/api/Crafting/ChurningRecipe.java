package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ChurningRecipe {

    private final ItemStack output;
    private final FluidStack input;
    private final float duration;

    public ChurningRecipe(ItemStack output, FluidStack input, float duration) {
        this.output = output;
        this.input = input;
        this.duration = duration;
    }

    public ItemStack getOutput() {
        return output;
    }

    public FluidStack getInput() {
        return input;
    }

    public float getDuration() {
        return duration;
    }

    public ItemStack getResult(FluidStack fs) {
        int runs = fs.amount / input.amount;
        if (output.getItem() instanceof IFood) {
            float singleRunWeight = Food.getWeight(output);
            return ItemFoodTFC.createTag(output.copy(), singleRunWeight * runs);
        } else {
            ItemStack result = output.copy();
            result.stackSize *= runs;
            return result;
        }
    }

    public float getTotalDuration(FluidStack fs) {
        int runs = fs.amount / input.amount;
        return runs * duration;
    }

    public boolean matches(FluidStack fs) {
        return inputFluidMatches(fs.getFluid())
            && inputAmountMatches(fs.amount);
    }

    protected boolean inputFluidMatches(Fluid fluid) {
        return fluid == input.getFluid();
    }

    protected boolean inputAmountMatches(int amount) {
        return amount % input.amount == 0;
    }

}
