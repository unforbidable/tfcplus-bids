package com.unforbidable.tfc.bids.Core.Recipes;

import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class RecipeAction {

    protected Item craftingItem;
    protected Block craftingBlock;

    public RecipeAction() {
    }

    public RecipeAction matchCraftingItem(Item craftingItem) {
        this.craftingItem = craftingItem;
        return this;
    }

    public RecipeAction matchCraftingBlock(Block craftingBlock) {
        this.craftingBlock = craftingBlock;
        return this;
    }

    public boolean craftingMatches(ItemStack itemStack) {
        return craftingMatchesItem(itemStack.getItem())
                || craftingMatchesBlock(Block.getBlockFromItem(itemStack.getItem()));
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
