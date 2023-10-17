package com.unforbidable.tfc.bids.Core.Drinks.Milk;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Entities.Mobs.EntityAnimalTFC;
import com.dunk.tfc.Entities.Mobs.EntityCowTFC;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.api.Entities.IAnimal;
import com.unforbidable.tfc.bids.Bids;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.Field;

public class MilkHelper {

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
            return canCowBeMilkedByPlayer(cow, player);
        } else if (entity instanceof EntityGoat) {
            EntityGoat goat = (EntityGoat) entity;
            return canGoatBeMilkedByPlayer(goat, player);
        } else {
            return false;
        }
    }

    private static boolean canGoatBeMilkedByPlayer(EntityGoat goat, EntityPlayer player) {
        Bids.LOG.debug("goat.isMilkable(): " + goat.isMilkable());
        Bids.LOG.debug("goat.isDomesticated(): " + goat.isDomesticated());
        return canAnimalBeMilkedByPlayer(goat, player) && goat.isMilkable() && goat.isDomesticated();
    }

    private static boolean canCowBeMilkedByPlayer(EntityCowTFC cow, EntityPlayer player) {
        Bids.LOG.debug("cow.isMilkable(): " + cow.isMilkable());
        return canAnimalBeMilkedByPlayer(cow, player) && cow.isMilkable();
    }

    private static boolean canAnimalBeMilkedByPlayer(IAnimal animal, EntityPlayer player) {
        Bids.LOG.debug("animal.isAdult(): " + animal.isAdult());
        Bids.LOG.debug("animal.getGender() == IAnimal.GenderEnum.FEMALE: " + (animal.getGender() == IAnimal.GenderEnum.FEMALE));
        Bids.LOG.debug("animal.checkFamiliarity(IAnimal.InteractionEnum.MILK, player): " + animal.checkFamiliarity(IAnimal.InteractionEnum.MILK, player));
        return animal.isAdult() && animal.getGender() == IAnimal.GenderEnum.FEMALE && animal.checkFamiliarity(IAnimal.InteractionEnum.MILK, player);
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
            long hasMilkTime = getHasMilkTime(amount, 24000);
            setCowHasMilkTime(cow, hasMilkTime);

            return true;
        } else if (entity instanceof EntityGoat) {
            EntityGoat goat = (EntityGoat) entity;
            long hasMilkTime = getHasMilkTime(amount, 24000 * 5);
            setGoatHasMilkTime(goat, hasMilkTime);

            return true;
        }

        return false;
    }

    private static void setGoatHasMilkTime(EntityGoat goat, long hasMilkTime) {
        // Goat does not have a public method for setting hasMilkTime
        try {
            Field hasMilkTimeField = EntityAnimalTFC.class.getDeclaredField("hasMilkTime");
            hasMilkTimeField.setAccessible(true);
            hasMilkTimeField.set(goat, hasMilkTime);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setCowHasMilkTime(EntityCowTFC cow, long hasMilkTime) {
        cow.setHasMilkTime(hasMilkTime);
    }

    private static long getHasMilkTime(int amount, long ticksPerBucket) {
        return TFC_Time.getTotalTicks() + Math.round((amount / 1000f) * ticksPerBucket);
    }

}
