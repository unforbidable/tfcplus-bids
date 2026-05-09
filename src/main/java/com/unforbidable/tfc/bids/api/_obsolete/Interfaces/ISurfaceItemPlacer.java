package com.unforbidable.tfc.bids.api._obsolete.Interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ISurfaceItemPlacer {

    boolean placeItemOnSurface(World world, int x, int y, int z, int face, float hitX, float hitY, float hitZ, EntityPlayer entityPlayer);

}
