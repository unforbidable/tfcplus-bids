package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackItem;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackItemInfo;
import com.unforbidable.tfc.bids.api.Crafting.DryingManager;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityDryingRack extends TileEntity {

    public static final int MAX_STORAGE = 4;

    static final long DRYING_INTERVAL = TFC_Time.HOUR_LENGTH / 12;

    final DryingRackItem[] storage = new DryingRackItem[MAX_STORAGE];

    long lastDryingTicks = 0;
    boolean initialized;
    int orientation = -1;

    int selectedSection = -1;

    Timer dryingTimer = new Timer(10);

    public TileEntityDryingRack() {
        super();
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setSelectedSection(int selectedSection) {
        this.selectedSection = selectedSection;
    }

    public void clearSelectedSection() {
        selectedSection = -1;
    }

    public int getSelectedSection() {
        return selectedSection;
    }

    public ItemStack getSelectedItem() {
        if (selectedSection != -1) {
            return getItem(selectedSection);
        }

        return null;
    }

    public ItemStack getItem(int section) {
        if (storage[section] != null) {
            return storage[section].dryingItem;
        }

        return null;
    }

    public DryingRackItemInfo getSelectedItemInfo() {
        if (selectedSection != -1) {
            return getItemInfo(selectedSection);
        }

        return null;
    }

    public DryingRackItemInfo getItemInfo(int section) {
        if (storage[section] != null) {
            final DryingRecipe recipe = DryingManager.getMatchingRecipe(storage[section].dryingItem);

            if (recipe != null) {
                final long total = recipe.getDuration() * TFC_Time.HOUR_LENGTH;
                final long elapsed = (int) (TFC_Time.getTotalTicks() - storage[section].dryingStartTicks);
                final int dryingTicksRemaining = Math.max((int) (total - elapsed), 0);

                return new DryingRackItemInfo(storage[section], recipe, dryingTicksRemaining);
            }
        }

        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // One time only right after creation
            if (!initialized) {
                lastDryingTicks = TFC_Time.getTotalTicks();

                initialized = true;
            }

            // Check if enough time had passed
            // for drying interval
            if (dryingTimer.tick() && TFC_Time.getTotalTicks() > lastDryingTicks + DRYING_INTERVAL) {
                dryItems();

                lastDryingTicks = TFC_Time.getTotalTicks();
            }
        }
    }

    private void dryItems() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                // Check time passed since item was added
                // if it is greater than drying time
                // according to the matching recipe

                final DryingRackItem item = storage[i];
                final DryingRecipe recipe = DryingManager.getMatchingRecipe(item.dryingItem);

                if (recipe != null) {
                    long ticksDelta = TFC_Time.getTotalTicks() - item.dryingStartTicks;
                    if (ticksDelta > recipe.getDuration() * TFC_Time.HOUR_LENGTH) {
                        ItemStack driedItem = recipe.getCraftingResult(item.dryingItem);

                        Bids.LOG.info("Item " + item.dryingItem.getDisplayName()
                                + " dried and become " + driedItem.getDisplayName());

                        storage[i] = new DryingRackItem(driedItem, item.tyingItem, item.dryingStartTicks);

                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }

                } else {
                    // Item has no matching recipe
                    // so either item is already dried
                    // or the recipe no longer exists
                }
            }
        }
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeDryingRackDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        readDryingRackDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeDryingRackDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readDryingRackDataFromNBT(tag);
    }

    public void writeDryingRackDataToNBT(NBTTagCompound tag) {
        tag.setLong("lastDryingTicks", lastDryingTicks);
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
        tag.setTag("storage", itemTagList);
    }

    public void readDryingRackDataFromNBT(NBTTagCompound tag) {
        lastDryingTicks = tag.getLong("lastDryingTicks");
        initialized = tag.getBoolean("clientInitialized");
        orientation = tag.getInteger("orientation");

        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("storage", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = DryingRackItem.loadItemFromNBT(itemTag);
        }
    }

    public boolean placeItem(int section, ItemStack itemStack) {
        Bids.LOG.debug("Placing item on drying rack: " + itemStack.getDisplayName() + ", section: " + section);

        if (section >= 0 && section < MAX_STORAGE
                && storage[section] == null) {
            ItemStack dryingItem = new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage());
            storage[section] = new DryingRackItem(dryingItem, null, TFC_Time.getTotalTicks());

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            return true;
        }

        return false;
    }

    public void onDryingRackBroken() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                if (storage[i].dryingItem != null) {
                    final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                            storage[i].dryingItem);
                    worldObj.spawnEntityInWorld(ei);
                }

                if (storage[i].tyingItem != null) {
                    final EntityItem ei2 = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                            storage[i].tyingItem);
                    worldObj.spawnEntityInWorld(ei2);
                }
            }
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public boolean retrieveItem(int section, EntityPlayer player) {
        Bids.LOG.debug("Retrieving item from drying rack, section: " + section);

        if (section >= 0 && section < MAX_STORAGE
                && storage[section] != null) {
            if (storage[section].dryingItem != null) {
                final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ,
                        storage[section].dryingItem);
                worldObj.spawnEntityInWorld(ei);
            }

            if (storage[section].tyingItem != null) {
                final EntityItem ei2 = new EntityItem(worldObj, player.posX, player.posY, player.posZ,
                        storage[section].tyingItem);
                worldObj.spawnEntityInWorld(ei2);
            }

            storage[section] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            return true;
        }

        return false;
    }

}
