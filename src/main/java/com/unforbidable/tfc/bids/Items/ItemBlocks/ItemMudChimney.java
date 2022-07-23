package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.Blocks.BlockMudChimney;
import com.unforbidable.tfc.bids.Core.ItemHelper;

import java.util.List;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemMudChimney extends ItemBlock implements ISize {

    public ItemMudChimney(Block block) {
        super(block);
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 32;
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.VERYSMALL;
    }

    @Override
    public boolean canStack() {
        return true;
    }

    @Override
    public EnumWeight getWeight(ItemStack is) {
        return EnumWeight.HEAVY;
    }

    @Override
    public EnumItemReach getReach(ItemStack is) {
        return EnumItemReach.SHORT;
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        Block block = Block.getBlockFromItem(is.getItem());
        if (block instanceof BlockMudChimney) {
            int dmg = is.getItemDamage() + ((BlockMudChimney) block).getStoneOffset();
            if (dmg < Global.STONE_ALL.length)
                return getUnlocalizedName().concat("." + Global.STONE_ALL[dmg]);
        }

        return super.getUnlocalizedName(is);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
        ItemHelper.addSizeInformation(is, arraylist);

        Block b = Block.getBlockFromItem(is.getItem());
        if (b instanceof BlockMudChimney) {
            int dam = is.getItemDamage();
            if (b == TFCBlocks.mudBricks2) {
                dam += 16;
            }

            if (dam < Global.STONE_ALL.length)
                arraylist.add(EnumChatFormatting.DARK_GRAY + Global.STONE_ALL[dam]);
            else
                arraylist.add(EnumChatFormatting.DARK_RED + "Unknown");
        }
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

}
