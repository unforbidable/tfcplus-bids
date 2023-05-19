package com.unforbidable.tfc.bids.TileEntities;

import codechicken.lib.vec.BlockCoord;
import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

public class TileEntityAquifer extends TileEntity {

    public static final ForgeDirection[] YAXIS_DIRS = new ForgeDirection[]{ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.WEST};
    private static final int MAX_WATER_COLUMN_RISE = 4;
    private static final int WATER_COLUMN_RISE_UPDATE_DELAY = 100;
    private static final int UPDATE_AQUIFER_NEIGHBORS_DELAY = 200;

    private static final long WATER_COLUMN_RISE_BASE_TICKS = TFC_Time.HOUR_LENGTH * 2;
    private static final float WATER_COLUMN_RISE_LEVEL_PENALTY = 2f;
    private static final float WATER_COLUMN_RISE_NEIGHBOR_BONUS = 0.25f;
    private static final float WATER_COLUMN_RISE_EXPOSED_NEIGHBOR_PENALTY = 1f;
    private static final float WATER_COLUMN_RISE_RAINFALL_BONUS = 1 / 400f;

    boolean isExposed = false;
    int neighborExposedCount = 0;
    int neighborTotalCount = 0;
    int lastKnownWaterColumnRise = 0;
    long lastWaterColumnRiseTime = 0;
    boolean updateAquiferNeighborsRequired = false;

    Timer waterColumnRiseUpdateTimer = new Timer(WATER_COLUMN_RISE_UPDATE_DELAY);
    Timer updateNeighborsTimer = new Timer(0);

    public int getNeighborExposedCount() {
        return neighborExposedCount;
    }

    public int getNeighborTotalCount() {
        return neighborTotalCount;
    }

    public void checkBeingExposed() {
        Bids.LOG.debug(worldObj.getBlock(xCoord, yCoord, zCoord));

        boolean isExposedNow = TileEntityAquifer.isAquiferBlockExposedAt(worldObj, xCoord, yCoord, zCoord);
        if (isExposedNow && !isExposed) {
            isExposed = true;
            onAquiferExposed();
        } else if (!isExposedNow && isExposed) {
            isExposed = false;
            onAquiferCovered();
        } else if (isExposed) {
            // Something changed other than exposure
            // Such as destroying a neighbor aquifer block
            // or adding one in creative
            // or a regular world tick update if those are enabled
            // however this is only ever interesting when exposed
            updateAquiferNeighborsDelayed();
        }
    }

    private void updateAquiferNeighborsDelayed() {
        Bids.LOG.debug("Aquifer needs delayed update at " + xCoord + "," + zCoord);

        updateAquiferNeighborsRequired = true;
        updateNeighborsTimer.delay(UPDATE_AQUIFER_NEIGHBORS_DELAY);
    }

    private void updateAquiferNeighborsImmediate() {
        Bids.LOG.debug("Aquifer needs immediate update at " + xCoord + "," + zCoord);

        updateAquiferNeighborsRequired = true;
        updateAquiferNeighbors();
    }

    private void onAquiferCovered() {
        Bids.LOG.debug("Aquifer covered at " + xCoord + "," + zCoord);

        updateAquiferNeighborsImmediate();
        clearWaterColumn();
    }

    private void onAquiferExposed() {
        Bids.LOG.debug("Aquifer exposed at " + xCoord + "," + zCoord);

        lastWaterColumnRiseTime = TFC_Time.getTotalTicks();
        lastKnownWaterColumnRise = 0;

        updateAquiferNeighborsImmediate();

        waterColumnRiseUpdateTimer.reset();
    }

    public void onBlockDestroyed() {
        Bids.LOG.debug("Aquifer destroyed at " + xCoord + "," + zCoord);

        updateAquiferNeighborsImmediate();
        clearWaterColumn();
    }

