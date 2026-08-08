package com.unforbidable.tfc.bids.util.nbt;

import com.unforbidable.tfc.bids.Bids;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemTag {

    private final ItemStack itemStack;

    public ItemTag(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static <T extends ItemTag> T of(ItemStack itemStack, Function<ItemStack, T> constructor) {
        if (!itemStack.hasTagCompound()) {
            Bids.LOG.error("Item missing NBT data: {}", itemStack);

            itemStack.setTagCompound(new NBTTagCompound());
        }

        return constructor.apply(itemStack);
    }

    public static <T extends ItemTag> ItemStack create(ItemStack itemStack, Function<ItemStack, T> constructor, Consumer<T> apply) {
        itemStack.setTagCompound(new NBTTagCompound());

        T tag = constructor.apply(itemStack);
        tag.init();

        apply.accept(tag);

        return itemStack;
    }

    protected void init() {
    }

    public ItemStack stack() {
        return itemStack;
    }

}
