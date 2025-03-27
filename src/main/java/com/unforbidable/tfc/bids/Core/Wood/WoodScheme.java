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

    public ItemStack getItemStack(WoodIndex wood, EnumWoodItemType type, int stackSize) {
        return getWoodItemStack(wood.index, type, stackSize);
    }

    protected int getWoodIndex(ItemStack is) {
        if (is.getItem() == TFCItems.logs || is.getItem() == BidsItems.logsSeasoned) {
            return is.getItemDamage() / 2;
        } else {
            return is.getItemDamage();
        }
    }

    protected ItemStack getWoodItemStack(int index, EnumWoodItemType type, int stackSize) {
        switch (type) {
            case LOG:
                return new ItemStack(TFCItems.logs, stackSize, index * 2);
            case CHOPPED_LOG:
                return new ItemStack(TFCItems.logs, stackSize, index * 2 + 1);
            case PEELED_LOG:
                return new ItemStack(BidsItems.peeledLog, stackSize, index);
            case FIREWOOD:
                return new ItemStack(BidsItems.firewood, stackSize, index);
            case SEASONED_LOG:
                return new ItemStack(BidsItems.logsSeasoned, stackSize, index * 2);
            case SEASONED_CHOPPED_LOG:
                return new ItemStack(BidsItems.logsSeasoned, stackSize, index * 2 + 1);
            case SEASONED_PEELED_LOG:
                return new ItemStack(BidsItems.peeledLogSeasoned, stackSize, index);
            case SEASONED_FIREWOOD:
                return new ItemStack(BidsItems.firewoodSeasoned, stackSize, index);
            case BARK:
                return new ItemStack(BidsItems.bark, stackSize, index);
            case LUMBER:
                return new ItemStack(TFCItems.singlePlank, stackSize, index);
        }

        return null;
    }

    public boolean hasWoodItemStack(WoodIndex wood, EnumWoodItemType type) {
        switch (type) {
            case LOG:
            case LUMBER:
                return true;

            case CHOPPED_LOG:
                return wood.hasLargeLogs;

            case FIREWOOD:
            case SEASONED_FIREWOOD:
                return !wood.inflammable;

            case SEASONED_CHOPPED_LOG:
                return wood.hasBark && wood.hasLargeLogs;

            case PEELED_LOG:
            case SEASONED_LOG:
            case SEASONED_PEELED_LOG:
            case BARK:
                return wood.hasBark;
        }

        return false;
    }

    public ItemStack getWoodBlockStack(WoodIndex wood, EnumWoodBlockType type, int stackSize) {
        switch (type) {
            case CHOPPING_BLOCK:
                return WoodBlockGroup.CHOPPING_BLOCK.getBlockStack(wood.index, stackSize);
            case PALISADE:
                return WoodBlockGroup.PALISADE.getBlockStack(wood.index, stackSize);
            case STACKED_FIREWOOD:
                return WoodBlockGroup.STACKED_FIREWOOD.getBlockStack(wood.index, stackSize);
            case LOG_WALL:
                return WoodBlockGroup.LOG_WALL.getBlockStack(wood.index, stackSize);
            case LOG_WALL_VERT:
                return WoodBlockGroup.LOG_WALL_VERT.getBlockStack(wood.index, stackSize);
            case WOOD_VERT:
                return WoodBlockGroup.WOOD_VERT.getBlockStack(wood.index, stackSize);
            case WOOD_SUPPORT:
                return WoodBlockGroup.WOOD_SUPPORT.getBlockStack(wood.index, stackSize);
            case FENCE:
                return WoodBlockGroup.FENCE.getBlockStack(wood.index, stackSize);
            case THICK_LOG:
                return WoodBlockGroup.THICK_LOG.getBlockStack(wood.index, stackSize);
            case THICK_LOG_ALT:
                return WoodBlockGroup.THICK_LOG_ALT.getBlockStack(wood.index, stackSize);
            case STACKED_LOGS:
                return WoodBlockGroup.STACKED_LOGS.getBlockStack(wood.index, stackSize);
            case STACKED_LOGS_ALT:
                return WoodBlockGroup.STACKED_LOGS_ALT.getBlockStack(wood.index, stackSize);
        }

        return null;
    }

    public boolean hasWoodBlockStack(WoodIndex wood, EnumWoodBlockType type) {
        switch (type) {
            case WOOD_VERT:
            case WOOD_SUPPORT:
            case FENCE:
            case LOG_WALL:
            case LOG_WALL_VERT:
            case PALISADE:
            case STACKED_LOGS:
            case STACKED_LOGS_ALT:
                return true;

            case CHOPPING_BLOCK:
            case THICK_LOG:
            case THICK_LOG_ALT:
                return wood.hasLargeLogs;

            case STACKED_FIREWOOD:
                return !wood.inflammable;
        }

        return false;
    }

    public WoodItemProvider getWoodItemProvider(WoodIndex wood) {
        return new WoodItemProvider(wood, this);
    }
    public WoodBlockProvider getWoodBlockProvider(WoodIndex wood) {
        return new WoodBlockProvider(wood, this);
    }

}
