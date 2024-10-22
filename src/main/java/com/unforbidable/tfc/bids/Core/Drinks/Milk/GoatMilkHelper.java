package com.unforbidable.tfc.bids.Core.Drinks.Milk;

import com.dunk.tfc.Entities.Mobs.EntityAnimalTFC;
import com.dunk.tfc.Entities.Mobs.EntityGoat;

import java.lang.reflect.Field;

public class GoatMilkHelper {

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
