package com.unforbidable.tfc.bids.api.features.cookingprep;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface CookingPrepOutput {

    void onCrafted(ItemStack is, EntityPlayer player);
    int getCookingSkillIncrease(ItemStack itemStack);

}
