package com.unforbidable.tfc.bids.features.device.lamp.main.firestarting;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingHandler;
import com.unforbidable.tfc.bids.features.device.lamp.tileentity.TileEntityClayLamp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ClayLampFileStartingHandler extends FireStartingHandler {

    public ClayLampFileStartingHandler(int priority) {
        super(priority);
    }

    @Override
    public boolean ignite(EntityPlayer player, World world, int x, int y, int z, int side) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityClayLamp) {
            TileEntityClayLamp teClayLamp = (TileEntityClayLamp) te;
            if (!teClayLamp.isOnFire() && !TFC_Core.isExposedToRain(world, x, y, z)) {
                teClayLamp.setOnFire(true);

                // true is not returned after successful lighting
                // to prevent kindling consumption when lighting lamps
            }
        }

        return false;
    }

}
