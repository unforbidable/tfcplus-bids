package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.Blocks.BlockAquifer;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemGenericSoil extends ItemBlock implements ISize {

    public ItemGenericSoil(Block block) {
        super(block);
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
        ItemHelper.addSizeInformation(is, arraylist);

        arraylist.add(EnumChatFormatting.AQUA + "Aquifer");

        int dam = is.getItemDamage();

        Block block = Block.getBlockFromItem(is.getItem());
        if (block instanceof BlockAquifer) {
            dam += ((BlockAquifer) block).getTextureOffset();
        }

        if (dam < Global.STONE_ALL.length) {
            arraylist.add(EnumChatFormatting.DARK_GRAY + Global.STONE_ALL[dam]);
        } else {
            arraylist.add(EnumChatFormatting.DARK_RED + "Unknown");
        }
    }

    @Override
    public EnumSize getSize(ItemStack itemStack) {
        return EnumSize.VERYSMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack itemStack) {
        return EnumWeight.MEDIUM;
    }

    @Override
    public EnumItemReach getReach(ItemStack itemStack) {
        return EnumItemReach.SHORT;
    }

    @Override
    public boolean canStack() {
        return true;
    }

}
