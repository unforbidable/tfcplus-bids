package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemClayLamp extends ItemBlock implements ISize {

    public ItemClayLamp(Block material) {
        super(material);

        setMaxStackSize(1);
    }

    @Override
    public int getItemStackLimit(ItemStack is)
    {
        return hasFuel(is) ? 1 : 4;
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.SMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack is) {
        return EnumWeight.LIGHT;
    }

    @Override
    public EnumItemReach getReach(ItemStack is) {
        return EnumItemReach.SHORT;
    }

    @Override
    public boolean canStack() {
        return false;
    }

    protected boolean hasFuel(ItemStack is)
    {
        return is.hasTagCompound();
    }

}
