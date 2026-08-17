package com.unforbidable.tfc.bids.api.util.nbt;

import net.minecraft.nbt.NBTTagCompound;

public class SkinTagAccess {

    public static final String STAGE_FLESHED = "fleshed";
    public static final String STAGE_CLEAN = "clean";
    public static final String STAGE_PRESERVED = "preserved";
    public static final String STAGE_PREPARED = "prepared";
    public static final String STAGE_DEHAIRED = "dehaired";
    public static final String STAGE_TANNED = "tanned";

    public static final String TAG_SKIN_PROCESSING = "Skin Processing";
    public static final String TAG_STAGE = "stage";
    public static final String TAG_FLUID = "fluid";
    public static final String TAG_ANIMAL = "animal";

    private final NBTTagCompound tag;

    public SkinTagAccess(NBTTagCompound tag) {
        this.tag = tag;
    }

    protected NBTTagCompound skinProcessing() {
        if (!tag.hasKey(TAG_SKIN_PROCESSING)) {
            NBTTagCompound processingTag = new NBTTagCompound();
            tag.setTag(TAG_SKIN_PROCESSING, processingTag);
            return processingTag;
        }

        return (NBTTagCompound) tag.getTag(TAG_SKIN_PROCESSING);
    }

    public String getStage() {
        return skinProcessing().getString(TAG_STAGE);
    }

    public void setStage(String value) {
        skinProcessing().setString(TAG_STAGE, value);
    }

    public String getFluid() {
        return skinProcessing().getString(TAG_FLUID);
    }

    public void setFluid(String value) {
        if (value == null) {
            skinProcessing().removeTag(TAG_FLUID);
        } else {
            skinProcessing().setString(TAG_FLUID, value);
        }
    }

    public String getAnimal() {
        return tag.getString(TAG_ANIMAL);
    }

    public void setAnimal(String value) {
        if (value == null) {
            tag.removeTag(TAG_ANIMAL);
        } else {
            tag.setString(TAG_ANIMAL, value);
        }
    }

}
