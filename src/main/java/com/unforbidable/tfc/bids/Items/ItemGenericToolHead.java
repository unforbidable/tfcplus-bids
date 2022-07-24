package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.Tools.ItemMiscToolHead;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemGenericToolHead extends ItemMiscToolHead {

    final private ToolMaterial material;

    public ItemGenericToolHead(ToolMaterial material) {
        super();
        this.material = material;
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    public ToolMaterial getMaterial() {
        return material;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String name = getUnlocalizedName().replace("item.", "")
                .replace("IgIn ", "").replace("IgEx ", "").replace("Sed ", "").replace("MM ", "");
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":toolheads/" + name);
    }

}
