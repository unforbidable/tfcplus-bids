package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemLogs;
import com.dunk.tfc.api.Enums.EnumSize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Wood.WoodIndex;
import com.unforbidable.tfc.bids.Core.Wood.WoodScheme;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

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
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.items.hasSeasonedLog()) {
                list.add(wood.items.getSeasonedLog());
            }
            if (wood.items.hasSeasonedChoppedLog()) {
                list.add(wood.items.getSeasonedChoppedLog());
            }
        }
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.VERYLARGE;
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 16;
    }

}
