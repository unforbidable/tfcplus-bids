package com.unforbidable.tfc.bids.Core.WoodPile.FireSetting;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Blocks.BlockCrackedSed;
import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ICrackableBlock;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

public class StoneCracker {

    // Base heat requirement for the blocks directly touching the wood pile (distance = 1 block)
    static final int BLOCK_CRACKING_HEAT_BASE = 1500;

    // Additional hear requirement for blocks not directly touching the wood pile per block distance squared (distance > 1 block)
    static final int BLOCK_CRACKING_HEAT_PER_DISTANCE_SQUARED = 4000;

    // Cap on propagation distance
    static final int MAX_PROPAGATION_DISTANCE = 7;

    public static List<BlockCoord> findNearbyStoneToCrack(World world, int x, int y, int z, int heat) {
        List<BlockCoord> blocksFound = new ArrayList<BlockCoord>();

        if (heat >= BLOCK_CRACKING_HEAT_BASE) {
            List<BlockCoord> blocksChecked = new ArrayList<BlockCoord>();
            Queue<BlockCoord> blocksToCheck = new ArrayDeque<BlockCoord>();

            // Exclude the log pile from search
            BlockCoord origin = new BlockCoord(x, y, z);
            blocksChecked.add(origin);

            findNeighborsToCrack(world, x, y, z, origin, heat, blocksChecked, blocksFound, blocksToCheck);

            while (!blocksToCheck.isEmpty()) {
                BlockCoord bc = blocksToCheck.poll();
                findNeighborsToCrack(world, bc.x, bc.y, bc.z, origin, heat, blocksChecked, blocksFound, blocksToCheck);
            }
        }

        return blocksFound;
    }

    private static void findNeighborsToCrack(World world, int x, int y, int z, BlockCoord origin, int heat, List<BlockCoord> blocksChecked, List<BlockCoord> blocksFound, Queue<BlockCoord> blocksToCheck) {
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            BlockCoord bc = new BlockCoord(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
            if (!blocksChecked.contains(bc)) {
                blocksChecked.add(bc);

                double distance = origin.distanceTo(bc);
                int heatRequiredForBlockDistance = BLOCK_CRACKING_HEAT_BASE + (int) Math.round(BLOCK_CRACKING_HEAT_PER_DISTANCE_SQUARED * (distance * distance - 1));

                if (canBlockCrack(world, bc.x, bc.y, bc.z)) {
                    float resistance = getBlockHeatResistance(world, bc.x, bc.y, bc.z);

                    if (heat >= heatRequiredForBlockDistance * resistance) {
                        blocksFound.add(bc);
                    }
                }

                if (heat >= heatRequiredForBlockDistance && distance < MAX_PROPAGATION_DISTANCE - 1) {
                    if (canBlockPropagateHeat(world, bc.x, bc.y, bc.z)) {
                        blocksToCheck.add(bc);
                    }
                }
            }
        }
    }

    public static ICrackableBlock getCrackableBlock(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);

        for (ICrackableBlock crackable : WoodPileRegistry.getCrackableBlocks()) {
            if (crackable.getSource() == block && crackable.isCrackable(world, x, y, z)) {
                return crackable;
            }
        }

        return null;
    }

    public static ICrackableBlock getCrackableBlock(Block block, int metadata) {
        for (ICrackableBlock crackable : WoodPileRegistry.getCrackableBlocks()) {
            if (crackable.getSource() == block && crackable.isCrackable(block, metadata)) {
                return crackable;
            }
        }

        return null;
    }

    public static boolean canBlockCrack(World world, int x, int y, int z) {
        return getCrackableBlock(world, x, y, z) != null;
    }

    public static boolean canBlockCrack(Block block, int metadata) {
        return getCrackableBlock(block, metadata) != null;
    }

    public static float getBlockHeatResistance(World world, int x, int y, int z) {
        ICrackableBlock crackable = getCrackableBlock(world, x, y, z);
        if (crackable != null) {
            return crackable.getHeatResistance(world, x, y, z);
        }

        return 1f;
    }

    public static float getBlockHeatResistance(Block block, int metadata) {
        ICrackableBlock crackable = getCrackableBlock(block, metadata);
        if (crackable != null) {
            return crackable.getHeatResistance(block, metadata);
        }

        return 1f;
    }

    public static boolean canBlockPropagateHeat(World world, int x, int y, int z) {
        // Air can propagate
        if (world.isAirBlock(x, y, z)) {
            return true;
        }

        // And any potentially crackable or already cracked block
        // Although we are not checking individual crackable block logic though
        Block block = world.getBlock(x, y, z);
        for (ICrackableBlock crackable : WoodPileRegistry.getCrackableBlocks()) {
            if (crackable.getSource() == block || crackable.getTarget() == block) {
                return true;
            }
        }

        return false;
    }

    public static void replaceStoneWithCracked(World world, int x, int y, int z) {
        ICrackableBlock crackable = getCrackableBlock(world, x, y, z);
        if (crackable != null) {
            crackable.crackBlock(world, x, y, z);
        } else {
            Bids.LOG.warn("Block not cracked! Expected crackable block at: " + world.getBlock(x, y, z));
        }
    }

    @SideOnly(Side.CLIENT)
    public static IIcon getCrackedStoneIcon(IBlockAccess world, int x, int y, int z) {
        Random rand = new Random(Minecraft.getMinecraft().theWorld.getSeed() + (x * x * 4987142L) + (x * 5947611L) + (z * z * 4392871L + (z * 389711L) ^ y));
        int stage = rand.nextInt(3) + 1;
        return ((BlockCrackedSed) BidsBlocks.crackedStoneSed).getDestroyStageIcon(stage);
    }

}
