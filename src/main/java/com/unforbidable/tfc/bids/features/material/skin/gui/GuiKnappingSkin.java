package com.unforbidable.tfc.bids.features.material.skin.gui;

import com.dunk.tfc.GUI.GuiKnapping;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GuiKnappingSkin extends GuiKnapping {

    public GuiKnappingSkin(InventoryPlayer inventoryplayer, ItemStack is, World world, int x, int y, int z) {
        // We cannot tell the super to use ContainerSpecialCraftingGlass
        // instead of ContainerSpecialCrafting
        // but it doesn't seem to matter as the functionality we need to override is
        // on server side where ContainerSpecialCraftingDough is used
        super(inventoryplayer, is, world, x, y, z);
    }

}
