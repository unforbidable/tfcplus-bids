package com.unforbidable.tfc.bids.features.building.carving;

import com.unforbidable.tfc.bids.core.keybinding.action.KeyBindingActionContext;
import com.unforbidable.tfc.bids.features.building.carving.main.CarvingHelper;
import com.unforbidable.tfc.bids.features.utility.adze.item.ItemAdze;

public class CarvingKeyBinding {

    public static void changeToolMode(KeyBindingActionContext context) {
        if (context.player.getCurrentEquippedItem().getItem() instanceof ItemAdze) {
            CarvingHelper.setPlayerCarvingMode(context.player);
        }
    }

}
