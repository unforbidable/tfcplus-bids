package com.unforbidable.tfc.bids.api.features.cooking;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface CookedMeal {

    void onCookedMealCreated(ItemStack is, EntityPlayer player);

}
