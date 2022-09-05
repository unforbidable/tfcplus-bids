package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemExtraFood extends ItemFoodTFC {

    public ItemExtraFood(EnumFoodGroup fg, int sw, int so, int sa, int bi, int um) {
        super(fg, sw, so, sa, bi, um);

        setCreativeTab(BidsCreativeTabs.bidsFoodstuffs);
    }

    public ItemExtraFood(EnumFoodGroup fg, int sw, int so, int sa, int bi, int um, boolean edible, boolean usable) {
        super(fg, sw, so, sa, bi, um, edible, usable);

        setCreativeTab(BidsCreativeTabs.bidsFoodstuffs);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":food/"
                + this.getUnlocalizedName().replace("item.", ""));
    }

}
