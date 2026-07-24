package com.unforbidable.tfc.bids.features.utility.flail.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class FlailItemRenderer implements IItemRenderer
{

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.EQUIPPED;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		EntityLivingBase entity = (EntityLivingBase) data[1];
		IIcon iicon = entity.getItemIcon(item, 0);

		if (iicon == null) {
			GL11.glPopMatrix();
			return;
		}
		GL11.glPushMatrix();

		Minecraft.getMinecraft().getTextureManager().bindTexture(Minecraft.getMinecraft().getTextureManager().getResourceLocation(item.getItemSpriteNumber()));
		TextureUtil.func_152777_a(false, false, 1.0F);
		Tessellator tessellator = Tessellator.instance;

		float f = iicon.getMinU();
		float f1 = iicon.getMaxU();
		float f2 = iicon.getMinV();
		float f3 = iicon.getMaxV();

		if (entity instanceof EntityPlayer) {
			GL11.glTranslatef(0.0F, 0.3F, 0.0F);
			GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);

			GL11.glTranslatef(0.9375F, 0.0625F, 0.0F);
			GL11.glScalef(4F, 4F, 4F);
			GL11.glTranslatef(-0.4F, -0.6F, 0.0F);

            if (((EntityPlayer) entity).isBlocking())
            {
                GL11.glTranslatef(0.0f, 0.0f, 0.3F);
                GL11.glRotatef(80.0F, 1.0F, 1.0F, 0.0F);
            }
            else if (entity.swingProgress != 0)
            {
                float partial = (((System.currentTimeMillis() % 1000) * 0.06f) % 3) * 0.333f;
                float p = entity.getSwingProgress(partial);
                if (p > 0.625f)
                {
                    p = 1.25f - p;
                }
                GL11.glRotatef(10.0F + (p) * 60f, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-10 + (p) * 60f, -1.0F, -1.0F, 0.0F);
                GL11.glTranslatef(-0.2f, 0.2f, 0);
            }
		}

		ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		Minecraft.getMinecraft().getTextureManager().bindTexture(Minecraft.getMinecraft().getTextureManager().getResourceLocation(item.getItemSpriteNumber()));
		TextureUtil.func_147945_b();
		GL11.glPopMatrix();
	}

}
