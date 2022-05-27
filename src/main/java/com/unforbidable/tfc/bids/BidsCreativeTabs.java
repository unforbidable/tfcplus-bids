package com.unforbidable.tfc.bids;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BidsCreativeTabs extends CreativeTabs {

    public static CreativeTabs BidsDefault = new BidsCreativeTabs();

    private ItemStack is = new ItemStack(Items.bowl);

    public BidsCreativeTabs() {
        super("Bids and Pieces");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return is.getItem();
    }

    @Override
    public ItemStack getIconItemStack() {
        return is;
    }

}
