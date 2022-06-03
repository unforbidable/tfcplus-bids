package com.unforbidable.tfc.bids.Gui;

import java.util.ArrayList;

import com.unforbidable.tfc.bids.Containers.ContainerCrucible;
import com.unforbidable.tfc.bids.TFC.GuiContainerTFC;
import com.unforbidable.tfc.bids.TFC.PlayerInventory;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;
import com.unforbidable.tfc.bids.api.BidsOptions;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class GuiCrucible extends GuiContainerTFC {

    protected final TileEntityCrucible crucibleTileEntity;

    public GuiCrucible(InventoryPlayer inventory, TileEntityCrucible te, World world, int x, int y, int z,
            ContainerCrucible container) {
        super(container, 176, 85);
        crucibleTileEntity = te;
        setDrawInventory(true);
    }

    protected abstract GaugeOffsets getGaugeOffsets();

    protected abstract ResourceLocation getGuiContainerBackgroundTexture();

    @Override
    protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) {
        ResourceLocation texture = getGuiContainerBackgroundTexture();
        drawGui(texture);

        PlayerInventory.drawInventory(this, width, height, getShiftedYSize());
    }

    private int getVolumeScaled(int scale) {
        return (int) Math.ceil((float) crucibleTileEntity.getLiquidVolume() * scale
                / crucibleTileEntity.getMaxVolume());
    }

    private int getTempScaled(int scale) {
        return (int) Math.ceil((float) crucibleTileEntity.getCombinedTemp() * scale
                / crucibleTileEntity.getMaxTemp());
    }

    /*
     * Draws extra pieces on a GUI such as moving gauges and arrows.
     * Must be called before PlayerInventory.drawInventory() to avoid extra binding
     * of textures.
     */
    @Override
    protected void drawForeground(int guiLeft, int guiTop) {
        GaugeOffsets offsets = getGaugeOffsets();

        int tempScaled = getTempScaled(49);
        int volumeScaled = getVolumeScaled(50);
        drawTexturedModalRect(guiLeft + offsets.tempX - 4, guiTop + offsets.tempY - tempScaled + 48,
                185, 32, 15, 5);
        drawTexturedModalRect(guiLeft + offsets.volumeX, guiTop + offsets.volumeY - volumeScaled + 50,
                204, 18, 7, volumeScaled);

        if (crucibleTileEntity.isSmelting()) {
            drawTexturedModalRect(guiLeft + offsets.volumeX - 8, guiTop + offsets.volumeY + 21,
                    195, 40, 5, 7);
        }

        if (crucibleTileEntity.isLiquidIn()) {
            drawTexturedModalRect(guiLeft + offsets.volumeX + 10, guiTop + offsets.volumeY + 3,
                    185, 40, 5, 7);
        }

        if (crucibleTileEntity.isLiquidOut()) {
            drawTexturedModalRect(guiLeft + offsets.volumeX + 10, guiTop + offsets.volumeY + 39,
                    195, 40, 5, 7);
        }

        if (crucibleTileEntity.isOutputAvailable()
                && BidsOptions.Crucible.enableOutputDisplay) {
            drawCenteredString(this.fontRendererObj, StatCollector.translateToLocal("gui.Output") + ": "
                    + crucibleTileEntity.getOutput(), guiLeft + 88, guiTop + 72, 0x555555);
        }
    }

    @Override
    public void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GaugeOffsets offsets = getGaugeOffsets();
        if (this.mouseInRegion(offsets.tempX, offsets.tempY, 9, 50, mouseX, mouseY)) {
            if (BidsOptions.Crucible.enableExactTemperatureDisplay) {
                ArrayList<String> list = new ArrayList<String>();
                list.add("" + crucibleTileEntity.getCombinedTemp());
                this.drawHoveringText(list, mouseX - guiLeft, mouseY - guiTop + 8, this.fontRendererObj);
            }
        } else if (this.mouseInRegion(offsets.volumeX, offsets.volumeY, 9, 50, mouseX, mouseY)) {
            ArrayList<String> list = new ArrayList<String>();
            if (crucibleTileEntity.getLiquidVolume() > 0) {
                list.addAll(crucibleTileEntity
                        .getLiquidInfo(StatCollector.translateToLocal("gui.symbol.BulletPoint") + " "));
                if (crucibleTileEntity.isSolidified())
                    list.add(StatCollector.translateToLocal("gui.Solidified"));
            } else {
                list.add(StatCollector.translateToLocal("gui.Empty"));
            }

            this.drawHoveringText(list, mouseX - guiLeft, mouseY - guiTop + 8, this.fontRendererObj);
        }
    }

    public static class GaugeOffsets {
        public final int tempX;
        public final int tempY;
        public final int volumeX;
        public final int volumeY;

        public GaugeOffsets(int tempX, int tempY, int volumeX, int volumeY) {
            this.tempX = tempX;
            this.tempY = tempY;
            this.volumeX = volumeX;
            this.volumeY = volumeY;
        }
    }

}
