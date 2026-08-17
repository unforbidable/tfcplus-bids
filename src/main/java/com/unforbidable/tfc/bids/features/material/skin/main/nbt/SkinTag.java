package com.unforbidable.tfc.bids.features.material.skin.main.nbt;

import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.compat.tfc.nbt.FoodTag;
import net.minecraft.item.ItemStack;

public class SkinTag extends FoodTag {

    private final SkinTagAccess access;

    public SkinTag(ItemStack itemStack) {
        super(itemStack);
        access = new SkinTagAccess(itemStack.getTagCompound());
    }

    public static SkinTag of(ItemStack itemStack) {
        return of(itemStack, SkinTag::new);
    }

    public String getStage() {
        return access.getStage();
    }

    public boolean isStage(String value) {
        return value.equals(getStage());
    }

    public SkinTag setStage(String value) {
        access.setStage(value);

        return this;
    }

    public String getAnimal() {
        return access.getAnimal();
    }

    public SkinTag setAnimal(String value) {
        access.setAnimal(value);

        return this;
    }

    public boolean hasAnimal() {
        return access.getAnimal().length() > 0;
    }

    public SkinTag setFluid(String value) {
        access.setFluid(value);

        return this;
    }

    public String getFluid() {
        return access.getFluid();
    }

    public boolean hasFluid() {
        return access.getFluid().length() > 0;
    }

}
