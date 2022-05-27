package com.unforbidable.tfc.bids.Gui;

import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Containers.ContainerClayCrucible;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayCrucible;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiClayCrucible extends GuiCrucible {

    private static ResourceLocation texture = new ResourceLocation(Tags.MOD_ID, "textures/gui/gui_claycrucible.png");
    private static GaugeOffsets gaugeOffsets = new GaugeOffsets(134, 18, 89, 18);

    public GuiClayCrucible(InventoryPlayer inventory, TileEntityClayCrucible te, World world, int x, int y, int z) {
        super(inventory, te, world, x, y, z, new ContainerClayCrucible(inventory, te, world, x, y, z));
    }

    @Override
    protected ResourceLocation getGuiContainerBackgroundTexture() {
        return texture;
    }

    @Override
    protected GaugeOffsets getGaugeOffsets() {
        return gaugeOffsets;
    }

}
