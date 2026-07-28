package com.unforbidable.tfc.bids.features.device.firepit.main.fuels;

import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import net.minecraft.item.ItemStack;

public class FuelCoalTFC implements FirepitFuelMaterial {

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        // Only charcoal is allowed
        return itemStack.getItemDamage() == 1;
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
    public int getFuelTasteProfile(ItemStack itemStack) {
        return EnumFuelMaterial.CHARCOAL.ordinal();
    }

}
