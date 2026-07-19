package com.unforbidable.tfc.bids.api.features.churning;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

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
