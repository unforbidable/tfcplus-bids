package com.unforbidable.tfc.bids.Core.Wood;

import net.minecraft.item.ItemStack;

public class WoodItemProvider {

    private final WoodIndex wood;
    private final WoodScheme scheme;

    public WoodItemProvider(WoodIndex wood, WoodScheme scheme) {
        this.wood = wood;
        this.scheme = scheme;
    }

    protected boolean hasItemStack(EnumWoodItemType type) {
        return scheme.hasItemStack(wood, type);
    }

    protected ItemStack getItemStack(EnumWoodItemType type) {
        return getItemStack(type, 1);
    }

    protected ItemStack getItemStack(EnumWoodItemType type, int stackSize) {
        return scheme.getItemStack(wood, type, stackSize);
    }

    public boolean hasLog() {
        return hasItemStack(EnumWoodItemType.LOG);
    }

    public ItemStack getLog() {
        return getItemStack(EnumWoodItemType.LOG);
    }

    public ItemStack getLog(int stackSize) {
        return getItemStack(EnumWoodItemType.LOG, stackSize);
    }

    public boolean hasChoppedLog() {
        return hasItemStack(EnumWoodItemType.CHOPPED_LOG);
    }

    public ItemStack getChoppedLog() {
        return getItemStack(EnumWoodItemType.CHOPPED_LOG);
    }

    public boolean hasSeasonedLog() {
        return hasItemStack(EnumWoodItemType.SEASONED_LOG);
    }

    public ItemStack getSeasonedLog() {
        return getItemStack(EnumWoodItemType.SEASONED_LOG);
    }

    public boolean hasSeasonedChoppedLog() {
        return hasItemStack(EnumWoodItemType.SEASONED_CHOPPED_LOG);
    }

    public ItemStack getSeasonedChoppedLog() {
        return getItemStack(EnumWoodItemType.SEASONED_CHOPPED_LOG);
    }

    public boolean hasPeeledLog() {
        return hasItemStack(EnumWoodItemType.PEELED_LOG);
    }

    public ItemStack getPeeledLog() {
        return getItemStack(EnumWoodItemType.PEELED_LOG);
    }

    public boolean hasSeasonedPeeledLog() {
        return hasItemStack(EnumWoodItemType.SEASONED_PEELED_LOG);
    }

    public ItemStack getSeasonedPeeledLog() {
        return getItemStack(EnumWoodItemType.SEASONED_PEELED_LOG);
    }

    public ItemStack getSeasonedPeeledLog(int stackSize) {
        return getItemStack(EnumWoodItemType.SEASONED_PEELED_LOG, stackSize);
    }

    public boolean hasFirewood() {
        return hasItemStack(EnumWoodItemType.FIREWOOD);
    }

    public ItemStack getFirewood() {
        return getItemStack(EnumWoodItemType.FIREWOOD);
    }

    public boolean hasSeasonedFirewood() {
        return hasItemStack(EnumWoodItemType.SEASONED_FIREWOOD);
    }

    public ItemStack getSeasonedFirewood() {
        return getItemStack(EnumWoodItemType.SEASONED_FIREWOOD);
    }

    public boolean hasBark() {
        return hasItemStack(EnumWoodItemType.BARK);
    }

    public ItemStack getBark() {
        return getItemStack(EnumWoodItemType.BARK);
    }

    public boolean hasLumber() {
        return hasItemStack(EnumWoodItemType.LUMBER);
    }

    public ItemStack getLumber() {
        return getItemStack(EnumWoodItemType.LUMBER);
    }

    public ItemStack getLumber(int stackSize) {
        return getItemStack(EnumWoodItemType.LUMBER, stackSize);
    }

}
