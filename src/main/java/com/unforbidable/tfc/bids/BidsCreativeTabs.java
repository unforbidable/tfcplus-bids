package com.unforbidable.tfc.bids;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BidsCreativeTabs extends CreativeTabs {

    public static final CreativeTabs bidsDefault = new BidsCreativeTabs("Default", Items.bowl);
    public static final CreativeTabs bidsFoodstuffs = new BidsCreativeTabs("Foodstuffs", Items.bread);
    public static final CreativeTabs bidsBuildingBlocks = new BidsCreativeTabs("BuildingBlocks", Items.brick);
    public static final CreativeTabs bidsMaterials = new BidsCreativeTabs("Materials", Items.string);
    public static final CreativeTabs bidsTools = new BidsCreativeTabs("Tools", Items.stone_axe);

    private final ItemStack is;

    public BidsCreativeTabs(String name, Item item) {
        this(name, item, 0);
    }

    public BidsCreativeTabs(String name, Item item, int metadata) {
        super("Bids." + name);
        is = new ItemStack(item, 1, metadata);
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
