package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryHelper;
import com.unforbidable.tfc.bids.api.QuarryRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityQuarry extends TileEntity {

    int wedgeCount = 1; // Always start with one
    int maxWedgeCount = 16; // Usually less than that

    IQuarriable quarriable;

    boolean initialized;

    Timer neighborCheckingTimer = new Timer(10);

    public TileEntityQuarry() {
        super();
    }

    public boolean isQuarryReady() {
        return wedgeCount >= maxWedgeCount;
    }

    public void onQuarryDrilled() {
        wedgeCount++;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        Bids.LOG.info("Wedge count increased: " + wedgeCount);
    }

    public int getWedgeCount() {
        return wedgeCount;
    }

    public int getMaxWedgeCount() {
        return maxWedgeCount;
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

    public void writeQuarryDataToNBT(NBTTagCompound tag) {
        tag.setInteger("wedgeCount", wedgeCount);
        tag.setInteger("maxWedgeCount", maxWedgeCount);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readQuarryDataFromNBT(tag);
    }

    public void readQuarryDataFromNBT(NBTTagCompound tag) {
        wedgeCount = tag.getInteger("wedgeCount");
        maxWedgeCount = tag.getInteger("maxWedgeCount");
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (!initialized) {
                findQuarriable();
                updateMaxWedgeCount();

                initialized = true;
            }

            if (neighborCheckingTimer.tick()) {
                updateMaxWedgeCount();
            }
        }
    }

    private void findQuarriable() {
        ForgeDirection o = getQuarryOrientation().getOpposite();
        int x = xCoord + o.offsetX;
        int y = yCoord + o.offsetY;
        int z = zCoord + o.offsetZ;
        Block block = worldObj.getBlock(x, y, z);
        quarriable = QuarryRegistry.getBlockQuarriable(block);
    }

    private void updateMaxWedgeCount() {
        // Quarried block location
        ForgeDirection o = getQuarryOrientation().getOpposite();
        int x = xCoord + o.offsetX;
        int y = yCoord + o.offsetY;
        int z = zCoord + o.offsetZ;

        int count = QuarryHelper.getSideRequiringWedgesCount(worldObj, x, y, z, quarriable, o.ordinal()) * 4;

        if (count != maxWedgeCount) {
            maxWedgeCount = count;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            Bids.LOG.info("Updated max wedge count: " + maxWedgeCount);
        }

        if (maxWedgeCount == 0) {
            // No wedges are needed
            // which means the quarry shouldn't even be here
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            Bids.LOG.info("Quarry destroyed as there is nothing to quarry");
        }
    }

}
