package com.unforbidable.tfc.bids.api.Events;

import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CruciblePlayerEvent extends PlayerEvent {

    public enum Action {
        RETRIEVE_OUTPUT,
        FILL_BLOWPIPE
    }

    public final TileEntityCrucible tileEntityCrucible;
    public final Action action;
    public final ItemStack result;

    public CruciblePlayerEvent(EntityPlayer player, TileEntityCrucible tileEntityCrucible, Action action, ItemStack result) {
        super(player);

        this.tileEntityCrucible = tileEntityCrucible;
        this.action = action;
        this.result = result;
    }

}
