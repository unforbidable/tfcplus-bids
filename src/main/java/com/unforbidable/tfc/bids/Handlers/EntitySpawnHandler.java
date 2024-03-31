package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Entities.Mobs.EntityChickenTFC;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Entities.AI.EntityAIFindNestEx;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EntitySpawnHandler {

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityChickenTFC) {
            ((EntityChickenTFC) event.entity).tasks.addTask(3, new EntityAIFindNestEx((EntityAnimal) event.entity, 1.2));
        }
    }

}
