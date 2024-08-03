package com.unforbidable.tfc.bids.Core.Wood;

import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Enums.EnumLogWallType;
import com.unforbidable.tfc.bids.api.Enums.EnumLogWallVertType;

import net.minecraft.block.Block;

public class WoodHelper {

    public static boolean canBuildLogWall(int meta) {
        return canBuildLogWallFromPeeled(meta) || canBuildLogWallFromNormal(meta);
    }

    public static boolean canBuildLogWallFromPeeled(int meta) {
        return WoodScheme.DEFAULT.findWood(meta).items.hasSeasonedPeeledLog();
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

    public static boolean canMakeFirewood(int meta) {
        return !WoodScheme.DEFAULT.findWood(meta).irregular;
    }

    public static boolean canMakeChoppingBlock(int meta) {
        return !WoodScheme.DEFAULT.findWood(meta).irregular;
    }

    public static EnumLogWallVertType getDefaultLogWallVertType() {
        return EnumLogWallVertType.DEFAULT;
    }

    public static Block getDefaultLogWallVertBlock(int offset) {
        if (offset == 0) {
            return BidsBlocks.logWallVert;
        } else if (offset == 16) {
            return BidsBlocks.logWallVert2;
        } else /* if (offset == 32) */ {
            return BidsBlocks.logWallVert3;
        }
    }

    public static Block getLogWallVertBlock(int offset, int side, boolean alt) {
        if (offset == 0) {
            return alt ? BidsBlocks.logWallVertAlt : BidsBlocks.logWallVert;
        } else if (offset == 16) {
            return alt ? BidsBlocks.logWallVertAlt2 : BidsBlocks.logWallVert2;
        } else /* if (offset == 32) */ {
            return alt ? BidsBlocks.logWallVertAlt3 : BidsBlocks.logWallVert3;
        }
    }

}
