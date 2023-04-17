package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemStonePressWeight extends ItemBlock implements ISize {

    public ItemStonePressWeight(Block block) {
        super(block);

        setMaxStackSize(1);
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 1;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public int getMetadata(int dmg) {
        return dmg;
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.HUGE;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.HEAVY;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

}
