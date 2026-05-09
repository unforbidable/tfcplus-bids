package com.unforbidable.tfc.bids.core.gui;

import com.unforbidable.tfc.bids.core.gui.provider.GuiProviderContext;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        String name = GuiRegistry.guis.stream()
            .filter(gui -> gui.value == id)
            .findAny()
            .map(gui -> gui.key)
            .orElse(null);

        if (name != null) {
            ContainerProvider<?, ?> containerProvider = GuiRegistry.container.get(name);
            if (containerProvider != null) {
                GuiProviderContext context = new GuiProviderContext(player, world, x, y, z);
                return containerProvider.provider.get(context);
            }
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

}
