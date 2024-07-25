package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnChamber;

import java.util.ArrayList;
import java.util.List;

public class KilnRegistry {

    private static final List<Class<?>> kilnChambers = new ArrayList<Class<?>>();

    public static void registerKilnChamber(Class<?> kilnChamberClass) {
        if (IKilnChamber.class.isAssignableFrom(kilnChamberClass)) {
            kilnChambers.add(kilnChamberClass);
        } else {
            Bids.LOG.warn("Kiln chamber class {} must implement the IKilnChamber interface.", kilnChamberClass);
        }
    }

    public static List<Class<?>> getKilnChambers() {
        return kilnChambers;
    }

}
