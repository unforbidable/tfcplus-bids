package com.unforbidable.tfc.bids.api.features.drying;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.item.ItemStack;

public abstract class DryingEvent extends Event {

    public final ItemStack input;
    public final ItemStack result;
    public final DryingEnvironment environment;

    public DryingEvent(ItemStack input, ItemStack result, DryingEnvironment environment) {
        this.input = input;
        this.result = result;
        this.environment = environment;
    }

    public static class ItemCrafted extends DryingEvent {

        public ItemCrafted(ItemStack input, ItemStack result, DryingEnvironment environment) {
            super(input, result, environment);
        }

    }

}
