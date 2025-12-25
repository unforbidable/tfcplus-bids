package com.unforbidable.tfc.bids.api.Events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class HandworkPlayerEvent extends PlayerEvent {

    public enum Action {
        ITEM_CRAFTED
    }

    public final Action action;
    public final ItemStack input;
    public final ItemStack result;
    public final ItemStack tool;

    public HandworkPlayerEvent(EntityPlayer player, Action action, ItemStack input, ItemStack result, ItemStack tool) {
        super(player);

        this.action = action;
        this.input = input;
        this.result = result;
        this.tool = tool;
    }

}
