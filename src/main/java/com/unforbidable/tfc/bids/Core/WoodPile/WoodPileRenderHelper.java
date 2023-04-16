package com.unforbidable.tfc.bids.Core.WoodPile;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderer;
import net.minecraft.util.IIcon;

public class WoodPileRenderHelper implements IWoodPileRenderer {

    private IIcon[] textures = new IIcon[6];
    private int[] rotations = new int[6];
    private float[] scalesXY = new float[6];
    private float[] scalesYZ = new float[6];

    public WoodPileRenderHelper(boolean rotated) {
        // Default scale
        // Short side is 0.5f
        // Scale long side, long edge to 1
        if (rotated) {
            setTextureScale(0, 1f, 0.5f);
            setTextureScale(1, 1f, 0.5f);
            setTextureScale(2, 1f, 0.5f);
            setTextureScale(3, 1f, 0.5f);
            setTextureScale(4, 0.5f, 0.5f);
            setTextureScale(5, 0.5f, 0.5f);
        } else {
            setTextureScale(0, 0.5f, 1f);
            setTextureScale(1, 0.5f, 1f);
            setTextureScale(2, 0.5f, 0.5f);
            setTextureScale(3, 0.5f, 0.5f);
            setTextureScale(4, 0.5f, 1f);
            setTextureScale(5, 0.5f, 1f);
        }
    }

    public boolean apply(RenderBlocksWithRotation render) {
        if (!isValid()) {
            return false;
        }

        render.setOverrideBlockTexture_y(textures[0]);
        render.setOverrideBlockTexture_Y(textures[1]);
        render.setOverrideBlockTexture_z(textures[2]);
        render.setOverrideBlockTexture_Z(textures[3]);
        render.setOverrideBlockTexture_x(textures[4]);
        render.setOverrideBlockTexture_X(textures[5]);

        render.textureStartXB = 0;
        render.textureEndXB = scalesXY[0];
        render.textureStartZB = 0f;
        render.textureEndZB = scalesYZ[0];

        render.textureStartXT = 0;
        render.textureEndXT = scalesXY[1];
        render.textureStartZT = 0f;
        render.textureEndZT = scalesYZ[1];

        render.textureStartXN = 0;
        render.textureEndXN = scalesXY[2];
        render.textureStartYN = 0;
        render.textureEndYN = scalesYZ[2];

        render.textureStartXS = 0;
        render.textureEndXS = scalesXY[3];
        render.textureStartYS = 0;
        render.textureEndYS = scalesYZ[3];

        render.textureStartYW = 0;
        render.textureEndYW = scalesXY[4];
        render.textureStartZW = 0f;
        render.textureEndZW = scalesYZ[4];

        render.textureStartYE = 0;
        render.textureEndYE = scalesXY[5];
        render.textureStartZE = 0f;
        render.textureEndZE = scalesYZ[5];

        render.uvRotateBottom = rotations[0];
        render.uvRotateTop = rotations[1];
        render.uvRotateNorth = rotations[2];
        render.uvRotateSouth = rotations[3];
        render.uvRotateWest = rotations[4];
        render.uvRotateEast = rotations[5];

        return true;
    }

    @Override
    public void setTexture(int side, IIcon texture) {
        if (isLegalSide(side)) {
            textures[side] = texture;
        }
    }

    @Override
    public void setTextureScale(int side, float scale) {
        setTextureScale(side, scale, scale);
    }

    @Override
    public void setTextureScale(int side, float scaleXY, float scaleYZ) {
        if (isLegalSide(side)) {
            scalesXY[side] = scaleXY;
            scalesYZ[side] = scaleYZ;
        }
    }

    @Override
    public void setTextureRotation(int side, int rotation) {
        if (isLegalSide(side)) {
            rotations[side] = rotation;
        }
    }

    private boolean isLegalSide(int side) {
        if (side < 0 && side > 5) {
            Bids.LOG.warn("Illegal side specified when setting up IWoodPileRenderer");
            return false;
        }

        return true;
    }

    private boolean isValid() {
        for (int i = 0; i < 6; i++) {
            if (textures[i] == null) {
                Bids.LOG.warn("Missing texture for side " + i + " when setting up IWoodPileRenderer");
                return false;
            }
        }

        return true;
    }
}
