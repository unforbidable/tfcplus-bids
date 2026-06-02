package com.unforbidable.tfc.bids.features.crafting.cooking.main;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.util.food.BidsFood;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CookingHelper {
    public static final int META_COOKING_POT_HAS_LID = 8;

    public static int getItemStackCookedLevel(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemFoodTFC && Food.isCooked(itemStack)) {
            ItemFoodTFC itemFoodTFC = (ItemFoodTFC)itemStack.getItem();
            int cookTempIndex = itemFoodTFC.cookTempIndex;
            return ((int) Food.getCooked(itemStack) - (int)Food.globalCookTemps[cookTempIndex]) / (int)(Food.globalCookTemps[cookTempIndex] / 5f) + 1;
        }

        return 0;
    }

    public static float getTempForItemStackCookedLevel(ItemStack itemStack, int level) {
        if (itemStack.getItem() instanceof ItemFoodTFC) {
            ItemFoodTFC itemFoodTFC = (ItemFoodTFC)itemStack.getItem();
            int cookTempIndex = itemFoodTFC.cookTempIndex;
            return Food.globalCookTemps[cookTempIndex] + (int)(Food.globalCookTemps[cookTempIndex] / 5f) * (level - 1);
        }

        return 0;
    }

    public static void setInputStackCookedNBT(ItemStack itemStack, FluidStack fluidStack, boolean steaming) {
        if (itemStack.getItem() instanceof ItemFoodTFC && Food.isCooked(itemStack)) {
            if (!steaming) {
                int[] profile = new int[5];
                for (int i = 0; i < 5; i++) {
                    profile[i] = -10;
                }

                if (fluidStack.getFluid() == TFCFluids.SALTWATER) {
                    profile[2] += 30;
                }

                Food.setCookedProfile(itemStack, profile);
                BidsFood.setBoiled(itemStack, true);
                BidsFood.setBoiledLiquid(itemStack, fluidStack.getUnlocalizedName());
            } else {
                BidsFood.setSteamed(itemStack, true);
            }
        }
    }

    public static int calculateRequiredCookingFluidAmount(float weight, boolean steaming, int cookedLevel) {
        // 1000 mB for a full stack of raw food to be cooked one level and 200 mB for each additional level
        int amount = Math.round(weight / 160 * 200);

        // Times 5 when then item is not yet cooked
        if (cookedLevel == 0) {
            amount *= 5;
        }

        // When steaming the amount is doubled
        if (steaming) {
            amount *= 2;
        }

        // Round up to multiple of 50 to avoid weird numbers
        return (int)Math.ceil(amount / 50f) * 50;
    }

    public static boolean isValidCookingFluid(FluidStack inputFluid, boolean steaming) {
        if (steaming) {
            return inputFluid.getFluid() == TFCFluids.FRESHWATER;
        } else {
            return inputFluid.getFluid() == TFCFluids.FRESHWATER || inputFluid.getFluid() == TFCFluids.SALTWATER;
        }
    }

    public static List<CookingRecipe> getRecipesMatchingTemplate(CookingRecipe template) {
        List<CookingRecipe> matches = new ArrayList<CookingRecipe>();

        for (CookingRecipe recipe : CookingRegistry.recipes) {
            if (recipe.matchesTemplate(template)) {
                matches.add(recipe);
            }
        }

        return matches;
    }

    public static List<CookingRecipe> getRecipesMatchingInput(ItemStack inputItemStack) {
        List<CookingRecipe> matches = new ArrayList<CookingRecipe>();

        for (CookingRecipe recipe : CookingRegistry.recipes) {
            if (recipe.matchesInput(inputItemStack)) {
                matches.add(recipe);
            }
        }

        return matches;
    }

}
