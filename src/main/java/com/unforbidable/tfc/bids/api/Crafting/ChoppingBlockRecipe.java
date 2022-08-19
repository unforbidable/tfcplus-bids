package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ChoppingBlockRecipe {

    final int choppingBlockId;
    final ItemStack output;
    final String toolOreName;
    final ItemStack input;

    public ChoppingBlockRecipe(String choppingBlockName, ItemStack output, String toolOreName, ItemStack input) {
        this.choppingBlockId = ChoppingBlockManager.getChoppingBlockId(choppingBlockName);
        this.output = output;
        this.toolOreName = toolOreName;
        this.input = input;
    }

    public int getWorkbenchId() {
        return choppingBlockId;
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

    public boolean match(ItemStack tool, ItemStack ingredient) {
        return matchTool(tool) && matchInput(ingredient);
    }

    private boolean matchInput(ItemStack ingredient) {
        return input.getItem() == ingredient.getItem() && (input.getItemDamage() == ingredient.getItemDamage()
                || input.getItemDamage() == OreDictionary.WILDCARD_VALUE);
    }

    private boolean matchTool(ItemStack tool) {
        for (ItemStack item : OreDictionary.getOres(toolOreName, false)) {
            if (tool.getItem() == item.getItem()) {
                return true;
            }
        }

        return false;
    }

}
