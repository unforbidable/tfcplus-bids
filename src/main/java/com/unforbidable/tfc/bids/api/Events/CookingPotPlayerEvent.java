package com.unforbidable.tfc.bids.api.Events;

import com.unforbidable.tfc.bids.TileEntities.TileEntityCookingPot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CookingPotPlayerEvent extends PlayerEvent {

    public enum Action {
        RETRIEVE_COOKED_MEAL
    }

    public final TileEntityCookingPot tileEntityCookingPot;
    public final Action action;
    public final ItemStack result;

    public CookingPotPlayerEvent(EntityPlayer player, TileEntityCookingPot tileEntityCookingPot, Action action, ItemStack result) {
        super(player);

        this.tileEntityCookingPot = tileEntityCookingPot;
        this.action = action;
        this.result = result;
    }

}
