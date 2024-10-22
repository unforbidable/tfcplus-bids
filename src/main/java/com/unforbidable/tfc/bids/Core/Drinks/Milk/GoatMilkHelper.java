package com.unforbidable.tfc.bids.Core.Drinks.Milk;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Entities.Mobs.EntityAnimalTFC;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.api.Entities.IAnimal;
import com.unforbidable.tfc.bids.api.BidsOptions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

import java.lang.reflect.Field;

public class GoatMilkHelper {

    private static final int GOAT_CAN_MILK_DATA_WATCHER_ID = 25;

    public static void initCanMilkDataWatcherValue(EntityGoat goat) {
        goat.getDataWatcher().addObject(GOAT_CAN_MILK_DATA_WATCHER_ID, 0);
    }

    public static void setCanMilkDataWatcherValue(EntityGoat goat, boolean canMilk) {
        goat.getDataWatcher().updateObject(GOAT_CAN_MILK_DATA_WATCHER_ID, canMilk ? 1 : 0);
    }

    public static boolean getCanMilkDataWatcherValue(EntityGoat goat) {
        return goat.getDataWatcher().getWatchableObjectInt(GOAT_CAN_MILK_DATA_WATCHER_ID) == 1;
    }

    public static boolean isMilkable(EntityGoat goat) {
        // EntityGoat.isMilkable() checks familiarity which in turn relies on goat being domesticated
        // so this method only considers whether any milk is due
        return getHasMilkTime(goat) < TFC_Time.getTotalTicks();
    }

    public static boolean checkFamiliarity(EntityGoat animal, IAnimal.InteractionEnum interaction, EntityPlayer player) {
        // EntityGoat.checkFamiliarity() only allows milking domesticated goat
        // so this method checks familiarity for milking even for undomesticated goat
        boolean flag = false;
        switch (interaction) {
            case BREED:
                flag = animal.getFamiliarity() > 20 || animal.isDomesticated();
                break;
            case SHEAR:
                flag = animal.getFamiliarity() > 10 || animal.isDomesticated();
                break;
            case NAME:
                flag = animal.getFamiliarity() > 40 || animal.isDomesticated();
                break;
            case MILK:
                // 15 is used for milking cows
                flag = BidsOptions.Husbandry.enableIbexHavingMilk && animal.getFamiliarity() > 15 || animal.isDomesticated();
                break;
        }

        if (!flag && player != null && !player.worldObj.isRemote) {
            TFC_Core.sendInfoMessage(player, new ChatComponentTranslation("entity.notFamiliar"));
        }

        return flag;
    }

    public static long getHasMilkTime(EntityGoat goat) {
        // Goat does not have a public getter for reading hasMilkTime
        try {
            Field hasMilkTimeField = EntityAnimalTFC.class.getDeclaredField("hasMilkTime");
            hasMilkTimeField.setAccessible(true);
            return (Long)hasMilkTimeField.get(goat);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setHasMilkTime(EntityGoat goat, long hasMilkTime) {
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

}
