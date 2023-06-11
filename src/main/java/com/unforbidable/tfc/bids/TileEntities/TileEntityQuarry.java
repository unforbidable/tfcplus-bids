package com.unforbidable.tfc.bids.TileEntities;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.Core.Quarry.QuarrySideWedgeData;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.Interfaces.IPlugAndFeather;
import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;
import com.unforbidable.tfc.bids.api.QuarryRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TileEntityQuarry extends TileEntity implements IMessageHanldingTileEntity<TileEntityUpdateMessage> {

    static final int WEDGE_PER_EDGE = 4;

    Map<ForgeDirection, QuarrySideWedgeData> wedgeData = new HashMap<ForgeDirection, QuarrySideWedgeData>();

    Timer neighborCheckingTimer = new Timer(10);

    boolean clientNeedToUpdate = true;

    public TileEntityQuarry() {
        super();
    }

    public boolean isQuarryReady() {
        return getWedgeCount() == getMaxWedgeCount();
    }

    public void onQuarryDrilled(ItemStack plugAndFeather) {
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (wedgeData.containsKey(d)) {
                QuarrySideWedgeData data = wedgeData.get(d);
                if (data.storage.size() < WEDGE_PER_EDGE) {
                    data.storage.add(plugAndFeather);

                    Bids.LOG.debug("Wedge added to side: " + d + ", total: " + getWedgeCount());

                    clientNeedToUpdate = true;
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                    return;
                }
            }
        }
    }

    public Collection<QuarrySideWedgeData> getWedges() {
        return wedgeData.values();
    }

    public int getWedgeCount() {
        int total = 0;
        for (QuarrySideWedgeData data : wedgeData.values()) {
            total += data.storage.size();
        }

        return total;
    }

    public int getMaxWedgeCount() {
        return wedgeData.size() * WEDGE_PER_EDGE;
    }

    public ForgeDirection getQuarryOrientation() {
        int meta = getBlockMetadata();
        return getQuarryOrientation(meta);
    }

    public ForgeDirection getQuarryOrientation(int meta) {
        return ForgeDirection.getOrientation(meta & 7);
    }

    public void dropAllWedges() {
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            dropWedgesFromSide(d, false);
        }
    }

    public void dropWedgesFromSide(ForgeDirection direction, boolean lossless) {
        if (wedgeData.size() == 0) {
            Bids.LOG.warn("Missing wedges in quarry at: " + xCoord + ", " + yCoord + ", " + zCoord);
        }

        if (wedgeData.containsKey(direction)) {
            QuarrySideWedgeData wedges = wedgeData.get(direction);
            for (ItemStack wedge : wedges.storage) {
                if (lossless) {
                    // When wedges just fall off during an update
                    // because some other block was removed
                    dropSingleWedgeAsItem(wedge);
                } else {
                    if (wedge.getItem() instanceof IPlugAndFeather) {
                        float dropRate = ((IPlugAndFeather) wedge.getItem()).getPlugAndFeatherDropRate(wedge);
                        if (worldObj.rand.nextFloat() < dropRate) {
                            dropSingleWedgeAsItem(wedge);
                        }
                    } else {
                        // Default 50% rate if not specified
                        if (worldObj.rand.nextFloat() < 0.5f) {
                            dropSingleWedgeAsItem(wedge);
                        }
                    }
                }
            }
        }
    }

    private void dropSingleWedgeAsItem(ItemStack is) {
        EntityItem ei = new EntityItem(worldObj, xCoord, yCoord, zCoord, is);
        worldObj.spawnEntityInWorld(ei);
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeQuarryDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        readQuarryDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeQuarryDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readQuarryDataFromNBT(tag);
    }

    public void writeQuarryDataToNBT(NBTTagCompound tag) {
        // Only for backward compatibility
        tag.setBoolean("initialized", true);

        NBTTagList wedgeList = new NBTTagList();
        for (QuarrySideWedgeData data : wedgeData.values()) {
            NBTTagCompound wedgeTag = new NBTTagCompound();
            data.writeToNBT(wedgeTag);
            wedgeList.appendTag(wedgeTag);
        }
        tag.setTag("wedges", wedgeList);
    }

    public void readQuarryDataFromNBT(NBTTagCompound tag) {
        wedgeData.clear();
        NBTTagList wedgeStorageList = tag.getTagList("wedges", 10);
        for (int i = 0; i < wedgeStorageList.tagCount(); i++) {
            NBTTagCompound wedgeTag = wedgeStorageList.getCompoundTagAt(i);
            QuarrySideWedgeData data = QuarrySideWedgeData.readFromNBT(wedgeTag);
            wedgeData.put(data.direction, data);
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }

            if (neighborCheckingTimer.tick()) {
                boolean changed = updateWedges();

                if (changed) {
                    if (getWedgeCount() == 0) {
                        // All wedges dropped during the update
                        // so remove quarry
                        worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                        Bids.LOG.debug("Quarry without wedges removed");
                    } else {
                        clientNeedToUpdate = true;
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                        Bids.LOG.debug("Updated max wedge count: " + getMaxWedgeCount());
                    }
                }
            }
        }
    }

    public void initializeWedges() {
        ForgeDirection orientation = getQuarryOrientation();
        ForgeDirection opposite = getQuarryOrientation().getOpposite();

        // Quarried block location
        int x2 = xCoord + opposite.offsetX;
        int y2 = yCoord + opposite.offsetY;
        int z2 = zCoord + opposite.offsetZ;
        Block block = worldObj.getBlock(x2, y2, z2);
        IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);

        Bids.LOG.debug("Initializing wedges for quarry side: " + orientation
                + " quarried at: " + x2 + ", " + y2 + ", " + z2);

        if (quarriable != null) {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                // Skip front and back relative to quarry location
                if (d != orientation && d != opposite) {
                    Block neighbor = worldObj.getBlock(x2 + d.offsetX, y2 + d.offsetY, z2 + d.offsetZ);
                    int neighborMetadata = worldObj.getBlockMetadata(x2 + d.offsetX, y2 + d.offsetY, z2 + d.offsetZ);
                    boolean useEdge = quarriable.blockRequiresWedgesToDetach(neighbor, neighborMetadata);
                    if (useEdge) {
                        wedgeData.put(d, new QuarrySideWedgeData(d));
                    }
                }
            }
        } else {
            // This should not happen when initializing but still
            Bids.LOG.warn("Quarriable block missing when initializing wedges for quarry side: " + orientation
                    + " quarried at: " + x2 + ", " + y2 + ", " + z2);
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            Bids.LOG.warn("An invalid quarry was removed from: "
                    + xCoord + ", " + yCoord + ", " + zCoord);
        }
    }

    private boolean updateWedges() {
        ForgeDirection orientation = getQuarryOrientation();
        ForgeDirection opposite = orientation.getOpposite();

        // Quarried block location
        int x2 = xCoord + opposite.offsetX;
        int y2 = yCoord + opposite.offsetY;
        int z2 = zCoord + opposite.offsetZ;
        Block block = worldObj.getBlock(x2, y2, z2);
        IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);

        Bids.LOG.debug("Updating wedges for quarry side: " + orientation
                + " quarried at: " + x2 + ", " + y2 + ", " + z2);

        boolean dirty = false;

        if (quarriable != null) {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                // Skip front and back relative to quarry location
                if (d != orientation && d != opposite) {
                    Block neighbor = worldObj.getBlock(x2 + d.offsetX, y2 + d.offsetY, z2 + d.offsetZ);
                    int neighborMetadata = worldObj.getBlockMetadata(x2 + d.offsetX, y2 + d.offsetY, z2 + d.offsetZ);
                    boolean useEdge = quarriable.blockRequiresWedgesToDetach(neighbor, neighborMetadata);
                    if (useEdge && !wedgeData.containsKey(d)) {
                        // Add a new edge to gather wedges
                        wedgeData.put(d, new QuarrySideWedgeData(d));
                        dirty = true;
                    } else if (!useEdge && wedgeData.containsKey(d)) {
                        // Remove previously used edge
                        dropWedgesFromSide(d, true);
                        wedgeData.remove(d);
                        dirty = true;
                    }
                }
            }
        } else {
            // This can happen when a quarried block that was previously quarriable
            // is quarriable no more,
            // e.g. when version 0.11.0 removed quarrying of metamorphic rocks
            // using a stone drill
            Bids.LOG.warn("Quarriable block missing when updating wedges for quarry side: " + orientation
                    + " quarried at: " + x2 + ", " + y2 + ", " + z2);
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            Bids.LOG.warn("An invalid quarry was removed from: "
                    + xCoord + ", " + yCoord + ", " + zCoord);
        }

        return dirty;
    }

    @Override
    public void onTileEntityMessage(TileEntityUpdateMessage message) {
        worldObj.markBlockForUpdate(message.getXCoord(), message.getYCoord(), message.getZCoord());
        Bids.LOG.debug("Client updated at: " + message.getXCoord() + ", " + message.getYCoord() + ", "
            + message.getZCoord());
    }

    public static void sendUpdateMessage(World world, int x, int y, int z) {
        NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 255);
        Bids.network.sendToAllAround(new TileEntityUpdateMessage(x, y, z, 0), tp);
        Bids.LOG.debug("Sent update message");
    }

}
