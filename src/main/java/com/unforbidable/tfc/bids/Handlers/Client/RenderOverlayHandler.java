package com.unforbidable.tfc.bids.Handlers.Client;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.Core.Carving.CarvingHelper;
import com.unforbidable.tfc.bids.Items.ItemAdze;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.Enums.EnumAdzeMode;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

public class RenderOverlayHandler {

    public static ResourceLocation icons = new ResourceLocation(Tags.MOD_ID, "textures/gui/icons.png");

    @SubscribeEvent
    public void render(RenderGameOverlayEvent.Pre event) {
        ScaledResolution sr = event.resolution;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        InventoryPlayer playerInventory = player.inventory;

        int mid = sr.getScaledWidth() / 2;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        TFC_Core.bindTexture(icons);

        if (playerInventory.getCurrentItem() != null) {
            Item currentItem = playerInventory.getCurrentItem().getItem();

            if (currentItem instanceof ItemAdze)
            {
                EnumAdzeMode mode = CarvingHelper.getAdzeCarvingMode(player);
                drawTexturedModalRect(mid + 95, sr.getScaledHeight() - 21, (20 * mode.ordinal()), 0, 20, 20);
            }
        }
    }

    void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par6, 0.0, (par3 + 0) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, 0.0, (par3 + par5) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + 0, 0.0, (par3 + par5) * f, (par4 + 0) * f1);
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0.0, (par3 + 0) * f, (par4 + 0) * f1);
        tessellator.draw();
    }

}
