package com.unforbidable.tfc.bids.Core.ProcessingSurface.IconProviders;

import com.dunk.tfc.Reference;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.Interfaces.IProcessingSurfaceIconProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class LeatherRackIconProviderTFC implements IProcessingSurfaceIconProvider {

    @Override
    public IIcon registerProcessingSurfaceIcon(IIconRegister registerer, ItemStack is) {
        if (is.getItem() == TFCItems.soakedHide) {
            return registerer.registerIcon(Reference.MOD_ID + ":" + "Soaked Hide");
        } else if (is.getItem() == TFCItems.scrapedHide) {
            return registerer.registerIcon(Reference.MOD_ID + ":" + "Scraped Hide");
        }
        return null;
    }

}
