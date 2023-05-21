package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Items.*;
import com.dunk.tfc.Items.Tools.*;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackHelper;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.Items.ItemBucketRopeEmpty;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.item.ItemShears;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityWallHook extends TileEntity implements IMessageHanldingTileEntity<TileEntityUpdateMessage> {

    public static final int HANG_POS_NONE = 0;
    public static final int HANG_POS_HIGH = 1;
    public static final int HANG_POS_MID = 2;
    public static final int HANG_POS_LOW = 3;

    private static final int MAX_STORAGE = 1;

    private static final int SLOT_ITEM = 0;

    private static final int DRYING_TIMER_INTERVAL = 20;

    ItemStack[] storage = new ItemStack[MAX_STORAGE];
    int orientation;

    Timer dryingTimer = new Timer(DRYING_TIMER_INTERVAL);

    boolean clientNeedToUpdate = false;

    public void onBlockBroken() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                    storage[i]);
                worldObj.spawnEntityInWorld(ei);

                storage[i] = null;
            }
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public ItemStack getPlacedItem() {
        return storage[SLOT_ITEM];
    }

    public boolean tryPlaceItemStack(ItemStack itemStack, EntityPlayer player) {
        if (storage[SLOT_ITEM] == null && canPlaceItemStack(itemStack)) {
            storage[SLOT_ITEM] = itemStack.copy();
            storage[SLOT_ITEM].stackSize = 1;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            clientNeedToUpdate = true;

            player.inventory.consumeInventoryItem(itemStack.getItem());

            return true;
        }

        return false;
    }

    private boolean canPlaceItemStack(ItemStack is) {
        return is.getItem() instanceof ItemTool ||
            is.getItem() instanceof ItemHoe ||
            is.getItem() instanceof ItemProPick ||
            is.getItem() instanceof ItemBow ||
            is.getItem() instanceof ItemSword ||
            is.getItem() instanceof ItemShears ||
            is.getItem() instanceof ItemSpindle ||
            is.getItem() instanceof ItemTrowel ||
            is.getItem() instanceof ItemLeatherBag && ((ISize)is.getItem()).getWeight(is) != EnumWeight.HEAVY ||
            is.getItem() instanceof ItemWaterskin ||
            is.getItem() instanceof ItemClothing;
    }

    public int getItemStackHangPosition() {
        ItemStack is = storage[SLOT_ITEM];

        if (is != null) {
            if (is.getItem() instanceof ItemClothing ||
                is.getItem() instanceof ItemLeatherBag) {
                return HANG_POS_LOW;
            } else if (is.getItem() instanceof ItemBow ||
                is.getItem() instanceof ItemWaterskin) {
                return HANG_POS_MID;
            } else {
                return HANG_POS_HIGH;
            }
        } else {
            return HANG_POS_NONE;
        }
    }

    public boolean tryRetrieveItemStack(EntityPlayer player) {
        if (storage[SLOT_ITEM] != null) {
            ItemStack is = storage[SLOT_ITEM];

            final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, is);
            worldObj.spawnEntityInWorld(ei);

            storage[SLOT_ITEM] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            clientNeedToUpdate = true;

            return true;
        }

        return false;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }

            // Check if enough time had passed
            // for drying interval
            if (dryingTimer.tick()) {
                dryClothes();
            }
        }
    }

    private void dryClothes() {
        for (ItemStack itemStack : storage) {
            if (itemStack != null && itemStack.getItem() instanceof ItemClothing) {
                float temp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord, zCoord);
                if (TFC_Core.isExposedToRain(worldObj, xCoord, yCoord, zCoord) && temp > 0) {
                    DryingRackHelper.handleClothesInRain(itemStack, DRYING_TIMER_INTERVAL);
                } else if (temp > 0) {
                    DryingRackHelper.handleNormalDry(worldObj, xCoord, yCoord, zCoord, itemStack, DRYING_TIMER_INTERVAL);
                }
            }
        }
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeTileEntityDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        readTileEntityDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeTileEntityDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readTileEntityDataFromNBT(tag);
    }

    public void writeTileEntityDataToNBT(NBTTagCompound tag) {
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

    public void readTileEntityDataFromNBT(NBTTagCompound tag) {
        orientation = tag.getInteger("orientation");

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
