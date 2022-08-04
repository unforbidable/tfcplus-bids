package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.Items.ItemLogs;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningManager;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemLogsSeasoned extends ItemLogs {

    public ItemLogsSeasoned() {
        super();

        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return this.getUnlocalizedName() + "." + metaNames[itemstack.getItemDamage()];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list) {
        for (int i = 0; i < Global.WOOD_ALL.length * 2; i++) {
            ItemStack is = new ItemStack(this, 1, i);
            if (SeasoningManager.hasMatchingRecipe(is)) {
                list.add(is);
            }
        }
    }

}
