package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.Items.Pottery.ItemPotteryBase;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemGenericPottery extends ItemPotteryBase {

    final boolean hasCeramicItem;

    public ItemGenericPottery(boolean hasCeramicItem) {
        super();
        setCreativeTab(BidsCreativeTabs.bidsDefault);
        this.hasCeramicItem = hasCeramicItem;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        clayIcon = registerer.registerIcon(Tags.MOD_ID + ":pottery/"
                + getUnlocalizedName().replace("item.", "") + ".Clay");
        if (hasCeramicItem)
            ceramicIcon = registerer.registerIcon(Tags.MOD_ID + ":pottery/"
                    + getUnlocalizedName().replace("item.", "") + ".Ceramic");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list) {
        list.add(new ItemStack(this, 1, 0));
        if (hasCeramicItem)
            list.add(new ItemStack(this, 1, 1));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (itemstack.getItemDamage() > 0)
            return getUnlocalizedName() + ".Ceramic";
        return getUnlocalizedName() + ".Clay";
    }

}
