package com.unforbidable.tfc.bids.common.item;

import com.dunk.tfc.Items.Tools.ItemMiscToolHead;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemCommonToolHead extends ItemMiscToolHead {

    public ItemCommonToolHead(ToolMaterial material) {
        super(material);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    public ItemCommonToolHead() {
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String name = getUnlocalizedName().replace("item.", "")
                .replace("IgIn ", "").replace("IgEx ", "").replace("Sed ", "").replace("MM ", "");
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":toolheads/" + name);
    }

}