    private void updateAquiferNeighbors() {
        if (updateAquiferNeighborsRequired) {
            Bids.LOG.debug("Aquifer updates neighbors at " + xCoord + "," + zCoord);

            // Find all neighbors
            Set<BlockCoord> foundNeighbors = new HashSet<BlockCoord>();
            foundNeighbors.add(new BlockCoord(this));
            findAquiferNeighborsAt(worldObj, xCoord, yCoord, zCoord, foundNeighbors);

            // Determine the "push" depending on how many are exposed and covered
            int totalCount = foundNeighbors.size();
            int exposedCount = 0;

            List<TileEntityAquifer> teNeighbors = new ArrayList<TileEntityAquifer>(foundNeighbors.size());
            for (BlockCoord bc : foundNeighbors) {
                TileEntityAquifer te = (TileEntityAquifer) worldObj.getTileEntity(bc.x, bc.y, bc.z);
                teNeighbors.add(te);
                if (te.isExposed) {
                    exposedCount++;
                }
            }

            Bids.LOG.debug("Aquifer found neighbors at " + xCoord + "," + zCoord + " total " + totalCount + " exposed " + exposedCount);

            // Update water columns of ours and of all neighbors
            for (TileEntityAquifer te : teNeighbors) {
                te.updateWaterColumnTargetRise(totalCount, exposedCount);
            }
        }
    }

    private void updateWaterColumnTargetRise(int total, int exposed) {
        if (isExposed) {
            waterColumnRiseUpdateTimer.reset();
            neighborTotalCount = total;
            neighborExposedCount = exposed;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            Bids.LOG.debug("Aquifer neighbor count total " + total + " and exposed " + exposed + " at " + xCoord + "," + zCoord);
        }

        updateAquiferNeighborsRequired = false;
    }

    private void clearWaterColumn() {
        for (int i = 1; i <= MAX_WATER_COLUMN_RISE; i++) {
            clearWaterBlockAt(worldObj, xCoord, yCoord + i, zCoord);
        }
    }

    private void updateWaterColumn() {
        Bids.LOG.debug("Aquifer water column update at " + xCoord + "," + zCoord);

        // Reduce target rise for too many exposed aquifers to prevent source blocks from refilling each other
        // Later we can add config to allow this kind of exploit
        int targetRise = neighborExposedCount > 2 ? 1 : Math.max(1, Math.min(MAX_WATER_COLUMN_RISE, 2 + (neighborTotalCount / 2) - neighborExposedCount));

        int currentRise = 0;
        for (int i = 1; i <= targetRise; i++) {
            // Check both that water is there
            // and also that it can be there - in case any well structure is broken
            if (isFullWaterBlockAt(worldObj, xCoord, yCoord + i, zCoord)) {
                if (canPlaceWaterBlockAt(worldObj, xCoord, yCoord + i, zCoord, i)) {
                    currentRise++;
                } else {
                    Bids.LOG.debug("Aquifer water column cannot stay at " + xCoord + "," + zCoord + " to " + yCoord + i);
                    break;
                }
            } else {
                break;
            }
        }

        // If there is less water than last time we checked
        // that usually means some was taken by a player
        // In that case we just want to reset the time for water rising at that moment
        // or the water might just fill back up instantly
        // and start filing next update when the required time passes
        if (currentRise < lastKnownWaterColumnRise) {
            lastWaterColumnRiseTime = TFC_Time.getTotalTicks();
        } else {
            // We save the current last time to be able to refill water instantly
            // on chunks loading and so on if enough time had already passed
            long currentWaterColumnRiseTime = lastWaterColumnRiseTime;
            if (currentRise < targetRise) {
                Bids.LOG.debug("Aquifer water column update rise " + currentRise + "/" + targetRise + " at " + xCoord + "," + zCoord);

                for (int i = 1; i <= MAX_WATER_COLUMN_RISE; i++) {
                    if (i <= targetRise && i > currentRise) {
                        Bids.LOG.debug("Aquifer water column rise needed at " + xCoord + "," + zCoord);
                        if (canPlaceWaterBlockAt(worldObj, xCoord, yCoord + i, zCoord, i)) {
                            long timeRequired = getTimeRequiredToRaiseWaterColumnToLevel(i);
                            if (timeRequired <= TFC_Time.getTotalTicks() - currentWaterColumnRiseTime) {
                                Bids.LOG.debug("Aquifer water column rise done at " + xCoord + "," + zCoord);
                                currentRise++;

                                placeWaterBlockAt(worldObj, xCoord, yCoord + i, zCoord);
                                currentWaterColumnRiseTime += timeRequired;
                                lastWaterColumnRiseTime = TFC_Time.getTotalTicks();
                            } else {
                                Bids.LOG.debug("Aquifer water column rise at " + xCoord + "," + zCoord + " requires " +
                                    (TFC_Time.getTotalTicks() - currentWaterColumnRiseTime) + "/" + timeRequired + " ticks");
                                lastWaterColumnRiseTime = currentWaterColumnRiseTime;
                                break;
                            }
                        } else {
                            Bids.LOG.debug("Aquifer water column cannot rise at " + xCoord + "," + zCoord + " to " + yCoord + i);
                            lastWaterColumnRiseTime = TFC_Time.getTotalTicks();
                            break;
                        }
                    }
                }
            }
        }

        lastKnownWaterColumnRise = currentRise;

        // Make sure no rogue water blocks are above the current rise
        for (int i = 1; i <= MAX_WATER_COLUMN_RISE; i++) {
            if (i > currentRise && isFullWaterBlockAt(worldObj, xCoord, yCoord + i, zCoord)) {
                clearWaterBlockAtWithNeighbors(worldObj, xCoord, yCoord + i, zCoord);

                Bids.LOG.debug("Aquifer water column rogue water block at level " + i + " removed at " + xCoord + "," + zCoord + " having current rise " + currentRise);
            }
        }
    }

