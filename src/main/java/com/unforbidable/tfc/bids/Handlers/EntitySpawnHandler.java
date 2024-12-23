package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Entities.Mobs.EntityChickenTFC;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.api.Entities.IAnimal;
import com.unforbidable.tfc.bids.Core.Common.DataWatching.GoatDataWatcher;
import com.unforbidable.tfc.bids.Core.Drinks.Milk.GoatMilkHelper;
import com.unforbidable.tfc.bids.Entities.AI.EntityAIFindNestEx;
import com.unforbidable.tfc.bids.api.BidsOptions;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

public class EntitySpawnHandler {

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityChickenTFC) {
            ((EntityChickenTFC) event.entity).tasks.addTask(3, new EntityAIFindNestEx((EntityAnimal) event.entity, 1.2));
        }

        if (event.entity instanceof EntityGoat) {
            if (BidsOptions.Husbandry.enableIbexHavingMilk) {
                new GoatDataWatcher(event.entity).init();
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (BidsOptions.Husbandry.enableIbexHavingMilk) {
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
