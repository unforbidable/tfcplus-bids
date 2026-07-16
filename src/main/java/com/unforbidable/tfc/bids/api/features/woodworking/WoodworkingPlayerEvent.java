package com.unforbidable.tfc.bids.api.features.woodworking;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import java.awt.geom.Area;

public class WoodworkingPlayerEvent extends PlayerEvent {

    public enum Action {
        ITEM_CRAFTED,
        ITEM_PICKED_UP
    }

    public final Action action;
    public final Area cutout;
    public final ItemStack input;
    public final ItemStack result;

    public WoodworkingPlayerEvent(EntityPlayer player, Action action, Area cutout, ItemStack input, ItemStack result) {
        super(player);

        this.action = action;
        this.cutout = cutout;
        this.input = input;
        this.result = result;
    }

}
