package com.unforbidable.tfc.bids.api.Events;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class FillContainerEvent extends Event {

    public final EntityPlayer player;
    public final ItemStack input;
    public final ItemStack output;

    public FillContainerEvent(EntityPlayer player, ItemStack input, ItemStack output) {
        this.player = player;
        this.input = input;
        this.output = output;
    }

}
