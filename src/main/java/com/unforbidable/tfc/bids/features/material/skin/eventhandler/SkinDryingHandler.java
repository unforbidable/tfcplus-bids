package com.unforbidable.tfc.bids.features.material.skin.eventhandler;

import com.unforbidable.tfc.bids.api.features.drying.DryingEvent;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SkinDryingHandler {

    @SubscribeEvent
    public void onDryingItemCrafted(DryingEvent.ItemCrafted event) {
        if (event.environment.isSmoked()) {
            if (event.input.getItem() instanceof ItemSkin && event.result.getItem() instanceof ItemSkin) {
                SkinTag inputTag = SkinTag.of(event.input);
                SkinTag resultTag = SkinTag.of(event.result);

                resultTag.setAnimal(inputTag.getAnimal());

                // Smoking removes salt and fluids
                resultTag.setSalted(false);
                resultTag.setFluid(null);

                // Decay is removed from the weight when skin is preserved
                float newWeight = inputTag.getWeight() - inputTag.getDecay();
                resultTag.setWeight(Math.round(newWeight));
            }
        }
    }

}
