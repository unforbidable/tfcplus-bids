package com.unforbidable.tfc.bids.Core.Drinks.Milk;

import com.dunk.tfc.Entities.Mobs.EntityCowTFC;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Drinks.Milk.Handlers.CowMilkHandler;
import com.unforbidable.tfc.bids.Core.Drinks.Milk.Handlers.GoatMilkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MilkHelper {

    private static final CowMilkHandler COW_MILK_HANDLER = new CowMilkHandler();
    private static final GoatMilkHandler GOAT_MILK_HANDLER = new GoatMilkHandler();

    public static boolean canEntityBeMilkedByPlayerSafe(Entity entity, EntityPlayer player) {
        try {
            return canEntityBeMilkedByPlayer(entity, player);
        } catch (Exception ex) {
            Bids.LOG.warn("Unable to determine if entity " + entity.getCommandSenderName() + " can be milked: " + ex.getMessage());
            Thread.dumpStack();

            return false;
        }
    }

    private static boolean canEntityBeMilkedByPlayer(Entity entity, EntityPlayer player) {
        if (entity instanceof EntityCowTFC) {
            EntityCowTFC cow = (EntityCowTFC) entity;
            return COW_MILK_HANDLER.canAnimalBeMilkedByPlayer(cow, player);
        } else if (entity instanceof EntityGoat) {
            EntityGoat goat = (EntityGoat) entity;
            return GOAT_MILK_HANDLER.canAnimalBeMilkedByPlayer(goat, player);
        } else {
            return false;
        }
    }

    public static boolean doMilkEntityByPlayerSafe(Entity entity, EntityPlayer player, int amount) {
        try {
            return doMilkEntityByPlayer(entity, player, amount);
        } catch (Exception ex) {
            Bids.LOG.warn("Unable to milk entity " + entity.getCommandSenderName() + ": " + ex.getMessage());
            Thread.dumpStack();

            return false;
        }
    }

    private static boolean doMilkEntityByPlayer(Entity entity, EntityPlayer player, int amount) {
        if (entity instanceof EntityCowTFC) {
            EntityCowTFC cow = (EntityCowTFC) entity;
            return COW_MILK_HANDLER.doMilkAnimalByPlayer(cow, player, amount);
        } else if (entity instanceof EntityGoat) {
            EntityGoat goat = (EntityGoat) entity;
            return GOAT_MILK_HANDLER.doMilkAnimalByPlayer(goat, player, amount);
        }

        return false;
    }

    public static boolean alwaysOverrideMilkingInteraction(EntityPlayer entityPlayer, Entity target) {
        // Always override default milking interaction when enabled
        // or for Ibex
        // or when a non TFC milking container is used
        return target instanceof EntityGoat && !((EntityGoat)target).isDomesticated() ||
            (entityPlayer.getHeldItem().getItem() != TFCItems.woodenBucketEmpty &&
                entityPlayer.getHeldItem().getItem() != TFCItems.clayBucketEmpty);
    }

}
