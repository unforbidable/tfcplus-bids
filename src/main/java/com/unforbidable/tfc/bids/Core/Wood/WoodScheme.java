package com.unforbidable.tfc.bids.Core.Wood;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WoodScheme {

    public static final WoodScheme DEFAULT = new WoodScheme();

    private final Map<Integer, WoodIndex> woods = new HashMap<Integer, WoodIndex>();

    public Collection<WoodIndex> getWoods() {
        return woods.values();
    }

    public void registerWood(WoodIndex wood) {
        woods.put(wood.index, wood);
    }

    public WoodIndex findWood(int index) {
        return woods.get(index);
    }

    public WoodIndex findWood(ItemStack is) {
        return findWood(getWoodIndex(is));
    }

    public ItemStack getItemStack(WoodIndex wood, EnumWoodItemType type, boolean seasoned, int stackSize) {
        ItemStack itemStack = getWoodItemStack(wood.index, type, seasoned);
        if (stackSize != 1) {
            itemStack.stackSize = stackSize;
        }

        return itemStack;
    }

    protected int getWoodIndex(ItemStack is) {
        if (is.getItem() == TFCItems.logs || is.getItem() == BidsItems.logsSeasoned) {
            return is.getItemDamage() / 2;
        } else {
            return is.getItemDamage();
        }
    }

    protected ItemStack getWoodItemStack(int index, EnumWoodItemType type, boolean seasoned) {
        switch (type) {
            case LOG:
                return seasoned ? new ItemStack(BidsItems.logsSeasoned, 1, index * 2) : new ItemStack(TFCItems.logs, 1, index * 2);
            case CHOPPED_LOG:
                return seasoned ? new ItemStack(BidsItems.logsSeasoned, 1, index * 2 + 1) :new ItemStack(TFCItems.logs, 1, index * 2 + 1);
            case PEELED_LOG:
                return seasoned ? new ItemStack(BidsItems.peeledLogSeasoned, 1, index) : new ItemStack(BidsItems.peeledLog, 1, index);
            case FIREWOOD:
                return seasoned ? new ItemStack(BidsItems.firewoodSeasoned, 1, index) : new ItemStack(BidsItems.firewood, 1, index);
            case BARK:
                return new ItemStack(BidsItems.bark, 1, index);
        }

        return null;
    }

}
