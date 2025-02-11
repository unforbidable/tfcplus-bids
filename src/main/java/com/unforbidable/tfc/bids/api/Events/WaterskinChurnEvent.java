package com.unforbidable.tfc.bids.api.Events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;

public class WaterskinChurnEvent extends PlayerEvent {

    public enum Action {
        DONE
    }

    public final Action action;
    public final ItemStack waterskin;
    public final ItemStack result;

    public WaterskinChurnEvent(EntityPlayer player, Action action, ItemStack waterskin, ItemStack result) {
        super(player);
        this.action = action;

        this.waterskin = waterskin;
        this.result = result;
    }

}
