package com.unforbidable.tfc.bids.Render.Tiles;

import com.dunk.tfc.Blocks.Devices.BlockAxleBearing;
import com.dunk.tfc.Render.TESR.TESRRotator;
import com.dunk.tfc.TileEntities.TERotator;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderTileScrew extends TESRRotator
{
    protected static final ResourceLocation WOOD_SCREW_1_TEXTURE = new ResourceLocation(Tags.MOD_ID + ":textures/blocks/Wood Screw 1.png");
    protected static final ResourceLocation WOOD_SCREW_2_TEXTURE = new ResourceLocation(Tags.MOD_ID + ":textures/blocks/Wood Screw 2.png");
    protected static final ResourceLocation WOOD_SCREW_3_TEXTURE = new ResourceLocation(Tags.MOD_ID + ":textures/blocks/Wood Screw 3.png");
    protected static final ResourceLocation WOOD_SCREW_4_TEXTURE = new ResourceLocation(Tags.MOD_ID + ":textures/blocks/Wood Screw 4.png");

	private static Vec3[] axleVectors;

	public RenderTileScrew()
	{
		initVectors();
	}

	@Override
	public void initVectors() {
		//axleVectors = setupVecs(flatXOffset, flatYOffset, flatZOffset, flatRotPX, flatRotPY, flatRotPZ, flatRotX, flatRotY, flatRotZ, flatWidth, flatHeight, flatDepth);
		axleVectors = setupVecs(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0.3, 0.3);
	}

	@Override
	public void renderSpecificRotator(TERotator te, float teRotation, float partialTick) {
		int direction = getDirection(te);
		bindTexture(WOOD_TEXTURE);

		if(direction > 1)
		{
			GL11.glTranslated(0.5, 0.5, 0.5);
			GL11.glRotatef(90, 0, 0, 1);
			GL11.glTranslated(-0.5, -0.5, -0.5);
		}

		handleVectorRotation(axleVectors, 1, 0.3f, 0.3f, 0,teRotation);
		if(direction > 1)
		{
			GL11.glTranslated(0.5, 0.5, 0.5);
			GL11.glRotatef(-90, 0, 0, 1);
			GL11.glTranslated(-0.5, -0.5, -0.5);
			//Can't hook up to a waterwheel if we're vertical
			return;
		}
		handleVectorRotation(axleVectors, 1, 0.3f, 0.3f, 0,teRotation);
	}

	@Override
	public int getDirection(TERotator te) {
		return BlockAxleBearing.getDirectionFromMetadata(te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord));
	}

    @Override
    protected void renderBox(Vec3 xyz, Vec3 Xyz, Vec3 xyZ, Vec3 XyZ, Vec3 xYz, Vec3 XYz, Vec3 xYZ, Vec3 XYZ, float bWidth, float bHeight, float bDepth) {
        Tessellator t = Tessellator.instance;
        //GL11.glDisable(GL11.GL_LIGHTING);
        double u, v, U, V;
        u = 0;
        v = 0;
        U = 1;
        V = 1;

        float bw = ((1 - bWidth) / 2f);
        float bd = ((1 - bDepth) / 2f);
        float bh = ((1 - bHeight) / 2f);

        //back
        bindTexture(WOOD_SCREW_2_TEXTURE);
        renderFace(t, Xyz, xyz, xYz, XYz, u + bh, U - bh, v + bw, V - bw);
        //front
        bindTexture(WOOD_SCREW_4_TEXTURE);
        renderFace(t, XYZ, xYZ, xyZ, XyZ, u + bh, U - bh, v + bw, V - bw);
        //top
        bindTexture(WOOD_SCREW_1_TEXTURE);
        renderFace(t, XYz, xYz, xYZ, XYZ, u + bd, U - bd, v + bw, V - bw);
        //right
        bindTexture(WOOD_TEXTURE);
        renderFace(t, xyz, xyZ, xYZ, xYz, u + bh, U - bh, v + bd, V - bd);
        //left
        bindTexture(WOOD_TEXTURE);
        renderFace(t, XYz, XYZ, XyZ, Xyz, u + bh, U - bh, v + bd, V - bd);
        //bottom
        bindTexture(WOOD_SCREW_3_TEXTURE);
        renderFace(t, XyZ, xyZ, xyz, Xyz, u + bd, U - bd, v + bw, V - bw);
    }
}
