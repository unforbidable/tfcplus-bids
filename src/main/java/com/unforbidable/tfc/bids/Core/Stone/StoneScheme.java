package com.unforbidable.tfc.bids.Core.Stone;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsItems;
import net.minecraft.item.ItemStack;

import java.util.*;

public class StoneScheme {

    public static final StoneScheme DEFAULT = new StoneScheme();

    private final Map<Integer, StoneIndex> stones = new HashMap<Integer, StoneIndex>();

    public Collection<StoneIndex> getStones() {
        return stones.values();
    }

    public void registerStone(StoneIndex stone) {
        stones.put(stone.index, stone);
    }

    public boolean hasStoneItemStack(StoneIndex stone, EnumStoneItemType type) {
        switch (type) {
            case ROCK:
            case STONE_BRICK:
            case ROUGH_STONE_TILE:
            case ROUGH_STONE_BRICK:
            case MUD_BRICK:
            case FLAT_ROCK:
            case ADZE_HEAD:
            case DRILL_HEAD:
            case HAND_AXE:
                return true;

            case SHINGLE:
                return stone.hasShingle;
        }

        return false;
    }

    public ItemStack getStoneItemStack(StoneIndex stone, EnumStoneItemType type, int stackSize) {
        switch (type) {
            case ROCK:
                return new ItemStack(TFCItems.looseRock, stackSize, stone.index);

            case STONE_BRICK:
                return new ItemStack(TFCItems.stoneBrick, stackSize, stone.index);

            case SHINGLE:
                return new ItemStack(TFCItems.slateShingle, stackSize);

            case ROUGH_STONE_BRICK:
                return new ItemStack(BidsItems.roughStoneBrick, stackSize, stone.index);

            case ROUGH_STONE_TILE:
                return new ItemStack(BidsItems.roughStoneTile, stackSize, stone.index);

            case MUD_BRICK:
                return new ItemStack(TFCItems.mudBrick, stackSize, stone.index);

            case FLAT_ROCK:
                return new ItemStack(TFCItems.flatRock, stackSize, stone.index);

            case ADZE_HEAD:
                return StoneToolHeadGroup.ADZE_HEAD.getToolHead(stone.index, stackSize);

            case DRILL_HEAD:
                return StoneToolHeadGroup.DRILL_HEAD.getToolHead(stone.index, stackSize);

            case HAND_AXE:
                return StoneToolHeadGroup.HAND_AXE.getToolHead(stone.index, stackSize);
        }

        Bids.LOG.warn("Unhandled getStoneItemStack for type {}", type);

        return null;
    }

    public boolean hasStoneBlockStack(StoneIndex stone, EnumStoneBlockType type) {
        switch (type) {
            case RAW:
            case SMOOTH_STONE:
            case ROUGH_STONE:
            case ROUGH_STONE_BRICKS:
            case ROUGH_STONE_TILES:
            case STONE_BRICKS:
            case STONE_BRICK_CHIMNEY:
            case STONE_LARGE_BRICKS:
            case STONE_LARGE_BRICK_CHIMNEY:
            case MUD_BRICKS:
            case MUD_BRICK_CHIMNEY:
            case ROUGH_BRICK_FENCE:
            case ROUGH_TILE_FENCE:
                return true;

            case SADDLE_QUERN:
            case HAND_STONE:
            case PRESSING_STONE:
            case WEIGHT_STONE:
                return stone.soft;
        }

        return false;
    }

    public ItemStack getStoneBlockStack(StoneIndex stone, EnumStoneBlockType type, int stackSize) {
        switch (type) {
            case RAW:
                return StoneBlockGroup.RAW_STONE.getBlockStack(stone.index, stackSize);
            case SMOOTH_STONE:
                return StoneBlockGroup.SMOOTH_STONE.getBlockStack(stone.index, stackSize);
            case ROUGH_STONE:
                return StoneBlockGroup.ROUGH_STONE.getBlockStack(stone.index, stackSize);
            case ROUGH_STONE_BRICKS:
                return StoneBlockGroup.ROUGH_STONE_BRICK.getBlockStack(stone.index, stackSize);
            case ROUGH_STONE_TILES:
                return StoneBlockGroup.ROUGH_STONE_TILE.getBlockStack(stone.index, stackSize);
            case SADDLE_QUERN:
                return StoneBlockGroup.SADDLE_QUERN.getBlockStack(stone.index, stackSize);
            case HAND_STONE:
                return StoneBlockGroup.HAND_STONE.getBlockStack(stone.index, stackSize);
            case PRESSING_STONE:
                return StoneBlockGroup.PRESSING_STONE.getBlockStack(stone.index, stackSize);
            case WEIGHT_STONE:
                return StoneBlockGroup.WEIGHT_STONE.getBlockStack(stone.index, stackSize);
            case STONE_BRICKS:
                return StoneBlockGroup.STONE_BRICKS.getBlockStack(stone.index, stackSize);
            case STONE_BRICK_CHIMNEY:
                return StoneBlockGroup.STONE_BRICK_CHIMNEY.getBlockStack(stone.index, stackSize);
            case STONE_LARGE_BRICKS:
                return StoneBlockGroup.STONE_LARGE_BRICKS.getBlockStack(stone.index, stackSize);
            case STONE_LARGE_BRICK_CHIMNEY:
                return StoneBlockGroup.STONE_LARGE_BRICK_CHIMNEY.getBlockStack(stone.index, stackSize);
            case MUD_BRICKS:
                return StoneBlockGroup.MUD_BRICK.getBlockStack(stone.index, stackSize);
            case MUD_BRICK_CHIMNEY:
                return StoneBlockGroup.MUD_BRICK_CHIMNEY.getBlockStack(stone.index, stackSize);
            case ROUGH_BRICK_FENCE:
                return StoneBlockGroup.ROUGH_BRICK_FENCE.getBlockStack(stone.index, stackSize);
            case ROUGH_TILE_FENCE:
                return StoneBlockGroup.ROUGH_TILE_FENCE.getBlockStack(stone.index, stackSize);
        }

        Bids.LOG.warn("Unhandled getStoneBlockStack for type {}", type);

        return null;
    }

    public StoneItemProvider getStoneItemProvider(StoneIndex index) {
        return new StoneItemProvider(index, this);
    }

    public StoneBlockProvider getStoneBlockProvider(StoneIndex index) {
        return new StoneBlockProvider(index, this);
    }

}
