package com.unforbidable.tfc.bids.TileEntities;

import java.util.HashMap;
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
    HashMap<ForgeDirection, Integer> wedgeData = new HashMap<ForgeDirection, Integer>();

    Timer neighborCheckingTimer = new Timer(10);

    public TileEntityQuarry() {
        super();
    }

    public boolean isQuarryReady() {
        return getWedgeCount() == getMaxWedgeCount();
    }

    public void onQuarryDrilled() {
        for (Entry<ForgeDirection, Integer> it : wedgeData.entrySet()) {
            if (it.getValue() < WEDGE_PER_EDGE) {
                it.setValue(it.getValue() + 1);
                Bids.LOG.info("Wedge added to side: " + it.getKey() + ", total: " + getWedgeCount());
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                break;
            }
        }
    }

    public int getWedgeCount() {
        int total = 0;
        for (int c : wedgeData.values())
            total += c;
        return total;
    }

    public int getMaxWedgeCount() {
        return wedgeData.size() * WEDGE_PER_EDGE;
    }

    public ForgeDirection getQuarryOrientation() {
        return ForgeDirection.getOrientation(getBlockMetadata());
    }

    public void dropWedges() {
        int n = getWedgeCount();
        ItemStack is = new ItemStack(TFCItems.stick, n);
        EntityItem ei = new EntityItem(worldObj, xCoord, yCoord, zCoord, is);
        worldObj.spawnEntityInWorld(ei);
        Bids.LOG.info("Querry dropped sticks: " + n);
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

        NBTTagList wedgeList = new NBTTagList();
        for (Entry<ForgeDirection, Integer> it : wedgeData.entrySet()) {
            NBTTagCompound wedgeTag = new NBTTagCompound();
            wedgeTag.setByte("d", (byte) it.getKey().ordinal());
            wedgeTag.setByte("c", (byte) it.getValue().intValue());
            wedgeList.appendTag(wedgeTag);
        }
        tag.setTag("wedges", wedgeList);
    }

    public void readQuarryDataFromNBT(NBTTagCompound tag) {
        initialized = tag.getBoolean("initialized");

        wedgeData.clear();
        NBTTagList wedgeList = tag.getTagList("wedges", 10);
        for (int i = 0; i < wedgeList.tagCount(); i++) {
            NBTTagCompound wedgeTag = wedgeList.getCompoundTagAt(i);
            ForgeDirection d = ForgeDirection.getOrientation(wedgeTag.getByte("d"));
            int c = wedgeTag.getByte("c");
            wedgeData.put(d, c);
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (!initialized) {
                initialized = true;
                updateWedges();
                onQuarryDrilled();
                Bids.LOG.info("Quarry initialized");
            }

            if (neighborCheckingTimer.tick()) {
                updateWedges();
            }
        }
    }

    private void updateWedges() {
        // Quarried block location
        ForgeDirection o = getQuarryOrientation().getOpposite();
        int x = xCoord + o.offsetX;
        int y = yCoord + o.offsetY;
        int z = zCoord + o.offsetZ;
        Block block = worldObj.getBlock(x, y, z);
        IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);
        ForgeDirection orientation = ForgeDirection.getOrientation(getBlockMetadata());

        boolean dirty = false;
        int wedgesEjected = 0;

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            // Skip front and back relative to quarry location
            if (d != orientation && d != orientation.getOpposite()) {
                Block neighbor = worldObj.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
                boolean useEdge = quarriable.blockRequiresWedgesToDetach(neighbor);
                if (useEdge && !wedgeData.containsKey(d)) {
                    // Add a new edge to gather wedges
                    wedgeData.put(d, 0);
                    dirty = true;
                } else if (!useEdge && wedgeData.containsKey(d)) {
                    // Remove previously used edge
                    wedgesEjected += wedgeData.get(d);
                    wedgeData.remove(d);
                    dirty = true;
                }
            }
        }

        if (dirty) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            Bids.LOG.info("Updated max wedge count: " + getMaxWedgeCount());
        }

        if (wedgesEjected > 0) {
            ItemStack is = new ItemStack(TFCItems.stick, wedgesEjected);
            EntityItem ei = new EntityItem(worldObj, xCoord, yCoord, zCoord, is);
            worldObj.spawnEntityInWorld(ei);
            Bids.LOG.info("Dropped wedges from retired edges: " + wedgesEjected);
        }

        if (getMaxWedgeCount() == 0) {
            // No wedges are needed
            // which means the quarry shouldn't even be here
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            Bids.LOG.info("Quarry destroyed as there is no edges to wedge and nothing to quarry");
        }
    }

}
