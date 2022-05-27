package com.unforbidable.tfc.bids.Gui;

import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Containers.ContainerFireClayCrucible;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiFireClayCrucible extends GuiCrucible {

    private static ResourceLocation texture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/gui_fireclaycrucible.png");
    private static GaugeOffsets gaugeOffsets = new GaugeOffsets(152, 18, 107, 18);

    public GuiFireClayCrucible(InventoryPlayer inventory, TileEntityCrucible te, World world, int x, int y, int z) {
        super(inventory, te, world, x, y, z, new ContainerFireClayCrucible(inventory, te, world, x, y, z));
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
