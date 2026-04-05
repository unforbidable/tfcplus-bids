package com.unforbidable.tfc.bids.TileEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemBlocks.ItemSoil;
import com.dunk.tfc.Items.ItemCoal;
import com.dunk.tfc.TileEntities.TEFirepit;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Firepit.Fuels.FuelPeatTFC;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.FirepitRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityNewFirepit extends TEFirepit implements IMessageHanldingTileEntity<TileEntityUpdateMessage> {

    public static final int FUEL_INPUT_SLOT = 0;
    public static final int FUEL_BURN_SLOT = 5;
    public static final int[] FUEL_SLOTS_ALL = new int[] { 5, 4, 3, 0 };

    List<Integer> pretendFuelSlots = new ArrayList<Integer>();
    ItemStack pretendFuelItemStack = new ItemStack(TFCItems.stick);

    float lastTempSendToClient = 0;
    boolean clientNeedToUpdate = false;
    Timer updateClientTempTimer = new Timer(50);

    public TileEntityNewFirepit() {
        super();

        // The default start is when a fire starter was used
        // with a bunch of sticks (and maybe straw)
        // Init using BidsItems.kindling parameters
        initWithKindling(new ItemStack(BidsItems.kindling), true);
    }

    public int getFuelCount() {
        int count = 0;

        for (int slot : FUEL_SLOTS_ALL) {
            if (fireItemStacks[slot] != null) {
                count++;
            }
        }

        return count;
    }

    public int getMaxFuelCount() {
        return FUEL_SLOTS_ALL.length;
    }

    public void initWithKindling(ItemStack kindling, boolean setOnFire) {
        IFirepitFuelMaterial fuel = FirepitRegistry.findFuel(kindling.getItem());
        if (setOnFire && fuel != null) {
            fuelTimeLeft = fuel.getFuelBurnTime(kindling) * BidsOptions.Firepit.burnTimeMultiplier;
            fuelBurnTemp = fuel.getFuelMaxTemp(kindling);
            fuelTasteProfile = fuel.getFuelTasteProfile(kindling);
            fireTemp = fuelBurnTemp / 2f;
            ashNumber = 1;
        } else {
            fireItemStacks[FUEL_BURN_SLOT] = new ItemStack(kindling.getItem(), 1, kindling.getItemDamage());
            fuelTimeLeft = 0;
            fuelBurnTemp = 0;
            fuelTasteProfile = 0;
            fireTemp = 0;
            ashNumber = 0;
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }

            if (updateClientTempTimer.tick()) {
                // Check periodically for temp change
                // Update client only when whole degrees change by 20 or more units
                float roundedFireTemp = (float) (Math.ceil(Math.floor(fireTemp) / 20) * 20);
                if (lastTempSendToClient != roundedFireTemp) {
                    lastTempSendToClient = roundedFireTemp;

                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                    clientNeedToUpdate = true;
                }
            }

            // TFC firepit only collects wood items it recognises
            // so we do the collection of any supported fuel
            final boolean isThereMoreToCollect = collectFuel();

            if (isThereMoreToCollect && fireItemStacks[FUEL_INPUT_SLOT] == null) {
                // To keep super firepit from collecting unsupported fuel
                // we fill it up with pretend fuel
                // Unfortunately this does not prevent the super from
                // scanning for items and calling worldObj.getEntitiesWithinAABB
                // because that is done every update no matter what
                for (int slot : FUEL_SLOTS_ALL) {
                    if (fireItemStacks[slot] == null) {
                        pretendFuelSlots.add(slot);
                        fireItemStacks[slot] = pretendFuelItemStack;
                    }
                }
            }

            // Make sure any fuel is in the burning slot
            // before trying to consume fuel
            handleFuelStack();

            // We also need to consume the fuel when needed
            // according to the registred properties
            // which conveniently keeps the super from consuming it
            consumeFuel();
        }

        super.updateEntity();
    }

    @Override
    public void careForInventorySlot(ItemStack itemStack) {
        // This is only overriden so that we can remove
        // pretend fuel right after super tried to collect fuel
        // The pretend fuel needs to be removed
        // before super might try to consume some
        if (pretendFuelSlots.size() > 0) {
            for (int slot : pretendFuelSlots) {
                fireItemStacks[slot] = null;
            }

            pretendFuelSlots.clear();
        }

        super.careForInventorySlot(itemStack);
    }

    @SuppressWarnings("unchecked")
    protected boolean collectFuel() {
        boolean isThereMoreToCollect = false;
        if (fireItemStacks[0] == null) {
            final AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord,
                    xCoord + 1, yCoord + 1.1, zCoord + 1);
            final List<EntityItem> list = (List<EntityItem>) worldObj.getEntitiesWithinAABB(EntityItem.class, bounds);

            for (EntityItem entity : list) {
                final ItemStack is = entity.getEntityItem();

                if (isValidFuelMaterial(is)) {
                    while (is.stackSize > 0 && fireItemStacks[FUEL_INPUT_SLOT] == null) {
                        ItemStack one = is.copy();
                        one.stackSize = 1;
                        setInventorySlotContents(0, one);

                        is.stackSize--;

                        Bids.LOG.debug("Fuel collected: " + is.getDisplayName());

                        // This is called now to push the fuel item that was just added down
                        handleFuelStack();

                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                        clientNeedToUpdate = true;
                    }

                    if (is.stackSize == 0) {
                        entity.setDead();
                    } else {
                        isThereMoreToCollect = true;

                        break;
                    }
                } else {
                    isThereMoreToCollect = true;
                }
            }
        }

        return isThereMoreToCollect;
    }

    protected boolean isValidFuelMaterial(ItemStack is) {
        IFirepitFuelMaterial fuel = FirepitRegistry.findFuel(is.getItem());
        return fuel != null && fuel.isFuelValid(is);
    }

    protected void consumeFuel() {
        if (fuelTimeLeft <= 0 && fireTemp >= 1 && fireItemStacks[FUEL_BURN_SLOT] != null
                && !TFC_Core.isExposedToRain(worldObj, xCoord, yCoord, zCoord)) {
            final ItemStack itemStack = fireItemStacks[FUEL_BURN_SLOT];
            final IFirepitFuelMaterial fuel = FirepitRegistry.findFuel(itemStack.getItem());

            fuelTasteProfile = fuel.getFuelTasteProfile(itemStack);
            fuelTimeLeft = fuel.getFuelBurnTime(itemStack) * BidsOptions.Firepit.burnTimeMultiplier;
            fuelBurnTemp = fuel.getFuelMaxTemp(itemStack);

            if (ashNumber < 5) {
                ashNumber += getAshForFuel(fuel, itemStack);
            }

            Bids.LOG.debug("Fuel consumed: " + itemStack.getDisplayName()
                    + " temp: " + fuelBurnTemp + " time: " + fuelTimeLeft);

            fireItemStacks[FUEL_BURN_SLOT] = null;
        }

    }

    protected int getAshForFuel(IFirepitFuelMaterial fuel, ItemStack is) {
        if (is.getItem() instanceof ItemCoal || is.getItem() == Item.getItemFromBlock(TFCBlocks.peat)) {
            return 0;
        } else {
            int burnTime = fuel.getFuelBurnTime(is);
            float ashChance = burnTime / 2000f;
            return new Random().nextFloat() < ashChance ? 1 : 0;
        }
    }

    @Override
    public void onTileEntityMessage(TileEntityUpdateMessage message) {
        worldObj.markBlockForUpdate(message.getXCoord(), message.getYCoord(), message.getZCoord());
    }

    public static void sendUpdateMessage(World world, int x, int y, int z) {
        NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 255);
        Bids.network.sendToAllAround(new TileEntityUpdateMessage(x, y, z, 0), tp);
    }

}
