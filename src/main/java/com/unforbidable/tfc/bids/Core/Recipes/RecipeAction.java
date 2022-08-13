package com.unforbidable.tfc.bids.Core.Recipes;

import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public abstract class RecipeAction {

    protected Item craftingItem;
    protected Block craftingBlock;
    protected int craftingItemDamage = OreDictionary.WILDCARD_VALUE;

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

    public boolean craftingMatches(ItemStack itemStack) {
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

    public boolean craftingMatchesItem(Item item) {
        return craftingItem != null && item == craftingItem;
    }

    public void onItemCrafted(ItemCraftedEvent event) {
    }

}
