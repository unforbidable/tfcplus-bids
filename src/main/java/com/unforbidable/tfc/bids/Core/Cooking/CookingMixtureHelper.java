package com.unforbidable.tfc.bids.Core.Cooking;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.FoodRegistry;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Crafting.CookingManager;
import com.unforbidable.tfc.bids.api.Crafting.CookingMixture;
import com.unforbidable.tfc.bids.api.Interfaces.ICookingIngredientOverride;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class CookingMixtureHelper {

    public static void addCookingFluidMergedFluid(FluidStack cookingFluid, FluidStack mergedFluid) {
        List<FluidStack> list = getCookingFluidMergedFluids(cookingFluid);
        list.add(mergedFluid);

        if (cookingFluid.tag == null) {
            cookingFluid.tag = new NBTTagCompound();
        }

        NBTTagList fluidTagList = new NBTTagList();
        for (FluidStack fluid : list) {
            NBTTagCompound fluidTag = new NBTTagCompound();
            fluid.writeToNBT(fluidTag);
            fluidTagList.appendTag(fluidTag);
        }

        cookingFluid.tag.setTag("mergedFluids", fluidTagList);

        // Adding fluid raises the food weight by 80 oz per 1 bucket of fluid
        float weight = cookingFluid.tag.getFloat("foodWeight");
        float addedWeight = 80f * (mergedFluid.amount / 1000f);
        cookingFluid.tag.setFloat("foodWeight", weight + addedWeight);
    }

    public static List<FluidStack> getCookingFluidMergedFluids(FluidStack cookingFluid) {
        List<FluidStack> list = new ArrayList<FluidStack>();

        if (cookingFluid.tag != null) {
            NBTTagList tagList = cookingFluid.tag.getTagList("mergedFluids", 10);
            for (int i = 0; i < tagList.tagCount(); i++) {
                FluidStack fluid = FluidStack.loadFluidStackFromNBT(tagList.getCompoundTagAt(i));
                if (fluid != null) {
                    list.add(fluid);
                }
            }
        }

        return list;
    }

    public static List<FluidStack> getCookedMealFluids(ItemStack cookedMeal) {
        List<FluidStack> list = new ArrayList<FluidStack>();

        if (cookedMeal.hasTagCompound()) {
            NBTTagList tagList = cookedMeal.getTagCompound().getTagList("mergedFluids", 10);
            for (int i = 0; i < tagList.tagCount(); i++) {
                FluidStack fluid = FluidStack.loadFluidStackFromNBT(tagList.getCompoundTagAt(i));
                list.add(fluid);
            }
        }

        return list;
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
        cookingFluid.tag.setFloat("foodWeight", cookingMix.getTagCompound().getFloat("foodWeight"));
        cookingFluid.tag.setIntArray("FG", cookingMix.getTagCompound().getIntArray("FG"));
        cookingFluid.tag.setInteger("tasteSweet", cookingMix.getTagCompound().getInteger("tasteSweet"));
        cookingFluid.tag.setInteger("tasteSour", cookingMix.getTagCompound().getInteger("tasteSweet"));
        cookingFluid.tag.setInteger("tasteSalty", cookingMix.getTagCompound().getInteger("tasteSweet"));
        cookingFluid.tag.setInteger("tasteBitter", cookingMix.getTagCompound().getInteger("tasteSweet"));
        cookingFluid.tag.setInteger("tasteUmami", cookingMix.getTagCompound().getInteger("tasteSweet"));
    }

    public static void combineCookingMixtureTags(FluidStack cookingFluid, FluidStack combinedFluid) {
        float cookingFluidWeight = cookingFluid.tag.getFloat("foodWeight");
        float combinedFluidWeight = combinedFluid.tag.getFloat("foodWeight");
        cookingFluid.tag.setFloat("foodWeight", cookingFluidWeight + combinedFluidWeight);
    }

    public static void copyCookingMixtureTags(FluidStack to, FluidStack from) {
        to.tag.setFloat("foodWeight", from.tag.getFloat("foodWeight"));
        to.tag.setIntArray("FG", from.tag.getIntArray("FG"));
        to.tag.setInteger("tasteSweet", from.tag.getInteger("tasteSweet"));
        to.tag.setInteger("tasteSour", from.tag.getInteger("tasteSweet"));
        to.tag.setInteger("tasteSalty", from.tag.getInteger("tasteSweet"));
        to.tag.setInteger("tasteBitter", from.tag.getInteger("tasteSweet"));
        to.tag.setInteger("tasteUmami", from.tag.getInteger("tasteSweet"));
        to.tag.setTag("mergedFluids", from.tag.getTagList("mergedFluids", 10));
    }

    public static void copyCookingMixtureTags(ItemStack cookedMeal, FluidStack cookingFluid) {
        cookedMeal.getTagCompound().setIntArray("FG", cookingFluid.tag.getIntArray("FG"));
        cookedMeal.getTagCompound().setInteger("tasteSweet", cookingFluid.tag.getInteger("tasteSweet"));
        cookedMeal.getTagCompound().setInteger("tasteSour", cookingFluid.tag.getInteger("tasteSweet"));
        cookedMeal.getTagCompound().setInteger("tasteSalty", cookingFluid.tag.getInteger("tasteSweet"));
        cookedMeal.getTagCompound().setInteger("tasteBitter", cookingFluid.tag.getInteger("tasteSweet"));
        cookedMeal.getTagCompound().setInteger("tasteUmami", cookingFluid.tag.getInteger("tasteSweet"));
        cookedMeal.getTagCompound().setTag("mergedFluids", cookingFluid.tag.getTagList("mergedFluids", 10));
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

    public static String getMainIngredientName(FluidStack fs) {
        if (fs.tag != null) {
            int[] fg = fs.tag.getIntArray("FG");
            if (fg != null && fg.length > 0) {
                return getIngredientName(fg[0]);
            }
        }

        return null;
    }

    public static String getMainIngredientName(ItemStack is) {
        if (is.hasTagCompound()) {
            int[] fg = is.getTagCompound().getIntArray("FG");
            if (fg != null && fg.length > 0) {
                return getIngredientName(fg[0]);
            }
        }

        return null;
    }

    private static String getIngredientName(int ingredientFoodId) {
        Item ingredient = FoodRegistry.getInstance().getFood(ingredientFoodId);
        if (ingredient != null) {
            // Add main ingredient name after the fluid name
            Item ingredientOverride = getIngredientOverride(ingredient);
            return ingredientOverride.getItemStackDisplayName(new ItemStack(ingredientOverride));
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

}
