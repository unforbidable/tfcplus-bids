package com.unforbidable.tfc.bids.Render.Item;

import com.unforbidable.tfc.bids.Core.Handwork.HandworkHelper;
import com.unforbidable.tfc.bids.Core.Handwork.HandworkProgress;
import com.unforbidable.tfc.bids.Items.ItemHandworkTool;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class HandworkToolItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack is, Object... data) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        renderIcon(0, 0, is.getItem().getIcon(is, 0), 16, 16);
        if (is.getItem().requiresMultipleRenderPasses()) {
            int i1 = is.getItem().getColorFromItemStack(is, 1);
            float r = (float)(i1 >> 16 & 255) / 255.0F;
            float g = (float)(i1 >> 8 & 255) / 255.0F;
            float b = (float)(i1 & 255) / 255.0F;
            GL11.glColor4f(r, g, b, 1.0F);

            renderIcon(0, 0, is.getItem().getIcon(is, 1), 16, 16);
        }

        HandworkProgress progress = HandworkHelper.loadHandworkProgress(is);
        if (progress != null) {
            float progressPercentage = ((ItemHandworkTool) is.getItem()).getHandworkProgressPercentage(is);
            float progressWidth = progressPercentage * 12.0F;

            renderQuad(2, 1, 12, 1, 0);
            renderQuad(2, 1, progressWidth + 1, 1, 0xffffaa00);
        }

        GL11.glPopAttrib();
    }

    public static void renderIcon(int x, int y, IIcon icon, int sizeX, int sizeY) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + sizeY, 0, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + sizeX, y + sizeY, 0, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + sizeX, y + 0, 0, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(x + 0, y + 0, 0, icon.getMinU(), icon.getMinV());
        tessellator.draw();
    }

    private static void renderQuad(double x, double y, double sizeX, double sizeY, int color) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.setColorOpaque_I(color);
        tess.addVertex(x + 0, y + 0, 0.0D);
        tess.addVertex(x + 0, y + sizeY, 0.0D);
        tess.addVertex(x + sizeX, y + sizeY, 0.0D);
        tess.addVertex(x + sizeX, y + 0, 0.0D);
        tess.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

}
