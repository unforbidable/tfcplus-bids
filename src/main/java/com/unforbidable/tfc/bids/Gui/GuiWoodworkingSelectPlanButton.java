package com.unforbidable.tfc.bids.Gui;

import com.dunk.tfc.Core.TFC_Core;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class GuiWoodworkingSelectPlanButton extends GuiButton
{
	public ItemStack item;
	private final GuiWoodworking screen;
	protected static final RenderItem ITEM_RENDERER = new RenderItem();

	public GuiWoodworkingSelectPlanButton(int index, int xPos, int yPos, int width, int height, ItemStack ico, GuiWoodworking gui, String s) {
		super(index, xPos, yPos, width, height, s);
		item = ico;
		screen = gui;
	}

	@Override
	public void drawButton(Minecraft mc, int x, int y) {
		if (this.visible) {
			int k = this.getHoverState(this.field_146123_n)-1;

			TFC_Core.bindTexture(GuiWoodworking.TEXTURE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 176, k*18, 18, 18);
			this.field_146123_n = isPointInRegion(x, y);

			if(item != null) {
				renderInventorySlot(item, this.xPosition+1, this.yPosition+1);
			}

			this.mouseDragged(mc, x, y);

			if(field_146123_n) {
				screen.drawTooltip(x, y, this.displayString);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	protected void renderInventorySlot(ItemStack is, int x, int y) {
		if (is != null) {
			ITEM_RENDERER.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), is, x, y);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}

	private boolean isPointInRegion(int mouseX, int mouseY) {
		return mouseX >= xPosition - 1 && mouseX < xPosition + width + 1 && mouseY >= yPosition - 1 && mouseY < yPosition + height + 1;
	}

}
