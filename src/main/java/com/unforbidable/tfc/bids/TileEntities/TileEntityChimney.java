package com.unforbidable.tfc.bids.TileEntities;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Chimney.ChimneyHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IChimney;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class TileEntityChimney extends TileEntity implements IChimney {

    int smoke = 0;
    int fire = 0;

    public TileEntityChimney() {
        super();
    }

    @Override
    public int getChimneyTier() {
        return 0;
    }

    @Override
    public void setChimneySmoke(int smoke) {
        this.smoke = smoke;
    }

    @Override
    public int getChimneySmoke() {
        return smoke;
    }

    @Override
    public void setChimneyFire(int fire) {
        this.fire = fire;
    }

    @Override
    public int getChimneyFire() {
        return fire;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (yCoord < 255) {
                // Push smoke or fire to entity above
                if (smoke > 0 || fire > 0) {
                    TileEntity above = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
                    if (ChimneyHelper.isChimney(above)) {
                        if (ChimneyHelper.getChimneySmoke(above) < smoke) {
                            ChimneyHelper.setChimneySmoke(above, smoke);
                        }

                        if (ChimneyHelper.getChimneyFire(above) < fire) {
                            ChimneyHelper.setChimneyFire(above, fire);
                        }

                        smoke = 0;
                        fire = 0;
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                }
            }

            if (smoke > 0) {
                smoke--;
                if (smoke == 0) {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }

            if (fire > 0) {
                doHarmToLivingEntities();

                fire--;
                if (fire == 0) {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void doHarmToLivingEntities() {
        if (!worldObj.getBlock(xCoord, yCoord + 1, zCoord).isOpaqueCube()) {
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 3, zCoord + 1);
            for (EntityLivingBase entityLiving : (List<EntityLivingBase>)worldObj.getEntitiesWithinAABB(EntityLivingBase.class, aabb)) {
                entityLiving.setFire(2);
            }
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();
        readChimneyDataFromNBT(tag);
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeChimneyDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        writeChimneyDataToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readChimneyDataFromNBT(tag);
    }

    public void writeChimneyDataToNBT(NBTTagCompound tag) {
        tag.setInteger("smoke", smoke);
        tag.setInteger("fire", fire);
    }

    public void readChimneyDataFromNBT(NBTTagCompound tag) {
        smoke = tag.getInteger("smoke");
        fire = tag.getInteger("fire");
    }

}
