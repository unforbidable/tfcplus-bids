package com.unforbidable.tfc.bids.features.crafting.cooking.main;

import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeCraftingResult;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeInputTemplate;
import com.unforbidable.tfc.bids.api.util.fluid.DualFluid;

public class CookingRecipeHelper {

    public static String getRecipeOutputDisplayText(CookingRecipe recipe, CookingRecipeInputTemplate template) {
        CookingRecipeCraftingResult result = recipe.getCraftingResult(template);

        if (result.getOutputItemStack() != null) {
            return result.getOutputItemStack().getDisplayName();
        } else if (result.getOutputFluidStack() != null) {
            if (DualFluid.isDualFluidStack(result.getOutputFluidStack())) {
                DualFluid recipeOutputDualFluid = DualFluid.of(result.getOutputFluidStack());
                return recipeOutputDualFluid.getPrimaryFluid().getLocalizedName() + " + " + recipeOutputDualFluid.getSecondaryFluid().getLocalizedName();
            } else {
                return result.getOutputFluidStack().getLocalizedName();
            }
        }

        return "N/A";
    }

    public static String getRecipeHashString(CookingRecipe recipe) {
        StringBuilder stringBuilder = new StringBuilder();

        if (recipe.getInputFluidStack() != null) {
            stringBuilder.append(recipe.getInputFluidStack().getUnlocalizedName())
                .append(":")
                .append(recipe.getInputFluidStack().amount);
        } else {
            stringBuilder.append("NULL");
        }
        stringBuilder.append(",");

        if (recipe.getSecondaryInputFluidStack() != null) {
            stringBuilder.append(recipe.getSecondaryInputFluidStack().getUnlocalizedName())
                .append(":")
                .append(recipe.getSecondaryInputFluidStack().amount);
        } else {
            stringBuilder.append("NULL");
        }
        stringBuilder.append(",");

        if (recipe.getOutputFluidStack() != null) {
            if (DualFluid.isDualFluidStack(recipe.getOutputFluidStack())) {
                DualFluid recipeOutputDualFluid = DualFluid.of(recipe.getOutputFluidStack());
                stringBuilder.append(recipeOutputDualFluid.getPrimaryFluid().getUnlocalizedName())
                    .append(":")
                    .append(recipeOutputDualFluid.getPrimaryFluid().amount);
                stringBuilder.append(",");
                stringBuilder.append(recipeOutputDualFluid.getSecondaryFluid().getUnlocalizedName())
                    .append(":")
                    .append(recipeOutputDualFluid.getSecondaryFluid().amount);
            } else {
                stringBuilder.append(recipe.getOutputFluidStack().getUnlocalizedName())
                    .append(":")
                    .append(recipe.getOutputFluidStack().amount);
                stringBuilder.append(",");
                stringBuilder.append("NULL");
            }
        } else {
            stringBuilder.append("NULL");
        }
        stringBuilder.append(",");

        if (recipe.getInputItemStack() != null) {
            stringBuilder.append(recipe.getInputItemStack().getUnlocalizedName())
                .append(":")
                .append(recipe.getInputItemStack().stackSize);
        } else {
            stringBuilder.append("NULL");
        }
        stringBuilder.append(",");

        if (recipe.getOutputItemStack() != null) {
            stringBuilder.append(recipe.getOutputItemStack().getUnlocalizedName())
                .append(":")
                .append(recipe.getOutputItemStack().stackSize);
        } else {
            stringBuilder.append("NULL");
        }
        stringBuilder.append(",");

        if (recipe.getAccessory() != null) {
            stringBuilder.append(recipe.getAccessory());
        } else {
            stringBuilder.append("NULL");
        }
        stringBuilder.append(",");

        if (recipe.getLidUsage() != null) {
            stringBuilder.append(recipe.getLidUsage());
        } else {
            stringBuilder.append("NULL");
        }

        return stringBuilder.toString();
    }

}
