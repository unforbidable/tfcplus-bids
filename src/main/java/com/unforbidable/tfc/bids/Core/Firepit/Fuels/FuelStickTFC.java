package com.unforbidable.tfc.bids.Core.Firepit.Fuels;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import net.minecraft.item.ItemStack;

public class FuelStickTFC implements IFirepitFuelMaterial {

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        if (itemStack.getItem() == TFCItems.stick) {
            return true;
        }

        if (itemStack.getItem() == TFCItems.fireStarter) {
            return true;
        }

        return false;
    }

    @Override
    public float getFuelKindlingQuality(ItemStack itemStack) {
        return 0.25f;
    }

    @Override
    public int getFuelBurnTime(ItemStack itemStack) {
        return EnumFuelMaterial.STICK.burnTimeMax;
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        return EnumFuelMaterial.STICK.burnTempMax;
    }

    @Override
    public int getFuelTasteProfile(ItemStack itemStack) {
        return EnumFuelMaterial.STICK.ordinal();
    }

}
