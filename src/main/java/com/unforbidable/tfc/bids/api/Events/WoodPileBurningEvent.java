package com.unforbidable.tfc.bids.api.Events;

import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import cpw.mods.fml.common.eventhandler.Event;

public class WoodPileBurningEvent extends Event {

    public final TileEntityWoodPile woodPile;
    public final long ticks;
    public final int temp;

    public WoodPileBurningEvent(TileEntityWoodPile woodPile, long ticks, int temp) {
        this.woodPile = woodPile;
        this.ticks = ticks;
        this.temp = temp;
    }

}
