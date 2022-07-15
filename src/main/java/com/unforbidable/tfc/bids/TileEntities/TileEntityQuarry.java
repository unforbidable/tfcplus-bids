package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityQuarry extends TileEntity {

    int wedgeCount = 1; // Always start with one
    int maxWedgeCount = 16; // Usually less than that

    public TileEntityQuarry() {
        super();
    }

    public boolean isQuarryReady() {
        return wedgeCount >= maxWedgeCount;
    }

    public void onQuarryDrilled() {
        wedgeCount++;
        Bids.LOG.info("Wedge Count increased: " + wedgeCount);
    }

    public int getWedgeCount() {
        return wedgeCount;
    }

    public void dropWedges() {
        int n = getWedgeCount();
        ItemStack is = new ItemStack(TFCItems.stick, n);
        EntityItem ei = new EntityItem(worldObj, xCoord, yCoord, zCoord, is);
        worldObj.spawnEntityInWorld(ei);
        Bids.LOG.info("Querry dropped sticks: " + n);
    }

}
