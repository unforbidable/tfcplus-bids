package com.unforbidable.tfc.bids.TileEntities;

import java.util.ArrayList;
import java.util.List;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileMessage;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningManager;
import com.unforbidable.tfc.bids.api.Crafting.SeasoningRecipe;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.client.Minecraft;
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

public class TileEntityWoodPile extends TileEntity implements IInventory, IMessageHanldingTileEntity<WoodPileMessage> {

    public static final int MAX_STORAGE = 16;

    static final long SEASONING_TICKS = TFC_Time.DAY_LENGTH;
    static final long SEASONING_INTERVAL = TFC_Time.HOUR_LENGTH;

    static final int ACTION_UPDATE = 1;

    final ItemStack[] storage = new ItemStack[MAX_STORAGE];

    long lastSeasoningTicks = 0;
    boolean initialized;

    boolean clientNeedToUpdate = false;
    Timer seasoningTimer = new Timer(10);
    int woodPileOpeningCounter = 0;

    public TileEntityWoodPile() {
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
                setInventorySlotContents(i, itemStack);

                return true;
            }
        }

        return false;
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

        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("items", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = ItemStack.loadItemStackFromNBT(itemTag);
        }
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

    protected void onItemAdded(ItemStack itemStack, int i) {
        Bids.LOG.debug("Item added " + itemStack.getDisplayName() + " to slot " + i);

        if (!worldObj.isRemote) {
            clientNeedToUpdate = true;
        }
    }

    protected void onItemRemoved(ItemStack itemStack, int i) {
        Bids.LOG.debug("Item removed " + itemStack.getDisplayName() + " from slot " + i);

        if (!worldObj.isRemote) {
            clientNeedToUpdate = true;
        }
    }

    protected void seasonItems() {
        final long ticksSinceLastSeasoning = TFC_Time.getTotalTicks() - lastSeasoningTicks;
        final float seasoningAdd = ticksSinceLastSeasoning / (float) SEASONING_TICKS;

        lastSeasoningTicks = TFC_Time.getTotalTicks();

        Bids.LOG.debug("Ticks since last seasoning: " + ticksSinceLastSeasoning
                + ", progress to add: " + seasoningAdd);

        boolean anyItemsSeasoned = false;
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                if (SeasoningManager.hasMatchingRecipe(storage[i])) {
                    seasonItemInSlot(i, seasoningAdd);

                    anyItemsSeasoned = true;
                }
            }
        }

        if (anyItemsSeasoned) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
            Bids.LOG.info("Item fully seasoned in slot: " + slot);
            ItemStack output = recipe.getCraftingResult(storage[slot]);
            decrStackSize(slot, storage[slot].stackSize);
            setInventorySlotContents(slot, output.copy());
        }
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
            if (storage[slot].stackSize <= amount) {
                onItemRemoved(storage[slot], slot);

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
        boolean isSameItem = itemStack != null && storage[slot] != null
                && ItemStack.areItemStacksEqual(itemStack, storage[slot])
                && ItemStack.areItemStackTagsEqual(itemStack, storage[slot]);

        if (storage[slot] != null && !isSameItem) {
            onItemRemoved(storage[slot], slot);
        }

        if (itemStack != null) {
            if (!isSameItem) {
                storage[slot] = itemStack;
                onItemAdded(itemStack, slot);
            }
        } else {
            storage[slot] = null;
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
                Minecraft.getMinecraft().theWorld.markBlockForUpdate(message.getXCoord(), message.getYCoord(),
                        message.getZCoord());
                Bids.LOG.debug("Received update message");
                break;
        }
    }

    public static void sendUpdateMessage(World world, int x, int y, int z) {
        TargetPoint tp = new TargetPoint(world.provider.dimensionId, x, y, z, 255);
        Bids.network.sendToAllAround(new WoodPileMessage(x, y, z, TileEntityWoodPile.ACTION_UPDATE), tp);
        Bids.LOG.debug("Sent update message");
    }

}
