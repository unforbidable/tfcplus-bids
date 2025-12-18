package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.Core.SoakingSurface.SoakingSurfaceItem;
import com.unforbidable.tfc.bids.Core.SoakingSurface.SoakingSurfaceSlotProgress;
import com.unforbidable.tfc.bids.api.Crafting.SoakingSurfaceManager;
import com.unforbidable.tfc.bids.api.Crafting.SoakingSurfaceRecipe;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntitySoakingSurface extends TileEntity implements IMessageHanldingTileEntity<TileEntityUpdateMessage> {

    public static final int MAX_STORAGE = 4;

    private final SoakingSurfaceItem[] storage = new SoakingSurfaceItem[MAX_STORAGE];

    private int originalBlockId = 0;
    private int originalBlockMetadata = 0;

    private int selectedSlot = -1;

    boolean clientNeedToUpdate = false;
    boolean clientDataLoaded = false;

    boolean initialized = false;

    public boolean isClientDataLoaded() {
        return clientDataLoaded;
    }

    public int getOriginalBlockId() {
        return originalBlockId;
    }

    public int getOriginalBlockMetadata() {
        return originalBlockMetadata;
    }

    public void setOriginalBlock(Block block, int meta) {
        originalBlockId = Block.getIdFromBlock(block);
        originalBlockMetadata = meta;
    }

    public void setSelectedSlot(int selectedSlot) {
        if (this.selectedSlot != selectedSlot) {
            this.selectedSlot = selectedSlot;
        }
    }

    public void clearSelectedSection() {
        selectedSlot = -1;
    }

    public int getSelectedSection() {
        return selectedSlot;
    }

    public ItemStack getSelectedActualItem() {
        if (selectedSlot != -1) {
            return getSlotActualItem(selectedSlot);
        }

        return null;
    }

    public SoakingSurfaceSlotProgress getSelectedItemProgress() {
        if (selectedSlot != -1) {
            return getSlotProgress(selectedSlot);
        }

        return null;
    }

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1.5, zCoord + 1);
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // One time only right after creation
            if (!initialized) {
                initialized = true;
            }

            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }
        }
    }

    public boolean hasValidFluidBlock() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                SoakingSurfaceRecipe recipe = SoakingSurfaceManager.findMatchingRecipe(storage[i].soakingItem, worldObj, xCoord, yCoord + 1, zCoord);
                if (recipe == null) {
                    return false;
                }
            }
        }

        return true;
    }

    public void onSoakingSurfaceBroken() {
        dropItems();
        restoreSoakingSurface();
    }

    private void dropItems() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            ItemStack is = getSlotActualItem(i);
            if (is != null) {
                final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.1, zCoord + 0.5, is);
                worldObj.spawnEntityInWorld(ei);
            }
        }
    }

    public ItemStack getSlotActualItem(int slot) {
        SoakingSurfaceSlotProgress slotProgress = getSlotProgress(slot);
        if (slotProgress != null) {
            return slotProgress.progress == 1 ? slotProgress.result : slotProgress.input;
        } else {
            return null;
        }
    }

    private SoakingSurfaceSlotProgress getSlotProgress(int slot) {
        if (storage[slot] != null) {
            SoakingSurfaceRecipe recipe = SoakingSurfaceManager.findMatchingRecipe(storage[slot].soakingItem, worldObj, xCoord, yCoord + 1, zCoord);
            if (recipe != null) {
                long elapsed = TFC_Time.getTotalTicks() - storage[slot].soakingStartTicks;
                float ticksNeeded = recipe.getHours() * TFC_Time.HOUR_LENGTH;
                float progress = elapsed > ticksNeeded ? 1 : elapsed / ticksNeeded;
                return new SoakingSurfaceSlotProgress(storage[slot].soakingItem, recipe.getResult(storage[slot].soakingItem).copy(), progress);
            } else {
                // dummy slot progress when recipe gets invalidated
                return new SoakingSurfaceSlotProgress(storage[slot].soakingItem, storage[slot].soakingItem, 0);
            }
        }

        return null;
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeSoakingSurfaceDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        // This forces client render after NBT data is loaded
        // until then the original block cannot be rendered
        if (worldObj.isRemote && !clientDataLoaded) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientDataLoaded = true;
        }

        readSoakingSurfaceDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeSoakingSurfaceDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readSoakingSurfaceDataFromNBT(tag);
    }

    public void writeSoakingSurfaceDataToNBT(NBTTagCompound tag) {
        tag.setShort("originalBlockId", (short) originalBlockId);
        tag.setByte("originalBlockMetadata", (byte) originalBlockMetadata);

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

    public void readSoakingSurfaceDataFromNBT(NBTTagCompound tag) {
        originalBlockId = tag.getShort("originalBlockId");
        originalBlockMetadata = tag.getByte("originalBlockMetadata");

        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("storage", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = SoakingSurfaceItem.loadItemStackFromNBT(itemTag);
        }
    }

    public boolean canPlaceItem(ItemStack item) {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] == null) {
                return true;
            }
        }

        return false;
    }

    public boolean placeItem(ItemStack item) {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] == null) {
                storage[i] = new SoakingSurfaceItem(item, TFC_Time.getTotalTicks());

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;

                return true;
            }
        }

        return false;
    }

    public boolean retrieveItem(int slot, EntityPlayer player) {
        ItemStack is = getSlotActualItem(slot);
        if (is != null) {
            final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, is);
            worldObj.spawnEntityInWorld(ei);

            storage[slot] = null;

            if (getTotalItems() > 0) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;
            } else {
                restoreSoakingSurface();
            }

            return true;
        }

        return false;
    }

    private void restoreSoakingSurface() {
        Block block = Block.getBlockById(originalBlockId);
        worldObj.setBlock(xCoord, yCoord, zCoord, block, originalBlockMetadata, 2);
    }

    private int getTotalItems() {
        int count = 0;
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                count++;
            }
        }

        return count;
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
