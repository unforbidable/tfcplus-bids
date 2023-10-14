package com.unforbidable.tfc.bids.Render.Blocks;

import com.unforbidable.tfc.bids.Core.Cooking.CookingHelper;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.CookingPotBounds;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.EnumCookingPotPlacement;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCookingPot;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderCookingPot implements ISimpleBlockRenderingHandler {

    private static final CookingPotBounds inventoryCookingPotBounds = CookingPotBounds.getBoundsForPlacement(EnumCookingPotPlacement.GROUND.getPlacement());

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        // actual block metadata always 0 or 1
        int actualMetadata = metadata & 1;

        renderPartInv(renderer, block, actualMetadata, inventoryCookingPotBounds.getBottomBounds());
        for (int i = 0; i < 4; i++) {
            renderPartInv(renderer, block, actualMetadata, inventoryCookingPotBounds.getSidesBounds()[i]);
        }

        if ((metadata & CookingHelper.META_COOKING_POT_HAS_LID) != 0) {
            for (int i = 0; i < 2; i++) {
                renderPartInv(renderer, BidsBlocks.cookingPotLid, actualMetadata, inventoryCookingPotBounds.getLidsBounds()[i]);
            }
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        renderer.renderAllFaces = true;

        TileEntityCookingPot te = (TileEntityCookingPot) world.getTileEntity(x, y, z);
        if (!te.isClientDataLoaded()) {
            return false;
        }

        CookingPotBounds bounds = te.getCachedBounds();

        renderPart(renderer, x, y, z, block, bounds.getBottomBounds());
        for (int i = 0; i < bounds.getSidesBounds().length; i++) {
            renderPart(renderer, x, y, z, block, bounds.getSidesBounds()[i]);
        }

        if (te.hasLid()) {
            for (int i = 0; i < bounds.getLidsBounds().length; i++) {
                renderPart(renderer, x, y, z, BidsBlocks.cookingPotLid, bounds.getLidsBounds()[i]);
            }
        } else {
            if (te.hasSteamingMesh()) {
                renderPart(renderer, x, y, z, BidsBlocks.steamingMesh, bounds.getMeshBounds());
            }

            if (te.hasFluid()) {
                FluidStack fluid = te.getTopFluidStack();
                float fullnessRatio = (float)te.getTotalLiquidVolume() / (float)te.getMaxFluidVolume();
                AxisAlignedBB contentBounds = bounds.getContentBounds().copy();
                contentBounds.maxY = contentBounds.minY + bounds.getMaxContentHeight() * fullnessRatio;
                renderContent(renderer, x, y, z, block, fluid, contentBounds);
            }
        }

        renderer.renderAllFaces = false;
        return true;
    }

    private void renderContent(RenderBlocks renderer, int x, int y, int z, Block block, FluidStack fluid, AxisAlignedBB bounds) {
        int color = fluid.getFluid().getColor(fluid);
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        IIcon still = fluid.getFluid().getStillIcon();
        renderer.setOverrideBlockTexture(still);

        renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
        renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, r, g, b);

        renderer.clearOverrideBlockTexture();
    }

    private void renderPart(RenderBlocks renderer, int x, int y, int z, Block block,
                            final AxisAlignedBB bounds) {
        renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
        renderer.renderStandardBlock(block, x, y, z);
    }

    private void renderPartInv(RenderBlocks renderer, Block block, int meta,
                            final AxisAlignedBB bounds) {
        renderer.setRenderBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
        renderInvBlock(block, meta, renderer);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return 0;
    }

    public static void renderInvBlock(Block block, int m, RenderBlocks renderer)
    {
        Tessellator var14 = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.4F, -0.5F);
        var14.startDrawingQuads();
        var14.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, m));
        var14.draw();
        var14.startDrawingQuads();
        var14.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, m));
        var14.draw();
        GL11.glTranslatef(0.5F, 0.4F, 0.5F);
    }

}
