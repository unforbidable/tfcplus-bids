package com.unforbidable.tfc.bids.features.device.cookingprep.eventhandler;

import com.unforbidable.tfc.bids.api.features.surfaceitem.SurfaceItemEvent;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingConfig;
import com.unforbidable.tfc.bids.features.device.cookingprep.main.CookingPrepHelper;
import com.unforbidable.tfc.bids.features.utility.largebowl.item.ItemLargeBowl;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CookingPrepEventHandler {

    @SubscribeEvent
    public void onSurfaceItemPlace(SurfaceItemEvent.Place event) {
        if (!event.placed && event.face == 1) {
            if (event.itemStack.getItem() instanceof ItemLargeBowl ||
                OreDictionaryHelper.itemStackIsOre(event.itemStack, "itemHandAxe") ||
                CookingConfig.enableFoodPrepPlacementOverride && OreDictionaryHelper.itemStackIsOre(event.itemStack, "itemKnife")) {
                if (CookingPrepHelper.isValidCookingPrepLocation(event.world, event.x, event.y + 1, event.z)) {
                    if (!event.world.isRemote) {
                        CookingPrepHelper.placeCookingPrep(event.world, event.x, event.y + 1, event.z);
                    }

                    event.placed = true;
                }
            }
        }
    }

}
