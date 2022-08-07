package com.unforbidable.tfc.bids.Core.Firepit.Fuels;

import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import net.minecraft.item.ItemStack;

public class FuelStickBundleTFC implements IFirepitFuelMaterial {

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        return true;
    }

    @Override
    public float getFuelKindlingQuality(ItemStack itemStack) {
        return 0.5f;
    }

    @Override
    public int getFuelBurnTime(ItemStack itemStack) {
        return EnumFuelMaterial.STICKBUNDLE.burnTempMax;
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        return EnumFuelMaterial.STICKBUNDLE.burnTempMax;
    }

    @Override
    public EnumFuelMaterial getFuelTasteProfile(ItemStack itemStack) {
        return EnumFuelMaterial.STICKBUNDLE;
    }

}
