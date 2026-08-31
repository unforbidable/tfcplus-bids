package com.unforbidable.tfc.bids.features.material.antler.eventhandler;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Entities.Mobs.EntityDeer;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.meta.AntlerMeta;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class AntlerLivingDropsEventHandler {

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (event.entityLiving instanceof EntityDeer) {
            EntityDeer deer = (EntityDeer) event.entityLiving;
            if (deer.getSex() == 0) {
                float ageMod = TFC_Core.getPercentGrown(deer);
                if (ageMod >= 1) {
                    ItemStack antler = new ItemStack(BidsItems.antler, 2, AntlerMeta.DEER);
                    EntityItem entityItem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, antler);
                    event.drops.add(entityItem);
                }
            }
        }
    }

}
