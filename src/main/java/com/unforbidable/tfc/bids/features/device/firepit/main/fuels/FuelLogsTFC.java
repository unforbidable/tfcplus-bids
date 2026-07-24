package com.unforbidable.tfc.bids.features.device.firepit.main.fuels;

import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import net.minecraft.item.ItemStack;

public class FuelLogsTFC implements FirepitFuelMaterial {

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
