package com.unforbidable.tfc.bids.Core.Cooking;

import com.unforbidable.tfc.bids.api.Crafting.CookingRecipe;

public class CookingRecipeHelper {

    public static String getRecipeOutputDisplayText(CookingRecipe recipe) {
        if (recipe.getOutputItemStack() != null) {
            return recipe.getOutputItemStack().getDisplayName();
        } else if (recipe.getOutputFluidStack() != null) {
            if (recipe.getSecondaryOutputFluidStack() != null) {
                return recipe.getSecondaryOutputFluidStack().getLocalizedName() + " + " + recipe.getOutputFluidStack().getLocalizedName();
            } else {
                return recipe.getOutputFluidStack().getLocalizedName();
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
            stringBuilder.append(recipe.getOutputFluidStack().getUnlocalizedName())
                .append(":")
                .append(recipe.getOutputFluidStack().amount);
        } else {
            stringBuilder.append("NULL");
        }
        stringBuilder.append(",");

        if (recipe.getSecondaryOutputFluidStack() != null) {
            stringBuilder.append(recipe.getSecondaryOutputFluidStack().getUnlocalizedName())
                .append(":")
                .append(recipe.getSecondaryOutputFluidStack().amount);
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
