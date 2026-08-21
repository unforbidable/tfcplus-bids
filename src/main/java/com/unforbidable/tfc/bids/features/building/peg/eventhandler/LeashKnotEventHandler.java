package com.unforbidable.tfc.bids.features.building.peg.eventhandler;

import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.features.building.peg.entity.EntityPegLeashKnot;
import com.unforbidable.tfc.bids.util.LeashHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.living.LivingEvent;

public class LeashKnotEventHandler {

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityLiving) {
            LeashHelper.updateLashedState((EntityLiving)event.entityLiving, BidsBlocks.woodenPeg, EntityPegLeashKnot.class);
        }
    }

}
