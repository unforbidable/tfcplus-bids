package com.unforbidable.tfc.bids.Gui;

import com.dunk.tfc.Core.Player.PlayerInventory;
import com.dunk.tfc.GUI.GuiContainerTFC;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Containers.ContainerWoodPile;
import com.unforbidable.tfc.bids.Containers.Slots.SlotWoodPile;
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
        int slotOffsetX = 17;
        int slotOffsetY = 26;
        int slotStrideX = 18;
        int slotStrideY = 18;
        int slotRows = 2;
        int slotColumns = 8;

        int i = 0;
        for (int iy = 0; iy < slotRows; iy++) {
            for (int ix = 0; ix < slotColumns; ix++) {
                if (!SlotWoodPile.isWoodPileSlotEnabled(i, woodPileTileEntity)) {
                    drawTexturedModalRect(guiLeft + slotOffsetX + ix * slotStrideX,
                            guiTop + slotOffsetY + iy * slotStrideY,
                            200, 44, 16, 16);
                }

                i++;
            }
        }
    }

    @Override
    public void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    }

}
