package com.unforbidable.tfc.bids.api.Events;

import com.unforbidable.tfc.bids.TileEntities.TileEntityChoppingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ChoppingBlockPlayerEvent extends PlayerEvent {

    public enum Action {
        ITEM_CRAFTED
    }

    public final TileEntityChoppingBlock tileEntityChoppingBlock;
    public final Action action;
    public final ItemStack result;

    public ChoppingBlockPlayerEvent(EntityPlayer player, TileEntityChoppingBlock tileEntityChoppingBlock, Action action, ItemStack result) {
        super(player);

        this.tileEntityChoppingBlock = tileEntityChoppingBlock;
        this.action = action;
        this.result = result;
    }

}
