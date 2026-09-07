package com.unforbidable.tfc.bids.features.utility.hidevessel.main.nbt;

import com.unforbidable.tfc.bids.util.nbt.ItemTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class HideVesselTag extends ItemTag {

    public HideVesselTag(ItemStack itemStack) {
        super(itemStack);
    }

    public static HideVesselTag of(ItemStack itemStack) {
        return new HideVesselTag(itemStack);
    }

    public boolean hasFluid() {
        return stack().hasTagCompound() && stack().getTagCompound().hasKey("fluid");
    }

    public FluidStack getFluid() {
        return hasFluid() ? FluidStack.loadFluidStackFromNBT(stack().getTagCompound().getCompoundTag("fluid")) : null;
    }

    public HideVesselTag setFluid(FluidStack fluidStack) {
        if (!stack().hasTagCompound()) {
            stack().setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound fluidTag = new NBTTagCompound();
        fluidStack.writeToNBT(fluidTag);
        stack().getTagCompound().setTag("fluid", fluidTag);

        return this;
    }

    public HideVesselTag clearFluid() {
        if (hasFluid()) {
            stack().getTagCompound().removeTag("fluid");
        }

        return this;
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

    public int getDamage() {
        return stack().hasTagCompound() ? stack().getTagCompound().getInteger("damage") : 0;
    }

    public HideVesselTag setDamage(int damage) {
        if (!stack().hasTagCompound()) {
            stack().setTagCompound(new NBTTagCompound());
        }
        stack().getTagCompound().setInteger("damage", damage);

        return this;
    }

}
