package com.unforbidable.tfc.bids.Core.Crafting;

import cpw.mods.fml.common.gameevent.PlayerEvent;

public class CraftingContext {

    public final PlayerEvent.ItemCraftedEvent event;

    public CraftingContext(PlayerEvent.ItemCraftedEvent event) {
        this.event = event;
    }

}
