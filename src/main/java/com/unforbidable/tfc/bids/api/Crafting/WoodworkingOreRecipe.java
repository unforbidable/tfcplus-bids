package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class WoodworkingOreRecipe extends WoodworkingRecipe {

    private final String inputOreName;

    public WoodworkingOreRecipe(String planName, String inputOreName, ItemStack output) {
        super(planName, null, output);
        this.inputOreName = inputOreName;
    }

    public String getInputOreName() {
        return inputOreName;
    }

    @Override
    public boolean matches(ItemStack ingredient) {
        int requiredOreId = OreDictionary.getOreID(inputOreName);
        for (int id : OreDictionary.getOreIDs(ingredient)) {
            if (id == requiredOreId) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<ItemStack> getIngredients() {
        return OreDictionary.getOres(inputOreName);
    }

}
