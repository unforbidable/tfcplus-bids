package com.unforbidable.tfc.bids.features.utility.hidevessel.main.nbt;

import com.unforbidable.tfc.bids.util.nbt.ItemTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class HideVesselTag extends ItemTag {

    public HideVesselTag(ItemStack itemStack) {
        super(itemStack);
    }

    public static HideVesselTag of(ItemStack itemStack) {
        return new HideVesselTag(itemStack);
    }

    public long getLastDamageTicks() {
        return stack().hasTagCompound() ? stack().getTagCompound().getLong("lastDamageTicks") : 0;
    }

    public HideVesselTag setLastDamageTicks(long lastDamageTicks) {
        if (!stack().hasTagCompound()) {
            stack().setTagCompound(new NBTTagCompound());
        }
        stack().getTagCompound().setLong("lastDamageTicks", lastDamageTicks);

        return this;
    }

    public void clearLastDamageTicks() {
        if (stack().hasTagCompound()) {
            stack().getTagCompound().removeTag("lastDamageTicks");

            if (stack().getTagCompound().hasNoTags()) {
                stack().setTagCompound(null);
            }
        }
    }

}
