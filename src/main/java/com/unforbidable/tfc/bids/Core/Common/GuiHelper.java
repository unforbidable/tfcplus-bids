package com.unforbidable.tfc.bids.Core.Common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class GuiHelper {

    public static Point getMousePosition() {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scaleWidth = res.getScaledWidth();
        int scaledHeight = res.getScaledHeight();
        int resWidth = mc.displayWidth;
        int resHeight = mc.displayHeight;
        int x = Mouse.getX() * scaleWidth / resWidth;
        int y = scaledHeight - Mouse.getY() * scaledHeight / resHeight - 1;
        return new Point(x, y);
    }

    public static void drawRect(Rectangle rect, int color) {
        Gui.drawRect(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, color);
    }

    public static void drawTriangles(List<Polygon> triangles, int color) {
        if (triangles.size() > 0) {
            float a = (float) (color >> 24 & 255) / 255.0F;
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;

            Tessellator tessellator = Tessellator.instance;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(r, g, b, a);
            tessellator.startDrawing(GL11.GL_TRIANGLES);

            for (Polygon polygon : triangles) {
                double x1 = polygon.xpoints[0];
                double y1 = polygon.ypoints[0];
                double x2 = polygon.xpoints[1];
                double y2 = polygon.ypoints[1];
                double x3 = polygon.xpoints[2];
                double y3 = polygon.ypoints[2];

                tessellator.addVertex(x1, y1, 0.0D);
                tessellator.addVertex(x2, y2, 0.0D);
                tessellator.addVertex(x3, y3, 0.0D);
            }

            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

}
