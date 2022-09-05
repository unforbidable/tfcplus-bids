package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Sounds;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Blocks.BlockWorkStone;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.Crafting.SaddleQuernManager;
import com.unforbidable.tfc.bids.api.Crafting.SaddleQuernRecipe;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntitySaddleQuern extends TileEntity implements IInventory {

    public static final int MAX_STORAGE = 3;

    public static final int SLOT_WORK_STONE = 0;
    public static final int SLOT_INPUT_STACK = 1;
    public static final int SLOT_OUTPUT_STACK = 2;

    private static final int OPERATION_TICKS = 70;

    private static final float MAX_GRIND_WEIGHT = 4;

    public static enum Selection {
        NONE, INPUT_STACK, WORK_STONE
    };

    ItemStack[] storage = new ItemStack[MAX_STORAGE];
    int orientation = 0;
    boolean isWorking = false;

    Timer cancelOperationTimer = new Timer(0);
    Timer operationTimer = new Timer(0);
    Timer decayTimer = new Timer(100);

    float workStonePosition = 0;
    int workStoneStage = 0;
    Selection selection = Selection.NONE;

    public TileEntitySaddleQuern() {
        super();
    }

    public ItemStack getBaseStone() {
        return new ItemStack(getBlockType(), 1, getBlockMetadata());
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public ItemStack getSelectedItemStack() {
        switch (selection) {
            case INPUT_STACK:
                return storage[SLOT_INPUT_STACK];

            case WORK_STONE:
                return storage[SLOT_WORK_STONE];

            default:
                return null;
        }
    }

    public boolean setWorkStone(ItemStack workStone) {
        if (hasWorkStone() || !isValidWorkStone(workStone)) {
            return false;
        }

        ItemStack is = workStone.copy();
        is.stackSize = 1;
        storage[SLOT_WORK_STONE] = is;

        workStone.stackSize--;

        updateClient();

        return true;
    }

    public ItemStack getWorkStone() {
        return storage[SLOT_WORK_STONE];
    }

    public boolean hasWorkStone() {
        return getWorkStone() != null;
    }

    public float getWorkStonePosition() {
        return workStonePosition;
    }

    public boolean retrieveWorkStone(EntityPlayer player) {
        if (!hasWorkStone() || isInOperation()) {
            return false;
        }

        final ItemStack is = storage[SLOT_WORK_STONE];
        final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, is);
        worldObj.spawnEntityInWorld(ei);

        Bids.LOG.info("Work stone retrieved from saddle quern: " + is.getDisplayName());

        storage[SLOT_WORK_STONE] = null;

        updateClient();

        return true;
    }

    public boolean isValidWorkStone(ItemStack itemStack) {
        return Block.getBlockFromItem(itemStack.getItem()) instanceof BlockWorkStone;
    }

    public boolean setInputStack(ItemStack inputStack) {
        if (hasInputStack() && !canInputStack(inputStack) || !isValidInput(inputStack) || isInOperation()) {
            return false;
        }

        if (!hasInputStack()) {
            storage[SLOT_INPUT_STACK] = inputStack.copy();
            inputStack.stackSize = 0;
        } else {
            int maxStackSize = inputStack.getMaxStackSize();
            int newStackSize = storage[SLOT_INPUT_STACK].stackSize + inputStack.stackSize;
            if (newStackSize > maxStackSize) {
                storage[SLOT_INPUT_STACK].stackSize = maxStackSize;
                inputStack.stackSize = newStackSize - maxStackSize;
            } else {
                storage[SLOT_INPUT_STACK].stackSize = newStackSize;
                inputStack.stackSize = 0;
            }
        }

        updateClient();

        return true;
    }

    public boolean isValidInput(ItemStack inputStack) {
        return SaddleQuernManager.hasMatchingRecipe(inputStack);
    }

    private boolean canInputStack(ItemStack inputStack) {
        return storage[SLOT_INPUT_STACK].getItem() == inputStack.getItem();
    }

    public boolean hasInputStack() {
        return getInputStack() != null;
    }

    public ItemStack getInputStack() {
        return storage[SLOT_INPUT_STACK];
    }

    public boolean retrieveInputStack(EntityPlayer player) {
        if (!hasInputStack() || isInOperation()) {
            return false;
        }

        final ItemStack is = storage[SLOT_INPUT_STACK];
        final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, is);
        worldObj.spawnEntityInWorld(ei);

        Bids.LOG.info("Input stack retrieved from saddle quern: " + is.getDisplayName());

        storage[SLOT_INPUT_STACK] = null;

        updateClient();

        return true;
    }

    public boolean operate() {
        if (!hasWorkStone()) {
            return false;
        }

        if (!isWorking && !isInOperation()) {
            Bids.LOG.debug("Quern hand stone starts being used");

            isWorking = true;

            worldObj.playSoundEffect(xCoord, yCoord, zCoord, TFC_Sounds.STONEDRAG, 1, 1);

            updateClient();

            operationTimer.delay(OPERATION_TICKS);
        }

        cancelOperationTimer.delay(5);

        return true;
    }

    private boolean isInOperation() {
        return operationTimer.getTicksToGo() > 0;
    }

    private void updateClient() {
        if (worldObj != null && !worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (cancelOperationTimer.tick()) {
                Bids.LOG.debug("Quern hand stone is no longer used");

                isWorking = false;

                updateClient();
            }

            if (decayTimer.tick()) {
                TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord, false);
            }

            if (operationTimer.tick()) {
                if (isWorking) {
                    worldObj.playSoundEffect(xCoord, yCoord, zCoord, TFC_Sounds.STONEDRAG, 1, 1);

                    operationTimer.delay(OPERATION_TICKS);
                }
            }

            if (operationTimer.getTicksToGo() == OPERATION_TICKS / 2) {
                processInput();
                ejectOutput();
            }

        } else {
            if (isWorking || workStoneStage > 0) {
                workStoneStage++;

                final int half = OPERATION_TICKS / 2;
                workStonePosition = (half - Math.abs(workStoneStage - half)) / (float) half;

                Bids.LOG.debug("Quern hand stone position: " + workStonePosition + ", stage: " + workStoneStage);

                if (workStoneStage >= OPERATION_TICKS) {
                    workStoneStage = 0;
                }
            }
        }
    }

    private void processInput() {
        Bids.LOG.debug("Quern works!");

        if (storage[SLOT_INPUT_STACK] != null && storage[SLOT_OUTPUT_STACK] == null) {
            ItemStack input = storage[SLOT_INPUT_STACK];
            SaddleQuernRecipe recipe = SaddleQuernManager.getMatchingRecipe(input);
            if (recipe != null) {
                ItemStack output = recipe.getCraftingResult();

                if (input.getItem() instanceof IFood) {
                    float inputDecay = Food.getDecay(input);
                    float inputWeight = Food.getWeight(input);

                    float outputWeight = Math.min(MAX_GRIND_WEIGHT, inputWeight);
                    float outputDecay = inputDecay / Global.FOOD_MAX_WEIGHT * outputWeight;

                    ItemFoodTFC.createTag(output, outputWeight, outputDecay);

                    if (outputWeight != inputWeight) {
                        Food.setWeight(storage[SLOT_INPUT_STACK], inputWeight - outputWeight);
                        Food.setDecay(storage[SLOT_INPUT_STACK], inputDecay - outputDecay);
                    } else {
                        storage[SLOT_INPUT_STACK] = null;
                    }

                    storage[SLOT_OUTPUT_STACK] = output;

                    Bids.LOG.debug("Output: " + output.getDisplayName() + " weight: " + Food.getWeight(output));

                    updateClient();
                } else {
                    Bids.LOG.warn("Only food stuffs are supported");
                }
            } else {
                Bids.LOG.warn("Recipe not found for: " + input.getDisplayName());
            }
        }
    }

    private void ejectOutput() {
        if (storage[SLOT_OUTPUT_STACK] != null) {
            ForgeDirection d = getOutputForgeDirection();

            ItemStack output = storage[SLOT_OUTPUT_STACK];
            final EntityItem ei = new EntityItem(worldObj, xCoord + d.offsetX + 0.5, yCoord,
                    zCoord + d.offsetZ + 0.5, output);
            ei.delayBeforeCanPickup = 30;
            worldObj.spawnEntityInWorld(ei);

            storage[SLOT_OUTPUT_STACK] = null;

            updateClient();
        }
    }

    private ForgeDirection getOutputForgeDirection() {
        switch (orientation) {
            case 0:
                return ForgeDirection.NORTH;

            case 1:
                return ForgeDirection.EAST;

            case 2:
                return ForgeDirection.SOUTH;

            case 3:
                return ForgeDirection.WEST;
        }

        return ForgeDirection.UNKNOWN;
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

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
        tag.setInteger("orientation", orientation);
        tag.setBoolean("isWorking", isWorking);

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

    public void readDataFromNBT(NBTTagCompound tag) {
        orientation = tag.getInteger("orientation");
        isWorking = tag.getBoolean("isWorking");

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

    @Override
    public int getSizeInventory() {
        return MAX_STORAGE;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        // This is called by TFC to retrieve items for decay calc etc
        return storage[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        // This is called by TFC to return items after decay calc etc
        if (itemStack == null) {
            if (storage[slot] != null) {
                // Item has decayed out of existence
                storage[slot] = null;

                updateClient();
            }
        } else {
            if (storage[slot] != null) {
                storage[slot] = itemStack;
            } else {
                // This should not happen
                Bids.LOG.warn("TFC returned an item after decay calculation into a slot that is empty.");
            }
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

}
