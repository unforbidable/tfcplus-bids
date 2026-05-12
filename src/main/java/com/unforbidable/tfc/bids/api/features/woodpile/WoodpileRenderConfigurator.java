package com.unforbidable.tfc.bids.api.features.woodpile;

import net.minecraft.util.IIcon;

public interface WoodpileRenderConfigurator {

    void setTexture(int side, IIcon texture);

    void setTextureScale(int side, float scale);

    void setTextureScale(int side, float scaleXY, float scaleYZ);

    void setTextureRotation(int side, int rotation);

}
