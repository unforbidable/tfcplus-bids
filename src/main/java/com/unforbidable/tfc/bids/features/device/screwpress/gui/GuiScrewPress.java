package com.unforbidable.tfc.bids.features.device.screwpress.gui;

import com.dunk.tfc.GUI.GuiContainerTFC;
import com.unforbidable.tfc.bids.features.device.screwpress.container.ContainerScrewPress;
import com.unforbidable.tfc.bids.features.device.screwpress.tileentity.TileEntityScrewPressBarrel;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiScrewPress extends GuiContainerTFC {

    private static ResourceLocation texture = new ResourceLocation(Tags.MOD_ID, "textures/gui/gui_screwpressbarrel.png");

    private final TileEntityScrewPressBarrel tileEntityScrewPressBarrel;

    public GuiScrewPress(InventoryPlayer inventoryplayer, TileEntityScrewPressBarrel te, World world, int x, int y, int z) {
        super(new ContainerScrewPress(inventoryplayer, te, world, x, y, z), 176, 85);

        tileEntityScrewPressBarrel = te;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        this.drawGui(texture);
    }

}
