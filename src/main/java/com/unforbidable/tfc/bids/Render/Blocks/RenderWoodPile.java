package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Render.RenderBlocksWithRotation;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileItemBounds;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderWoodPile implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        TileEntityWoodPile te = (TileEntityWoodPile) world.getTileEntity(x, y, z);

        RenderBlocksWithRotation rendererAlt = new RenderBlocksWithRotation(renderer);
        rendererAlt.renderAllFaces = true;
        rendererAlt.staticTexture = true;

        for (WoodPileItemBounds itemBounds : te.getItemBounds()) {
            final ItemStack item = itemBounds.getItemStack();
            final IWoodPileRenderProvider provider = itemBounds.getRenderProvider();
            final boolean isRowRotated = itemBounds.isRowRotated();
            final AxisAlignedBB bounds = itemBounds.getBounds();

            final RendererHelper helper = new RendererHelper(isRowRotated);
            provider.onWoodPileRender(item, isRowRotated, helper);
            helper.apply(rendererAlt);

            rendererAlt.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ,
                    bounds.maxX, bounds.maxY, bounds.maxZ);
            rendererAlt.renderStandardBlock(block, x, y, z);
        }

        rendererAlt.renderAllFaces = false;

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

    public static class RendererHelper implements IWoodPileRenderer {

        private IIcon[] textures = new IIcon[6];
        private int[] rotations = new int[6];
        private float[] scalesXY = new float[6];
        private float[] scalesYZ = new float[6];

        public RendererHelper(boolean rotated) {
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
}
