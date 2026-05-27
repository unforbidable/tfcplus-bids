package com.unforbidable.tfc.bids.features.device.wallhook.block.blockitem;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.util.ItemHelper;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemWallHook extends ItemBlock implements ISize {

    public ItemWallHook(Block block) {
        super(block);
    }

    @Override
    public EnumSize getSize(ItemStack itemStack) {
        return EnumSize.VERYSMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack itemStack) {
        return EnumWeight.LIGHT;
    }

    @Override
    public EnumItemReach getReach(ItemStack itemStack) {
        return EnumItemReach.SHORT;
    }

    @Override
    public boolean canStack() {
        return true;
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 16;
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

}
