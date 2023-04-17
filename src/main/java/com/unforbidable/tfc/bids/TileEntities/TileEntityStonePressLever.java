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
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityStonePressLever extends TileEntity implements IMessageHanldingTileEntity<TileEntityUpdateMessage> {

    public static final int MAX_STORAGE = 2;

    final ItemStack[] storage = new ItemStack[MAX_STORAGE];

    private static final int SLOT_LOG = 0;
    private static final int SLOT_EXTRA = 1; // Rope

    public static final int PART_UNDEFINED = 0; // Briefly used during initialization
    public static final int PART_BASE = 1; // Over the pressing stone
    public static final int PART_FREE = 2; // Hanging free
    public static final int PART_WEIGHT = 3; // Holding weight stone

    int leverPart = PART_UNDEFINED;

    int orientation = 0;

    boolean clientNeedToUpdate = false;

    public TileEntityStonePressLever() {
        super();
    }

    public ItemStack getLogItem() {
        return storage[SLOT_LOG];
    }

    public void setLogItem(ItemStack is) {
        storage[SLOT_LOG] = is;
    }

    public int getLeverPart() {
        return leverPart;
    }

    public void setLeverPart(int leverPart) {
        this.leverPart = leverPart;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public ForgeDirection getSaddleQuernOutputForgeDirection() {
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
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }
        }
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
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeLeverDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        readLeverDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeLeverDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readLeverDataFromNBT(tag);
    }

    public void writeLeverDataToNBT(NBTTagCompound tag) {
        tag.setInteger("orientation", orientation);
        tag.setInteger("leverPart", leverPart);

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

    public void readLeverDataFromNBT(NBTTagCompound tag) {
        orientation = tag.getInteger("orientation");
        leverPart = tag.getInteger("leverPart");

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

    public boolean setRopeItem(ItemStack rope) {
        if (getLeverPart() != PART_WEIGHT || hasRope() || !isWeightStoneWithRopeBelow() || !isValidRopeItem(rope)) {
            return false;
        }

        ItemStack is = rope.copy();
        is.stackSize = 1;
        storage[SLOT_EXTRA] = is;

        rope.stackSize--;

        Bids.LOG.debug("Rope attached to lever: " + is.getDisplayName());

        updateClient();

        return true;
    }

    private boolean isWeightStoneWithRopeBelow() {
        if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityStonePressWeight) {
            TileEntityStonePressWeight weightTileEntity = (TileEntityStonePressWeight) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);

            return weightTileEntity.hasRope();
        }

        return false;
    }

    public boolean hasRope() {
        return getLeverPart() == PART_WEIGHT && storage[SLOT_EXTRA] != null;
    }

    public boolean retrieveRope(EntityPlayer player) {
        if (!hasRope()) {
            return false;
        }

        final ItemStack is = storage[SLOT_EXTRA];
        final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, is);
        worldObj.spawnEntityInWorld(ei);

        storage[SLOT_EXTRA] = null;

        Bids.LOG.debug("Rope retrieved from lever: " + is.getDisplayName());

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
