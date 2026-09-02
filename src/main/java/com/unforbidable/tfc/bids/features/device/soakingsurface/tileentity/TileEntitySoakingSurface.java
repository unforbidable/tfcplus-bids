package com.unforbidable.tfc.bids.features.device.soakingsurface.tileentity;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.api.features.soaking.SoakingSurfaceRecipe;
import com.unforbidable.tfc.bids.common.network.SimpleUpdatePacket;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import com.unforbidable.tfc.bids.features.crafting.soaking.SoakingConfig;
import com.unforbidable.tfc.bids.features.device.soakingsurface.main.SoakingSurfaceHelper;
import com.unforbidable.tfc.bids.features.device.soakingsurface.main.SoakingSurfaceItem;
import com.unforbidable.tfc.bids.features.device.soakingsurface.main.SoakingSurfaceSlotProgress;
import com.unforbidable.tfc.bids.util.Timer;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

public class TileEntitySoakingSurface extends TileEntity implements PacketHandler<SimpleUpdatePacket> {

    public static final int MAX_STORAGE = 4;

    private final SoakingSurfaceItem[] storage = new SoakingSurfaceItem[MAX_STORAGE];

    private int originalBlockId = 0;
    private int originalBlockMetadata = 0;

    private final Timer checkProgressTimer = new Timer(100);

    private int selectedSlot = -1;

    private long freezingLastTrackedTicks = 0;
    private long freezingProgressDelayTicks = 0;

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

    public ItemStack getSelectedItemStack() {
        if (selectedSlot != -1) {
            return getSlotItemStack(selectedSlot);
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
                sendUpdateMessage();

                clientNeedToUpdate = false;
            }

            if (checkProgressTimer.tick()) {
                trackFreezing();
                checkProgress();
            }
        }
    }

    private void trackFreezing() {
        if (freezingLastTrackedTicks == 0) {
            freezingLastTrackedTicks = TFC_Time.getTotalTicks();
        } else {
            long nextCheck = freezingLastTrackedTicks + TFC_Time.HOUR_LENGTH;
            if (nextCheck < TFC_Time.getTotalTicks()) {
                while (nextCheck < TFC_Time.getTotalTicks()) {
                    int th = (int) (nextCheck / TFC_Time.HOUR_LENGTH);
                    int day = TFC_Time.getDayFromTotalHours(th);
                    int hour = TFC_Time.getHourOfDayFromTotalHours(th);
                    float temp = TFC_Climate.getHeightAdjustedTempSpecificDay(worldObj, day, hour, xCoord, yCoord, zCoord);
                    if (temp < 0) {
                        freezingProgressDelayTicks += TFC_Time.HOUR_LENGTH;
                    }

                    nextCheck += TFC_Time.HOUR_LENGTH;
                    freezingLastTrackedTicks += TFC_Time.HOUR_LENGTH;

                    markDirty();
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }
        }
    }

    private void checkProgress() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            SoakingSurfaceSlotProgress progress = getSlotProgress(i);
            if (progress != null && progress.progress >= 1f) {
                ItemStack result = progress.recipe.getResult(storage[i].soakingItem).copy();
                Fluid fluid = getSoakingFluid();
                FluidStack fluidStack = fluid != null ? new FluidStack(fluid, 1000) : null;

                BidsEventFactory.onSoakingItemCrafted(storage[i].soakingItem, result, fluidStack);

                storage[i] = new SoakingSurfaceItem(result, TFC_Time.getTotalTicks());

                markDirty();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;
            }
        }
    }

    public boolean hasValidFluidBlock() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                SoakingSurfaceRecipe recipe = SoakingSurfaceHelper.findMatchingRecipe(storage[i].soakingItem, worldObj, xCoord, yCoord + 1, zCoord);
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
            ItemStack is = getSlotItemStack(i);
            if (is != null) {
                final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.1, zCoord + 0.5, is);
                worldObj.spawnEntityInWorld(ei);
            }
        }
    }

    public ItemStack getSlotItemStack(int slot) {
        if (storage[slot] != null) {
            return storage[slot].soakingItem;
        } else {
            return null;
        }
    }

    private SoakingSurfaceSlotProgress getSlotProgress(int slot) {
        if (storage[slot] != null) {
            SoakingSurfaceRecipe recipe = SoakingSurfaceHelper.findMatchingRecipe(storage[slot].soakingItem, worldObj, xCoord, yCoord + 1, zCoord);
            if (recipe != null) {
                long elapsed = TFC_Time.getTotalTicks() - storage[slot].soakingStartTicks - freezingProgressDelayTicks;
                float ticksNeeded = recipe.getTicks() * SoakingConfig.soakingDurationMultiplier;
                float progress = elapsed > ticksNeeded ? 1 : elapsed / ticksNeeded;
                float hoursRemaining = (ticksNeeded - elapsed) / TFC_Time.HOUR_LENGTH;
                return new SoakingSurfaceSlotProgress(recipe, progress, hoursRemaining);
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

        tag.setLong("freezingLastTrackedTicks", freezingLastTrackedTicks);
        tag.setLong("freezingProgressDelayTicks", freezingProgressDelayTicks);
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

        freezingLastTrackedTicks = tag.getLong("freezingLastTrackedTicks");
        freezingProgressDelayTicks = tag.getLong("freezingProgressDelayTicks");
    }

    public boolean canPlaceItem(ItemStack item) {
        return findAvailableSlot() != -1;
    }

    public boolean placeItem(ItemStack item, int preferredSlot) {
        int slot = preferredSlot != -1 && storage[preferredSlot] == null ? preferredSlot : findAvailableSlot();
        if (slot != -1) {
            storage[slot] = new SoakingSurfaceItem(item, TFC_Time.getTotalTicks());

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            return true;
        }

        return false;
    }

    private Fluid getSoakingFluid() {
        Block surfaceBlock = worldObj.getBlock(xCoord, yCoord + 1, zCoord);
        if (surfaceBlock instanceof IFluidBlock) {
            return ((IFluidBlock) surfaceBlock).getFluid();
        }

        return null;
    }

    private int findAvailableSlot() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] == null) {
                return i;
            }
        }

        return -1;
    }

    public boolean retrieveItem(int slot, EntityPlayer player) {
        ItemStack is = getSlotItemStack(slot);
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

    public void sendUpdateMessage() {
        Network.sendToTileEntity(new SimpleUpdatePacket(), this);
        Bids.LOG.debug("Sent update message");
    }

    @Override
    public void handleNetworkPacket(SimpleUpdatePacket packet) {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        Bids.LOG.debug("Client updated at [{},{},{}]", xCoord, yCoord, zCoord);
    }

}
