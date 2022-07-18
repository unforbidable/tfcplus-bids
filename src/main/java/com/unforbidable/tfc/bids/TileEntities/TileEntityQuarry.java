package com.unforbidable.tfc.bids.TileEntities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.QuarryRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityQuarry extends TileEntity {

    final static int WEDGE_PER_EDGE = 4;

    boolean initialized;
    Map<ForgeDirection, Integer> wedgeData;

    Timer neighborCheckingTimer = new Timer(10);

    int clientInitRenderAttempts;
    Timer clientInitRenderTimer = new Timer(0);

    public TileEntityQuarry() {
        super();
    }

    public boolean isQuarryReady() {
        return getWedgeCount() == getMaxWedgeCount();
    }

    public void onQuarryDrilled() {
        boolean added = false;
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (wedgeData.containsKey(d)) {
                int value = wedgeData.get(d);
                if (value < WEDGE_PER_EDGE) {
                    wedgeData.put(d, value + 1);
                    Bids.LOG.debug("Wedge added to side: " + d + ", total: " + getWedgeCount());
                    added = true;
                    break;
                }
            }
        }

        if (added) {
            forceClientUpdate();
            forceClientRenderUpdate();
        }
    }

    private void forceClientUpdate() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void forceClientRenderUpdate() {
        // Forcing client update by setting/clearing an extra meta data bit
        int side = getBlockMetadata() & 0x7;
        int flag = (getBlockMetadata() & 0x8) == 0 ? 0x8 : 0;
        // Flag 1 will cause a block update. Flag 2 will send the change to clients (you
        // almost always want this). Flag 4 prevents the block from being re-rendered,
        // if this is a client world. Flags can be added together.
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, side | flag, 3);
    }

    public Map<ForgeDirection, Integer> getWedges() {
        return wedgeData;
    }

    public int getWedgeCount() {
        int total = 0;
        if (wedgeData != null) {
            for (int c : wedgeData.values())
                total += c;
        }
        return total;
    }

    public int getMaxWedgeCount() {
        return wedgeData != null ? wedgeData.size() * WEDGE_PER_EDGE : 0;
    }

    public ForgeDirection getQuarryOrientation() {
        int meta = getBlockMetadata();
        return getQuarryOrientation(meta);
    }

    public ForgeDirection getQuarryOrientation(int meta) {
        return ForgeDirection.getOrientation(meta & 7);
    }

    public void dropAllWedges() {
        int n = getWedgeCount();
        dropWedges(n, true);
    }

    public void dropWedges(int n, boolean randomized) {
        if (n > 0) {
            if (randomized) {
                // Drop a random amount of wedges requested
                // but 1 minimum
                n = worldObj.rand.nextInt(n) + 1;
            }
            ItemStack is = new ItemStack(TFCItems.stick, n);
            EntityItem ei = new EntityItem(worldObj, xCoord, yCoord, zCoord, is);
            worldObj.spawnEntityInWorld(ei);
            Bids.LOG.debug("Querry dropped sticks: " + n);
        }
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
        readFromNBT(tag);
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
        tag.setBoolean("initialized", initialized);

        if (initialized) {
            NBTTagList wedgeList = new NBTTagList();
            for (Entry<ForgeDirection, Integer> it : wedgeData.entrySet()) {
                NBTTagCompound wedgeTag = new NBTTagCompound();
                wedgeTag.setByte("d", (byte) it.getKey().ordinal());
                wedgeTag.setByte("c", (byte) it.getValue().intValue());
                wedgeList.appendTag(wedgeTag);
            }
            tag.setTag("wedges", wedgeList);
        }
    }

    public void readQuarryDataFromNBT(NBTTagCompound tag) {
        initialized = tag.getBoolean("initialized");

        if (initialized) {
            NBTTagList wedgeList = tag.getTagList("wedges", 10);
            wedgeData = new HashMap<ForgeDirection, Integer>();
            for (int i = 0; i < wedgeList.tagCount(); i++) {
                NBTTagCompound wedgeTag = wedgeList.getCompoundTagAt(i);
                ForgeDirection d = ForgeDirection.getOrientation(wedgeTag.getByte("d"));
                int c = wedgeTag.getByte("c");
                wedgeData.put(d, c);
            }
            Bids.LOG.debug("Quarry client updated after server initialization");
        } else {
            Bids.LOG.debug("Quarry client updated before server initialization");
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (!initialized) {
                Bids.LOG.debug("Quarry initializing");
                initialized = true;

                wedgeData = initializeWedges();

                forceClientUpdate();

                // This triggers a delayed render update
                // when the quarry is created and
                // a wedge is added
                // hopefully after the client has received
                // initialized wedges from the server
                // This is needed because during the initial
                // render, we can't predict
                // where the first wedge appears
                // For that client would need to see the
                // neightbors, and that is not guaranteed
                // during the initial render
                // Repeat a number of times
                clientInitRenderAttempts = 3;
                clientInitRenderTimer.delay(2);
            }

            if (clientInitRenderTimer.tick()) {
                forceClientRenderUpdate();
                Bids.LOG.debug("Quarry client render forced");

                if (--clientInitRenderAttempts > 0) {
                    clientInitRenderTimer.delay(2);
                }
            }

            if (neighborCheckingTimer.tick()) {
                boolean changed = updateWedges(wedgeData);

                if (changed) {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    Bids.LOG.debug("Updated max wedge count: " + getMaxWedgeCount());

                    if (getWedgeCount() == 0) {
                        // All wedges dropped during the update
                        // so remove quarry
                        worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                        Bids.LOG.debug("Quarry without wedges removed");
                    }
                }
            }
        }
    }

    private Map<ForgeDirection, Integer> initializeWedges() {
        Map<ForgeDirection, Integer> data = new HashMap<ForgeDirection, Integer>();

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

        boolean addFirstWedge = true;

        if (quarriable != null) {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                // Skip front and back relative to quarry location
                if (d != orientation && d != opposite) {
                    Block neighbor = worldObj.getBlock(x2 + d.offsetX, y2 + d.offsetY, z2 + d.offsetZ);
                    boolean useEdge = quarriable.blockRequiresWedgesToDetach(neighbor);
                    if (useEdge) {
                        // Add a new edge to gather wedges
                        if (addFirstWedge) {
                            data.put(d, 1);
                            addFirstWedge = false;
                        } else {
                            data.put(d, 0);
                        }
                    }
                }
            }
        } else {
            Bids.LOG.warn("Quarriable block missing when initializing wedges for quarry side: " + orientation
                    + " quarried at: " + x2 + ", " + y2 + ", " + z2);
        }

        return data;
    }

    private boolean updateWedges(Map<ForgeDirection, Integer> data) {
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
        int wedgesDropped = 0;

        if (quarriable != null) {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                // Skip front and back relative to quarry location
                if (d != orientation && d != opposite) {
                    Block neighbor = worldObj.getBlock(x2 + d.offsetX, y2 + d.offsetY, z2 + d.offsetZ);
                    boolean useEdge = quarriable.blockRequiresWedgesToDetach(neighbor);
                    if (useEdge && !data.containsKey(d)) {
                        // Add a new edge to gather wedges
                        data.put(d, 0);
                        dirty = true;
                    } else if (!useEdge && data.containsKey(d)) {
                        // Remove previously used edge
                        wedgesDropped += data.get(d);
                        data.remove(d);
                        dirty = true;
                    }
                }
            }
        } else {
            Bids.LOG.warn("Quarriable block missing when updating wedges for quarry side: " + orientation
                    + " quarried at: " + x2 + ", " + y2 + ", " + z2);
        }

        if (wedgesDropped > 0) {
            dropWedges(wedgesDropped, false);
        }

        return dirty;
    }

}
