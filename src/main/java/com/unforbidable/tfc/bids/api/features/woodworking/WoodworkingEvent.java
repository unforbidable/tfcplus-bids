package com.unforbidable.tfc.bids.api.features.woodworking;

import cpw.mods.fml.common.eventhandler.Event;
import java.awt.geom.Area;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class WoodworkingEvent extends Event {

    public final EntityPlayer player;
    public final Area cutout;
    public final ItemStack input;

    public WoodworkingEvent(EntityPlayer player, Area cutout, ItemStack input) {
        this.player = player;
        this.cutout = cutout;
        this.input = input;
    }

    public static class ItemCrafted extends WoodworkingEvent {

        public final ItemStack result;

        public ItemCrafted(EntityPlayer player, Area cutout, ItemStack input, ItemStack result) {
            super(player, cutout, input);

            this.result = result;
        }

    }

    public static class ItemPickedUp extends WoodworkingEvent {

        public final ItemStack result;

        public ItemPickedUp(EntityPlayer player, Area cutout, ItemStack input, ItemStack result) {
            super(player, cutout, input);

            this.result = result;
        }

    }

}
