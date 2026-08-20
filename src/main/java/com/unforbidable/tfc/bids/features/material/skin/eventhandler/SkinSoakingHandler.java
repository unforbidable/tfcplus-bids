package com.unforbidable.tfc.bids.features.material.skin.eventhandler;

import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.features.soaking.SoakingEvent;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SkinSoakingHandler {

    @SubscribeEvent
    public void onSoakingItemCrafted(SoakingEvent.ItemCrafted event) {
        if (event.input.getItem() instanceof ItemSkin && event.result.getItem() instanceof ItemSkin) {
            SkinTag inputTag = SkinTag.of(event.input);
            SkinTag resultTag = SkinTag.of(event.result);

            resultTag.setAnimal(inputTag.getAnimal());

            // Only set fluids other than fresh water
            if (event.fluid.getFluid() != TFCFluids.FRESHWATER) {
                resultTag.setFluid(event.fluid.getFluid().getName());
            }

            // Soaking removes salting
            resultTag.setSalted(false);

            resultTag.setWeight(inputTag.getWeight());
            resultTag.setDecay(inputTag.getDecay());
            resultTag.setDecayTimer(inputTag.getDecayTimer());
        }
    }

}
