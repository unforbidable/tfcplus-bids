package com.unforbidable.tfc.bids.features.food.milk.eventhandler;

import com.dunk.tfc.Entities.Mobs.EntityCowTFC;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.features.food.milk.MilkConfig;
import com.unforbidable.tfc.bids.features.food.milk.main.MilkHelper;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.features.milk.AnimalMilkEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AnimalMilkHandler {

    @SubscribeEvent
    public void onAnimalMilkCheck(AnimalMilkEvent.Check event) {
        if (event.entity instanceof EntityCowTFC && MilkHelper.canEntityBeMilkedByPlayerSafe(event.entity, event.player)) {
            event.fluid = TFCFluids.MILK;
        }

        if (event.entity instanceof EntityGoat && MilkHelper.canEntityBeMilkedByPlayerSafe(event.entity, event.player)) {
            if (MilkConfig.enableGoatMilkFromGoats) {
                event.fluid = BidsFluids.goatMilk;
            } else {
                event.fluid = TFCFluids.MILK;
            }
        }
    }

    @SubscribeEvent
    public void onAnimalMilkDrain(AnimalMilkEvent.Milking event) {
        if (event.entity instanceof EntityCowTFC || event.entity instanceof EntityGoat) {
            if (!MilkHelper.doMilkEntityByPlayerSafe(event.entity, event.player, event.amount)) {
                event.setCanceled(true);
            }
        }
    }

}
