package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemFlatGeneric;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemGenericFlat extends ItemFlatGeneric {

    protected String textureFolder = "";

    public ItemGenericFlat() {
        super();
    }

    public ItemGenericFlat setTextureFolder(String textureFolder) {
        this.textureFolder = textureFolder;

        return this;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/"
                + this.getUnlocalizedName().replace("item.", ""));
    }

}
