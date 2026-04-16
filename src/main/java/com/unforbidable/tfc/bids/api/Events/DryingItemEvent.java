package com.unforbidable.tfc.bids.api.Events;

import com.unforbidable.tfc.bids.Core.Drying.DryingItem;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class DryingItemEvent extends Event {

    public final TileEntity dryingTileEntity;
    public final DryingItem dryingItem;
    public final DryingRecipe dryingRecipe;

    public DryingItemEvent(TileEntity dryingTileEntity, DryingItem dryingItem, DryingRecipe dryingRecipe) {
        this.dryingTileEntity = dryingTileEntity;
        this.dryingItem = dryingItem;
        this.dryingRecipe = dryingRecipe;
    }

    @Cancelable
    public static class SelectNextRecipe extends DryingItemEvent {

        public final DryingRecipe nextDryingRecipe;

        public SelectNextRecipe(TileEntity dryingTileEntity, DryingItem dryingItem, DryingRecipe dryingRecipe, DryingRecipe nextDryingRecipe) {
            super(dryingTileEntity, dryingItem, dryingRecipe);

            this.nextDryingRecipe = nextDryingRecipe;
        }
    }

    public static class Activate extends DryingItemEvent {

        public final EntityPlayer player;
        public final int slot;

        public boolean handled = false;

        public Activate(TileEntity dryingTileEntity, DryingItem dryingItem, DryingRecipe dryingRecipe, EntityPlayer player, int slot) {
            super(dryingTileEntity, dryingItem, dryingRecipe);

            this.player = player;
            this.slot = slot;
        }

    }

    public static class Retrieve extends DryingItemEvent {

        public final EntityPlayer player;
        public final int slot;

        public Retrieve(TileEntity dryingTileEntity, DryingItem dryingItem, DryingRecipe dryingRecipe, EntityPlayer player, int slot) {
            super(dryingTileEntity, dryingItem, dryingRecipe);

            this.player = player;
            this.slot = slot;
        }

    }

}
