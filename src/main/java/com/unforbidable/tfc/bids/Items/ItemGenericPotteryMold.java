package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.Pottery.ItemPotteryMold;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class ItemGenericPotteryMold extends ItemPotteryMold {

    public ItemGenericPotteryMold() {
        super();
        setMaxDamage(101);
        setMaxUnits(100);
        setBaseDamage(2);
        setCreativeTab(BidsCreativeTabs.bidsTools);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        clayIcon = registerer.registerIcon(Tags.MOD_ID + ":pottery/"
                + getUnlocalizedName().replace("item.", "") + ".Clay");
        ceramicIcon = registerer.registerIcon(Tags.MOD_ID + ":pottery/"
                + getUnlocalizedName().replace("item.", "") + ".Ceramic");
        if (metaNames.length > 2) {
            this.metalIcons = new IIcon[metaNames.length - 2];
            for (int i = 0; i < metalIcons.length; i++) {
                metalIcons[i] = registerer.registerIcon(Tags.MOD_ID + ":pottery/"
                        + getUnlocalizedName().replace("item.", "") + "." + metaNames[2 + i]);
            }
        }
    }

}
