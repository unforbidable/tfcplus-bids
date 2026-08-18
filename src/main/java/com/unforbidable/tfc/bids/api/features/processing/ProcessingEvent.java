package com.unforbidable.tfc.bids.api.features.processing;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class ProcessingEvent extends Event {

    public final ItemStack input;
    public final ItemStack result;
    public final EntityPlayer player;

    public ProcessingEvent(ItemStack input, ItemStack result, EntityPlayer player) {
        this.input = input;
        this.result = result;
        this.player = player;
    }

    public static class ItemCrafted extends ProcessingEvent {

        public ItemCrafted(ItemStack input, ItemStack result, EntityPlayer player) {
            super(input, result, player);
        }

    }

}
