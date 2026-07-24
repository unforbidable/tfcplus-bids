package com.unforbidable.tfc.bids.features.device.firepit.main.fuels;

import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import net.minecraft.item.ItemStack;

public class FuelStickBundleTFC implements FirepitFuelMaterial {

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        return true;
    }

    @Override
    public float getFuelKindlingQuality(ItemStack itemStack) {
        return 0.25f;
    }

    @Override
    public int getFuelBurnTime(ItemStack itemStack) {
        return EnumFuelMaterial.STICKBUNDLE.burnTimeMax;
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        return EnumFuelMaterial.STICKBUNDLE.burnTempMax;
    }

    @Override
    public int getFuelTasteProfile(ItemStack itemStack) {
        return EnumFuelMaterial.STICKBUNDLE.ordinal();
    }

}
