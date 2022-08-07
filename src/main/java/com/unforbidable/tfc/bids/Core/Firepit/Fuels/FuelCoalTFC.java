package com.unforbidable.tfc.bids.Core.Firepit.Fuels;

import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import net.minecraft.item.ItemStack;

public class FuelCoalTFC implements IFirepitFuelMaterial {

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        // Only charcoal is allowed
        return itemStack.getItemDamage() == 1;
    }

    @Override
    public float getFuelKindlingQuality(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getFuelBurnTime(ItemStack itemStack) {
        // Charcoal burns longer in a firepit
        return (int) (EnumFuelMaterial.CHARCOAL.burnTimeMax * 1.5f);
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        // Charcoal doesn't burn as hot in a firepit
        return (int) (EnumFuelMaterial.CHARCOAL.burnTempMax * 0.75f);
    }

    @Override
    public EnumFuelMaterial getFuelTasteProfile(ItemStack itemStack) {
        return EnumFuelMaterial.CHARCOAL;
    }

}