    private long getTimeRequiredToRaiseWaterColumnToLevel(int level) {
        float rainfall = TFC_Climate.getRainfall(worldObj, xCoord, Global.SEALEVEL, zCoord);

        float base = WATER_COLUMN_RISE_BASE_TICKS;
        float levelPenalty = WATER_COLUMN_RISE_LEVEL_PENALTY * (level - 1) + 1f;
        float exposedNeighborPenalty = Math.min(WATER_COLUMN_RISE_EXPOSED_NEIGHBOR_PENALTY * (neighborExposedCount - 1) + 1f, 2f);
        float neighborBonus = Math.min(WATER_COLUMN_RISE_NEIGHBOR_BONUS * neighborTotalCount + 1f, 3f);
        float rainfallBonus = Math.min(WATER_COLUMN_RISE_RAINFALL_BONUS * rainfall + 1f, 6f);

        Bids.LOG.debug("Aquifer water column raise time at " + xCoord + "," + zCoord +
            " base: " + base +
            " level penalty: " + levelPenalty +
            " exposed neighbor penalty: " + exposedNeighborPenalty +
            " neighbor bonus: " + neighborBonus +
            " rainfall bonus: " + rainfallBonus);

        return Math.round(base * levelPenalty * exposedNeighborPenalty / neighborBonus / rainfallBonus);
    }

    private static void findAquiferNeighborsAt(World world, int x, int y, int z, Set<BlockCoord> found) {
        for (ForgeDirection dir : YAXIS_DIRS) {
            int x2 = x + dir.offsetX;
            int z2 = z + dir.offsetZ;
            if (isAquiferBlockAt(world, x2, y, z2)) {
                BlockCoord bc = new BlockCoord(x2, y, z2);
                if (found.add(bc)) {
                    findAquiferNeighborsAt(world, x2, y, z2, found);
                }
            }
        }
    }

    private static boolean isAquiferBlockAt(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return block == BidsBlocks.aquifer || block == BidsBlocks.aquifer2;
    }

    public static boolean isAquiferBlockExposedAt(World world, int x, int y, int z) {
        return world.isAirBlock(x, y + 1, z)
            || TFC_Core.isFreshWater(world.getBlock(x, y + 1, z));
    }

    public static boolean isFullWaterBlockAt(World world, int x, int y, int z) {
        return TFC_Core.isFreshWater(world.getBlock(x, y, z)) && world.getBlockMetadata(x, y, z) == 0;
    }

    public static boolean isPartialWaterBlockAt(World world, int x, int y, int z) {
        return TFC_Core.isFreshWater(world.getBlock(x, y, z)) && world.getBlockMetadata(x, y, z) != 0;
    }

    private static boolean canPlaceWaterBlockAt(World world, int x, int y, int z, int level) {
        Queue<BlockCoord> blocksToCheck = new ArrayDeque<BlockCoord>();
        Set<BlockCoord> blocksChecked = new HashSet<BlockCoord>();
        blocksToCheck.add(new BlockCoord(x, y, z));

        while (blocksToCheck.size() > 0) {
            BlockCoord b = blocksToCheck.poll();

            blocksChecked.add(b);

            for (ForgeDirection dir : YAXIS_DIRS) {
                int x2 = b.x + dir.offsetX;
                int z2 = b.z + dir.offsetZ;
                BlockCoord b2 = new BlockCoord(x2, y, z2);

                if (!blocksChecked.contains(b2)) {
                    if (!isValidWellSideBlockAt(world, x2, y, z2, dir.getOpposite(), level)) {
                        if (!isExposedAquiferBlockAtOrBelow(world, x2, y - 1, z2)) {
                            return false;
                        } else {
                            blocksToCheck.add(b2);
                        }
                    }
                }
            }
        }

        Bids.LOG.debug("Aquifer checked total of " + blocksChecked.size() + " blocks at " + x + "," + y);

        return true;
    }

