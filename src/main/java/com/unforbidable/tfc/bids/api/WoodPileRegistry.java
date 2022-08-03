package com.unforbidable.tfc.bids.api;

import java.util.HashMap;
import java.util.Map;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

import net.minecraft.item.Item;

public class WoodPileRegistry {

    static final Map<Item, IWoodPileRenderProvider> entries = new HashMap<Item, IWoodPileRenderProvider>();

    public static void registerItem(Item item) {
        if (item instanceof IWoodPileRenderProvider) {
            entries.put(item, (IWoodPileRenderProvider) item);
        } else {
            Bids.LOG.warn(
                    "Item must implement IWoodPileIconProvider to be able to register with WoodPileRegistry, or provide class that implements it instead.");
        }
    }

    public static void registerItem(Item item, Class<? extends IWoodPileRenderProvider> iconProviderType) {
        IWoodPileRenderProvider iconProviderInstance;
        try {
            iconProviderInstance = iconProviderType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Default constructor for type " + iconProviderType.getCanonicalName()
                    + " is not available", e);
        }

        entries.put(item, iconProviderInstance);
    }

    public static IWoodPileRenderProvider findItem(Item item) {
        return entries.get(item);
    }

    public static Item[] getItems() {
        return entries.values().toArray(new Item[] {});
    }

}
