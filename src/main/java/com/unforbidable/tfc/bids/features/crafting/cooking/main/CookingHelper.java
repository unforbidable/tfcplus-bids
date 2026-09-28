package com.unforbidable.tfc.bids.features.crafting.cooking.main;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.Interfaces.ICookableFood;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.features.cooking.CookingOreRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeInputTemplate;
import com.unforbidable.tfc.bids.api.util.food.BidsFood;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import com.unforbidable.tfc.bids.api.util.fluid.DualFluid;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

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

    public static List<CookingRecipe> getRecipesMatchingTemplate(CookingRecipeInputTemplate template) {
        List<CookingRecipe> matches = new ArrayList<>();

        for (CookingRecipe recipe : CookingRegistry.recipes) {
            if (recipe.matchesTemplate(template)) {
                if (template.getInputItemStack() != null && recipe instanceof CookingOreRecipe) {
                    matches.add(flattenCookingOreRecipe((CookingOreRecipe) recipe, template.getInputItemStack()));
                } else {
                    matches.add(recipe);
                }
            }
        }

        return matches;
    }

    public static List<CookingRecipe> getRecipesMatchingInput(ItemStack inputItemStack) {
        List<CookingRecipe> matches = new ArrayList<>();

        for (CookingRecipe recipe : CookingRegistry.recipes) {
            if (recipe.matchesInput(inputItemStack)) {
                if (recipe instanceof CookingOreRecipe) {
                    matches.add(flattenCookingOreRecipe((CookingOreRecipe) recipe, inputItemStack));
                } else {
                    matches.add(recipe);
                }
            }
        }

        return matches;
    }

    public static CookingRecipe flattenCookingOreRecipe(CookingOreRecipe recipe, ItemStack inputItemStack) {
        // Cooking ore recipes are processed as normal cooking recipes
        // with input item stack resolved to ore ingredient that matches the actual input stack
        return new CookingRecipe(recipe.getInputFluidStack(), recipe.getSecondaryInputFluidStack(),
            recipe.getOutputFluidStack(),
            getOreMatchingInputItemStack(recipe.getInputOreName(), inputItemStack), recipe.getOutputItemStack(),
            recipe.getAccessory(), recipe.getLidUsage(),
            recipe.getMinHeatLevel(), recipe.getMaxHeatLevel(),
            recipe.getTime(), recipe.isFixedTime());
    }

    private static ItemStack getOreMatchingInputItemStack(String oreName, ItemStack inputItemStack) {
        for (ItemStack ore : OreDictionary.getOres(oreName)) {
            if (OreDictionary.itemMatches(ore, inputItemStack, false)) {
                return ore;
            }
        }

        return null;
    }

    public static ItemStack doDrainAmount(ItemStack itemStack, int requiredAmount) {
        int drainedAmount = 0;
        ItemStack drainedItemStack = itemStack;
        while (drainedAmount < requiredAmount && FluidContainerRegistry.isFilledContainer(drainedItemStack)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(drainedItemStack);

            int availableAmount = fluidStack.amount;
            drainedItemStack = FluidContainerRegistry.drainFluidContainer(drainedItemStack);
            drainedAmount += availableAmount;
        }

        return drainedItemStack;
    }

    public static int canDrainAmount(ItemStack itemStack, int requiredAmount) {
        int drainedAmount = 0;
        int tolerance = FluidContainerRegistry.getContainerCapacity(itemStack) / 2;
        ItemStack drainedItemStack = itemStack;
        FluidStack originalFluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
        while (drainedAmount + tolerance < requiredAmount && FluidContainerRegistry.isFilledContainer(drainedItemStack)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(drainedItemStack);
            // Ensure the fluid is still the same
            if (!originalFluidStack.isFluidEqual(fluidStack)) {
                break;
            }

            int availableAmount = fluidStack.amount;
            drainedItemStack = FluidContainerRegistry.drainFluidContainer(drainedItemStack);
            drainedAmount += availableAmount;
        }

        // Ensure the exact amount was drained
        if (drainedAmount <= requiredAmount + tolerance) {
            return drainedAmount;
        } else {
            return 0;
        }
    }

    public static boolean isCookableFoodItemStack(ItemStack itemStack) {
        // Only allow cooking food items with heat index
        HeatIndex hi = HeatRegistry.getInstance().findMatchingIndex(itemStack);
        if (itemStack.getItem() instanceof ItemFoodTFC
            && itemStack.getItem() instanceof ICookableFood
            && hi != null) {

            // Check heat registry to ensure there is no output or meltTemp lower than 160
            // The assumption is that the output is for baking (cooking directly in the fire), such as bread
            // which should not be possible with boiling or steaming in a cooking pot
            // Therefore food that turns into another item when cooked, cannot be boiled
            // Grain based food can still be cooked, such pasta, as long as it's the same item
            // See weed can be cooked, even though it turns into soda ash
            // because cooking pot cannot reach the temperate at which it burns
            return !hi.hasOutput() || hi.meltTemp > 150;
        }

        return false;
    }


    public static String getFluidInfoLine(FluidStack fluidStack) {
        if (DualFluid.isDualFluidStack(fluidStack)) {
            FluidStack primary = DualFluid.of(fluidStack)
                .getPrimaryFluid();
            return primary.getFluid().getLocalizedName(primary) + " " + primary.amount + " mB";
        } else {
            return fluidStack.getFluid().getLocalizedName(fluidStack) + " " + fluidStack.amount + " mB";
        }
    }

    public static String getItemInfoLine(ItemStack itemStack) {
        String stackSizeInfo = itemStack.stackSize > 1 ? (itemStack.stackSize + "x ") : "";
        return stackSizeInfo + itemStack.getDisplayName();
    }

    public static boolean isDilutableRecipeOutputFluid(FluidStack outputFluidStack) {
        return CookingRegistry.dilutable.has(outputFluidStack.getFluid());
    }

}
