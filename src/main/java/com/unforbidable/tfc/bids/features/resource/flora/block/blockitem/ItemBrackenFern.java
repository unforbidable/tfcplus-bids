package com.unforbidable.tfc.bids.features.resource.flora.block.blockitem;

import com.dunk.tfc.Core.ColorizerFoliageTFC;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBrackenFern extends ItemBlock {

    public ItemBrackenFern(Block block) {
        super(block);
    }

    @Override
    public int getItemStackLimit(ItemStack itemStack) {
        return 1;
    }

    @Override
    public boolean getHasSubtypes() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack is, int damage)
    {
        return ColorizerFoliageTFC.getFoliageColorBasic();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        return BidsBlocks.brackenFern.getIcon(0, 0);
    }

}
