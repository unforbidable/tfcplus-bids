package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class TileEntityClayLamp extends TileEntity implements IMessageHanldingTileEntity<TileEntityUpdateMessage> {

    public static final int FUEL_NONE = 0;
    public static final int FUEL_LOW = 1;
    public static final int FUEL_MED = 2;
    public static final int FUEL_HIGH = 3;

    public static final int FUEL_MAX_VOLUME = 300;

    private static final float FUEL_CONSUMED_PER_HOUR = 1 / 4f;

    FluidStack fuelStack;
    int orientation;
    long lastTimeFuelConsumed;
    float fuelAmount;

    boolean clientNeedToUpdate = false;
    boolean clientDataLoaded = false;

    int lastFuelLevel = FUEL_NONE;
    boolean fuelLevelUpdated = false;

    public TileEntityClayLamp() {
        lastTimeFuelConsumed = TFC_Time.getTotalTicks();
    }

    public boolean isClientDataLoaded() {
        return clientDataLoaded;
    }

    public int getOrientation() {
        return orientation;
    }

    public void updateFuelAmount(boolean updateFuelLevel) {
        if (hasFuel()) {
            long timeSinceLastConsumption =  TFC_Time.getTotalTicks() - lastTimeFuelConsumed;
            lastTimeFuelConsumed = TFC_Time.getTotalTicks();

            float fuelConsumedNow = timeSinceLastConsumption * FUEL_CONSUMED_PER_HOUR / TFC_Time.HOUR_LENGTH;
            fuelAmount -= fuelConsumedNow;
            if (fuelAmount < 0) {
                fuelAmount = 0;
            }
            Bids.LOG.debug("Consumed fuel: " + fuelConsumedNow);
            Bids.LOG.debug("Remaining fuel: " + fuelAmount);

            if (fuelAmount != fuelStack.amount) {
                fuelStack.amount = (int) Math.floor(fuelAmount);

                Bids.LOG.debug("Updated fuel amount: " + fuelStack.amount);

                if (fuelStack.amount == 0) {
                    fuelStack = null;
                    Bids.LOG.debug("Fuel depleted!");
                }

                if (fuelStack == null || TFC_Core.isExposedToRain(worldObj, xCoord, yCoord, zCoord)) {
                    int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord) & 7;
                    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);
                }

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                if (updateFuelLevel) {
                    updateFuelLevel();
                }
            }
        }
    }

    public boolean isOnFire() {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 7;
    }

    public void setOnFire(boolean onFire) {
        if (isOnFire()) {
            updateFuelAmount(true);
        }

        if (!onFire || getFuelTimeLeft() > 0) {
            int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            if (onFire) {
                metadata = metadata | 8;
            } else {
                metadata = metadata & 7;
            }
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);
        }

        if (isOnFire()) {
            lastTimeFuelConsumed = TFC_Time.getTotalTicks();
        }
    }

    public void updateFuelLevel() {
        int newFuelLevel = hasFuel() ? getFuelLevelForAmount(getFuel().amount) : FUEL_NONE;
        if (!fuelLevelUpdated || newFuelLevel != lastFuelLevel) {
            lastFuelLevel = newFuelLevel;
            fuelLevelUpdated = true;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;
        }
    }

    public float getFuelTimeLeft() {
        if (hasFuel()) {
            return getFuel().amount / FUEL_CONSUMED_PER_HOUR;
        } else {
            return 0;
        }
    }

    public void onPlacedFromItemStack(ItemStack is, int orientation) {
        fuelStack = FluidStack.loadFluidStackFromNBT(is.getTagCompound());
        if (fuelStack != null) {
            fuelAmount = fuelStack.amount;
        }

        this.orientation = orientation;
    }

    public boolean hasFuel() {
        return fuelStack != null;
    }

    public FluidStack getFuel() {
        return fuelStack;
    }

    public static int getFuelLevelForAmount(int amount) {
        if (amount < FUEL_MAX_VOLUME - 250) {
            return FUEL_LOW;
        } else if (amount < FUEL_MAX_VOLUME / 2) {
            return FUEL_MED;
        } else {
            return FUEL_HIGH;
        }
    }

    public boolean addLiquid(FluidStack inFS)
    {
        if (isOnFire()) {
            updateFuelAmount(false);
        }

        if (inFS != null)
        {
            if (fuelStack == null)
            {
                // Cannot fill up from larger amount
                if (inFS.amount > FUEL_MAX_VOLUME || inFS.getFluid() != TFCFluids.OLIVEOIL) {
                    return false;
                }

                fuelStack = inFS.copy();
                inFS.amount = 0;
            }
            else
            {
                // check if the container would overflow or if the fluid being added does
                // not match the contained liquid
                if (fuelStack.amount + inFS.amount > FUEL_MAX_VOLUME || !fuelStack.isFluidEqual(inFS)) {
                    return false;
                }

                fuelStack.amount += inFS.amount;
                inFS.amount = 0;
            }

            fuelAmount = fuelStack.amount;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            updateFuelLevel();

            return true;
        }

        return false;
    }

    public ItemStack addLiquid(ItemStack is)
    {
        if (is == null || is.stackSize > 1)
            return is;
        if (FluidContainerRegistry.isFilledContainer(is))
        {
            FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(is);
            if (addLiquid(fs))
            {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return FluidContainerRegistry.drainFluidContainer(is);
            }
        }
        else if (is.getItem() instanceof IFluidContainerItem)
        {
            FluidStack isfs = ((IFluidContainerItem) is.getItem()).getFluid(is);
            if (isfs != null && addLiquid(isfs))
            {
                ((IFluidContainerItem) is.getItem()).drain(is, is.getMaxDamage(), true);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }
        return is;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }
        }
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeLampDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        // This forces client render after NBT data is loaded
        // until then fuel level in clay lamp cannot be rendered
        if (worldObj.isRemote && !clientDataLoaded) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientDataLoaded = true;
        }

        readLampDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeLampDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readLampDataFromNBT(tag);
    }

    public void writeLampDataToNBT(NBTTagCompound tag) {
        tag.setInteger("orientation", orientation);
        tag.setLong("lastTimeFuelConsumed", lastTimeFuelConsumed);
        tag.setFloat("fuelAmount", fuelAmount);

        if (hasFuel()) {
            tag.setTag("Fuel", getFuel().writeToNBT(new NBTTagCompound()));
        }
    }

    public void readLampDataFromNBT(NBTTagCompound tag) {
        orientation = tag.getInteger("orientation");
        lastTimeFuelConsumed = tag.getLong("lastTimeFuelConsumed");
        fuelAmount = tag.getFloat("fuelAmount");

        if (tag.hasKey("Fuel")) {
            fuelStack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("Fuel"));
        }
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
