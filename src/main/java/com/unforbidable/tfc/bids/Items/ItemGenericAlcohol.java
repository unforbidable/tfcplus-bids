package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemAlcohol;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemGenericAlcohol extends ItemAlcohol {

    public ItemGenericAlcohol(float volume) {
        super(volume);
        setCreativeTab(BidsCreativeTabs.BidsDrinks);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":glassware/"
                + getContainerItem().getUnlocalizedName().replace("item.", "") + " Overlay");
    }

}
