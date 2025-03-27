package com.unforbidable.tfc.bids.Core.Wood;

import net.minecraft.item.ItemStack;

public class WoodBlockProvider {

    private final WoodIndex wood;
    private final WoodScheme scheme;

    public WoodBlockProvider(WoodIndex wood, WoodScheme scheme) {
        this.wood = wood;
        this.scheme = scheme;
    }

    protected boolean hasBlockStack(EnumWoodBlockType type) {
        return scheme.hasWoodBlockStack(wood, type);
    }

    protected ItemStack getBlockStack(EnumWoodBlockType type) {
        return getBlockStack(type, 1);
    }

    protected ItemStack getBlockStack(EnumWoodBlockType type, int stackSize) {
        return scheme.getWoodBlockStack(wood, type, stackSize);
    }

    public boolean hasChoppingBlock() {
        return hasBlockStack(EnumWoodBlockType.CHOPPING_BLOCK);
    }

    public ItemStack getChoppingBlock() {
        return getBlockStack(EnumWoodBlockType.CHOPPING_BLOCK);
    }

    public boolean hasLogWall() {
        return hasBlockStack(EnumWoodBlockType.LOG_WALL);
    }

    public ItemStack getLogWall() {
        return getBlockStack(EnumWoodBlockType.LOG_WALL);
    }

    public ItemStack getLogWallVert() {
        return getBlockStack(EnumWoodBlockType.LOG_WALL_VERT);
    }

    public boolean hasPalisade() {
        return hasBlockStack(EnumWoodBlockType.PALISADE);
    }

    public ItemStack getPalisade() {
        return getBlockStack(EnumWoodBlockType.PALISADE);
    }

    public ItemStack getPalisade(int stackSize) {
        return getBlockStack(EnumWoodBlockType.PALISADE, stackSize);
    }

    public ItemStack getWoodVert() {
        return getBlockStack(EnumWoodBlockType.WOOD_VERT);
    }

    public ItemStack getWoodSupport(int stackSize) {
        return getBlockStack(EnumWoodBlockType.WOOD_SUPPORT, stackSize);
    }

    public ItemStack getFence(int stackSize) {
        return getBlockStack(EnumWoodBlockType.FENCE, stackSize);
    }

    public boolean hasStackedFirewood() {
        return hasBlockStack(EnumWoodBlockType.STACKED_FIREWOOD);
    }

    public ItemStack getStackedFirewood() {
        return getBlockStack(EnumWoodBlockType.STACKED_FIREWOOD);
    }

    public boolean hasThickLog() {
        return hasBlockStack(EnumWoodBlockType.THICK_LOG);
    }

    public ItemStack getThickLog() {
        return getBlockStack(EnumWoodBlockType.THICK_LOG);
    }

    public ItemStack getThickLogAlt() {
        return getBlockStack(EnumWoodBlockType.THICK_LOG_ALT);
    }

    public boolean hasStackedLogs() {
        return hasBlockStack(EnumWoodBlockType.STACKED_LOGS);
    }

    public ItemStack getStackedLogs() {
        return getBlockStack(EnumWoodBlockType.STACKED_LOGS);
    }

    public ItemStack getStackedLogsAlt() {
        return getBlockStack(EnumWoodBlockType.STACKED_LOGS_ALT);
    }

}
