package com.unforbidable.tfc.bids.Render.Blocks;

import com.dunk.tfc.Blocks.Devices.*;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Common.Bounds.AxleWallBearingBounds;
import com.unforbidable.tfc.bids.TileEntities.TileEntityAxleWallBearing;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderAxleWallBearing implements ISimpleBlockRenderingHandler {

    static boolean enableBrightness = false;

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        //forward
        renderer.overrideBlockTexture = TFCBlocks.woodSupportH.getIcon(0, 0);

        renderer.setRenderBounds(0F, 0F, 0.1F, 1F, 0.3F, 0.9F);
        renderInvBlock(block, renderer);

        //mid
        renderer.setRenderBounds(0F, 0F, 0.1F, 0.3F, 1F, 0.9F);
        renderInvBlock(block, renderer);

        renderer.setRenderBounds(0.7F, 0F, 0.1F, 1F, 1F, 0.9F);
        renderInvBlock(block, renderer);

        //back
        renderer.setRenderBounds(0F, 0.7F, 0.1F, 1F, 1F, 0.9F);
        renderInvBlock(block, renderer);

        renderer.clearOverrideBlockTexture();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.partialRenderBounds = false;
        renderer.renderAllFaces = true;

        int meta = world.getBlockMetadata(x, y, z);
        int direction = BlockAxleBearing.getDirectionFromMetadata(meta);
        AxleWallBearingBounds bounds = AxleWallBearingBounds.getBounds(direction);

        TileEntityAxleWallBearing tileEntityAxleWallBearing = (TileEntityAxleWallBearing) world.getTileEntity(x, y, z);

        if (tileEntityAxleWallBearing.hasCover()) {
            renderer.overrideBlockTexture = TFCBlocks.woodSupportH.getIcon(0, 0);

            for (AxisAlignedBB inningBounds : bounds.getInning()) {
                renderer.setRenderBounds(inningBounds.minX, inningBounds.minY, inningBounds.minZ, inningBounds.maxX, inningBounds.maxY, inningBounds.maxZ);
                renderer.renderStandardBlock(TFCBlocks.planks, x, y, z);
            }

            renderer.clearOverrideBlockTexture();

            Block renderedBlock = Block.getBlockById(tileEntityAxleWallBearing.getCoverBlockId());
            int renderedMetadata = tileEntityAxleWallBearing.getCoverBlockMetadata();

            Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, renderedMetadata, 0);

            for (AxisAlignedBB coverBounds : bounds.getCover()) {
                renderer.setRenderBounds(coverBounds.minX, coverBounds.minY, coverBounds.minZ, coverBounds.maxX, coverBounds.maxY, coverBounds.maxZ);
                renderer.renderStandardBlock(renderedBlock, x, y, z);
            }

            Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, meta, 0);
        } else {
            renderer.overrideBlockTexture = TFCBlocks.woodSupportH.getIcon(0, 0);

            for (AxisAlignedBB bearingBounds : bounds.getBearing()) {
                renderer.setRenderBounds(bearingBounds.minX, bearingBounds.minY, bearingBounds.minZ, bearingBounds.maxX, bearingBounds.maxY, bearingBounds.maxZ);
                renderer.renderStandardBlock(TFCBlocks.planks, x, y, z);
            }

            renderer.clearOverrideBlockTexture();
        }

        renderer.renderAllFaces = false;

        return true;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

    public static void renderInvBlock(Block block, RenderBlocks renderer) {
        Tessellator var14 = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        int brightness = 100 + (int) ((TFC_Time.getTotalTicks() % 50 > 25) ? (50 - (TFC_Time.getTotalTicks() % 50)) : TFC_Time.getTotalTicks() % 50);
        //System.out.println(brightness);
        var14.startDrawingQuads();
        if (enableBrightness)
        {
            var14.setBrightness(brightness);
        }
        var14.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 2));
        var14.draw();
        var14.startDrawingQuads();
        if (enableBrightness)
        {
            var14.setBrightness(brightness);
        }
        var14.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 2));
        var14.draw();
        var14.startDrawingQuads();
        if (enableBrightness)
        {
            var14.setBrightness(brightness);
        }
        var14.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 1));
        var14.draw();
        var14.startDrawingQuads();
        if (enableBrightness)
        {
            var14.setBrightness(brightness);
        }
        var14.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 3));
        var14.draw();
        var14.startDrawingQuads();
        if (enableBrightness)
        {
            var14.setBrightness(brightness);
        }
        var14.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 0));
        var14.draw();
        var14.startDrawingQuads();
        if (enableBrightness)
        {
            var14.setBrightness(brightness);
        }
        var14.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, 0));
        var14.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

    }

}
