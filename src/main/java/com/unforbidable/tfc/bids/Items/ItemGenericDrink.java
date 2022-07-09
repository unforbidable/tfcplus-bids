package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemDrink;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemGenericDrink extends ItemDrink {

    public ItemGenericDrink(float volume, boolean isPottery) {
        super(volume);
        pottery = isPottery;
        setCreativeTab(BidsCreativeTabs.BidsDrinks);
        setFolder(isPottery ? "pottery" : "glassware");
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/"
                + getContainerItem().getUnlocalizedName().replace("item.", "") + ".Overlay");
    }

}
