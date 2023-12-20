package com.unforbidable.tfc.bids.Core.Crops;

import com.dunk.tfc.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;

public class CropRendererTFC extends CropRenderer {

    public CropRendererTFC(EnumCropRenderType renderType, double width, double height, String blockIconName) {
        super(renderType, width, height, blockIconName);
    }

    @Override
    protected void registerBlockIconsForStage(IIconRegister register, int stage) {
        blockIcons[stage] = register.registerIcon(Reference.MOD_ID + ":" + "plants/crops/" + getBlockIconNameForStage(stage));
    }

}
