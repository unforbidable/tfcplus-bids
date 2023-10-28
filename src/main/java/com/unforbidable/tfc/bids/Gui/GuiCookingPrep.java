package com.unforbidable.tfc.bids.Gui;

import com.dunk.tfc.GUI.GuiContainerTFC;
import com.unforbidable.tfc.bids.Containers.ContainerCookingPrep;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCookingPrep;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GuiCookingPrep extends GuiContainerTFC {

    private static ResourceLocation texture = new ResourceLocation(Tags.MOD_ID, "textures/gui/gui_cookingprep.png");

    private final TileEntityCookingPrep tileEntityCookingPrep;

    public GuiCookingPrep(InventoryPlayer inventoryplayer, TileEntityCookingPrep te, World world, int x, int y, int z) {
        super(new ContainerCookingPrep(inventoryplayer, te, world, x, y, z), 176, 85);

        tileEntityCookingPrep = te;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        this.drawGui(texture);
    }

    /*
     * Draws extra pieces on a GUI such as moving gauges and arrows.
     * Must be called before PlayerInventory.drawInventory() to avoid extra binding
     * of textures.
     */
    @Override
    protected void drawForeground(int guiLeft, int guiTop) {
        float[] weights = tileEntityCookingPrep.getRecipeIngredientWeights();
        int[] xs = { 40, 62, 80, 98, 116 };
        if (weights != null && weights.length == 5) {
            for (int i = 0; i < 5; i++) {
                if (weights[i] > 0) {
                    drawCenteredString(this.fontRendererObj, String.valueOf(Math.round(weights[i])), guiLeft + 8 + xs[i], guiTop + 14, 0x555555);
                }
            }
        }
    }

    @Override
    public void drawCenteredString(FontRenderer fontrenderer, String s, int i, int j, int k) {
        fontrenderer.drawString(s, i - fontrenderer.getStringWidth(s) / 2, j, k);
    }

}
