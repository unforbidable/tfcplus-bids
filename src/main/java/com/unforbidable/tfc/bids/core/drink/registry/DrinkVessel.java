package com.unforbidable.tfc.bids.core.drink.registry;

import net.minecraft.item.Item;

public class DrinkVessel {

    public final Item containerItem;
    public final int volume;
    public final boolean pottery;
    public final int[] overlays;

    public DrinkVessel(Item containerItem, int volume, boolean pottery, int[] overlays) {
        this.containerItem = containerItem;
        this.volume = volume;
        this.pottery = pottery;
        this.overlays = overlays;
    }

}
