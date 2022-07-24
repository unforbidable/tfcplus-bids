package com.unforbidable.tfc.bids.Core.Recipes;

import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class RecipeAction {

    protected Item craftingItem;

    public RecipeAction() {
    }

    public RecipeAction matchCraftingItem(Item craftingItem) {
        this.craftingItem = craftingItem;
        return this;
    }

    public boolean craftingMatches(ItemStack itemStack) {
        return craftingMatchesItem(itemStack.getItem());
    }

    public boolean craftingMatchesItem(Item item) {
        return craftingItem != null && item == craftingItem;
    }

    public void onItemCrafted(ItemCraftedEvent event) {
    }

}
