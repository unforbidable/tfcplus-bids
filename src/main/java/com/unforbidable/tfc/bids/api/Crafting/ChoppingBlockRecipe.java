package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ChoppingBlockRecipe {

    final String choppingBlockOreName;
    final ItemStack output;
    final String toolOreName;
    final ItemStack input;

    public ChoppingBlockRecipe(String choppingBlockOreName, String toolOreName, ItemStack output, ItemStack input) {
        this.choppingBlockOreName = choppingBlockOreName;
        this.toolOreName = toolOreName;
        this.output = output;
        this.input = input;
    }

    public String getChoppingBlockOreName() {
        return choppingBlockOreName;
    }

    public ItemStack getInput() {
        return input;
    }

    public String getToolOreName() {
        return toolOreName;
    }

    public ItemStack getCraftingResult(ItemStack ingredient) {
        return output.copy();
    }

    public boolean matches(ItemStack choppingBlock, ItemStack tool, ItemStack ingredient) {
        return matchesChoppingBlock(choppingBlock) && matchesTool(tool) && matchesInput(ingredient);
    }

    public boolean matchesAny(ItemStack choppingBlock, ItemStack tool, ItemStack ingredient) {
        return matchesChoppingBlock(choppingBlock) || matchesTool(tool) || matchesInput(ingredient);
    }

    public boolean matchesInput(ItemStack ingredient) {
        return input.getItem() == ingredient.getItem() && (input.getItemDamage() == ingredient.getItemDamage()
                || input.getItemDamage() == OreDictionary.WILDCARD_VALUE);
    }

    public boolean matchesChoppingBlock(ItemStack choppingBlock) {
        for (ItemStack item : OreDictionary.getOres(choppingBlockOreName, false)) {
            if (choppingBlock.getItem() == item.getItem()
                    && (choppingBlock.getItemDamage() == item.getItemDamage()
                            || item.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                return true;
            }
        }

        return false;
    }

    public boolean matchesTool(ItemStack tool) {
        for (ItemStack item : OreDictionary.getOres(toolOreName, false)) {
            if (tool.getItem() == item.getItem()) {
                return true;
            }
        }

        return false;
    }

}
