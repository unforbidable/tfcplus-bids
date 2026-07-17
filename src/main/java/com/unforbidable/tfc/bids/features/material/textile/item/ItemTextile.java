package com.unforbidable.tfc.bids.features.material.textile.item;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.features.crafting.handwork.item.ItemHandworkMaterial;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemTextile extends ItemHandworkMaterial {

    public ItemTextile() {
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setFolder("textile");
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/" +
            getUnlocalizedName().replace("item.", ""));
    }

}
