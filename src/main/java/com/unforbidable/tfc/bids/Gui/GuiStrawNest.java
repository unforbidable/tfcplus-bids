package com.unforbidable.tfc.bids.Gui;

import com.dunk.tfc.GUI.GuiContainerTFC;
import com.unforbidable.tfc.bids.Containers.ContainerStrawNest;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityStrawNest;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiStrawNest extends GuiContainerTFC {

    private static final ResourceLocation texture = new ResourceLocation(Tags.MOD_ID, "textures/gui/gui_strawnest.png");

    public GuiStrawNest(InventoryPlayer inventoryplayer, TileEntityStrawNest te, World world, int x, int y, int z) {
        super(new ContainerStrawNest(inventoryplayer, te, world, x, y, z), 176, 85);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        this.drawGui(texture);
    }

}
