package com.unforbidable.tfc.bids.TileEntities;

import java.util.ArrayList;
import java.util.List;

import com.dunk.tfc.Blocks.BlockRoof;
import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileBoundsIterator;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileItemBounds;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileMessage;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningManager;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningRecipe;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockStainedGlass;
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
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityWoodPile extends TileEntity implements IInventory, IMessageHanldingTileEntity<WoodPileMessage> {

    public static final int MAX_STORAGE = 16;

    static final long SEASONING_TICKS = TFC_Time.DAY_LENGTH;
    static final long SEASONING_INTERVAL = TFC_Time.HOUR_LENGTH;
    static final float SEASONING_COVERED_BONUS = 1.5f;

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

    public TileEntityWoodPile() {
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
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
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] == null) {
                return false;
            }
        }

        return true;
    }

    public boolean addItem(ItemStack itemStack) {
        if (isFull()) {
            return false;
        }

        onStorageChanged();

        // When adding items by right clicking
        // we want to put items on top of the pile
        // The current solution is to move
        // all items to the beginning of the array
        int shift = 0;
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] == null) {
                shift++;
            } else if (shift > 0) {
                storage[i - shift] = storage[i];
                storage[i] = null;
            }
        }

        int totalItemsAdded = 0;
        while (itemStack.stackSize > totalItemsAdded) {
            ItemStack toAdd = itemStack.copy();
            toAdd.stackSize = 1;

            if (!addItem(toAdd, 0)) {
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

    protected boolean addItem(ItemStack itemStack, float seasoning) {
        if (itemStack.stackSize != 1) {
            Bids.LOG.warn("Denied attempt to add stack of size other than 1 to a log pile");
            return false;
        }

        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] == null) {
                storage[i] = itemStack;

                return true;
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

            if (woodPileOpeningCounter == 0 && isEmpty()) {
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            } else {
                onStorageChanged();
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

            // Check if enough time had passed
            // for seasoning interval
            if (seasoningTimer.tick() && TFC_Time.getTotalTicks() > lastSeasoningTicks + SEASONING_INTERVAL) {
                seasonItems();
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
        readFromNBT(tag);
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
        float seasoningDelta = ticksSinceLastSeasoning / (float) SEASONING_TICKS;

        lastSeasoningTicks = TFC_Time.getTotalTicks();

        Bids.LOG.debug("Ticks since last seasoning: " + ticksSinceLastSeasoning
                + ", progress to add: " + seasoningDelta);

        boolean coverageChecked = false;
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                if (SeasoningManager.hasMatchingRecipe(storage[i])) {
                    // Lazy coverage calculation
                    // only when there is anything to season
                    // Apply bonus once
                    if (!coverageChecked) {
                        if (isCovered()) {
                            seasoningDelta *= SEASONING_COVERED_BONUS;
                        }
                        coverageChecked = true;
                    }

                    seasonItemInSlot(i, seasoningDelta);
                }
            }
        }
    }

    protected void seasonItemInSlot(int slot, float seasoningDelta) {
        final SeasoningRecipe recipe = SeasoningManager.getMatchingRecipe(storage[slot]);
        float seasoningDeltaAdjusted = seasoningDelta / recipe.getDurationMultipliter(storage[slot]);

        float seasoning = SeasoningHelper.getItemSeasoningTag(storage[slot]);
        seasoning = Math.min(1, seasoning + seasoningDeltaAdjusted);
        SeasoningHelper.setItemSeasoningTag(storage[slot], seasoning);

        Bids.LOG.debug("Item " + storage[slot].getDisplayName()
                + " updated seasoning: " + seasoning);

        if (seasoning == 1f) {
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

        if (woodPileOpeningCounter == 0 && isEmpty()) {
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return false;
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
