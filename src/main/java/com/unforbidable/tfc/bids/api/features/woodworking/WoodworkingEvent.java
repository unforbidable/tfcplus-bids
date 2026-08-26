package com.unforbidable.tfc.bids.api.features.woodworking;

import cpw.mods.fml.common.eventhandler.Event;
import java.awt.geom.Area;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class WoodworkingEvent extends Event {

    public final EntityPlayer player;
    public final Area cutout;
    public final ItemStack input;
    public final ItemStack result;
    public final List<WoodworkingActionSummary> summary;

    public WoodworkingEvent(EntityPlayer player, Area cutout, ItemStack input, ItemStack result, List<WoodworkingActionSummary> summary) {
        this.player = player;
        this.cutout = cutout;
        this.input = input;
        this.result = result;
        this.summary = summary;
    }

    public static class ItemCrafted extends WoodworkingEvent {

        public ItemCrafted(EntityPlayer player, Area cutout, ItemStack input, ItemStack result, List<WoodworkingActionSummary> summary) {
            super(player, cutout, input, result, summary);

        }

    }

    public static class ItemPickedUp extends WoodworkingEvent {

        public ItemPickedUp(EntityPlayer player, Area cutout, ItemStack input, ItemStack result, List<WoodworkingActionSummary> summary) {
            super(player, cutout, input, result, summary);
        }

    }

}
