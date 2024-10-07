package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.api.Food;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemWrap extends ItemMoreSandwich {

    public ItemWrap(float[] ingredientWeights) {
        super(ingredientWeights);
    }

    @Override
    public void onCrafted(ItemStack is, EntityPlayer player) {
        // Normally sandwiches decay at rate of 2.0
        // Wraps have a slightly lower decay rate
        Food.setDecayRate(is, 1.5f);
    }

    @Override
    protected float getFillingBoost() {
        // Less filling than a TFC sandwich
        // just to make sandwiches from bread using more advantageous in this regard
        return 1.15f;
    }

    @Override
    public boolean renderWeight() {
        return false;
    }

}
