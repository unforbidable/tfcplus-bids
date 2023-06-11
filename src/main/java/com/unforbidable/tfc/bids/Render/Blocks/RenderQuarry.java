package com.unforbidable.tfc.bids.Render.Blocks;

import com.unforbidable.tfc.bids.Core.Quarry.QuarrySideWedgeData;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryWedgeBounds;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryWedgeModeller;
import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;
import com.unforbidable.tfc.bids.api.Interfaces.IPlugAndFeather;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class RenderQuarry implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        ForgeDirection front = ForgeDirection.UP;

        for (ForgeDirection edge : ForgeDirection.VALID_DIRECTIONS) {
            if (edge != front && edge != front.getOpposite()) {
                for (int i = 0; i < 4; i++) {
                    QuarryWedgeBounds[] b = QuarryWedgeModeller.getEdgeWedgeBounds(front, edge);
                    renderer.setRenderBounds(b[i].x1, b[i].y1, b[i].z1, b[i].x2, b[i].y2, b[i].z2);
                    renderInvBlock(block, metadata, renderer);
                }
            }
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        TileEntityQuarry quarry = (TileEntityQuarry) world.getTileEntity(x, y, z);
        ForgeDirection orientation = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z) & 7);

        renderer.renderAllFaces = true;

        for (QuarrySideWedgeData data : quarry.getWedges()) {
            QuarryWedgeBounds[] b = QuarryWedgeModeller.getEdgeWedgeBounds(orientation, data.direction);
            int i = 0;
            for (ItemStack wedge : data.storage) {
                Block wedgeBlock = ((IPlugAndFeather)wedge.getItem()).getPlugAndFeatherRenderBlock(wedge);
                int wedgeMetadata = ((IPlugAndFeather)wedge.getItem()).getPlugAndFeatherRenderBlockMetadata(wedge);

                Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, wedgeMetadata, 0);

                renderer.setRenderBounds(b[i].x1, b[i].y1, b[i].z1, b[i].x2, b[i].y2, b[i].z2);
                renderer.renderStandardBlock(wedgeBlock, x, y, z);
                i++;
            }
        }

        Minecraft.getMinecraft().theWorld.setBlockMetadataWithNotify(x, y, z, orientation.ordinal(), 0);

        renderer.renderAllFaces = false;

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

    public static void renderInvBlock(Block block, int m, RenderBlocks renderer) {
        Tessellator var14 = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        var14.startDrawingQuads();
        var14.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, m));
        var14.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

}
