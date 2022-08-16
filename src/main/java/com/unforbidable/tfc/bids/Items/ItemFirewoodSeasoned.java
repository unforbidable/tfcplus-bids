package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.unforbidable.tfc.bids.Core.Firepit.FirepitHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import net.minecraft.item.ItemStack;

public class ItemFirewoodSeasoned extends ItemFirewood implements IFirepitFuelMaterial {

    public ItemFirewoodSeasoned() {
        super();
    }

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
        return (int) (FirepitHelper.getEnumFuelMaterial(itemStack).burnTimeMax);
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        return (int) (FirepitHelper.getEnumFuelMaterial(itemStack).burnTempMax);
    }

    @Override
    public EnumFuelMaterial getFuelTasteProfile(ItemStack itemStack) {
        return FirepitHelper.getEnumFuelMaterial(itemStack);
    }

}
