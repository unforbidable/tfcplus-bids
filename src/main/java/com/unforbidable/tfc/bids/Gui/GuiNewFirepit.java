package com.unforbidable.tfc.bids.Gui;

import com.dunk.tfc.GUI.GuiContainerTFC;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Containers.ContainerNewFirepit;
import com.unforbidable.tfc.bids.TileEntities.TileEntityNewFirepit;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiNewFirepit extends GuiContainerTFC {

    private static ResourceLocation texture = new ResourceLocation(Tags.MOD_ID, "textures/gui/gui_firepit.png");

    private final TileEntityNewFirepit firepitTE;

    public GuiNewFirepit(InventoryPlayer inventoryplayer, TileEntityNewFirepit te, World world, int x, int y, int z) {
        super(new ContainerNewFirepit(inventoryplayer, te, world, x, y, z), 176, 85);

        firepitTE = te;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        this.drawGui(texture);
    }

    @Override
    protected void drawForeground(int guiLeft, int guiTop) {
        // Fixes OpenEye-reported NPE
        if (firepitTE != null) {
            int scale = firepitTE.getTemperatureScaled(49);
            drawTexturedModalRect(guiLeft + 30, guiTop + 65 - scale, 185, 31, 15, 6);
        }
    }

}
