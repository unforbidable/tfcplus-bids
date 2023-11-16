package com.unforbidable.tfc.bids.Render.Blocks;

import com.unforbidable.tfc.bids.Core.Crops.BidsCropIndex;
import com.unforbidable.tfc.bids.Core.Crops.BidsCropManager;
import com.unforbidable.tfc.bids.TileEntities.TileEntityNewCrop;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderNewCrop implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityNewCrop) {
            TileEntityNewCrop tileEntityNewCrop = (TileEntityNewCrop) world.getTileEntity(x, y, z);

            BidsCropIndex crop = BidsCropManager.findCropById(tileEntityNewCrop.cropId);
            if (crop != null) {

                switch (crop.cropRenderer.getRenderType()) {
                    case BLOCK:
                        renderBlockCropsImpl(block, x, y, z, renderer, crop.cropRenderer.getWidth(), crop.cropRenderer.getHeight());
                        break;

                    case CROSSED_SQUARES:
                        drawCrossedSquares(block, x, y, z, renderer, crop.cropRenderer.getWidth(), crop.cropRenderer.getHeight());
                        break;

                    default:
                        renderer.renderBlockCrops(block, x, y, z);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

    private static void renderBlockCropsImpl(Block block, double i, double j, double k, RenderBlocks renderblocks,
                                             double width, double height) {
        Tessellator tess = Tessellator.instance;
		/*
	 	int l = block.colorMultiplier(renderblocks.blockAccess, (int)i, (int)j, (int)k);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }*/

        //GL11.glColor3f(f, f1, f2);
        GL11.glColor3f(1, 1, 1);
        int brightness = block.getMixedBrightnessForBlock(renderblocks.blockAccess, (int) i, (int) j, (int) k);
        tess.setBrightness(brightness);
        //tess.setColorOpaque_F(f, f1, f2);
        tess.setColorOpaque_F(1, 1, 1);

        IIcon icon = block.getIcon(renderblocks.blockAccess, (int) i, (int) j, (int) k,
            renderblocks.blockAccess.getBlockMetadata((int) i, (int) j, (int) k));
        if (renderblocks.hasOverrideBlockTexture())
            icon = renderblocks.overrideBlockTexture;

        if (icon != null)
        {
            if (((int) i & 1) > 0)
            {
                k += 0.0001;
            }
            if (((int) k & 1) > 0)
            {
                i += 0.0001;
            }

            double minU = icon.getMinU();
            double maxU = icon.getMaxU();
            double minV = icon.getMinV();
            double maxV = icon.getMaxV();
            double minX = i + 0.25D;
            double maxX = i + 0.75D;
            double minZ = k + 0.5D - width;
            double maxZ = k + 0.5D + width;
            double y = j;

            tess.addVertexWithUV(minX, y + height, minZ, minU, minV);
            tess.addVertexWithUV(minX, y, minZ, minU, maxV);
            tess.addVertexWithUV(minX, y, maxZ, maxU, maxV);
            tess.addVertexWithUV(minX, y + height, maxZ, maxU, minV);
            tess.addVertexWithUV(minX, y + height, maxZ, minU, minV);
            tess.addVertexWithUV(minX, y, maxZ, minU, maxV);
            tess.addVertexWithUV(minX, y, minZ, maxU, maxV);
            tess.addVertexWithUV(minX, y + height, minZ, maxU, minV);
            tess.addVertexWithUV(maxX, y + height, maxZ, minU, minV);
            tess.addVertexWithUV(maxX, y, maxZ, minU, maxV);
            tess.addVertexWithUV(maxX, y, minZ, maxU, maxV);
            tess.addVertexWithUV(maxX, y + height, minZ, maxU, minV);
            tess.addVertexWithUV(maxX, y + height, minZ, minU, minV);
            tess.addVertexWithUV(maxX, y, minZ, minU, maxV);
            tess.addVertexWithUV(maxX, y, maxZ, maxU, maxV);
            tess.addVertexWithUV(maxX, y + height, maxZ, maxU, minV);
            minX = i + 0.5D - width;
            maxX = i + 0.5D + width;
            minZ = k + 0.5D - 0.25D;
            maxZ = k + 0.5D + 0.25D;
            tess.addVertexWithUV(minX, y + height, minZ, minU, minV);
            tess.addVertexWithUV(minX, y, minZ, minU, maxV);
            tess.addVertexWithUV(maxX, y, minZ, maxU, maxV);
            tess.addVertexWithUV(maxX, y + height, minZ, maxU, minV);
            tess.addVertexWithUV(maxX, y + height, minZ, minU, minV);
            tess.addVertexWithUV(maxX, y, minZ, minU, maxV);
            tess.addVertexWithUV(minX, y, minZ, maxU, maxV);
            tess.addVertexWithUV(minX, y + height, minZ, maxU, minV);
            tess.addVertexWithUV(maxX, y + height, maxZ, minU, minV);
            tess.addVertexWithUV(maxX, y, maxZ, minU, maxV);
            tess.addVertexWithUV(minX, y, maxZ, maxU, maxV);
            tess.addVertexWithUV(minX, y + height, maxZ, maxU, minV);
            tess.addVertexWithUV(minX, y + height, maxZ, minU, minV);
            tess.addVertexWithUV(minX, y, maxZ, minU, maxV);
            tess.addVertexWithUV(maxX, y, maxZ, maxU, maxV);
            tess.addVertexWithUV(maxX, y + height, maxZ, maxU, minV);
        }
    }

    private static void drawCrossedSquares(Block block, double x, double y, double z, RenderBlocks renderblocks,
                                           double width, double height) {
        Tessellator tess = Tessellator.instance;
        GL11.glColor3f(1, 1, 1);

        int brightness = block.getMixedBrightnessForBlock(renderblocks.blockAccess, (int) x, (int) y, (int) z);
        tess.setBrightness(brightness);
        tess.setColorOpaque_F(1, 1, 1);

        IIcon icon = block.getIcon(renderblocks.blockAccess, (int) x, (int) y, (int) z,
            renderblocks.blockAccess.getBlockMetadata((int) x, (int) y, (int) z));
        if (renderblocks.hasOverrideBlockTexture())
            icon = renderblocks.overrideBlockTexture;

        double minU = icon.getMinU();
        double maxU = icon.getMaxU();
        double minV = icon.getMinV();
        double maxV = icon.getMaxV();

        double minX = x + 0.5D - width;
        double maxX = x + 0.5D + width;
        double minZ = z + 0.5D - width;
        double maxZ = z + 0.5D + width;

        tess.addVertexWithUV(minX, y + height, minZ, minU, minV);
        tess.addVertexWithUV(minX, y + 0.0D, minZ, minU, maxV);
        tess.addVertexWithUV(maxX, y + 0.0D, maxZ, maxU, maxV);
        tess.addVertexWithUV(maxX, y + height, maxZ, maxU, minV);

        tess.addVertexWithUV(maxX, y + height, maxZ, minU, minV);
        tess.addVertexWithUV(maxX, y + 0.0D, maxZ, minU, maxV);
        tess.addVertexWithUV(minX, y + 0.0D, minZ, maxU, maxV);
        tess.addVertexWithUV(minX, y + height, minZ, maxU, minV);

        tess.addVertexWithUV(minX, y + height, maxZ, minU, minV);
        tess.addVertexWithUV(minX, y + 0.0D, maxZ, minU, maxV);
        tess.addVertexWithUV(maxX, y + 0.0D, minZ, maxU, maxV);
        tess.addVertexWithUV(maxX, y + height, minZ, maxU, minV);

        tess.addVertexWithUV(maxX, y + height, minZ, minU, minV);
        tess.addVertexWithUV(maxX, y + 0.0D, minZ, minU, maxV);
        tess.addVertexWithUV(minX, y + 0.0D, maxZ, maxU, maxV);
        tess.addVertexWithUV(minX, y + height, maxZ, maxU, minV);
    }

}
