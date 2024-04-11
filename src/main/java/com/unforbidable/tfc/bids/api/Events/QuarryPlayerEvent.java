package com.unforbidable.tfc.bids.api.Events;

import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class QuarryPlayerEvent extends PlayerEvent {

    public enum Action {
        QUARRY_FINISHED
    };

    public final TileEntityQuarry tileEntityQuarry;
    public final Action action;
    public final ItemStack result;

    public QuarryPlayerEvent(EntityPlayer player, TileEntityQuarry tileEntityQuarry, Action action, ItemStack result) {
        super(player);

        this.tileEntityQuarry = tileEntityQuarry;
        this.action = action;
        this.result = result;
    }

}
