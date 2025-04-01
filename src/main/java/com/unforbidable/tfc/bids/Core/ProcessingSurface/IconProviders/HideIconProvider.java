package com.unforbidable.tfc.bids.Core.ProcessingSurface.IconProviders;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.Interfaces.IProcessingSurfaceIconProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class HideIconProvider implements IProcessingSurfaceIconProvider {

    @Override
    public IIcon registerProcessingSurfaceIcon(IIconRegister registerer, ItemStack is) {
        if (is.getItem() == TFCItems.hide) {
            return registerer.registerIcon(Tags.MOD_ID + ":surface/" + "Hide");
        } else if (is.getItem() == TFCItems.fur) {
            return registerer.registerIcon(Tags.MOD_ID + ":surface/" + "Fur");
        } else if (is.getItem() == TFCItems.furScrap) {
            return registerer.registerIcon(Tags.MOD_ID + ":surface/" + "Fur Scrap");
        } else if (is.getItem() == TFCItems.bearFur) {
            return registerer.registerIcon(Tags.MOD_ID + ":surface/" + "Bear Fur");
        } else if (is.getItem() == TFCItems.bearFurScrap) {
            return registerer.registerIcon(Tags.MOD_ID + ":surface/" + "Bear Fur Scrap");
        } else if (is.getItem() == TFCItems.wolfFur) {
            return registerer.registerIcon(Tags.MOD_ID + ":surface/" + "Wolf Fur");
        } else if (is.getItem() == TFCItems.wolfFurScrap) {
            return registerer.registerIcon(Tags.MOD_ID + ":surface/" + "Wolf Fur Scrap");
        }

        return null;
    }

}
