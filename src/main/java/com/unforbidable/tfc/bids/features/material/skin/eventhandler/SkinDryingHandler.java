package com.unforbidable.tfc.bids.features.material.skin.eventhandler;

import com.unforbidable.tfc.bids.api.features.drying.DryingEvent;
import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemFinishedSkin;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SkinDryingHandler {

    @SubscribeEvent
    public void onDryingItemCrafted(DryingEvent.ItemCrafted event) {
        if (event.input.getItem() instanceof ItemSkin && event.result.getItem() instanceof ItemSkin) {
            SkinTag inputTag = SkinTag.of(event.input);
            SkinTag resultTag = SkinTag.of(event.result);

            if (event.environment.isSmoked()) {
                // Smoking removes salt and fluids
                resultTag.setSalted(false);
                resultTag.setFluid(null);
            }

            if (resultTag.isStage(SkinTagAccess.STAGE_PRESERVED) || event.result.getItem() instanceof ItemFinishedSkin) {
                // When skin is preserved, or turned into rawhide or leather
                // remove decay from weight
                float newWeight = inputTag.getWeight() - Math.max(0, inputTag.getDecay());
                resultTag.setWeight(Math.round(newWeight));
            } else {
                resultTag.setWeight(inputTag.getWeight());
                resultTag.setDecay(inputTag.getDecay());
                resultTag.setDecayTimer(inputTag.getDecayTimer() + 1);
            }

            if (!(event.result.getItem() instanceof ItemFinishedSkin)) {
                // No animal tag when skin turns into rawhide or leather
                resultTag.setAnimal(inputTag.getAnimal());
            }
        }
    }

}
