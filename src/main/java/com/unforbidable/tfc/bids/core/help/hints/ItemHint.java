package com.unforbidable.tfc.bids.core.help.hints;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHint {

    public final ItemStack itemStack;
    public final String hint;

    public ItemHint(ItemStack itemStack, String hint) {
        this.itemStack = itemStack;
        this.hint = hint;
    }

    public boolean matchesItemStack(ItemStack itemStack) {
        return this.itemStack.getItem() == itemStack.getItem() &&
            (this.itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || this.itemStack.getItemDamage() == itemStack.getItemDamage());
    }

    public String getHintForItemStack(ItemStack itemStack) {
        return hint;
    }

}
