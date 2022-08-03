package com.unforbidable.tfc.bids.Gui;

import com.dunk.tfc.Core.Player.PlayerInventory;
import com.dunk.tfc.GUI.GuiContainerTFC;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Containers.ContainerWoodPile;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiWoodPile extends GuiContainerTFC {

    private static ResourceLocation texture = new ResourceLocation(Tags.MOD_ID, "textures/gui/gui_woodpile.png");

    final TileEntityWoodPile woodPileTileEntity;

    public GuiWoodPile(InventoryPlayer inventory, TileEntityWoodPile te, World world, int x, int y, int z) {
        super(new ContainerWoodPile(inventory, te, world, x, y, z), 176, 85);

        woodPileTileEntity = te;

        setDrawInventory(true);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) {
        drawGui(texture);

        PlayerInventory.drawInventory(this, width, height, getShiftedYSize());
    }

    /*
     * Draws extra pieces on a GUI such as moving gauges and arrows.
     * Must be called before PlayerInventory.drawInventory() to avoid extra binding
     * of textures.
     */
    @Override
    protected void drawForeground(int guiLeft, int guiTop) {
        // int slotOffsetX = 17;
        // int slotOffsetY = 26;
        // int slotStrideX = 18;
        // int slotStrideY = 18;
        // int slotRows = 2;
        // int slotColumns = 8;

        // int i = 0;
        // for (int iy = 0; iy < slotRows; iy++) {
        // for (int ix = 0; ix < slotColumns; ix++) {
        // final float seasoning = woodPileTileEntity.getItemInSlotSeasoning(i++);
        // if (seasoning < 1) {
        // final int scaled = (int) Math.ceil(16 * (1 - seasoning));
        // drawTexturedModalRect(guiLeft + slotOffsetX + slotStrideX * ix,
        // guiTop + slotOffsetY + slotStrideY * iy + 16 - scaled,
        // 200, 26 + 16 - scaled, 16, scaled);
        // }
        // }
        // }
    }

    @Override
    public void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        // int slotOffsetX = 17;
        // int slotOffsetY = 26;
        // int slotStrideX = 18;
        // int slotStrideY = 18;
        // int slotRows = 2;
        // int slotColumns = 8;

        // int i = 0;
        // for (int iy = 0; iy < slotRows; iy++) {
        //     for (int ix = 0; ix < slotColumns; ix++) {
        //         final float seasoning = woodPileTileEntity.getItemInSlotSeasoning(i++);
        //         if (seasoning < 1) {
        //             final int scaled = (int) Math.floor(13 * seasoning);
        //             int x = slotOffsetX + slotStrideX * ix + 1;
        //             int y = slotOffsetY + slotStrideY * iy + 13;
        //             drawRect(x, y, x + 14, y + 2, 0xff000000);
        //             drawRect(x, y, x + 13, y + 1, 0xff404040);
        //             drawRect(x, y, x + scaled + 1, y + 1, 0xfff0f0f0);
        //         }
        //     }
        // }
    }

}
