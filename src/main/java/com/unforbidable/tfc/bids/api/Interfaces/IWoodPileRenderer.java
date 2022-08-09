package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.util.IIcon;

public interface IWoodPileRenderer {

    void setTexture(int side, IIcon texture);

    void setTextureScale(int side, float scale);

    void setTextureScale(int side, float scaleXY, float scaleYZ);

    void setTextureRotation(int side, int rotation);

}
