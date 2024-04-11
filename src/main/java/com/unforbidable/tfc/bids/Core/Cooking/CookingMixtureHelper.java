package com.unforbidable.tfc.bids.Core.Cooking;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Items.ItemDrink;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.FoodRegistry;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Crafting.CookingManager;
import com.unforbidable.tfc.bids.api.Crafting.CookingMixture;
import com.unforbidable.tfc.bids.api.Interfaces.ICookingIngredientOverride;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class CookingMixtureHelper {

    public static void addCookingFluidMergedFluid(FluidStack cookingFluid, FluidStack mergedFluid) {
        CookingMixtureInfo info = CookingMixtureInfo.loadCookingMixtureInfoFromNBT(cookingFluid.tag);

        info.getMergedFluids().add(mergedFluid);

        float weight = info.getMixtureWeight();
        float addedWeight = 80f * (mergedFluid.amount / 1000f);
        info.setMixtureWeight(weight + addedWeight);

        info.writeCookingMixtureInfoToNBT(cookingFluid.tag);
    }

    public static List<FluidStack> getCookingFluidMergedFluids(FluidStack cookingFluid) {
        return CookingMixtureInfo.loadCookingMixtureInfoFromNBT(cookingFluid.tag)
            .getMergedFluids();
    }

    public static List<FluidStack> getCookedMealFluids(ItemStack cookedMeal) {
        return CookingMixtureInfo.loadCookingMixtureInfoFromNBT(cookedMeal.getTagCompound())
            .getMergedFluids();
    }

    public static FluidStack createCookingMixtureFluidStack(String mixtureName, int amount) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("mixtureName", mixtureName);
        return new FluidStack(BidsFluids.COOKINGMIXTURE, amount, tag);
    }

    public static ItemStack createCookingMixtureItemStack(String mixtureName) {
        ItemStack is = new ItemStack(BidsItems.cookingMixture);
        ItemFoodTFC.createTag(is);
        is.getTagCompound().setString("mixtureName", mixtureName);
        return is;
    }

    public static String getCookingMixtureName(FluidStack fs) {
        if (fs.tag != null && fs.tag.hasKey("mixtureName")) {
            return fs.tag.getString("mixtureName");
        } else {
            return null;
        }
    }

    public static String getCookingMixtureName(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().hasKey("mixtureName")) {
            return is.getTagCompound().getString("mixtureName");
        } else {
            return null;
        }
    }

    public static boolean areCookingMixtureNamesEqual(FluidStack one, FluidStack other) {
        String oneName = getCookingMixtureName(one);
        String otherName = getCookingMixtureName(other);
        return oneName != null && oneName.equals(otherName);
    }

    public static CookingMixture getCookingMixture(FluidStack fs) {
        String mixtureName = getCookingMixtureName(fs);
        if (mixtureName != null) {
            return CookingManager.getCookingMixture(mixtureName);
        } else {
            return null;
        }
    }

    public static CookingMixture getCookingMixture(ItemStack is) {
        String mixtureName = getCookingMixtureName(is);
        if (mixtureName != null) {
            return CookingManager.getCookingMixture(mixtureName);
        } else {
            return null;
        }
    }

    public static void initCookingMixtureTags(FluidStack cookingFluid, ItemStack cookingMix) {
        CookingMixtureInfo info = CookingMixtureInfo.loadCookingMixtureInfoFromNBT(cookingMix.getTagCompound());

        // Cooking mix food weight as mixture weight
        info.setMixtureWeight(Food.getWeight(cookingMix));
        info.writeCookingMixtureInfoToNBT(cookingFluid.tag);
    }

    public static void combineCookingMixtureTags(FluidStack cookingFluid, FluidStack combinedFluid) {
        CookingMixtureInfo cookingFluidInfo = CookingMixtureInfo.loadCookingMixtureInfoFromNBT(cookingFluid.tag);
        CookingMixtureInfo combinedFluidInfo = CookingMixtureInfo.loadCookingMixtureInfoFromNBT(combinedFluid.tag);

        cookingFluidInfo.setMixtureWeight(cookingFluidInfo.getMixtureWeight() + combinedFluidInfo.getMixtureWeight());
        cookingFluidInfo.writeCookingMixtureInfoToNBT(cookingFluid.tag);
    }

    public static void copyCookingMixtureTags(FluidStack to, FluidStack from) {
        CookingMixtureInfo info = CookingMixtureInfo.loadCookingMixtureInfoFromNBT(from.tag);
        info.writeCookingMixtureInfoToNBT(to.tag);
    }

    public static void copyCookingMixtureTags(ItemStack cookedMeal, FluidStack cookingFluid) {
        CookingMixtureInfo info = CookingMixtureInfo.loadCookingMixtureInfoFromNBT(cookingFluid.tag);

        // Don't write mixture weight into cookedMeal
        info.setMixtureWeight(0);
        info.writeCookingMixtureInfoToNBT(cookedMeal.getTagCompound());
    }

    public static boolean canCombineCookingFluids(FluidStack one, FluidStack other) {
        // Cooking fluids must be the same fluid and fluid ID
        if (!areCookingMixtureNamesEqual(one, other)) {
            return false;
        }

        // Cooking fluids that were merged with other fluids cannot be combined
        if (getCookingFluidMergedFluids(one).size() > 0 ||
            getCookingFluidMergedFluids(other).size() > 0) {
            return false;
        }

        // FG must match
        int[] oneFoodGroups = one.tag.getIntArray("FG");
        int[] otherFoodGroups = other.tag.getIntArray("FG");
        if (oneFoodGroups.length == otherFoodGroups.length) {
            for (int i = 0; i < oneFoodGroups.length; i++) {
                if (oneFoodGroups[i] != otherFoodGroups[i]) {
                    return false;
                }
            }
        }

        // We can expect tastes to be equal

        return true;
    }

    public static Item getMainIngredient(FluidStack fs) {
        if (fs.tag != null) {
            int[] fg = fs.tag.getIntArray("FG");
            if (fg != null && fg.length > 0) {
                return getIngredient(fg[0]);
            }
        }

        return null;
    }

    public static Item getMainIngredient(ItemStack is) {
        if (is.hasTagCompound()) {
            int[] fg = is.getTagCompound().getIntArray("FG");
            if (fg != null && fg.length > 0) {
                return getIngredient(fg[0]);
            }
        }

        return null;
    }

    private static Item getIngredient(int ingredientFoodId) {
        Item ingredient = FoodRegistry.getInstance().getFood(ingredientFoodId);
        if (ingredient != null) {
            // Add main ingredient name after the fluid name
            return getIngredientOverride(ingredient);
        }

        return null;
    }

    private static Item getIngredientOverride(Item ingredient) {
        Item registeredOverride = CookingManager.getCookingIngredientOverride(ingredient);
        if (registeredOverride != null) {
            return registeredOverride;
        }

        if (ingredient instanceof ICookingIngredientOverride) {
            return ((ICookingIngredientOverride) ingredient).getIngredientOverride();
        }

        return ingredient;
    }

    public static EnumFoodGroup getFluidFoodGroup(FluidStack fluid) {
        // Try to put the fluid into a bottle
        // and if it is drinkable, get the food group
        // At the time of writing, only milk and beer have food group
        ItemStack fluidBottle = FluidContainerRegistry.fillFluidContainer(fluid, new ItemStack(TFCItems.glassBottle));
        if (fluidBottle != null && fluidBottle.getItem() instanceof ItemDrink) {
            return ((ItemDrink) fluidBottle.getItem()).getFoodGroup();
        }

        return null;
    }

}
