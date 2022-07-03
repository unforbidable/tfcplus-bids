package com.unforbidable.tfc.bids;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BidsCreativeTabs extends CreativeTabs {

    public static CreativeTabs BidsDefault = new BidsCreativeTabs("Default", Items.bowl);
    public static CreativeTabs BidsDrinks = new BidsCreativeTabs("Drinks", Items.glass_bottle);

    private final ItemStack is;

    public BidsCreativeTabs(String name, Item item) {
        super("Bids." + name);
        is = new ItemStack(item);
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
