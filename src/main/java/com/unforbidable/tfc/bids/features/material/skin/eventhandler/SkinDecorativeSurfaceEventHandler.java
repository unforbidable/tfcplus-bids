package com.unforbidable.tfc.bids.features.material.skin.eventhandler;

import com.unforbidable.tfc.bids.api.features.decorativesurface.DecorativeSurfaceEvent;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SkinDecorativeSurfaceEventHandler {

    @SubscribeEvent
    public void onDecorativeSurfacePlace(DecorativeSurfaceEvent.Place event) {
        if (event.item.getItem() instanceof ItemSkin) {
            event.canPlace = SkinTag.of(event.item).getWeight() >= SkinHelper.WEIGHT_SMALL;
        }
    }

}
