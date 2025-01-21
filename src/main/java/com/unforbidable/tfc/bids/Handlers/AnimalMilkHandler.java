package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Entities.Mobs.EntityCowTFC;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Core.Drinks.Milk.MilkHelper;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Events.AnimalMilkEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AnimalMilkHandler {

    @SubscribeEvent
    public void onAnimalMilkCheck(AnimalMilkEvent.Check event) {
        if (event.entity instanceof EntityCowTFC && MilkHelper.canEntityBeMilkedByPlayerSafe(event.entity, event.player)) {
            event.fluid = TFCFluids.MILK;
        }

        if (event.entity instanceof EntityGoat && MilkHelper.canEntityBeMilkedByPlayerSafe(event.entity, event.player)) {
            if (BidsOptions.Husbandry.enableGoatMilkFromGoats) {
                event.fluid = BidsFluids.GOATMILK;
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
