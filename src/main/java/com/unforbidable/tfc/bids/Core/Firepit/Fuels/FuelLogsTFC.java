package com.unforbidable.tfc.bids.Core.Firepit.Fuels;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import net.minecraft.item.ItemStack;

public class FuelLogsTFC implements IFirepitFuelMaterial {

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        return true;
    }

    @Override
    public float getFuelKindlingQuality(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getFuelBurnTime(ItemStack itemStack) {
        return getEnumFuelMaterial(itemStack).burnTimeMax;
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        return getEnumFuelMaterial(itemStack).burnTempMax;
    }

    @Override
    public EnumFuelMaterial getFuelTasteProfile(ItemStack itemStack) {
        return getEnumFuelMaterial(itemStack);
    }

    private static EnumFuelMaterial getEnumFuelMaterial(ItemStack itemStack) {
        return TFC_Core.getFuelMaterial(itemStack);
    }

}
