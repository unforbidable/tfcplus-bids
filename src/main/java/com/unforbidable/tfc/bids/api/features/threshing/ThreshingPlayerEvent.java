package com.unforbidable.tfc.bids.api.features.threshing;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import java.util.List;

public class ThreshingPlayerEvent extends PlayerEvent {

    public enum Action {
        ITEM_CRAFTED
    }

    public final Action action;
    public final ItemStack input;
    public final ItemStack result;
    public final ItemStack extraDrop;
    public final ItemStack tool;

    public ThreshingPlayerEvent(EntityPlayer player, Action action, ItemStack input, ItemStack result, ItemStack extraDrop, ItemStack tool) {
        super(player);

        this.action = action;
        this.input = input;
        this.result = result;
        this.extraDrop = extraDrop;
        this.tool = tool;
    }

}
