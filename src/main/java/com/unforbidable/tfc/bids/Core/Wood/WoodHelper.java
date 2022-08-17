package com.unforbidable.tfc.bids.Core.Wood;

import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Enums.EnumLogWallType;

import net.minecraft.block.Block;

public class WoodHelper {

    // 0 - "Oak","Aspen","Birch","Chestnut",
    // 4 - "Douglas Fir","Hickory","Maple","Ash",
    // 8 - "Pine","Sequoia","Spruce","Sycamore",
    // 12 - "White Cedar","White Elm","Willow","Kapok",
    // 16 - "Acacia","Palm","Ebony","Fever",
    // 20 - "Baobab","Limba","Mahogany","Teak",
    // 24 - "Bamboo","Gingko","Fruitwood","Mangrove",
    // 28 - "Ghaf","Mahoe","Laurel","Joshua",
    // 32 - "Yew"

    public static boolean canPeelLog(int meta) {
        return meta != 17 && meta != 24 && meta != 31;
    }

    public static boolean canBuildLogWall(int meta) {
        return canBuildLogWallFromPeeled(meta) || canBuildLogWallFromNormal(meta);
    }

    public static boolean canBuildLogWallFromPeeled(int meta) {
        return canPeelLog(meta);
    }

    public static boolean canBuildLogWallFromNormal(int meta) {
        return false;
    }

    public static Block getLogWallBlock(int offset, int side, boolean alt) {
        if (offset == 0) {
            switch (side) {
                case 0:
                case 1:
                    return alt ? BidsBlocks.logWallCornerAlt : BidsBlocks.logWallCorner;

                case 2:
                case 3:
                    return alt ? BidsBlocks.logWallNorthAlt : BidsBlocks.logWallNorth;

                case 4:
                case 5:
                default:
                    return alt ? BidsBlocks.logWallEastAlt : BidsBlocks.logWallEast;
            }
        } else if (offset == 16) {
            switch (side) {
                case 0:
                case 1:
                    return alt ? BidsBlocks.logWallCornerAlt2 : BidsBlocks.logWallCorner2;

                case 2:
                case 3:
                    return alt ? BidsBlocks.logWallNorthAlt2 : BidsBlocks.logWallNorth2;

                case 4:
                case 5:
                default:
                    return alt ? BidsBlocks.logWallEastAlt2 : BidsBlocks.logWallEast2;
            }
        } else /* if (offset == 32) */ {
            switch (side) {
                case 0:
                case 1:
                    return alt ? BidsBlocks.logWallCornerAlt3 : BidsBlocks.logWallCorner3;

                case 2:
                case 3:
                    return alt ? BidsBlocks.logWallNorthAlt3 : BidsBlocks.logWallNorth3;

                case 4:
                case 5:
                default:
                    return alt ? BidsBlocks.logWallEastAlt3 : BidsBlocks.logWallEast3;
            }
        }
    }

    public static String[] getWoodOffsetNames(int offset) {
        final int length = Math.min(16, Global.WOOD_ALL.length - offset);
        final String[] names = new String[length];
        for (int i = 0; i < length; i++) {
            names[i] = Global.WOOD_ALL[i + offset];
        }
        return names;
    }

    public static EnumLogWallType getDefaultLogWallType() {
        // The log wall type block to show in creative
        // Which one it is doesn't really matter
        // They all place the same block depending on circumstances
        return EnumLogWallType.EAST;
    }

    public static Block getDefaultLogWallBlock(int offset) {
        // This is the block that breaking any wall block drops
        // Others should only exist when placed in the world
        // This block should also be used as the input and output for crafting
        if (offset == 0) {
            return BidsBlocks.logWallEast;
        } else if (offset == 16) {
            return BidsBlocks.logWallEast2;
        } else /* if (offset == 32) */ {
            return BidsBlocks.logWallEast3;
        }
    }

    public static boolean canBarkMakeTannin(int meta) {
        switch (meta) {
            // Values are identical to making tannin from logs
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 9:
            case 16:
            case 19:
            case 21:
            case 22:
                return true;

            default:
                return false;
        }
    }

    public static boolean canBarkMakeFibers(int meta) {
        switch (meta) {
            // Popular trees with fibrous inner bark
            case 12:
            case 13:
            case 14:
                return true;

            default:
                return false;
        }
    }

    public static boolean canMakeFirewood(int meta) {
        return meta != 17 && meta != 24 && meta != 31;
    }

}
