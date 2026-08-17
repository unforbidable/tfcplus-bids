package com.unforbidable.tfc.bids.util.accessor;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface ItemSpecialCraftingAccessor {

    Item getSpecialCraftingItem();
    void setSpecialCraftingItem(Item item);

    ItemStack getSpecialCraftingItemStack(ItemStack itemStack);

}
