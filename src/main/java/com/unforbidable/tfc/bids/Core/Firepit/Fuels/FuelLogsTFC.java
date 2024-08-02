package com.unforbidable.tfc.bids.Core.Firepit.Fuels;

import com.unforbidable.tfc.bids.Core.Wood.WoodScheme;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;
import net.minecraft.item.ItemStack;

public class FuelLogsTFC implements IFirepitFuelMaterial {

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        return !WoodScheme.DEFAULT.findWood(itemStack).inflammable;
    }

    @Override
    public float getFuelKindlingQuality(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getFuelBurnTime(ItemStack itemStack) {
        return WoodScheme.DEFAULT.findWood(itemStack).maxBurnTime;
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        return WoodScheme.DEFAULT.findWood(itemStack).maxBurnTemp;
    }

    @Override
    public int getFuelTasteProfile(ItemStack itemStack) {
        return WoodScheme.DEFAULT.findWood(itemStack).tasteProfile;
    }

}
