package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Blocks.BlockRoof;
import com.dunk.tfc.Blocks.Devices.BlockFirepit;
import com.dunk.tfc.Blocks.Devices.BlockHopper;
import com.dunk.tfc.Blocks.Flora.BlockFruitLeaves;
import com.dunk.tfc.Blocks.Vanilla.BlockCustomLeaves;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.TileEntities.TEFirepit;
import com.dunk.tfc.TileEntities.TEHopper;
import com.dunk.tfc.api.Interfaces.IHeatSourceTE;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCOptions;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Blocks.BlockWoodPile;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.Core.WoodPile.*;
import com.unforbidable.tfc.bids.api.*;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningManager;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningRecipe;
import com.unforbidable.tfc.bids.api.Enums.EnumWoodFatness;
import com.unforbidable.tfc.bids.api.Enums.EnumWoodHardness;
import com.unforbidable.tfc.bids.api.Events.FireSettingEvent;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnManager;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;
import com.unforbidable.tfc.bids.api.Providers.KilnManagerProvider;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TileEntityWoodPile extends TileEntity implements IInventory, IMessageHanldingTileEntity<WoodPileMessage>, IHeatSourceTE {

    public static final int MAX_STORAGE = 16;

    public static final float SEASONING_EXPOSED_MULTIPLIER = 1f;
    public static final float SEASONING_COVERED_MULTIPLIER = 1.5f;

    static final long SEASONING_INTERVAL = TFC_Time.HOUR_LENGTH;

    static final int ACTION_UPDATE = 1;
    static final int ACTION_RETRIEVE_ITEM = 2;

    // TFC kiln burns 16 logs in 18000 ticks
    // A log will burn in a fire pit for a minimum of 2000 ticks
    // with oak burning the longest for 4500 ticks
    // This means at rate of 1f our 16 firewood will burn for at least 32000 ticks
    // therefore at rate of 0.5625f those 16 firewood will burn for 18000 ticks
    // At the rate of 0.5625f our 16 oak firewood will burn for cca 40500 ticks
    // 8 oak firewood will burn 20250 ticks
    // 7 oak firewood will burn 17718.75 ticks
    // and so on
    // Using this mechanics later on kiln could require 7 or 8 oak firewood or 16 baobab firewood per cycle
    // instead of always 16 logs as per TFC kiln
    static final float KILN_FACTOR = 0.5625f;

    // The numbers bellow are calculated as follows:
    // 18000 * X + 450 * X * R = 1
    // 18000 * (Y / R) + 450 * Y = 1
    // where
    // X is the weight of ticks
    // Y is the weight of temperature
    // R is the ratio of contribution of ticks and temperature
    // The value of R is chosen as 12 meaning temperature contributes 12 times as much as ticks
    // 18000 is the number of ticks it takes 16 firewood of Baobab to burn
    // and 450 is the average temperature produced by burning 16 firewood of Baobab
    // hence for Baobab the progress will reach 1 when 16 firewood of Baobab has been burned
    // using firewood that burns hotter and longer than Baobab will consume lest firewood and complete sooner.
    // At the same time using firewood that runs less hot will consume more fuel and complete later.
    private static final double KILN_PROGRESS_TEMP_TO_TICKS_RATIO = 12.0;
    private static final double KILN_PROGRESS_TEMP_TARGET = 450.0;
    private static final double KILN_PROGRESS_TICKS_TARGET = 18000.0;
    private static final double KILN_PROGRESS_TICKS_WEIGHT = 1 / (KILN_PROGRESS_TEMP_TARGET * (KILN_PROGRESS_TEMP_TO_TICKS_RATIO + KILN_PROGRESS_TICKS_TARGET / KILN_PROGRESS_TEMP_TARGET));
    private static final double KILN_PROGRESS_TEMP_WEIGHT = KILN_PROGRESS_TEMP_TO_TICKS_RATIO / (KILN_PROGRESS_TEMP_TARGET * (KILN_PROGRESS_TEMP_TO_TICKS_RATIO + KILN_PROGRESS_TICKS_TARGET / KILN_PROGRESS_TEMP_TARGET));

    // Choose such a value of R so that the value below adds up to exactly 1.0
    // For certain R the value will not add up to exactly 1.0 due to rounding errors
    private static final double THIS_VALUE_MUST_ADD_UP_TO_EXACTLY_ONE = KILN_PROGRESS_TEMP_WEIGHT * KILN_PROGRESS_TEMP_TARGET + KILN_PROGRESS_TICKS_WEIGHT * KILN_PROGRESS_TICKS_TARGET;

    // This is the maximum temp value that can contribute to the progress
    // This prevents the process from being able to finish too early,
    // when high heat wood type is burned at an increased rate
    // Low heat wood types will still benefit from burning at an increased rate
    // and the kiln may finish earlier as long as enough wood items (more than 16) is provided
    private static final int KILN_PROGRESS_TEMP_CAP = 800;

    // This is the maximum ticks value that can contribute to the progress
    // This prevents the progress from being completed timely,
    // even if the temperature is not quite high enough
    // especially at burning at reduced rate
    private static final int KILN_PROGRESS_TICKS_CAP = 18000;

    // How long it takes to catch fire from a fire pit
    private static final int SET_ON_FIRE_FROM_SOURCE_DELAY = 200;

    private static final ForgeDirection[] HORIZONTAL_FORGE_DIRECTIONS = { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST };

    // How long it takes to catch fire from a Blocks.fire
    private static final int CATCH_FIRE_DELAY = 100;

    // With default settings,
    // a log pile produces about 135 mB of pitch during the process of making charcoal
    // that is about 8.5 mB per log,
    // although the log pile needs to be ticking, so you can't leave, sleep or otherwise skip time
    // 5 mB is used a neat baseline for any wood type
    // although perhaps way too much for non-resinous wood types
    // to encourage the use of resinous woods
    private static final float PITCH_PER_ITEM = 5;

    // How much pitch moves to neighbor wood pile or hopper below per cycle
    private static final int PITCH_MOVE_AMOUNT = 20;

    // Pitch is moved faster when a large amount builds up
    private static final int PITCH_MOVE_BULK_AMOUNT = 100;

    // After pitch movement fails, set a time out for the next attempt
    private static final int PITCH_MOVEMENT_FAILURE_DELAY = 200;

    final ItemStack[] storage = new ItemStack[MAX_STORAGE];

    long lastSeasoningTicks = 0;
    boolean initialized;
    int orientation;

    int selectedItemIndex = -1;

    boolean clientNeedToUpdate = false;
    Timer seasoningTimer = new Timer(10);
    int woodPileOpeningCounter = 0;

    float lastBurningTicks = 0;
    Timer burningTimer = new Timer(10);
    int totalBurningTemp = 0;
    float totalBurningTicks = 0;
    int totalBurningItems = 0;

    int cachedHeatSourceTemp = 0;

    Timer setOnFireFromSourceCheckTimer = new Timer(100);
    Timer setOnFireFromSourceDelayedTimer = new Timer(0);

    Timer catchFireDelayedTimer = new Timer(0);

    boolean isItemBoundsCacheActual = false;
    List<WoodPileItemBounds> itemBoundsCache = new ArrayList<WoodPileItemBounds>();

    EntityPlayer openDelayedGUIplayer = null;

    boolean onFire = false;

    Timer charcoalTimer = new Timer(200);
    Timer pitchTimer = new Timer(10);
    int charcoalProgress = 0;
    long lastCharcoalTicks = 0;
    int pitchCounter = 0;

    Timer torchDetectionTimer = new Timer(20);
    Timer torchDetectionClientTimer = new Timer(10);
    long torchDetectedTicks = 0;

    private final IKilnManager kilnManager = KilnManagerProvider.getKilnManager(new WoodPileKilnHeatSource(this));

    public TileEntityWoodPile() {
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }

    public void openDelayedGUI(EntityPlayer player) {
        openDelayedGUIplayer = player;
    }

    public List<ItemStack> getItems(boolean stacked) {
        List<ItemStack> list = new ArrayList<ItemStack>();
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                if (stacked) {
                    // Try to find same item already added to the output list
                    boolean stackFound = false;
                    for (ItemStack item : list) {
                        if (item.getItem() == storage[i].getItem()
                                && item.getItemDamage() == storage[i].getItemDamage()) {
                            // If found, increatse stack...
                            item.stackSize = item.stackSize + storage[i].stackSize;
                            stackFound = true;
                            break;
                        }
                    }

                    // And continue the loop
                    if (stackFound) {
                        continue;
                    }
                }

                // When not stacking or not found in the output list
                // add a copy to the output list
                list.add(storage[i].copy());
            }
        }

        return list;
    }

    public List<WoodPileItemBounds> getItemBounds() {
        if (!isItemBoundsCacheActual) {
            itemBoundsCache.clear();
            for (WoodPileItemBounds itemBounds : new WoodPileBoundsIterator(storage, orientation)) {
                itemBoundsCache.add(itemBounds);
            }

            isItemBoundsCacheActual = true;

            Bids.LOG.debug("Wood pile item bounds updated: " + itemBoundsCache.size());
        }

        return itemBoundsCache;
    }

    public float getActualBlockHeight() {
        double maxY = 0;
        for (WoodPileItemBounds itemBounds : getItemBounds()) {
            if (maxY < itemBounds.getBounds().maxY) {
                maxY = itemBounds.getBounds().maxY;
            }
        }

        return (float) maxY;
    }

    public boolean isEmpty() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                return false;
            }
        }

        return true;
    }

    public boolean isFull() {
        return getActualOccupiedSlotCount() == 16;
    }

    private int getActualOccupiedSlotCount() {
        int count = 0;
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                IWoodPileRenderProvider render = WoodPileRegistry.findItem(storage[i].getItem());
                if (render.isWoodPileLargeItem(storage[i])) {
                    count += 4;
                } else {
                    count++;
                }
            }
        }

        return count;
    }

    public boolean addItem(ItemStack itemStack) {
        if (isFull()) {
            return false;
        }

        onStorageChanged();

        // When adding items by right clicking
        // we want to put items on top of the pile
        // The current solution is to clear the storage
        // and add existing items back one by one
        List<ItemStack> exisitingItems = new ArrayList<ItemStack>();
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                exisitingItems.add(storage[i]);
                storage[i] = null;
            }
        }

        for (ItemStack is : exisitingItems) {
            addOneItem(is);
        }

        int totalItemsAdded = 0;
        while (itemStack.stackSize > totalItemsAdded) {
            ItemStack toAdd = itemStack.copy();
            toAdd.stackSize = 1;

            if (!addOneItem(toAdd)) {
                // Cannot add any more items in the stack
                break;
            }

            totalItemsAdded++;
        }

        if (totalItemsAdded > 0) {
            itemStack.stackSize = itemStack.stackSize - totalItemsAdded;

            return true;
        }

        return false;
    }

    protected boolean addOneItem(ItemStack itemStack) {
        if (itemStack.stackSize != 1) {
            Bids.LOG.warn("Denied attempt to add stack of size other than 1 to a log pile");
            return false;
        }

        final IWoodPileRenderProvider render = WoodPileRegistry.findItem(itemStack.getItem());

        for (EnumSlotGroup slotGroup : EnumSlotGroup.ALL_VALUES) {
            if (render.isWoodPileLargeItem(itemStack)) {
                // Large items must find an empty hybrid slot and empty shared slots
                if (storage[slotGroup.getHybridSlot()] == null) {
                    int emptySlotCount = 0;
                    for (int slot : slotGroup.getSharedSlots()) {
                        if (storage[slot] == null) {
                            emptySlotCount++;
                        }
                    }

                    // And if all 3 shared slots are empty
                    // place into the hybrid slot
                    if (emptySlotCount == 3) {
                        storage[slotGroup.getHybridSlot()] = itemStack;

                        return true;
                    }
                }
            } else {
                // A normal item can use any of the 4 slots
                // as long as there is no large item in the hybrid slot
                if (storage[slotGroup.getHybridSlot()] != null) {
                    ItemStack itemInHybridSlot = storage[slotGroup.getHybridSlot()];
                    IWoodPileRenderProvider renderInHybridSlot = WoodPileRegistry.findItem(itemInHybridSlot.getItem());
                    if (renderInHybridSlot.isWoodPileLargeItem(itemInHybridSlot)) {
                        // No items can be added to this slot groups
                        // because there is a large item in the hybrid slot
                        continue;
                    }
                }

                // Iterate the whole array in order
                // so that the item is placed in order
                for (int i = 0; i < MAX_STORAGE; i++) {
                    if (storage[i] == null && slotGroup.hasSlot(i)) {
                        storage[i] = itemStack;

                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void setSelectedItemIndex(int index) {
        if (selectedItemIndex != index) {
            Bids.LOG.debug("Selected item index: " + index);
        }

        selectedItemIndex = index;
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public ItemStack getSelectedItem(boolean stacked) {
        return getItemAtIndex(selectedItemIndex, stacked);
    }

    private ItemStack getItemAtIndex(int index, boolean stacked) {
        if (index >= 0 && index < MAX_STORAGE && storage[index] != null) {
            final ItemStack itemStack = storage[index].copy();

            if (stacked) {
                for (int i = 0; i < MAX_STORAGE; i++) {
                    if (storage[i] != null && i != index) {
                        if (storage[i].getItem() == itemStack.getItem()
                                && storage[i].getItemDamage() == itemStack.getItemDamage()) {
                            itemStack.stackSize = itemStack.stackSize + storage[i].stackSize;
                        }
                    }
                }
            }

            return itemStack;
        }

        return null;
    }

    private void retrieveItemAtIndex(int index, EntityPlayer player) {
        if (storage[index] != null) {
            final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, storage[index]);
            worldObj.spawnEntityInWorld(ei);

            storage[index] = null;

            tryToPullItemsFromAbove();

            if (woodPileOpeningCounter == 0 && isEmpty()) {
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            } else {
                onStorageChanged();
            }
        }
    }

    public ItemStack retrieveFirstAvailableItemToPullDown(boolean acceptingLargeItems) {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                IWoodPileRenderProvider render = WoodPileRegistry.findItem(storage[i].getItem());
                if (render.isWoodPileLargeItem(storage[i])) {
                    if (acceptingLargeItems) {
                        return retrieveFirstItemAtIndexToPullDown(i);
                    }
                } else {
                    return retrieveFirstItemAtIndexToPullDown(i);
                }
            }
        }

        return null;
    }

    private ItemStack retrieveFirstItemAtIndexToPullDown(int index) {
        if (storage[index] != null) {
            ItemStack retrieved = storage[index];

            storage[index] = null;

            tryToPullItemsFromAbove();

            if (woodPileOpeningCounter == 0 && isEmpty()) {
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            } else {
                onStorageChanged();
            }

            return retrieved;
        } else {
            return null;
        }
    }

    private void tryToPullItemsFromAbove() {
        TileEntity teAbove = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
        if (teAbove instanceof TileEntityWoodPile) {
            Bids.LOG.debug("Wood pile above found");

            int occupied = getActualOccupiedSlotCount();
            while (occupied < 16) {
                Bids.LOG.debug("Trying to pull an item from wood pile above");

                TileEntityWoodPile woodPileAbove = (TileEntityWoodPile) teAbove;

                // Check if large item fits
                boolean largeItemFits = occupied <= 12;
                ItemStack retrieved = woodPileAbove.retrieveFirstAvailableItemToPullDown(largeItemFits);
                if (retrieved != null) {
                    Bids.LOG.debug("Pulled item " + retrieved.getDisplayName() + "[" + retrieved.stackSize + "] from wood pile above");

                    addItem(retrieved);

                    occupied = getActualOccupiedSlotCount();
                } else {
                    break;
                }
            }
        }

    }

    private void onStorageChanged() {
        if (!worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;
        }

        isItemBoundsCacheActual = false;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // One time only right after creation
            if (!initialized) {
                lastSeasoningTicks = TFC_Time.getTotalTicks();
                lastBurningTicks = TFC_Time.getTotalTicks();
                lastCharcoalTicks = TFC_Time.getTotalTicks();

                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                initialized = true;
            }

            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }

            // Better to do this on the entity item
            // but that's a TFC torch
            // So we detect torches above, when not already on fire
            if (torchDetectionTimer.tick()) {
                Item torchItem = Item.getItemFromBlock(TFCBlocks.torch);
                boolean torchDetected = false;

                for (EntityItem entityItem : detectEntityItems()) {
                    if (!onFire) {
                        if (entityItem.getEntityItem().getItem() == torchItem) {
                            Bids.LOG.debug("Torch detected above woodpile at " + xCoord + "," + yCoord + "," + zCoord);

                            if (torchDetectedTicks == 0) {
                                torchDetectedTicks = TFC_Time.getTotalTicks();
                            } else if (torchDetectedTicks + 160 <= TFC_Time.getTotalTicks()) {
                                setOnFire(true);

                                // Torches are not flammable, so just set that one dead
                                entityItem.setDead();
                            }

                            torchDetected = true;
                        }
                    }

                    handleDroppedItem(entityItem);

                    if (onFire) {
                        entityItem.setFire(100);
                    }
                }

                if (!torchDetected) {
                    // Reset detection ticks in case the torch was picked up
                    torchDetectedTicks = 0;
                }
            }

            // Wood pile can catch fire from lit fire pit nearby
            // that is sufficiently enclosed
            if (!onFire && setOnFireFromSourceDelayedTimer.getTicksToGo() == 0 && setOnFireFromSourceCheckTimer.tick()) {
                if (canSetOnFireFromSourceNearby()) {
                    setOnFireFromSourceDelayedTimer.delay(SET_ON_FIRE_FROM_SOURCE_DELAY);
                }
            }

            // Actually catch fire from lit fire pit nearby after a delay
            if (!onFire && setOnFireFromSourceDelayedTimer.tick()) {
                if (canSetOnFireFromSourceNearby()) {
                    setOnFire(true);
                }
            }

            // Wood piles are not flammable anymore,
            // so they are now set on fire from fire blocks manually after delay
            if (!onFire && catchFireDelayedTimer.tick()) {
                if (canCatchFire()) {
                    setOnFire(true);
                }
            }

            // Check if enough time had passed
            // for seasoning interval
            if (!onFire && seasoningTimer.tick() && TFC_Time.getTotalTicks() > lastSeasoningTicks + SEASONING_INTERVAL) {
                seasonItems();
            }

            // Burn off logs when on fire and not in a charcoal pit
            // Burning logs is paused when the UI is open
            // to avoid sync glitches
            if (onFire && burningTimer.tick() && woodPileOpeningCounter == 0) {
                WoodPileBurningItem burningItem = findNextBurningItem();
                EnumBurningRate burningRate = getBurningRate();
                if (burningItem != null && burningRate != EnumBurningRate.NONE) {
                    // Cache heat source temp
                    cachedHeatSourceTemp = (int) (burningItem.getFuel().getFuelMaxTemp(burningItem.getItemStack()) * burningRate.getBurnTimeMultiplier());

                    // Wood pile can burn and there is anything inside to burn
                    handleBurningItem(burningItem, burningRate);

                    // Make sure fire is spreading when burning items
                    doSpreadFire();

                    // Harm entities
                    doHarmToLivingEntities();
                } else {
                    // Reset cached heat source temp
                    cachedHeatSourceTemp = 0;

                    // Reset burn ticks and reduce strength
                    lastBurningTicks = TFC_Time.getTotalTicks();
                }
            }

            // Invoke the kiln manager deferred update
            // with an inexpensive onFire check to see if anything can actually happen
            if (onFire) {
                kilnManager.update();
            }

            // Charcoal making
            // when on fire and if charcoal can be made
            if (onFire && charcoalTimer.tick() && canMakeCharcoal()) {
                EnumBurningRate burningRate = getBurningRate();
                if (burningRate == EnumBurningRate.NONE) {
                    if (isCharcoalDone()) {
                        // After the charcoal process is complete
                        // it takes another timer cycle to create it
                        // This allows time to move out any remaining pitch
                        doCreateCharcoal();
                    } else {
                        handleMakingCharcoal();
                    }
                } else {
                    lastCharcoalTicks = TFC_Time.getTotalTicks();
                }
            }

            // Try to move pitch towards a hopper
            if (pitchCounter > 0 && pitchTimer.tick()) {
                if (!handlePitch()) {
                    // Either the pitch has nowhere to go at all
                    // or the destination barrel is full,
                    // so next attempt is delayed
                    pitchTimer.delay(PITCH_MOVEMENT_FAILURE_DELAY);
                };
            }

            if (openDelayedGUIplayer != null) {
                openDelayedGUIplayer.openGui(Bids.instance, BidsGui.woodPileGui, worldObj, xCoord, yCoord, zCoord);
                openDelayedGUIplayer = null;
            }
        } else {
            // Torch detection client side to show sparks
            if (!onFire && torchDetectionClientTimer.tick()) {
                Item torchItem = Item.getItemFromBlock(TFCBlocks.torch);

                for (EntityItem entityItem : detectEntityItems()) {
                    if (entityItem.getEntityItem().getItem() == torchItem) {
                        worldObj.spawnParticle("lava", xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, -0.5F + worldObj.rand.nextFloat(), -0.5F + worldObj.rand.nextFloat(), -0.5F + worldObj.rand.nextFloat());
                    }
                }
            }
        }
    }

    private boolean handlePitch() {
        TileEntity teBelow = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
        if (teBelow instanceof TEHopper) {
            // Hopper below
            // Pitch is moved bit by bit into the hopper (and barrel)
            // If there is a lot of pitch, move it faster
            int amountToMove = pitchCounter < PITCH_MOVE_BULK_AMOUNT ?
                Math.min(pitchCounter, PITCH_MOVE_AMOUNT) :
                PITCH_MOVE_BULK_AMOUNT * (pitchCounter / PITCH_MOVE_BULK_AMOUNT);
            Bids.LOG.info("Pitch moving to hopper: {}/{}", amountToMove, pitchCounter);
            int amountMoved = WoodPileHelper.offerPitchToHopper(worldObj, xCoord, yCoord - 1, zCoord, amountToMove);
            pitchCounter -= amountMoved;
            // Failure if no pitch was moved
            return amountMoved != 0;
        } else if (teBelow instanceof TileEntityWoodPile) {
            // Wood pile below
            movePitchToNeighbor((TileEntityWoodPile) teBelow);
            return true;
        } else {
            // Wood pile to any side, which also has wood pile or hopper below
            for (ForgeDirection d : HORIZONTAL_FORGE_DIRECTIONS) {
                TileEntity teSide = worldObj.getTileEntity(xCoord + d.offsetX, yCoord, zCoord + d.offsetZ);
                TileEntity teSideBelow = worldObj.getTileEntity(xCoord + d.offsetX, yCoord - 1, zCoord + d.offsetZ);
                if (teSide instanceof TileEntityWoodPile && (teSideBelow instanceof TileEntityWoodPile || teSideBelow instanceof TEHopper)) {
                    movePitchToNeighbor((TileEntityWoodPile) teSide);
                    return true;
                }
            }
        }

        return false;
    }

    private void movePitchToNeighbor(TileEntityWoodPile woodPile) {
        // Pitch is moved in its entirety between wood piles
        woodPile.takePitch(pitchCounter);
        pitchCounter = 0;
    }

    private void takePitch(int pitch) {
        pitchCounter += pitch;
    }

    private boolean canMakeCharcoal() {
        for (ItemStack is : storage) {
            if (is != null && !WoodPileHelper.isItemValidWoodPileItemForCharcoal(is)) {
                return false;
            }
        }

        return true;
    }

    private void handleMakingCharcoal() {
        // By default, it takes 18 hours to process 16 items,
        // using the charcoal time from the config, we calculate the ticks to process one item
        long ticksNeededForCharcoal = (long) (TFCOptions.charcoalPitBurnTime * TFC_Time.HOUR_LENGTH / 16);
        long ticksSincePreviousCharcoal = TFC_Time.getTotalTicks() - lastCharcoalTicks;
        if (ticksSincePreviousCharcoal >= ticksNeededForCharcoal) {
            // When time is skipped, multiple items can be processed at a time
            int count = (int) (ticksSincePreviousCharcoal / (float)ticksNeededForCharcoal);
            int targetProgress = Math.min(16, charcoalProgress + count);

            while (charcoalProgress < targetProgress) {
                ItemStack is = storage[charcoalProgress];
                if (is != null) {
                    processItemForCharcoal(is);
                }

                charcoalProgress++;
            }

            lastCharcoalTicks += ticksNeededForCharcoal * count;

            Bids.LOG.info("charcoalProgress: {}/16", charcoalProgress);
        }
    }

    private boolean isCharcoalDone() {
        return charcoalProgress == 16;
    }

    private boolean isMakingCharcoal() {
        return charcoalProgress > 0;
    }

    private void processItemForCharcoal(ItemStack is) {
        int pitchAmount = Math.round(getPitchRateForWoodType(is) * PITCH_PER_ITEM);
        pitchCounter += pitchAmount;
    }

    private float getAverageBurningTemp() {
        return totalBurningItems > 0 ? (totalBurningTemp / (float)totalBurningItems) : 0;
    }

    public double getKilnProgress() {
        // Values over cap is not necessarily truncated
        // however it is greatly diminished
        // In case of ticks, diminish factor of 0.1 allows the progress to complete
        // when burning tied bundles of sticks at relatively low temperature, having burned 28 items, in 24 hours
        double temp = getCappedProgressValue(getAverageBurningTemp(), KILN_PROGRESS_TEMP_CAP, 0);
        double ticks = getCappedProgressValue(totalBurningTicks, KILN_PROGRESS_TICKS_CAP, 0.1);
        return ticks * KILN_PROGRESS_TICKS_WEIGHT + temp * KILN_PROGRESS_TEMP_WEIGHT;
    }

    private static double getCappedProgressValue(double value, double cap, double diminishFactor) {
        if (value > cap) {
            return cap + (value - cap) * diminishFactor;
        } else {
            return value;
        }
    }

    public void resetKilnProgress() {
        totalBurningTicks = 0;
        totalBurningTemp = 0;
        totalBurningItems = 0;
        lastBurningTicks = TFC_Time.getTotalTicks();
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

    private void handleDroppedItem(EntityItem entityItem) {
        ItemStack itemStack = entityItem.getEntityItem();
        if (WoodPileHelper.isItemValidWoodPileItem(itemStack)) {
            addItem(itemStack);
            if (itemStack.stackSize == 0) {
                entityItem.setDead();
            }
        }
    }

    private boolean canSetOnFireFromSourceNearby() {
        for (ForgeDirection d : HORIZONTAL_FORGE_DIRECTIONS) {
            if (isValidSetOnFireSource(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ)) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidSetOnFireSource(int x, int y, int z) {
        TileEntity te = worldObj.getTileEntity(x, y, z);

        // Both TFC fire pit and new fire pit are accepted
        if (te instanceof TEFirepit) {
            TEFirepit teFirepit = (TEFirepit) te;
            // Is fire pit lit and burning fuel
            if (teFirepit.fireTemp > 100) {
                int exposedSides = 0;
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    if (isSetOnFireSourceSideExposed(x + d.offsetX, y + d.offsetY, z + d.offsetZ, d)) {
                        exposedSides++;
                    }
                }

                // Exactly one side must be exposed
                return exposedSides == 1;
            }
        }

        return false;
    }

    private boolean isSetOnFireSourceSideExposed(int x, int y, int z, ForgeDirection d) {
        if (worldObj.getTileEntity(x, y, z) instanceof TileEntityWoodPile) {
            // Wood piles don't cause fire source exposure
            return false;
        } else {
            // Otherwise check if the side that faces the fire source is solid
            return !worldObj.isSideSolid(x, y, z, d.getOpposite());
        }
    }

    private EnumBurningRate getBurningRate() {
        int exposedSides = 0;
        int woodPileSides = 0;

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            Block b = worldObj.getBlock(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
            if (b instanceof BlockWoodPile) {
                woodPileSides++;
            } else if (!isValidCharcoalPitBlock(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ, d)) {
                exposedSides++;
            }
        }

        if (exposedSides == 0) {
            // No exposure unless touching another burning wood pile - making charcoal
            return EnumBurningRate.NONE;
        } else if (exposedSides + woodPileSides == 1) {
            // One side exposed or touching another burning wood pile - burning hole or cauldron
            return EnumBurningRate.REDUCED;
        } else if (exposedSides + woodPileSides == 2) {
            // Two sides exposed or touching another burning wood pile - some sort of kiln
            return EnumBurningRate.NORMAL;
        } else {
            // More exposure - pyres
            return EnumBurningRate.INCREASED;
        }
    }

    private WoodPileBurningItem findNextBurningItem() {
        for (int i = MAX_STORAGE - 1; i >= 0; i--) {
            if (storage[i] != null) {
                IFirepitFuelMaterial fuel = FirepitRegistry.findFuel(storage[i].getItem());
                if (fuel != null && fuel.isFuelValid(storage[i])) {
                    return new WoodPileBurningItem(i, storage[i], fuel);
                }
            }
        }

        return null;
    }

    private void handleBurningItem(WoodPileBurningItem burningItem, EnumBurningRate burningRate) {
        float fuelBurnTime = burningItem.getFuel().getFuelBurnTime(burningItem.getItemStack()) * BidsOptions.WoodPile.burnTimeMultiplier;
        int fuelBurnTemp = burningItem.getFuel().getFuelMaxTemp(burningItem.getItemStack());

        float ticksNeededToBurnItem = fuelBurnTime * KILN_FACTOR;
        float ticksNeededToBurnItemForBurningRate = (ticksNeededToBurnItem / burningRate.getBurnTimeMultiplier());
        float ticksSincePreviousLogBurning = TFC_Time.getTotalTicks() - lastBurningTicks;

        if (ticksNeededToBurnItemForBurningRate <= ticksSincePreviousLogBurning) {
            // Enough ticks have passed to burn this item
            lastBurningTicks += ticksNeededToBurnItemForBurningRate;

            double lastKilnProgress = getKilnProgress();

            // Burning rate increases the temp
            totalBurningTemp += fuelBurnTemp * burningRate.getBurnTempMultiplier();
            totalBurningTicks += ticksNeededToBurnItemForBurningRate;
            totalBurningItems++;

            double currentKilnProgress = getKilnProgress();
            kilnManager.updateProgress(lastKilnProgress, currentKilnProgress);

            onItemBurned();

            Bids.LOG.debug("Wood pile has been burning at rate {} for {} ticks with total temperature of {}, average temperature {} and {} items burned: {}", burningRate, totalBurningTicks, totalBurningTemp, getAverageBurningTemp(), totalBurningItems, burningItem.getItemStack().getDisplayName());

            storage[burningItem.getIndex()] = null;

            // A wood pile above will also be burning
            // but any logs in it would still fall into the wood pile below
            tryToPullItemsFromAbove();

            // When the last log burns off
            // the wood pile disappears
            if (isEmpty()) {
                setOnFire(false);
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            } else {
                onStorageChanged();
            }
        }
    }

    private void onItemBurned() {
        if (BidsOptions.WoodPile.enableFireSetting) {
            handleFireSetting();
        }
    }

    private void handleFireSetting() {
        // Fire setting is handled through events
        FireSettingEvent.BurningEvent event = new FireSettingEvent.BurningEvent(worldObj, xCoord, yCoord, zCoord, (long)totalBurningTicks, totalBurningTemp, totalBurningItems);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeWoodPileDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();
        readWoodPileDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeWoodPileDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readWoodPileDataFromNBT(tag);
    }

    public void writeWoodPileDataToNBT(NBTTagCompound tag) {
        tag.setLong("lastSeasoningTicks", lastSeasoningTicks);
        tag.setBoolean("clientInitialized", initialized);
        tag.setInteger("orientation", orientation);
        tag.setBoolean("onFire", onFire);
        tag.setFloat("lastBurningTicks", lastBurningTicks);
        tag.setFloat("totalBurningTicks", totalBurningTicks);
        tag.setInteger("totalBurningTemp", totalBurningTemp);
        tag.setInteger("totalBurningItems", totalBurningItems);
        tag.setLong("lastCharcoalTicks", lastCharcoalTicks);
        tag.setInteger("charcoalProgress", charcoalProgress);

        NBTTagList itemTagList = new NBTTagList();
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("slot", i);
                storage[i].writeToNBT(itemTag);
                itemTagList.appendTag(itemTag);
            }
        }
        tag.setTag("items", itemTagList);

        kilnManager.writeKilnManagerToNBT(tag);
    }

    public void readWoodPileDataFromNBT(NBTTagCompound tag) {
        lastSeasoningTicks = tag.getLong("lastSeasoningTicks");
        initialized = tag.getBoolean("clientInitialized");
        orientation = tag.getInteger("orientation");
        onFire = tag.getBoolean("onFire");
        lastBurningTicks = tag.getFloat("lastBurningTicks");
        totalBurningTicks = tag.getFloat("totalBurningTicks");
        totalBurningTemp = tag.getInteger("totalBurningTemp");
        totalBurningItems = tag.getInteger("totalBurningItems");
        lastCharcoalTicks = tag.getLong("lastCharcoalTicks");
        charcoalProgress = tag.getInteger("charcoalProgress");

        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("items", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = ItemStack.loadItemStackFromNBT(itemTag);
        }
        isItemBoundsCacheActual = false;

        kilnManager.readKilnManagerFromNBT(tag);
    }

    public void onWoodPileBroken() {
        for (int i = 0; i < getSizeInventory(); i++) {
            if (storage[i] != null) {
                final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, storage[i]);
                worldObj.spawnEntityInWorld(ei);
            }
        }

        setOnFire(false);
    }

    protected void seasonItems() {
        final long ticksSinceLastSeasoning = TFC_Time.getTotalTicks() - lastSeasoningTicks;
        lastSeasoningTicks = TFC_Time.getTotalTicks();

        boolean coverageChecked = false;
        boolean isCovered = false;
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                final SeasoningRecipe recipe = SeasoningManager.getMatchingRecipe(storage[i]);

                if (recipe != null) {
                    // Lazy coverage calculation
                    // only when there is anything to season
                    if (!coverageChecked) {
                        isCovered = isCovered();
                        coverageChecked = true;
                    }

                    seasonItemInSlot(recipe, i, ticksSinceLastSeasoning, isCovered);
                }
            }
        }
    }

    protected void seasonItemInSlot(SeasoningRecipe recipe, int slot, long ticksSinceLastSeasoning, boolean isCovered) {
        final float currectSeasoning = SeasoningHelper.getItemSeasoningTag(storage[slot]);

        final float totalSeasoningTicks = recipe.getDuration() * TFC_Time.HOUR_LENGTH
                * BidsOptions.WoodPile.seasoningDurationMultiplier;
        final float remainingSeasoningTicks = totalSeasoningTicks * (1 - currectSeasoning);
        final float seasoningDelta = ticksSinceLastSeasoning / remainingSeasoningTicks;
        final float coverageMultiplier = isCovered ? SEASONING_COVERED_MULTIPLIER : SEASONING_EXPOSED_MULTIPLIER;

        final float newSeasoning = Math.min(1, currectSeasoning + seasoningDelta * coverageMultiplier);
        SeasoningHelper.setItemSeasoningTag(storage[slot], newSeasoning);

        Bids.LOG.debug("Item " + storage[slot].getDisplayName()
                + " updated seasoning: " + newSeasoning);

        if (newSeasoning == 1f) {
            Bids.LOG.debug("Item fully seasoned in slot: " + slot);
            ItemStack output = recipe.getCraftingResult(storage[slot]);
            storage[slot] = output.copy();
            onStorageChanged();
        }
    }

    private boolean isCovered() {
        int highestY = worldObj.getPrecipitationHeight(xCoord, zCoord) - 1;
        if (highestY == yCoord) {
            // Rain falls directly on the wood pile
            return false;
        }

        int y = yCoord;
        while (y++ < highestY) {
            TileEntity te = worldObj.getTileEntity(xCoord, y, zCoord);
            if (te instanceof TileEntityWoodPile) {
                // Another wood pile is above
                return ((TileEntityWoodPile) te).isCovered();
            }

            if (isBlockAboveCovering(y)) {
                return true;
            }
        }

        return false;
    }

    private boolean isBlockAboveCovering(int y) {
        Block block = worldObj.getBlock(xCoord, y, zCoord);
        if (block instanceof BlockGlass || block instanceof BlockStainedGlass
                || block instanceof BlockRoof) {
            return true;
        }

        if (block instanceof BlockCustomLeaves || block instanceof BlockFruitLeaves) {
            // Mostly for consistency, as leaves have solid sides,
            // but branches (with leaves) do not
            // So neither protects the wood pile
            return false;
        }

        if (worldObj.isSideSolid(xCoord, y, zCoord, ForgeDirection.UP)
                || worldObj.isSideSolid(xCoord, y, zCoord, ForgeDirection.DOWN)) {
            // Any block with top or bottom side solid
            return true;
        }

        return false;
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
    public ItemStack decrStackSize(int slot, int amount) {
        if (storage[slot] != null) {
            onStorageChanged();

            if (storage[slot].stackSize <= amount) {
                final ItemStack is = storage[slot];
                storage[slot] = null;

                return is;
            }

            return storage[slot].splitStack(amount);
        }

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        storage[slot] = itemStack;

        onStorageChanged();
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
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory() {
        woodPileOpeningCounter++;
    }

    @Override
    public void closeInventory() {
        woodPileOpeningCounter--;

        if (woodPileOpeningCounter == 0) {
            tryToPullItemsFromAbove();

            if (isEmpty()) {
                setOnFire(false);
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return false;
    }

    public boolean isBurning() {
        return isOnFire() && getBurningRate() != EnumBurningRate.NONE && findNextBurningItem() != null;
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        if (onFire && !this.onFire) {
            this.onFire = true;

            Bids.LOG.debug("Woodpile set on fire at " + xCoord + "," + yCoord + "," + zCoord);

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            doSpreadFire();

            // Reset burning tick progress
            lastBurningTicks = TFC_Time.getTotalTicks();
            totalBurningTicks = 0;
            totalBurningTemp = 0;
            totalBurningItems = 0;

            // Reset charcoal ticks
            lastCharcoalTicks = TFC_Time.getTotalTicks();
        } else if (!onFire && this.onFire) {
            this.onFire = false;

            Bids.LOG.debug("Woodpile extinguished at " + xCoord + "," + yCoord + "," + zCoord);

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            doExtinguishFire();

            // Reset seasoning tick progress
            lastSeasoningTicks = TFC_Time.getTotalTicks();
        }
    }

    private void doCreateCharcoal() {
        int totalCharcoalCount = 0;

        for (ItemStack itemStack : storage) {
            if (itemStack != null) {
                if (WoodPileHelper.isItemValidWoodPileItemForCharcoal(itemStack)) {
                    totalCharcoalCount += getCharcoalCountForWoodType(itemStack);
                }
            }
        }

        int charcoalCount = Math.min(Math.round(totalCharcoalCount / 16f) + worldObj.rand.nextInt(3), 8);

        Arrays.fill(storage, null);

        if (charcoalCount > 0) {
            worldObj.setBlock(xCoord, yCoord, zCoord, TFCBlocks.charcoal, charcoalCount, 0x2);
            Bids.LOG.info("Created " + charcoalCount + " charcoal at " + xCoord + "," + yCoord + "," + zCoord);
        } else {
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            Bids.LOG.info("Created no charcoal at " + xCoord + "," + yCoord + "," + zCoord);
        }

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
            if (te instanceof TileEntityWoodPile) {
                TileEntityWoodPile neighbor = (TileEntityWoodPile) te;
                if (neighbor.isOnFire() && neighbor.isMakingCharcoal()) {
                    neighbor.doCreateCharcoal();
                }
            }
        }
    }

    private int getCharcoalCountForWoodType(ItemStack itemStack) {
        EnumWoodHardness hardness = EnumWoodHardness.fromDamage(itemStack.getItemDamage());
        switch (hardness) {
            case HARD:
                return 7; // 7 - 8
            case MODERATE:
                return 5; // 5 - 7
            default:
                return 4; // 4 - 6;
        }
    }

    private float getPitchRateForWoodType(ItemStack itemStack) {
        EnumWoodFatness fatness = EnumWoodFatness.fromDamage(itemStack.getItemDamage());
        if (!BidsOptions.WoodPile.pitchResinousWoodOnly || fatness.isResinous()) {
            return fatness.getResinRate() * BidsOptions.WoodPile.pitchYieldMultiplier;
        } else {
            return 0;
        }
    }

    public void tryToSpreadFire() {
        if (onFire) {
            doSpreadFire();
        }
    }

    private void doSpreadFire() {
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
            if (te instanceof TileEntityWoodPile) {
                TileEntityWoodPile neighbor = (TileEntityWoodPile) te;
                neighbor.setOnFire(true);
            }
        }

        // Try to ignite neighbors only when actually burning items
        if (isBurning()) {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                // Set fire to non-charcoal pit cover blocks
                if (canCharcoalPitBlockBurnDown(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ, d)) {
                    replaceNeighborBlockWithFire(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ, BidsBlocks.light);
                }
            }

            // When the wood pile is large enough
            // Fire is spread 2 blocks above to any flammable blocks around
            if (worldObj.isAirBlock(xCoord, yCoord + 1, zCoord) && worldObj.isAirBlock(xCoord, yCoord + 2, zCoord)) {
                replaceNeighborBlockWithFire(xCoord, yCoord + 2, zCoord, Blocks.air);
            }
        }
    }

    private void replaceNeighborBlockWithFire(int x, int y, int z, Block alternativeBlock) {
        Block block = worldObj.getBlock(x, y, z);
        if (WoodPileHelper.canPlaceFireBlockAt(worldObj, x, y, z)) {
            if (block != Blocks.fire) {
                Bids.LOG.debug("{} => {}", block.getUnlocalizedName(), Blocks.fire.getUnlocalizedName());
                worldObj.setBlock(x, y, z, Blocks.fire);
            }
        } else if (block != alternativeBlock) {
            Bids.LOG.debug("{} => {}", block.getUnlocalizedName(), alternativeBlock.getUnlocalizedName());
            worldObj.setBlock(x, y, z, alternativeBlock);
        }
    }

    public void tryToCatchFire() {
        if (!onFire && canCatchFire()) {
            catchFireDelayedTimer.delay(CATCH_FIRE_DELAY);
        }
    }

    private boolean canCatchFire() {
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (worldObj.getBlock(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ) == Blocks.fire) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidCharcoalPitBlock(int x, int y, int z, ForgeDirection d) {
        // Check if the block keeps char coal pit from burning
        Block block = worldObj.getBlock(x, y, z);

        // Another woodpile is allowed
        if (block == BidsBlocks.woodPile) {
            return true;
        }

        // Hopper below is allowed
        if (d == ForgeDirection.DOWN && block instanceof BlockHopper) {
            return true;
        }

        // Or default to TFC rules
        return TFC_Core.isValidCharcoalPitCover(block);
    }

    private boolean canCharcoalPitBlockBurnDown(int x, int y, int z, ForgeDirection d) {
        // Check if block burns down when touching a wood pile that is on fire
        Block block = worldObj.getBlock(x, y, z);

        // As an exception to the condition below
        // firepit needs to burn down, and it realistically should,
        // which is non-opaque block with material set as ground
        if (block instanceof BlockFirepit) {
            return true;
        }

        // Non-opaque blocks made from materials that are obviously not flammable
        // Basically these are the blocks that should not burn down
        // even though they might not keep the charcoal pit closed airtight
        // Examples are anvils, hoppers, stone walls and rock carvings
        // This also protects chiseled plank blocks, because they are technically Material.rock,
        // and any other block with incorrect material for the matter,
        // however it is not the scope of this check to make exceptions for blocks with incorrect material,
        // and it is left to the player's discretion to do foolish things, or not.
        if (!block.isOpaqueCube() &&
            (block.getMaterial() == Material.rock ||
            block.getMaterial() == Material.iron ||
            block.getMaterial() == Material.glass ||
            block.getMaterial() == Material.ground ||
            block.getMaterial() == Material.sand)) {
            return false;
        }

        // Else burn any blocks that aren't a valid charcoal pit wall
        return !isValidCharcoalPitBlock(x, y, z, d);
    }

    private void doExtinguishFire() {
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            Block block = worldObj.getBlock(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
            if (block == Blocks.fire || block == BidsBlocks.light) {
                Bids.LOG.debug("{} => {}", block.getUnlocalizedName(), Blocks.air.getUnlocalizedName());
                worldObj.setBlockToAir(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
                worldObj.markBlockForUpdate(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected List<EntityItem> detectEntityItems() {
        if (worldObj.isAirBlock(xCoord, yCoord + 1, zCoord)) {
            final AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord,
                xCoord + 1, yCoord + 1.1, zCoord + 1);
            return (List<EntityItem>) worldObj.getEntitiesWithinAABB(EntityItem.class, bounds);
        }

        return Collections.emptyList();
    }

    @Override
    public float getHeatSourceTemp() {
        return cachedHeatSourceTemp;
    }

    @Override
    public void onTileEntityMessage(WoodPileMessage message) {
        switch (message.getAction()) {
            case ACTION_UPDATE:
                worldObj.markBlockForUpdate(message.getXCoord(), message.getYCoord(), message.getZCoord());
                Bids.LOG.debug("Client updated at: " + message.getXCoord() + ", " + message.getYCoord() + ", "
                        + message.getZCoord());
                break;

            case ACTION_RETRIEVE_ITEM:
                retrieveItemAtIndex(message.getSelectedItemIndex(), message.getPlayer());
                Bids.LOG.debug("Item retrieved");
                break;
        }
    }

    public static void sendUpdateMessage(World world, int x, int y, int z) {
        TargetPoint tp = new TargetPoint(world.provider.dimensionId, x, y, z, 255);
        Bids.network.sendToAllAround(new WoodPileMessage(x, y, z, TileEntityWoodPile.ACTION_UPDATE), tp);
        Bids.LOG.debug("Sent update message");
    }

    public static void sendRetrieveItem(World world, int x, int y, int z, int index, EntityPlayer player) {
        Bids.network.sendToServer(new WoodPileMessage(x, y, z, TileEntityWoodPile.ACTION_RETRIEVE_ITEM)
                .setSelectedItemIndex(index)
                .setPlayer(player));
        Bids.LOG.debug("Send retrieve item message " + index);
    }

}
