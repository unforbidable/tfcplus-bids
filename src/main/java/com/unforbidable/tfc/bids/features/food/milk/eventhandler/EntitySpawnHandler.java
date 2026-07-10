package com.unforbidable.tfc.bids.features.food.milk.eventhandler;

import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.api.Entities.IAnimal;
import com.unforbidable.tfc.bids.features.food.milk.MilkConfig;
import com.unforbidable.tfc.bids.util.datawatching.GoatDataWatcher;
import com.unforbidable.tfc.bids.features.food.milk.main.GoatMilkHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

public class EntitySpawnHandler {

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityGoat) {
            if (MilkConfig.enableIbexHavingMilk) {
                new GoatDataWatcher(event.entity).init();
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (MilkConfig.enableIbexHavingMilk) {
            if (event.entity instanceof EntityGoat) {
                EntityGoat goat = (EntityGoat) event.entity;
                if (!goat.isDomesticated() && goat.isAdult() && goat.getGender() == IAnimal.GenderEnum.FEMALE) {
                    if (!event.entity.worldObj.isRemote) {
                        // We use data watcher to send value that indicates whether an ibex can be milked
                        // because TFC only supports goats
                        boolean canMilk = GoatMilkHelper.isMilkable(goat);
                        new GoatDataWatcher(goat).setCanMilk(canMilk);
                    }
                }
            }
        }
    }

}
