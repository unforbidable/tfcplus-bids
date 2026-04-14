package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ISurfaceItemPlacer {

    boolean placeItemOnSurface(World world, int x, int y, int z, int face, EntityPlayer entityPlayer);

}
