package com.unforbidable.tfc.bids.api.Events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class WoodworkingPlayerEvent extends PlayerEvent {

    public enum Action {
        ITEM_CRAFTED
    }

    public final Action action;
    public final ItemStack input;
    public final ItemStack result;

    public WoodworkingPlayerEvent(EntityPlayer player, Action action, ItemStack input, ItemStack result) {
        super(player);

        this.action = action;
        this.input = input;
        this.result = result;
    }

}
