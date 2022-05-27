package com.unforbidable.tfc.bids.TFC;

import com.unforbidable.tfc.bids.Containers.ContainerCrucible;
import com.unforbidable.tfc.bids.Gui.GuiCrucible;

import net.minecraft.entity.player.InventoryPlayer;

public class PlayerInventory {

    public static void buildInventoryLayout(ContainerCrucible container, InventoryPlayer inventory, int i,
            int j, boolean b, boolean c) {
        com.dunk.tfc.Core.Player.PlayerInventory.buildInventoryLayout(container, inventory, i, j, b, c);
    }

    public static void drawInventory(GuiCrucible guiCrucible, int width, int height, int shiftedYSize) {
    }

}
