package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.Tools.ItemMiscToolHead;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemGenericToolHead extends ItemMiscToolHead {

    public ItemGenericToolHead(ToolMaterial material) {
        super(material);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    public ItemGenericToolHead() {
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String name = getUnlocalizedName().replace("item.", "")
                .replace("IgIn ", "").replace("IgEx ", "").replace("Sed ", "").replace("MM ", "");
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":toolheads/" + name);
    }

}
