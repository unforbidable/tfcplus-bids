package com.unforbidable.tfc.bids.Items.ItemBlocks;

import java.util.List;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.Blocks.BlockRoughStone;
import com.unforbidable.tfc.bids.TFC.ItemTerra;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemRoughStone extends ItemBlock implements ISize {

    public ItemRoughStone(Block block) {
        super(block);
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 32;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        Block block = Block.getBlockFromItem(this);
        if (block != null && block instanceof BlockRoughStone) {
            String[] names = ((BlockRoughStone) block).getNames();
            if (names != null && is.getItemDamage() < names.length) {
                return getUnlocalizedName().concat("." + names[is.getItemDamage()]);
            }
        }

        return super.getUnlocalizedName(is);
    }

    @Override
    public int getMetadata(int dmg) {
        return dmg;
    }

    @Override
    public boolean canStack() {
        return true;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.MEDIUM;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.HEAVY;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemTerra.addSizeInformation(is, list);
    }

}
