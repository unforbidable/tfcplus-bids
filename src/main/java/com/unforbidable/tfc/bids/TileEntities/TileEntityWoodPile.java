package com.unforbidable.tfc.bids.TileEntities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dunk.tfc.Blocks.BlockRoof;
import com.dunk.tfc.Blocks.Devices.BlockHopper;
import com.dunk.tfc.Blocks.Flora.BlockFruitLeaves;
import com.dunk.tfc.Blocks.Vanilla.BlockCustomLeaves;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Core.Vector3f;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCOptions;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.Core.WoodPile.*;
import com.unforbidable.tfc.bids.api.BidsGui;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Enums.EnumWoodHardness;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningManager;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockStainedGlass;
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
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityWoodPile extends TileEntity implements IInventory, IMessageHanldingTileEntity<WoodPileMessage> {

    public static final int MAX_STORAGE = 16;

    public static final float SEASONING_EXPOSED_MULTIPLIER = 1f;
    public static final float SEASONING_COVERED_MULTIPLIER = 1.5f;

    static final long SEASONING_INTERVAL = TFC_Time.HOUR_LENGTH;

    static final int ACTION_UPDATE = 1;
    static final int ACTION_RETRIEVE_ITEM = 2;

    final ItemStack[] storage = new ItemStack[MAX_STORAGE];

    long lastSeasoningTicks = 0;
    boolean initialized;
    int orientation;

    int selectedItemIndex = -1;

    boolean clientNeedToUpdate = false;
    Timer seasoningTimer = new Timer(10);
    int woodPileOpeningCounter = 0;

    boolean isItemBoundsCacheActual = false;
    List<WoodPileItemBounds> itemBoundsCache = new ArrayList<WoodPileItemBounds>();

    EntityPlayer openDelayedGUIplayer = null;

    boolean onFire = false;
    long hoursOnFire = 0;

    Timer torchDetectionTimer = new Timer(20);
    Timer torchDetectionClientTimer = new Timer(10);
    long torchDetectedTicks = 0;

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
            Bids.LOG.info("Wood pile above found");

            int occupied = getActualOccupiedSlotCount();
            while (occupied < 16) {
                Bids.LOG.info("Trying to pull an item from wood pile above");

                TileEntityWoodPile woodPileAbove = (TileEntityWoodPile) teAbove;

                // Check if large item fits
                boolean largeItemFits = occupied <= 12;
                ItemStack retrieved = woodPileAbove.retrieveFirstAvailableItemToPullDown(largeItemFits);
                if (retrieved != null) {
                    Bids.LOG.info("Pulled item " + retrieved.getDisplayName() + "[" + retrieved.stackSize + "] from wood pile above");

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
        } else {
            isItemBoundsCacheActual = false;
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // One time only right after creation
            if (!initialized) {
                lastSeasoningTicks = TFC_Time.getTotalTicks();

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
            if (!onFire && torchDetectionTimer.tick()) {
                EntityItem ei = detectTorch();
                if (ei != null) {
                    Bids.LOG.debug("Torch detected above woodpile at " + xCoord + "," + yCoord + "," + zCoord);

                    if (torchDetectedTicks == 0) {
                        torchDetectedTicks = TFC_Time.getTotalTicks();
                    } else if (torchDetectedTicks + 160 < TFC_Time.getTotalTicks()) {
                        setOnFire(true);
                        ei.setDead();
                    }
                } else {
                    torchDetectedTicks = 0;
                }
            }

            // Check if enough time had passed
            // for seasoning interval
            if (seasoningTimer.tick() && TFC_Time.getTotalTicks() > lastSeasoningTicks + SEASONING_INTERVAL) {
                if (onFire) {
                    // No seasoning when making charcoal
                    lastSeasoningTicks = TFC_Time.getTotalTicks();
                } else {
                    seasonItems();
                }
            }

            if (openDelayedGUIplayer != null) {
                openDelayedGUIplayer.openGui(Bids.instance, BidsGui.woodPileGui, worldObj, xCoord, yCoord, zCoord);
                openDelayedGUIplayer = null;
            }
        } else {
            // Torch detection client side to show sparks
            if (!onFire && torchDetectionClientTimer.tick()) {
                if (detectTorch() != null) {
                    worldObj.spawnParticle("lava", xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, -0.5F + worldObj.rand.nextFloat(), -0.5F + worldObj.rand.nextFloat(), -0.5F + worldObj.rand.nextFloat());
                }
            }
        }
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
        tag.setLong("hoursOnFire", hoursOnFire);

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
    }

    public void readWoodPileDataFromNBT(NBTTagCompound tag) {
        lastSeasoningTicks = tag.getLong("lastSeasoningTicks");
        initialized = tag.getBoolean("clientInitialized");
        orientation = tag.getInteger("orientation");
        onFire = tag.getBoolean("onFire");
        hoursOnFire = tag.getLong("hoursOnFire");

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
    }

    public void onWoodPileBroken() {
        for (int i = 0; i < getSizeInventory(); i++) {
            if (storage[i] != null) {
                final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, storage[i]);
                worldObj.spawnEntityInWorld(ei);
            }
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        if (onFire && !this.onFire) {
            this.onFire = true;
            hoursOnFire = TFC_Time.getTotalHours();

            Bids.LOG.debug("Woodpile set on fire at " + xCoord + "," + yCoord + "," + zCoord);

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            doSpreadFire();
        } else if (!onFire && this.onFire) {
            this.onFire = false;
            hoursOnFire = 0;

            Bids.LOG.debug("Woodpile extinguished at " + xCoord + "," + yCoord + "," + zCoord);

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            doExtinguishFire();
        }
    }

    public void tryToCreateCharcoal() {
        Bids.LOG.debug("Trying to create charcoal at " + xCoord + "," + yCoord + "," + zCoord);

        if (isOnFire() && hoursOnFire + TFCOptions.charcoalPitBurnTime < TFC_Time.getTotalHours()) {
            doCreateCharcoal();
        }
    }

    private void doCreateCharcoal() {
        boolean invalidItemsFound = false;
        int charcoalCount = 0;

        for (ItemStack itemStack : storage) {
            if (itemStack != null) {
                if (itemStack.getItem() == BidsItems.firewoodSeasoned) {
                    float percent = getCharcoalPercentageForWoodType(itemStack);
                    charcoalCount += worldObj.rand.nextInt(100) < percent ? 1 : 0;
                } else {
                    invalidItemsFound = true;
                    break;
                }
            }
        }

        if (invalidItemsFound) {
            // Invalid woodpile for charcoal making, leave as is
            setOnFire(false);

            Bids.LOG.debug("Invalid item found for charcoal at " + xCoord + "," + yCoord + "," + zCoord);
        } else {
            Arrays.fill(storage, null);

            if (charcoalCount > 0)
            {
                worldObj.setBlock(xCoord, yCoord, zCoord, TFCBlocks.charcoal, Math.min(charcoalCount, 8), 0x2);
                Bids.LOG.debug("Created " + charcoalCount + " charcoal at " + xCoord + "," + yCoord + "," + zCoord);
            }
            else
            {
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                Bids.LOG.debug("Created no charcoal at " + xCoord + "," + yCoord + "," + zCoord);
            }
        }

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
            if (te instanceof TileEntityWoodPile) {
                TileEntityWoodPile neighbor = (TileEntityWoodPile) te;
                if (neighbor.isOnFire()) {
                    neighbor.doCreateCharcoal();
                }
            }
        }
    }

    private float getCharcoalPercentageForWoodType(ItemStack itemStack) {
        EnumWoodHardness hardness = EnumWoodHardness.fromDamage(itemStack.getItemDamage());
        switch (hardness) {
            case HARD:
                return 45 + worldObj.rand.nextInt(6); // ~7.7
            case MODERATE:
                return 35 + worldObj.rand.nextInt(16); // ~6.9
            default:
                return 25 + worldObj.rand.nextInt(26); // ~6.1 (original TFC+ rating)
        }
    }

    public void tryToSpreadFire() {
        if (onFire) {
            doSpreadFire();
        }
    }

    private void doSpreadFire() {
        ArrayDeque<Vector3f> blocksToBeSetOnFire = new ArrayDeque<Vector3f>();

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
            if (te instanceof TileEntityWoodPile) {
                TileEntityWoodPile neighbor = (TileEntityWoodPile) te;
                neighbor.setOnFire(true);
            } else {
                Block block = worldObj.getBlock(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
                if (!TFC_Core.isValidCharcoalPitCover(block)) {
                    // Hopper below is allowed
                    if (!(d.offsetY == -1 && block instanceof BlockHopper)) {
                        blocksToBeSetOnFire.add(new Vector3f(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ));
                    }
                }
            }
        }

        while (blocksToBeSetOnFire.size() > 0) {
            Vector3f pos = blocksToBeSetOnFire.poll();
            if (worldObj.getBlock((int) pos.x, (int) pos.y, (int) pos.z) != Blocks.fire
                && worldObj.getBlock((int) pos.x, (int) pos.y - 1, (int) pos.z) != Blocks.air) {
                worldObj.setBlock((int) pos.x, (int) pos.y, (int) pos.z, Blocks.fire);
                worldObj.markBlockForUpdate((int) pos.x, (int) pos.y, (int) pos.z);
            }
        }
    }

    private void doExtinguishFire() {
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            Block block = worldObj.getBlock(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
            if (block == Blocks.fire) {
                worldObj.setBlockToAir(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
                worldObj.markBlockForUpdate(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected EntityItem detectTorch() {
        if (worldObj.isAirBlock(xCoord, yCoord + 1, zCoord)) {
            final AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord,
                xCoord + 1, yCoord + 1.1, zCoord + 1);
            final List<EntityItem> list = (List<EntityItem>) worldObj.getEntitiesWithinAABB(EntityItem.class, bounds);

            for (EntityItem entity : list) {
                final ItemStack is = entity.getEntityItem();
                if (is.getItem() == Item.getItemFromBlock(TFCBlocks.torch)) {
                    return entity;
                }
            }
        }

        return null;
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
