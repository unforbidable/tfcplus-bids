package com.unforbidable.tfc.bids.Core.ProcessingSurface.IconProviders;

import com.dunk.tfc.Blocks.Devices.BlockLeatherRack;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.Interfaces.IProcessingSurfaceIconProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class LeatherRackIconProviderTFC implements IProcessingSurfaceIconProvider {

    @Override
    public IIcon getProcessingSurfaceIcon(ItemStack is) {
        if (is.getItem() == TFCItems.soakedHide) {
            return TFCBlocks.leatherRack.getIcon(0, 0);
        } else if (is.getItem() == TFCItems.scrapedHide) {
            return ((BlockLeatherRack)TFCBlocks.leatherRack).scrapedTex;
        }
        return null;
    }

}
