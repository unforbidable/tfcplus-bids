package com.unforbidable.tfc.bids.Render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

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

}
