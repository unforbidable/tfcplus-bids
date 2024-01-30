package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressDiscPosition;
import com.unforbidable.tfc.bids.Core.ScrewPress.ScrewPressHelper;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.Crafting.ScrewPressManager;
import com.unforbidable.tfc.bids.api.Crafting.ScrewPressRecipe;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class TileEntityScrewPressBarrel extends TileEntity  implements IMessageHanldingTileEntity<TileEntityUpdateMessage>, IInventory {

    private static final int MAX_STORAGE = 10;
    private static final int SLOT_INPUT_START = 0;
    private static final int SLOT_OUTPUT_START = 5;

    private static final int MAX_FLUIDS = 1;
    private static final int FLUID_OUTPUT = 0;

    private static final int MAX_OUTPUT_FLUID = 8000;

    // Value 220 makes pressing apples and olives require roughly 5 full 18 to 9 gear step-downs (1/(2^5) spin)
    // for a mechanism system powered by one waterwheel with 1 block high water column
    private static final float LOAD_MULTIPLIER = 220;

    // Number of ticks it takes an axle to complete a revolution with spin 1
    // This is a constant value from TFC+
    private static final float REVOLUTION_TIME_FOR_SPIN_ONE = 3 * 20;
    // Number of revolution it takes for the nut to travel from one end to the other
    // Given the length of the screw is 2 blocks, this also means the screw lead is 1/8 of block
    private static final float MAX_REVOLUTIONS = 16;

    private static final int PRESSING_DELAY = 50;
    private static final int PRESSING_READY_CHECK_DELAY = 20;
    private static final int DECAY_DELAY = 100;

    private static final int[] TIME_SKIPPED_RECIPE_MULTIPLIERS = new int[] { 100, 50, 10, 1 };

    long lastPressOperationTicks;
    float unusedProgress;

    // Progress is determined by the fullness of the barrel
    // however these values are used to track the progress for the client attempting smooth rotation when rendered
    float currentProgress;
    float nextProgress;

    private final Timer pressingReadyCheckTimer = new Timer(PRESSING_READY_CHECK_DELAY);
    private final Timer decayTimer = new Timer(DECAY_DELAY);

    private final ItemStack[] storage = new ItemStack[MAX_STORAGE];
    private final FluidStack[] fluids = new FluidStack[MAX_FLUIDS];

    private float inputFullness;
    private float inputLoad;
    private boolean inputFullnessValueIsValid;
    private boolean inputLoadValueIsValid;
    private boolean progressPaused;

    boolean clientNeedToUpdate = false;

    public TileEntityScrewPressDisc getScrewPressDiscTileEntity() {
        TileEntity te = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
        if (te instanceof TileEntityScrewPressDisc) {
            return (TileEntityScrewPressDisc) te;
        } else {
            return null;
        }
    }

    public TileEntityScrewPressLever getScrewPressLeverTileEntity() {
        TileEntityScrewPressDisc teDisc = getScrewPressDiscTileEntity();
        if (teDisc != null) {
            return teDisc.getScrewPressLeverTileEntity();
        } else {
            return null;
        }
    }

    public TileEntityScrew getScrewTileEntity() {
        TileEntityScrewPressLever teLever = getScrewPressLeverTileEntity();
        if (teLever != null) {
            return teLever.getScrewTileEntity();
        } else {
            return null;
        }
    }

    public ItemStack removeLiquid(ItemStack container) {
        if (container == null || container.stackSize > 1) {
            return container;
        }

        if (fluids[FLUID_OUTPUT] == null) {
            return container;
        }

        if (FluidContainerRegistry.isEmptyContainer(container)) {
            ItemStack out = FluidContainerRegistry.fillFluidContainer(fluids[FLUID_OUTPUT], container);
            if (out != null) {
                removeLiquidAmount(FluidContainerRegistry.getFluidForFilledItem(out).amount);

                return out;
            }
        } else if (container.getItem() instanceof IFluidContainerItem) {
            FluidStack isfs = ((IFluidContainerItem) container.getItem()).getFluid(container);
            if (isfs == null || fluids[FLUID_OUTPUT].isFluidEqual(isfs)) {
                removeLiquidAmount(((IFluidContainerItem) container.getItem()).fill(container, fluids[FLUID_OUTPUT], true));

                return container;
            }
        }

        return container;
    }

    private void removeLiquidAmount(int amount) {
        fluids[FLUID_OUTPUT].amount -= amount;
        if (fluids[FLUID_OUTPUT].amount == 0) {
            fluids[FLUID_OUTPUT] = null;
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        updateOutputFluidFullnessForClient();
    }

    public void drainLiquid() {
        fluids[FLUID_OUTPUT] = null;

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        updateOutputFluidFullnessForClient();
    }

    public FluidStack getOutputFluid() {
        return fluids[FLUID_OUTPUT];
    }

    public int getMaxOutputFluidAmount() {
        return MAX_OUTPUT_FLUID;
    }

    public int getOutputFluidAmount() {
        if (fluids[FLUID_OUTPUT] != null) {
            return fluids[FLUID_OUTPUT].amount;
        } else {
            return 0;
        }
    }

    public boolean isProgressPaused() {
        return progressPaused;
    }

    private void invalidateInputProperties() {
        inputFullnessValueIsValid = false;
        inputLoadValueIsValid = false;
    }

    public void onPressingDiscPlaced() {
        // Whenever the disc is placed the progress is updated
        // which males the disc (and lever) drop according to the fullness
        currentProgress = getInputFullness();
        nextProgress = currentProgress;
    }

    public void onBlockBroken() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                    storage[i]);
                worldObj.spawnEntityInWorld(ei);

                storage[i] = null;
            }
        }
    }

    public ScrewPressDiscPosition getDiscPosition(float partialTick) {
        float ticksPassed = Math.min(PRESSING_DELAY, (float) (TFC_Time.getTotalTicks() - lastPressOperationTicks) + partialTick);
        float partialProgress = ticksPassed / (float) PRESSING_DELAY * (nextProgress - currentProgress);
        return new ScrewPressDiscPosition(currentProgress + partialProgress);
    }

    public float getInputFullness() {
        if (!inputFullnessValueIsValid) {
            inputFullness = getInputFullnessNoCache();
            inputFullnessValueIsValid = true;
        }

        return inputFullness;
    }

    private float getInputFullnessNoCache() {
        float f = 0;
        for (int i = SLOT_INPUT_START; i < SLOT_OUTPUT_START; i++) {
            f += getSlotFullness(i);
        }
        return f / SLOT_OUTPUT_START;
    }

    private float getSlotFullness(int i) {
        if (storage[i] != null) {
            if (storage[i].getItem() instanceof IFood) {
                float weight = Food.getWeight(storage[i]);
                float decay = Math.max(0, Food.getDecay(storage[i]));
                return (weight - decay) / Global.FOOD_MAX_WEIGHT;
            } else {
                return (float) storage[i].stackSize / storage[i].getMaxStackSize();
            }
        } else {
            return 0;
        }
    }

    public float getInputLoad() {
        if (!inputLoadValueIsValid) {
            // Current load depends on the maximum load of any input item stack
            float load = 0;
            for (int i = SLOT_INPUT_START; i < SLOT_OUTPUT_START; i++) {
                if (storage[i] != null) {
                    float slotLoad = getSlotLoad(i);
                    if (slotLoad > load) {
                        load = slotLoad;
                    }
                }
            }

            inputLoad = load;
            inputLoadValueIsValid = true;
        }

        return inputLoad;
    }

    private float getSlotLoad(int i) {
        float hardness = ScrewPressHelper.getScrewPressInputItemHardness(storage[i]);
        return hardness * LOAD_MULTIPLIER;
    }

    private void processPressingInput() {
        TileEntityScrew teScrew = getScrewTileEntity();
        float screwSpin = teScrew.getScrewSpin();

        currentProgress = nextProgress;

        if (screwSpin > 0) {
            long elapsedTicks = TFC_Time.getTotalTicks() - lastPressOperationTicks;
            float progressMade = getProgressMadeForTicks(screwSpin, elapsedTicks);
            float availableProgress = progressMade * SLOT_OUTPUT_START + unusedProgress;

            int prevOutputFluidAmount = getOutputFluidAmount();

            float remainingProgress = availableProgress;
            int prevSuccessfulRecipeMultiplier = Integer.MAX_VALUE;

            while (true) {
                Bids.LOG.debug("remainingProgress: " + remainingProgress);

                // Keep using the progress to press the input
                // allowing to complete a recipe multiple times
                float prevRemainingProgress = remainingProgress;

                if (elapsedTicks > PRESSING_DELAY + 5) {
                    // Try to process multiple recipes at once
                    // Minimizes lag after skipping time / reloading chunks
                    for (int recipeMultiplier : TIME_SKIPPED_RECIPE_MULTIPLIERS) {
                        // Skip multipliers that were not successful
                        if (recipeMultiplier > prevSuccessfulRecipeMultiplier) {
                            continue;
                        }

                        remainingProgress = pressInput(remainingProgress, recipeMultiplier);

                        // Until any progress in consumed
                        if (remainingProgress < prevRemainingProgress) {
                            Bids.LOG.debug("recipeMultiplier: " + recipeMultiplier);
                            prevSuccessfulRecipeMultiplier = recipeMultiplier;
                            break;
                        }
                    }
                } else {
                    remainingProgress = pressInput(remainingProgress, 1);
                }

                if (remainingProgress == 0 || remainingProgress == prevRemainingProgress) {
                    // No more progress left
                    // or progress has not changed, so there is not enough to complete any more recipe
                    break;
                }
            }

            // If none of the available progress was used indicate pause
            progressPaused = remainingProgress == availableProgress;

            if (progressPaused) {
                unusedProgress = 0;
            } else {
                unusedProgress = remainingProgress;
            }

            if (elapsedTicks > PRESSING_DELAY + 5) {
                // When many ticks are skipped the currentProgress would be far in the past
                // reset the progress counters to the actual fullness
                currentProgress = nextProgress = getInputFullness();
            } else if (progressPaused) {
                // When progress gets paused
                // reset the next progress counter to the actual fullness
                nextProgress = getInputFullness();
            } else {
                nextProgress -= progressMade;
            }

            Bids.LOG.debug("currentProgress: " + currentProgress + ", nextProgress: " + nextProgress);

            updateOutputFluidFullnessForClient(prevOutputFluidAmount, getOutputFluidAmount());
        }
    }

    private float getProgressMadeForTicks(float screwSpin, long elapsedTicks) {
        // Time it takes the screw to complete a revolution with given spin
        float revolutionTimeForCurrentSpin = REVOLUTION_TIME_FOR_SPIN_ONE / screwSpin;
        // How much of one revolution has been completed
        float revolutionsMade = elapsedTicks / revolutionTimeForCurrentSpin;
        // How much progress has been made
        float progressMade = revolutionsMade / MAX_REVOLUTIONS;

        //Bids.LOG.info("screwSpin: " + screwSpin);
        //Bids.LOG.info("revolutionTimeForCurrentSpin: " + revolutionTimeForCurrentSpin);
        //Bids.LOG.info("elapsedTicks: " + elapsedTicks);
        //Bids.LOG.info("revolutionsMade: " + revolutionsMade);
        //Bids.LOG.info("progress: " + progressMade);

        return progressMade;
    }

    private float pressInput(float progress, int recipeMultiplier) {
        for (int i = SLOT_INPUT_START; i < SLOT_OUTPUT_START; i++) {
            if (storage[i] != null) {
                return pressInputSlot(progress, recipeMultiplier, i);
            }
        }

        return progress;
    }

    private float pressInputSlot(float progress, int recipeMultiplier, int slot) {
        ScrewPressRecipe recipe = ScrewPressManager.getMatchingRecipe(storage[slot]);
        if (recipe != null){
            if (canAddFluid(recipe.getFluidCraftingResult().getFluid())) {
                if (storage[slot].getItem() instanceof IFood) {
                    float weight = Food.getWeight(storage[slot]);
                    float decay = Math.max(0, Food.getDecay(storage[slot]));
                    float weightAvailable = weight - decay;
                    float weightToBeConsumed = Global.FOOD_MAX_WEIGHT * progress;
                    float weightThatCanBeConsumed = Math.min(weightToBeConsumed, weightAvailable);
                    float weightRequiredForRecipe = Food.getWeight(recipe.getInput()) * recipeMultiplier;
                    float weightActuallyConsumed = Math.min(weightRequiredForRecipe, weightThatCanBeConsumed);

                    //Bids.LOG.info("slot: " + slot);
                    //Bids.LOG.info("weight: " + weight);
                    //Bids.LOG.info("weightAvailable: " + weightAvailable);
                    //Bids.LOG.info("weightToBeConsumed: " + weightToBeConsumed);
                    //Bids.LOG.info("weightThatCanBeConsumed: " + weightThatCanBeConsumed);
                    //Bids.LOG.info("weightRequiredForRecipe: " + weightRequiredForRecipe);
                    //Bids.LOG.info("weightActuallyConsumed: " + weightActuallyConsumed);

                    if (weightRequiredForRecipe > weightActuallyConsumed && weightRequiredForRecipe < weightAvailable) {
                        // When there is not enough progress to complete the recipe
                        // but there is enough weight to consume
                        // just carry the progress to the next run
                        //Bids.LOG.info("Not enough progress: " + progress);

                        return progress;
                    } else {
                        float fluidAmountForRecipe = recipe.getFluidCraftingResult().amount * recipeMultiplier;
                        int fluidActuallyProduced = Math.round(fluidAmountForRecipe * (weightActuallyConsumed / weightRequiredForRecipe));

                        //Bids.LOG.info("fluidAmountForRecipe: " + fluidAmountForRecipe);
                        //Bids.LOG.info("fluidActuallyProduced: " + fluidActuallyProduced);

                        if (recipeMultiplier > 1 && fluidActuallyProduced + getOutputFluidAmount() > MAX_OUTPUT_FLUID) {
                            // When recipe is multiplied, we don't allow overflowing
                            return progress;
                        }

                        float weightRemaining = weight - weightActuallyConsumed;
                        if (weightRemaining > decay) {
                            Food.setWeight(storage[slot], weightRemaining);
                        } else {
                            storage[slot] = null;
                        }

                        int fluidActuallyAdded = addFluid(recipe.getFluidCraftingResult().getFluid(), fluidActuallyProduced);
                        float progressUsed = weightActuallyConsumed / Global.FOOD_MAX_WEIGHT;

                        //Bids.LOG.info("fluidActuallyAdded: " + fluidActuallyAdded);
                        //Bids.LOG.info("progressUsed: " + progressUsed);

                        invalidateInputProperties();

                        return progress - progressUsed;
                    }
                } else {
                    int sizeAvailable = storage[slot].stackSize;
                    int maxStackSize = storage[slot].getMaxStackSize();
                    int sizeToBeConsumed = (int) Math.floor(storage[slot].getMaxStackSize() * progress);
                    int sizeThatCanBeConsumed = Math.min(sizeToBeConsumed, sizeAvailable);
                    int sizeRequiredForRecipe = recipe.getInput().stackSize * recipeMultiplier;
                    int sizeActuallyConsumed = Math.min(sizeRequiredForRecipe, sizeThatCanBeConsumed);

                    //Bids.LOG.info("slot: " + slot);
                    //Bids.LOG.info("maxStackSize: " + maxStackSize);
                    //Bids.LOG.info("sizeAvailable: " + sizeAvailable);
                    //Bids.LOG.info("sizeToBeConsumed: " + sizeToBeConsumed);
                    //Bids.LOG.info("sizeThatCanBeConsumed: " + sizeThatCanBeConsumed);
                    //Bids.LOG.info("sizeRequiredForRecipe: " + sizeRequiredForRecipe);
                    //Bids.LOG.info("sizeActuallyConsumed: " + sizeActuallyConsumed);

                    if (sizeRequiredForRecipe > sizeActuallyConsumed && sizeRequiredForRecipe < sizeAvailable) {
                        // When there is not enough progress to complete the recipe
                        // but there is enough to be consumed
                        // just carry the progress to the next run
                        //Bids.LOG.info("Not enough progress: " + progress);

                        return progress;
                    } else {
                        float fluidAmountForRecipe = recipe.getFluidCraftingResult().amount * recipeMultiplier;
                        int fluidActuallyProduced = Math.round(fluidAmountForRecipe * ((float)sizeActuallyConsumed / sizeRequiredForRecipe));

                        //Bids.LOG.info("fluidAmountForRecipe: " + fluidAmountForRecipe);
                        //Bids.LOG.info("fluidActuallyProduced: " + fluidActuallyProduced);

                        if (recipeMultiplier > 1 && fluidActuallyProduced + getOutputFluidAmount() > MAX_OUTPUT_FLUID) {
                            // When recipe is multiplied, we don't allow overflowing
                            return progress;
                        }

                        int sizeRemaining = sizeAvailable - sizeActuallyConsumed;
                        if (sizeRemaining > 0) {
                            storage[slot].stackSize = sizeRemaining;
                        } else {
                            storage[slot] = null;
                        }

                        int fluidActuallyAdded = addFluid(recipe.getFluidCraftingResult().getFluid(), fluidActuallyProduced);
                        float progressUsed = (float)sizeActuallyConsumed / maxStackSize;

                        //Bids.LOG.info("fluidActuallyAdded: " + fluidActuallyAdded);
                        //Bids.LOG.info("progressUsed: " + progressUsed);

                        invalidateInputProperties();

                        return progress - progressUsed;
                    }
                }
            }
        }

        // Recipe not found
        // or produced fluid could not be added
        return progress;
    }

    private void updateOutputFluidFullnessForClient() {
        clientNeedToUpdate = true;
    }

    private void updateOutputFluidFullnessForClient(int prev, int current) {
        if (current == 0 && prev != 0 ||
            current == MAX_OUTPUT_FLUID && prev != MAX_OUTPUT_FLUID ||
            Math.round(prev / 500f) != Math.round(current / 500f)) {
            clientNeedToUpdate = true;
        }
    }

    private boolean canAddFluid(Fluid fluid) {
        // Empty basin or compatible fluid that is not full
        return fluids[FLUID_OUTPUT] == null ||
            fluids[FLUID_OUTPUT].getFluid() == fluid &&
            fluids[FLUID_OUTPUT].amount < getMaxOutputFluidAmount();
    }

    private int addFluid(Fluid fluid, int amount) {
        if (fluids[FLUID_OUTPUT] == null) {
            fluids[FLUID_OUTPUT] = new FluidStack(fluid, amount);

            return amount;
        } else {
            if (fluids[FLUID_OUTPUT].amount + amount > MAX_OUTPUT_FLUID) {
                int amountActuallyAdded = MAX_OUTPUT_FLUID - fluids[FLUID_OUTPUT].amount;

                fluids[FLUID_OUTPUT].amount = MAX_OUTPUT_FLUID;

                return amountActuallyAdded;
            } else {
                fluids[FLUID_OUTPUT].amount = fluids[FLUID_OUTPUT].amount + amount;

                return amount;
            }
        }
    }

    private boolean isPressActive() {
        return getScrewTileEntity() != null;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }

            if (decayTimer.tick()) {
                TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord, false);
            }

            if (pressingReadyCheckTimer.tick() && lastPressOperationTicks == 0) {
                if (isPressActive()) {
                    lastPressOperationTicks = TFC_Time.getTotalTicks() - PRESSING_DELAY;
                    unusedProgress = 0;

                    worldObj.markBlockForUpdate(xCoord, zCoord, yCoord);

                    Bids.LOG.debug("Pressing is ready");
                }
            }

            if (lastPressOperationTicks > 0 && lastPressOperationTicks + PRESSING_DELAY <= TFC_Time.getTotalTicks()) {
                if (isPressActive()) {
                    processPressingInput();
                    lastPressOperationTicks = TFC_Time.getTotalTicks();

                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                } else {
                    lastPressOperationTicks = 0;

                    worldObj.markBlockForUpdate(xCoord, zCoord, yCoord);

                    Bids.LOG.debug("Pressing stopped");
                }
            }
        }
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeDataToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
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
        tag.setLong("lastPressOperationTicks", lastPressOperationTicks);
        tag.setFloat("unusedProgress", unusedProgress);
        tag.setFloat("currentProgress", currentProgress);
        tag.setFloat("nextProgress", nextProgress);
        tag.setBoolean("progressPaused", progressPaused);

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

    public void readDataFromNBT(NBTTagCompound tag) {
        lastPressOperationTicks = tag.getLong("lastPressOperationTicks");
        unusedProgress = tag.getFloat("unusedProgress");
        currentProgress = tag.getFloat("currentProgress");
        nextProgress = tag.getFloat("nextProgress");
        progressPaused = tag.getBoolean("progressPaused");

        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("storage", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = ItemStack.loadItemStackFromNBT(itemTag);
        }

        for (int i = 0; i < MAX_FLUIDS; i++) {
            fluids[i] = null;
        }

        NBTTagList fluidTagList = tag.getTagList("fluids", 10);
        for (int i = 0; i < fluidTagList.tagCount(); i++) {
            NBTTagCompound fluidTag = fluidTagList.getCompoundTagAt(i);
            final int slot = fluidTag.getInteger("slot");
            fluids[slot] = FluidStack.loadFluidStackFromNBT(fluidTag);
        }

        invalidateInputProperties();
    }

    @Override
    public int getSizeInventory() {
        return MAX_STORAGE;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return storage[slot];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (storage[i] != null) {
            invalidateInputProperties();

            if (storage[i].stackSize <= j) {
                ItemStack itemstack = storage[i];
                storage[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = storage[i].splitStack(j);
            if (storage[i].stackSize == 0) {
                storage[i] = null;
            }
            return itemstack1;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        storage[slot] = itemStack;

        invalidateInputProperties();
    }

    @Override
    public String getInventoryName() {
        return "ScrewPressBarrel";
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
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return false;
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