    private static boolean isValidWellSideBlockAt(World world, int x, int y, int z, ForgeDirection dir, int level) {
        return world.isSideSolid(x, y, z, dir.getOpposite()) &&
            isValidWellSideBlock(world.getBlock(x, y, z), level);
    }

    private static boolean isValidWellSideBlock(Block block, int level) {
        return level == 1 && TFC_Core.isGravel(block) ||
            level > 1 && !TFC_Core.isGround(block);
    }

    private static boolean isExposedAquiferBlockAtOrBelow(World world, int x, int y, int z) {
        for (int i = 0; i < MAX_WATER_COLUMN_RISE; i++) {
            if (isAquiferBlockAt(world, x, y - i, z)) {
                return isAquiferBlockExposedAt(world, x, y - i, z);
            }
        }

        return false;
    }

    public static void clearWaterBlockAtWithNeighbors(World world, int x, int y, int z) {
        Queue<BlockCoord> blocksToClear = new ArrayDeque<BlockCoord>();
        Set<BlockCoord> blocksCleared = new HashSet<BlockCoord>();
        blocksToClear.add(new BlockCoord(x, y, z));

        while (blocksToClear.size() > 0) {
            BlockCoord b = blocksToClear.poll();

            clearWaterBlockAt(world, b.x, b.y, b.z);
            blocksCleared.add(b);

            for (ForgeDirection dir : YAXIS_DIRS) {
                int x2 = b.x + dir.offsetX;
                int z2 = b.z + dir.offsetZ;
                BlockCoord b2 = new BlockCoord(x2, y, z2);

                if (!blocksCleared.contains(b2)) {
                    if (isFullWaterBlockAt(world, x2, y, z2)) {
                        blocksToClear.add(b2);
                    }
                }
            }
        }

        Bids.LOG.debug("Aquifer removed total of " + blocksCleared.size() + " water blocks at " + x + "," + y);
    }

    public static void clearWaterBlockAt(World world, int x, int y, int z) {
        if (isFullWaterBlockAt(world, x, y, z)) {
            // Set receding water block (meta=1)
            world.setBlockMetadataWithNotify(x, y, z, 1, 3);
        }
    }

    public static void placeWaterBlockAt(World world, int x, int y, int z) {
        if (world.isAirBlock(x, y, z)
            || isPartialWaterBlockAt(world, x, y, z)) {
            world.setBlock(x, y, z, TFCBlocks.freshWaterStationary);
            world.notifyBlockOfNeighborChange(x, y, z, TFCBlocks.freshWaterStationary);
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (isExposed && waterColumnRiseUpdateTimer.tick()) {
                updateWaterColumn();
            }

            if (updateNeighborsTimer.tick()) {
                updateAquiferNeighbors();
            }
        }
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeAquiferDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        readAquiferDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeAquiferDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readAquiferDataFromNBT(tag);
    }

    public void writeAquiferDataToNBT(NBTTagCompound tag) {
        tag.setBoolean("isExposed", isExposed);

        if (isExposed) {
            tag.setLong("lastWaterColumnRiseTime", lastWaterColumnRiseTime);
            tag.setInteger("lastKnownWaterColumnRise", lastKnownWaterColumnRise);
            tag.setInteger("neighborTotalCount", neighborTotalCount);
            tag.setInteger("neighborExposedCount", neighborExposedCount);
            tag.setBoolean("updateAquiferNeighborsRequired", updateAquiferNeighborsRequired);
        }
    }

    public void readAquiferDataFromNBT(NBTTagCompound tag) {
        isExposed = tag.getBoolean("isExposed");

        if (isExposed) {
            lastWaterColumnRiseTime = tag.getLong("lastWaterColumnRiseTime");
            lastKnownWaterColumnRise = tag.getInteger("lastKnownWaterColumnRise");
            neighborTotalCount = tag.getInteger("neighborTotalCount");
            neighborExposedCount = tag.getInteger("neighborExposedCount");
            updateAquiferNeighborsRequired = tag.getBoolean("updateAquiferNeighborsRequired");
        }
    }

}
