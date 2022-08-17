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
    protected List<Item> ingredients = new ArrayList<Item>();

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
        ingredients.add(ingredient);
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

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack is = inv.getStackInSlot(i);
            if (is != null) {
                for (Item ingredient : ingredients) {
                    if (is.getItem() == ingredient) {
                        ingredientsFound++;

                        Bids.LOG.info("Ingredient found: " + is.getDisplayName());
                    }
                }
            }
        }

        return ingredientsFound == ingredients.size();
    }

    public void onItemCrafted(ItemCraftedEvent event) {
    }

}
