package com.unforbidable.tfc.bids.util;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.gui.GuiRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiUtil {

    public static void openGui(String name, EntityPlayer player, TileEntity tileEntity) {
        openGui(name, player, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    }

    public static void openGui(String name, EntityPlayer player) {
        openGui(name, player, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    private static void openGui(String name, EntityPlayer player, World world, int x, int y, int z) {
        Integer id = GuiRegistry.guis.get(name);
        if (id != null) {
            player.openGui(Bids.instance, id, world, x, y, z);
        } else {
            Bids.LOG.warn("GUI name '{}' not registered", name);
        }
    }

}

