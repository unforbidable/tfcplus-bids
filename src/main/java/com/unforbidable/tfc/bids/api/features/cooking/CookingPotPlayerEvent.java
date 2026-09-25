package com.unforbidable.tfc.bids.api.features.cooking;

import com.unforbidable.tfc.bids.features.device.cookingpot.tileentity.TileEntityCookingPot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CookingPotPlayerEvent extends PlayerEvent {

    public enum Action {
        RETRIEVE_COOKED_MEAL
    }

    public final CookingHost cookingHost;
    public final Action action;
    public final ItemStack result;

    public CookingPotPlayerEvent(EntityPlayer player, CookingHost cookingHost, Action action, ItemStack result) {
        super(player);

        this.cookingHost = cookingHost;
        this.action = action;
        this.result = result;
    }

}
