package com.unforbidable.tfc.bids.api;

import java.util.HashMap;
import java.util.Map;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import net.minecraft.item.Item;

public class FirepitRegistry {

    static Map<Item, IFirepitFuelMaterial> fuels = new HashMap<Item, IFirepitFuelMaterial>();

    public static void registerFuel(Item item) {
        if (item instanceof IFirepitFuelMaterial) {
            fuels.put(item, (IFirepitFuelMaterial) item);
        } else {
            Bids.LOG.warn(
                    "Item must implement IFirepitFuelMaterial to be able to register with FirepitFuelRegistry"
                            + " or provide class that implements it instead.");
        }
    }

    public static void registerFuel(Item item, Class<? extends IFirepitFuelMaterial> implementingType) {
        IFirepitFuelMaterial impl;
        try {
            impl = implementingType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Default constructor for type " + implementingType.getCanonicalName()
                    + " is not available", e);
        }

        fuels.put(item, impl);
    }

    public static IFirepitFuelMaterial findFuel(Item item) {
        return fuels.get(item);
    }

    public static Item[] getFuels() {
        return fuels.keySet().toArray(new Item[] {});
    }

}
