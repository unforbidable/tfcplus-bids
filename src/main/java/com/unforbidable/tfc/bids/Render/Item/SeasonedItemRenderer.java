package com.unforbidable.tfc.bids.Render.Item;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class SeasonedItemRenderer implements IItemRenderer {

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
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        renderIcon(0, 0, is.getItem().getIconIndex(is), 16, 16);
        renderQuad(1, 14, 14, 1, 0xffffaa00);

        GL11.glPopAttrib();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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
