package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityStonePressWeight extends TileEntity implements IMessageHanldingTileEntity<TileEntityUpdateMessage> {

    public static final int MAX_STORAGE = 1;

    final ItemStack[] storage = new ItemStack[MAX_STORAGE];

    private static final int SLOT_ROPE = 0;

    boolean clientNeedToUpdate = false;

    public TileEntityStonePressWeight() {
        super();
    }

    public void onBlockBroken() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, storage[i]);
                worldObj.spawnEntityInWorld(ei);

                storage[i] = null;
            }
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }
        }
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeWeightDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        readWeightDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeWeightDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readWeightDataFromNBT(tag);
    }

    public void writeWeightDataToNBT(NBTTagCompound tag) {
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

    public void readWeightDataFromNBT(NBTTagCompound tag) {
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

    public boolean isLifted() {
        if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityStonePressLever) {
            TileEntityStonePressLever leverTileEntity = (TileEntityStonePressLever) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
            return hasRope() && leverTileEntity.hasRope();
        }

        return false;
    }

    public boolean setRopeItem(ItemStack rope) {
        if (isLifted() || hasRope() || !isValidRopeItem(rope)) {
            return false;
        }

        ItemStack is = rope.copy();
        is.stackSize = 1;
        storage[SLOT_ROPE] = is;

        rope.stackSize--;

        Bids.LOG.debug("Rope attached to weight: " + is.getDisplayName());

        updateClient();

        return true;
    }

    public boolean hasRope() {
        return storage[SLOT_ROPE] != null;
    }

    public boolean retrieveRope(EntityPlayer player) {
        if (isLifted() || !hasRope()) {
            return false;
        }

        final ItemStack is = storage[SLOT_ROPE];
        final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, is);
        worldObj.spawnEntityInWorld(ei);

        storage[SLOT_ROPE] = null;

        Bids.LOG.debug("Rope retrieved from weight: " + is.getDisplayName());

        updateClient();

        return true;
    }

    private void updateClient() {
        if (worldObj != null && !worldObj.isRemote) {
            Bids.LOG.debug("Update " + xCoord + "," + yCoord + "," + zCoord);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            clientNeedToUpdate = true;
        }
    }

    public boolean isValidRopeItem(ItemStack itemStack) {
        return itemStack.getItem() == TFCItems.rope;
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
