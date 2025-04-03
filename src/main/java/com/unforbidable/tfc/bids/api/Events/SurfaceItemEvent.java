package com.unforbidable.tfc.bids.api.Events;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.item.ItemStack;

public abstract class SurfaceItemEvent extends Event {

    public final ItemStack itemStack;

    public SurfaceItemEvent(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static class Icon extends SurfaceItemEvent {

        public String iconName;

        public Icon(ItemStack itemStack, String iconName) {
            super(itemStack);
            this.iconName = iconName;
        }

    }

}
