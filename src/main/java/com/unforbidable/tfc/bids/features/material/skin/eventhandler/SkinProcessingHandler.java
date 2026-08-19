package com.unforbidable.tfc.bids.features.material.skin.eventhandler;

import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.features.processing.ProcessingEvent;
import com.unforbidable.tfc.bids.api.features.processing.ProcessingSurfaceEvent;
import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SkinProcessingHandler {

    @SubscribeEvent
    public void onProcessingSurfaceEffortCheck(ProcessingSurfaceEvent.EffortCheck event) {
        if (event.input.getItem() instanceof ItemSkin) {
            SkinTag tag = SkinTag.of(event.input);

            // 1 unit of effort per small skin
            event.newEffort *= (tag.getWeight() / 16);

            // 4 times easier dehairing of skins prepared in lime or lye fluid
            // note: this also reduces BidsStats.materialScraped value but let's just say the value tracks effort for now
            if (tag.isStage(SkinTagAccess.STAGE_PREPARED) && (tag.isFluid(TFCFluids.LIMEWATER.getName()) || tag.isFluid(BidsFluids.weakWoodAshLye.getName()))) {
                event.newEffort *= 0.25f;
            }
        }
    }

    @SubscribeEvent
    public void onProcessingItemCrafted(ProcessingEvent.ItemCrafted event) {
        if (event.input.getItem() instanceof ItemSkin && event.result.getItem() instanceof ItemSkin) {
            SkinTag input = SkinTag.of(event.input);
            SkinTag resultTag = SkinTag.of(event.result);

            if (input.hasAnimal()) {
                // Only set the animal if source skin has it
                // otherwise keep result animal if any
                // This is important for dehairing skins of specific animals into generic dehaired skins
                resultTag.setAnimal(input.getAnimal());
            }

            // Salted is removed with the scraped material
            resultTag.setSalted(false);

            resultTag.setFluid(input.getFluid());
            resultTag.setWeight(input.getWeight());
            resultTag.setDecay(input.getDecay());
            resultTag.setDecayTimer(input.getDecayTimer());
        }
    }

}
