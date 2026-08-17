package com.unforbidable.tfc.bids.features.material.skin.render;

import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class SkinItemRenderer implements IItemRenderer {

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

        if (is.getItem() instanceof ItemSkin && is.hasTagCompound()) {
            renderIcon(0, 0, is.getItem().getIconIndex(is), 16, 16);

            float alpha = 1f;
            int color = is.getItem().getColorFromItemStack(is, 0);
            GL11.glColor4f(((color & 0xFF0000) >> 16) / 255f, ((color & 0x00ff00) >> 8) / 255f, (color & 0x0000ff) / 255f, alpha);
            renderIcon(0, 0, is.getItem().getIconIndex(is), 16, 16);

            float decayPerc = Math.max(Food.getDecay(is) / Food.getWeight(is), 0);
            float decayTop = decayPerc * 13.0F;

            float weightPerc = Food.getWeight(is) / ((IFood) is.getItem()).getFoodMaxWeight(is);

            if (weightPerc <= 1 && weightPerc >= 0) {
                if (((IFood) is.getItem()).renderDecay()) {
                    if (decayPerc < 0.10) {
                        decayTop = decayTop * 10;
                        renderQuad(1, 13, 13 - decayTop, 1, 0x00ff00);
                    } else
                        renderQuad(1, 13, 13 - decayTop, 1, 0xff0000);
                }
                if (((IFood) is.getItem()).renderWeight()) {
                    renderQuad(1, 14, 13, 1, 0);
                    float weightTop = weightPerc * 13.0F;

                    renderQuad(1, 14, weightTop, 1, 0xffffff);
                }
            }
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
