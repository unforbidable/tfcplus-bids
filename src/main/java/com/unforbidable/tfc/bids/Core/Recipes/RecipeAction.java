package com.unforbidable.tfc.bids.Core.Recipes;

import java.util.ArrayList;
import java.util.List;

import com.unforbidable.tfc.bids.Bids;

import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public abstract class RecipeAction {

    protected Item craftingItem;
    protected Block craftingBlock;
    protected int craftingItemDamage = OreDictionary.WILDCARD_VALUE;
    protected List<ItemStack> ingredients = new ArrayList<ItemStack>();
    protected List<String> ingredientOreNames = new ArrayList<String>();

    public RecipeAction() {
    }

    public RecipeAction matchCraftingItem(Item craftingItem) {
        this.craftingItem = craftingItem;
        return this;
    }

    public RecipeAction matchCraftingItem(Item craftingItem, int craftingItemDamage) {
        this.craftingItem = craftingItem;
        this.craftingItemDamage = craftingItemDamage;
        return this;
    }

    public RecipeAction matchCraftingBlock(Block craftingBlock) {
        this.craftingBlock = craftingBlock;
        return this;
    }

    public RecipeAction matchIngredient(Item ingredient) {
        ingredients.add(new ItemStack(ingredient, 1, OreDictionary.WILDCARD_VALUE));
        return this;
    }

    public RecipeAction matchIngredient(Item ingredient, int damage) {
        ingredients.add(new ItemStack(ingredient, 1, damage));
        return this;
    }

    public RecipeAction matchIngredient(String ingredientOreName) {
        ingredientOreNames.add(ingredientOreName);
        return this;
    }

    public boolean eventMatches(ItemCraftedEvent event) {
        return craftingMatches(event.crafting) &&
                ingredientsMatch(event.craftMatrix);
    }

    private boolean craftingMatches(ItemStack itemStack) {
        return craftingMatchesItem(itemStack.getItem()) &&
                craftingMatchesItemDamage(itemStack.getItemDamage())
                || craftingMatchesBlock(Block.getBlockFromItem(itemStack.getItem()));
    }

    private boolean craftingMatchesItemDamage(int damage) {
        return craftingItemDamage == OreDictionary.WILDCARD_VALUE || craftingItemDamage == damage;
    }

    private boolean craftingMatchesBlock(Block block) {
        return craftingBlock != null && block == craftingBlock;
    }

    private boolean craftingMatchesItem(Item item) {
        return craftingItem != null && item == craftingItem;
    }

    private boolean ingredientsMatch(IInventory inv) {
        int ingredientsFound = 0;
        int ingredientOresFound = 0;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack is = inv.getStackInSlot(i);
            if (is != null) {
                for (ItemStack ingredient : ingredients) {
                    if (is.getItem() == ingredient.getItem() &&
                        (is.getItemDamage() == ingredient.getItemDamage() || ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                        Bids.LOG.debug("Match ingredient: " + is.getDisplayName());

                        ingredientsFound++;

                        break;
                    }
                }

                for (String oreName : ingredientOreNames) {
                    if (ingredientMatchesOreName(is, oreName)) {
                        Bids.LOG.debug("Match ore ingredient: " + is.getDisplayName());

                        ingredientOresFound++;

                        break;
                    }
                }
            }
        }

        Bids.LOG.debug("Expected ingredients to match: " + ingredients.size() + " found: " + ingredientsFound);
        Bids.LOG.debug("Expected ore ingredients to match: " + ingredientOreNames.size() + " found: " + ingredientOresFound);

        return ingredientsFound == ingredients.size()
                && ingredientOresFound == ingredientOreNames.size();
    }

    private boolean ingredientMatchesOreName(ItemStack is, String oreName) {
        List<ItemStack> ores = OreDictionary.getOres(oreName, false);
        for (ItemStack ore : ores) {
            if (is.getItem() == ore.getItem()) {
                return true;
            }
        }

        return false;
    }

    public void onItemCrafted(ItemCraftedEvent event) {
    }

}
