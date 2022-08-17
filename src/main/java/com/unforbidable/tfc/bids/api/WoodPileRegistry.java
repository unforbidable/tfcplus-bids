package com.unforbidable.tfc.bids.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

import net.minecraft.item.Item;

public class WoodPileRegistry {

    static final Map<Item, IWoodPileRenderProvider> entries = new HashMap<Item, IWoodPileRenderProvider>();
    static final List<Item> seasonableItems = new ArrayList<Item>();
    static final List<Item> seasonedItems = new ArrayList<Item>();

    public static void registerSeasonableItem(Item seasonableItem) {
        seasonableItems.add(seasonableItem);

        registerItem(seasonableItem);
    }

    public static void registerSeasonedItem(Item seasonedItem) {
        seasonedItems.add(seasonedItem);

        registerItem(seasonedItem);
    }

    public static void registerItem(Item item) {
        if (item instanceof IWoodPileRenderProvider) {
            entries.put(item, (IWoodPileRenderProvider) item);
        } else {
            Bids.LOG.warn(
                    "Item must implement IWoodPileIconProvider to be able to register with WoodPileRegistry, or provide class that implements it instead.");
        }
    }

    public static void registerSeasonableItem(Item seasonableItem, Class<? extends IWoodPileRenderProvider> type) {
        seasonableItems.add(seasonableItem);

        registerItem(seasonableItem, type);
    }

    public static void registerSeasonedItem(Item seasonedItem, Class<? extends IWoodPileRenderProvider> type) {
        seasonedItems.add(seasonedItem);

        registerItem(seasonedItem, type);
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
        return entries.keySet().toArray(new Item[] {});
    }

    public static List<Item> getSeasonableItems() {
        return new ArrayList<Item>(seasonableItems);
    }

    public static List<Item> getSeasonedItems() {
        return new ArrayList<Item>(seasonedItems);
    }

}
