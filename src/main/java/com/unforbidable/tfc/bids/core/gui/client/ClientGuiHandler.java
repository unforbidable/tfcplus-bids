package com.unforbidable.tfc.bids.core.gui.client;

import com.unforbidable.tfc.bids.core.gui.GuiHandler;
import com.unforbidable.tfc.bids.core.gui.GuiRegistry;
import com.unforbidable.tfc.bids.core.gui.provider.GuiProviderContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ClientGuiHandler extends GuiHandler {

    @SideOnly(Side.CLIENT)
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        String name = GuiRegistry.guis.stream()
            .filter(gui -> gui.value == id)
            .findAny()
            .map(gui -> gui.key)
            .orElse(null);

        if (name != null) {
            GuiScreenProvider<?, ?> guiScreenProvider = ClientGuiRegistry.screens.get(name);
            if (guiScreenProvider != null) {
                GuiProviderContext context = new GuiProviderContext(player, world, x, y, z);
                return guiScreenProvider.provider.get(context);
            }
        }

        return null;
    }

}
