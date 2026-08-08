package com.unforbidable.tfc.bids.compat.tfc.nbt;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.util.nbt.ItemTag;
import net.minecraft.item.ItemStack;
import java.util.function.Consumer;

public class FoodTag extends ItemTag {

    public FoodTag(ItemStack itemStack) {
        super(itemStack);
    }

    public static FoodTag of(ItemStack itemStack) {
        return of(itemStack, FoodTag::new);
    }

    public static ItemStack createFoodTag(ItemStack itemStack, Consumer<FoodTag> apply) {
        return ItemTag.create(itemStack, FoodTag::new, apply);
    }

    @Override
    protected void init() {
        super.init();

        ItemFoodTFC.createTag(stack(), 160f);
    }

    public float getWeight() {
        return Food.getWeight(stack());
    }

    public FoodTag setWeight(float value) {
        Food.setWeight(stack(), value);
        return this;
    }

    public float getDecay() {
        return Food.getDecay(stack());
    }

    public FoodTag setDecay(float value) {
        Food.setDecay(stack(), value);
        return this;
    }

    public boolean isSalted() {
        return Food.isSalted(stack());
    }

    public FoodTag setSalted() {
        Food.setSalted(stack(), true);
        return this;
    }

    public FoodTag setSalted(boolean value) {
        Food.setSalted(stack(), value);
        return this;
    }

    public boolean isSmoked() {
        return Food.isSmoked(stack());
    }

    public FoodTag setFuelProfile(int[] value) {
        Food.setFuelProfile(stack(), value);

        return this;
    }

}
