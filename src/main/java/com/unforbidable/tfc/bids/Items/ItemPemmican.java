package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPemmican extends ItemMoreSandwich {

    public ItemPemmican(float[] ingredientWeights) {
        super(ingredientWeights);
    }

    @Override
    public void onCrafted(ItemStack is, EntityPlayer player) {
        // Pemmican does not spoil for 6 months at all after crafting
        // and after 6 months it decays at a very low rate
        Food.setDecayTimer(is, (int)TFC_Time.getTotalHours() + TFC_Time.getHoursInMonth() * 6);
        Food.setDecayRate(is, 0.02f);
    }

    @Override
    public ItemStack onEaten(ItemStack is, World world, EntityPlayer player) {
        ItemStack isEaten = super.onEaten(is, world, player);

        // Once eaten pemmican begins to spoil at a slightly higher rate
        // even if 6 months have not passed
        Food.setDecayTimer(is, (int)TFC_Time.getTotalHours());
        Food.setDecayRate(is, 0.1f);

        if (isEaten.stackSize == 0) {
            return new ItemStack(TFCItems.hide, 1, 0);
        }

        return isEaten;
    }

    @Override
    protected float getAmountEatenPerCycle() {
        return 10;
    }

    @Override
    protected float getFillingBoost() {
        return 1.2f;
    }

}
