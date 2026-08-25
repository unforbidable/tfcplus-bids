package com.unforbidable.tfc.bids.features.material.skin.eventhandler;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.features.soaking.SoakingEvent;
import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SkinSoakingHandler {

    @SubscribeEvent
    public void onSoakingItemCrafted(SoakingEvent.ItemCrafted event) {
        if (event.result.getItem() instanceof ItemSkin) {
            SkinTag resultTag = SkinTag.of(event.result);

            if (event.input.getItem() instanceof ItemSkin) {
                SkinTag inputTag = SkinTag.of(event.input);

                resultTag.setAnimal(inputTag.getAnimal());

                // Only set fluids other than fresh water
                if (event.fluid.getFluid() != TFCFluids.FRESHWATER) {
                    resultTag.setFluid(event.fluid.getFluid().getName());
                }

                // Soaking removes salting
                resultTag.setSalted(false);

                resultTag.setWeight(inputTag.getWeight());
                resultTag.setDecay(inputTag.getDecay());
                resultTag.setDecayTimer(inputTag.getDecayTimer() + 1);
            }

            if (resultTag.isStage(SkinTagAccess.STAGE_CLEAN) || resultTag.isStage(SkinTagAccess.STAGE_DEHAIRED)) {
                // Rehydrated skin gets decay postponed further
                resultTag.setDecayTimer((int) (TFC_Time.getTotalHours() + 6));
            }
        }
    }

}
