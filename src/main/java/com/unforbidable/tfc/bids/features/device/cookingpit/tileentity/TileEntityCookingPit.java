package com.unforbidable.tfc.bids.features.device.cookingpit.tileentity;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingAccessory;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHost;
import com.unforbidable.tfc.bids.api.features.cooking.CookingLidUsage;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeInputTemplate;
import com.unforbidable.tfc.bids.api.meta.HeatingStoneMeta;
import com.unforbidable.tfc.bids.common.network.SimpleUpdatePacket;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingEngine;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingFoodTicker;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingFoodTickerResult;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingHelper;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingRecipeProgress;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitBounds;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitHelper;
import com.unforbidable.tfc.bids.features.material.heatingstone.item.ItemHeatingStone;
import com.unforbidable.tfc.bids.util.EntityScanner;
import com.unforbidable.tfc.bids.util.Timer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public abstract class TileEntityCookingPit extends TileEntity implements CookingHost, PacketHandler<SimpleUpdatePacket>, IInventory {

    private static final int MAX_STORAGE = 2;
    private static final int MAX_FLUIDS = 1;

    private static final int SLOT_INPUT = 0;
    private static final int SLOT_STONES = 1;

    private static final int FLUID_PRIMARY = 0;

    private static final int HEAT_CHECK_INTERVAL = 10;
    private static final int ITEM_TICK_INTERVAL = 50;

    private final ItemStack[] storage = new ItemStack[MAX_STORAGE];
    private final FluidStack[] fluids = new FluidStack[MAX_FLUIDS];

    private CookingHeatLevel heatLevel = CookingHeatLevel.NONE;
    private long heatedUntilTicks = 0;

    boolean clientNeedToUpdate = false;
    boolean clientDataLoaded = false;
    boolean inputItemSelected = false;

    private final CookingEngine cookingEngine = new CookingEngine(this);
    private final CookingFoodTicker cookingFoodTicker = new CookingFoodTicker(this, ITEM_TICK_INTERVAL);

    private final Timer heatCheckTimer = new Timer(HEAT_CHECK_INTERVAL);
    private final Timer itemTickTimer = new Timer(ITEM_TICK_INTERVAL);

    public boolean isClientDataLoaded() {
        return clientDataLoaded;
    }

    public CookingPitBounds getCachedBounds() {
        return CookingPitBounds.getBounds();
    }

    public void setInputItemSelected(boolean selected) {
        inputItemSelected = selected;
    }

    public boolean isInputItemSelected() {
        return inputItemSelected;
    }

    @Override
    public CookingHeatLevel getHeatLevel() {
        return heatLevel;
    }

    public long getHeatedUntilTicks() {
        return heatedUntilTicks;
    }

    public boolean hasInputItem() {
        return storage[SLOT_INPUT] != null;
    }

    @Override
    public ItemStack getInputItemStack() {
        return storage[SLOT_INPUT];
    }

    public int getTotalLiquidVolume() {
        return hasFluid() ? getFluidStack().amount : 0;
    }

    public abstract int getMaxFluidVolume();

    public boolean hasFluid() {
        return fluids[FLUID_PRIMARY] != null;
    }

    @Override
    public FluidStack getFluidStack() {
        return fluids[FLUID_PRIMARY];
    }

    public void onBreakBlock() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                    storage[i]);
                worldObj.spawnEntityInWorld(ei);

                storage[i] = null;
            }
        }
    }

    @Override
    public CookingRecipeInputTemplate createRecipeTemplate() {
        return new CookingRecipeInputTemplate(
            getFluidStack(), null,
            getInputItemStack(),
            CookingAccessory.NONE,
            CookingLidUsage.OFF,
            getHeatLevel(), getHeatLevel());
    }

    public CookingRecipeProgress getRecipeProgress() {
        return cookingEngine.getRecipeProgress();
    }

    @Override
    public void onItemChanged(ItemStack output) {
        storage[SLOT_INPUT] = output;

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();

        clientNeedToUpdate = true;
    }

    @Override
    public void onFluidChanged(FluidStack output, FluidStack secondaryOutput) {
        fluids[FLUID_PRIMARY] = output;

        if (output == null) {
            heatLevel = CookingHeatLevel.NONE;
            onHeatLevelChanged();
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();

        clientNeedToUpdate = true;
    }

    public void retrieveItemStack(EntityPlayer player) {
        if (hasInputItem()) {
            Bids.LOG.debug("Retrieving input item: " + storage[SLOT_INPUT].getDisplayName());
            giveItemStackToPlayer(player, storage[SLOT_INPUT]);
            storage[SLOT_INPUT] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            cookingEngine.onRecipeParametersChanged(true);
        }
    }

    public void retrieveStones(EntityPlayer player) {
        if (heatLevel == CookingHeatLevel.NONE && storage[SLOT_STONES] != null) {
            giveItemStackToPlayer(player, storage[SLOT_STONES]);
            storage[SLOT_STONES] = null;
        }
    }

    public boolean placeItemStack(ItemStack itemStack, EntityPlayer player) {
        // If there is already an input item, the items need to be able to form a stack
        if (hasInputItem()) {
            if (itemStack.getItem() instanceof ItemFoodTFC) {
                // Food cannot be stacked
                return false;
            } else if (!itemStack.isItemEqual(getInputItemStack()) || itemStack.getItemDamage() != getInputItemStack().getItemDamage()) {
                // Different items cannot be stacked
                return false;
            }
        }

        // For input items the stack size is determined based on the matching recipe
        int requiredStackSize = getInputItemStackSizeForRecipe(itemStack);
        if (requiredStackSize == 0) {
            // Stack size could not be determined based on the matching recipe,
            // or it was determined as 0
            // which means the item stack in fact cannot be placed
            return false;
        }
        Bids.LOG.debug("requiredStackSize: " + requiredStackSize);

        // Ensure the required stack size does not exceed max stack size
        int checkedRequiredStackSize = Math.min(requiredStackSize, itemStack.getMaxStackSize());
        Bids.LOG.debug("checkedRequiredStackSize: " + checkedRequiredStackSize);

        // Reduce target stack size by existing stack size
        int existingStackSize = hasInputItem() ? getInputItemStack().stackSize : 0;
        int targetStackSize = checkedRequiredStackSize - existingStackSize;
        Bids.LOG.debug("targetStackSize: " + targetStackSize);

        if (targetStackSize <= 0) {
            // The target stack size is already fulfilled
            return false;
        }

        // Consider the stack size that is available
        int consumedStackSize = Math.min(targetStackSize, itemStack.stackSize);
        Bids.LOG.debug("consumedStackSize: " + consumedStackSize);

        if (hasInputItem()) {
            storage[SLOT_INPUT].stackSize += consumedStackSize;
        } else {
            storage[SLOT_INPUT] = itemStack.copy();
            storage[SLOT_INPUT].stackSize = consumedStackSize;
        }

        itemStack.stackSize -= consumedStackSize;

        Bids.LOG.debug("Placed input item: " + storage[SLOT_INPUT].getDisplayName() + "[" + storage[SLOT_INPUT].stackSize + "]");

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        clientNeedToUpdate = true;

        // Cancel any recipe that may be in progress
        // otherwise instant recipes returning output immediately will not work
        //cancelRecipeProgress();

        // Process recipe right after item is placed
        // and remember the input item
        ItemStack before = storage[SLOT_INPUT].copy();
        //handleRecipeProgress();

        if (hasInputItem() && !ItemStack.areItemStacksEqual(before, storage[SLOT_INPUT])) {
            // If the input item changed
            // the recipe completed immediately
            // so return the crafted item immediately
            giveItemStackToPlayer(player, storage[SLOT_INPUT]);
            storage[SLOT_INPUT] = null;

            cookingEngine.onRecipeParametersChanged(true);
        }

        return true;
    }

    private void giveItemStackToPlayer(EntityPlayer player, ItemStack itemStack) {
        final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, itemStack);
        worldObj.spawnEntityInWorld(ei);
    }

    private int getInputItemStackSizeForRecipe(ItemStack itemStack) {
        if (CookingHelper.isCookableFoodItemStack(itemStack)) {
            // Food items default to stack size of 1
            return 1;
        } else {
            // Look for all recipes matching specific input item
            // as we don't know the final parameters for the recipes
            // and manually ensure certain parameters such as steaming mesh and existing liquid
            for (CookingRecipe recipe : CookingHelper.getRecipesMatchingInput(itemStack)) {
                // Ensure accessory matches
                if (recipe.getAccessory() != CookingAccessory.NONE) {
                    continue;
                }

                if (recipe.getInputFluidStack() != null) {
                    // Ensure existing liquid matches recipe input liquid
                    if (hasFluid() && !recipe.getInputFluidStack().isFluidEqual(getFluidStack())) {
                        continue;
                    }

                    // If recipe has secondary input liquid (e.g. fluid mixing)
                    // those recipes are completed instantly when item containers are used
                    if (recipe.getSecondaryInputFluidStack() != null) {
                        continue;
                    }

                    // If the recipe has both input and output item (e.g. soaking)
                    // we can only do one run
                    if (recipe.getOutputItemStack() != null) {
                        return 1;
                    }

                    // If input item is food
                    // we take the whole stack to be processed fully or partially
                    if (recipe.getInputItemStack().getItem() instanceof ItemFoodTFC) {
                        return 1;
                    }

                    // Recipes with fluid input (salt + fresh water)
                    // Stack size depends on how much input liquid is in the cooking pot
                    // If there is no input liquid (yet) assume max volume
                    int currentAmount = hasFluid() ? getFluidStack().amount : getMaxFluidVolume();
                    float runs = (float) currentAmount / recipe.getInputFluidStack().amount;
                    int count = (int) Math.ceil(runs);
                    int stackSize = count * recipe.getInputItemStack().stackSize;

                    Bids.LOG.debug("Determined input item stack size: " + stackSize + " for runs: " + runs + " and input amount: " + currentAmount);

                    return stackSize;
                } else if (recipe.getOutputFluidStack() != null) {
                    // Ensure existing liquid matches recipe output liquid
                    if (hasFluid() && !recipe.getOutputFluidStack().isFluidEqual(getFluidStack())) {
                        continue;
                    }

                    // If the recipe has both input and output item (creating liquid and changing solid item)
                    // we can only do one run
                    if (recipe.getOutputItemStack() != null) {
                        return 1;
                    }

                    // Recipes without fluid input (melting snow)
                    // Stack size depends on how much output liquid can fit in the cooking pot
                    // account for existing liquid
                    int currentAmount = hasFluid() ? getFluidStack().amount : 0;
                    int outputAmount = getMaxFluidVolume() - currentAmount;
                    float runs = (float) outputAmount / recipe.getOutputFluidStack().amount;
                    int count = (int) Math.floor(runs);
                    int stackSize = count * recipe.getInputItemStack().stackSize;

                    Bids.LOG.debug("Determined input item stack size: " + stackSize + " for runs: " + runs + " and output amount: " + outputAmount);

                    return stackSize;
                } else {
                    // Recipes without input or output liquid (turning solid item into another solid item)
                    // always one item
                    return 1;
                }
            }

            // No valid recipe found for the input item
            return 0;
        }
    }

    public boolean addLiquid(FluidStack inFS) {
        if (inFS != null && isValidFluid(inFS)) {
            if (fluids[FLUID_PRIMARY] == null) {
                // Cannot fill up from larger amount
                if (inFS.amount > getMaxFluidVolume()) {
                    return false;
                }

                fluids[FLUID_PRIMARY] = inFS.copy();
                inFS.amount = 0;
            } else {
                // check if the container would overflow or if the fluid being added does
                // not match the contained liquid
                if (fluids[FLUID_PRIMARY].amount + inFS.amount > getMaxFluidVolume() || !fluids[FLUID_PRIMARY].isFluidEqual(inFS)) {
                    return false;
                }

                fluids[FLUID_PRIMARY].amount += inFS.amount;
                inFS.amount = 0;
            }

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            cookingEngine.onRecipeParametersChanged(true);

            return true;
        }

        return false;
    }

    protected abstract boolean isValidFluid(FluidStack fluid);

    public ItemStack addLiquid(ItemStack is) {
        if (is == null || is.stackSize > 1) {
            return is;
        }

        if (FluidContainerRegistry.isFilledContainer(is)) {
            ItemStack draining = is.copy();
            boolean drained = false;
            while (FluidContainerRegistry.isFilledContainer(draining)) {
                FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(draining);
                if (addLiquid(fs)) {
                    drained = true;
                    draining = FluidContainerRegistry.drainFluidContainer(draining);
                    Bids.LOG.debug("Draining item stack: " + draining.getDisplayName() + "[" + draining.getItemDamage() + "]");
                } else {
                    break;
                }
            }

            if (drained) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
            return draining;
        } else if (is.getItem() instanceof IFluidContainerItem) {
            FluidStack isfs = ((IFluidContainerItem) is.getItem()).getFluid(is);
            if (addLiquid(isfs)) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                ((IFluidContainerItem) is.getItem()).drain(is, is.getMaxDamage(), true);
                return is;
            }
        }

        return is;
    }

    public ItemStack removeLiquid(ItemStack is) {
        if (is == null || is.stackSize > 1) {
            return is;
        }

        if (!hasFluid()) {
            return is;
        }

        FluidStack topFluidStack = getFluidStack();

        if (FluidContainerRegistry.isEmptyContainer(is)) {
            ItemStack out = FluidContainerRegistry.fillFluidContainer(topFluidStack, is);
            if (out != null) {
                removeLiquidAmount(FluidContainerRegistry.getFluidForFilledItem(out).amount);

                return out;
            }
        } else if (is.getItem() instanceof IFluidContainerItem) {
            FluidStack isfs = ((IFluidContainerItem) is.getItem()).getFluid(is);
            if (isfs == null || topFluidStack.isFluidEqual(isfs)) {
                int amount = ((IFluidContainerItem) is.getItem()).fill(is, topFluidStack, true);
                if (amount > 0) {
                    removeLiquidAmount(amount);
                }

                return is;
            }
        }

        return is;
    }

    private void removeLiquidAmount(int amount) {
        // Remove liquid from the top layer first
        fluids[FLUID_PRIMARY].amount -= amount;
        if (fluids[FLUID_PRIMARY].amount == 0) {
            fluids[FLUID_PRIMARY] = null;

            heatLevel = CookingHeatLevel.NONE;
            onHeatLevelChanged();
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        clientNeedToUpdate = true;

        cookingEngine.onRecipeParametersChanged(true);
    }

    public void drainLiquid() {
        if (hasFluid()) {
            fluids[FLUID_PRIMARY] = null;

            heatLevel = CookingHeatLevel.NONE;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            cookingEngine.onRecipeParametersChanged(true);
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage();

                clientNeedToUpdate = false;
            }

            if (heatCheckTimer.tick()) {
                findAndConsumeHeatingStones();
                checkRemainingHeat();
            }

            if (cookingEngine.update()) {
                // Sync data with the client
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
            }

            if (itemTickTimer.tick()) {
                handleInputItemTicking();
            }
        }
    }

    private void handleInputItemTicking() {
        CookingFoodTickerResult result = cookingFoodTicker.tick(getInputItemStack(), getFluidStack(), heatLevel, false, false, true);
        if (result.updated) {
            if (fluids[FLUID_PRIMARY].amount == 0) {
                fluids[FLUID_PRIMARY] = null;
            }

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();

            clientNeedToUpdate = true;
        }
    }

    private void checkRemainingHeat() {
        if (heatLevel != CookingHeatLevel.NONE) {
            if (heatedUntilTicks < TFC_Time.getTotalTicks()) {
                heatLevel = CookingHeatLevel.values()[heatLevel.ordinal() - 1];

                onHeatLevelChanged();

                if (heatLevel != CookingHeatLevel.NONE) {
                    heatedUntilTicks = TFC_Time.getTotalTicks() + CookingPitHelper.COOLING_DOWN_TICKS;
                }

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
            }
        }
    }

    private void findAndConsumeHeatingStones() {
        if (hasFluid()) {
            EntityScanner scanner = new EntityScanner(worldObj, xCoord, yCoord, zCoord);
            for (EntityItem ei : scanner.getEntities()) {
                if (ei.getEntityItem().getItem() instanceof ItemHeatingStone && ei.getEntityItem().getItemDamage() == HeatingStoneMeta.HOT) {
                    if (heatLevel == CookingHeatLevel.NONE) {
                        heatLevel = CookingHeatLevel.LOW;

                        onHeatLevelChanged();
                    } else if (heatLevel != getMaxHeatLevel()) {
                        long ticksRemaining = heatedUntilTicks - TFC_Time.getTotalTicks();
                        long ticksSpent = CookingPitHelper.TICKS_PER_HOT_STONE - ticksRemaining;
                        if (ticksSpent < CookingPitHelper.TICKS_SPENT_THRESHOLD_TO_ALLOW_INCREASE) {
                            heatLevel = CookingHeatLevel.values()[heatLevel.ordinal() + 1];

                            onHeatLevelChanged();
                        }
                    }

                    heatedUntilTicks = TFC_Time.getTotalTicks() + CookingPitHelper.TICKS_PER_HOT_STONE;

                    ei.setDead();

                    onHeatingStoneConsumed();

                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    markDirty();
                }
            }
        }
    }

    protected void onHeatingStoneConsumed() {
        if (storage[SLOT_STONES] == null) {
            storage[SLOT_STONES] = new ItemStack(BidsItems.heatingStone, 1, HeatingStoneMeta.COLD);
        } else {
            storage[SLOT_STONES].stackSize++;
        }
    }

    protected void onHeatLevelChanged() {
        Bids.LOG.debug("Heat level changed: " + heatLevel);

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

        cookingEngine.onRecipeParametersChanged(false);
    }

    protected abstract CookingHeatLevel getMaxHeatLevel();

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeDataToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        // This forces client render after NBT data is loaded
        // until then the cooking pit cannot be fully rendered
        if (worldObj.isRemote && !clientDataLoaded) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientDataLoaded = true;
        }

        readDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readDataFromNBT(tag);
    }

    public void writeDataToNBT(NBTTagCompound tag) {
        if (heatLevel != CookingHeatLevel.NONE) {
            tag.setString("heatLevel", heatLevel.name());
            tag.setLong("heatedUntilTicks", heatedUntilTicks);
        }

        if (hasInputItem()) {
            NBTTagList itemTagList = new NBTTagList();
            for (int i = 0; i < MAX_STORAGE; i++) {
                if (storage[i] != null) {
                    NBTTagCompound itemTag = new NBTTagCompound();
                    itemTag.setInteger("slot", i);
                    storage[i].writeToNBT(itemTag);
                    itemTagList.appendTag(itemTag);
                }
            }
            tag.setTag("storage", itemTagList);
        }

        if (hasFluid()) {
            NBTTagList fluidTagList = new NBTTagList();
            for (int i = 0; i < MAX_FLUIDS; i++) {
                if (fluids[i] != null) {
                    NBTTagCompound fluidTag = new NBTTagCompound();
                    fluidTag.setInteger("slot", i);
                    fluids[i].writeToNBT(fluidTag);
                    fluidTagList.appendTag(fluidTag);
                }
            }
            tag.setTag("fluids", fluidTagList);
        }

        cookingEngine.writeDataToNBT(tag);
    }

    public void readDataFromNBT(NBTTagCompound tag) {
        try {
            heatLevel = CookingHeatLevel.valueOf(tag.getString("heatLevel"));
        } catch (IllegalArgumentException e) {
            heatLevel = CookingHeatLevel.NONE;
        }

        if (heatLevel != CookingHeatLevel.NONE) {
            heatedUntilTicks = tag.getLong("heatedUntilTicks");
        } else {
            heatedUntilTicks = 0;
        }

        {
            for (int i = 0; i < MAX_STORAGE; i++) {
                storage[i] = null;
            }

            NBTTagList itemTagList = tag.getTagList("storage", 10);
            for (int i = 0; i < itemTagList.tagCount(); i++) {
                NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
                final int slot = itemTag.getInteger("slot");
                storage[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }

        {
            for (int i = 0; i < MAX_FLUIDS; i++) {
                fluids[i] = null;
            }

            NBTTagList fluidTagList = tag.getTagList("fluids", 10);
            for (int i = 0; i < fluidTagList.tagCount(); i++) {
                NBTTagCompound fluidTag = fluidTagList.getCompoundTagAt(i);
                final int slot = fluidTag.getInteger("slot");
                fluids[slot] = FluidStack.loadFluidStackFromNBT(fluidTag);
            }
        }

        cookingEngine.readDataFromNBT(tag);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return storage[SLOT_INPUT];
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if (itemStack == null) {
            if (storage[SLOT_INPUT] != null) {
                // Item has decayed out of existence
                storage[SLOT_INPUT] = null;

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;
            }
        } else {
            storage[SLOT_INPUT] = itemStack;
        }
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return false;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }

    @Override
    public void handleNetworkPacket(SimpleUpdatePacket packet) {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        Bids.LOG.debug("Client updated at: [{},{},{}]", xCoord, yCoord, zCoord);
    }

    public void sendUpdateMessage() {
        Network.sendToTileEntity(new SimpleUpdatePacket(), this);
        Bids.LOG.debug("Sent update message");
    }

}
