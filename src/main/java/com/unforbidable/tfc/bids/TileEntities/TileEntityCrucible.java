package com.unforbidable.tfc.bids.TileEntities;

import java.util.List;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Items.ItemMeltedMetal;
import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.TFC_ItemHeat;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Interfaces.IHeatSourceTE;
import com.dunk.tfc.api.Interfaces.ISmeltable;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Containers.Slots.ISlotTracker;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.Core.Chimney.ChimneyHelper;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleInputMonitor;
import com.unforbidable.tfc.bids.Core.Crucible.CrucibleLiquidStorage;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Interfaces.ILiquidMetalContainer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityCrucible extends TileEntity implements IInventory, ISlotTracker, ILiquidMetalContainer {

    ItemStack[] storage;
    float solidTemp = 0;
    float liquidTemp = 0;
    CrucibleLiquidStorage liquidStorage = new CrucibleLiquidStorage();
    CrucibleInputMonitor inputMonitor = new CrucibleInputMonitor(this);

    int combinedTemp = 0;
    String output = null;
    boolean isOutputAvailable = false;
    boolean isOutputDirty = true;
    int alloyMixingCountdown = 0;
    TileEntity glassMakingChimney = null;
    Metal outputMetal = Global.UNKNOWN;

    float heatingMult = 10;
    Timer solidHeatingTimer = new Timer(10);
    Timer liquidHeatingTimer = new Timer(10);
    Timer liquidInputTimer = new Timer(1);
    Timer liquidOutputTimer = new Timer(2);
    Timer alloyMixingTimer = new Timer(0);

    static final long GLASS_MAKING_TICKS = TFC_Time.HOUR_LENGTH * 8;
    long glassMakingCompletedTicks = 0;

    static final byte UPDATE_NONE = 0;
    static final byte UPDATE_TEMP = 1;
    static final byte UPDATE_LIQUID = 2;
    static final byte UPDATE_FLAGS = 4;
    static final byte UPDATE_OUTPUT = 8;
    static final byte UPDATE_ALLOY_MIXING = 16;
    static final byte UPDATE_GLASS_MAKING = 32;
    static final byte UPDATE_ALL = 63;
    byte updateMask = UPDATE_NONE;
    boolean useUpdateMask = false;

    static final byte FLAGS_NONE = 0;
    static final byte FLAGS_SOLIDIFIED = 1;
    static final byte FLAGS_SMELTING = 2;
    static final byte FLAGS_LIQUID_IN = 4;
    static final byte FLAGS_LIQUID_OUT = 8;
    byte flags = FLAGS_NONE;
    byte flagsPreviouslyUpdated = FLAGS_NONE;

    public TileEntityCrucible() {
        storage = new ItemStack[getSizeInventory() + 2];
    }

    public abstract int getGui();

    public abstract int getInputSlotCount();

    public abstract int getMaxVolume();

    public abstract int getMaxTemp();

    public abstract boolean hasLiquidInputSlot();

    public abstract float getHeatTransferEfficiency();

    @SideOnly(Side.CLIENT)
    public int getCombinedTemp() {
        return combinedTemp;
    }

    @SideOnly(Side.CLIENT)
    public int getLiquidVolume() {
        return liquidStorage.getVolume();
    }

    @SideOnly(Side.CLIENT)
    public boolean isSolidified() {
        return (flags & FLAGS_SOLIDIFIED) != 0;
    }

    @SideOnly(Side.CLIENT)
    public boolean isSmelting() {
        return (flags & FLAGS_SMELTING) != 0;
    }

    @SideOnly(Side.CLIENT)
    public boolean isLiquidIn() {
        return (flags & FLAGS_LIQUID_IN) != 0;
    }

    @SideOnly(Side.CLIENT)
    public boolean isLiquidOut() {
        return (flags & FLAGS_LIQUID_OUT) != 0;
    }

    @SideOnly(Side.CLIENT)
    public List<String> getLiquidInfo(String componentPrefix) {
        return liquidStorage.getInfo(componentPrefix);
    }

    @SideOnly(Side.CLIENT)
    public String getOutput() {
        return output;
    }

    @SideOnly(Side.CLIENT)
    public boolean isOutputAvailable() {
        return isOutputAvailable;
    }

    @SideOnly(Side.CLIENT)
    public int getAlloyMixingCountdown() {
        return alloyMixingCountdown;
    }

    @SideOnly(Side.CLIENT)
    public float getGlassMakingProgress() {
        return (glassMakingCompletedTicks - TFC_Time.getTotalTicks()) / (float) GLASS_MAKING_TICKS;
    }

    @SideOnly(Side.CLIENT)
    public int getGlassMakingRemainingHours() {
        return (int) Math.ceil(getGlassMakingProgress() * 12);
    }

    @SideOnly(Side.CLIENT)
    public boolean isGlassMakingActive() {
        return glassMakingCompletedTicks > 0;
    }

    @SideOnly(Side.CLIENT)
    public TileEntity getGlassMakingChimney() {
        return glassMakingChimney;
    }

    public boolean isItemStackValidInput(ItemStack is) {
        return is.getItem() instanceof ISmeltable
                && ((ISmeltable) is.getItem()).isSmeltable(is)
                && isValidInputMetal(((ISmeltable) is.getItem()).getMetalType(is))
                && CrucibleHelper.isMeltedAtTemp(is, getMaxTemp())
                && !CrucibleHelper.isOreIron(is)
                || CrucibleHelper.isGlassIngredient(is)
                || CrucibleHelper.isGlass(is);
    }

    public boolean isItemStackValidLiquidInput(ItemStack is) {
        return is.getItem() instanceof ItemMeltedMetal;
    }

    public boolean isItemStackValidLiquidOutput(ItemStack is) {
        return CrucibleHelper.isValidMold(is, null);
    }

    @Override
    public boolean canEjectLiquidMetal(Metal metal, int volume) {
        return inputMonitor.getVolume() == 0
                && liquidStorage.getVolume() >= volume
                && liquidStorage.isAllLiquid(liquidTemp)
                && liquidStorage.getOutputMetal() == metal;
    }

    @Override
    public boolean canAcceptLiquidMetal(Metal metal, int volume, float temp) {
        return inputMonitor.getVolume() == 0
                && temp <= getMaxTemp()
                && (liquidStorage.getVolume() == 0
                        || liquidStorage.getVolume() + volume <= getMaxVolume()
                                && liquidStorage.getOutputMetal() == metal);
    }

    @Override
    public int ejectLiquidMetal(Metal metal, int volume) {
        if (canEjectLiquidMetal(metal, 1)) {
            int volumeToRemove = Math.min(volume, liquidStorage.getVolume());
            liquidStorage.removeLiquid(volumeToRemove);
            updateGui(UPDATE_LIQUID);
            Bids.LOG.debug("Crucible ejected " + volumeToRemove + " of " + metal.name);
            if (liquidStorage.getVolume() == 0) {
                liquidTemp = 0;
                updateGui(UPDATE_TEMP);
            }
            return volumeToRemove;
        }
        return 0;
    }

    @Override
    public int acceptLiquidMetal(Metal metal, int volume, float temp) {
        if (canAcceptLiquidMetal(metal, 1, temp)) {
            float prevLiquidStorageHeatCapacity = liquidStorage.getHeatCapacity();
            int volumeToAdd = Math.min(volume, getMaxVolume() - liquidStorage.getVolume());
            liquidStorage.addLiquid(metal, volumeToAdd);
            updateGui(UPDATE_LIQUID);
            Bids.LOG.debug("Crucible accepted " + volumeToAdd + " of " + metal.name);
            float addedMetalHeatCapacity = CrucibleHelper.getHeatCapacity(new ItemStack(metal.ingot)) * volume;
            liquidTemp = combineTemp(temp, addedMetalHeatCapacity, liquidTemp,
                    prevLiquidStorageHeatCapacity);
            updateGui(UPDATE_TEMP);
            Bids.LOG.debug("Added metal changed liquid temp to " + liquidTemp);
            return volumeToAdd;
        }
        return 0;
    }

    @Override
    public float getLiquidMetalTemp() {
        return liquidTemp;
    }

    @Override
    public int getLiquidMetalVolume() {
        return liquidStorage.getVolume();
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        if (!useUpdateMask) {
            // When we are not using the update mask
            // it means that a regular init packet is requested
            // so we want to update everything
            updateMask = UPDATE_ALL;
        }
        // System.out.println("Update: " + updateMask + " useMask: " + useUpdateMask);

        NBTTagCompound tagCompound = new NBTTagCompound();
        if (updateMask != 0) {
            tagCompound.setByte("updateMask", updateMask);
            if ((updateMask & UPDATE_TEMP) != 0) {
                tagCompound.setInteger("combinedTemp", combinedTemp);
            }
            if ((updateMask & UPDATE_LIQUID) != 0) {
                liquidStorage.writeToNBT(tagCompound);
            }
            if ((updateMask & UPDATE_FLAGS) != 0) {
                tagCompound.setByte("flags", flags);
            }
            if ((updateMask & UPDATE_OUTPUT) != 0) {
                tagCompound.setBoolean("isOutputAvailable", isOutputAvailable);
                if (isOutputAvailable)
                    tagCompound.setString("output", output);
            }
            if ((updateMask & UPDATE_ALLOY_MIXING) != 0) {
                tagCompound.setInteger("alloyMixingCountdown", alloyMixingCountdown);
            }
            if ((updateMask & UPDATE_GLASS_MAKING) != 0) {
                tagCompound.setLong("glassMakingCompletedTicks", glassMakingCompletedTicks);
            }
            updateMask = 0;
            useUpdateMask = false;
        }
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tagCompound);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tagCompound = pkt.func_148857_g();
        updateMask = tagCompound.getByte("updateMask");
        // System.out.println("Updating: " + updateMask);
        if ((updateMask & UPDATE_TEMP) != 0) {
            combinedTemp = tagCompound.getInteger("combinedTemp");
        }
        if ((updateMask & UPDATE_LIQUID) != 0) {
            liquidStorage.readFromNBT(tagCompound);
        }
        if ((updateMask & UPDATE_FLAGS) != 0) {
            flags = tagCompound.getByte("flags");
        }
        if ((updateMask & UPDATE_OUTPUT) != 0) {
            isOutputAvailable = tagCompound.getBoolean("isOutputAvailable");
            if (isOutputAvailable)
                output = tagCompound.getString("output");
        }
        if ((updateMask & UPDATE_ALLOY_MIXING) != 0) {
            alloyMixingCountdown = tagCompound.getInteger("alloyMixingCountdown");
        }
        if ((updateMask & UPDATE_GLASS_MAKING) != 0) {
            glassMakingCompletedTicks = tagCompound.getLong("glassMakingCompletedTicks");
            onGlassMakingStatusChanged();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        writeCrucibleDataToNBT(tag);
    }

    public void writeCrucibleDataToNBT(NBTTagCompound tag) {
        tag.setInteger("solidTemp", (int) Math.floor(solidTemp));
        tag.setInteger("liquidTemp", (int) Math.floor(liquidTemp));

        NBTTagList storageTags = new NBTTagList();
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] != null) {
                NBTTagCompound slotTag = new NBTTagCompound();
                slotTag.setByte("Slot", (byte) i);
                storage[i].writeToNBT(slotTag);
                storageTags.appendTag(slotTag);
            }
        }
        tag.setTag("Storage", storageTags);

        tag.setLong("glassMakingCompletedTicks", glassMakingCompletedTicks);

        liquidStorage.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readCrucibleDataFromNBT(tag);
    }

    public void readCrucibleDataFromNBT(NBTTagCompound tag) {
        solidTemp = tag.getInteger("solidTemp");
        liquidTemp = tag.getInteger("liquidTemp");

        for (int i = 0; i < storage.length; i++) {
            storage[i] = null;
        }

        NBTTagList storageTags = tag.getTagList("Storage", 10);
        for (int i = 0; i < storageTags.tagCount(); i++) {
            NBTTagCompound slotTag = storageTags.getCompoundTagAt(i);
            byte slot = slotTag.getByte("Slot");
            if (slot >= 0 && slot < storage.length) {
                storage[slot] = ItemStack.loadItemStackFromNBT(slotTag);
            }
        }

        glassMakingCompletedTicks = tag.getLong("glassMakingCompletedTicks");

        liquidStorage.readFromNBT(tag);

        isOutputDirty = true;
    }

    @Override
    public void onSlotChanged(Slot slot) {
        if (!worldObj.isRemote) {
            if (slot.slotNumber < getInputSlotCount()) {
                onInputSlotChanged();
            } else if (slot.slotNumber == getInputSlotCount()) {
                onLiquidInputSlotChanged();
            } else if (slot.slotNumber == getInputSlotCount() + 1) {
                onLiquidOutputSlotChanged();
            }

            checkAlloyMixing();
        }
    }

    @Override
    public void onPickupFromSlot(Slot slot, EntityPlayer player, ItemStack itemStack) {
        if (slot.slotNumber == getInputSlotCount() + 1) {
            onPickupFromLiquidOutputSlot(player, itemStack);
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            doWork(solidHeatingTimer.tick(), liquidHeatingTimer.tick(), liquidInputTimer.tick(),
                    liquidOutputTimer.tick(), alloyMixingTimer.tick());

            if (isOutputDirty) {
                updateOutput();
                isOutputDirty = false;
            }

            if (alloyMixingTimer.getTicksToGo() > 0) {
                updateAlloyMixingCountdown();
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return getInputSlotCount() + 2;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return storage[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (storage[i] != null) {
            if (storage[i].stackSize <= j) {
                ItemStack itemstack = storage[i];
                storage[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = storage[i].splitStack(j);
            if (storage[i].stackSize == 0)
                storage[i] = null;
            return itemstack1;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return storage[i];
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        storage[i] = itemstack;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (i < getInputSlotCount()) {
            return isItemStackValidInput(itemstack);
        } else {
            switch (i - getInputSlotCount()) {
                case 0:
                    return isItemStackValidLiquidInput(itemstack);

                case 1:
                    return isItemStackValidLiquidOutput(itemstack);
            }

            return false;
        }
    }

    private float getHeatSourceTemp() {
        TileEntity te = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if (te != null && te instanceof IHeatSourceTE) {
            return ((IHeatSourceTE) te).getHeatSourceTemp();
        } else {
            return 0;
        }
    }

    private void updateOutput() {
        String prevOutput = output;
        isOutputAvailable = false;

        CrucibleLiquidStorage predictedLiquidStorage = liquidStorage.copy();
        for (int i = 0; i < getInputSlotCount(); i++) {
            ItemStack is = getStackInSlot(i);
            if (is != null) {
                predictedLiquidStorage.addLiquid(CrucibleHelper.getMetalFromSmeltable(is),
                        CrucibleHelper.getMetalReturnAmount(is) * is.stackSize);
                isOutputAvailable |= true;
            }
        }

        outputMetal = predictedLiquidStorage.getOutputMetal();
        int outputVolume = predictedLiquidStorage.getVolume();

        if (isOutputAvailable && outputMetal != null) {
            output = outputVolume + " " + outputMetal.name;
        } else {
            output = null;
            outputMetal = Global.UNKNOWN;
        }

        if (prevOutput != output) {
            updateGui(UPDATE_OUTPUT);
        }
    }

    private void checkAlloyMixing() {
        // Alloy can only be mixed when we are not smelting,
        // or adding and removing liquid
        // To be fool-proof, only when all slots are empty
        if (inputMonitor.getItemCount() == 0
                && getStackInSlot(getInputSlotCount()) == null
                && getStackInSlot(getInputSlotCount() + 1) == null
                // Is there anything to be mixed?
                // We need 2 or more items, and the output cannot be null (UNKNOWN)
                // Also, the molten metal needs to be actually liquid at this time
                && liquidStorage.getItemCount() > 1
                && liquidStorage.getOutputMetal() != Global.UNKNOWN
                && liquidStorage.isAllLiquid(liquidTemp)) {
            // Do nothing if the timer countdown is already running
            if (alloyMixingTimer.getTicksToGo() == 0) {
                alloyMixingTimer.delay(30 * 20);
                Bids.LOG.debug("Alloy will be mixed in 30 seconds");
            }
        } else if (alloyMixingTimer.getTicksToGo() > 0) {
            Bids.LOG.debug("Alloy mixing was cancelled");
            alloyMixingTimer.delay(0);
        }

        updateAlloyMixingCountdown();
    }

    private void updateAlloyMixingCountdown() {
        int prevAlloyMixingCooldown = alloyMixingCountdown;
        alloyMixingCountdown = (int) Math.ceil(alloyMixingTimer.getTicksToGo() / 20f);
        if (prevAlloyMixingCooldown != alloyMixingCountdown) {
            updateGui(UPDATE_ALLOY_MIXING);
        }
    }

    @SideOnly(Side.CLIENT)
    private void onGlassMakingStatusChanged() {
        Bids.LOG.debug("Glass making progress: " + getGlassMakingProgress()
                + " active: " + isGlassMakingActive());

        if (isGlassMakingActive() && glassMakingChimney == null) {
            TileEntity chimney = CrucibleHelper.findValidGlassmakingStructureChimney(this);
            if (chimney != null) {
                glassMakingChimney = ChimneyHelper.getTopMostChimney(chimney);
            }
        } else if (!isGlassMakingActive() && glassMakingChimney != null) {
            glassMakingChimney = null;
        }
    }

    private void onInputSlotChanged() {
        // When an input slot changes
        // check if heat capacity changed
        // reset solid material temp and delay heating if needed
        float prevHeatCapacity = inputMonitor.getHeatCapacity();
        inputMonitor.makeDirty();
        if (prevHeatCapacity != inputMonitor.getHeatCapacity()) {
            if (solidTemp > 0) {
                Bids.LOG.debug("Heating solid materials interrupted, will resume in 5s");
                solidTemp = 0;
                updateGui(UPDATE_TEMP);
            }

            isOutputDirty = true;

            solidHeatingTimer.delay(100);
        }

        if (canSmelt())
            setFlags(FLAGS_SMELTING);
        else
            clearFlags(FLAGS_SMELTING);

        // Making sure other indicators are off
        // while solid input is present
        if (inputMonitor.getItemCount() > 0) {
            clearFlags(FLAGS_LIQUID_IN);
            clearFlags(FLAGS_LIQUID_OUT);
        } else {
            if (canAcceptLiquid())
                setFlags(FLAGS_LIQUID_IN);
            else if (canEjectLiquid())
                setFlags(FLAGS_LIQUID_OUT);
        }

        updateFlags();
    }

    private void onLiquidInputSlotChanged() {
        // Wait a bit before accepting liquid
        liquidInputTimer.delay(20);

        if (canAcceptLiquid())
            setFlags(FLAGS_LIQUID_IN);
        else
            clearFlags(FLAGS_LIQUID_IN);

        // Making sure liquid output indicator is off
        // while accepting liquid
        if (canAcceptLiquid())
            clearFlags(FLAGS_LIQUID_OUT);
        else if (canEjectLiquid())
            setFlags(FLAGS_LIQUID_OUT);

        updateFlags();
    }

    private void onLiquidOutputSlotChanged() {
        // Wait a bit before ejecting liquid
        liquidOutputTimer.delay(20);

        if (canEjectLiquid())
            setFlags(FLAGS_LIQUID_OUT);
        else
            clearFlags(FLAGS_LIQUID_OUT);
        updateFlags();
    }

    private void onPickupFromLiquidOutputSlot(EntityPlayer player, ItemStack itemStack) {
        // Trigger the copper age achievement when a full
        // tool mold is removed from the output slot
        if (CrucibleHelper.isFullToolMold(itemStack)) {
            CrucibleHelper.triggerCopperAgeAchievement(player);
        }
    }

    private float combineTemp(float tempA, float heatCapacityA, float tempB, float heatCapacityB) {
        return (tempA * heatCapacityA + tempB * heatCapacityB) / (heatCapacityA + heatCapacityB);
    }

    private float getHeatingCurve(float hc) {
        return (float) (Math.sqrt(hc) + hc / 5) / 10;
    }

    private boolean isValidInputMetal(Metal inputMetal) {
        return inputMetal != Global.GARBAGE;
    }

    private boolean canSmelt() {
        return inputMonitor.getVolume() > 0
                && inputMonitor.getVolume() <= getMaxVolume() - liquidStorage.getVolume()
                && getHeatSourceTemp() > solidTemp;
    }

    private boolean canAcceptLiquid() {
        ItemStack is = getStackInSlot(getLiquidInputSlotIndex());
        return hasLiquidInputSlot()
                && inputMonitor.getVolume() == 0 && liquidStorage.getVolume() < getMaxVolume()
                && is != null
                && is.getItem() instanceof ItemMeltedMetal
                && CrucibleHelper.isMeltedAtTemp(is, getMaxTemp())
                && CrucibleHelper.isMeltedAtTemp(is, TFC_ItemHeat.getTemp(is))
                && isValidInputMetal(CrucibleHelper.getMetalFromItem(is.getItem()));
    }

    private boolean canEjectLiquid() {
        return inputMonitor.getVolume() == 0 && liquidStorage.getVolume() > 0
                && liquidStorage.isAllLiquid(liquidTemp)
                && storage[getLiquidOutputSlotIndex()] != null
                && CrucibleHelper.isValidMold(storage[getLiquidOutputSlotIndex()], liquidStorage.getOutputMetal())
                && !canAcceptLiquid();
    }

    private float enforceMinimumDelta(float delta, float max) {
        // We want to enforce minimum temp delta
        // to ensure the temp reaches equilibrium with the heat source
        // at some point instead of forever approaching it
        float minAbs = 0.001f;
        if (Math.abs(delta) < minAbs) {
            if (delta > 0)
                delta = Math.min(minAbs, max);
            else if (delta < 0)
                delta = Math.max(-minAbs, max);
        }
        return delta;
    }

    private void doWork(boolean updateSolidTemp, boolean updateLiquidTemp, boolean acceptLiquid, boolean ejectLiquid,
            boolean alloyMixing) {
        if (updateSolidTemp || updateLiquidTemp) {
            float heatSourceTemp = getHeatSourceTemp() * getHeatTransferEfficiency();

            if (updateLiquidTemp) {
                if (liquidStorage.getVolume() > 0
                        && (heatSourceTemp > liquidTemp && liquidTemp < getMaxTemp()
                                || heatSourceTemp < liquidTemp && liquidTemp > 0)) {
                    float deltaFromHeatSource = (heatSourceTemp - liquidTemp) / 1000
                            / getHeatingCurve(liquidStorage.getHeatCapacity());
                    if (heatSourceTemp > liquidTemp) {
                        deltaFromHeatSource *= BidsOptions.Crucible.liquidHeatingMultiplier;
                    } else {
                        deltaFromHeatSource *= BidsOptions.Crucible.coolingMultiplier;
                    }
                    float delta = enforceMinimumDelta(deltaFromHeatSource,
                            (heatSourceTemp - liquidTemp) / heatingMult);
                    liquidTemp += delta * heatingMult;
                    liquidTemp = Math.min(Math.max(liquidTemp, 0), getMaxTemp());
                    Bids.LOG.debug("Liquid temp updated to " + liquidTemp
                            + " (heat source: " + deltaFromHeatSource + ")");
                }
            }

            if (updateSolidTemp) {
                if (inputMonitor.getVolume() > 0
                        && inputMonitor.getVolume() <= getMaxVolume() - liquidStorage.getVolume()
                        && (heatSourceTemp > solidTemp && solidTemp < getMaxTemp()
                                || heatSourceTemp < solidTemp && solidTemp > 0)) {
                    float deltaFromHeatSource = ((heatSourceTemp - solidTemp) / 1000
                            / getHeatingCurve(inputMonitor.getHeatCapacity()));
                    float deltaFromLiquid = liquidStorage.getVolume() > 0
                            ? ((liquidTemp - solidTemp) / 1000
                                    / getHeatingCurve(inputMonitor.getHeatCapacity()))
                                    / ((inputMonitor.getHeatCapacity() + liquidStorage.getHeatCapacity())
                                            / liquidStorage.getHeatCapacity())
                            : 0;
                    if (heatSourceTemp > solidTemp) {
                        deltaFromHeatSource *= BidsOptions.Crucible.solidHeatingMultiplier;
                        deltaFromLiquid *= BidsOptions.Crucible.solidHeatingFromLiquidBonusMultiplier;
                    } else {
                        deltaFromHeatSource *= BidsOptions.Crucible.coolingMultiplier;
                        deltaFromLiquid *= BidsOptions.Crucible.coolingMultiplier;
                    }
                    float delta = deltaFromHeatSource + deltaFromLiquid;
                    // Apply heating bonus based on purity
                    // 0.5 is default speed, 1.0 is double
                    if (heatSourceTemp > solidTemp)
                        delta *= inputMonitor.getPurity() * 2;
                    delta = enforceMinimumDelta(delta,
                            (heatSourceTemp - solidTemp) / heatingMult);
                    solidTemp += delta * heatingMult;
                    solidTemp = Math.min(Math.max(solidTemp, 0), getMaxTemp());
                    Bids.LOG.debug("Solid temp updated to " + solidTemp
                            + " (heat source: " + deltaFromHeatSource
                            + ", liquid: " + deltaFromLiquid + ")"
                            + ", purity: " + inputMonitor.getPurity());
                }
            }

            if (updateSolidTemp || updateLiquidTemp) {
                int prevCombinedTemp = combinedTemp;
                combinedTemp = (int) Math.floor(inputMonitor.getVolume() > 0 ? solidTemp : liquidTemp);
                if (prevCombinedTemp != combinedTemp) {
                    updateGui(UPDATE_TEMP);
                }
            }

            if (updateSolidTemp) {
                // Glass requires very high temperatures to melt
                // and we achieve this by enclosing the crucible inside a solid structure
                // This is only for glass, and it doesn't make sense for other things
                boolean glassCanBeCreated = false;
                if (glassMakingCompletedTicks == 0
                        && outputMetal == Global.GLASS && heatSourceTemp > 1050 && solidTemp > 1050
                        && liquidStorage.isAllLiquid(liquidTemp)
                        && CrucibleHelper.findValidGlassmakingStructureChimney(this) != null) {
                    glassMakingCompletedTicks = TFC_Time.getTotalTicks() + GLASS_MAKING_TICKS;
                    Bids.LOG.debug("Glassmaking started");
                    updateGui(UPDATE_GLASS_MAKING);
                } else if (glassMakingCompletedTicks > 0
                        && CrucibleHelper.findValidGlassmakingStructureChimney(this) == null) {
                    // Only keep checking the structure
                    // Temperature would drop if the forge runs out of fuel
                    glassMakingCompletedTicks = 0;
                    Bids.LOG.debug("Glassmaking interrupted");
                    updateGui(UPDATE_GLASS_MAKING);
                } else if (glassMakingCompletedTicks > TFC_Time.getTotalTicks()) {
                    glassMakingCompletedTicks = 0;
                    glassCanBeCreated = true;
                    Bids.LOG.debug("Glassmaking complete");
                    updateGui(UPDATE_GLASS_MAKING);
                }

                // See if we can melt any input materials
                // but first make sure the liquid materials are not solid
                // This is to avoid adding melted Tin to warm, but solidified Copper
                // Glass ingredients will melt all at once when glassmaking
                // Also when glassmaking we ignore the temperature of any liquid glass
                // already present in the crucible
                if (glassCanBeCreated || liquidStorage.isAllLiquid(liquidTemp)) {
                    for (int i = 0; i < getInputSlotCount(); i++) {
                        ItemStack is = getStackInSlot(i);
                        if (is != null) {
                            if (CrucibleHelper.isMeltedAtTemp(is, solidTemp)
                                    && !CrucibleHelper.isOreIron(is)
                                    && !CrucibleHelper.isGlassIngredient(is)
                                    || glassCanBeCreated && CrucibleHelper.isGlassIngredient(is)) {
                                float prevLiquidStorageHeatCapacity = liquidStorage.getHeatCapacity();
                                Bids.LOG.debug("Input item stack " + is.getUnlocalizedName() + "[" + is.stackSize
                                        + "] melted");
                                liquidStorage.addLiquid(CrucibleHelper.getMetalFromSmeltable(is),
                                        CrucibleHelper.getMetalReturnAmount(is) * is.stackSize);

                                decrStackSize(i, is.stackSize); // won't trigger onSlotChanged, nice!
                                inputMonitor.makeDirty();

                                // Combine melted stack temp with liquid temp
                                float meltedStackHeatCapacity = CrucibleHelper.getHeatCapacity(is)
                                        * CrucibleHelper.getMetalReturnAmount(is) * is.stackSize;
                                liquidTemp = combineTemp(solidTemp, meltedStackHeatCapacity, liquidTemp,
                                        prevLiquidStorageHeatCapacity);
                                Bids.LOG.debug("Melted item changed liquid temp to " + liquidTemp);

                                // Reset input material temp once empty
                                if (inputMonitor.getVolume() == 0) {
                                    solidTemp = 0;

                                    // Turn off smelting indicator
                                    // and turn on liquid output indicator if there is a mold already
                                    // and start ejecting after a delay
                                    clearFlags(FLAGS_SMELTING);
                                    if (canEjectLiquid()) {
                                        ejectLiquid = false;
                                        liquidOutputTimer.delay(20);
                                        setFlags(FLAGS_LIQUID_OUT);
                                    }

                                    // Also update/clear output display
                                    updateOutput();

                                    // When glass is made, set fixed liquid temperature
                                    if (glassCanBeCreated) {
                                        liquidTemp = Global.GLASS.getMeltingPoint();
                                    }

                                    // Mix glass immediately
                                    if (liquidStorage.getOutputMetal() == Global.GLASS && liquidStorage.mixAlloy()) {
                                        Bids.LOG.debug("Liquid metal has been mixed into: "
                                                + liquidStorage.getOutputMetal().name);
                                        updateGui(UPDATE_LIQUID);
                                    }
                                }

                                updateGui(UPDATE_LIQUID | UPDATE_TEMP);
                            }
                        }
                    }
                }
            }

            if (updateLiquidTemp) {
                boolean solidified = !liquidStorage.isAllLiquid(liquidTemp);
                if (solidified)
                    setFlags(FLAGS_SOLIDIFIED);
                else
                    clearFlags(FLAGS_SOLIDIFIED);
            }
        }

        if (acceptLiquid) {
            // Only allow liquid items with melting point lower then crucible max temp
            if (hasLiquidInputSlot()
                    && inputMonitor.getVolume() == 0 && liquidStorage.getVolume() < getMaxVolume()) {
                ItemStack liquidInputStack = getStackInSlot(getLiquidInputSlotIndex());
                if (liquidInputStack != null && liquidInputStack.getItem() instanceof ItemMeltedMetal
                        && CrucibleHelper.isMeltedAtTemp(liquidInputStack, getMaxTemp())
                        && CrucibleHelper.isMeltedAtTemp(liquidInputStack, TFC_ItemHeat.getTemp(liquidInputStack))) {
                    Metal inputMetal = CrucibleHelper.getMetalFromItem(liquidInputStack.getItem());
                    if (isValidInputMetal(inputMetal)) {
                        int newDamage = liquidInputStack.getItemDamage() + 1;
                        int maxDamage = liquidInputStack.getMaxDamage() - 1;

                        int volumeAccepted = 1;
                        if (newDamage >= maxDamage) {
                            storage[getLiquidInputSlotIndex()] = new ItemStack(TFCItems.ceramicMold, 1, 1);
                            volumeAccepted++;

                            // Turn off liquid input indicator
                            // and turn on liquid output indicator if there is a mold already
                            // and start ejecting after a delay
                            clearFlags(FLAGS_LIQUID_IN);
                            if (canEjectLiquid()) {
                                ejectLiquid = false;
                                liquidOutputTimer.delay(20);
                                setFlags(FLAGS_LIQUID_OUT);
                            }
                        } else {
                            liquidInputStack.setItemDamage(newDamage);
                        }

                        float prevLiquidStorageHeatCapacity = liquidStorage.getHeatCapacity();
                        Bids.LOG.debug("Liquid metal " + inputMetal + " accepted");
                        liquidStorage.addLiquid(inputMetal, volumeAccepted);

                        // Combine melted stack temp with liquid temp
                        float addedLiquidHeatCapacity = CrucibleHelper.getHeatCapacity(liquidInputStack);
                        float addedLiquidTemp = TFC_ItemHeat.getTemp(liquidInputStack);
                        liquidTemp = combineTemp(addedLiquidTemp, addedLiquidHeatCapacity, liquidTemp,
                                prevLiquidStorageHeatCapacity);
                        Bids.LOG.debug("Accepted liquid metal changed liquid temp to " + liquidTemp);

                        updateGui(UPDATE_LIQUID | UPDATE_TEMP);
                    }
                }
            }
        }

        if (ejectLiquid) {
            // Only allow ejecting liquid when the input storage is empty
            // and there is anything to eject
            // and it's melted
            if (inputMonitor.getVolume() == 0 && liquidStorage.getVolume() > 0
                    && liquidStorage.isAllLiquid(liquidTemp) && !canAcceptLiquid()) {
                ItemStack liquidOutputStack = storage[getLiquidOutputSlotIndex()];
                if (liquidOutputStack != null
                        && CrucibleHelper.isValidMold(liquidOutputStack, liquidStorage.getOutputMetal())) {
                    Bids.LOG.debug("Ejecting 1 unit of: " + liquidStorage.getOutputMetal().name);

                    ItemStack newMold = CrucibleHelper.fillMold(liquidOutputStack, liquidStorage.getOutputMetal(), 1);
                    TFC_ItemHeat.setTemp(newMold, liquidTemp);

                    // For some molds, when we are 2 units away from full
                    // the mold gets filled up with only 1 unit added
                    // Not sure why this happens but it means we cannot assume only 1 unit is added
                    // when we try to add one unit
                    // So we remove whatever amount of units was trully added
                    int prevUnits = CrucibleHelper.getMoldUnits(liquidOutputStack);
                    int newUnits = CrucibleHelper.getMoldUnits(newMold);
                    liquidStorage.removeLiquid(newUnits - prevUnits);

                    storage[getLiquidOutputSlotIndex()] = newMold;

                    if (liquidStorage.getVolume() == 0
                            || !CrucibleHelper.isValidMold(newMold, liquidStorage.getOutputMetal())) {
                        // No more liquid to eject
                        // or t mold is full so remove the output indicator
                        clearFlags(FLAGS_LIQUID_OUT);
                    }

                    updateGui(UPDATE_LIQUID);
                }

            }
        }

        if (alloyMixing) {
            // This is when the alloy mixing countdown expired
            // Make sure the metal is still liquid
            if (liquidStorage.isAllLiquid(liquidTemp) && liquidStorage.mixAlloy()) {
                Bids.LOG.debug("Liquid metal has been mixed into: " + liquidStorage.getOutputMetal().name);
            } else {
                Bids.LOG.debug("Liquid metal has not been not mixed");
            }

            updateGui(UPDATE_LIQUID);
            updateAlloyMixingCountdown();
        } else if (updateLiquidTemp) {
            // See if alloying countdown might need to be started or stopped
            // when temp changes
            checkAlloyMixing();
        }

        updateFlags();
    }

    private void setFlags(byte mask) {
        flags |= mask;
    }

    private void clearFlags(byte mask) {
        flags &= ~mask;
    }

    private void updateFlags() {
        if (flags != flagsPreviouslyUpdated) {
            flagsPreviouslyUpdated = flags;
            updateGui(UPDATE_FLAGS);
        }
    }

    private void updateGui(int mask) {
        updateMask |= mask;
        useUpdateMask = true;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private int getLiquidInputSlotIndex() {
        return getInputSlotCount();
    }

    private int getLiquidOutputSlotIndex() {
        return getInputSlotCount() + 1;
    }

}
