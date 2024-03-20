package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IMoreSandwich {

    void onCrafted(ItemStack is, EntityPlayer player);
    int getCookingSkillIncrease(ItemStack itemStack);

}
