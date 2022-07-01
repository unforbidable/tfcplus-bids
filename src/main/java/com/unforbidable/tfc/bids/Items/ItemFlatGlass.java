package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemFlatGeneric;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemFlatGlass extends ItemFlatGeneric {

    public ItemFlatGlass() {
        super();
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":"
                + this.getUnlocalizedName().replace("item.", ""));
    }

}
