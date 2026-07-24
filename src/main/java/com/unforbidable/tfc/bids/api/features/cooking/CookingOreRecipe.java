package com.unforbidable.tfc.bids.api.features.cooking;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class CookingOreRecipe extends CookingRecipe {

    private final String inputOreName;

    public CookingOreRecipe(FluidStack inputFluidStack, FluidStack secondaryInputFluidStack, FluidStack outputFluidStack, FluidStack secondaryOutputFluidStack, String inputOreName, ItemStack outputItemStack, CookingAccessory accessory, CookingLidUsage lidUsage, CookingHeatLevel minHeatLevel, CookingHeatLevel maxHeatLevel, float time, boolean fixedTime) {
        super(inputFluidStack, secondaryInputFluidStack, outputFluidStack, secondaryOutputFluidStack, null, outputItemStack, accessory, lidUsage, minHeatLevel, maxHeatLevel, time, fixedTime);

        this.inputOreName = inputOreName;
    }

    public String getInputOreName() {
        return inputOreName;
    }

    @Override
    public List<ItemStack> getInputItemStacks() {
        return OreDictionary.getOres(inputOreName);
    }

    @Override
    protected boolean doesInputItemMatch(CookingRecipeInputTemplate template) {
        if (template.getInputItemStack() != null) {
            for (ItemStack ore : OreDictionary.getOres(inputOreName)) {
                boolean match = OreDictionary.itemMatches(ore, template.getInputItemStack(), false);
                //Bids.LOG.info("doesInputItemMatch: " + match);

                if (match) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean matchesInput(ItemStack itemStack) {
        for (ItemStack ore : getInputItemStacks()) {
            boolean match = OreDictionary.itemMatches(ore, itemStack, false);
            //Bids.LOG.info("doesInputItemMatch: " + match);

            if (match) {
                return true;
            }
        }

        return false;
    }

}
