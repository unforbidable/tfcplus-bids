package com.unforbidable.tfc.bids.common.item;

import com.dunk.tfc.Items.Tools.ItemCustomShovel;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemCommonShovel extends ItemCustomShovel {

    public ItemCommonShovel(ToolMaterial material) {
        super(material);

        setCreativeTab(BidsCreativeTabs.bidsTools);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String name = getUnlocalizedName().replace("item.", "");
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":tools/" + name);
    }

}
