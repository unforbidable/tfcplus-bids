package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.api.Crafting.Builders.CookingCheeseRecipeBuilder;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingAccessory;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingLidUsage;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CookingCheeseRecipe extends CookingRecipe {

    protected final boolean allowInfusion;

    public CookingCheeseRecipe(FluidStack inputFluidStack, FluidStack secondaryInputFluidStack, FluidStack outputFluidStack, FluidStack secondaryOutputFluidStack, ItemStack inputItemStack, ItemStack outputItemStack, EnumCookingAccessory accessory, EnumCookingLidUsage lidUsage, EnumCookingHeatLevel minHeatLevel, EnumCookingHeatLevel maxHeatLevel, float time, boolean fixedTime, boolean allowInfusion) {
        super(inputFluidStack, secondaryInputFluidStack, outputFluidStack, secondaryOutputFluidStack, inputItemStack, outputItemStack, accessory, lidUsage, minHeatLevel, maxHeatLevel, time, fixedTime);
        this.allowInfusion = allowInfusion;
    }

    @Override
    protected boolean doesInputItemMatch(CookingRecipe template) {
        return template.getInputItemStack() == null ||
            allowInfusion && template.getInputFluidStack() != null &&
                canInfuseCheeseWithItemStack(template);
    }

    protected boolean canInfuseCheeseWithItemStack(CookingRecipe template) {
        if (template.getInputItemStack().getItem() instanceof IFood) {
            // Maximum allowed infuser weight is 20 per 10000 mB
            float maxInfuserWeight = template.getInputFluidStack().amount / 10000f * (Global.FOOD_MAX_WEIGHT / 8);
            IFood food = (IFood) template.getInputItemStack().getItem();
            return food.getFoodGroup() != EnumFoodGroup.Dairy &&
                food.isEdible(template.getInputItemStack()) &&
                Food.getWeight(template.getInputItemStack()) <= maxInfuserWeight;
        }

        return false;
    }

    @Override
    public CookingRecipeCraftingResult getCraftingResult(CookingRecipe template) {
        CookingRecipeCraftingResult result = super.getCraftingResult(template);

        if (allowInfusion && template.getInputItemStack() != null && template.getInputFluidStack() != null) {
            ItemStack cheese = result.getOutputItemStack();
            ItemStack infuser = template.getInputItemStack();
            if (infuser != null && infuser.getItem() instanceof IFood) {
                float infuserWeight = Food.getWeight(infuser);
                float infuserDecay = Food.getDecay(infuser);
                float infuserWeightWithoutDecay = infuserDecay > 0 ? infuserWeight - infuserDecay : infuserWeight;
                float infuserWeightToBarrelRatio = infuserWeightWithoutDecay / (Global.FOOD_MAX_WEIGHT / 8);
                float infuserWeightActualRatio = infuserWeightToBarrelRatio * (10000f / template.getInputFluidStack().amount);

                int[] profile = Food.getFoodTasteProfile(template.getInputItemStack());
                Food.setSweetMod(cheese, (int) Math.floor(profile[0] * infuserWeightActualRatio));
                Food.setSourMod(cheese, (int) Math.floor(profile[1] * infuserWeightActualRatio));
                Food.setSaltyMod(cheese, (int) Math.floor(profile[2] * infuserWeightActualRatio));
                Food.setBitterMod(cheese, (int) Math.floor(profile[3] * infuserWeightActualRatio));
                Food.setSavoryMod(cheese, (int) Math.floor(profile[4] * infuserWeightActualRatio));
                Food.setInfusion(cheese, infuser.getItem().getUnlocalizedName());
            }
        }

        return result;
    }

    public static CookingCheeseRecipeBuilder builder() {
        return new CookingCheeseRecipeBuilder();
    }

}
