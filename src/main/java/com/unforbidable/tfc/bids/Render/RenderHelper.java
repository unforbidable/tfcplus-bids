package com.unforbidable.tfc.bids.Render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class RenderHelper {

    public static List<Vec3[]> boundsToVectors(AxisAlignedBB[] disc) {
        return boundsToVectors(disc, 0, 0, 0);
    }

    public static List<Vec3[]> boundsToVectors(AxisAlignedBB[] disc, double rx, double ry, double rz) {
        ArrayList<Vec3[]> vectors = new ArrayList<Vec3[]>();
        for (AxisAlignedBB bb : disc) {
            Vec3[] vs = new Vec3[8];
            vs[0] = Vec3.createVectorHelper(bb.minX, bb.minY, bb.minZ);
            vs[1] = Vec3.createVectorHelper(bb.maxX, bb.minY, bb.minZ);
            vs[2] = Vec3.createVectorHelper(bb.minX, bb.minY, bb.maxZ);
            vs[3] = Vec3.createVectorHelper(bb.maxX, bb.minY, bb.maxZ);
            vs[4] = Vec3.createVectorHelper(bb.minX, bb.maxY, bb.minZ);
            vs[5] = Vec3.createVectorHelper(bb.maxX, bb.maxY, bb.minZ);
            vs[6] = Vec3.createVectorHelper(bb.minX, bb.maxY, bb.maxZ);
            vs[7] = Vec3.createVectorHelper(bb.maxX, bb.maxY, bb.maxZ);

            for (int i = 0; i < 8; i++) {
                vs[i] = vs[i].addVector(rx, ry, rz);
            }

            vectors.add(vs);
        }
        return vectors;
    }

    public static void renderBoxWithOffsetAndRotation(Vec3[] vectors, double offsetX, double offsetY, double offsetZ, float rotation) {
        float width = (float) (vectors[1].xCoord - vectors[0].xCoord);
        float height = (float) (vectors[4].yCoord - vectors[0].yCoord);
        float depth = (float) (vectors[2].zCoord - vectors[0].zCoord);

        Vec3[] copy = new Vec3[8];
        for (int i = 0; i < 8; i++) {
            copy[i] = vectors[i].addVector(0, 0, 0);
            copy[i].rotateAroundZ(rotation);
            copy[i] = copy[i].addVector(offsetX, offsetY, offsetZ);
        }

        renderBox(copy[0], copy[1], copy[2], copy[3], copy[4], copy[5], copy[6], copy[7], width, height, depth);
    }

    private static void renderBox(Vec3 xyz, Vec3 Xyz, Vec3 xyZ, Vec3 XyZ, Vec3 xYz, Vec3 XYz, Vec3 xYZ, Vec3 XYZ, float width, float height, float depth) {
        Tessellator t = Tessellator.instance;
        double u, v, U, V;
        u = 0;
        v = 0;
        U = 1;
        V = 1;

        float bw = ((1 - width) / 2f);
        float bd = ((1 - depth) / 2f);
        float bh = ((1 - height) / 2f);

        //back
        renderFace(t, Xyz, xyz, xYz, XYz, u + bh, U - bh, v + bw, V - bw);
        //front
        renderFace(t, xyZ, XyZ, XYZ, xYZ, u + bh, U - bh, v + bw, V - bw);
        //top
        renderFace(t, XYz, xYz, xYZ, XYZ, u + bd, U - bd, v + bw, V - bw);
        //right
        renderFace(t, xyz, xyZ, xYZ, xYz, u + bh, U - bh, v + bd, V - bd);
        //left
        renderFace(t, XYz, XYZ, XyZ, Xyz, u + bh, U - bh, v + bd, V - bd);
        //bottom
        renderFace(t, xyz, Xyz, XyZ, xyZ, u + bd, U - bd, v + bw, V - bw);
    }

    private static void renderFace(Tessellator t, Vec3 a, Vec3 b, Vec3 c, Vec3 d, double u, double U, double v, double V) {
        Vec3 v1 = Vec3.createVectorHelper(b.xCoord - a.xCoord, b.yCoord - a.yCoord, b.zCoord - a.zCoord);
        Vec3 v2 = Vec3.createVectorHelper(c.xCoord - b.xCoord, c.yCoord - b.yCoord, c.zCoord - b.zCoord);
        Vec3 norm = v1.crossProduct(v2).normalize();

        t.startDrawingQuads();
        t.setNormal((float) norm.xCoord, (float) norm.yCoord, (float) norm.zCoord);

        t.addVertexWithUV(a.xCoord, a.yCoord, a.zCoord, u, v);
        t.addVertexWithUV(b.xCoord, b.yCoord, b.zCoord, u, V);
        t.addVertexWithUV(c.xCoord, c.yCoord, c.zCoord, U, V);
        t.addVertexWithUV(d.xCoord, d.yCoord, d.zCoord, U, v);
        t.draw();
    }

    public static void renderBlockFire(int x, int y, int z, RenderBlocks renderer, float offsetY, float scaleXZ, float scaleY, float color) {
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = Blocks.fire.getFireIcon(0);
        IIcon iicon1 = Blocks.fire.getFireIcon(1);
        IIcon iicon2 = iicon;

        if (renderer.hasOverrideBlockTexture())
        {
            iicon2 = renderer.overrideBlockTexture;
        }

        tessellator.setColorOpaque_F(color, color, color);
        tessellator.setBrightness(Blocks.fire.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        double minU = iicon2.getMinU();
        double minV = iicon2.getMinV();
        double maxU = iicon2.getMaxU();
        double maxV = iicon2.getMaxV();
        float f = 1.4F * scaleY;
        double x0;
        double x1;
        double z0;
        double z1;
        double x2;
        double x3;
        double z2;
        double z3;

        double s0 = (1 - scaleXZ) / 2;

        x0 = (double)x + 0.5D + 0.2D * scaleXZ;
        x1 = (double)x + 0.5D - 0.2D * scaleXZ;
        z0 = (double)z + 0.5D + 0.2D * scaleXZ;
        z1 = (double)z + 0.5D - 0.2D * scaleXZ;
        x2 = (double)x + 0.5D - 0.3D * scaleXZ;
        x3 = (double)x + 0.5D + 0.3D * scaleXZ;
        z2 = (double)z + 0.5D - 0.3D * scaleXZ;
        z3 = (double)z + 0.5D + 0.3D * scaleXZ;
        tessellator.addVertexWithUV(x2, (float)y + f + offsetY, z + 1 - s0, maxU, minV);
        tessellator.addVertexWithUV(x0, y + 0 + offsetY, z + 1 - s0, maxU, maxV);
        tessellator.addVertexWithUV(x0, y + 0 + offsetY, z + s0, minU, maxV);
        tessellator.addVertexWithUV(x2, (float)y + f + offsetY, z + s0, minU, minV);
        tessellator.addVertexWithUV(x3, (float)y + f + offsetY, z + s0, maxU, minV);
        tessellator.addVertexWithUV(x1, y + 0 + offsetY, z + s0, maxU, maxV);
        tessellator.addVertexWithUV(x1, y + 0 + offsetY, z + 1 - s0, minU, maxV);
        tessellator.addVertexWithUV(x3, (float)y + f + offsetY, z + 1 - s0, minU, minV);

        minU = iicon1.getMinU();
        minV = iicon1.getMinV();
        maxU = iicon1.getMaxU();
        maxV = iicon1.getMaxV();
        tessellator.addVertexWithUV(x + 1 - s0, (float)y + f + offsetY, z3, maxU, minV);
        tessellator.addVertexWithUV(x + 1 - s0, y + 0 + offsetY, z1, maxU, maxV);
        tessellator.addVertexWithUV(x + s0, y + 0 + offsetY, z1, minU, maxV);
        tessellator.addVertexWithUV(x + s0, (float)y + f + offsetY, z3, minU, minV);
        tessellator.addVertexWithUV(x + s0, (float)y + f + offsetY, z2, maxU, minV);
        tessellator.addVertexWithUV(x + s0, y + 0 + offsetY, z0, maxU, maxV);
        tessellator.addVertexWithUV(x + 1 - s0, y + 0 + offsetY, z0, minU, maxV);
        tessellator.addVertexWithUV(x + 1 - s0, (float)y + f + offsetY, z2, minU, minV);

        x0 = (double)x + 0.5D - 0.5D * scaleXZ;
        x1 = (double)x + 0.5D + 0.5D * scaleXZ;
        z0 = (double)z + 0.5D - 0.5D * scaleXZ;
        z1 = (double)z + 0.5D + 0.5D * scaleXZ;
        x2 = (double)x + 0.5D - 0.4D * scaleXZ;
        x3 = (double)x + 0.5D + 0.4D * scaleXZ;
        z2 = (double)z + 0.5D - 0.4D * scaleXZ;
        z3 = (double)z + 0.5D + 0.4D * scaleXZ;
        tessellator.addVertexWithUV(x2, (float)y + f + offsetY, z + s0, minU, minV);
        tessellator.addVertexWithUV(x0, y + 0 + offsetY, z + s0, minU, maxV);
        tessellator.addVertexWithUV(x0, y + 0 + offsetY, z + 1 - s0, maxU, maxV);
        tessellator.addVertexWithUV(x2, (float)y + f + offsetY, z + 1 - s0, maxU, minV);
        tessellator.addVertexWithUV(x3, (float)y + f + offsetY, z + 1 - s0, minU, minV);
        tessellator.addVertexWithUV(x1, y + 0 + offsetY, z + 1 - s0, minU, maxV);
        tessellator.addVertexWithUV(x1, y + 0 + offsetY, z + s0, maxU, maxV);
        tessellator.addVertexWithUV(x3, (float)y + f + offsetY, z + s0, maxU, minV);

        minU = iicon.getMinU();
        minV = iicon.getMinV();
        maxU = iicon.getMaxU();
        maxV = iicon.getMaxV();
        tessellator.addVertexWithUV(x + s0, (float)y + f + offsetY, z3, minU, minV);
        tessellator.addVertexWithUV(x + s0, y + 0 + offsetY, z1, minU, maxV);
        tessellator.addVertexWithUV(x + 1 - s0, y + 0 + offsetY, z1, maxU, maxV);
        tessellator.addVertexWithUV(x + 1 - s0, (float)y + f + offsetY, z3, maxU, minV);
        tessellator.addVertexWithUV(x + 1 - s0, (float)y + f + offsetY, z2, minU, minV);
        tessellator.addVertexWithUV(x + 1 - s0, y + 0 + offsetY, z0, minU, maxV);
        tessellator.addVertexWithUV(x + s0, y + 0 + offsetY, z0, maxU, maxV);
        tessellator.addVertexWithUV(x + s0, (float)y + f + offsetY, z2, maxU, minV);
    }

    private static boolean isFireRenderedSide(IBlockAccess world, int x, int y, int z, ForgeDirection d) {
        Block block = world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
        return !block.isOpaqueCube() && !block.isSideSolid(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite());
    }

    public static void renderBlockFireSides(int x, int y, int z, RenderBlocks renderer, float offsetY, float scaleXZ, float scaleY, float color, boolean alwaysRenderSides) {
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = Blocks.fire.getFireIcon(0);
        IIcon iicon1 = Blocks.fire.getFireIcon(1);
        IIcon iicon2 = iicon;

        if (renderer.hasOverrideBlockTexture()) {
            iicon2 = renderer.overrideBlockTexture;
        }

        tessellator.setColorOpaque_F(color, color, color);
        tessellator.setBrightness(Blocks.fire.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        double d0 = iicon2.getMinU();
        double d1 = iicon2.getMinV();
        double d2 = iicon2.getMaxU();
        double d3 = iicon2.getMaxV();
        float f = 1.4F * scaleY;
        double d5;

        float f2 = 0.2F * offsetY;
        float f1 = 0.0F + offsetY;

        double s0 = (1 - scaleXZ) / 2;

        if ((x + y + z & 1) == 1) {
            d0 = iicon1.getMinU();
            d1 = iicon1.getMinV();
            d2 = iicon1.getMaxU();
            d3 = iicon1.getMaxV();
        }

        if ((x / 2 + y / 2 + z / 2 & 1) == 1) {
            d5 = d2;
            d2 = d0;
            d0 = d5;
        }

        if (alwaysRenderSides || isFireRenderedSide(renderer.blockAccess, x, y, z, ForgeDirection.EAST)) {
            x++;
            tessellator.addVertexWithUV((float) x - s0 + f2, (float) y + f + f1, z + 1 - s0, d2, d1);
            tessellator.addVertexWithUV(x - s0, (float) (y) + f1, z + 1 - s0, d2, d3);
            tessellator.addVertexWithUV(x - s0, (float) (y) + f1, z + s0, d0, d3);
            tessellator.addVertexWithUV((float) x - s0 + f2, (float) y + f + f1, z + s0, d0, d1);
            tessellator.addVertexWithUV((float) x - s0 + f2, (float) y + f + f1, z + s0, d0, d1);
            tessellator.addVertexWithUV(x - s0, (float) (y) + f1, z + s0, d0, d3);
            tessellator.addVertexWithUV(x - s0, (float) (y) + f1, z + 1 - s0, d2, d3);
            tessellator.addVertexWithUV((float) x - s0 + f2, (float) y + f + f1, z + 1 - s0, d2, d1);
            x--;
        }

        if (alwaysRenderSides || isFireRenderedSide(renderer.blockAccess, x, y, z, ForgeDirection.WEST)) {
            x--;
            tessellator.addVertexWithUV((float) (x + s0 + 1) - f2, (float) y + f + f1, z + s0, d0, d1);
            tessellator.addVertexWithUV(x + s0 + 1, (float) (y) + f1, z + s0, d0, d3);
            tessellator.addVertexWithUV(x + s0 + 1, (float) (y) + f1, z + 1 - s0, d2, d3);
            tessellator.addVertexWithUV((float) (x + s0 + 1) - f2, (float) y + f + f1, z + 1 - s0, d2, d1);
            tessellator.addVertexWithUV((float) (x + s0 + 1) - f2, (float) y + f + f1, z + 1 - s0, d2, d1);
            tessellator.addVertexWithUV(x + s0 + 1, (float) (y) + f1, z + 1 - s0, d2, d3);
            tessellator.addVertexWithUV(x + s0 + 1, (float) (y) + f1, z + s0, d0, d3);
            tessellator.addVertexWithUV((float) (x + s0 + 1) - f2, (float) y + f + f1, z + s0, d0, d1);
            x++;
        }

        if (alwaysRenderSides || isFireRenderedSide(renderer.blockAccess, x, y, z, ForgeDirection.SOUTH)) {
            z++;
            tessellator.addVertexWithUV(x + s0, (float) y + f + f1, (float) z - s0 + f2, d2, d1);
            tessellator.addVertexWithUV(x + s0, (float) (y) + f1, z - s0, d2, d3);
            tessellator.addVertexWithUV(x + 1 - s0, (float) (y) + f1, z - s0, d0, d3);
            tessellator.addVertexWithUV(x + 1 - s0, (float) y + f + f1, (float) z - s0 + f2, d0, d1);
            tessellator.addVertexWithUV(x + 1 - s0, (float) y + f + f1, (float) z - s0 + f2, d0, d1);
            tessellator.addVertexWithUV(x + 1 - s0, (float) (y) + f1, z - s0, d0, d3);
            tessellator.addVertexWithUV(x + s0, (float) (y) + f1, z - s0, d2, d3);
            tessellator.addVertexWithUV(x + s0, (float) y + f + f1, (float) z - s0 + f2, d2, d1);
            z--;
        }

        if (alwaysRenderSides || isFireRenderedSide(renderer.blockAccess, x, y, z, ForgeDirection.NORTH)) {
            z--;
            tessellator.addVertexWithUV(x + 1 - s0, (float) y + f + f1, (float) (z + s0 + 1) - f2, d0, d1);
            tessellator.addVertexWithUV(x + 1 - s0, (float) (y) + f1, z + s0 + 1, d0, d3);
            tessellator.addVertexWithUV(x + s0, (float) (y) + f1, z + s0 + 1, d2, d3);
            tessellator.addVertexWithUV(x + s0, (float) y + f + f1, (float) (z + s0 + 1) - f2, d2, d1);
            tessellator.addVertexWithUV(x + s0, (float) y + f + f1, (float) (z + s0 + 1) - f2, d2, d1);
            tessellator.addVertexWithUV(x + s0, (float) (y) + f1, z + s0 + 1, d2, d3);
            tessellator.addVertexWithUV(x + 1 - s0, (float) (y) + f1, z + s0 + 1, d0, d3);
            tessellator.addVertexWithUV(x + 1 - s0, (float) y + f + f1, (float) (z + s0 + 1) - f2, d0, d1);
            //z++;
        }
    }

}
